package gui.settings.appearance;

import com.alee.laf.panel.WebPanel;
import com.alee.laf.tabbedpane.WebTabbedPane;
import net.miginfocom.swing.MigLayout;

public class AppearancePane extends WebPanel {
    private MigLayout layout;
    private WebTabbedPane tabbedPane;
    private AusenciasPane ausenciasPane;
    private CalendariosPane calendariosPane;
    public AppearancePane(){
        layout=new MigLayout();
        tabbedPane=new WebTabbedPane();
        ausenciasPane=new AusenciasPane();
        tabbedPane.addTab("AUSENCIAS",ausenciasPane);
        calendariosPane=new CalendariosPane();
        tabbedPane.addTab("CALENDARIOS",calendariosPane);
        add(tabbedPane);
        revalidate();
    }
    public void saveSettings(){
        ausenciasPane.saveSettings();
        calendariosPane.saveSettings();
    }
}
