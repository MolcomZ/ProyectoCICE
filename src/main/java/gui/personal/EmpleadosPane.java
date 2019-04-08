package gui.personal;

import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import gui.personal.editors.puestos.PuestoCellEditor;
import gui.personal.editors.puestos.PuestoCellRenderer;
import gui.personal.util.TurnoCellEditor;
import gui.personal.util.TurnoCellRenderer;
import jpa.empleados.EmpleadoEntity;
import jpa.empleados.EmpleadoService;
import jpa.puestos.PuestoEntity;
import jpa.puestos.PuestoService;
import jpa.turnos.TurnoEntity;
import jpa.turnos.TurnoService;
import net.miginfocom.swing.MigLayout;
import util.InclusionFilter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.LinkedList;

public class EmpleadosPane extends WebPanel {
    private MigLayout layout;
    private EmpleadosTable table;
    private DefaultTableModel model;
    private WebScrollPane scroll;

    private EntityManagerFactory empleadoEMF;
    private EntityManager empleadoEM;
    private EmpleadoService empleadoService;
    private EntityManagerFactory turnoEMF;
    private EntityManager turnoEM;
    private TurnoService turnoService;
    private EntityManagerFactory puestoEMF;
    private EntityManager puestoEM;
    private PuestoService puestoService;

    private EntityManagerFactory EMF;
    private EntityManager EM;

    public EmpleadosPane(){
        layout=new MigLayout("","[GROW]","[GROW]");
        this.setLayout(layout);
        model=new DefaultTableModel();
        table=new EmpleadosTable(model);
        scroll=new WebScrollPane(table);

        this.add(scroll,"GROW");

        //empleadoEMF=Persistence.createEntityManagerFactory("EmpleadoService");
        //empleadoEM=empleadoEMF.createEntityManager();
        EMF=Persistence.createEntityManagerFactory("MySQL_JPA");
        EM=EMF.createEntityManager();
        empleadoService=new EmpleadoService(EM);
        turnoService=new TurnoService(EM);
        puestoService=new PuestoService(EM);
        //empleadoService=new EmpleadoService(empleadoEM);
        //turnoEMF=Persistence.createEntityManagerFactory("TurnoService");
        //turnoEM=turnoEMF.createEntityManager();
        //turnoService=new TurnoService(turnoEM);
        //puestoEMF=Persistence.createEntityManagerFactory("PuestoService");
        //puestoEM=puestoEMF.createEntityManager();
        //puestoService=new PuestoService(puestoEM);

        configTable();
        fillEmpleados();
    }
    private void configTable(){
        model.setColumnCount(4);
        model.setColumnIdentifiers(new String[]{"ID","NOMBRE","APELLIDOS","TURNO","PUESTO"});
        TurnoCellRenderer turnoCellRenderer=new TurnoCellRenderer();
        table.getColumn("TURNO").setCellRenderer(turnoCellRenderer);
        TurnoCellEditor turnoEditor=new TurnoCellEditor();
        turnoEditor.setList((ArrayList<TurnoEntity>) turnoService.findAllTurnos());
        table.getColumn("TURNO").setCellEditor(turnoEditor);
        
        
        PuestoCellRenderer puestoCellRenderer=new PuestoCellRenderer();
        table.getColumn("PUESTO").setCellRenderer(puestoCellRenderer);
        PuestoCellEditor puestoEditor=new PuestoCellEditor();
        puestoEditor.setList((ArrayList<PuestoEntity>) puestoService.findAllPuestos());
        table.getColumn("PUESTO").setCellEditor(puestoEditor);
        
        //updateSeccionEditor();
    }
    private void fillEmpleados(){
        Object o[];
        model.setRowCount(0);
        for(EmpleadoEntity entity:empleadoService.findAllEmpleadoes()){
            o=new Object[5];
            o[0]=entity.getId();
            o[1]=entity.getNombre();
            o[2]=entity.getApellido();
            o[3]=entity.getTurno();
            o[4]=entity.getPuesto();
            model.addRow(o);
        }
    }
    public void updateFilters(ArrayList<TurnoEntity> turnosList, ArrayList<PuestoEntity> puestosList){
        LinkedList<RowFilter<Object,Object>> filters=new LinkedList<>();
        TableRowSorter<DefaultTableModel> sorter=new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        InclusionFilter<TurnoEntity> turnosFilter=new InclusionFilter<>();
        InclusionFilter<PuestoEntity> puestosFilter=new InclusionFilter<>();

        for(TurnoEntity entity:turnosList) {
            turnosFilter.add(3, entity);
        }
        for(PuestoEntity entity:puestosList){
            puestosFilter.add(4,entity);
        }
        filters.add(turnosFilter);
        filters.add(puestosFilter);

        sorter.setRowFilter(RowFilter.andFilter(filters));
    }
    public EmpleadoEntity getSelectedEmpleado(){
        EmpleadoEntity entity=null;
        Integer row=table.getSelectedRow();
        if(row!=-1){
            entity=new EmpleadoEntity();
            entity.setId((Long) table.getValueAt(row,table.convertColumnIndexToView(0)));
            entity.setNombre((String) table.getValueAt(row,table.convertColumnIndexToView(1)));
            entity.setApellido((String) table.getValueAt(row,table.convertColumnIndexToView(2)));
            entity.setTurno((TurnoEntity) table.getValueAt(row,table.convertColumnIndexToView(3)));
            entity.setPuesto((PuestoEntity) table.getValueAt(row,table.convertColumnIndexToView(4)));
        }
        return entity;
    }
}
