package gui.settings;

import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.list.WebList;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.tabbedpane.WebTabbedPane;
import gui.settings.groups.AppearanceSettingsGroupPane;
import gui.settings.individual.AusenciasColorsSettingsPane;
import gui.settings.individual.CalendariosColorsSettingsPane;
import gui.settings.individual.IndividualSettingsPane;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class SettingsFrame {
    private MigLayout layout;
    private WebFrame frame;
    private WebList list;
    private DefaultListModel model;
    private WebPanel panel;
    private CardLayout cardLayout;
    private WebScrollPane scroll;

    private AppearanceSettingsGroupPane appearancePane;
    private WebButton cancelButton;
    private WebButton okButton;

    public SettingsFrame(){
        layout=new MigLayout("","[][GROW]","[GROW][]");
        frame=new WebFrame("Configuración");
        frame.setLayout(layout);
        list=new WebList();
        list.setOpaque(false);
        list.setModel(model=new DefaultListModel());
        model.addElement("APARIENCIA");
        model.addElement("OTROS");
        list.setSelectedIndex(0);

        cardLayout=new CardLayout();
        panel=new WebPanel(cardLayout);
        scroll=new WebScrollPane(panel);
        appearancePane=new AppearanceSettingsGroupPane();
        panel.add(appearancePane,"Appearance");
        panel.add(new WebLabel("WebLabel"),"HOLA");
        okButton=new WebButton("ACEPTAR");
        cancelButton=new WebButton("CANCELAR");

        frame.add(list,"TOP");
        frame.add(scroll,"GROW,WRAP");
        frame.add(cancelButton,"CELL 1 1,RIGHT,SPLIT");
        frame.add(okButton,"WRAP");
        initListeners();
    }
    private void initListeners(){
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                switch(list.getSelectedIndex()){
                    case 0:
                        cardLayout.show(panel,"Appearance");
                        break;
                    case 1:
                        cardLayout.show(panel,"HOLA");
                        break;
                }
            }
        });
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                appearancePane.saveSettings();
                frame.dispatchEvent(new WindowEvent(frame,WindowEvent.WINDOW_CLOSING));
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame,WindowEvent.WINDOW_CLOSING));
            }
        });
    }
    public void showFrame(){
        frame.setVisible(true);
        frame.pack();
    }
}
