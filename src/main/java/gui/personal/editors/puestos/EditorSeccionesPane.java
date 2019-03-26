package gui.personal.editors.puestos;

import com.alee.laf.button.WebButton;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.splitpane.WebSplitPane;
import jpa.secciones.SeccionEntity;
import jpa.secciones.SeccionService;
import net.miginfocom.swing.MigLayout;
import util.AutoSizeableTable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class EditorSeccionesPane extends WebPanel {
    private MigLayout layout;
    private WebButton addButton;
    private WebButton removeButton;
    private AutoSizeableTable table;
    private WebScrollPane scroll;
    private DefaultTableModel model;

    //PERSISTENCE
    private EntityManagerFactory EMF;
    private EntityManager EM;
    private SeccionService seccionService;

    public EditorSeccionesPane(){
        layout=new MigLayout("","[GROW]","[][GROW]");
        setLayout(layout);
        addButton=new WebButton(new ImageIcon(getClass().getResource("/Add.png")));
        removeButton=new WebButton(new ImageIcon(getClass().getResource("/Delete.png")));
        model=new DefaultTableModel();
        table=new AutoSizeableTable(model);
        scroll=new WebScrollPane(table);

        add(addButton,"SPLIT");
        add(removeButton,"WRAP");
        add(scroll,"GROW,WRAP");

        EMF=Persistence.createEntityManagerFactory("MySQL_JPA");
        EM=EMF.createEntityManager();
        seccionService=new SeccionService(EM);

        configTable();
        fillSecciones();
    }
    void fireUpdate(){
        firePropertyChange("DataUpdated",1,0);
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
    }

}
