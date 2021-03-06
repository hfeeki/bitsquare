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

import io.bitsquare.common.taskrunner.Task;
import io.bitsquare.common.taskrunner.TaskRunner;
import io.bitsquare.p2p.listener.SendMessageListener;
import io.bitsquare.trade.protocol.trade.messages.RequestDepositPaymentMessage;
import io.bitsquare.trade.protocol.trade.offerer.models.BuyerAsOffererModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestDepositPayment extends Task<BuyerAsOffererModel> {
    private static final Logger log = LoggerFactory.getLogger(RequestDepositPayment.class);

    public RequestDepositPayment(TaskRunner taskHandler, BuyerAsOffererModel model) {
        super(taskHandler, model);
    }

    @Override
    protected void doRun() {
        RequestDepositPaymentMessage tradeMessage = new RequestDepositPaymentMessage(
                model.id,
                model.offerer.connectedOutputsForAllInputs,
                model.offerer.outputs,
                model.offerer.pubKey,
                model.offerer.fiatAccount,
                model.offerer.accountId);

        model.messageService.sendMessage(model.taker.peer, tradeMessage, new SendMessageListener() {
            @Override
            public void handleResult() {
                log.trace("RequestTakerDepositPaymentMessage successfully arrived at peer");
                complete();
            }

            @Override
            public void handleFault() {
                failed();
            }
        });
    }

    @Override
    protected void updateStateOnFault() {
    }
}
