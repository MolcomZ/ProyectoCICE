package gui.personal.filtros.ausencias;

import util.AutoSizeableTable;

import javax.swing.*;
import javax.swing.table.TableModel;

public class TiposAusenciaTable extends AutoSizeableTable {
    public TiposAusenciaTable(TableModel tableModel) {
        super(tableModel);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    @Override
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 0:
                return Boolean.class;
            case 1:
                return Integer.class;
            default:
                return String.class;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        switch (column){
            case 0:
                return true;
            default:
                return false;
        }
    }
}
