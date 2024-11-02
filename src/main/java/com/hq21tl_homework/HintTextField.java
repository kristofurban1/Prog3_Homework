package com.hq21tl_homework;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.text.JTextComponent;

public class HintTextField<T extends JTextComponent> implements FocusListener{
    public final T base;
    private String hint = "<Hint>";
    private boolean isShowing = true;

    
    public HintTextField(String hint, T component) {
        this.base = component;
        this.hint = hint;
        base.setText(hint);
        base.setFocusable(false);
    }
    
    public void initialize(){
        base.addFocusListener(this);
        base.setFocusable(true);
        focusLost(null);
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
        }
        if (isShowing) base.setForeground(new Color(0,0,0,80));
        else base.setForeground(Color.BLACK);
        System.out.println("Focus gained! Hint: " + hint + ", " + (isShowing ? "showing" : "hidden"));

    }

    @Override
    public void focusLost(FocusEvent e) {
        if (!isShowing){
            if(base.getText().isEmpty()){
                base.setText(hint);
                isShowing = true;
            }
        }
        if (isShowing) base.setForeground(new Color(0,0,0,80));
        else base.setForeground(Color.BLACK);
        System.out.println("Focus lost: " + (isShowing ? "showing" : "hidden"));
    }

    public String getText() {
        if (isShowing) return "";
        return base.getText();
    }

    public void setText(String t) {
        base.setText(t);
        focusLost(null);
    }
}
