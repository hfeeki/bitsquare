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

package io.bitsquare.trade.protocol.trade.offerer.tasks;

import io.bitsquare.btc.FeePolicy;
import io.bitsquare.btc.TradeWalletService;
import io.bitsquare.common.taskrunner.Task;
import io.bitsquare.common.taskrunner.TaskRunner;
import io.bitsquare.trade.protocol.trade.offerer.models.BuyerAsOffererModel;

import org.bitcoinj.core.Coin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateOffererDepositTxInputs extends Task<BuyerAsOffererModel> {
    private static final Logger log = LoggerFactory.getLogger(CreateOffererDepositTxInputs.class);

    public CreateOffererDepositTxInputs(TaskRunner taskHandler, BuyerAsOffererModel model) {
        super(taskHandler, model);
    }

    @Override
    protected void doRun() {
        try {
            Coin offererInputAmount = model.trade.getSecurityDeposit().add(FeePolicy.TX_FEE);
            TradeWalletService.Result result = model.tradeWalletService.createOffererDepositTxInputs(offererInputAmount,
                    model.offerer.addressEntry);

            model.offerer.connectedOutputsForAllInputs = result.getConnectedOutputsForAllInputs();
            model.offerer.outputs = result.getOutputs();

            complete();
        } catch (Throwable e) {
            failed(e);
        }
    }

    @Override
    protected void updateStateOnFault() {
    }
}
