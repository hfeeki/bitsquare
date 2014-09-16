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

package io.bitsquare.gui.main.trade.orderbook;

import io.bitsquare.gui.PresentationModel;
import io.bitsquare.gui.main.trade.OrderBookInfo;
import io.bitsquare.gui.util.BSFormatter;
import io.bitsquare.gui.util.validation.InputValidator;
import io.bitsquare.gui.util.validation.OptionalBtcValidator;
import io.bitsquare.gui.util.validation.OptionalFiatValidator;
import io.bitsquare.locale.BSResources;
import io.bitsquare.locale.Country;
import io.bitsquare.trade.Direction;
import io.bitsquare.trade.Offer;

import com.google.inject.Inject;

import java.util.Comparator;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.transformation.SortedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.bitsquare.gui.util.BSFormatter.*;

public class OrderBookPM extends PresentationModel<OrderBookModel> {
    private static final Logger log = LoggerFactory.getLogger(OrderBookPM.class);

    private final OptionalBtcValidator optionalBtcValidator;
    private final OptionalFiatValidator optionalFiatValidator;

    final StringProperty amount = new SimpleStringProperty();
    final StringProperty price = new SimpleStringProperty();
    final StringProperty volume = new SimpleStringProperty();
    final StringProperty btcCode = new SimpleStringProperty();
    final StringProperty fiatCode = new SimpleStringProperty();
    final ObjectProperty<Country> bankAccountCountry = new SimpleObjectProperty<>();
    final ObjectProperty<Comparator<OrderBookListItem>> comparator = new SimpleObjectProperty<>();
    final StringProperty restrictionsInfo = new SimpleStringProperty();


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Constructor
    ///////////////////////////////////////////////////////////////////////////////////////////

    @Inject
    public OrderBookPM(OrderBookModel model,
                       OptionalFiatValidator optionalFiatValidator,
                       OptionalBtcValidator optionalBtcValidator) {
        super(model);

        this.optionalFiatValidator = optionalFiatValidator;
        this.optionalBtcValidator = optionalBtcValidator;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Lifecycle
    ///////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void initialize() {
        super.initialize();

        btcCode.bind(model.btcCode);
        fiatCode.bind(model.fiatCode);
        bankAccountCountry.bind(model.bankAccountCountry);
        restrictionsInfo.bind(model.restrictionsInfo);
        comparator.bind(model.comparator);

        // Bidirectional bindings are used for all input fields: amount, price and volume 
        // We do volume/amount calculation during input, so user has immediate feedback
        amount.addListener((ov, oldValue, newValue) -> {
            if (isBtcInputValid(newValue).isValid) {
                setAmountToModel();
                setPriceToModel();
                model.calculateVolume();
            }
        });

        price.addListener((ov, oldValue, newValue) -> {
            if (isFiatInputValid(newValue).isValid) {
                setAmountToModel();
                setPriceToModel();
                model.calculateVolume();
            }
        });

        volume.addListener((ov, oldValue, newValue) -> {
            if (isFiatInputValid(newValue).isValid) {
                setPriceToModel();
                setVolumeToModel();
                model.calculateAmount();
            }
        });

        // Binding with Bindings.createObjectBinding does not work because of bi-directional binding
        model.amountAsCoinProperty().addListener((ov, oldValue, newValue) -> amount.set(formatCoin(newValue)));
        model.priceAsFiatProperty().addListener((ov, oldValue, newValue) -> price.set(formatFiat(newValue)));
        model.volumeAsFiatProperty().addListener((ov, oldValue, newValue) -> volume.set(formatFiat(newValue)));
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    public void activate() {
        super.activate();
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    public void deactivate() {
        super.deactivate();
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    public void terminate() {
        super.terminate();
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Public methods
    ///////////////////////////////////////////////////////////////////////////////////////////

    void setOrderBookInfo(OrderBookInfo orderBookInfo) {
        model.setOrderBookInfo(orderBookInfo);
    }

    void removeOffer(Offer offer) {
        model.removeOffer(offer);
    }

    boolean isTradable(Offer offer) {
        return model.isTradable(offer);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Getters
    ///////////////////////////////////////////////////////////////////////////////////////////

    SortedList<OrderBookListItem> getOfferList() {
        return model.getOfferList();
    }

    StringProperty getAmount() {
        return amount;
    }

    StringProperty getPrice() {
        return price;
    }

    StringProperty getVolume() {
        return volume;
    }

    boolean isRegistered() {
        return model.isRegistered();
    }

    boolean isMyOffer(Offer offer) {
        return model.isMyOffer(offer);
    }

    String getAmount(OrderBookListItem item) {
        return (item != null) ? BSFormatter.formatCoin(item.getOffer().getAmount()) : "";
    }

    String getPrice(OrderBookListItem item) {
        return (item != null) ? BSFormatter.formatFiat(item.getOffer().getPrice()) : "";
    }

    String getVolume(OrderBookListItem item) {
        return (item != null) ? BSFormatter.formatFiat(item.getOffer().getOfferVolume()) : "";
    }

    String getBankAccountType(OrderBookListItem item) {
        return (item != null) ? BSResources.get(item.getOffer().getBankAccountType().toString()) : "";
    }

    String getDirectionLabel(Offer offer) {
        // mirror direction
        Direction direction = offer.getDirection() == Direction.BUY ? Direction.SELL : Direction.BUY;
        return BSFormatter.formatDirection(direction, true);
    }

    OrderBookInfo getOrderBookInfo() {
        return model.getOrderBookInfo();
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Private methods
    ///////////////////////////////////////////////////////////////////////////////////////////

    private InputValidator.ValidationResult isBtcInputValid(String input) {
        return optionalBtcValidator.validate(input);
    }

    private InputValidator.ValidationResult isFiatInputValid(String input) {
        return optionalFiatValidator.validate(input);
    }

    private void setAmountToModel() {
        model.setAmount(parseToCoinWith4Decimals(amount.get()));
    }

    private void setPriceToModel() {
        model.setPrice(parseToFiatWith2Decimals(price.get()));
    }

    private void setVolumeToModel() {
        model.setVolume(parseToFiatWith2Decimals(volume.get()));
    }


}