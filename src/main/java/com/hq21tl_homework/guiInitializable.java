package com.hq21tl_homework;

import java.awt.Window;

import javax.swing.JComponent;

public interface guiInitializable<T extends Window> {
    public void initialize(JComponent parent, T root);
}
