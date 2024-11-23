package com.hq21tl_homework;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

public class HintTextField<T extends JTextComponent> implements FocusListener, DocumentListener{
    public final T base;
    private String hint = "<Hint>";
    private boolean isShowing = true;

    public boolean isShowingHint(){
        return isShowing;
    }

    
    public HintTextField(String hint, T component) {
        this.base = component;
        this.hint = hint;
    }
    
    public void initialize(){
        base.setText(hint);
        base.addFocusListener(this);
    }
    
    public void setHint(String hint) {
        this.hint = hint;
        focusLost(null);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (isShowing){
            base.setText("");
            isShowing = false;
            base.setEditable(true);
        }

        if (isShowing) base.setForeground(new Color(0,0,0,80));
        else base.setForeground(Color.BLACK);
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (isShowing){
            base.setText(hint);
        }
        if(!isShowing && base.getText().isBlank()){
            isShowing = true;
            base.setEditable(false);
        }
        
        if (isShowing) base.setForeground(new Color(0,0,0,80));
        else base.setForeground(Color.BLACK);
    }

    public String getText() {
        if (isShowing) return "";
        return base.getText();
    }

    public void setText(String t) {
        isShowing = (t.isBlank());
        focusLost(null);
        base.setText(t.isBlank() ? "" : t);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        focusGained(null);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        focusGained(null);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {/* Unnesessery */ }
}
