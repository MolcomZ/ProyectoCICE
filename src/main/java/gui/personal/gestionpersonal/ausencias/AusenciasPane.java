package gui.personal.gestionpersonal.ausencias;

import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.spinner.WebSpinner;
import com.alee.laf.splitpane.WebSplitPane;
import com.alee.laf.tabbedpane.WebTabbedPane;
import gui.PrincipalFrame;
import gui.personal.gestionpersonal.EmpleadosTable;
import gui.personal.editors.puestos.PuestoCellRenderer;
import gui.personal.util.TurnoCellRenderer;
import jpa.empleados.EmpleadoEntity;
import jpa.empleados.EmpleadoService;
import jpa.puestos.PuestoEntity;
import jpa.puestos.PuestoService;
import jpa.turnos.TurnoEntity;
import jpa.turnos.TurnoService;
import net.miginfocom.swing.MigLayout;
import util.EntityListener;
import util.EntityListenerManager;
import util.calendarday.CalendarDayCellRenderer;
import util.InclusionFilter;
import util.calendarday.CalendarDayValue;
import util.calendarday.CalendarDayValueList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

public class AusenciasPane extends WebPanel {
    public final static String TABLE_DOUBLE_CLICKED="Table double clicked";

    private MigLayout layout;
    private MigLayout leftLayout;
    private WebLabel yearLabel;
    private WebSpinner yearSpinner;

    private WebTabbedPane tabbed;

//    private DayTable ausenciasTable;
  //  private DefaultTableModel ausenciasModel;
    //private WebScrollPane ausenciasScroll;

    private DiasDeSemanaPane diasSemanaPane;
    private SemanasDelYearPane semanasYearPane;

    private EmpleadoService empleadoService;
    private TurnoService turnoService;
    private PuestoService puestoService;

    public AusenciasPane(){
        layout=new MigLayout("","[GROW]","[][GROW]");
        this.setLayout(layout);

        leftLayout=new MigLayout("","[GROW]","[][GROW]");
        yearLabel=new WebLabel("AÑO:");
        yearSpinner=new WebSpinner();
        yearSpinner.setValue(Calendar.getInstance().get(Calendar.YEAR));
        add(yearLabel,"SPLIT 2");
        add(yearSpinner,"W 60,WRAP");

        //ausenciasModel=new DefaultTableModel();
        //ausenciasTable=new DayTable(ausenciasModel);
        //ausenciasScroll=new WebScrollPane(ausenciasTable);
        tabbed=new WebTabbedPane();
        //tabbed.addTab("PRUEBA",ausenciasScroll);
        tabbed.setTabPlacement(WebTabbedPane.BOTTOM);
        diasSemanaPane=new DiasDeSemanaPane();
        diasSemanaPane.setYear((Integer) yearSpinner.getValue());
        tabbed.addTab("SEMANA",diasSemanaPane);
        semanasYearPane=new SemanasDelYearPane();
        semanasYearPane.setYear((Integer) yearSpinner.getValue());
        tabbed.addTab("AÑO",semanasYearPane);

        //split=new WebSplitPane();

        //split.setLeftComponent(leftPanel);
        //split.setRightComponent(tabbed);

        this.add(tabbed,"GROW");

        empleadoService=new EmpleadoService(PrincipalFrame.EM);
        turnoService=new TurnoService(PrincipalFrame.EM);
        puestoService=new PuestoService(PrincipalFrame.EM);

        configTable();
        //fillEmpleados();
        initListeners();
    }
    private void initListeners(){
        //table.addMouseListener(new MouseAdapter() {
         //   @Override
          //  public void mouseClicked(MouseEvent e) {
           //     if(e.getClickCount()==2){
            //        firePropertyChange(TABLE_DOUBLE_CLICKED,0,1);
            //    }
           // }
       // });
        yearSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                diasSemanaPane.setYear((Integer) yearSpinner.getValue());
                semanasYearPane.setYear((Integer) yearSpinner.getValue());
            }
        });
        diasSemanaPane.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getPropertyName().equals(diasSemanaPane.PROPERTY_HEIGHTSCHANGED)){
                }
            }
        });
        EntityListenerManager.addListener(TurnoEntity.class, new EntityListener() {
            @Override
            public void entityUpdated() {
//                refresh();
                //fillEmpleados();
            }
        });
        EntityListenerManager.addListener(PuestoEntity.class, new EntityListener() {
            @Override
            public void entityUpdated() {
//                refresh();
                //fillEmpleados();
            }
        });
    }
    private void configTable(){
        TurnoCellRenderer turnoCellRenderer=new TurnoCellRenderer();
        PuestoCellRenderer puestoCellRenderer=new PuestoCellRenderer();
    }
    public void updateFilters(ArrayList<TurnoEntity> turnosList, ArrayList<PuestoEntity> puestosList){
        diasSemanaPane.updateFilters(turnosList,puestosList);
    }
    public EmpleadoEntity getSelectedEmpleado(){
        EmpleadoEntity entity=null;
        //Integer row=table.getSelectedRow();
        //if(row!=-1){
         //   entity=new EmpleadoEntity();
          //  entity.setId((Long) table.getValueAt(row,table.convertColumnIndexToView(0)));
           // entity.setNombre((String) table.getValueAt(row,table.convertColumnIndexToView(1)));
           // entity.setApellido((String) table.getValueAt(row,table.convertColumnIndexToView(2)));
           // entity.setTurno((TurnoEntity) table.getValueAt(row,table.convertColumnIndexToView(3)));
           // entity.setPuesto((PuestoEntity) table.getValueAt(row,table.convertColumnIndexToView(4)));
       // }
        return entity;
    }
    public void refresh(){
        diasSemanaPane.refresh();


        //Integer row=table.getSelectedRow();
        //fillEmpleados();
       // if(table.getRowCount()>row){
       //     table.setSelectedRow(row);
       // }else{
       //     table.setSelectedRow(table.getRowCount());
       // }
    }
}
