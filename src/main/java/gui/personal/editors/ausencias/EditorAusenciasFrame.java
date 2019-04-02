package gui.personal.editors.ausencias;

import com.alee.laf.button.WebButton;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.scroll.WebScrollPane;
import jpa.ausencias.TiposAusenciaEntity;
import jpa.ausencias.TiposAusenciaService;
import net.miginfocom.swing.MigLayout;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class EditorAusenciasFrame {
    private WebFrame frame;
    private MigLayout layout;
    private WebButton addButton;
    private WebButton removeButton;
    private EditorTiposAusenciaTable table;
    private DefaultTableModel model;
    private WebScrollPane scroll;
    private WebButton closeButton;

    //PERSISTENCE
    private EntityManagerFactory EMF;
    private EntityManager EM;
    private TiposAusenciaService tiposAusenciaService;

    public EditorAusenciasFrame(){
        frame=new WebFrame("Editor de ausencias");
        layout=new MigLayout("","[GROW]","[][GROW][]");
        frame.setLayout(layout);
        model=new DefaultTableModel();
        table=new EditorTiposAusenciaTable(model);
        scroll=new WebScrollPane(table);
        closeButton=new WebButton("CERRAR");
        addButton=new WebButton(new ImageIcon(getClass().getResource("/Add.png")));
        removeButton=new WebButton(new ImageIcon(getClass().getResource("/Delete.png")));

        frame.add(addButton,"SPLIT");
        frame.add(removeButton,"WRAP");
        frame.add(scroll,"GROW,WRAP");
        frame.add(closeButton,"RIGHT");

        EMF=Persistence.createEntityManagerFactory("MySQL_JPA");
        EM=EMF.createEntityManager();
        tiposAusenciaService=new TiposAusenciaService(EM);

        configTable();
        fillTiposAusencias();

        frame.pack();
        initListeners();
    }
    public void showFrame(){
        frame.setVisible(true);
    }
    void fireUpdate(){
        frame.firePropertyChange("DataUpdated",1,0);
    }
    private void configTable(){
        model.setColumnCount(3);
        model.setColumnIdentifiers(new String[]{"ID","NOMBRE","DESCRIPCION"});
    }
    public void fillTiposAusencias(){
        Object o[];
        model.setRowCount(0);
        for(TiposAusenciaEntity entity:tiposAusenciaService.findAllTiposAusencias()){
            o=new Object[3];
            o[0]=entity.getId();
            o[1]=entity.getNombre();
            o[2]=entity.getDescripcion();
            model.addRow(o);
        }
    }
    private void initListeners(){
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTiposAusencia();
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTiposAusencia();
            }
        });
        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if(e.getType()==TableModelEvent.UPDATE){
                    editTiposAusencia();
                }
            }
        });
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });
    }
    private void addTiposAusencia(){
        try {
            tiposAusenciaService.createTiposAusencia(null,"","");
            fireUpdate();
        }catch(Exception e){
            WebOptionPane.showMessageDialog(frame,
                    "Error al agregar registro.",
                    "Error",
                    WebOptionPane.ERROR_MESSAGE
            );
        }
        fillTiposAusencias();
        table.setSelectedRow(table.getRowCount()-1);
    }
    private void editTiposAusencia(){
        int row=table.getSelectedRow();
        TiposAusenciaEntity entity=getSelectedTiposAusencia();
        if(entity!=null){
            try {
                tiposAusenciaService.updateTiposAusencia(entity.getId(), entity.getNombre(),entity.getDescripcion());
                fireUpdate();
            }catch(Exception e){
                WebOptionPane.showMessageDialog(frame,
                        "Error al editar registro.",
                        "Error",
                        WebOptionPane.ERROR_MESSAGE
                );
            }
            fillTiposAusencias();
        }
        table.setSelectedRow(row);
    }
    private void deleteTiposAusencia(){
        TiposAusenciaEntity entity=getSelectedTiposAusencia();
        Integer selectedRow=table.getSelectedRow();
        if(entity!=null){
            try {
                tiposAusenciaService.removeTiposAusencia(entity.getId());
                fireUpdate();
            }catch(Exception e){
                WebOptionPane.showMessageDialog(frame,
                        "Error al eliminar registro.",
                        "Error",
                        WebOptionPane.ERROR_MESSAGE
                );
            }
            fillTiposAusencias();
        }
        if(table.getRowCount()>selectedRow){
            table.setSelectedRow(selectedRow);
        }else if (selectedRow>0){
            table.setSelectedRow(selectedRow-1);
        }
    }
    private TiposAusenciaEntity getSelectedTiposAusencia(){
        TiposAusenciaEntity entity=null;
        Integer row=table.getSelectedRow();
        if(row!=-1){
            entity=new TiposAusenciaEntity();
            entity.setId(Long.parseLong(table.getValueAt(row,table.convertColumnIndexToView(0)).toString()));
            entity.setNombre(table.getValueAt(row,table.convertColumnIndexToView(1)).toString());
            entity.setDescripcion((String) table.getValueAt(row,table.convertColumnIndexToView(2)));
        }
        return entity;
    }
}
