package gui.personal.gestion_empleado;

import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.splitpane.WebSplitPane;
import com.alee.laf.table.WebTable;
import com.alee.laf.text.WebTextField;
import jpa.empleados.EmpleadoEntity;
import jpa.puestos.PuestoEntity;
import jpa.puestos.PuestoService;
import jpa.secciones.SeccionEntity;
import jpa.secciones.SeccionService;
import net.miginfocom.swing.MigLayout;
import util.AutoSizeableTable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PuestoPane extends WebPanel {
    public static final String PUESTO_CHANGED="Puesto changed";
    private MigLayout layout;
    private WebLabel puestoLabel;
    private WebTextField idPuestoText;
    private WebTextField puestoText;
    private WebSplitPane split;
    private SeccionesTable seccionesTable;
    private DefaultTableModel seccionesModel;
    private WebScrollPane seccionesScroll;
    private PuestosTable puestosTable;
    private DefaultTableModel puestosModel;
    private WebScrollPane puestosScroll;

    //PERSISTENCE
    private EntityManagerFactory EMF;
    private EntityManager EM;
    private PuestoService puestoService;
    private SeccionService seccionService;

    private SeccionEntity selectedSeccionEntity;

    public PuestoPane(){
        layout=new MigLayout("","[GROW]","[][GROW]");
        this.setLayout(layout);
        puestoLabel=new WebLabel("PUESTO:");
        idPuestoText=new WebTextField();
        idPuestoText.setEditable(false);
        puestoText=new WebTextField();
        puestoText.setEditable(false);
        split=new WebSplitPane();
        seccionesModel=new DefaultTableModel();
        seccionesTable= new SeccionesTable(seccionesModel);
        seccionesScroll=new WebScrollPane(seccionesTable);
        split.setLeftComponent(seccionesScroll);
        puestosModel=new DefaultTableModel();
        puestosTable= new PuestosTable(puestosModel);
        puestosScroll=new WebScrollPane(puestosTable);
        split.setRightComponent(puestosScroll);

        this.add(puestoLabel,"SPLIT");
        this.add(idPuestoText,"LEFT,WIDTH 80,SPLIT");
        this.add(puestoText,"LEFT,WIDTH 200,WRAP");
        this.add(split,"GROW");

        EMF=Persistence.createEntityManagerFactory("MySQL_JPA");
        EM=EMF.createEntityManager();
        puestoService=new PuestoService(EM);
        seccionService=new SeccionService(EM);

        configTables();
        fillSecciones();
        initListeners();
    }
    private void initListeners(){
        seccionesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                SeccionEntity entity=getSelectedSeccion();
                if(entity!=null){
                    fillPuestos(entity.getId());
                }
            }
        });
        puestosTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    PuestoEntity puesto=getSelectedPuesto();
                    idPuestoText.setText(puesto.getId().toString());
                    puestoText.setText(puesto.getNombre());
                    firePropertyChange(PUESTO_CHANGED,1,0);
                }
            }
        });
    }
    private void configTables(){
        seccionesModel.setColumnCount(2);
        seccionesModel.setColumnIdentifiers(new String[]{"ID","NOMBRE"});
        puestosModel.setColumnCount(2);
        puestosModel.setColumnIdentifiers(new String[]{"ID","NOMBRE"});
    }
    private void fillSecciones(){
        Object o[];
        seccionesModel.setRowCount(0);
        for (SeccionEntity entity : seccionService.findAllSecciones()) {
            o = new Object[2];
            o[0] = entity.getId();
            o[1] = entity.getNombre();
            seccionesModel.addRow(o);
        }
        revalidate();
    }
    public void fillPuestos(Long id){
        Object o[];
        puestosModel.setRowCount(0);
        for(PuestoEntity entity:puestoService.findPuestosByIdSeccion(id)){
            o=new Object[2];
            o[0]=entity.getId();
            o[1]=entity.getNombre();
            //o[2]=entity.getSeccion();
            puestosModel.addRow(o);
        }
        revalidate();
    }
    public void setEmpleado(EmpleadoEntity empleado){
        Long id;
        for(int n=0;n<seccionesTable.getRowCount();n++){
            id= (Long) seccionesTable.getValueAt(n,0);
            if(id==empleado.getPuesto().getSeccion().getId()){
                seccionesTable.setSelectedRow(n);
                break;
            }
        }
        for(int n=0;n<puestosTable.getRowCount();n++){
            id= (Long) puestosTable.getValueAt(n,0);
            if(id==empleado.getPuesto().getId()){
                puestosTable.setSelectedRow(n);
            }
        }
        idPuestoText.setText(empleado.getPuesto().getId().toString());
        puestoText.setText(empleado.getPuesto().getNombre());
    }
    private SeccionEntity getSelectedSeccion(){
        SeccionEntity entity=null;
        Integer row=seccionesTable.getSelectedRow();
        if(row!=-1){
            entity=new SeccionEntity();
            entity.setId(Long.parseLong(seccionesTable.getValueAt(row,seccionesTable.convertColumnIndexToView(0)).toString()));
            entity.setNombre(seccionesTable.getValueAt(row,seccionesTable.convertColumnIndexToView(1)).toString());
        }
        return entity;
    }
    private PuestoEntity getSelectedPuesto(){
        PuestoEntity entity=null;
        Integer row=puestosTable.getSelectedRow();
        if(row!=-1){
            entity=new PuestoEntity();
            entity.setId(Long.parseLong(puestosTable.getValueAt(row,puestosTable.convertColumnIndexToView(0)).toString()));
            entity.setNombre(puestosTable.getValueAt(row,puestosTable.convertColumnIndexToView(1)).toString());
        }
        return entity;
    }

    /**Devuelve un <code>PuestoEntity</code>que corresponde al puesto asignado al empleado.
     * El puesto lo busca a través del <i>id</i> indicado en la interfaz. Si el <i>id</i> no
     * es válido o no se encuentra en la base de datos un puesto con ese <i>id</i> devuelve un
     * <code>PuestoEntity</code> que contiene <i>null</i>.
     * @return <code>PuestoEntity</code>
     */
    public PuestoEntity getPuesto(){
        PuestoEntity puesto=null;
        try{
            Long id=Long.parseLong(idPuestoText.getText());
            puesto=puestoService.findPuesto(id);
        }catch(NumberFormatException e){
        }
        return puesto;
    }
}
