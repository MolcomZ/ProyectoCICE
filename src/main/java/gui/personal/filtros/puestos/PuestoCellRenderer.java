package gui.personal.filtros.puestos;

import jpa.puestos.PuestoEntity;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class PuestoCellRenderer extends DefaultTableCellRenderer {
    Component c;
    PuestoEntity value;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        c=super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
        this.value=(PuestoEntity)value;
        if(c instanceof JLabel) {
            if (value != null) {
                ((JLabel) c).setText(((PuestoEntity) value).getNombre());
            } else {
                ((JLabel) c).setText("*"+value);
            }
        }
        return c;
    }
    public PuestoEntity getValue(){
        if(value!=null){
            return value;
        }else{
            return null;
        }
    }
}
