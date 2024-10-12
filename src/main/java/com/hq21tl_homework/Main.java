package com.hq21tl_homework;

import com.hq21tl_homework.file_dialog.*;

import javax.swing.JFrame;
import javax.swing.JButton;

public class Main {
     private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame(Locales.getString("Title"));
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        //Add the ubiquitous "Hello World" label.
        //JLabel label = new JLabel("Hello World");
        //frame.getContentPane().add(label);
        
        JButton button = new JButton(Locales.getString("Btn"));
        button.addActionListener((e) -> {
            FileDialog.openDialog();
        });

        frame.add(button);


        //Display the window.
        frame.setVisible(true);
    }
 
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(Main::createAndShowGUI);
    }
}