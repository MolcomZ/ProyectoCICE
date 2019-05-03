package gui;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.menu.WebMenu;
import com.alee.laf.menu.WebMenuBar;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.separator.WebSeparator;
import com.sun.xml.internal.ws.api.config.management.policy.ManagementAssertion;
import gui.contenedores.ContenedoresFrame;
import gui.personal.editors.ausencias.EditorAusenciasFrame;
import gui.personal.editors.puestos.EditorPuestosFrame;
import gui.personal.editors.turnos.EditorTurnosFrame;
import gui.personal.gestionpersonal.GestionPersonalFrame;
import gui.settings.SettingsFrame;
import util.UserSettings;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class PrincipalFrame {
    WebFrame frame;
    WebMenuBar menu;
    WebMenu inicio_menu;
    WebMenuItem conexion_menu;
    WebMenuItem configuracion_menu;
    WebMenuItem salir_menu;

    WebMenu gestion_menu;
    WebMenuItem gestionPersonal_menuitem;
    WebMenuItem gestionLocalidades_menuitem;

    WebMenu edicion_menu;
    WebMenuItem puestos_menuitem;
    WebMenuItem turnos_menuitem;
    WebMenuItem ausencias_menuitem;

    WebMenu ventanas_menu;
    WebMenuItem lecturaContenedores_menuitem;
    WebMenuItem calendario_ausencias_menuitem;
    WebMenuItem pedidos_menu;
    WebMenuItem rutas_menu;
    WebMenuItem tiendas_menu;
    WebMenu tablas_menu;
    WebMenuItem secciones_menu;
    WebMenuItem puestos_menu;
    WebMenuItem turnos_menu;

    WebMenuItem comunidadesautonomas_menuitem;
    WebMenuItem provincias_menuitem;
    WebMenuItem empresastransporte_menuitem;
    WebMenuItem tabla_personal_menuitem;
    WebMenu tabla_ausencias_menu;
    WebMenuItem tipotabla_ausencias_menu;
    WebMenuItem saldos_menu;
    WebMenuItem registrotabla_ausencias_menu;

    //Declaración de todas las ventanas
//    ConexionFrame conexion;
//    ConfiguracionFrame configuracion;
//    PedidosFrame pedidos;
//    RutasFrame rutas;
//    TiendasFrame tiendas;
//
    SettingsFrame settingsFrame;
    GestionPersonalFrame gestionPersonal;
    GestionLocalidadesFrame gestionLocalidades;
    ContenedoresFrame contenedores;
    EditorPuestosFrame editorPuestosFrame;
    EditorTurnosFrame editorTurnosFrame;
    EditorAusenciasFrame editorAusenciasFrame;
//
//    ComunidadesAutonomasFrame comunidadesautonomas;
//    ProvinciasFrame provincias;
//    EmpresasTransporteFrame empresastransporte;
//    SeccionesFrame puestos;
//    PuestosFrame puestos;
//    TurnosFrame turnos;
//    PersonalFrame personal;
//    TipoAusenciasFrame tipoausencias;
//    SaldoAusenciasFrame saldo;
//    RegistroAusenciasFrame registroAusenciasFrame;
//    CalendarioFrame calendarioausencias;


    //Objeto conexion base de datos
//    DbInterface dbi;

    //Objeto propiedades
    static Properties propiedades;
    public static UserSettings setting=new UserSettings();


    //JPA
    static EntityManagerFactory EMF=Persistence.createEntityManagerFactory("MySQL_JPA");
    public static EntityManager EM=EMF.createEntityManager();


    public PrincipalFrame(){
        WebLookAndFeel.setDecorateFrames(true);
        WebLookAndFeel.setDecorateAllWindows(true);

        try {
            setting.loadSettings();
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        frame=new WebFrame("Proyecto fin de curso");
        //frame.setDefaultCloseOperation(WebFrame.EXIT_ON_CLOSE);
        menu=new WebMenuBar();
        inicio_menu=new WebMenu("Inicio");
        conexion_menu=new WebMenuItem("Conexión");
        configuracion_menu=new WebMenuItem("Configuración");
        salir_menu=new WebMenuItem("Salir");
        frame.setJMenuBar(menu);
        menu.add(inicio_menu);
        inicio_menu.add(conexion_menu);
        inicio_menu.add(configuracion_menu);
        inicio_menu.add(salir_menu);

        gestion_menu=new WebMenu("Gestión");
        gestionPersonal_menuitem=new WebMenuItem("Gestión personal");
        gestionLocalidades_menuitem=new WebMenuItem("Gestión localidades");
        menu.add(gestion_menu);
        gestion_menu.add(gestionPersonal_menuitem);
        gestion_menu.add(gestionLocalidades_menuitem);

        edicion_menu=new WebMenu("Edición");
        puestos_menuitem=new WebMenuItem("Puestos");
        turnos_menuitem=new WebMenuItem("Turnos");
        ausencias_menuitem=new WebMenuItem("Ausencias");
        menu.add(edicion_menu);
        edicion_menu.add(puestos_menuitem);
        edicion_menu.add(turnos_menuitem);
        edicion_menu.add(ausencias_menuitem);

        ventanas_menu=new WebMenu("Ventanas");
        lecturaContenedores_menuitem=new WebMenuItem("Lectura de contenedores");
        calendario_ausencias_menuitem=new WebMenuItem("Calendario ausencias");
        pedidos_menu=new WebMenuItem("Pedidos");
        rutas_menu=new WebMenuItem("Rutas");
        tiendas_menu=new WebMenuItem("Tiendas");
        menu.add(ventanas_menu);
        ventanas_menu.add(lecturaContenedores_menuitem);
        ventanas_menu.add(calendario_ausencias_menuitem);
        ventanas_menu.add(pedidos_menu);
        ventanas_menu.add(rutas_menu);
        ventanas_menu.add(tiendas_menu);

        tablas_menu=new WebMenu("Tablas");
        comunidadesautonomas_menuitem=new WebMenuItem("Comunidades autónomas");
        provincias_menuitem=new WebMenuItem("Provincias");
        empresastransporte_menuitem=new WebMenuItem("Empresas transporte");
        secciones_menu=new WebMenuItem("Secciones");
        puestos_menu=new WebMenuItem("Puestos");
        turnos_menu=new WebMenuItem("Turnos");
        tabla_personal_menuitem=new WebMenuItem("Personal");
        menu.add(tablas_menu);
        tablas_menu.add(comunidadesautonomas_menuitem);
        tablas_menu.add(provincias_menuitem);
        tablas_menu.add(empresastransporte_menuitem);
        tablas_menu.add(new WebSeparator());
        tablas_menu.add(secciones_menu);
        tablas_menu.add(puestos_menu);
        tablas_menu.add(turnos_menu);
        tablas_menu.add(tabla_personal_menuitem);
        tabla_ausencias_menu=new WebMenu("Ausencias");
        tipotabla_ausencias_menu=new WebMenuItem("Tipos");
        saldos_menu=new WebMenuItem("Saldos");
        registrotabla_ausencias_menu=new WebMenuItem("Registro");
        tabla_ausencias_menu.add(tipotabla_ausencias_menu);
        tabla_ausencias_menu.add(saldos_menu);
        tabla_ausencias_menu.add(registrotabla_ausencias_menu);
        tablas_menu.add(tabla_ausencias_menu);

//        dbi=new DbInterface();
//        comunidadesautonomas=new ComunidadesAutonomasFrame(dbi);
//        provincias=new ProvinciasFrame(dbi);
//        empresastransporte=new EmpresasTransporteFrame(dbi);
//        puestos=new SeccionesFrame(dbi);
//        puestos=new PuestosFrame(dbi);
//        turnos=new TurnosFrame(dbi);
//        personal=new PersonalFrame(dbi);
//        tipoausencias=new TipoAusenciasFrame(dbi);
//        saldo=new SaldoAusenciasFrame(dbi);
//        registroAusenciasFrame=new RegistroAusenciasFrame(dbi);
        settingsFrame=new SettingsFrame();
        gestionPersonal=new GestionPersonalFrame();
        gestionLocalidades=new GestionLocalidadesFrame();
        editorPuestosFrame=new EditorPuestosFrame();
        editorTurnosFrame=new EditorTurnosFrame();
        editorAusenciasFrame=new EditorAusenciasFrame();
        contenedores=new ContenedoresFrame();
//        calendarioausencias=new CalendarioFrame(dbi);
        iniciaListeners();
//        relacionaTablas();

        //Crea propiedades
        propiedades=new Properties();
        try {
            cargaPropiedades();
        } catch (IOException e) {
            e.printStackTrace();
        }
//
//        conexion_menu.doClick();

//        NovedadesFrame nf=new NovedadesFrame(dbi);
//        nf.showFrame();


//        SettingsFrame settingsFrame=new SettingsFrame();
//        settingsFrame.showFrame();




    }
    public void muestraFrame(){
        Integer w,h;
        w=Math.max(500,Toolkit.getDefaultToolkit().getScreenSize().width);
        frame.setSize(new Dimension(w, (int) frame.getPreferredSize().getHeight()));
        //frame.pack();
        //frame.setLocationRelativeTo(null);
        frame.setLocation(0,0);
        frame.setVisible(true);

    }
    public void iniciaListeners(){
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    guardaPropiedades();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                System.exit(0);
            }
        });
