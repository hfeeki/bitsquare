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

package io.bitsquare.trade;

import io.bitsquare.offer.Offer;

import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.utils.Fiat;

import java.io.Serializable;

import java.util.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Trade implements Serializable {
    private static final long serialVersionUID = -8275323072940974077L;


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Enum
    ///////////////////////////////////////////////////////////////////////////////////////////

    public static enum State {
        OPEN,
        TAKE_OFFER_FEE_PUBLISH_FAILED,
        TAKE_OFFER_FEE_TX_CREATED,
        DEPOSIT_PUBLISHED,
        TAKE_OFFER_FEE_PUBLISHED,
        DEPOSIT_CONFIRMED,
        FIAT_PAYMENT_STARTED,
        FIAT_PAYMENT_RECEIVED,
        PAYOUT_PUBLISHED,
        MESSAGE_SENDING_FAILED,
        FAULT;

        private String errorMessage;

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    private final Offer offer;
    private final Date date;
    private State state;
    private Coin tradeAmount;
    private Contract contract;
    private String contractAsJson;
    private String takerContractSignature;
    private String offererContractSignature;
    private Transaction depositTx;
    private Transaction payoutTx;

    // For changing values we use properties to get binding support in the UI (table)
    // When serialized those transient properties are not instantiated, so we instantiate them in the getters at first
    // access. Only use the accessor not the private field.
    transient private ObjectProperty<Coin> _tradeAmount;
    transient private ObjectProperty<Fiat> _tradeVolume;
    transient private ObjectProperty<State> _state;


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Constructor
    ///////////////////////////////////////////////////////////////////////////////////////////

    public Trade(Offer offer) {
        this.offer = offer;
        date = new Date();

        setState(State.OPEN);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Setters
    ///////////////////////////////////////////////////////////////////////////////////////////

    public void setTakerContractSignature(String takerSignature) {
        this.takerContractSignature = takerSignature;
    }

    public void setOffererContractSignature(String offererContractSignature) {
        this.offererContractSignature = offererContractSignature;
    }

    public Coin getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(Coin tradeAmount) {
        this.tradeAmount = tradeAmount;
        tradeAmountProperty().set(tradeAmount);
        tradeVolumeProperty().set(getTradeVolume());
    }

    public Contract getContract() {
        return contract;
    }

    public void setContractAsJson(String contractAsJson) {
        this.contractAsJson = contractAsJson;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public void setDepositTx(Transaction tx) {
        this.depositTx = tx;
    }

    public void setPayoutTx(Transaction tx) {
        this.payoutTx = tx;
    }

    public void setState(State state) {
        this.state = state;
        stateProperty().set(state);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Getters
    ///////////////////////////////////////////////////////////////////////////////////////////

    public Fiat getTradeVolume() {
        return offer.getVolumeByAmount(tradeAmount);
    }

    public String getTakerContractSignature() {
        return takerContractSignature;
    }

    public String getOffererContractSignature() {
        return offererContractSignature;
    }

    public Transaction getDepositTx() {
        return depositTx;
    }

    public Transaction getPayoutTx() {
        return payoutTx;
    }

    public State getState() {
        return state;
    }

    public Coin getSecurityDeposit() {
        return offer.getSecurityDeposit();
    }

    public String getId() {
        return offer.getId();
    }

    public Offer getOffer() {
        return offer;
    }

    public String getContractAsJson() {
        return contractAsJson;
    }

    public Date getDate() {
        return date;
    }

    // When serialized those transient properties are not instantiated, so we need to instantiate them at first access
    public ObjectProperty<Coin> tradeAmountProperty() {
        if (_tradeAmount == null)
            _tradeAmount = new SimpleObjectProperty<>(tradeAmount);

        return _tradeAmount;
    }

    public ObjectProperty<Fiat> tradeVolumeProperty() {
        if (_tradeVolume == null)
            _tradeVolume = new SimpleObjectProperty<>(getTradeVolume());

        return _tradeVolume;
    }

    public ObjectProperty<State> stateProperty() {
        if (_state == null)
            _state = new SimpleObjectProperty<>(state);

        return _state;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "offer=" + offer +
                ", date=" + date +
                ", state=" + state +
                ", tradeAmount=" + tradeAmount +
                ", contract=" + contract +
                ", contractAsJson='" + contractAsJson + '\'' +
                ", takerContractSignature='" + takerContractSignature + '\'' +
                ", offererContractSignature='" + offererContractSignature + '\'' +
                ", depositTx=" + depositTx +
                ", payoutTx=" + payoutTx +
                '}';
    }
}
