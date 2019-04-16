package gui.personal.util;

import jpa.turnos.TurnoEntity;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public abstract class EntityCellRenderer<T> extends DefaultTableCellRenderer {
    Component c;
    T t;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        c=super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
        t= (T) value;
        if(c instanceof JLabel) {
            if (value != null) {
                ((JLabel) c).setText(getText(t));
            } else {
                ((JLabel) c).setText("*"+value);
            }
        }
        return c;
    }
    public T getValue(){
        if(t!=null){
            return t;
        }else{
            return null;
        }
    }
    public abstract String getText(T t);
}
