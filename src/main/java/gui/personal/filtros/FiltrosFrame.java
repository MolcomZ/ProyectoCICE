package gui.personal.filtros;

import com.alee.laf.button.WebButton;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.tabbedpane.WebTabbedPane;
import gui.personal.filtros.puestos.FiltroPuestosPane;
import gui.personal.filtros.turnos.FiltroTurnosPane;
import jpa.puestos.PuestoEntity;
import jpa.turnos.TurnoEntity;
import net.miginfocom.swing.MigLayout;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class FiltrosFrame {
    private WebFrame frame;
    private MigLayout layout;

    private WebTabbedPane tabbedPane;
    private FiltroPuestosPane filtroPuestosPane;
    private FiltroTurnosPane filtroTurnosPane;
    private WebButton closeButton;

    public FiltrosFrame(){
        layout=new MigLayout("","[GROW]","[GROW][]");
        frame=new WebFrame("Filtros");
        frame.setLayout(layout);
        tabbedPane=new WebTabbedPane();
        filtroPuestosPane=new FiltroPuestosPane();
        filtroTurnosPane=new FiltroTurnosPane();
        tabbedPane.addTab("PUESTOS",filtroPuestosPane);
        tabbedPane.addTab("TURNOS",filtroTurnosPane);
        closeButton=new WebButton("CERRAR");

        frame.add(tabbedPane,"GROW,WRAP");
        frame.add(closeButton,"RIGHT");

        frame.pack();
        initListeners();
    }
    public void showFrame(){
        //update();
        //loadProperties();
        frame.setVisible(true);
    }
    public void initListeners(){
        filtroPuestosPane.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                fireUpdate();
            }
        });
        filtroTurnosPane.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                fireUpdate();
            }
        });
    }
    public ArrayList<PuestoEntity> getSelectedPuestos(){
        return filtroPuestosPane.getSelectedItems();
    }
    public ArrayList<TurnoEntity> getSelectedTurnos(){
        return filtroTurnosPane.getSelectedItems();
    }
    private void fireUpdate(){
        frame.firePropertyChange("DataUpdated",1,0);
    }
    public void addPropertyChangeListener(PropertyChangeListener listener){
        frame.addPropertyChangeListener(listener);
    }
}
