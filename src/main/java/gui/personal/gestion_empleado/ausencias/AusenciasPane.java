package gui.personal.gestion_empleado.ausencias;

import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.spinner.WebSpinner;
import com.alee.laf.splitpane.WebSplitPane;
import jpa.ausencias.TiposAusenciaEntity;
import jpa.ausenciasempleado.AusenciasEmpleadoEntity;
import jpa.ausenciasempleado.AusenciasEmpleadoService;
import jpa.empleados.EmpleadoEntity;
import net.miginfocom.swing.MigLayout;
import util.AutoSizeableTable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AusenciasPane extends WebPanel {
    private MigLayout layout;
    private WebLabel yearLabel;
    private WebSpinner yearSpinner;
    private WebSplitPane split;
    private TiposPane tiposPane;
    private ConfirmadasPane confirmadasPane;

    //PERSISTENCE
    EntityManagerFactory EMF;
    EntityManager EM;
    AusenciasEmpleadoService ausenciasEmpleadoService;

    EmpleadoEntity empleado;

    public AusenciasPane() {
        layout=new MigLayout("","[GROW]","[][GROW]");
        setLayout(layout);
        yearLabel=new WebLabel("AÑO:");
        yearSpinner=new WebSpinner();
        yearSpinner.setValue(Calendar.getInstance().get(Calendar.YEAR));
        split=new WebSplitPane();
        tiposPane=new TiposPane();
        tiposPane.setYear((Integer) yearSpinner.getValue());
        confirmadasPane=new ConfirmadasPane();

        split.setLeftComponent(tiposPane);
        split.setRightComponent(confirmadasPane);
        add(yearLabel,"SPLIT 2");
        add(yearSpinner,"WRAP");
        add(split,"GROW");

        EMF=Persistence.createEntityManagerFactory("MySQL_JPA");
        EM=EMF.createEntityManager();
        ausenciasEmpleadoService=new AusenciasEmpleadoService(EM);
        initListeners();
    }
    private void initListeners(){
        yearSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                tiposPane.setYear((Integer) yearSpinner.getValue());
            }
        });
        tiposPane.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getPropertyName().equals(tiposPane.SELECTION_CHANGED)){
                    confirmadasPane.setAusencia((AusenciasEmpleadoEntity) evt.getNewValue());
                }
            }
        });
    }
    public void setEmpleado(EmpleadoEntity empleado){
        this.empleado=empleado;
        tiposPane.setEmpleado(empleado);
    }
}
