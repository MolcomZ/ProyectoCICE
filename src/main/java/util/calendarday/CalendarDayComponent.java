package util.calendarday;

import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.laf.label.WebLabel;

import javax.swing.*;
import java.awt.*;

public class CalendarDayComponent extends JComponent {
    private CalendarDayValueList values;
    private boolean isSelected;

    public CalendarDayComponent(){
        values=new CalendarDayValueList();
        setLayout(new VerticalFlowLayout());
    }
    public void setValues(CalendarDayValueList values){
        this.values=values;
        update();
    }
    private void addLabel(CalendarDayValue value){
        WebLabel label=new WebLabel(value.getText());
        label.setOpaque(true);
        if(isSelected){
            label.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
            label.setForeground(Color.WHITE);
            label.setBackground(shadeColor(value.getColor()));
        }else {
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
            label.setForeground(Color.BLACK);
            label.setBackground(value.getColor());
        }
        add(label);
        revalidate();
    }
    private void update(){
        removeAll();
        for(CalendarDayValue value:values.getList()){
            addLabel(value);
        }
    }
    public void setSelected(boolean selected){
        isSelected=selected;
    }
    private Color shadeColor(Color color){
        int r,g,b;
        r=color.getRed();
        g=color.getGreen();
        b=color.getBlue();
        r=r/4;
        g=g/4;
        b=b/4;
        return new Color(r,g,b);
    }
}

