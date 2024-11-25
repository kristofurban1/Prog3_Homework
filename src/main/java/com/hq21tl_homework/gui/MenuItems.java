package com.hq21tl_homework.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.swing.JFileChooser;

import com.hq21tl_homework.Locales;
import com.hq21tl_homework.Main;
import com.hq21tl_homework.error_dialog.ErrorDialog;
import com.hq21tl_homework.error_dialog.ErrorDialog.DialogBehaviour;
import com.hq21tl_homework.error_dialog.ErrorDialog.DialogResult;
import com.hq21tl_homework.error_dialog.ErrorDialog.DialogType;
import com.hq21tl_homework.error_dialog.ErrorDialog.ErrorDialogSettings;
import com.hq21tl_homework.error_dialog.ErrorDialog.ErrorLevel;
import com.hq21tl_homework.gui.lang_selector.LangSelector;
import com.hq21tl_homework.gui.recipe_editor.RecipeEditor;
import com.hq21tl_homework.recipe_book.Recipe;
import com.hq21tl_homework.recipe_book.Recipe.RecipeBuilder;
import com.hq21tl_homework.recipe_book.RecipeBook;
import com.hq21tl_homework.recipe_book.RecipeEntry;
import com.hq21tl_homework.recipe_book.RecipeEntry.RecipeEntryBuilder;
import com.hq21tl_homework.recipe_book.RecipeEntryXMLHander;

public class MenuItems {

    private MenuItems() {
    }

    public static class AddRecipe implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            RecipeEditor editor = new RecipeEditor(null);
            RecipeEntry entry = editor.showGUI();
            if (entry == null) return; // cancelled
            if (entry.getName().isBlank()) return;
            
            boolean success = StateContainer.EntryCollectionState.getRecipeBookInstance().addRecipe(entry);
            if (success) {
                RecipeBook.exportRecipeBook(StateContainer.EntryCollectionState.getRecipeBookInstance(), Main.PERSISTENT_PATH); 
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
                        .getRecipeBookInstance()
                        .updateRecipe(entry);
            }
            
