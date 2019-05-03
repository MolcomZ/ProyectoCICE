package gui.personal.filtros.turnos;

import com.alee.laf.button.WebButton;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import gui.personal.editors.turnos.EditorTurnosFrame;
import jpa.turnos.TurnoEntity;
import jpa.turnos.TurnoService;
import net.miginfocom.swing.MigLayout;
import util.EntityListener;
import util.EntityListenerManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FiltroTurnosPane extends WebPanel {
    private MigLayout layout;

    private WebPanel turnoPane;
    private MigLayout turnoLayout;
    private WebScrollPane turnoScroll;
    private TurnoTable turnoTable;
    private DefaultTableModel turnoModel;
    private WebButton editButton;
    private EditorTurnosFrame editor;

    //PERSISTENCE
    private EntityManagerFactory EMF;
    private EntityManager EM;
    private TurnoService turnoService;

    public FiltroTurnosPane(){
        layout=new MigLayout("","[GROW]","[][GROW]");
        this.setLayout(layout);

        turnoPane=new WebPanel();
        turnoLayout=new MigLayout("","[GROW]","[]");
        turnoPane.setLayout(turnoLayout);
        turnoModel=new DefaultTableModel();
        turnoTable=new TurnoTable(turnoModel);
        turnoScroll=new WebScrollPane(turnoTable);
        turnoPane.add(turnoScroll,"GROW");
        editButton=new WebButton(new ImageIcon(getClass().getResource("/Edit.png")));
        editor=new EditorTurnosFrame();

        this.add(editButton,"RIGHT,TOP,WRAP");
        this.add(turnoPane,"GROW,WRAP");

        EMF=Persistence.createEntityManagerFactory("MySQL_JPA");
        EM=EMF.createEntityManager();
        turnoService=new TurnoService(EM);

        configTable();
        fillTurnos();
        initListeners();
    }
    private void initListeners(){
        turnoTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if(e.getType()==TableModelEvent.UPDATE){
                    fireUpdate();
                }
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.showFrame();
            }
        });
        EntityListenerManager.addListener(TurnoEntity.class, new EntityListener() {
            @Override
            public void entityUpdated() {
                fillTurnos();
            }
        });
    }
    private void configTable(){
        turnoModel.setColumnCount(4);
        turnoModel.setColumnIdentifiers(new String[]{"","ID","NOMBRE","DESCRIPCION"});

    }
    public void fillTurnos(){
        Object o[];
        turnoModel.setRowCount(0);
        for(TurnoEntity entity:turnoService.findAllTurnos()){
            EM.refresh(entity);
            o=new Object[4];
            o[0]=true;
            o[1]=entity.getId();
            o[2]=entity.getNombre();
            o[3]=entity.getDescripcion();
            turnoModel.addRow(o);
        }
        revalidate();
    }
    private void fireUpdate(){
        this.firePropertyChange("DataUpdated",1,0);
    }
    public ArrayList<TurnoEntity> getSelectedItems(){
        ArrayList<TurnoEntity> entities=new ArrayList<>();
        for(int n=0;n<turnoTable.getRowCount();n++){
            Boolean selected= (Boolean) turnoTable.getValueAt(n,turnoTable.convertColumnIndexToView(0));
            Long id= (Long) turnoTable.getValueAt(n,turnoTable.convertColumnIndexToView(1));
            String nombre= (String) turnoTable.getValueAt(n,turnoTable.convertColumnIndexToView(2));
            String descripcion= (String) turnoTable.getValueAt(n,turnoTable.convertColumnIndexToView(3));
            //TurnoEntity entity=new TurnoEntity(id,nombre,descripcion);
            if(selected) {
                entities.add(turnoService.findTurno(id));
            }
        }
        return entities;
    }
}
