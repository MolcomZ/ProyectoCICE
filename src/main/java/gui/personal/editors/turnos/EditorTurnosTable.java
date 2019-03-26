package gui.personal.editors.turnos;

import util.AutoSizeableTable;

import javax.swing.table.TableModel;

public class EditorTurnosTable extends AutoSizeableTable {

    public EditorTurnosTable(TableModel model) {
        super(model);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        switch(column){
            case 0:
                return false;
            default:
                return true;
        }
    }
}
