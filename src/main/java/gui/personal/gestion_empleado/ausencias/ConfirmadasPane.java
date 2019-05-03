package gui.personal.gestion_empleado.ausencias;

import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextField;
import gui.PrincipalFrame;
import gui.personal.util.DateCellEditor;
import gui.personal.util.DateCellRenderer;
import jpa.ausenciasempleado.AusenciasEmpleadoEntity;
import jpa.ausenciasempleadoconfirmadas.AusenciasConfirmadasEntity;
import jpa.ausenciasempleadoconfirmadas.AusenciasConfirmadasService;
import net.miginfocom.swing.MigLayout;
import util.AutoSizeableTable;
import util.EntityListenerManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class ConfirmadasPane extends WebPanel {
    private MigLayout layout;
    private DefaultTableModel model;
    private AutoSizeableTable table;
    private WebScrollPane scroll;
    private WebButton addButton;
    private WebButton removeButton;
    private WebLabel totalLabel;
    private WebTextField totalText;
    private WebLabel confirmadasLabel;
    private WebTextField confirmadasText;
    private WebLabel restantesLabel;
    private WebTextField restantesText;

    AusenciasConfirmadasService service;

    private AusenciasEmpleadoEntity ausencia;

    public ConfirmadasPane(){
        layout=new MigLayout("","[GROW]","[GROW][]");
        setLayout(layout);

        model=new DefaultTableModel();
        table=new AutoSizeableTable(model);
        scroll=new WebScrollPane(table);
        addButton=new WebButton("Agregar",new ImageIcon(getClass().getResource("/Add.png")));
        removeButton=new WebButton("Eliminar",new ImageIcon(getClass().getResource("/Remove.png")));
        totalLabel=new WebLabel("Total:");
        totalText=new WebTextField();
        totalText.setEditable(false);
        confirmadasLabel=new WebLabel("Confirmadas:");
        confirmadasText=new WebTextField();
        confirmadasText.setEditable(false);
        restantesLabel=new WebLabel("Restantes:");
        restantesText =new WebTextField();
        restantesText.setEditable(false);

        add(scroll,"GROW,WRAP");
        add(totalLabel,"SPLIT");
        add(totalText,"W 50");
        add(confirmadasLabel);
        add(confirmadasText,"W 50");
        add(restantesLabel);
        add(restantesText,"W 50,WRAP");
        add(addButton,"SPLIT 2,LEFT");
        add(removeButton,"LEFT,WRAP");

        service=new AusenciasConfirmadasService(PrincipalFrame.EM);

        configTable();
        initListeners();
    }
    private void initListeners(){
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addConfirmada();
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeComfirmada();
            }
        });
        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if(e.getType()==TableModelEvent.UPDATE){
                    editConfirmada();
                }
            }
        });
    }
    private void configTable(){
        model.setColumnCount(4);
        model.setColumnIdentifiers(new String[]{"ID","INICIO","FIN","DIAS"});
        DateCellEditor editor=new DateCellEditor();
        table.getColumn("INICIO").setCellRenderer(new DateCellRenderer());
        table.getColumn("FIN").setCellRenderer(new DateCellRenderer());
        table.getColumn("INICIO").setCellEditor(editor);
        table.getColumn("FIN").setCellEditor(editor);
        //table.setRowHeight((int) editor.getTableCellEditorComponent(table,null,false,0,0).getPreferredSize().getHeight());
    }
    private void fillTable(){
        Date d1,d2;
        Long confirmadas=0L;
        Integer total=0;
        Long restantes=0L;

        model.setRowCount(0);
        if(ausencia==null)return;
        List<AusenciasConfirmadasEntity> ausencias;
        ausencias=service.findAusenciasConfirmadasByIdAusenciaEmpleado(ausencia.getId());
        for(AusenciasConfirmadasEntity entity:ausencias){
            Object o[]=new Object[4];
            o[0]=entity.getId();
            d1=entity.getInicio();
            o[1]=d1;
            d2=entity.getFin();
            o[2]=d2;

            try {
                o[3]= ChronoUnit.DAYS.between(d1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        d2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())+1;
                confirmadas+=(Long)o[3];
            }catch(Exception e){
                o[3]="ERROR";
            }
            model.addRow(o);
        }
        confirmadasText.setText(confirmadas.toString());
        total=ausencia.getCantidad();
        restantes=total-confirmadas;
        totalText.setText(total.toString());
        restantesText.setText(restantes.toString());

        //PRUEBA
        //table.adjustRowMax(0,0);
        //table.adjustRowMax(1,0);
    }
    public void setAusencia(AusenciasEmpleadoEntity ausencia){
        this.ausencia=ausencia;
        fillTable();
    }
    public void addConfirmada(){
        Date date=new Date();
        service.createAusenciasConfirmadas(ausencia,date,date);
        fillTable();
        if(table.getRowCount()>0)table.setSelectedRow(table.getRowCount()-1);
    }
    private void removeComfirmada(){
        Integer row=table.getSelectedRow();
        try{
            if(row!=-1) {
                service.removeAusenciasConfirmadas(getSelectedConfirmada().getId());
                fillTable();
                if(table.getRowCount()>row){
                    table.setSelectedRow(row);
                }else{
                    table.setSelectedRow(table.getRowCount()-1);
                }
            }

        }catch(Exception e){
            WebOptionPane.showMessageDialog(this,"No se ha podido eliminar el registro.","Error",WebOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    private void editConfirmada(){
        int row=table.getSelectedRow();
        AusenciasConfirmadasEntity entity=getSelectedConfirmada();
        System.out.println("edit confirmada");
        if(entity!=null){
            System.out.println("not null");
            if(entity.getInicio().compareTo(entity.getFin())>0){
                WebOptionPane.showMessageDialog(this,"La fecha final no puede ser anterior a la inicial.");
                fillTable();
                table.setSelectedRow(row);
                return;
            }
            try {
                service.updateAusenciasConfirmadas(entity.getId(),
                        entity.getAusencia(),
                        entity.getInicio(),
                        entity.getFin());
            }catch(Exception e){
                WebOptionPane.showMessageDialog(this,
                        "Error al editar registro.",
                        "Error",
                        WebOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }
        fillTable();
        table.setSelectedRow(row);
//        firePropertyChange(UPDATE_EVENT,1,0);
        EntityListenerManager.fireEntityUpdated(AusenciasConfirmadasEntity.class);
    }

    private AusenciasConfirmadasEntity getSelectedConfirmada(){
        Integer row;
        Long id;
        AusenciasEmpleadoEntity ausenciaEmpleado;
        Date inicio,fin;

        AusenciasConfirmadasEntity entity=null;
        row=table.getSelectedRow();
        if(row!=-1){
            id=(Long)table.getValueAt(table.convertRowIndexToModel(row),table.convertColumnIndexToModel(0));
            inicio= (Date) table.getValueAt(table.convertRowIndexToModel(row),table.convertColumnIndexToModel(1));
            fin= (Date) table.getValueAt(table.convertRowIndexToModel(row),table.convertColumnIndexToModel(2));
            entity=new AusenciasConfirmadasEntity(id,ausencia,inicio,fin);
            //entity=service.findAusenciasConfirmadas(id);
        }
        return entity;
    }
}
