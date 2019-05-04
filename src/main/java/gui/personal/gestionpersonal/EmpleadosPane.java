package gui.personal.gestionpersonal;

import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.table.WebTable;
import gui.PrincipalFrame;
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
import util.InclusionFilter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;

public class EmpleadosPane extends WebPanel {
    public final static String TABLE_DOUBLE_CLICKED="Table double clicked";

    private MigLayout layout;
    private EmpleadosTable table;
    private DefaultTableModel model;
    private WebScrollPane scroll;

    private EmpleadoService empleadoService;
    private TurnoService turnoService;
    private PuestoService puestoService;

    public EmpleadosPane(){
        layout=new MigLayout("","0 [GROW] 0","0 [GROW] 0");
        this.setLayout(layout);
        model=new DefaultTableModel();
        table=new EmpleadosTable(model);

        scroll=new WebScrollPane(table);
        this.add(scroll,"GROW");

        empleadoService=new EmpleadoService(PrincipalFrame.EM);
        turnoService=new TurnoService(PrincipalFrame.EM);
        puestoService=new PuestoService(PrincipalFrame.EM);

        configTable();
        fillEmpleados();
        initListeners();
    }
    private void initListeners(){
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    firePropertyChange(TABLE_DOUBLE_CLICKED,0,1);
                }
            }
        });
        EntityListenerManager.addListener(EmpleadoEntity.class, new EntityListener() {
            @Override
            public void entityUpdated() {
                refresh();
            }
        });
        EntityListenerManager.addListener(TurnoEntity.class, new EntityListener() {
            @Override
            public void entityUpdated() {
                refresh();
            }
        });
        EntityListenerManager.addListener(PuestoEntity.class, new EntityListener() {
            @Override
            public void entityUpdated() {
                refresh();
            }
        });
    }
    private void configTable(){
        model.setColumnCount(4);
        model.setColumnIdentifiers(new String[]{"ID","NOMBRE","APELLIDOS","TURNO","PUESTO"});
        TurnoCellRenderer turnoCellRenderer=new TurnoCellRenderer();
        table.getColumn("TURNO").setCellRenderer(turnoCellRenderer);

        PuestoCellRenderer puestoCellRenderer=new PuestoCellRenderer();
        table.getColumn("PUESTO").setCellRenderer(puestoCellRenderer);
    }
    private void fillEmpleados(){
        Object o[];
        model.setRowCount(0);

        for(EmpleadoEntity entity:empleadoService.findAllEmpleados()){
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
    public TableRowSorter getSorter(){
        return (TableRowSorter) table.getRowSorter();
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
    public void refresh(){
        Integer row=table.getSelectedRow();
        fillEmpleados();
        if(table.getRowCount()>row){
            table.setSelectedRow(row);
        }else{
            table.setSelectedRow(table.getRowCount());
        }
        System.out.println("EmpleadosPane refreshed");
    }
    public ListSelectionModel getSelectionModel(){
        return table.getSelectionModel();
    }
    public BoundedRangeModel getScrollModel(){
        return scroll.getVerticalScrollBar().getModel();
    }
    public void setHeights(ArrayList<Integer> heights){
        for(int n=0;n<table.getRowCount();n++){
            table.setRowHeight(n,heights.get(n));
        }
    }
}
