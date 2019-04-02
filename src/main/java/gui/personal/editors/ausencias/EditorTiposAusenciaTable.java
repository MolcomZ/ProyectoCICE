package gui.personal.editors.ausencias;

import util.AutoSizeableTable;

import javax.swing.table.TableModel;

public class EditorTiposAusenciaTable extends AutoSizeableTable {

    public EditorTiposAusenciaTable(TableModel model) {
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
