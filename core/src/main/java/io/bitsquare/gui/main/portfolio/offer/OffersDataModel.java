/*
 * This file is part of Bitsquare.
 *
 * Bitsquare is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bitsquare is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bitsquare. If not, see <http://www.gnu.org/licenses/>.
 */

package io.bitsquare.gui.main.portfolio.offer;

import io.bitsquare.common.handlers.ErrorMessageHandler;
import io.bitsquare.common.handlers.ResultHandler;
import io.bitsquare.common.viewfx.model.Activatable;
import io.bitsquare.common.viewfx.model.DataModel;
import io.bitsquare.offer.Direction;
import io.bitsquare.offer.Offer;
import io.bitsquare.trade.TradeManager;
import io.bitsquare.user.User;

import com.google.inject.Inject;

import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class OffersDataModel implements Activatable, DataModel {
    private static final Logger log = LoggerFactory.getLogger(OffersDataModel.class);

    private final TradeManager tradeManager;
    private final User user;

    private final ObservableList<OfferListItem> list = FXCollections.observableArrayList();
    private final MapChangeListener<String, Offer> offerMapChangeListener;


    @Inject
    public OffersDataModel(TradeManager tradeManager, User user) {
        this.tradeManager = tradeManager;
        this.user = user;

        this.offerMapChangeListener = change -> {
            if (change.wasAdded())
                list.add(new OfferListItem(change.getValueAdded()));
            else if (change.wasRemoved())
                list.removeIf(e -> e.getOffer().getId().equals(change.getValueRemoved().getId()));
        };
    }

    @Override
    public void activate() {
        list.clear();
        list.addAll(tradeManager.getOpenOffers().values().stream().map(OfferListItem::new).collect(Collectors.toList()));

        // we sort by date, earliest first
        list.sort((o1, o2) -> o2.getOffer().getCreationDate().compareTo(o1.getOffer().getCreationDate()));

        tradeManager.getOpenOffers().addListener(offerMapChangeListener);
    }

    @Override
    public void deactivate() {
        tradeManager.getOpenOffers().removeListener(offerMapChangeListener);
    }

    void removeOpenOffer(Offer offer, ResultHandler resultHandler, ErrorMessageHandler errorMessageHandler) {
        tradeManager.removeOpenOffer(offer, resultHandler, errorMessageHandler);
    }


    public ObservableList<OfferListItem> getList() {
        return list;
    }

    public Direction getDirection(Offer offer) {
        return offer.getMessagePublicKey().equals(user.getMessagePubKey()) ?
                offer.getDirection() : offer.getMirroredDirection();
    }
}
