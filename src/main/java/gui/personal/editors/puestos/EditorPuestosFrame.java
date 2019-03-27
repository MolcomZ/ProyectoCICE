package gui.personal.editors.puestos;

import com.alee.laf.button.WebButton;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.splitpane.WebSplitPane;
import net.miginfocom.swing.MigLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class EditorPuestosFrame {
    public static final String UPDATE_EVENT="UPDATED";
    public static final String SELECTION_CHANGE_EVENT="SELECTION_CHANGED";

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

        initListeners();
    }
    public void showFrame(){
        frame.setVisible(true);
    }
    private void initListeners(){
        seccionesPane.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                switch(evt.getPropertyName()){
                    case EditorSeccionesPane.SELECTION_CHANGE_EVENT:
                        puestosPane.setSelectedSeccion(seccionesPane.getSelectedSeccion());
                        break;
                    case EditorSeccionesPane.UPDATE_EVENT:
                        frame.firePropertyChange(UPDATE_EVENT,1,0);
                        break;
                }
            }
        });
        puestosPane.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                switch(evt.getPropertyName()){
                    case EditorPuestosPane.UPDATE_EVENT:
                        frame.firePropertyChange(UPDATE_EVENT,1,0);
                        break;
                }
            }
        });
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });
    }
    public void addPropertyChangeListener(PropertyChangeListener listener){
        frame.addPropertyChangeListener(listener);
    }
}
