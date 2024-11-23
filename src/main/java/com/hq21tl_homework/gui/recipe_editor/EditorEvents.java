package com.hq21tl_homework.gui.recipe_editor;


import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.hq21tl_homework.HintTextField;

public class EditorEvents {
    private EditorEvents() {}
    public static class NameChanged implements DocumentListener{
        private final HintTextField<JTextField> base;
        private final RecipeEditor editor;

        public NameChanged(HintTextField<JTextField> base, RecipeEditor editor) {
            this.base = base;
            this.editor = editor;
        }
        
        @Override
        public void insertUpdate(DocumentEvent e) {
            editor.entryBuilder.name = base.getText();
        }
        
        @Override
        public void removeUpdate(DocumentEvent e) {
            editor.entryBuilder.name = base.getText();
        }
        
        @Override
        public void changedUpdate(DocumentEvent e) {
            // Auto-generated method stub
        }

    }
    public static class CategoryChanged implements DocumentListener{
        private final HintTextField<JTextField> base;
        private final RecipeEditor editor;
        
        public CategoryChanged(HintTextField<JTextField> base, RecipeEditor editor) {
            this.base = base;
            this.editor = editor;
        }
        
        @Override
        public void insertUpdate(DocumentEvent e) {
            editor.entryBuilder.category = base.getText();
        }
        
        @Override
        public void removeUpdate(DocumentEvent e) {
            editor.entryBuilder.category = base.getText();
        }
        
        @Override
        public void changedUpdate(DocumentEvent e) {
            // Auto-generated method stub
        }
        
    }
    public static class DescriptionChanged implements DocumentListener{
        private final HintTextField<JTextArea> base;
        private final RecipeEditor editor;
        
        public DescriptionChanged(HintTextField<JTextArea> base, RecipeEditor editor) {
            this.base = base;
            this.editor = editor;
        }
        
        @Override
        public void insertUpdate(DocumentEvent e) {
            editor.entryBuilder.description = base.getText();
        }
        
        @Override
        public void removeUpdate(DocumentEvent e) {
            editor.entryBuilder.description = base.getText();
        }
        
        @Override
        public void changedUpdate(DocumentEvent e) {
            // Auto-generated method stub
        }
    }
}