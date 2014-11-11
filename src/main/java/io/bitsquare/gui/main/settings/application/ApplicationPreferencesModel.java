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

package io.bitsquare.gui.main.settings.application;

import io.bitsquare.gui.UIModel;

import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ApplicationPreferencesModel extends UIModel {
    private static final Logger log = LoggerFactory.getLogger(ApplicationPreferencesModel.class);


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Constructor
    ///////////////////////////////////////////////////////////////////////////////////////////

    @Inject
    ApplicationPreferencesModel() {
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Lifecycle
    ///////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void initialize() {

        super.initialize();
    }

    @Override
    public void activate() {
        super.activate();

    }

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
    // Methods
    ///////////////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Getters
    ///////////////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Private
    ///////////////////////////////////////////////////////////////////////////////////////////

}
