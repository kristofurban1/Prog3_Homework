package com.hq21tl_homework.file_dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class BottomPanelObjects {
    private BottomPanelObjects() {}

    public static class FileNameInputField extends JTextField{
        
        public void initialize(JComponent parent, MyFileDialog root){

        }
    }

    public static class ConfirmButton extends JButton implements ActionListener{

        public void initialize(JComponent parent, MyFileDialog root){

        }
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(e.toString());
        }
    }

    public static class CancelButton extends JButton implements ActionListener{
        public void initialize(JComponent parent, MyFileDialog root){

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(e.toString());
        }
    }
}
