package com.hq21tl_homework.gui;

import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.hq21tl_homework.error_dialog.ErrorDialog;
import com.hq21tl_homework.error_dialog.ErrorDialog.ErrorLevel;
import com.hq21tl_homework.gui.SearchBarComponents.RadioSearchCategory;
import com.hq21tl_homework.gui.SearchBarComponents.RadioSearchName;
import com.hq21tl_homework.recipe_book.RecipeBook;
import com.hq21tl_homework.recipe_book.RecipeEntry;

public class StateContainer {
    private StateContainer() {}
    public static class RadioButtonState {

        private RadioButtonState() {}

        private boolean state = false; // f: name ; t: category
        private RadioSearchName nameInstance = null;
        private RadioSearchCategory categoryInstance = null;

        public void setInstance(JRadioButton instance) {
            if (instance instanceof RadioSearchName ins) {
                nameInstance = ins; 
            }else if (instance instanceof RadioSearchCategory ins) {
                categoryInstance = ins;
            }

            if (nameInstance != null && categoryInstance != null) {
                setState(nameInstance);
            }
        }

        private boolean instanceVerify(){
            if (nameInstance == null ||categoryInstance == null){
                ErrorDialog.ErrorDialogSettings dialogSettings = new ErrorDialog.ErrorDialogSettings(
                    "StateContainer - RadioButtonState fault", 
                    ErrorLevel.ERROR, 
                    ErrorDialog.DialogType.OK, 
                    ErrorDialog.DialogBehaviour.BLOCKING_DIALOG,
                    "Error: StateContainer.RadioButtonState used without proper initialization!" +
                        (nameInstance == null ? "\nNameInstance was null!" : "") + 
                        (categoryInstance == null ? "\nCategoryInstance was null!" : ""), 
                    null
                    );
                ErrorDialog errorDialog = new ErrorDialog(dialogSettings);
                errorDialog.showError();

                return false;
            }
            return true;
        }

        public void setState(JRadioButton instance) {
            if (!instanceVerify()) return;
            if (instance instanceof RadioSearchName) {
                state = false; 
            }else if (instance instanceof RadioSearchCategory) {
                state = true;
            }else{
                state = false;
            }

            nameInstance.setSelected(!state);
            categoryInstance.setSelected(state);

            EntryCollectionState.setSearchType(state ? 1 : 0);
        }
        public void discardInstance(){
            nameInstance = null;
            categoryInstance = null;
        }
    }
    public static class EntryCollectionState{
        private EntryCollectionState() {}
        private static RecipeBook recipeBookInstance = null;
        private static EntryContainer entryContainerInstance = null;
        private static JLabel entryCountInstance = new JLabel();
        private static JTextField searchTerm = null;
        private static int searchType = 0; // Name, Category, Ingridient

        public static final RadioButtonState radioButtonState = new RadioButtonState(); 
        private static String[] ingridientFilter = null;

        public static void setInstance(Object instance){
            if (instance instanceof RecipeBook ins) {
                recipeBookInstance = ins; 
            }else if (instance instanceof EntryContainer ins) {
                entryContainerInstance = ins;
            }else if (instance instanceof JLabel ins) {
                entryCountInstance = ins;
            }else if (instance instanceof JTextField ins) {
                searchTerm = ins;
            }
            else {
                new ErrorDialog(
                    ErrorLevel.ERROR, 
                    "StateContainer - EntryCollectionState Error", 
                    "Error: StateContainer.EntryCollectionState has been given an unknown instance: " + instance.getClass().toString())
                .showError();
            }

            if (recipeBookInstance != null &&
                entryContainerInstance != null && 
                entryCountInstance != null &&
                searchTerm != null) 
                performUpdate();
        }

        private static boolean instanceVerify(){
            if (recipeBookInstance == null ||
            entryContainerInstance == null || 
            entryCountInstance == null ||
            searchTerm == null
            ){
                ErrorDialog.ErrorDialogSettings dialogSettings = new ErrorDialog.ErrorDialogSettings(
                    "StateContainer - EntryCollectionState Error", 
                    ErrorLevel.ERROR, 
                    ErrorDialog.DialogType.OK, 
                    ErrorDialog.DialogBehaviour.BLOCKING_DIALOG,
                    "Error: StateContainer.EntryCollectionState used without proper initialization!" +
                        (recipeBookInstance == null ? "\nRecipeBookInstance was null!" : "") + 
                        (entryContainerInstance == null ? "\nEntryContainerInstance was null!" : "") +
                        (entryContainerInstance == null ? "\nEntryCountInstance was null!" : "") +
                        (searchTerm == null ? "\nSearchTerm was null!" : ""), 
                    null
                    );
                ErrorDialog errorDialog = new ErrorDialog(dialogSettings);
                errorDialog.showError();

                return false;
            }
            return true;
        }

        public static void setSearchType(int type){
            searchType = type;
        }

        public static void setIngredientFilter(String[] filter){
            ingridientFilter = filter;
        }

        public static RecipeBook getRecipeBookInsatnce(){
            if (!instanceVerify()) return null;
            return recipeBookInstance;
        }

        public static JTextField getSearchTerm(){
            if (!instanceVerify()) return null;
            return searchTerm;
        }

        public static void performUpdate(){
            if (!instanceVerify()) return;
            entryContainerInstance.removeAll();
            RecipeEntry[] toDisplay = new RecipeEntry[0];
            switch (searchType) {
                case 0 -> toDisplay = recipeBookInstance.filterRecipesByName(searchTerm.getText());
                case 1 -> toDisplay = recipeBookInstance.filterRecipesByCategory(searchTerm.getText());
                case 2 -> toDisplay = recipeBookInstance.filterRecipesByAvailableIngredients(Arrays.asList(ingridientFilter));
                default -> {
                    new ErrorDialog(ErrorLevel.ERROR, "State Container", "Filter by available ingridients has not been implemented yet.")
                            .showError();
                    setSearchType(0);
                    performUpdate();
                }
            }

            entryContainerInstance.revalidate();
            
            for (RecipeEntry recipeEntry : toDisplay) {
                EntryContainer.Entry entry = new EntryContainer.Entry();
                entry.initialize(entryContainerInstance, null);
                entry.setRecipeEntry(recipeEntry);
            }
            
            entryContainerInstance.revalidate();
            entryContainerInstance.repaint();
        }
    }
}
