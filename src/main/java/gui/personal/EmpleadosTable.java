package gui.personal;

import util.AutoSizeableTable;

import javax.swing.*;
import javax.swing.table.TableModel;

public class EmpleadosTable extends AutoSizeableTable {
    public EmpleadosTable(TableModel tableModel) {
        super(tableModel);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    @Override
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 0:
                return Long.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            default:
                return String.class;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        switch (column){
            case 0:
                return false;
            default:
                return true;
        }
    }
}
