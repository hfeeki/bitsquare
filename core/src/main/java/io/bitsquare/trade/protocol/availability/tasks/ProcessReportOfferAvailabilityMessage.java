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

package io.bitsquare.trade.protocol.availability.tasks;

import io.bitsquare.common.taskrunner.Task;
import io.bitsquare.common.taskrunner.TaskRunner;
import io.bitsquare.offer.Offer;
import io.bitsquare.trade.protocol.availability.CheckOfferAvailabilityModel;
import io.bitsquare.trade.protocol.availability.messages.ReportOfferAvailabilityMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessReportOfferAvailabilityMessage extends Task<CheckOfferAvailabilityModel> {
    private static final Logger log = LoggerFactory.getLogger(ProcessReportOfferAvailabilityMessage.class);

    public ProcessReportOfferAvailabilityMessage(TaskRunner taskHandler, CheckOfferAvailabilityModel model) {
        super(taskHandler, model);
    }

    @Override
    protected void doRun() {
        try {
            ReportOfferAvailabilityMessage reportOfferAvailabilityMessage = (ReportOfferAvailabilityMessage) model.getMessage();

            if (model.offer.getState() != Offer.State.REMOVED) {
                if (reportOfferAvailabilityMessage.isOfferOpen)
                    model.offer.setState(Offer.State.AVAILABLE);
                else
                    model.offer.setState(Offer.State.RESERVED);
            }

            complete();
        } catch (Throwable t) {
            model.offer.setState(Offer.State.FAULT);

            failed(t);
        }
    }
}
