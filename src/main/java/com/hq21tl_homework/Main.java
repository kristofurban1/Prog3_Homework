package com.hq21tl_homework;

import com.hq21tl_homework.file_dialog.*;

import javax.swing.JFrame;

import javax.swing.JButton;

public class Main {
     private static void createAndShowGUI() {
        JFrame frame = new JFrame(Locales.getString("Title"));
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        JButton button = new JButton(Locales.getString("Btn"));
        button.addActionListener(e -> {
            MyFileDialog fd = new MyFileDialog(frame,Locales.getString("FileDialog_Title"));
            fd.openDialog("");
        });

        frame.add(button);


        //Display the window.
        frame.setVisible(true);
    }
 
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(Main::createAndShowGUI);
    }
}