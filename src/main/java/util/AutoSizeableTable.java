package util;

import com.alee.laf.menu.WebMenu;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.laf.separator.WebSeparator;
import com.alee.laf.table.WebTable;

import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AutoSizeableTable extends WebTable {
    private static final Integer SAFE=6;
    private static final Integer HEADERSAFE=14;

    public AutoSizeableTable(TableModel model) {
        super(model);
        this.setAutoResizeMode(WebTable.AUTO_RESIZE_LAST_COLUMN);
        initListenerMenu();
    }

    public void adjustColumn(Integer column){
        adjustColumnMax(column,0);
    }
    public void adjustColumns(){
        for(Integer n=0;n<getColumnCount();n++){
            adjustColumn(n);
        }
    }
    public void adjustColumnsTitle(){
        for(Integer n=0;n<getColumnCount();n++){
            adjustColumnTitle(n);
        }
    }
    public void adjustColumnTitle(Integer column){
        String headerText=this.getColumnName(column);
        Graphics g=this.getTableHeader().getGraphics();
        Integer headerWidth=g.getFontMetrics().stringWidth(headerText);
        adjustColumnMax(column,headerWidth+HEADERSAFE);
    }
    private void adjustColumnMax(Integer column, Integer max){
        int width;

        Graphics g=this.getGraphics();

        for(int n=0;n<this.getRowCount();n++){
            width=g.getFontMetrics().stringWidth(this.getValueAt(n,column).toString());
            if(width>max){
                max=width;
            }
        }
        this.getColumn(this.getColumnName(column)).setPreferredWidth(max+SAFE);
    }
    private void initListenerMenu(){

        this.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton()==MouseEvent.BUTTON3){
                    Integer column=+AutoSizeableTable.this.getTableHeader().columnAtPoint(e.getPoint());
                    showMenu(e.getX(),e.getY(),column);
                }
            }
        });
    }
    private void showMenu(Integer x, Integer y,Integer column){
        String name=this.getColumnName(column);
        WebPopupMenu menu=new WebPopupMenu();
        WebMenuItem adjustColumnItem=new WebMenuItem("Ajusta columna '"+name+"'");
        WebMenuItem adjustColumnTitleItem=new WebMenuItem("Ajusta columna '"+name+"' (incluído título)");
        WebMenuItem adjustColumnsItem=new WebMenuItem("Ajusta todas las columnas");
        WebMenuItem adjustColumnsTitleItem=new WebMenuItem("Ajusta todas las columnas (incluído título)");

        adjustColumnItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adjustColumn(column);
            }
        });
        adjustColumnTitleItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adjustColumnTitle(column);
            }
        });
        adjustColumnsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adjustColumns();
            }
        });
        adjustColumnsTitleItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adjustColumnsTitle();
            }
        });

        menu.add(adjustColumnItem);
        menu.add(adjustColumnTitleItem);
        menu.add(new WebSeparator());
        menu.add(adjustColumnsItem);
        menu.add(adjustColumnsTitleItem);
        menu.show(this,x,y);
    }
}
