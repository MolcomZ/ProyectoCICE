package gui.personal.gestionpersonal;

import com.alee.laf.button.WebButton;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.tabbedpane.WebTabbedPane;
import gui.PrincipalFrame;
import gui.personal.gestionpersonal.ausencias.AusenciasPane;
import gui.personal.filtros.FiltrosFrame;
import gui.personal.gestion_empleado.GestionEmpleadoFrame;
import jpa.empleados.EmpleadoEntity;
import jpa.secciones.SeccionService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class GestionPersonalFrame{
    public static final String PROPERTY_LEFT="PROPERTY_LEFT";
    public static final String PROPERTY_TOP="PROPERTY_TOP";
    public static final String PROPERTY_WIDTH="PROPERTY_WIDTH";
    public static final String PROPERTY_HEIGHT="PROPERTY_HEIGHT";
    public static final String PROPERTY_PUESTOSCOLLAPSE="PROPERTY_PUESTOSCOLLAPSE";

    private WebFrame frame;
    private MigLayout layout;
    private WebButton editButton;
    private WebButton refreshButton;
    private WebButton filtersButton;
    private WebTabbedPane tabbedPane;
    private EmpleadosPane empleadosPane;
    private AusenciasPane ausenciasPane;
    private WebButton closeButton;
    private FiltrosFrame filtrosFrame;
    private GestionEmpleadoFrame empleadosFrame;

    private SeccionService seccionService;

    public GestionPersonalFrame() {
        frame = new WebFrame("Gestión personal");
        layout = new MigLayout("","[GROW]","[][GROW][]");
        frame.setLayout(layout);
        tabbedPane=new WebTabbedPane();
        tabbedPane.setTabPlacement(WebTabbedPane.BOTTOM);
        empleadosPane=new EmpleadosPane();
        ausenciasPane=new AusenciasPane();
        tabbedPane.addTab("GENERAL",empleadosPane);
        tabbedPane.addTab("AUSENCIAS",ausenciasPane);
        tabbedPane.addTab("HORAS POR HORAS",null);
        tabbedPane.addTab("CURSOS",null);
        tabbedPane.addTab("ANTICIPOS",null);
        filtersButton=new WebButton("FILTROS");
        editButton=new WebButton(new ImageIcon(getClass().getResource("/Edit.png")));
        refreshButton=new WebButton(new ImageIcon(getClass().getResource("/Refresh.png")));
        closeButton=new WebButton("CERRAR");
        filtrosFrame=new FiltrosFrame();
        empleadosFrame=new GestionEmpleadoFrame();

        frame.add(filtersButton,"LEFT");
        frame.add(editButton,"RIGHT,SPLIT");
        frame.add(refreshButton,"WRAP");
        frame.add(tabbedPane,"SPAN 2,GROW,WRAP");
        frame.add(closeButton,"CELL 1 2,RIGHT");

        seccionService=new SeccionService(PrincipalFrame.EM);

        frame.setPreferredSize(new Dimension(500,400));
        frame.pack();

        initListeners();
    }
    public void showFrame(){
        //update();
        loadProperties();
        frame.setVisible(true);
    }
    private void initListeners(){
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveProperties();
            }

        });
        filtersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filtrosFrame.showFrame();
            }
        });
        empleadosPane.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getPropertyName().equals(EmpleadosPane.TABLE_DOUBLE_CLICKED)){
                    empleadosFrame.setEmpleado(empleadosPane.getSelectedEmpleado());
                    empleadosFrame.showFrame();
                }
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EmpleadoEntity empleado=empleadosPane.getSelectedEmpleado();
                if(empleado==null){
                    if(WebOptionPane.showConfirmDialog(frame,"No ha seleccionado ningún empleado,\n¿Crear uno nuevo?.","Editar empleado",WebOptionPane.YES_NO_OPTION)==WebOptionPane.YES_OPTION){
                        if(empleadosFrame.addEmpleado()){
                            empleadosFrame.showFrame();
                        }
                    }else{
                        return;
                    }
                }else {
                    empleadosFrame.setEmpleado(empleadosPane.getSelectedEmpleado());
                    empleadosFrame.showFrame();
                }

            }
        });
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //refresh();
            }
        });
        filtrosFrame.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateFilters();
            }
        });
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });
//        empleadosFrame.addPropertyChangeListener(new PropertyChangeListener() {
  //          @Override
    //        public void propertyChange(PropertyChangeEvent evt) {
      //          if(evt.getPropertyName().equals(empleadosFrame.DATA_CHANGED)){
        //            refresh();
          //      }
   //         }
     //   });
    }
    //private void refresh(){
      //  empleadosPane.refresh();
      //  ausenciasPane.refresh();
   // }
    private void updateFilters(){
        empleadosPane.updateFilters(filtrosFrame.getSelectedTurnos(),filtrosFrame.getSelectedPuestos());
        ausenciasPane.updateFilters(filtrosFrame.getSelectedTurnos(),filtrosFrame.getSelectedPuestos());
        //empleadosPane.updateFilters(filtroTurnosPane.getSelectedItems(),filtroPuestosPane.getSelectedItems());
    }

    private void loadProperties(){
        int l,t,w,h,sp;
        try {
            FileInputStream is=new FileInputStream(getClass().getSimpleName()+".xml");
            Properties properties=new Properties();
            properties.loadFromXML(is);
            try{
                l=Integer.parseInt(properties.getProperty(PROPERTY_LEFT));
                t=Integer.parseInt(properties.getProperty(PROPERTY_TOP));
                w=Integer.parseInt(properties.getProperty(PROPERTY_WIDTH));
                h=Integer.parseInt(properties.getProperty(PROPERTY_HEIGHT));
                frame.setBounds(l,t,w,h);
            }catch (NumberFormatException e){
            }
            is.close();
        } catch (IOException e) {
            WebOptionPane.showMessageDialog(frame,"Error al cargar las propiedades.","Error",WebOptionPane.WARNING_MESSAGE);
        }
    }
    private void saveProperties(){
        Rectangle bounds;
        try {
            FileOutputStream os=new FileOutputStream(getClass().getSimpleName()+".xml");
            Properties properties=new Properties();
            bounds=frame.getBounds();
            properties.setProperty(PROPERTY_LEFT,String.valueOf(bounds.x));
            properties.setProperty(PROPERTY_TOP,String.valueOf(bounds.y));
            properties.setProperty(PROPERTY_WIDTH,String.valueOf(bounds.width));
            properties.setProperty(PROPERTY_HEIGHT,String.valueOf(bounds.height));
            properties.storeToXML(os,null);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
