package gui.personal.ausencias;

import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.splitpane.WebSplitPane;
import gui.personal.EmpleadosTable;
import gui.personal.editors.puestos.PuestoCellRenderer;
import gui.personal.util.TurnoCellRenderer;
import jpa.empleados.EmpleadoEntity;
import jpa.empleados.EmpleadoService;
import jpa.puestos.PuestoEntity;
import jpa.puestos.PuestoService;
import jpa.turnos.TurnoEntity;
import jpa.turnos.TurnoService;
import net.miginfocom.swing.MigLayout;
import util.calendarday.CalendarDayCellRenderer;
import util.InclusionFilter;
import util.calendarday.CalendarDayValue;
import util.calendarday.CalendarDayValueList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;

public class AusenciasPane extends WebPanel {
    public final static String TABLE_DOUBLE_CLICKED="Table double clicked";

    private MigLayout layout;
    private WebSplitPane split;
    private EmpleadosTable table;
    private DefaultTableModel model;
    private WebScrollPane scroll;

    private DayTable ausenciasTable;
    private DefaultTableModel ausenciasModel;
    private WebScrollPane ausenciasScroll;

    private EmpleadoService empleadoService;
    private TurnoService turnoService;
    private PuestoService puestoService;

    private EntityManagerFactory EMF;
    private EntityManager EM;

    public AusenciasPane(){
        layout=new MigLayout("","[GROW]","[GROW]");
        this.setLayout(layout);
        model=new DefaultTableModel();
        table=new EmpleadosTable(model);
        scroll=new WebScrollPane(table);
        ausenciasModel=new DefaultTableModel();
        ausenciasTable=new DayTable(ausenciasModel);
        ausenciasScroll=new WebScrollPane(ausenciasTable);
        split=new WebSplitPane();

        split.setLeftComponent(scroll);
        split.setRightComponent(ausenciasScroll);

        this.add(split,"GROW");

        EMF=Persistence.createEntityManagerFactory("MySQL_JPA");
        EM=EMF.createEntityManager();
        empleadoService=new EmpleadoService(EM);
        turnoService=new TurnoService(EM);
        puestoService=new PuestoService(EM);

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
    }
    private void configTable(){
        model.setColumnCount(4);
        model.setColumnIdentifiers(new String[]{"ID","NOMBRE","APELLIDOS","TURNO","PUESTO"});
        TurnoCellRenderer turnoCellRenderer=new TurnoCellRenderer();
        table.getColumn("TURNO").setCellRenderer(turnoCellRenderer);
        PuestoCellRenderer puestoCellRenderer=new PuestoCellRenderer();
        table.getColumn("PUESTO").setCellRenderer(puestoCellRenderer);

        ausenciasModel.setColumnCount(3);
        ausenciasModel.setColumnIdentifiers(new String[]{"ID","TURNO","PUESTO","1"});
        CalendarDayCellRenderer dayRenderer=new CalendarDayCellRenderer();
        ausenciasTable.setDefaultRenderer(CalendarDayValueList.class,dayRenderer);
        ausenciasTable.setDefaultRenderer(TurnoEntity.class,turnoCellRenderer);
        ausenciasTable.setDefaultRenderer(PuestoEntity.class,puestoCellRenderer);
        scroll.getVerticalScrollBar().setModel(ausenciasScroll.getVerticalScrollBar().getModel());
        table.setSelectionModel(ausenciasTable.getSelectionModel());
    }
    private void fillEmpleados(){
        Object o[],o2[];
        model.setRowCount(0);

        for(EmpleadoEntity entity:empleadoService.findAllEmpleados()){
            EM.refresh(entity);
            o=new Object[5];
            o[0]=entity.getId();
            o[1]=entity.getNombre();
            o[2]=entity.getApellido();
            o[3]=entity.getTurno();
            o[4]=entity.getPuesto();
            model.addRow(o);
            o2=new Object[4];
            o2[0]=entity.getId();
            o2[1]=entity.getTurno();
            o2[2]=entity.getPuesto();
            ArrayList<CalendarDayValue> list=new ArrayList<>();
            list.add(new CalendarDayValue(1l,"A.P",Color.ORANGE));
            list.add(new CalendarDayValue(2l,"V. Verano",Color.GREEN));
            list.add(new CalendarDayValue(3l,"V. Invierno",Color.CYAN));
            list.add(new CalendarDayValue(3l,"F. Local",Color.MAGENTA));
            o2[3]=new CalendarDayValueList(list);
            ausenciasModel.addRow(o2);
        }
        for(int n=0;n<ausenciasTable.getRowCount();n++){
            ausenciasTable.setRowHeight(n,68);
        }
    }
    public void updateFilters(ArrayList<TurnoEntity> turnosList, ArrayList<PuestoEntity> puestosList){
        LinkedList<RowFilter<Object,Object>> filters=new LinkedList<>();
        TableRowSorter<DefaultTableModel> sorter=new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        ausenciasTable.setRowSorter(sorter);
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
    public void refresh(){
        Integer row=table.getSelectedRow();
        fillEmpleados();
        if(table.getRowCount()>row){
            table.setSelectedRow(row);
        }else{
            table.setSelectedRow(table.getRowCount());
        }
    }
}
