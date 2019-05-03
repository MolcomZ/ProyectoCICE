package gui.personal.editors.puestos;

import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import gui.PrincipalFrame;
import jpa.puestos.PuestoEntity;
import jpa.puestos.PuestoService;
import jpa.secciones.SeccionEntity;
import jpa.secciones.SeccionService;
import net.miginfocom.swing.MigLayout;
import util.AutoSizeableTable;
import util.EntityListenerManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class EditorPuestosPane extends WebPanel {
    public static final String UPDATE_EVENT="UPDATED";
    public static final String SELECTION_CHANGE_EVENT="SELECTION_CHANGED";

    private MigLayout layout;
    private WebPanel titlePane;
    private WebButton addButton;
    private WebButton removeButton;
    private AutoSizeableTable table;
    private WebScrollPane scroll;
    private DefaultTableModel model;

    private PuestoService puestoService;
    private SeccionService seccionService;
    
    private SeccionEntity selectedSeccionEntity;

    public EditorPuestosPane(){
        layout=new MigLayout("","[GROW]","[][][GROW]");
        setLayout(layout);
        addButton=new WebButton(new ImageIcon(getClass().getResource("/Add.png")));
        removeButton=new WebButton(new ImageIcon(getClass().getResource("/Remove.png")));
        titlePane=new WebPanel();
        titlePane.setUndecorated(false);
        titlePane.setRound(4);
        titlePane.setLayout(new FlowLayout());
        titlePane.add(new WebLabel("PUESTOS"));
        model=new DefaultTableModel();
        table=new AutoSizeableTable(model);
        scroll=new WebScrollPane(table);

        add(addButton,"SPLIT");
        add(removeButton,"WRAP");
        add(titlePane,"GROWX,WRAP");
        add(scroll,"GROW,WRAP");

        puestoService=new PuestoService(PrincipalFrame.EM);
        seccionService=new SeccionService(PrincipalFrame.EM);

        configTable();
        initListeners();
    }
    private void initListeners(){
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPuesto();
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePuesto();
            }
        });
        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if(e.getType()==TableModelEvent.UPDATE){
                    editPuesto();
                }
            }
        });
    }
    private void configTable(){
        model.setColumnCount(3);
        model.setColumnIdentifiers(new String[]{"ID","NOMBRE","SECCION"});
        SeccionCellRenderer seccionCellRenderer=new SeccionCellRenderer();
        table.getColumn("SECCION").setCellRenderer(seccionCellRenderer);
        SeccionCellEditor seccionCellEditor=new SeccionCellEditor();
        table.getColumn("SECCION").setCellEditor(seccionCellEditor);
        seccionCellEditor.setList((ArrayList<SeccionEntity>) seccionService.findAllSecciones());
    }
    public void fillPuestos(){
        Object o[];
        model.setRowCount(0);
        if(selectedSeccionEntity!=null) {
            for (PuestoEntity entity : puestoService.findPuestosByIdSeccion(selectedSeccionEntity.getId())) {
                o = new Object[3];
                o[0] = entity.getId();
                o[1] = entity.getNombre();
                o[2] = entity.getSeccion();
                model.addRow(o);
            }
        }
        revalidate();
    }
    public void setSelectedSeccion(SeccionEntity entity){
        selectedSeccionEntity=entity;
        fillPuestos();
    }
    private void addPuesto(){
        if(selectedSeccionEntity==null){
            WebOptionPane.showMessageDialog(this,
                    "No hay seleccionada ninguna sección.",
                    "Error",
                    WebOptionPane.ERROR_MESSAGE
            );
            return;
        }
        try{
            puestoService.createPuesto(null,"",selectedSeccionEntity);
        }catch(Exception e){
            WebOptionPane.showMessageDialog(this,
                    "Error al agregar registro.",
                    "Error",
                    WebOptionPane.ERROR_MESSAGE
            );
            return;
        }
        fillPuestos();
        table.setSelectedRow(table.getRowCount()-1);
        firePropertyChange(UPDATE_EVENT,1,0);
        EntityListenerManager.fireEntityUpdated(PuestoEntity.class);
    }
    private void editPuesto(){
        int row=table.getSelectedRow();
        PuestoEntity entity=getSelectedPuesto();
        if(entity!=null){
            try {
                puestoService.updatePuesto(entity.getId(), entity.getNombre(),entity.getSeccion());
            }catch(Exception e){
                WebOptionPane.showMessageDialog(this,
                        "Error al editar registro.",
                        "Error",
                        WebOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }
        table.setSelectedRow(row);
        firePropertyChange(UPDATE_EVENT,1,0);
        EntityListenerManager.fireEntityUpdated(PuestoEntity.class);
    }
    private void deletePuesto(){
        PuestoEntity entity=getSelectedPuesto();
        Integer selectedRow=table.getSelectedRow();
        if(entity!=null){
            try {
                puestoService.removePuesto(entity.getId());
                firePropertyChange(UPDATE_EVENT,1,0);
            }catch(Exception e){
                WebOptionPane.showMessageDialog(this,
                        "Error al eliminar registro.",
                        "Error",
                        WebOptionPane.ERROR_MESSAGE
                );
            }
            EntityListenerManager.fireEntityUpdated(PuestoEntity.class);
            fillPuestos();
        }
        if(table.getRowCount()>selectedRow){
            table.setSelectedRow(selectedRow);
        }else if (selectedRow>0){
            table.setSelectedRow(selectedRow-1);
        }
    }
    private PuestoEntity getSelectedPuesto(){
        PuestoEntity entity=null;
        Integer row=table.getSelectedRow();
        if(row!=-1){
            entity=new PuestoEntity();
            entity.setId((Long) table.getValueAt(row,table.convertColumnIndexToView(0)));
            entity.setNombre((String) table.getValueAt(row,table.convertColumnIndexToView(1)));
            entity.setSeccion((SeccionEntity) table.getValueAt(row,table.convertColumnIndexToView(2)));
        }
        return entity;
    }

}
