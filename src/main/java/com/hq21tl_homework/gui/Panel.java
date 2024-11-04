package com.hq21tl_homework.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.hq21tl_homework.Locales;
import com.hq21tl_homework.Locales.LocalizationChangeListener;
import com.hq21tl_homework.gui.SearchBarComponents.ClearBtn;
import com.hq21tl_homework.gui.SearchBarComponents.IngridientSearchBtn;
import com.hq21tl_homework.gui.SearchBarComponents.SearchBtn;
import com.hq21tl_homework.gui.SearchBarComponents.SearchField;
import com.hq21tl_homework.gui.SearchBarComponents.SortAndSearchTypePanel;
import com.hq21tl_homework.gui.recipe_editor.MenuItems;
import com.hq21tl_homework.guiInitializable;

public class Panel {
    private Panel() {}

    public static class Menu extends JMenuBar implements guiInitializable<RecipeBookGUI>, LocalizationChangeListener{
        private final JMenu fileMenu = new JMenu();
        private final JMenu recipeMenu = new JMenu();
        
        private final JMenuItem menuImport = new JMenuItem();
        private final JMenuItem menuExport = new JMenuItem();
        private final JMenuItem menuPreferences = new JMenuItem();
        private final JMenuItem menuExit = new JMenuItem();
        
        private final JMenuItem menuAddRecipeEntry = new JMenuItem();
        //private final JMenuItem menuIngridientCollection = new JMenuItem();

        @Override
        public void initialize(JComponent parent, RecipeBookGUI root) {
            
            localizationChanged();
            fileMenu.add(menuImport);
            fileMenu.add(menuExport);
            fileMenu.add(menuPreferences);
            fileMenu.add(menuExit);

            recipeMenu.add(menuAddRecipeEntry);
            //recipeMenu.add(menuIngridientCollection);

            add(fileMenu);
            add(recipeMenu);

            menuAddRecipeEntry.addActionListener(new MenuItems.AddRecipe());

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 1;
            constraints.weighty = 0.02;
            
            parent.add(this, constraints);
        }

        @Override
        public void localizationChanged() {
            fileMenu.setText(Locales.getString("Menu_File"));

            menuImport.setText(Locales.getString("Menu_File_Import"));
            menuExport.setText(Locales.getString("Menu_File_Export"));
            menuPreferences.setText(Locales.getString("Menu_File_Preferences"));
            menuExit.setText(Locales.getString("Menu_File_Exit"));
            
            recipeMenu.setText(Locales.getString("Menu_Recipe"));
            
            menuAddRecipeEntry.setText(Locales.getString("Menu_Recipe_AddRecipeEntry"));
            //menuIngridientCollection.setText(Locales.getString("Menu_Recipe_IngridientCollection"));

        }
    }

    public static class SearchBar extends JPanel implements guiInitializable<RecipeBookGUI>{
        private final SearchField searchField = new SearchField();
        private final SortAndSearchTypePanel sastPanel = new SortAndSearchTypePanel();
        private final SearchBtn searchBtn = new SearchBtn();
        private final ClearBtn clearBtn = new ClearBtn();
        private final IngridientSearchBtn ingridientSearch = new IngridientSearchBtn();
        @Override
        public void initialize(JComponent parent, RecipeBookGUI root) {
            setLayout(new GridBagLayout());

            searchField.initialize(this, root);
            sastPanel.initialize(this, root);
            searchBtn.initialize(this, root);
            clearBtn.initialize(this, root);
            ingridientSearch.initialize(this, root);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.weightx = 1;
            constraints.weighty = 0.01;
            
            parent.add(this, constraints);
        }
    }

    public static class CenterPanel extends JPanel implements guiInitializable<RecipeBookGUI>{
        private final EntryContainer entryContainer = new EntryContainer();
        @Override
        public void initialize(JComponent parent, RecipeBookGUI root) {
            setLayout(new BorderLayout());
            setBackground(Color.BLUE);
            entryContainer.initialize(this, root);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridx = 0;
            constraints.gridy = 2;
            constraints.weightx = 1;
            constraints.weighty = 1;
            
            parent.add(this, constraints);
        }
    }

    public static class StatusPanel extends JPanel implements guiInitializable<RecipeBookGUI>{
        private final JLabel entryCountLabel = new JLabel();
        @Override
        public void initialize(JComponent parent, RecipeBookGUI root) {

            entryCountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            StateContainer.EntryCollectionState.setInstance(entryCountLabel);
            add(entryCountLabel);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridx = 0;
            constraints.gridy = 3;
            constraints.weightx = 1;
            constraints.weighty = 0.05;
            
            parent.add(this, constraints);
        }
    }


    public static class WindowPanel extends JPanel implements guiInitializable<RecipeBookGUI>{
        private final Menu menu = new Menu();
        private final SearchBar searchBar = new SearchBar();
        private final CenterPanel centerPanel = new CenterPanel();
        private final StatusPanel statusPanel = new StatusPanel();

        @Override
        public void initialize(JComponent parent, RecipeBookGUI root) {
            setLayout(new GridBagLayout());

            menu.initialize(this, root);
            searchBar.initialize(this, root);
            centerPanel.initialize(this, root);
            statusPanel.initialize(this, root);

            root.add(this, BorderLayout.CENTER);
        }
    }
}
