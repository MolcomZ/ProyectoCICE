package gui.personal.util;

import com.alee.laf.text.WebFormattedTextField;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class DateCellRenderer extends DefaultTableCellRenderer {
    WebFormattedTextField component;
    public DateCellRenderer() {
        component=new WebFormattedTextField();
        component.setDrawBorder(false);
        component.setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c=super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
        component.setOpaque(c.isOpaque());
        component.setBackground(c.getBackground());
        component.setForeground(c.getForeground());
        component.setValue(value);
        return component;
    }
}