//        conexion_menu.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if(conexion==null){
//                    conexion=new ConexionFrame(dbi);
//                    conexion.muestraFrame();
//                }else{
//                    conexion.muestraFrame();
//                }
//            }
//        });
//        configuracion_menu.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if(configuracion==null){
//                    configuracion=new ConfiguracionFrame();
//                    configuracion.muestraFrame();
//                }else{
//                    configuracion.muestraFrame();
//                }
//            }
//        });
        configuracion_menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingsFrame.showFrame();
            }
        });
        salir_menu.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });
        gestionPersonal_menuitem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gestionPersonal.showFrame();
            }
        });
        gestionLocalidades_menuitem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gestionLocalidades.showFrame();
            }
        });
        puestos_menuitem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorPuestosFrame.showFrame();
            }
        });
        turnos_menuitem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorTurnosFrame.showFrame();
            }
        });
        ausencias_menuitem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorAusenciasFrame.showFrame();
            }
        });
        lecturaContenedores_menuitem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contenedores.showFrame();
            }
        });
//        calendario_ausencias_menuitem.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                calendarioausencias.muestraFrame();
//            }
//        });
//        pedidos_menu.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if(pedidos==null){
//                    pedidos=new PedidosFrame(dbi);
//                    pedidos.muestraFrame();
//                }else{
//                    pedidos.muestraFrame();
//                }
//            }
//        });
//        rutas_menu.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if(rutas==null){
//                    rutas=new RutasFrame(dbi);
//                    rutas.muestraFrame();
//                }else{
//                    rutas.muestraFrame();
//                }
//            }
//        });
//        tiendas_menu.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if(tiendas==null){
//                    tiendas=new TiendasFrame(dbi);
//                    tiendas.muestraFrame();
//                }else{
//                    tiendas.muestraFrame();
//                }
//            }
//        });
//        comunidadesautonomas_menuitem.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                comunidadesautonomas.showFrame();
//            }
//        });
//        provincias_menuitem.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                provincias.showFrame();
//            }
//        });
//        empresastransporte_menuitem.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                empresastransporte.muestraFrame();
//            }
//        });
//        secciones_menu.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                puestos.muestraFrame();
//            }
//        });
//        puestos_menu.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                puestos.muestraFrame();
//            }
//        });
//        turnos_menu.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                turnos.muestraFrame();
//            }
//        });
//        tabla_personal_menuitem.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                personal.muestraFrame();
//            }
//        });
//        tipotabla_ausencias_menu.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                tipoausencias.muestraFrame();
//            }
//        });
//        saldos_menu.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                saldo.muestraFrame();
//            }
//        });
//        registrotabla_ausencias_menu.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                registroAusenciasFrame.muestraFrame();
//            }
//        });
    }
