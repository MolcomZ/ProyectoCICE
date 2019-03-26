import com.alee.laf.WebLookAndFeel;
import gui.PrincipalFrame;

import javax.swing.*;

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
        PrincipalFrame principal=new PrincipalFrame();
        principal.muestraFrame();
    }
}
