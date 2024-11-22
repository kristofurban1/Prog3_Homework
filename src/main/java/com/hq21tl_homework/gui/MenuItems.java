package com.hq21tl_homework.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.hq21tl_homework.error_dialog.ErrorDialog;
import com.hq21tl_homework.error_dialog.ErrorDialog.DialogBehaviour;
import com.hq21tl_homework.error_dialog.ErrorDialog.DialogResult;
import com.hq21tl_homework.error_dialog.ErrorDialog.DialogType;
import com.hq21tl_homework.error_dialog.ErrorDialog.ErrorLevel;
import com.hq21tl_homework.gui.recipe_editor.RecipeEditor;
import com.hq21tl_homework.recipe_book.RecipeEntry;

public class MenuItems {

    private MenuItems() {
    }

    public static class AddRecipe implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            RecipeEditor editor = new RecipeEditor(null);
            RecipeEntry entry = editor.showGUI();
            if (entry == null) return; // cancelled
            boolean success = StateContainer.EntryCollectionState.getRecipeBookInsatnce().addRecipe(entry);
            if (success) {
                StateContainer.EntryCollectionState.performUpdate();
                return;
            }

            ErrorDialog.ErrorDialogSettings settings = new ErrorDialog.ErrorDialogSettings(
                    "Recipe already exists!",
                    ErrorLevel.INFO,
                    DialogType.YES_NO,
                    DialogBehaviour.BLOCKING_DIALOG,
                    String.format("This recipe {%s} already exists!%nDo you want to override it?", entry.getName()),
                    null
            );

            ErrorDialog dialog = new ErrorDialog(settings);
            ErrorDialog.DialogResult result = dialog.showError();

            if (result == DialogResult.YES) {
                StateContainer.EntryCollectionState
                        .getRecipeBookInsatnce()
                        .updateRecipe(entry);
            }
            
            StateContainer.EntryCollectionState.performUpdate();

        }
    }
}
