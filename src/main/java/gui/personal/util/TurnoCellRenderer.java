package gui.personal.util;

import jpa.turnos.TurnoEntity;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TurnoCellRenderer extends DefaultTableCellRenderer {
    Component c;
    TurnoEntity value;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        c=super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
        this.value=(TurnoEntity)value;
        if(c instanceof JLabel) {
            if (value != null) {
                ((JLabel) c).setText(((TurnoEntity) value).getNombre());
            } else {
                ((JLabel) c).setText("*"+value);
            }
        }
        return c;
    }
    public TurnoEntity getValue(){
        if(value!=null){
            return value;
        }else{
            return null;
        }
    }
}
