package gui.personal.gestionpersonal.ausencias;

import util.AutoSizeableTable;
import util.calendarday.CalendarDayValueList;

import javax.swing.*;
import javax.swing.table.TableModel;

public class WeekTable extends AutoSizeableTable {
    public WeekTable(TableModel model) {
        super(model);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    @Override
    public Class<?> getColumnClass(int column) {
        switch(column){
            default:
                return CalendarDayValueList.class;
        }
    }
}
