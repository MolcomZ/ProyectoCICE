package gui.personal.filtros.puestos;

import com.alee.laf.combobox.WebComboBox;
import com.alee.utils.swing.PopupMenuAdapter;
import jpa.puestos.PuestoEntity;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class PuestoCellEditor extends AbstractCellEditor implements TableCellEditor{
    private WebComboBox combo;
    private HashMap<Long,String> map;
    private ArrayList<PuestoEntity> list;

    public PuestoCellEditor(){
        combo=new WebComboBox();
        combo.addPopupMenuListener(new PopupMenuAdapter() {
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                if (combo.getSelectedIndex() != -1){
                    PuestoCellEditor.this.fireEditingStopped();
                }else{
                    PuestoCellEditor.this.fireEditingCanceled();
                }
            }
        });
    }

    public void setMap(HashMap<Long,String> map){
        this.map=map;
        refreshCombo();
    }
    public void setList(ArrayList<PuestoEntity> list){
        this.list=list;
        refreshCombo();
    }
    private void refreshCombo() {
        combo.removeAllItems();
        for(PuestoEntity entity:list){
            combo.addItem(entity.getNombre());
        }
        //for(Long l:map.keySet()){
            //combo.addItem(map.get(l));

        //}
    }
    @Override
    public Object getCellEditorValue() {
        String selectedItem=combo.getSelectedItem().toString();
        for(PuestoEntity entity:list){
            if(entity.getNombre().equals(selectedItem)){
                return entity;
            }
        }
//        Iterator it=map.entrySet().iterator();
//        while(it.hasNext()){
//            Map.Entry pair= (Map.Entry) it.next();
//            if(pair.getValue().equals(combo.getSelectedItem())){
//                return pair.getKey();
//            }
//        }
        return null;
    }


    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        Long v;
        try{
            v=Long.parseLong(value.toString());
            combo.setSelectedItem(map.get(v));
        }catch(NumberFormatException e){

        }
        return combo;
    }
}
