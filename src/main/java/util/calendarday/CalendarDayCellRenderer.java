package util.calendarday;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;

public class CalendarDayCellRenderer extends DefaultTableCellRenderer {
    CalendarDayComponent c;

    public CalendarDayCellRenderer(){
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        c=new CalendarDayComponent();
        try {
            c.setSelected(isSelected);
            c.setValues((CalendarDayValueList) value);
        }catch(Exception e){

        }
        return c;
    }
}
