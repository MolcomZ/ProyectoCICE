package gui;

import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.table.WebTable;
import jpa.comunidades_autonomas.ComunidadEntity;
import jpa.comunidades_autonomas.ComunidadService;
import jpa.localidades.LocalidadEntity;
import jpa.localidades.LocalidadService;
import jpa.provincias.ProvinciaEntity;
import jpa.provincias.ProvinciaService;
import net.miginfocom.swing.MigLayout;
import util.AutoSizeableTable;
import util.PropertiesManager;

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
import java.beans.PropertyChangeListener;

public class GestionLocalidadesFrame implements PropertiesManager {
    public static final String LEFT="GestionLocalidadesFrame.LEFT";
    public static final String TOP="GestionLocalidadesFrame.TOP";
    public static final String WIDTH="GestionLocalidadesFrame.WIDTH";
    public static final String HEIGHT="GestionLocalidadesFrame.HEIGHT";

    WebFrame frame;
    MigLayout layout;
    
    WebButton addComunidadButton;
    WebButton deleteComunidadButton;
    WebPanel comunidadPanel;
    WebLabel comunidadLabel;
    WebScrollPane tableComunidadScrollPane;
    AutoSizeableTable comunidadTable;
    DefaultTableModel comunidadModel;

    WebButton addProvinciaButton;
    WebButton deleteProvinciaButton;
    WebPanel provinciaPanel;
    WebScrollPane tableProvinciaScrollPane;
    AutoSizeableTable provinciaTable;
    DefaultTableModel provinciaModel;

    WebButton addLocalidadButton;
    WebButton deleteLocalidadButton;
    WebPanel localidadPanel;
    WebScrollPane tableLocalidadScrollPane;
    AutoSizeableTable localidadTable;
    DefaultTableModel localidadModel;
    
    WebButton updateButton;
    WebButton closeButton;

    //PERSISTENCE
    private EntityManagerFactory EMF;
    private EntityManager EM;
    private ComunidadService comunidadService;
    private ProvinciaService provinciaService;
    private LocalidadService localidadService;

