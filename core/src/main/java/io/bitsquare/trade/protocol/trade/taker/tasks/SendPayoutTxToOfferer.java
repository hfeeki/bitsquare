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

package io.bitsquare.trade.protocol.trade.taker.tasks;

import io.bitsquare.common.taskrunner.Task;
import io.bitsquare.common.taskrunner.TaskRunner;
import io.bitsquare.p2p.listener.SendMessageListener;
import io.bitsquare.trade.protocol.trade.messages.PayoutTxPublishedMessage;
import io.bitsquare.trade.protocol.trade.taker.models.SellerAsTakerModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendPayoutTxToOfferer extends Task<SellerAsTakerModel> {
    private static final Logger log = LoggerFactory.getLogger(SendPayoutTxToOfferer.class);

    public SendPayoutTxToOfferer(TaskRunner taskHandler, SellerAsTakerModel model) {
        super(taskHandler, model);
    }

    @Override
    protected void doRun() {
        PayoutTxPublishedMessage tradeMessage = new PayoutTxPublishedMessage(model.id, model.getPayoutTx());
        model.messageService.sendMessage(model.offerer.peer, tradeMessage, new SendMessageListener() {
            @Override
            public void handleResult() {
                log.trace("PayoutTxPublishedMessage successfully arrived at peer");
                complete();
            }

            @Override
            public void handleFault() {
                failed("Sending PayoutTxPublishedMessage failed.");
            }
        });
    }
}
