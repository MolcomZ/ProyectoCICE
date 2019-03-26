package gui.personal.editors.turnos;

import com.alee.laf.button.WebButton;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.scroll.WebScrollPane;
import jpa.turnos.TurnoEntity;
import jpa.turnos.TurnoService;
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

public class EditorTurnosFrame {
    private WebFrame frame;
    private MigLayout layout;
    private WebButton addButton;
    private WebButton removeButton;
    private EditorTurnosTable table;
    private DefaultTableModel model;
    private WebScrollPane scroll;
    private WebButton closeButton;

    //PERSISTENCE
    private EntityManagerFactory EMF;
    private EntityManager EM;
    private TurnoService turnoService;

    public EditorTurnosFrame(){
        frame=new WebFrame("Editor de turnos");
        layout=new MigLayout("","[GROW]","[][GROW][]");
        frame.setLayout(layout);
        model=new DefaultTableModel();
        table=new EditorTurnosTable(model);
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
        turnoService=new TurnoService(EM);

        configTable();
        fillTurnos();

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
    public void fillTurnos(){
        Object o[];
        model.setRowCount(0);
        for(TurnoEntity entity:turnoService.findAllTurnos()){
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
                addTurno();
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTurno();
            }
        });
        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if(e.getType()==TableModelEvent.UPDATE){
                    editTurno();
                }
            }
        });
    }
    private void addTurno(){
        try {
            turnoService.createTurno(null,"","");
            fireUpdate();
        }catch(Exception e){
            WebOptionPane.showMessageDialog(frame,
                    "Error al agregar registro.",
                    "Error",
                    WebOptionPane.ERROR_MESSAGE
            );
        }
        fillTurnos();
        table.setSelectedRow(table.getRowCount()-1);
    }
    private void editTurno(){
        int row=table.getSelectedRow();
        TurnoEntity entity=getSelectedTurno();
        if(entity!=null){
            try {
                turnoService.updateTurno(entity.getId(), entity.getNombre(),entity.getDescripcion());
                fireUpdate();
            }catch(Exception e){
                WebOptionPane.showMessageDialog(frame,
                        "Error al editar registro.",
                        "Error",
                        WebOptionPane.ERROR_MESSAGE
                );
            }
            fillTurnos();
        }
        table.setSelectedRow(row);
    }
    private void deleteTurno(){
        TurnoEntity entity=getSelectedTurno();
        Integer selectedRow=table.getSelectedRow();
        if(entity!=null){
            try {
                turnoService.removeTurno(entity.getId());
                fireUpdate();
            }catch(Exception e){
                WebOptionPane.showMessageDialog(frame,
                        "Error al eliminar registro.",
                        "Error",
                        WebOptionPane.ERROR_MESSAGE
                );
            }
            fillTurnos();
        }
        if(table.getRowCount()>selectedRow){
            table.setSelectedRow(selectedRow);
        }else if (selectedRow>0){
            table.setSelectedRow(selectedRow-1);
        }
    }
    private TurnoEntity getSelectedTurno(){
        TurnoEntity entity=null;
        Integer row=table.getSelectedRow();
        if(row!=-1){
            entity=new TurnoEntity();
            entity.setId(Long.parseLong(table.getValueAt(row,table.convertColumnIndexToView(0)).toString()));
            entity.setNombre(table.getValueAt(row,table.convertColumnIndexToView(1)).toString());
            entity.setDescripcion((String) table.getValueAt(row,table.convertColumnIndexToView(2)));
        }
        return entity;
    }
}