            StateContainer.EntryCollectionState.performUpdate();
            RecipeBook.exportRecipeBook(StateContainer.EntryCollectionState.getRecipeBookInstance(), Main.PERSISTENT_PATH); 
        }
    }

    public static class SetLanguage implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            @SuppressWarnings("unused")
            LangSelector unused = new LangSelector();
        }
    }


    public static class Exit implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            RecipeBookGUI instance = StateContainer.EntryCollectionState.getRecipeBookGUI();
            if (instance == null) return;
            RecipeBook.exportRecipeBook(StateContainer.EntryCollectionState.getRecipeBookInstance(), Main.PERSISTENT_PATH); 
            instance.dispose();
        }
    }

    /*
    * Code from https://www.baeldung.com/java-compress-and-uncompress
    * Not intended to plagerize, just have no idea how this flow works.
    * More direct copy than i wanted. This is a fixed flow of operations. I cannot write it otherwise.
    */

    public static class Import implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser(".");
            fileChooser.showSaveDialog((Component)e.getSource());
            File fileZip = fileChooser.getSelectedFile();

            List<RecipeEntry> readEntries = new ArrayList<>();

            try {
                byte[] buffer = new byte[1024];
                ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
                ZipEntry zipEntry = zis.getNextEntry();
                while (zipEntry != null) {
                    File newFile = newFile(new File("."), zipEntry);
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();

                    readEntries.add(RecipeEntryXMLHander.importRecipe(newFile));
                    newFile.delete();

                    zipEntry = zis.getNextEntry();
                }

                zis.closeEntry();
                zis.close();
            } catch (IOException ex) {
                ErrorDialog.ErrorDialogSettings settings = new ErrorDialogSettings(
                    "Failed reading ZIP", 
                    ErrorLevel.FATAL, 
                    DialogType.OK,
                    DialogBehaviour.BLOCKING_DIALOG, 
                    ex.getMessage(), 
                    Arrays.toString(ex.getStackTrace()));
                ErrorDialog dialog = new ErrorDialog(settings);
                dialog.showError();
                System.exit(1);
            }

            RecipeBook recipeBook = StateContainer.EntryCollectionState.getRecipeBookInstance();
            for (RecipeEntry recipeEntry : readEntries) {
                if (recipeBook.addRecipe(recipeEntry)) continue; // Add successful

                //On failure user can merge, rename
                ErrorDialog.ErrorDialogSettings settings = new ErrorDialogSettings(
                    Locales.getString("Import_Collision"), 
                    ErrorLevel.INFO, 
                    DialogType.YES_NO_CANCEL,
                    DialogBehaviour.BLOCKING_DIALOG, 
                    String.format(Locales.getString("Import_Collision_Message"), recipeEntry.getName()), 
                    null);
                ErrorDialog dialog = new ErrorDialog(settings);
                DialogResult result = dialog.showError();

                switch (result) {
                    case CANCEL:
                        break;
                    case NO:
                        RecipeEditor editor = new RecipeEditor(recipeEntry);
                        RecipeEntry updated = editor.showGUI();
                        if (updated == null) continue;
                        recipeBook.addRecipe(updated);
                        break;
                    case YES:
                        // Get recipe we collided with
                        RecipeEntryBuilder recipeEntryBuilder = new RecipeEntryBuilder(recipeBook.getRecipe(recipeEntry.getName()));
                        for (Recipe recipe : recipeEntry.getRecipes()){
                            recipeEntryBuilder.recipes.add(new RecipeBuilder(recipe));
                        }
                        recipeBook.updateRecipe(recipeEntryBuilder.build());
                        break;
                }
            }

            StateContainer.EntryCollectionState.performUpdate();
            
        }
    }

    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());
    
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();
    
        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
    
        return destFile;
    }

    public static class Export implements ActionListener {

        @Override
        @SuppressWarnings("ConvertToTryWithResources")
        public void actionPerformed(ActionEvent e) {
            RecipeEntry[] entries = StateContainer.EntryCollectionState.getEntryContainer().getSelectedRecipes();

            if (entries.length == 0){
                new ErrorDialog(
                    ErrorLevel.INFO, 
                    Locales.getString("Export_NoSelection_Title"),
                    Locales.getString("Export_NoSelection_Message")).showError();
                return;
            }

            JFileChooser fileChooser = new JFileChooser(".");
            fileChooser.setSelectedFile(new File("export.zip"));
            fileChooser.showSaveDialog((Component)e.getSource());

            File selected = fileChooser.getSelectedFile();
            if(selected.exists()){
                ErrorDialog.ErrorDialogSettings settings = new ErrorDialogSettings(
                    Locales.getString("Export_FileExists_Title"), 
                    ErrorLevel.WARNING, 
                    DialogType.YES_NO_CANCEL,
                    DialogBehaviour.BLOCKING_DIALOG, 
                    Locales.getString("Export_FileExists_Message"), 
                    null);
                ErrorDialog dialog = new ErrorDialog(settings);
                DialogResult result = dialog.showError();
                switch (result) {
                    case CANCEL:
                        return;
                    case NO:
                        actionPerformed(e);
                        return;
                    default: break; // Do nothing
                }
            }

            try {
                final FileOutputStream fos = new FileOutputStream(selected);
                ZipOutputStream zipOut = new ZipOutputStream(fos);

                for (RecipeEntry recipeEntry : entries) {
                    File toZip = RecipeEntryXMLHander.exportRecipe(recipeEntry);
                    FileInputStream fis = new FileInputStream(toZip);
                    ZipEntry zipEntry = new ZipEntry(toZip.getName());
                    zipOut.putNextEntry(zipEntry);

                    byte[] bytes = new byte[1024];
                    int length;
                    while((length = fis.read(bytes)) >= 0) {
                        zipOut.write(bytes, 0, length);
                    }
                    fis.close();
                    toZip.delete();
                }

                zipOut.close();
                fos.close();
            } 
            catch (IOException ex) {
                ErrorDialog.ErrorDialogSettings settings = new ErrorDialogSettings(
                    "Failed writing ZIP", 
                    ErrorLevel.FATAL, 
                    DialogType.OK,
                    DialogBehaviour.BLOCKING_DIALOG, 
                    Locales.getString(ex.getMessage()), 
                    Arrays.toString(ex.getStackTrace()));
                ErrorDialog dialog = new ErrorDialog(settings);
                dialog.showError();
                System.exit(1);
            }
        }
    }
}
