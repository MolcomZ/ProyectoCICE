package gui.settings;

import com.alee.extended.image.WebDecoratedImage;
import com.alee.laf.button.WebButton;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.tabbedpane.WebTabbedPane;
import gui.settings.appearance.AppearancePane;
import net.miginfocom.swing.MigLayout;
import util.UserSettings;

import javax.swing.*;
import javax.xml.bind.JAXBException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class SettingsFrame {
    private MigLayout layout;
    private WebFrame frame;
    private AppearancePane appearancePane;
    private WebTabbedPane tabbedPane;
    private WebButton cancelButton;
    private WebButton okButton;

    public SettingsFrame(){
        layout=new MigLayout("","[GROW]","[GROW][]");
        frame=new WebFrame("Configuración");
        frame.setLayout(layout);
        tabbedPane=new WebTabbedPane();
        tabbedPane.setTabPlacement(WebTabbedPane.LEFT);
        appearancePane=new AppearancePane();
        tabbedPane.addTab("Apariencia",new ImageIcon(getClass().getResource("/Add.png")),appearancePane);
        tabbedPane.addTab("Otros",new ImageIcon(getClass().getResource("/Remove.png")),null);
        okButton=new WebButton("ACEPTAR");
        cancelButton=new WebButton("CANCELAR");

        frame.add(tabbedPane,"GROW,WRAP");
        frame.add(cancelButton,"RIGHT,SPLIT");
        frame.add(okButton,"WRAP");
        initListeners();
    }
    private void initListeners(){
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                appearancePane.saveSettings();
               // try {
                    //UserSettings.saveSettings();
                //} catch (JAXBException e1) {
                  //  e1.printStackTrace();
               // }
            }
        });
    }
    public void showFrame(){
        frame.setVisible(true);
        frame.pack();
    }
}
