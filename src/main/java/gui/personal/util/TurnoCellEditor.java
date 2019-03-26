package gui.personal.util;

import com.alee.laf.combobox.WebComboBox;
import com.alee.utils.swing.PopupMenuAdapter;
import jpa.turnos.TurnoEntity;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TurnoCellEditor extends AbstractCellEditor implements TableCellEditor{
    private WebComboBox combo;
    private HashMap<Long,String> map;
    private ArrayList<TurnoEntity> list;

    public TurnoCellEditor(){
        combo=new WebComboBox();
        combo.addPopupMenuListener(new PopupMenuAdapter() {
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                if (combo.getSelectedIndex() != -1){
                    TurnoCellEditor.this.fireEditingStopped();
                }else{
                    TurnoCellEditor.this.fireEditingCanceled();
                }
            }
        });
    }

    public void setMap(HashMap<Long,String> map){
        this.map=map;
        refreshCombo();
    }
    public void setList(ArrayList<TurnoEntity> list){
        this.list=list;
        refreshCombo();
    }
    private void refreshCombo() {
        combo.removeAllItems();
        for(TurnoEntity entity:list){
            combo.addItem(entity.getNombre());
        }
        //for(Long l:map.keySet()){
            //combo.addItem(map.get(l));

        //}
    }
    @Override
    public Object getCellEditorValue() {
        String selectedItem=combo.getSelectedItem().toString();
        for(TurnoEntity entity:list){
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
