package util;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;

@XmlRootElement(name="setting")
public class UserSettings {
    public static final String FILE="settings.xml";

    private int sabadoIntColor=Color.WHITE.getRGB();
    private int domingoIntColor=Color.CYAN.getRGB();
    private ArrayList<LongColorPair> ausenciasColorList=new ArrayList<>();
    private ArrayList<LongColorPair> empresasColorList=new ArrayList<>();

    public Color getSabadoColor() {
        return new Color(sabadoIntColor);
    }

    public void setSabadoColor(int sabadoColor) {
        this.sabadoIntColor = sabadoColor;
    }

    public Color getDomingoColor() {
        return new Color(domingoIntColor);
    }

    public void setDomingoColor(int domingoColor) {
        this.domingoIntColor = domingoColor;
    }

    public int getSabadoIntColor() {
        return sabadoIntColor;
    }

    public void setSabadoIntColor(int sabadoIntColor) {
        this.sabadoIntColor = sabadoIntColor;
    }

    public int getDomingoIntColor() {
        return domingoIntColor;
    }

    public void setDomingoIntColor(int domingoIntColor) {
        this.domingoIntColor = domingoIntColor;
    }

    public ArrayList<LongColorPair> getAusenciasColorList() {
        return ausenciasColorList;
    }
    public void setAusenciasColorList(ArrayList<LongColorPair> ausenciasColorList) {
        this.ausenciasColorList = ausenciasColorList;
    }
    public void clearAusenciaColorList(){
        ausenciasColorList.clear();
    }
    public void addAusenciaColor(Long id, Color color){
        ausenciasColorList.add(new LongColorPair(id,color.getRGB()));
    }
    public Color getAusenciaColor(Long id){
        for(LongColorPair pair:ausenciasColorList){
            if(pair.getId()==id){
                return new Color(pair.getColor());
            }
        }
        return null;
    }

    public ArrayList<LongColorPair> getEmpresasColorList() {
        return empresasColorList;
    }
    public void setEmpresasColorList(ArrayList<LongColorPair> empresasColorList) {
        this.empresasColorList = empresasColorList;
    }
    public void clearEmpresasColorList(){
        empresasColorList.clear();
    }
    public void addEmpresaColor(Long id, Color color){
        empresasColorList.add(new LongColorPair(id,color.getRGB()));
    }
    public Color getEmpresaColor(Long id){
        for(LongColorPair pair:empresasColorList){
            if(pair.getId()==id){
                return new Color(pair.getColor());
            }
        }
        return null;
    }
    
    
    public void loadSettings() throws JAXBException, FileNotFoundException {
        UserSettings tempSettings;
        File file=new File(FILE);
        JAXBContext jaxbContext = JAXBContext.newInstance(UserSettings.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        tempSettings=(UserSettings) jaxbUnmarshaller.unmarshal(file);
        this.setSabadoColor(tempSettings.getSabadoIntColor());
        this.setDomingoColor(tempSettings.getDomingoIntColor());
        this.setAusenciasColorList(tempSettings.getAusenciasColorList());
        this.setEmpresasColorList(tempSettings.getEmpresasColorList());
    }

    public void saveSettings() throws JAXBException {
        File file=new File(FILE);
        JAXBContext jaxbContext=JAXBContext.newInstance(UserSettings.class);
        Marshaller jaxbMarshaller=jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
        jaxbMarshaller.marshal(this,file);
    }
}
@XmlRootElement(name="ausenciascolors")
@XmlAccessorType(XmlAccessType.FIELD)
class LongColors {
    private ArrayList<LongColorPair> longcolors = new ArrayList<>();

    public ArrayList<LongColorPair> getLongColors() {
        return longcolors;
    }

    public void setLongcolors(ArrayList<LongColorPair> longcolors) {
        this.longcolors = longcolors;
    }

    public void addLongcolors(LongColorPair pair) {
        longcolors.add(pair);
    }
    public void clear(){
        longcolors.clear();
    }

}

    @XmlRootElement(name="longcolorpair")
    //@XmlAccessorType(XmlAccessType.FIELD)
    class LongColorPair{
        Long id;
        int color;

        public LongColorPair() {
            setId(0l);
            setColor(Color.BLACK.getRGB());
        }

        public LongColorPair(Long id, int color) {
            setId(id);
            setColor(color);
        }

        public Long getId() {
            return id;
        }

        @XmlAttribute
        public void setId(Long id) {
            this.id = id;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color=color;
        }
    }
