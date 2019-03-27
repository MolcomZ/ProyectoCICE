package gui.personal.editors.puestos;

import jpa.secciones.SeccionEntity;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class SeccionCellRenderer extends DefaultTableCellRenderer {
    Component c;
    SeccionEntity value;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        c=super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
        this.value=(SeccionEntity)value;
        if(c instanceof JLabel) {
            if (value != null) {
                ((JLabel) c).setText(((SeccionEntity) value).getNombre());
            } else {
                ((JLabel) c).setText("*"+value);
            }
        }
        return c;
    }
    public SeccionEntity getValue(){
        if(value!=null){
            return value;
        }else{
            return null;
        }
    }
}
