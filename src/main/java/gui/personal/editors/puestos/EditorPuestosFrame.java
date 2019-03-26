package gui.personal.editors.puestos;

import com.alee.laf.button.WebButton;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.splitpane.WebSplitPane;
import net.miginfocom.swing.MigLayout;

public class EditorPuestosFrame {
    private MigLayout layout;
    private WebFrame frame;
    private WebSplitPane split;
    private EditorSeccionesPane seccionesPane;
    private EditorPuestosPane puestosPane;
    private WebButton closeButton;

    public EditorPuestosFrame(){
        layout=new MigLayout("","[GROW]","[GROW][]");
        frame=new WebFrame("Editor de puestos");
        frame.setLayout(layout);
        split=new WebSplitPane();
        seccionesPane=new EditorSeccionesPane();
        puestosPane=new EditorPuestosPane();
        split.setLeftComponent(seccionesPane);
        split.setRightComponent(puestosPane);
        closeButton=new WebButton("CERRAR");
        frame.add(split,"GROW,WRAP");
        frame.add(closeButton,"RIGHT");

        frame.pack();
    }
    public void showFrame(){
        frame.setVisible(true);
    }
}
