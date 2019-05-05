package gui.settings.individual;

import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;

import java.awt.*;

public class IndividualSettingsPane extends WebPanel {
    protected WebLabel title=new WebLabel("Title");
    protected WebPanel panel=new WebPanel();

    public IndividualSettingsPane() {
        setLayout(new BorderLayout());
        add(title,BorderLayout.NORTH);
        add(panel,BorderLayout.CENTER);
        title.setBoldFont(true);
        title.setHorizontalAlignment(WebLabel.CENTER);
    }
    public void setTitle(String title){
        this.title.setText(title);
    }
    public String getTitle(){
        return title.getText();
    }
}