//    public void relacionaTablas(){
//        puestos.addPropertyChangeListener(new PropertyChangeListener() {
//            @Override
//            public void propertyChange(PropertyChangeEvent evt) {
//                if(evt.getPropertyName().equals("DataUpdated")){
//                    puestos.actualiza();
//                    personal.actualiza();
//                }
//            }
//        });
//        puestos.addPropertyChangeListener(new PropertyChangeListener() {
//            @Override
//            public void propertyChange(PropertyChangeEvent evt) {
//                if(evt.getPropertyName().equals("DataUpdated")){
//                    personal.actualiza();
//                }
//            }
//        });
//        turnos.addPropertyChangeListener(new PropertyChangeListener() {
//            @Override
//            public void propertyChange(PropertyChangeEvent evt) {
//                if(evt.getPropertyName().equals("DataUpdated")){
//                    personal.actualiza();
//                }
//            }
//        });
//        registroAusenciasFrame.addPropertyChangeListener(new PropertyChangeListener() {
//            @Override
//            public void propertyChange(PropertyChangeEvent evt) {
//                if(evt.getPropertyName().equals(DataUpdatedNoticer.DATAUPDATED)){
//                    calendarioausencias.actualiza();
//                }
//            }
//        });
//    }
    public void cargaPropiedades() throws IOException {
        InputStream is=new FileInputStream("properties.xml");
        propiedades.loadFromXML(is);
        is.close();
    }
    public void guardaPropiedades() throws IOException {
        OutputStream os=new FileOutputStream("properties.xml");
        propiedades.storeToXML(os,null);
        os.close();
    }
    public static String obtienePropiedad(String key) throws NoSuchFieldException {
        String returnvalue;
        returnvalue=propiedades.getProperty(key);
        if(returnvalue==null){
            throw new NoSuchFieldException();
        }
        return returnvalue;
    }
    public static void establecePropiedad(String key, String value){
        propiedades.setProperty(key,value);
    }
    public static int compareDates(Date date1, Date date2){
        Integer y1,m1,d1;
        Integer y2,m2,d2;
        Calendar cal1,cal2;
        cal1=Calendar.getInstance();
        cal1.setTime(date1);
        cal2=Calendar.getInstance();
        cal2.setTime(date2);
        y1=cal1.get(Calendar.YEAR);
        m1=cal1.get(Calendar.MONTH);
        d1=cal1.get(Calendar.DATE);
        y2=cal2.get(Calendar.YEAR);
        m2=cal2.get(Calendar.MONTH);
        d2=cal2.get(Calendar.DATE);

        if(y1>y2){
            return 1;
        }
        if(y1<y2){
            return -1;
        }
        if(m1>m2){
            return 1;
        }
        if(m1<m2){
            return -1;
        }
        if(d1>d2){
            return 1;
        }
        if(d1<d2){
            return -1;
        }
        return 0;
    }
}
