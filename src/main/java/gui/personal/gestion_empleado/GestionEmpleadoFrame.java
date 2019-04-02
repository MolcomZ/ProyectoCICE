package gui.personal.gestion_empleado;

import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.laf.text.WebTextField;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GestionEmpleadoFrame {
    private MigLayout layout;
    private WebFrame frame;

    private WebLabel idLabel;
    private WebToggleButton idlockButton;
    private WebLabel nombreLabel;
    private WebLabel apellidoLabel;
    private WebTextField idText;
    private WebTextField nombreText;
    private WebTextField apellidoText;


    private WebTabbedPane tabbedPane;
    private WebButton closeButton;

    public GestionEmpleadoFrame(){
        layout=new MigLayout("","[][][][GROW]","[][][GROW][]");
        frame=new WebFrame("Gestión empleado");
        frame.setLayout(layout);
        idLabel=new WebLabel("ID:");
        idlockButton=new WebToggleButton(new ImageIcon(this.getClass().getResource("/Lock.png")));
        nombreLabel=new WebLabel("NOMBRE:");
        apellidoLabel=new WebLabel("APELLIDO");
        idText=new WebTextField();
        idText.setEnabled(false);
        nombreText=new WebTextField();
        apellidoText=new WebTextField();

        tabbedPane=new WebTabbedPane();
        tabbedPane.addTab("Puesto",null);
        closeButton=new WebButton("CERRAR");

        frame.add(idLabel);
        frame.add(idText,"WIDTH 80,LEFT,SPLIT");
        frame.add(idlockButton,"WRAP");
        frame.add(nombreLabel);
        frame.add(nombreText,"WIDTH 120,LEFT");
        frame.add(apellidoLabel);
        frame.add(apellidoText,"WIDTH 180,LEFT,WRAP");
        frame.add(tabbedPane,"GROW,WRAP,SPAN 4");
        frame.add(closeButton,"CELL 3 3,RIGHT");

        frame.pack();
        initListeners();
    }
    public void initListeners(){
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
    }
    public void showFrame(){
        frame.setVisible(true);
    }
}
