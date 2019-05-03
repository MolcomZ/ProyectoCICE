package gui.personal.editors.puestos;

import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import gui.PrincipalFrame;
import jpa.secciones.SeccionEntity;
import jpa.secciones.SeccionService;
import net.miginfocom.swing.MigLayout;
import util.AutoSizeableTable;
import util.EntityListenerManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.Principal;

public class EditorSeccionesPane extends WebPanel {
    public static final String UPDATE_EVENT="UPDATED";
    public static final String SELECTION_CHANGE_EVENT="SELECTION_CHANGED";

    private MigLayout layout;
    private WebPanel titlePane;
    private WebButton addButton;
    private WebButton removeButton;
    private AutoSizeableTable table;
    private WebScrollPane scroll;
    private DefaultTableModel model;

    private SeccionService seccionService;

    public EditorSeccionesPane(){
        layout=new MigLayout("","[GROW]","[][][GROW]");
        setLayout(layout);
        addButton=new WebButton(new ImageIcon(getClass().getResource("/Add.png")));
        removeButton=new WebButton(new ImageIcon(getClass().getResource("/Remove.png")));
        titlePane=new WebPanel();
        titlePane.setUndecorated(false);
        titlePane.setRound(4);
        titlePane.setLayout(new FlowLayout());
        titlePane.add(new WebLabel("SECCIONES"));
        model=new DefaultTableModel();
        table=new AutoSizeableTable(model);
        scroll=new WebScrollPane(table);

        add(addButton,"SPLIT");
        add(removeButton,"WRAP");
        add(titlePane,"GROWX,WRAP");
        add(scroll,"GROW,WRAP");

        seccionService=new SeccionService(PrincipalFrame.EM);

        configTable();
        fillSecciones();
        initListeners();
    }
    public void initListeners(){
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSeccion();
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSeccion();
            }
        });
        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if(e.getType()==TableModelEvent.UPDATE){
                    editSeccion();
                }
            }
        });
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                SeccionEntity entity=getSelectedSeccion();
                if(entity!=null){
                    firePropertyChange(SELECTION_CHANGE_EVENT,1,0);
                }
            }
        });
    }
    private void configTable(){
        model.setColumnCount(3);
        model.setColumnIdentifiers(new String[]{"ID","NOMBRE"});
    }
    public void fillSecciones(){
        Object o[];
        model.setRowCount(0);
        for(SeccionEntity entity:seccionService.findAllSecciones()){
            o=new Object[2];
            o[0]=entity.getId();
            o[1]=entity.getNombre();
            model.addRow(o);
        }
        revalidate();
    }
    private void addSeccion(){
        try {
            seccionService.createSeccion(null, "");
            firePropertyChange(UPDATE_EVENT,1,0);
        }catch(Exception e){
            WebOptionPane.showMessageDialog(this,
                    "Error al agregar registro.",
                    "Error",
                    WebOptionPane.ERROR_MESSAGE
            );
        }
        EntityListenerManager.fireEntityUpdated(SeccionEntity.class);
        fillSecciones();
        table.setSelectedRow(table.getRowCount()-1);
    }
    private void editSeccion(){
        int row=table.getSelectedRow();
        SeccionEntity entity=getSelectedSeccion();
        if(entity!=null){
            try {
                seccionService.updateSeccion(entity.getId(), entity.getNombre());
                firePropertyChange(UPDATE_EVENT,1,0);
                EntityListenerManager.fireEntityUpdated(SeccionEntity.class);
            }catch(Exception e){
                WebOptionPane.showMessageDialog(this,
                        "Error al editar registro.",
                        "Error",
                        WebOptionPane.ERROR_MESSAGE
                );
            }
            fillSecciones();
        }
        table.setSelectedRow(row);
    }
    private void deleteSeccion(){
        SeccionEntity entity=getSelectedSeccion();
        Integer selectedRow=table.getSelectedRow();
        if(entity!=null){
            try {
                seccionService.removeSeccion(entity.getId());
                firePropertyChange(UPDATE_EVENT,1,0);
                EntityListenerManager.fireEntityUpdated(SeccionEntity.class);
            }catch(Exception e){
                WebOptionPane.showMessageDialog(this,
                        "Error al eliminar registro.",
                        "Error",
                        WebOptionPane.ERROR_MESSAGE
                );
            }
            fillSecciones();
        }
        if(table.getRowCount()>selectedRow){
            table.setSelectedRow(selectedRow);
        }else if (selectedRow>0){
            table.setSelectedRow(selectedRow-1);
        }

    }
    public SeccionEntity getSelectedSeccion(){
        SeccionEntity entity=null;
        Integer row=table.getSelectedRow();
        if(row!=-1){
            entity=new SeccionEntity();
            entity.setId(Long.parseLong(table.getValueAt(row,table.convertColumnIndexToView(0)).toString()));
            entity.setNombre(table.getValueAt(row,table.convertColumnIndexToView(1)).toString());
        }
        return entity;
    }

}
