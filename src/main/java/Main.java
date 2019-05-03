import com.alee.laf.WebLookAndFeel;
import gui.PrincipalFrame;
import util.UserSettings;


import javax.swing.*;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {

        try {
            //Inicia WebLookAndFeel
            UIManager.setLookAndFeel ( WebLookAndFeel.class.getCanonicalName () );
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        //PRUEBA
        UIDefaults defaults=UIManager.getDefaults();
        defaults.getColor("Table.selectionBackground");
        //UIManager.put("Table.selectionBackground",new Color(200,175,33));

        //PRUEBA SETTINGS
        //try {
         //   UserSettings.loadSettings();
       // } catch (JAXBException e) {
        //    e.printStackTrace();
        //} catch (FileNotFoundException e) {
         //   e.printStackTrace();
       // }


        PrincipalFrame principal=new PrincipalFrame();
        principal.muestraFrame();
    }
}
