package gui.personal.util;

import com.alee.laf.combobox.WebComboBox;
import com.alee.utils.swing.PopupMenuAdapter;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.ArrayList;

public abstract class EntityCellEditor<T> extends AbstractCellEditor implements TableCellEditor{
    private WebComboBox combo;
    private ArrayList<T> list;

    public EntityCellEditor(){
        combo=new WebComboBox();
        combo.addPopupMenuListener(new PopupMenuAdapter() {
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                if (combo.getSelectedIndex() != -1){
                    EntityCellEditor.this.fireEditingStopped();
                }else{
                    EntityCellEditor.this.fireEditingCanceled();
                }
            }
        });
    }
    public void setList(ArrayList<T> list){
        this.list=list;
        refreshCombo();
    }
    public abstract String getText(T t);
    public abstract Long getID(T t);
    private void refreshCombo() {
        combo.removeAllItems();
        for(T t:list){
            combo.addItem(getText(t));
        }
    }
    @Override
    public Object getCellEditorValue() {
        String selectedItem=combo.getSelectedItem().toString();
        for(T t:list){
            if(getText(t).equals(selectedItem)){
                return t;
            }
        }
        return null;
    }
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
       for(T t:list){
            if(getID(t)==getID((T) value)){
                combo.setSelectedItem(getText(t));
            }
        }
        return combo;
    }
}
