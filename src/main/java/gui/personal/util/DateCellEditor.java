package gui.personal.util;

import com.alee.laf.text.WebFormattedTextField;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class DateCellEditor extends AbstractCellEditor implements TableCellEditor{
    private WebFormattedTextField component;

    public DateCellEditor(){
        component=new WebFormattedTextField(new Date());
        component.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DateCellEditor.this.fireEditingStopped();
            }
        });
    }
    @Override
    public Object getCellEditorValue() {
        return component.getValue();
    }
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        component.setValue(value);
        return component;
    }

    @Override
    public void cancelCellEditing() {
        System.out.println("CANCELANDO");
        fireEditingCanceled();
    }
}
