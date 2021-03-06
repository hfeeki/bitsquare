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

package io.bitsquare.gui.main.account.content.changepassword;

import io.bitsquare.common.viewfx.view.FxmlView;
import io.bitsquare.common.viewfx.view.InitializableView;
import io.bitsquare.common.viewfx.view.Wizard;
import io.bitsquare.gui.main.help.Help;
import io.bitsquare.gui.main.help.HelpId;

import javax.inject.Inject;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;

@FxmlView
public class ChangePasswordView extends InitializableView<GridPane, ChangePasswordViewModel> implements Wizard.Step {

    @FXML HBox buttonsHBox;
    @FXML Button saveButton, skipButton;
    @FXML PasswordField oldPasswordField, passwordField, repeatedPasswordField;

    private Wizard wizard;

    @Inject
    private ChangePasswordView(ChangePasswordViewModel model) {
        super(model);
    }

    @Override
    public void initialize() {
        passwordField.textProperty().bindBidirectional(model.passwordField);
        repeatedPasswordField.textProperty().bindBidirectional(model.repeatedPasswordField);

        saveButton.disableProperty().bind(model.saveButtonDisabled);
    }

    @Override
    public void setWizard(Wizard wizard) {
        this.wizard = wizard;
    }

    @Override
    public void hideWizardNavigation() {
        buttonsHBox.getChildren().remove(skipButton);
    }

    @FXML
    private void onSaved() {
        if (wizard != null && model.requestSavePassword())
            wizard.nextStep(this);
        else
            log.debug(model.getErrorMessage()); // TODO use validating TF
    }

    @FXML
    private void onOpenHelp() {
        Help.openWindow(HelpId.SETUP_PASSWORD);
    }

    @FXML
    private void onSkipped() {
        if (wizard != null)
            wizard.nextStep(this);
    }
}

