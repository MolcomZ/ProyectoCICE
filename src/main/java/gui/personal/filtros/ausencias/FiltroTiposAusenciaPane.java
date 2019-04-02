package gui.personal.filtros.ausencias;

import com.alee.laf.button.WebButton;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import gui.personal.editors.ausencias.EditorAusenciasFrame;
import jpa.ausencias.TiposAusenciaEntity;
import jpa.ausencias.TiposAusenciaService;
import net.miginfocom.swing.MigLayout;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FiltroTiposAusenciaPane extends WebPanel {
    private MigLayout mainlayout;

    private WebPanel pane;
    private MigLayout layout;
    private WebScrollPane scroll;
    private TiposAusenciaTable table;
    private DefaultTableModel model;
    private WebButton editButton;
    private EditorAusenciasFrame editor;

    //PERSISTENCE
    private EntityManagerFactory EMF;
    private EntityManager EM;
    private TiposAusenciaService tiposAusenciaService;

    public FiltroTiposAusenciaPane(){
        mainlayout =new MigLayout("","[GROW]","[][GROW]");
        this.setLayout(mainlayout);

        pane =new WebPanel();
        layout =new MigLayout("","[GROW]","[]");
        pane.setLayout(layout);
        model =new DefaultTableModel();
        table =new TiposAusenciaTable(model);
        scroll =new WebScrollPane(table);
        pane.add(scroll,"GROW");
        editButton=new WebButton("EDITAR");
        editor=new EditorAusenciasFrame();

        this.add(editButton,"RIGHT,TOP,WRAP");
        this.add(pane,"GROW,WRAP");

        EMF=Persistence.createEntityManagerFactory("MySQL_JPA");
        EM=EMF.createEntityManager();
        tiposAusenciaService=new TiposAusenciaService(EM);

        configTable();
        fillTiposAusencias();
        initListeners();
    }
    private void initListeners(){
        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if(e.getType()==TableModelEvent.UPDATE){
                    fireUpdate();
                }
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.showFrame();
            }
        });
    }
    private void configTable(){
        model.setColumnCount(4);
        model.setColumnIdentifiers(new String[]{"","ID","NOMBRE","DESCRIPCION"});

    }
    public void fillTiposAusencias(){
        Object o[];
        model.setRowCount(0);
        for(TiposAusenciaEntity entity:tiposAusenciaService.findAllTiposAusencias()){
            o=new Object[4];
            o[0]=true;
            o[1]=entity.getId();
            o[2]=entity.getNombre();
            o[3]=entity.getDescripcion();
            model.addRow(o);
        }
    }
    private void fireUpdate(){
        this.firePropertyChange("DataUpdated",1,0);
    }
    public ArrayList<TiposAusenciaEntity> getSelectedItems(){
        ArrayList<TiposAusenciaEntity> entities=new ArrayList<>();
        for(int n = 0; n< table.getRowCount(); n++){
            Boolean selected= (Boolean) table.getValueAt(n, table.convertColumnIndexToView(0));
            Long id= (Long) table.getValueAt(n, table.convertColumnIndexToView(1));
            String nombre= (String) table.getValueAt(n, table.convertColumnIndexToView(2));
            String descripcion= (String) table.getValueAt(n, table.convertColumnIndexToView(3));
            //TiposAusenciaEntity entity=new TiposAusenciaEntity(id,nombre,descripcion);
            if(selected) {
                entities.add(tiposAusenciaService.findTiposAusencia(id));
            }
        }
        return entities;
    }
}
