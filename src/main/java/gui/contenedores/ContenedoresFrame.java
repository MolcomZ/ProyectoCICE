package gui.contenedores;

import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.scroll.WebScrollPane;
import net.miginfocom.swing.MigLayout;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import util.BarbecueComponent;
import util.WebTextFieldEnterFocus;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;
import java.util.ArrayList;

public class ContenedoresFrame{
    private MigLayout layout;
    private WebFrame frame;
    private WebButton pasteButton;
    private WebScrollPane scroll;
    private JComponent barcodesPane;
    private WebButton closeButton;

    public ContenedoresFrame(){
        frame=new WebFrame("Lectura de contenedores");
        layout=new MigLayout("","[GROW]","[][GROW][]");
        frame.setLayout(layout);
        pasteButton=new WebButton("PASTE");
        barcodesPane= new JComponent(){};
        barcodesPane.setLayout(new VerticalFlowLayout());
        //barcodesPane.setLayout(new BoxLayout(barcodesPane,BoxLayout.Y_AXIS));
        scroll=new WebScrollPane(barcodesPane);
        closeButton=new WebButton("CERRAR");

        frame.add(pasteButton,"WRAP");
        frame.add(scroll,"GROW,WRAP");
        frame.add(closeButton,"RIGHT");
        frame.pack();
        initListeners();
    }
    private void initListeners(){
        pasteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    paste();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (UnsupportedFlavorException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
    public void showFrame(){
        frame.setVisible(true);
    }
    public void paste() throws IOException, UnsupportedFlavorException {
        String string;
        String fragment = "";
        char c;

        Clipboard clipboard=Toolkit.getDefaultToolkit().getSystemClipboard();
        string=(String)clipboard.getData(DataFlavor.stringFlavor);
        for(int n=0;n<string.length();n++){
            c=string.charAt(n);
            if(Character.isLetterOrDigit(c)){
                fragment+=c;
            }else{
                if(fragment.length()>0){
                    addBarcode(fragment);
                    fragment="";
                }
            }
        }
        if(fragment.length()>0){
            addBarcode(fragment);
        }
    }
    private void addBarcode(String code){
        BarbecueComponent barcode=new BarbecueComponent();
        try {
            barcode.setCode(code);
        } catch (BarcodeException e) {
            e.printStackTrace();
        }
        barcodesPane.add(barcode);
        barcodesPane.revalidate();
    }
}

