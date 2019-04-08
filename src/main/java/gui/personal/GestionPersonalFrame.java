package gui.personal;

import com.alee.laf.button.WebButton;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.tabbedpane.WebTabbedPane;
import gui.PrincipalFrame;
import gui.personal.filtros.FiltrosFrame;
import gui.personal.gestion_empleado.GestionEmpleadoFrame;
import jpa.empleados.EmpleadoEntity;
import jpa.secciones.SeccionService;
import net.miginfocom.swing.MigLayout;
import util.PropertiesManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GestionPersonalFrame implements PropertiesManager {
    public static final String LEFT="GestionPersonalFrame.LEFT";
    public static final String TOP="GestionPersonalFrame.TOP";
    public static final String WIDTH="GestionPersonalFrame.WIDTH";
    public static final String HEIGHT="GestionPersonalFrame.HEIGHT";
    public static final String PUESTOSCOLLAPSE="GestionPersonalFrame.PUESTOSCOLLAPSE";

    private WebFrame frame;
    private MigLayout layout;
    private WebButton editButton;
    private WebButton filtersButton;
    private WebTabbedPane tabbedPane;
    private EmpleadosPane empleadosPane;
    private WebButton closeButton;
    private FiltrosFrame filtrosFrame;
    private GestionEmpleadoFrame empleadosFrame;

    //PERSISTENCE
    private EntityManagerFactory EMF;
    private EntityManager EM;
    private SeccionService seccionService;

    public GestionPersonalFrame() {
        frame = new WebFrame("Gestión personal");
        layout = new MigLayout("","[GROW]","[][GROW][]");
        frame.setLayout(layout);
        tabbedPane=new WebTabbedPane();
        empleadosPane=new EmpleadosPane();
        tabbedPane.addTab("GENERAL",empleadosPane);
        tabbedPane.addTab("AUSENCIAS",null);
        tabbedPane.addTab("HORAS POR HORAS",null);
        tabbedPane.addTab("CURSOS",null);
        tabbedPane.addTab("ANTICIPOS",null);
        filtersButton=new WebButton("FILTROS");
        editButton=new WebButton("EDITAR");
        closeButton=new WebButton("CERRAR");
        filtrosFrame=new FiltrosFrame();
        empleadosFrame=new GestionEmpleadoFrame();

        frame.add(filtersButton,"LEFT");
        frame.add(editButton,"RIGHT,WRAP");
        frame.add(tabbedPane,"SPAN 2,GROW,WRAP");
        frame.add(closeButton,"CELL 1 2,RIGHT");

        EMF=Persistence.createEntityManagerFactory("MySQL_JPA");
        EM=EMF.createEntityManager();
        seccionService=new SeccionService(EM);

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
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                empleadosFrame.setEmpleado(empleadosPane.getSelectedEmpleado());
                empleadosFrame.showFrame();
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
    }
    private void updateFilters(){
        empleadosPane.updateFilters(filtrosFrame.getSelectedTurnos(),filtrosFrame.getSelectedPuestos());
        //empleadosPane.updateFilters(filtroTurnosPane.getSelectedItems(),filtroPuestosPane.getSelectedItems());
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
