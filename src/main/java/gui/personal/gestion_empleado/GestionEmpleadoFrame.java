package gui.personal.gestion_empleado;

import com.alee.laf.button.WebButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.tabbedpane.WebTabbedPane;
import gui.personal.gestion_empleado.ausencias.AusenciasPane;
import jpa.empleados.EmpleadoEntity;
import jpa.empleados.EmpleadoService;
import jpa.puestos.PuestoEntity;
import jpa.puestos.PuestoService;
import jpa.turnos.TurnoEntity;
import jpa.turnos.TurnoService;
import net.miginfocom.swing.MigLayout;
import util.WebTextFieldEnterFocus;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class GestionEmpleadoFrame {
    //PROPERTIES CONSTANTS
    public static final String PROPERTY_LEFT="PROPERTY_LEFT";
    public static final String PROPERTY_TOP="PROPERTY_TOP";
    public static final String PROPERTY_WIDTH="PROPERTY_WIDTH";
    public static final String PROPERTY_HEIGHT="PROPERTY_HEIGHT";

    public static final String DATA_CHANGED="Data changed";
    private MigLayout layout;
    private WebFrame frame;

    private WebLabel idLabel;
    //private WebToggleButton idlockButton;
    private WebButton addButton;
    private WebButton removeButton;
    private WebLabel nombreLabel;
    private WebLabel apellidoLabel;
    private WebTextFieldEnterFocus idText;
    private WebTextFieldEnterFocus nombreText;
    private WebTextFieldEnterFocus apellidoText;
    private WebLabel turnoLabel;
    private WebComboBox turnoCombo;

    private WebTabbedPane tabbedPane;
    private PuestoPane puestoPane;
    private AusenciasPane ausenciasPane;
    private WebButton closeButton;

    private EmpleadoEntity empleado;

    //PERSISTENCE
    private EntityManagerFactory EMF;
    private EntityManager EM;
    private EmpleadoService empleadoService;
    private TurnoService turnoService;

    private HashMap<Long,String> turnosMap;

    private boolean userEditing=false;

    public GestionEmpleadoFrame(){
        layout=new MigLayout("","[][][][GROW]","[][][][GROW][]");
        frame=new WebFrame("Gestión empleado");
        frame.setLayout(layout);
        idLabel=new WebLabel("ID:");
        //idlockButton=new WebToggleButton(new ImageIcon(this.getClass().getResource("/Lock.png")));
        addButton=new WebButton(new ImageIcon(getClass().getResource("/Add.png")));
        removeButton=new WebButton(new ImageIcon(getClass().getResource("/Delete.png")));
        nombreLabel=new WebLabel("NOMBRE:");
        apellidoLabel=new WebLabel("APELLIDO:");
        idText= new WebTextFieldEnterFocus();
        idText.setInputVerifier(new idVerifier());
        idText.setEnabled(false);
        nombreText= new WebTextFieldEnterFocus();
        nombreText.setInputVerifier(new nombreVerifier());
        apellidoText= new WebTextFieldEnterFocus();
        apellidoText.setInputVerifier(new apellidoVerifier());
        turnoLabel=new WebLabel("TURNO:");
        turnoCombo=new WebComboBox();

        tabbedPane=new WebTabbedPane();
        puestoPane=new PuestoPane();
        ausenciasPane=new AusenciasPane();
        tabbedPane.addTab("Puesto",puestoPane);
        tabbedPane.addTab("Ausencias",ausenciasPane);
        closeButton=new WebButton("CERRAR");

        frame.add(idLabel);
        frame.add(idText,"WIDTH 80,LEFT,SPLIT 2,WRAP");
        //frame.add(idlockButton,"WRAP");
        frame.add(nombreLabel);
        frame.add(nombreText,"WIDTH 120,LEFT");
        frame.add(apellidoLabel);
        frame.add(apellidoText,"WIDTH 180,LEFT,WRAP");
        frame.add(turnoLabel);
        frame.add(turnoCombo,"WIDTH 120,LEFT,WRAP");
        frame.add(tabbedPane,"GROW,WRAP,SPAN 4");
        frame.add(addButton,"CELL 0 4,LEFT,SPLIT 2");
        frame.add(removeButton);
        frame.add(closeButton,"CELL 3 4,RIGHT");

        turnosMap=new HashMap<>();

        EMF=Persistence.createEntityManagerFactory("MySQL_JPA");
        EM=EMF.createEntityManager();
        empleadoService=new EmpleadoService(EM);
        turnoService=new TurnoService(EM);

        updateTurnos();
        fillEmpleadoData();
        frame.pack();
        initListeners();
    }
    public void initListeners(){
/*
        idlockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(idlockButton.isSelected()){
                    WebOptionPane.showMessageDialog(frame,"El ID es un dato común a otros departamentos y/o bases de datos.\nSi se cambia puede que se pierda la relación.","¡Atención!",WebOptionPane.WARNING_MESSAGE);
                    idText.setEnabled(true);
                }else{
                    idText.setEnabled(false);
                }
            }
        });
*/
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEmpleado();
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeEmpleado();
            }
        });
        turnoCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange()==ItemEvent.SELECTED) {
                    editEmpleado();
                }
            }
        });
        puestoPane.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getPropertyName().equals(puestoPane.PUESTO_CHANGED)){
                    editEmpleado();
                }
            }
        });
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveProperties();
                puestoPane.saveProperties();
            }
        });
        idText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                editEmpleado();
            }
        });
        nombreText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                editEmpleado();
            }
        });
        apellidoText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                editEmpleado();
            }
        });
    }
    private void updateTurnos(){
        List<TurnoEntity> turnos;
        turnos=turnoService.findAllTurnos();
        turnoCombo.removeAllItems();
        turnosMap.clear();
        for(TurnoEntity turno:turnos){
            turnoCombo.addItem(turno.getNombre());
            turnosMap.put(turno.getId(),turno.getNombre());
        }
    }
    public void showFrame(){
        loadProperties();
        puestoPane.loadProperties();
        frame.setVisible(true);
        userEditing=true;
    }
    public void setEmpleado(EmpleadoEntity empleado){
        if(empleado==null) {
            WebOptionPane.showMessageDialog(frame,"No hay ningún empleado a editar.","Error",WebOptionPane.ERROR_MESSAGE);
            return;
        }
        this.empleado = empleado;
        ausenciasPane.setEmpleado(empleado);
        fillEmpleadoData();
    }
    private void fillEmpleadoData(){
        userEditing=false;
        if(empleado!=null) {
            idText.setText(empleado.getId().toString());
            nombreText.setText(empleado.getNombre());
            apellidoText.setText(empleado.getApellido());
            puestoPane.setEmpleado(empleado);
            turnoCombo.setSelectedItem(empleado.getTurno().getNombre());
        }
        userEditing=true;
    }
    private void removeEmpleado(){
        Long id;
        if(WebOptionPane.showConfirmDialog(frame,"¿Está seguro de que quiere\neliminar el empleado?.","Confirmar...",WebOptionPane.YES_NO_OPTION)!=WebOptionPane.YES_OPTION){
            return;
        }
        try{
            id=Long.parseLong(idText.getText());
            empleadoService.removeEmpleado(id);
        }catch(NumberFormatException e){
            WebOptionPane.showMessageDialog(frame,"El ID no es válido.");
            return;
        }catch(Exception e){
            WebOptionPane.showMessageDialog(frame,"Error al eliminar el empleado.\nPara eliminar un empleado, éste,\nno debe tener datos en otras tablas.");
            return;
        }
        WebOptionPane.showMessageDialog(frame,"Empleado eliminado.");
        frame.firePropertyChange(DATA_CHANGED,1,0);
        frame.dispose();
    }
    public void addEmpleado(){
        TurnoService turnoService=new TurnoService(EM);
        PuestoService puestoService=new PuestoService(EM);
        TurnoEntity turno=new TurnoEntity();
        turno=turnoService.findAllTurnos().get(0);
        PuestoEntity puesto=new PuestoEntity();
        puesto=puestoService.findAllPuestos().get(0);
        empleado=empleadoService.createEmpleado(null,"","",turno,puesto);
        WebOptionPane.showMessageDialog(frame,"Empleado creado");
        fillEmpleadoData();
        frame.firePropertyChange(DATA_CHANGED,1,0);
    }
    private TurnoEntity getTurno(){
        Long id;
        for(Map.Entry<Long,String> entry:turnosMap.entrySet()){
            if(entry.getValue().equals(turnoCombo.getSelectedItem())){
                id=entry.getKey();
                return turnoService.findTurno(id);
            }
        }
        return null;
    }
    private void editEmpleado(){
        if(!userEditing)return;
        Long id;
        String nombre;
        String apellido;
        TurnoEntity turno;
        PuestoEntity puesto;
        try{
            id=Long.parseLong(idText.getText());
            nombre=nombreText.getText();
            apellido=apellidoText.getText();
            puesto=puestoPane.getPuesto();
            turno=getTurno();
            empleadoService.updateEmpleado(empleado.getId(),id,nombre,apellido,turno,puesto);
            frame.firePropertyChange(DATA_CHANGED,1,0);
        }catch(NumberFormatException e){
            WebOptionPane.showMessageDialog(frame,"El ID no es válido.");
        }catch(Exception e){
            WebOptionPane.showMessageDialog(frame,"Error al actualizar el registro.");
        }
    }
    public void addPropertyChangeListener(PropertyChangeListener listener){
        frame.addPropertyChangeListener(listener);
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
    private class nombreVerifier extends InputVerifier {
        @Override
        public boolean verify(JComponent input) {
            String text=((WebTextFieldEnterFocus)input).getText();
            if(text.length()>20){
                WebOptionPane.showMessageDialog(input,"Máximo 20 caracteres.\nIntroducidos "+text.length()+".");
                return false;
            }else{
                return true;
            }
        }
    }
    private class apellidoVerifier extends InputVerifier {
        @Override
        public boolean verify(JComponent input) {
            String text=((WebTextFieldEnterFocus)input).getText();
            if(text.length()>50){
                WebOptionPane.showMessageDialog(input,"Máximo 50 caracteres.\nIntroducidos "+text.length()+".");
                return false;
            }else{
                return true;
            }
        }
    }
    private class idVerifier extends InputVerifier {
        @Override
        public boolean verify(JComponent input) {
            String text = ((WebTextFieldEnterFocus) input).getText();
            try {
                Long.parseLong(text);
            } catch (NumberFormatException e) {
                WebOptionPane.showMessageDialog(input, "No es un número válido.");
                return false;
            }
            return true;
        }
    }
}
