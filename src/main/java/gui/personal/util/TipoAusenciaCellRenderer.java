package gui.personal.util;

import jpa.ausencias.TiposAusenciaEntity;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TipoAusenciaCellRenderer extends DefaultTableCellRenderer {
    Component c;
    TiposAusenciaEntity value;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        c=super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
        this.value=(TiposAusenciaEntity)value;
        if(c instanceof JLabel) {
            if (value != null) {
                ((JLabel) c).setText(((TiposAusenciaEntity) value).getNombre());
            } else {
                ((JLabel) c).setText("*"+value);
            }
        }
        return c;
    }
    public TiposAusenciaEntity getValue(){
        if(value!=null){
            return value;
        }else{
            return null;
        }
    }
}
