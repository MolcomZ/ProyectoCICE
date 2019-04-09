package gui.personal.gestion_empleado;

import util.AutoSizeableTable;

import javax.swing.table.TableModel;

public class PuestosTable extends AutoSizeableTable {
    public PuestosTable(TableModel model) {
        super(model);
    }

    @Override
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 0:
                return Long.class;
            default:
                return String.class;
        }
    }
    @Override
    public boolean isCellEditable(int row, int column) {
        switch (column){
            default:
                return false;
        }
    }

}
