package gui.personal.gestionpersonal.ausencias;

import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.spinner.WebSpinner;
import com.alee.laf.splitpane.WebSplitPane;
import gui.PrincipalFrame;
import gui.personal.gestionpersonal.EmpleadosPane;
import jpa.ausenciasempleadoconfirmadas.AusenciasConfirmadasEntity;
import jpa.ausenciasempleadoconfirmadas.AusenciasConfirmadasService;
import jpa.empleados.EmpleadoEntity;
import jpa.empleados.EmpleadoService;
import jpa.puestos.PuestoEntity;
import jpa.turnos.TurnoEntity;
import net.miginfocom.swing.MigLayout;
import util.EntityListener;
import util.EntityListenerManager;
import util.calendarday.CalendarDayCellRenderer;
import util.calendarday.CalendarDayValue;
import util.calendarday.CalendarDayValueList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DiasDeSemanaPane extends WebPanel {
    public static final String PROPERTY_HEIGHTSCHANGED="Property heights changed";

    private MigLayout layout;
    private WebSplitPane split;
    private WebPanel semanaPanel;
    private WebLabel semanaLabel;
    private WebSpinner semanaSpinner;
    private SpinnerNumberModel spinnerModel;
    private DefaultTableModel model;
    private WebScrollPane scroll;
    private DayTable table;
    private EmpleadosPane empleadosPane;

    private Integer year;
    Calendar fechainicio;
    Calendar fechafin;

    EmpleadoService empleadoService;
    AusenciasConfirmadasService confirmadaSservice;


    public DiasDeSemanaPane() {
        layout = new MigLayout("", "[GROW]", "[][GROW]");
        semanaPanel=new WebPanel();
        setLayout(layout);
        semanaLabel = new WebLabel("SEMANA:");
        semanaSpinner = new WebSpinner(spinnerModel = new SpinnerNumberModel());
        spinnerModel.setValue(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));
        model = new DefaultTableModel();
        table = new DayTable(model);
        scroll = new WebScrollPane(table);
        empleadosPane=new EmpleadosPane();
        empleadosPane.setMargin(0);
        split=new WebSplitPane();
        split.setDividerSize(3);

        add(semanaLabel, "SPLIT 2");
        add(semanaSpinner, "W 60,WRAP");
        semanaPanel.setLayout(new MigLayout("","0 [GROW] 0","0 [GROW] 0"));
        semanaPanel.setMargin(0);
        semanaPanel.add(scroll,"GROW");

        split.setLeftComponent(empleadosPane);
        split.setRightComponent(semanaPanel);

        add(split,"GROW");

        empleadoService = new EmpleadoService(PrincipalFrame.EM);
        confirmadaSservice = new AusenciasConfirmadasService(PrincipalFrame.EM);

        configTable();
        initListeners();
    }

    private void initListeners() {
        semanaSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                configSemana();
                fillTable();
            }
        });
        EntityListenerManager.addListener(AusenciasConfirmadasEntity.class, new EntityListener() {
            @Override
            public void entityUpdated() {
                refresh();
            }
        });
        table.setSelectionModel(empleadosPane.getSelectionModel());
        scroll.getVerticalScrollBar().setModel(empleadosPane.getScrollModel());
    }

    public void setYear(Integer year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        this.year = year;
        spinnerModel.setMinimum(1);
        spinnerModel.setMaximum(calendar.getMaximum(Calendar.WEEK_OF_YEAR));
        configSemana();
        fillTable();
    }

    private void configTable() {
        model.setColumnCount(3);
        model.setColumnIdentifiers(new String[]{"ID", "TURNO", "PUESTO"});
        CalendarDayCellRenderer dayRenderer = new CalendarDayCellRenderer();
        table.setDefaultRenderer(CalendarDayValueList.class, dayRenderer);
        table.setDefaultRenderer(TurnoEntity.class, null);
        table.setDefaultRenderer(PuestoEntity.class, null);
        scroll.getVerticalScrollBar().setModel(scroll.getVerticalScrollBar().getModel());
        table.setSelectionModel(table.getSelectionModel());
        configSemana();
    }

    public void configSemana() {
        model.setColumnCount(0);
        if (year == null) {
            return;
        }
        fechainicio = Calendar.getInstance();
        fechainicio.set(Calendar.YEAR, year);
        fechainicio.set(Calendar.WEEK_OF_YEAR, (Integer) semanaSpinner.getValue());
        fechainicio.set(Calendar.DAY_OF_WEEK, fechainicio.getFirstDayOfWeek());
        fechafin = Calendar.getInstance();
        fechafin = (Calendar) fechainicio.clone();
        fechafin.add(Calendar.DATE, 7);

        if (fechainicio == null || fechafin == null) return;
        LocalDate a = fechainicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate b = fechafin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        for (; a.isBefore(b); a = a.plusDays(1)) {
            model.addColumn(a.toString());
        }
    }

    private void fillTable() {
        TableRowSorter<DefaultTableModel> sorter= (TableRowSorter<DefaultTableModel>) table.getRowSorter();
        table.setRowSorter(null);
        ArrayList<EmpleadoEntity> list;
        model.setRowCount(0);
        Object o[];
        list = (ArrayList<EmpleadoEntity>) empleadoService.findAllEmpleados();
        for (EmpleadoEntity entity : list) {
            o = new Object[model.getColumnCount()];
            for (int n = 0; n < model.getColumnCount(); n++) {
                Calendar cal;
                cal= (Calendar) fechainicio.clone();
                cal.add(Calendar.DATE,n);
                o[n] = getValueList(entity,cal.getTime());
            }
            model.addRow(o);
        }
        table.setRowSorter(sorter);
        adjustCellsSize();
    }
    private void adjustCellsSize(){
        ArrayList<Integer> heights=new ArrayList<>();
        for(int n=0;n<table.getRowCount();n++) {
            table.adjustRowMax(n, 20);
            heights.add(table.getRowHeight(n));
        }
        empleadosPane.setHeights(heights);
        table.adjustColumnsTitle();
        firePropertyChange(PROPERTY_HEIGHTSCHANGED,0,1);
    }

    private CalendarDayValueList getValueList(EmpleadoEntity entity, Date day) {
        Calendar cal;
        cal=Calendar.getInstance();
        cal.setTime(day);
        cal.add(Calendar.DATE,-1);
        Date dayplusone=cal.getTime();
        CalendarDayValue value;
        CalendarDayValueList list = new CalendarDayValueList();
        ArrayList<AusenciasConfirmadasEntity> ausencias = (ArrayList<AusenciasConfirmadasEntity>) confirmadaSservice.findAusenciasConfirmadasesByIdEmpleado(entity.getId());
        for (AusenciasConfirmadasEntity confirmadas : ausencias) {
            if (confirmadas.getInicio().compareTo(day) <=0 && confirmadas.getFin().compareTo(dayplusone)>=0) {
                value = new CalendarDayValue();
                value.setId(confirmadas.getAusencia().getTipo().getId());
                value.setText(confirmadas.getAusencia().getTipo().getNombre());
                value.setColor(PrincipalFrame.setting.getAusenciaColor(value.getId()));
                list.add(value);
            }
        }
        return list;
    }
    public void refresh() {
        Integer row=table.getSelectedRow();
        fillTable();
         if(table.getRowCount()>row){
             table.setSelectedRow(row);
         }else{
             table.setSelectedRow(table.getRowCount());
         }
        System.out.println("DiasDeSemanaPane refreshed");
    }

    public void updateFilters(ArrayList<TurnoEntity> turnosList, ArrayList<PuestoEntity> puestosList){
        empleadosPane.updateFilters(turnosList,puestosList);
        setRowSorter(empleadosPane.getSorter());
        adjustCellsSize();
    }
    public void setRowSorter(RowSorter sorter){
        table.setRowSorter(sorter);
    }
    public ArrayList<Integer> getRowHeights(){
        ArrayList<Integer> heights=new ArrayList<>();
        for(int n=0;n<table.getRowCount();n++){
            heights.add(table.getRowHeight(n));
        }
        return heights;
    }
    public void setSelectionModel(ListSelectionModel model){
        table.setSelectionModel(model);
    }
    public void setVerticalScrollBarModel(BoundedRangeModel model){
        scroll.getVerticalScrollBar().setModel(model);
    }

}