    public GestionLocalidadesFrame(){
        frame=new WebFrame("Gestión localidades");
        layout=new MigLayout("","[][GROW][][GROW][][GROW]","[][]0[GROW][]");
        frame.setLayout(layout);

        addComunidadButton=new WebButton(new ImageIcon(getClass().getResource("/Add.png")));
        deleteComunidadButton=new WebButton(new ImageIcon(getClass().getResource("/Delete.png")));
        updateButton=new WebButton(new ImageIcon(getClass().getResource("/Refresh.png")));
        comunidadPanel=new WebPanel();
        comunidadPanel.setUndecorated(false);
        comunidadPanel.setRound(4);
        comunidadPanel.setLayout(new FlowLayout());
        comunidadPanel.add(new WebLabel("COMUNIDADES AUTÓNOMAS"));
        comunidadModel=new DefaultTableModel();
        comunidadTable=new AutoSizeableTable(comunidadModel);
        tableComunidadScrollPane=new WebScrollPane(comunidadTable);

        addProvinciaButton=new WebButton(new ImageIcon(getClass().getResource("/Add.png")));
        deleteProvinciaButton=new WebButton(new ImageIcon(getClass().getResource("/Delete.png")));
        provinciaPanel=new WebPanel();
        provinciaPanel.setUndecorated(false);
        provinciaPanel.setRound(4);
        provinciaPanel.setLayout(new FlowLayout());
        provinciaPanel.add(new WebLabel("PROVINCIAS"));
        provinciaModel=new DefaultTableModel();
        provinciaTable=new AutoSizeableTable(provinciaModel);
        tableProvinciaScrollPane=new WebScrollPane(provinciaTable);

        addLocalidadButton=new WebButton(new ImageIcon(getClass().getResource("/Add.png")));
        deleteLocalidadButton=new WebButton(new ImageIcon(getClass().getResource("/Delete.png")));
        localidadPanel=new WebPanel();
        localidadPanel.setUndecorated(false);
        localidadPanel.setRound(4);
        localidadPanel.setLayout(new FlowLayout());
        localidadPanel.add(new WebLabel("LOCALIDADES"));
        localidadModel=new DefaultTableModel();
        localidadTable=new AutoSizeableTable(localidadModel);
        tableLocalidadScrollPane=new WebScrollPane(localidadTable);
        
        closeButton=new WebButton("CERRAR");
        frame.add(addComunidadButton,"SPLIT 2");
        frame.add(deleteComunidadButton,"LEFT");
        frame.add(addProvinciaButton,"CELL 2 0,SPLIT 2");
        frame.add(deleteProvinciaButton,"LEFT");
        frame.add(addLocalidadButton,"CELL 4 0,SPLIT 2");
        frame.add(deleteLocalidadButton,"LEFT");
        frame.add(updateButton,"RIGHT,WRAP");
        frame.add(comunidadPanel,"SPAN 2,GROWX,GAPBOTTOM 0");
        frame.add(provinciaPanel,"SPAN 2,GROWX");
        frame.add(localidadPanel,"SPAN 2,GROWX,WRAP");
        frame.add(tableComunidadScrollPane,"SPAN 2,GROW,GAPTOP 0");
        frame.add(tableProvinciaScrollPane,"SPAN 2,GROW");
        frame.add(tableLocalidadScrollPane,"SPAN 2,WRAP,GROW");
        frame.add(closeButton,"CELL 5 3,RIGHT");

        EMF=Persistence.createEntityManagerFactory("MySQL_JPA");
        EM=EMF.createEntityManager();
        comunidadService=new ComunidadService(EM);
        provinciaService=new ProvinciaService(EM);
        localidadService=new LocalidadService(EM);

        configTable();
        fillComunidades();
        frame.setPreferredSize(new Dimension(500,400));
        frame.pack();
        initListeners();
    }
    private void configTable(){
        comunidadTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        comunidadModel.setColumnCount(2);
        comunidadModel.setColumnIdentifiers(new String[]{"ID","NOMBRE"});
        provinciaTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        provinciaModel.setColumnCount(3);
        provinciaModel.setColumnIdentifiers(new String[]{"ID","NOMBRE","COMUNIDAD"});
        provinciaTable.removeColumn(provinciaTable.getColumn("COMUNIDAD"));
        localidadTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        localidadModel.setColumnCount(3);
        localidadModel.setColumnIdentifiers(new String[]{"ID","NOMBRE","PROVINCIA"});
        localidadTable.removeColumn(localidadTable.getColumn("PROVINCIA"));
    }
    public void fillComunidades(){
        Object o[];
        comunidadModel.setRowCount(0);
        for(ComunidadEntity entity:comunidadService.findAllComunidades()){
            o=new Object[2];
            o[0]=entity.getId();
            o[1]=entity.getNombre();
            comunidadModel.addRow(o);
        }
        provinciaModel.setRowCount(0);
        localidadModel.setRowCount(0);
    }
    public void fillProvincias(Long comunidadid){
        Object o[];
        provinciaModel.setRowCount(0);
        for(ProvinciaEntity entity:provinciaService.findProvinciasByIdComunidad(comunidadid)){
            o=new Object[3];
            o[0]=entity.getId();
            o[1]=entity.getNombre();
            o[2]=entity.getComunidad();
            provinciaModel.addRow(o);
        }
        localidadModel.setRowCount(0);
    }
    public void fillLocalidades(Long provinciaid){
        Object o[];
        localidadModel.setRowCount(0);
        for(LocalidadEntity entity:localidadService.findLocalidadesByIdProvincia(provinciaid)){
            o=new Object[3];
            o[0]=entity.getId();
            o[1]=entity.getNombre();
            o[2]=entity.getProvincia();
            localidadModel.addRow(o);
        }
    }
    public void initListeners(){
        addComunidadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addComunidad();
            }
        });
        deleteComunidadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteComunidad();
            }
        });
        addProvinciaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProvincia();
            }
        });
        deleteProvinciaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProvincia();
            }
        });
        addLocalidadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addLocalidad();
            }
        });
        deleteLocalidadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteLocalidad();
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });

        comunidadTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                ComunidadEntity entity=getSelectedComunidad();
                if(entity!=null){
                    fillProvincias(entity.getId());
                }
            }
        });
        comunidadTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if(e.getType()==TableModelEvent.UPDATE){
                    //editComunidad(comunidadTable.getSelectedRow());
                    editComunidad();
                }
            }
        });
        provinciaTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                ProvinciaEntity entity=getSelectedProvincia();
                if(entity!=null){
                    fillLocalidades(entity.getId());
                }
            }
        });
        provinciaTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if(e.getType()==TableModelEvent.UPDATE){
                    editProvincia();
                }
            }
        });
        localidadTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if(e.getType()==TableModelEvent.UPDATE){
                    editLocalidad();
                }
            }
        });
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProperties();
                frame.setVisible(false);
            }
        });
    }
    public void showFrame(){
        update();
        loadProperties();
        frame.setVisible(true);
    }
    private void addComunidad(){
        try {
            comunidadService.createComunidad(null, "");
            fireUpdate();
        }catch(Exception e){
            WebOptionPane.showMessageDialog(frame,
                    "Error al agregar registro.",
                    "Error",
                    WebOptionPane.ERROR_MESSAGE
            );
        }
        fillComunidades();
        comunidadTable.setSelectedRow(comunidadTable.getRowCount()-1);
    }
    private void editComunidad(){
        int row=comunidadTable.getSelectedRow();
        ComunidadEntity entity=getSelectedComunidad();
        if(entity!=null){
            try {
                comunidadService.updateComunidad(entity.getId(), entity.getNombre());
                fireUpdate();
            }catch(Exception e){
                WebOptionPane.showMessageDialog(frame,
                        "Error al editar registro.",
                        "Error",
                        WebOptionPane.ERROR_MESSAGE
                );
            }
            fillComunidades();
        }
        comunidadTable.setSelectedRow(row);
    }
    private void deleteComunidad(){
        ComunidadEntity entity=getSelectedComunidad();
        Integer selectedRow=comunidadTable.getSelectedRow();
        if(entity!=null){
            try {
                comunidadService.removeComunidad(entity.getId());
                fireUpdate();
            }catch(Exception e){
                WebOptionPane.showMessageDialog(frame,
                        "Error al eliminar registro.",
                        "Error",
                        WebOptionPane.ERROR_MESSAGE
                );
            }
            fillComunidades();
        }
        if(comunidadTable.getRowCount()>selectedRow){
            comunidadTable.setSelectedRow(selectedRow);
        }else if (selectedRow>0){
            comunidadTable.setSelectedRow(selectedRow-1);
        }

    }
    private void addProvincia(){
        ComunidadEntity comunidadEntity=getSelectedComunidad();
        if(comunidadEntity==null){
            WebOptionPane.showMessageDialog(frame,
                    "No hay seleccionada ninguna comunidad autónoma.",
                    "Error",
                    WebOptionPane.ERROR_MESSAGE
            );
            return;
        }
        try{
            provinciaService.createProvincia(null,"",comunidadEntity);
            fireUpdate();
        }catch(Exception e){
            WebOptionPane.showMessageDialog(frame,
                    "Error al agregar registro.",
                    "Error",
                    WebOptionPane.ERROR_MESSAGE
            );
        }
        fillProvincias(comunidadEntity.getId());
        provinciaTable.setSelectedRow(provinciaTable.getRowCount()-1);
    }
    private void editProvincia(){
        int row=provinciaTable.getSelectedRow();
        ProvinciaEntity entity=getSelectedProvincia();
        if(entity!=null){
            System.out.println("editProvincia: "+entity.getId());
            try {
                provinciaService.updateProvincia(entity.getId(), entity.getNombre());
                fireUpdate();
            }catch(Exception e){
                WebOptionPane.showMessageDialog(frame,
                        "Error al editar registro.",
                        "Error",
                        WebOptionPane.ERROR_MESSAGE
                );
                e.printStackTrace();
            }
            fillProvincias(entity.getComunidad().getId());
        }
        provinciaTable.setSelectedRow(row);
    }
    private void deleteProvincia(){
        ProvinciaEntity entity=getSelectedProvincia();
        Integer selectedRow=provinciaTable.getSelectedRow();
        if(entity!=null){
            try {
                provinciaService.removeProvincia(entity.getId());
                fireUpdate();
            }catch(Exception e){
                WebOptionPane.showMessageDialog(frame,
                        "Error al eliminar registro.",
                        "Error",
                        WebOptionPane.ERROR_MESSAGE
                );
            }
            fillProvincias(entity.getComunidad().getId());
        }
        if(provinciaTable.getRowCount()>selectedRow){
            provinciaTable.setSelectedRow(selectedRow);
        }else if (selectedRow>0){
            provinciaTable.setSelectedRow(selectedRow-1);
        }
    }
    private void addLocalidad(){
        ProvinciaEntity provinciaEntity=getSelectedProvincia();
        if(provinciaEntity==null){
            WebOptionPane.showMessageDialog(frame,
                    "No hay seleccionada ninguna provincia.",
                    "Error",
                    WebOptionPane.ERROR_MESSAGE
            );
            return;
        }
        try{
            localidadService.createLocalidad(null,"",provinciaEntity);
            fireUpdate();
        }catch(Exception e){
            WebOptionPane.showMessageDialog(frame,
                    "Error al agregar registro.",
                    "Error",
                    WebOptionPane.ERROR_MESSAGE
            );
        }
        fillLocalidades(provinciaEntity.getId());
        localidadTable.setSelectedRow(localidadTable.getRowCount()-1);
    }
    private void editLocalidad(){
        int row=localidadTable.getSelectedRow();
        LocalidadEntity entity=getSelectedLocalidad();
        if(entity!=null){
            try {
                localidadService.updateLocalidad(entity.getId(), entity.getNombre());
                fireUpdate();
            }catch(Exception e){
                WebOptionPane.showMessageDialog(frame,
                        "Error al editar registro.",
                        "Error",
                        WebOptionPane.ERROR_MESSAGE
                );
            }
            fillLocalidades(entity.getProvincia().getId());
        }
        localidadTable.setSelectedRow(row);
    }
    private void deleteLocalidad(){
        LocalidadEntity entity=getSelectedLocalidad();
        Integer selectedRow=localidadTable.getSelectedRow();
        if(entity!=null){
            try {
                localidadService.removeLocalidad(entity.getId());
                fireUpdate();
            }catch(Exception e){
                WebOptionPane.showMessageDialog(frame,
                        "Error al eliminar registro.",
                        "Error",
                        WebOptionPane.ERROR_MESSAGE
                );
            }
            fillLocalidades(entity.getProvincia().getId());
            if(localidadTable.getRowCount()>selectedRow){
                localidadTable.setSelectedRow(selectedRow);
            }else if (selectedRow>0){
                localidadTable.setSelectedRow(selectedRow-1);
            }
        }
    }
    public void update(){
        configTable();
        fillComunidades();
    }
    void fireUpdate(){
        frame.firePropertyChange("DataUpdated",1,0);
    }
    public void addPropertyChangeListener(PropertyChangeListener listener){
        frame.addPropertyChangeListener(listener);
    }
    private ComunidadEntity getSelectedComunidad(){
        ComunidadEntity entity=null;
        Integer row=comunidadTable.getSelectedRow();
        if(row!=-1){
            entity=new ComunidadEntity();
            entity.setId(Long.parseLong(comunidadTable.getValueAt(row,comunidadTable.convertColumnIndexToView(0)).toString()));
            entity.setNombre(comunidadTable.getValueAt(row,comunidadTable.convertColumnIndexToView(1)).toString());
        }
        return entity;
    }
    private ProvinciaEntity getSelectedProvincia(){
        ProvinciaEntity entity=null;
        Integer row=provinciaTable.getSelectedRow();
        ComunidadEntity comunidadEntity=getSelectedComunidad();
        if(row!=-1){
            entity=new ProvinciaEntity();
            entity.setId((Long.parseLong(provinciaTable.getValueAt(row,provinciaTable.convertColumnIndexToView(0)).toString())));
            entity.setNombre(provinciaTable.getValueAt(row,provinciaTable.convertColumnIndexToView(1)).toString());
            entity.setComunidad(comunidadEntity);
        }
        return entity;
    }
    private LocalidadEntity getSelectedLocalidad(){
        LocalidadEntity entity=null;
        Integer row=localidadTable.getSelectedRow();
        ProvinciaEntity provinciaEntity=getSelectedProvincia();
        if(row!=-1 && provinciaEntity!=null){
            entity=new LocalidadEntity();
            entity.setId(Long.parseLong(localidadTable.getValueAt(row,localidadTable.convertColumnIndexToView(0)).toString()));
            entity.setNombre(localidadTable.getValueAt(row,localidadTable.convertColumnIndexToView(1)).toString());
            entity.setProvincia(provinciaEntity);
        }
        return entity;
    }

    @Override
    public void loadProperties() {
        Integer x,y,width,height;
        try {
            x=Integer.parseInt(PrincipalFrame.obtienePropiedad(LEFT));
            y=Integer.parseInt(PrincipalFrame.obtienePropiedad(TOP));
            width=Integer.parseInt(PrincipalFrame.obtienePropiedad(WIDTH));
            height=Integer.parseInt(PrincipalFrame.obtienePropiedad(HEIGHT));
            frame.setBounds(x,y,width,height);
        } catch (Exception e) {
            WebOptionPane.showMessageDialog(frame,
                    "Error al cargar las propiedades de la ventana.",
                    "Error",
                    WebOptionPane.WARNING_MESSAGE
            );
        }
    }

    @Override
    public void saveProperties() {
        PrincipalFrame.establecePropiedad(LEFT,String.valueOf(frame.getBounds().x));
        PrincipalFrame.establecePropiedad(TOP,String.valueOf(frame.getBounds().y));
        PrincipalFrame.establecePropiedad(WIDTH,String.valueOf(frame.getBounds().width));
        PrincipalFrame.establecePropiedad(HEIGHT,String.valueOf(frame.getBounds().height));
    }
}
