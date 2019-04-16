package util;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;

import javax.swing.*;
import java.awt.*;

public class BarbecueComponent extends JComponent {
    Barcode barcode;
    public BarbecueComponent(){
        setPreferredSize(new Dimension(150,100));
    }
    public void setCode(String code) throws BarcodeException {
        barcode=BarcodeFactory.createSSCC18(code);
        repaint();
    }
    public void setLabel(String label){
        barcode.setLabel(label);
        repaint();
    }
    @Override
    public void paintComponent(Graphics g) {
        if(barcode!=null){
            barcode.setSize(getSize());
            barcode.setBarWidth(getWidth()/(barcode.getData().length()*8));
            barcode.setBarHeight(getHeight()-50);
            barcode.paint(g);
        }
    }
}
