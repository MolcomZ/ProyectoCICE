package gui.personal.filtros.puestos;

import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.splitpane.WebSplitPane;
import gui.personal.editors.puestos.EditorPuestosFrame;
import jpa.puestos.PuestoEntity;
import jpa.puestos.PuestoService;
import jpa.secciones.SeccionEntity;
import jpa.secciones.SeccionService;
import net.miginfocom.swing.MigLayout;
import util.InclusionFilter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FiltroPuestosPane extends WebPanel {
    private MigLayout layout;
    private WebSplitPane splitPane;
    private WebButton editButton;

    // SECCIONES
    private WebPanel seccionPane;
    private MigLayout seccionLayout;
    private WebPanel seccionTitlePane;
    private WebScrollPane seccionScroll;
    private SeccionTable seccionTable;
    private DefaultTableModel seccionModel;
    // PUESTOS
    private WebPanel puestoPane;
    private MigLayout puestoLayout;
    private WebPanel puestoTitlePane;
    private WebScrollPane puestoScroll;
    private PuestoTable puestoTable;
    private DefaultTableModel puestoModel;
    //PERSISTENCE
    private EntityManagerFactory EMF;
    private EntityManager EM;
    private SeccionService seccionService;
    private PuestoService puestoService;

    private EditorPuestosFrame editor;

    public FiltroPuestosPane(){
        layout=new MigLayout("","[GROW]","[][GROW]");
        this.setLayout(layout);

        splitPane=new WebSplitPane();
        editButton=new WebButton("EDITAR");

        seccionPane=new WebPanel();
        seccionLayout=new MigLayout("","[GROW]","[]");
        seccionPane.setLayout(seccionLayout);
        seccionTitlePane=new WebPanel();
        seccionTitlePane.setUndecorated(false);
        seccionTitlePane.setRound(4);
        seccionTitlePane.setLayout(new FlowLayout());
        seccionTitlePane.add(new WebLabel("SECCIONES"));
        seccionModel=new DefaultTableModel();
        seccionTable=new SeccionTable(seccionModel);
        seccionScroll=new WebScrollPane(seccionTable);
        seccionPane.add(seccionTitlePane,"GROWX,WRAP");
        seccionPane.add(seccionScroll,"GROW");

        puestoPane=new WebPanel();
        puestoLayout=new MigLayout("","[GROW]","[]");
        puestoPane.setLayout(puestoLayout);
        puestoTitlePane=new WebPanel();
        puestoTitlePane.setUndecorated(false);
        puestoTitlePane.setRound(4);
        puestoTitlePane.setLayout(new FlowLayout());
        puestoTitlePane.add(new WebLabel("PUESTOS"));
        puestoModel=new DefaultTableModel();
        puestoTable=new PuestoTable(puestoModel);
        puestoScroll=new WebScrollPane(puestoTable);
        puestoPane.add(puestoTitlePane,"GROWX,WRAP");
        puestoPane.add(puestoScroll,"GROW");

        splitPane.setLeftComponent(seccionPane);
        splitPane.setRightComponent(puestoPane);
        this.add(editButton,"RIGHT,WRAP");
        this.add(splitPane,"GROW,WRAP");

        editor=new EditorPuestosFrame();

        EMF=Persistence.createEntityManagerFactory("MySQL_JPA");
        EM=EMF.createEntityManager();
        seccionService=new SeccionService(EM);
        puestoService=new PuestoService(EM);

        configTable();
        fillSecciones();
        fillPuestos();
        initListeners();
    }
    private void initListeners(){
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.showFrame();
            }
        });
        seccionTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if(e.getType()==TableModelEvent.UPDATE){
                    printFilterPrueba();
                    revalidate();
                    fireUpdate();
                }
            }
        });
        puestoTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if(e.getType()==TableModelEvent.UPDATE){
                    fireUpdate();
                }
            }
        });
    }
    private void configTable(){
        seccionModel.setColumnCount(2);
        seccionModel.setColumnIdentifiers(new String[]{"","ID","NOMBRE"});

        puestoModel.setColumnCount(3);
        puestoModel.setColumnIdentifiers(new String[]{"","ID","NOMBRE","SECCION"});
        SeccionCellRenderer seccionCellRenderer=new SeccionCellRenderer();
        puestoTable.getColumn("SECCION").setCellRenderer(seccionCellRenderer);
        //SeccionCellEditor seccionEditor=new SeccionCellEditor();
        //puestoTable.getColumn("SECCION").setCellEditor(seccionEditor);
//        updateSeccionEditor();
    }
    private void updateSeccionEditor(){
        SeccionCellEditor seccionEditor= (SeccionCellEditor) puestoTable.getColumn("SECCION").getCellEditor();
        seccionEditor.setList((ArrayList<SeccionEntity>)seccionService.findAllSecciones());
    }
    public void fillSecciones(){
        Object o[];
        seccionModel.setRowCount(0);
        for(SeccionEntity entity:seccionService.findAllSecciones()){
            o=new Object[3];
            o[0]=true;
            o[1]=entity.getId();
            o[2]=entity.getNombre();
            seccionModel.addRow(o);
        }
    }
    public void fillPuestos(){
        Object o[];
        puestoModel.setRowCount(0);
        for(PuestoEntity entity:puestoService.findAllPuestos()){
            o=new Object[4];
            o[0]=true;
            o[1]=entity.getId();
            o[2]=entity.getNombre();
            o[3]=entity.getSeccion();
            puestoModel.addRow(o);
        }
    }
    private SeccionEntity getSelectedSeccion(){
        SeccionEntity entity=null;
        Integer row=seccionTable.getSelectedRow();
        if(row!=-1){
            entity=new SeccionEntity();
            entity.setId(Long.parseLong(seccionTable.getValueAt(row,seccionTable.convertColumnIndexToView(1)).toString()));
            entity.setNombre(seccionTable.getValueAt(row,seccionTable.convertColumnIndexToView(2)).toString());
        }
        return entity;
    }
    private PuestoEntity getSelectedPuesto(){
        PuestoEntity entity=null;
        Integer row=puestoTable.getSelectedRow();
        if(row!=-1){
            entity=new PuestoEntity();
            entity.setId(Long.parseLong(puestoTable.getValueAt(row,puestoTable.convertColumnIndexToView(1)).toString()));
            entity.setNombre(puestoTable.getValueAt(row,puestoTable.convertColumnIndexToView(2)).toString());
            entity.setSeccion((SeccionEntity) puestoTable.getValueAt(row,puestoTable.convertColumnIndexToView(3)));
        }
        return entity;
    }
    private void fireUpdate(){
        this.firePropertyChange("DataUpdated",1,0);
    }
    private void printFilterPrueba(){
        Boolean selected;
        Long id;
        InclusionFilter<SeccionEntity> filter=new InclusionFilter<>();

        for(int n=0;n<seccionTable.getRowCount();n++){
            selected=(Boolean)seccionTable.getValueAt(n,seccionTable.convertColumnIndexToView(0));
            id=(Long)seccionTable.getValueAt(n,seccionTable.convertColumnIndexToView(1));
            if(selected){
                filter.add(3,seccionService.findSeccion(id));
            }
        }
        TableRowSorter<TableModel> sorter=new TableRowSorter<>(puestoModel);
        puestoTable.setRowSorter(sorter);
        sorter.setRowFilter(filter);

    }
    public ArrayList<PuestoEntity> getSelectedItems(){
        ArrayList<PuestoEntity> entities=new ArrayList<>();
        System.out.println("TOTAL ROWS: "+puestoTable.getRowCount());
        for(int n=0;n<puestoTable.getRowCount();n++){
            Boolean selected= (Boolean) puestoTable.getValueAt(n,puestoTable.convertColumnIndexToView(0));
            Long id= (Long) puestoTable.getValueAt(n,puestoTable.convertColumnIndexToView(1));
            String nombre= (String) puestoTable.getValueAt(n,puestoTable.convertColumnIndexToView(2));
            SeccionEntity seccion= (SeccionEntity) puestoTable.getValueAt(n,puestoTable.convertColumnIndexToView(3));
            PuestoEntity entity=new PuestoEntity(id,nombre,seccion);
            if(selected) {
                entities.add(entity);
            }
        }
        return entities;
    }

}
