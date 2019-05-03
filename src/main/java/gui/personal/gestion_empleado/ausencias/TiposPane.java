package gui.personal.gestion_empleado.ausencias;

import com.alee.laf.button.WebButton;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import gui.personal.util.EntityCellEditor;
import gui.personal.util.EntityCellRenderer;
import gui.personal.util.TipoAusenciaCellRenderer;
import jpa.ausencias.TiposAusenciaEntity;
import jpa.ausencias.TiposAusenciaService;
import jpa.ausenciasempleado.AusenciasEmpleadoEntity;
import jpa.ausenciasempleado.AusenciasEmpleadoService;
import jpa.empleados.EmpleadoEntity;
import net.miginfocom.swing.MigLayout;
import util.AutoSizeableTable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TiposPane extends WebPanel {
    public static final String SELECTION_CHANGED="Selection changed";
    private MigLayout layout;
    private DefaultTableModel model;
    private AutoSizeableTable table;
    private WebScrollPane scroll;
    private WebButton addButton;
    private WebButton removeButton;

    //PERSISTENCE
    EntityManagerFactory EMF;
    EntityManager EM;
    AusenciasEmpleadoService service;
    TiposAusenciaService tiposService;

    private EmpleadoEntity empleado;
    private Integer year;

    public TiposPane(){
        layout=new MigLayout("","[GROW]","[GROW][]");
        setLayout(layout);

        model=new DefaultTableModel();
        table=new AutoSizeableTable(model);
        scroll=new WebScrollPane(table);
        addButton=new WebButton("Agregar",new ImageIcon(getClass().getResource("/Add.png")));
        removeButton=new WebButton("Eliminar",new ImageIcon(getClass().getResource("/Remove.png")));

        add(scroll,"GROW,WRAP");
        add(addButton,"SPLIT 2,LEFT");
        add(removeButton,"LEFT,WRAP");

        EMF=Persistence.createEntityManagerFactory("MySQL_JPA");
        EM=EMF.createEntityManager();
        service=new AusenciasEmpleadoService(EM);
        tiposService=new TiposAusenciaService(EM);

        configTable();
        initListeners();
    }
    public void setYear(Integer year){
        this.year=year;
        fillTable();
        firePropertyChange(SELECTION_CHANGED,null,getSelectedAusencia());
    }
    private void initListeners(){
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTipo();
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeTipo();
            }
        });
        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if(e.getType()==TableModelEvent.UPDATE){
                    editTipo();
                    System.out.println("EDIT");
                }
            }
        });
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                firePropertyChange(SELECTION_CHANGED,null,getSelectedAusencia());
            }
        });
    }
    private void configTable(){
        model.setColumnCount(4);
        model.setColumnIdentifiers(new String[]{"ID","TIPO","AÑO","CANTIDAD"});
        EntityCellRenderer<TiposAusenciaEntity> renderer=new EntityCellRenderer<TiposAusenciaEntity>() {
            @Override
            public String getText(TiposAusenciaEntity tiposAusenciaEntity) {
                return tiposAusenciaEntity.getNombre();
            }
        };
        table.getColumn("TIPO").setCellRenderer(renderer);
        EntityCellEditor<TiposAusenciaEntity> editor=new EntityCellEditor<TiposAusenciaEntity>() {
            @Override
            public String getText(TiposAusenciaEntity tiposAusenciaEntity) {
                return tiposAusenciaEntity.getNombre();
            }

            @Override
            public Long getID(TiposAusenciaEntity tiposAusenciaEntity) {
                return tiposAusenciaEntity.getId();
            }
        };
        editor.setList((ArrayList<TiposAusenciaEntity>) tiposService.findAllTiposAusencias());
        table.getColumn("TIPO").setCellEditor(editor);
    }
    private void fillTable(){
        if(empleado==null)return;
        List<AusenciasEmpleadoEntity> ausencias;
        ausencias=service.findAusenciasEmpleadoesByIdEmpleadoAndByYear(empleado.getId(),year);
        model.setRowCount(0);
        for(AusenciasEmpleadoEntity entity:ausencias){
            Object o[]=new Object[4];
            o[0]=entity.getId();
            o[1]=entity.getTipo();
            o[2]=entity.getYear();
            o[3]=entity.getCantidad();
            model.addRow(o);
        }
    }
    public void setEmpleado(EmpleadoEntity empleado){
        this.empleado=empleado;
        fillTable();
    }
    public void addTipo(){
        ArrayList<TiposAusenciaEntity> tipos;
        List<AusenciasEmpleadoEntity> usados;
        tipos=(ArrayList<TiposAusenciaEntity>) tiposService.findAllTiposAusencias();
        usados=service.findAusenciasEmpleadoesByIdEmpleadoAndByYear(empleado.getId(),year);
        for(AusenciasEmpleadoEntity ausenciasEmpleadoEntity:usados){
            TiposAusenciaEntity tipo=ausenciasEmpleadoEntity.getTipo();
            tipos.remove(tipo);
        }
        if(tipos.size()>0){
            service.createAusenciasEmpleado(empleado,
                    tipos.get(0),
                    year,
                    0);
            fillTable();
        }else{
            WebOptionPane.showMessageDialog(this,"El empleado ya tiene todos los tipos de ausencias.");
        }
    }
    private void removeTipo(){
        AusenciasEmpleadoEntity entity=getSelectedAusencia();
        if(entity==null){
            WebOptionPane.showMessageDialog(this,"No hay seleccionado ningún tipo de ausencia.");
            return;
        }
        Integer row=table.getSelectedRow();
        service.removeAusenciasEmpleado(entity.getId());
        fillTable();
        if (row >= table.getRowCount()) {
            row = table.getRowCount()-1;
        }
        if(row>=0) {
            table.setSelectedRow(row);
        }
    }
    private void editTipo(){
        AusenciasEmpleadoEntity entity=getSelectedAusencia();
        try {
            service.updateAusenciasEmpleado(entity.getId(),
                    entity.getTipo(),
                    entity.getYear(),
                    entity.getCantidad());
        }catch(Exception e){
            WebOptionPane.showMessageDialog(this,
                    "Error al editar registro.",
                    "Error",
                    WebOptionPane.ERROR_MESSAGE
            );
            return;
        }
    }
    private AusenciasEmpleadoEntity getSelectedAusencia(){
        Integer row;
        Long id;
        TiposAusenciaEntity tiposAusenciaEntity;
        Integer year;
        Integer cantidad;
        AusenciasEmpleadoEntity entity=null;
        row=table.getSelectedRow();
        if(row!=-1){
            id=(Long) table.getValueAt(table.convertRowIndexToModel(row), table.convertColumnIndexToModel(0));
            tiposAusenciaEntity=(TiposAusenciaEntity) table.getValueAt(table.convertRowIndexToModel(row), table.convertColumnIndexToModel(1));
            year=(Integer) table.getValueAt(table.convertRowIndexToModel(row), table.convertColumnIndexToModel(2));
            cantidad=Integer.parseInt(table.getValueAt(table.convertRowIndexToModel(row), table.convertColumnIndexToModel(3)).toString());
            entity=new AusenciasEmpleadoEntity(id,empleado,tiposAusenciaEntity,year,cantidad);
        }
        return entity;
    }

}
