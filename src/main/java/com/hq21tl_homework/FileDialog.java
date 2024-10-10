package com.hq21tl_homework;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Locale;

import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class FileDialog extends JDialog{

    private static FileDialog dialog = null;

    JPanel topPanel; JLabel topLabel;
    JPanel sidePanel;
    JPanel centerPanel; JPanel cpBottomPanel;
    private void SizeObjects(){
        topPanel.setPreferredSize(new Dimension(getWidth(), 50));
        sidePanel.setPreferredSize(new Dimension(150, getHeight() - topPanel.getHeight()));
        cpBottomPanel.setPreferredSize(new Dimension(getWidth() - sidePanel.getSize().width, 50));
        
        
        topPanel.revalidate();
        topPanel.repaint();
        sidePanel.revalidate();
        sidePanel.repaint();
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void guiBuilder(){
        setMinimumSize(new Dimension(750, 500));
        setTitle(Locales.getString("FileDialog_WindowName"));
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setAlwaysOnTop(true);
        setFocusable(true);
        setLayout(new BorderLayout(2, 2));

        setBackground(new Color(100, 100, 100));

        topPanel = new JPanel(new BorderLayout(10, 0));

        JButton navigateBack = new JButton("^");
        navigateBack.setPreferredSize(new Dimension((int)(topPanel.getSize().width * 0.05), topPanel.getSize().height - 20));
        topPanel.add(navigateBack, BorderLayout.WEST);

        JButton navigate = new JButton(Locales.getString("FileDialog_Navigate"));
        navigate.setPreferredSize(new Dimension((int)(topPanel.getSize().width * 0.02), topPanel.getSize().height - 20));
        topPanel.add(navigate, BorderLayout.EAST);

        JTextField pathInput = new JTextField();
        pathInput.setPreferredSize(new Dimension(getWidth() - navigateBack.getSize().width - navigate.getSize().width - 20, HEIGHT));
        pathInput.setBackground(new Color(200,200,200));
        topPanel.add(pathInput, BorderLayout.CENTER);


        sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));



        centerPanel = new JPanel(new BorderLayout());

        cpBottomPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        centerPanel.add(cpBottomPanel, BorderLayout.SOUTH);
        
        
        topPanel.setBackground(new Color(180, 180, 180));
        sidePanel.setBackground(new Color(180, 180, 180));
        centerPanel.setBackground(new Color(180, 180, 180));
        cpBottomPanel.setBackground(new Color(180, 180, 180));

        topLabel = new JLabel();
        topPanel.add(topLabel);

        SizeObjects();
        
        add(topPanel, BorderLayout.NORTH);
        add(sidePanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);

        setVisible(true);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                SizeObjects();
            }
        });
    }
    private FileDialog(){
        super(new JFrame());
        guiBuilder();
    }

    public static void openDialog(){
        if (dialog == null) dialog = new FileDialog();
        else dialog.setVisible(true);
    }

    public static void disposeDialog(){
        dialog = null;
    }
}