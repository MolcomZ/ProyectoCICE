package gui.transporte.editors.empresas_transporte;

import util.AutoSizeableTable;

import javax.swing.table.TableModel;

public class EditorEmpresasTransporteTable extends AutoSizeableTable {

    public EditorEmpresasTransporteTable(TableModel model) {
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
