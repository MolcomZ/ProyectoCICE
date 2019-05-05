package gui.settings.individual;

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
import jpa.empresas_transporte.EmpresasTransporteEntity;
import jpa.empresas_transporte.EmpresasTransporteService;
import net.miginfocom.swing.MigLayout;
import util.EntityListener;
import util.EntityListenerManager;

import javax.xml.bind.JAXBException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmpresasTransporteColorsSettingsPane extends IndividualSettingsPane {
    private MigLayout layout;

    EmpresasTransporteService service;

    public EmpresasTransporteColorsSettingsPane(){
        super();
        setTitle("Colores de empresas de transporte");
        layout=new MigLayout();
        panel.setLayout(layout);
        service= new EmpresasTransporteService(PrincipalFrame.EM);
        fillColors();
        EntityListenerManager.addListener(EmpresasTransporteEntity.class, new EntityListener() {
            @Override
            public void entityUpdated() {
                fillColors();
            }
        });
    }
    public void saveSettings(){
        PrincipalFrame.setting.clearEmpresasColorList();
        for(Component c:panel.getComponents()){
            if(c instanceof ColorPicker){
                PrincipalFrame.setting.addEmpresaColor(((ColorPicker) c).id,((ColorPicker) c).color);
            }
        }
        try {
            PrincipalFrame.setting.saveSettings();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    private void fillColors(){
        panel.removeAll();
        for(EmpresasTransporteEntity entity:service.findAllEmpresasTransporte()){
            panel.add(new ColorPicker(entity.getId(),entity.getNombre()),"WRAP");
        }
        revalidate();
    }

    class ColorPicker extends WebPanel {
        private WebButton button=new WebButton();
        private WebDecoratedImage image=new WebDecoratedImage();
        private WebLabel label=new WebLabel();
        private Long id;
        private Color color;

        public ColorPicker(Long id,String nombre){
            image.setRound(1);
//            image.setImage(new ImageIcon(getClass().getResource("/a.png")).getImage());
//            image.setDrawBorder(false);
            //button.setUndecorated(false);
            setLayout(new HorizontalFlowLayout());
            label.setText(nombre);
            if( (color=PrincipalFrame.setting.getEmpresaColor(id))!=null){
                setColor(color);
            }else{
                setColor(Color.WHITE);
            }
            setPreferredSize(new Dimension(20,20));
            this.id=id;
            panel.add(button);
            panel.add(label);
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
