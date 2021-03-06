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

package io.bitsquare.trade.protocol.trade.messages;

import io.bitsquare.fiat.FiatAccount;

import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionOutput;

import java.io.Serializable;

import java.security.PublicKey;

import java.util.List;

public class RequestOffererPublishDepositTxMessage extends TradeMessage implements Serializable {
    private static final long serialVersionUID = 2179683654379803071L;

    public final FiatAccount takerFiatAccount;
    public final String takerAccountId;
    public final PublicKey takerMessagePublicKey;
    public final String takerContractAsJson;
    public final String takerContractSignature;
    public final String takerPayoutAddressString;
    public final Transaction takersPreparedDepositTx;
    public final List<TransactionOutput> takerConnectedOutputsForAllInputs;
    public final List<TransactionOutput> takerOutputs;

    public RequestOffererPublishDepositTxMessage(String tradeId,
                                                 FiatAccount takerFiatAccount,
                                                 String takerAccountId,
                                                 PublicKey takerMessagePublicKey,
                                                 String takerContractAsJson,
                                                 String takerContractSignature,
                                                 String takerPayoutAddressString,
                                                 Transaction takersPreparedDepositTx,
                                                 List<TransactionOutput> takerConnectedOutputsForAllInputs,
                                                 List<TransactionOutput> takerOutputs) {
        this.tradeId = tradeId;
        this.takerFiatAccount = takerFiatAccount;
        this.takerAccountId = takerAccountId;
        this.takerMessagePublicKey = takerMessagePublicKey;
        this.takerContractAsJson = takerContractAsJson;
        this.takerContractSignature = takerContractSignature;
        this.takerPayoutAddressString = takerPayoutAddressString;
        this.takersPreparedDepositTx = takersPreparedDepositTx;
        this.takerConnectedOutputsForAllInputs = takerConnectedOutputsForAllInputs;
        this.takerOutputs = takerOutputs;
    }
}
