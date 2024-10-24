package com.hq21tl_homework;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.hq21tl_homework.file_dialog.MyFileDialog;

public class Main {
     private static void createAndShowGUI() {
        JFrame frame = new JFrame(Locales.getString("Title"));
        frame.setLayout(new FlowLayout(FlowLayout.LEADING));
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        JTextField tf = new JTextField();
        JLabel label = new JLabel();
        JButton button = new JButton(Locales.getString("Btn"));

        
        button.setSize(300, 50);
        tf.setMinimumSize(new Dimension(300, 50));
        tf.setPreferredSize(new Dimension(300, 50));
        label.setSize(50,50);


        button.addActionListener(e -> {
            String p = tf.getText();
            MyFileDialog fd = new MyFileDialog(frame,Locales.getString("FileDialog_WindowName"));
            File got = fd.openDialog(p);
            if (got != null) label.setText(got.getAbsolutePath());
        });

        frame.add(tf);
        frame.add(button);
        frame.add(label);

        //Display the window.
        frame.setVisible(true);
    }
 
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(Main::createAndShowGUI);
    }
}