package com.hq21tl_homework;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.event.ActionListener;

import javax.swing.JButton;

public class Main {
     private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("HelloWorldSwing");
        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Add the ubiquitous "Hello World" label.
        //JLabel label = new JLabel("Hello World");
        //frame.getContentPane().add(label);
        
        JButton button = new JButton("Dialog");
        button.addActionListener((e) -> {
            FileDialog fd = new FileDialog();
        });

        frame.add(button);


        //Display the window.
        frame.setVisible(true);
    }
 
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }
}