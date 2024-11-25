package com.hq21tl_homework.gui;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.hq21tl_homework.HintTextField;
import com.hq21tl_homework.error_dialog.ErrorDialog;
import com.hq21tl_homework.error_dialog.ErrorDialog.ErrorLevel;
import com.hq21tl_homework.gui.SearchBarComponents.OrderComboBox;
import com.hq21tl_homework.gui.SearchBarComponents.RadioSearchCategory;
import com.hq21tl_homework.gui.SearchBarComponents.RadioSearchName;
import com.hq21tl_homework.gui.SearchBarComponents.SortComboBox;
import com.hq21tl_homework.recipe_book.RecipeBook;
import com.hq21tl_homework.recipe_book.RecipeEntry;

public class StateContainer {
    private StateContainer() {}
    public static class RadioButtonState {

        private RadioButtonState() {}
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
            boolean state = (instance instanceof RadioSearchCategory); // false if RadioSearchName

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
        private static RecipeBookGUI recipeBookGUIInstance = null;
        private static RecipeBook recipeBookInstance = null;
        private static EntryContainer entryContainerInstance = null;
        private static JLabel entryCountInstance = new JLabel();
        private static HintTextField<JTextField> searchTerm = null;
        private static SortComboBox sortCombo = null;
        private static OrderComboBox orderCombo = null;
        private static int searchType = 0; // Name, Category, Ingridient

        public static final RadioButtonState radioButtonState = new RadioButtonState(); 
        private static String[] ingridientFilter = null;

        @SuppressWarnings("unchecked")
        public static void setInstance(Object instance){
            if (instance instanceof RecipeBook ins) {
                recipeBookInstance = ins; 
            }else if (instance instanceof RecipeBookGUI ins) {
                recipeBookGUIInstance = ins; 
            }else if (instance instanceof EntryContainer ins) {
                entryContainerInstance = ins;
            }else if (instance instanceof JLabel ins) {
                entryCountInstance = ins;
            }else if (instance instanceof HintTextField ins) { //NOSONAR // Illegal in Java
                searchTerm = ins;
            }else if (instance instanceof SortComboBox ins) {
                sortCombo = ins;
            }else if (instance instanceof OrderComboBox ins) {
                orderCombo = ins;
            }
            else {
                new ErrorDialog(
                    ErrorLevel.ERROR, 
                    "StateContainer - EntryCollectionState Error", 
                    "Error: StateContainer.EntryCollectionState has been given an unknown instance: " + (instance != null ? instance.getClass().toString() : "null"))
                .showError();
            }

            if (recipeBookInstance != null &&
                recipeBookGUIInstance != null && 
                entryContainerInstance != null && 
                entryCountInstance != null &&
                sortCombo != null &&
                orderCombo != null &&
                searchTerm != null) 
                performUpdate();
        }

        private static boolean instanceVerify(){ //NOSONAR
            if (recipeBookInstance == null ||
            recipeBookGUIInstance == null || 
            entryContainerInstance == null || 
            entryCountInstance == null ||
            sortCombo == null ||
            orderCombo == null ||
            searchTerm == null
            ){
                ErrorDialog.ErrorDialogSettings dialogSettings = new ErrorDialog.ErrorDialogSettings(
                    "StateContainer - EntryCollectionState Error", 
                    ErrorLevel.ERROR, 
                    ErrorDialog.DialogType.OK, 
                    ErrorDialog.DialogBehaviour.BLOCKING_DIALOG,
                    "Error: StateContainer.EntryCollectionState used without proper initialization!" +
                        (recipeBookInstance == null ? "\nRecipeBookInstance was null!" : "") + 
                        (recipeBookGUIInstance == null ? "\nRecipeBookGUIInstance was null!" : "") + 
                        (entryContainerInstance == null ? "\nEntryContainerInstance was null!" : "") +
                        (entryCountInstance == null ? "\nEntryCountInstance was null!" : "") +
                        (sortCombo == null ? "\nSortComboBoxInstance was null!" : "") +
                        (orderCombo == null ? "\nOrderComboBoxInstance was null!" : "") +
                        (searchTerm == null ? "\nSearchTerm was null!" : ""), 
                    null
                    );
                ErrorDialog errorDialog = new ErrorDialog(dialogSettings);
                errorDialog.showError();

                return false;
            }
            return true;
        }

        public static RecipeBookGUI getRecipeBookGUI(){
            if (!instanceVerify()) return null;
            return recipeBookGUIInstance;
        }

        public static EntryContainer getEntryContainer(){
            if (!instanceVerify()) return null;
            return entryContainerInstance;
        }

        public static void setSearchType(int type){
            searchType = type;
        }

        public static void setIngredientFilter(String[] filter){
            if (!instanceVerify()) return;
            ingridientFilter = filter;
        }

        public static RecipeBook getRecipeBookInstance(){
            if (!instanceVerify()) return null;
            return recipeBookInstance;
        }

        public static HintTextField<JTextField> getSearchTerm(){
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
                    new ErrorDialog(ErrorLevel.FATAL, "State Container", "Unknown searchType!")
                            .showError();
                    setSearchType(0);
                    performUpdate();
                }
            }

            List<RecipeEntry> orderedToDisplay = Arrays.asList(toDisplay);
            if (sortCombo.getSelectedIndex() == 0)
                Collections.sort(orderedToDisplay, (o1, o2) -> o1.getName().compareTo(o2.getName()));
            else
                Collections.sort(orderedToDisplay, (o1, o2) -> o1.getCategory().compareTo(o2.getCategory()));


            if (orderCombo.getSelectedIndex() == 1)
                Collections.reverse(orderedToDisplay);
            
            entryContainerInstance.revalidate();
            
            for (RecipeEntry recipeEntry : orderedToDisplay) {
                EntryContainer.Entry entry = new EntryContainer.Entry();
                entry.initialize(entryContainerInstance, null);
                entry.setRecipeEntry(recipeEntry);
            }
            
            entryContainerInstance.revalidate();
            entryContainerInstance.repaint();
        }
    }
}
