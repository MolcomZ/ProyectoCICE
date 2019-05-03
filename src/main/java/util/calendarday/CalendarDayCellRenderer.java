package util.calendarday;

import gui.PrincipalFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarDayCellRenderer extends DefaultTableCellRenderer {
    CalendarDayComponent c;

    public CalendarDayCellRenderer(){
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        c=new CalendarDayComponent();
        int dayofweek=getDayOfWeek(table.getColumnName(column));
        if(dayofweek==7){
            setBackgroundColor(PrincipalFrame.setting.getSabadoColor());
        }else{
            if(dayofweek==Calendar.SUNDAY){
                setBackgroundColor(PrincipalFrame.setting.getDomingoColor());
            }
        };
        try {
            c.setSelected(isSelected);
            c.setValues((CalendarDayValueList) value);
        }catch(Exception e){

        }

        return c;
    }
    public void setBackgroundColor(Color color){
        c.setBackground(color);
    }
    private int getDayOfWeek(String string){
        try {
            Calendar cal=Calendar.getInstance();
            cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(string));
            return cal.get(Calendar.DAY_OF_WEEK);
        } catch (ParseException e) {
            return 0;
        }
    }
}
