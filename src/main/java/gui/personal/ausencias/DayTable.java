package gui.personal.ausencias;

import jpa.puestos.PuestoEntity;
import jpa.turnos.TurnoEntity;
import util.AutoSizeableTable;
import util.calendarday.CalendarDayValueList;

import javax.swing.*;
import javax.swing.table.TableModel;

public class DayTable extends AutoSizeableTable {
    public DayTable(TableModel model) {
        super(model);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    @Override
    public Class<?> getColumnClass(int column) {
        switch(column){
            case 0:
                return Long.class;
            case 1:
                return TurnoEntity.class;
            case 2:
                return PuestoEntity.class;
            default:
                return CalendarDayValueList.class;
        }
    }
}
