package gui.transporte.editors.empresas_transporte;

import com.alee.laf.button.WebButton;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.scroll.WebScrollPane;
import gui.PrincipalFrame;
import jpa.empresas_transporte.EmpresasTransporteEntity;
import jpa.empresas_transporte.EmpresasTransporteService;
import net.miginfocom.swing.MigLayout;
import util.EntityListenerManager;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class EditorEmpresasTransporteFrame {
    private WebFrame frame;
    private MigLayout layout;
    private WebButton addButton;
    private WebButton removeButton;
    private EditorEmpresasTransporteTable table;
    private DefaultTableModel model;
    private WebScrollPane scroll;
    private WebButton closeButton;

    //PERSISTENCE
    private EmpresasTransporteService service;

    public EditorEmpresasTransporteFrame(){
        frame=new WebFrame("Editor de empresas de transporte");
        layout=new MigLayout("","[GROW]","[][GROW][]");
        frame.setLayout(layout);
        model=new DefaultTableModel();
        table=new EditorEmpresasTransporteTable(model);
        scroll=new WebScrollPane(table);
        closeButton=new WebButton("CERRAR");
        addButton=new WebButton(new ImageIcon(getClass().getResource("/Add.png")));
        removeButton=new WebButton(new ImageIcon(getClass().getResource("/Remove.png")));

        frame.add(addButton,"SPLIT");
        frame.add(removeButton,"WRAP");
        frame.add(scroll,"GROW,WRAP");
        frame.add(closeButton,"RIGHT");

        service=new EmpresasTransporteService(PrincipalFrame.EM);

        configTable();
        fillTable();

        frame.pack();
        initListeners();
    }
    public void showFrame(){
        frame.setVisible(true);
    }
    //void fireUpdate(){
    //    frame.firePropertyChange("DataUpdated",1,0);
    //}
    private void configTable(){
        model.setColumnCount(4);
        model.setColumnIdentifiers(new String[]{"ID","NOMBRE","ALIAS","NIF"});
    }
    public void fillTable(){
        Object o[];
        model.setRowCount(0);
        for(EmpresasTransporteEntity entity:service.findAllEmpresasTransporte()){
            o=new Object[4];
            o[0]=entity.getId();
            o[1]=entity.getNombre();
            o[2]=entity.getAlias();
            o[3]=entity.getNif();
            model.addRow(o);
        }
    }
    private void initListeners(){
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEmpresa();
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
                    editEmpresa();
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
    private void addEmpresa(){
        try {
            service.createEmpresaTransporte("","","");
        }catch(Exception e){
            WebOptionPane.showMessageDialog(frame,
                    "Error al agregar registro.",
                    "Error",
                    WebOptionPane.ERROR_MESSAGE
            );
        }
        EntityListenerManager.fireEntityUpdated(EmpresasTransporteEntity.class);
        fillTable();
        table.setSelectedRow(table.getRowCount()-1);
    }
    private void editEmpresa(){
        int row=table.getSelectedRow();
        EmpresasTransporteEntity entity=getSelectedEmpresa();
        if(entity!=null){
            try {
                service.updateEmpresaTransporte(entity.getId(),
                        entity.getNombre(),
                        entity.getAlias(),
                        entity.getNif());
            }catch(Exception e){
                WebOptionPane.showMessageDialog(frame,
                        "Error al editar registro.",
                        "Error",
                        WebOptionPane.ERROR_MESSAGE
                );
            }
            EntityListenerManager.fireEntityUpdated(EmpresasTransporteEntity.class);
            fillTable();
        }
        table.setSelectedRow(row);
    }
    private void deleteTiposAusencia(){
        EmpresasTransporteEntity entity=getSelectedEmpresa();
        Integer selectedRow=table.getSelectedRow();
        if(entity!=null){
            try {
                service.removeEmpresaTransporte(entity.getId());
            }catch(Exception e){
                WebOptionPane.showMessageDialog(frame,
                        "Error al eliminar registro.",
                        "Error",
                        WebOptionPane.ERROR_MESSAGE
                );
            }
            EntityListenerManager.fireEntityUpdated(EmpresasTransporteEntity.class);
            fillTable();
        }
        if(table.getRowCount()>selectedRow){
            table.setSelectedRow(selectedRow);
        }else if (selectedRow>0){
            table.setSelectedRow(selectedRow-1);
        }
    }
    private EmpresasTransporteEntity getSelectedEmpresa(){
        EmpresasTransporteEntity entity=null;
        Integer row=table.getSelectedRow();
        if(row!=-1){
            entity=new EmpresasTransporteEntity();
            entity.setId(Long.parseLong(table.getValueAt(row,table.convertColumnIndexToView(0)).toString()));
            entity.setNombre(table.getValueAt(row,table.convertColumnIndexToView(1)).toString());
            entity.setAlias((String) table.getValueAt(row,table.convertColumnIndexToView(2)));
            entity.setNif((String) table.getValueAt(row,table.convertColumnIndexToView(3)));
        }
        return entity;
    }
}
