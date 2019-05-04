package util.calendarday;

import com.alee.extended.image.WebDecoratedImage;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.label.WebLabel;
import com.alee.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;

public class CalendarDayComponent extends JLabel {
    private CalendarDayValueList values;
    private boolean isSelected;
    private Color selectionColor;
    private WebDecoratedImage image;
    //private CalendarDayItem item;

    public CalendarDayComponent(){
        values=new CalendarDayValueList();
        setLayout(new VerticalFlowLayout());
        setOpaque(true);
        image=new WebDecoratedImage();
        image.setRound(1);
        UIDefaults defaults=UIManager.getDefaults();
        selectionColor=defaults.getColor("Table.selectionBackground");
//        item=new CalendarDayItem(Color.ORANGE,"TEXT");
    }
    public void setValues(CalendarDayValueList values){
        this.values=values;
        update();
    }
    private void addLabel(CalendarDayValue value){
        WebLabel label=new WebLabel(value.getText());
        label.setOpaque(true);
        image.setImage(ImageUtils.createColorImage(Color.BLUE,18,16));
        if(isSelected){
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
            label.setForeground(Color.BLACK);
            //label.setBackground(shadeColor(value.getColor()));
            label.setBackground(Color.BLUE);
        }else {
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
            label.setForeground(Color.BLACK);
            label.setBackground(value.getColor());
        }
        add(new CalendarDayItem(value.getColor(),value.getText()));
        //add(image);
        //add(label);
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
        if(selected){
            setBackground(selectionColor);
        }else{
            //setBackground(Color.WHITE);
        }
    }
    private Color shadeColor(Color color){
        int shade=2;
        int r,g,b;
        int r2,g2,b2;
        r=color.getRed();
        g=color.getGreen();
        b=color.getBlue();
        r2=selectionColor.getRed();
        g2=selectionColor.getGreen();
        b2=selectionColor.getBlue();
        r=(r+r2)/shade;
        g=(g+g2)/shade;
        b=(b+b2)/shade;
        return new Color(r,g,b);
    }
    class CalendarDayItem extends JComponent{
        private WebDecoratedImage image;
        private WebLabel label;
        private Color color;
        private String text;

        public CalendarDayItem(Color color,String text){
            this.color=color;
            this.text=text;
            image=new WebDecoratedImage();
            image.setRound(1);
            label=new WebLabel("e8e8e8");
            setLayout(new HorizontalFlowLayout());
            add(image);
            add(label);
            redraw();
        }
        public void setText(String text){
            this.text=text;
            redraw();
        }
        public void setColor(Color color){
            this.color=color;
            redraw();
        }
        private void redraw(){
            label.setText(text);
            image.setImage(ImageUtils.createColorImage(color,50,50).getSubimage(10,10,14,16));
        }
    }
}

