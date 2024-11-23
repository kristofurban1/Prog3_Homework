package com.hq21tl_homework.gui.lang_selector;

import java.awt.BorderLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.hq21tl_homework.Locales;
import com.hq21tl_homework.Utils.KeyValuePair;

public class LangSelector extends JDialog {
    private void guiBuilder(){
        setTitle(Locales.getString("LangSelector_Title"));
        setSize(300,100);
        setLayout(new BorderLayout());
        JLabel langLabel = new JLabel(Locales.getString("LangSelector_Label"));
        add(langLabel, BorderLayout.WEST);
        JComboBox<String> langSelector = new JComboBox<>();

        String selectedLang = Locales.getSelectedLocale();
        int selectedIndex = 0;
        KeyValuePair<String, String>[] availableLocales = Locales.getAvailableLocales();
        String[] names = new String[availableLocales.length];
        for (int i = 0; i < availableLocales.length; i++) {
            names[i] = availableLocales[i].getValue() + " ("+availableLocales[i].getKey()+")";
            
            if (selectedLang.equals(availableLocales[i].getKey()))
                selectedIndex = i;
        }
        langSelector.setModel(new DefaultComboBoxModel<>(names));
        langSelector.setSelectedIndex(selectedIndex);
        add(langSelector, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        JButton okBtn = new JButton(Locales.getString("LangSelector_Ok"));
        JButton cancelBtn = new JButton(Locales.getString("LangSelector_Cancel"));
        southPanel.add(okBtn);
        southPanel.add(cancelBtn);
        add(southPanel, BorderLayout.SOUTH);

        okBtn.addActionListener(l ->{
            Locales.switchLanguage(availableLocales[langSelector.getSelectedIndex()].getKey());
            dispose();
        });
        cancelBtn.addActionListener(l->dispose());

        setVisible(true);
        
    }
    public LangSelector(){
        guiBuilder();
    }
}
