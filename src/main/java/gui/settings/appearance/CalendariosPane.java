package gui.settings.appearance;

import com.alee.extended.image.WebDecoratedImage;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.laf.button.WebButton;
import com.alee.laf.colorchooser.WebColorChooserDialog;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.utils.ImageUtils;
import gui.PrincipalFrame;
import jpa.ausencias.TiposAusenciaEntity;
import jpa.ausencias.TiposAusenciaService;
import net.miginfocom.swing.MigLayout;
import util.UserSettings;

import javax.swing.*;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalendariosPane extends WebPanel {
    private MigLayout layout;
    private ColorPicker domingoPicker;
    private ColorPicker sabadoPicker;

    public CalendariosPane(){
        layout=new MigLayout();
        this.setLayout(layout);
        sabadoPicker=new ColorPicker(1L,"Sábados",PrincipalFrame.setting.getSabadoColor());
        domingoPicker=new ColorPicker(2L,"Domingos",PrincipalFrame.setting.getDomingoColor());
        fillColors();
    }
    public void saveSettings(){
        PrincipalFrame.setting.setDomingoColor(domingoPicker.color.getRGB());
        PrincipalFrame.setting.setSabadoColor(sabadoPicker.color.getRGB());
        try {
            PrincipalFrame.setting.saveSettings();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    private void fillColors(){
        this.removeAll();
        sabadoPicker.setColor(PrincipalFrame.setting.getSabadoColor());
        domingoPicker.setColor(PrincipalFrame.setting.getDomingoColor());
        add(sabadoPicker,"WRAP");
        add(domingoPicker,"WRAP");
        revalidate();
    }

    class ColorPicker extends WebPanel {
        private WebButton button=new WebButton();
        private WebDecoratedImage image=new WebDecoratedImage();
        private WebLabel label=new WebLabel();
        private Long id;
        private Color color;

        public ColorPicker(Long id,String nombre,Color color){
            image.setRound(1);
            setLayout(new HorizontalFlowLayout());
            label.setText(nombre);
            setColor(color);
            setPreferredSize(new Dimension(20,20));
            this.id=id;
            add(button);
            add(label);
            initListener();
        }
        private void initListener(){
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    WebColorChooserDialog dialog=new WebColorChooserDialog();
                    dialog.setColor(color);
                    if(dialog.showDialog()==WebColorChooserDialog.OK_OPTION){
                        setColor(dialog.getColor());
                    }
                }
            });
        }
        public void setColor(Color color){
            this.color=color;
            image.setImage(ImageUtils.createColorImage(color,50,50).getSubimage(10,10,16,16));
            button.setIcon(ImageUtils.createColorIcon(color));
            button.setIcon(image.getPreviewIcon());
        }
        public Color getColor(){
            return color;
        }
    }
}
