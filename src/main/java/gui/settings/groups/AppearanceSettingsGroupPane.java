package gui.settings.groups;

import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.tabbedpane.WebTabbedPane;
import gui.settings.individual.AusenciasColorsSettingsPane;
import gui.settings.individual.CalendariosColorsSettingsPane;
import gui.settings.individual.EmpresasTransporteColorsSettingsPane;
import jpa.empresas_transporte.EmpresasTransporteEntity;
import net.miginfocom.swing.MigLayout;

public class AppearanceSettingsGroupPane extends WebPanel {
    private AusenciasColorsSettingsPane ausenciasPane;
    private CalendariosColorsSettingsPane calendariosPane;
    private EmpresasTransporteColorsSettingsPane empresasPane;

    public AppearanceSettingsGroupPane(){
        setLayout(new VerticalFlowLayout());
        ausenciasPane=new AusenciasColorsSettingsPane();
        calendariosPane=new CalendariosColorsSettingsPane();
        empresasPane=new EmpresasTransporteColorsSettingsPane();
        add(ausenciasPane);
        add(calendariosPane);
        add(empresasPane);
    }
    public void saveSettings(){
        ausenciasPane.saveSettings();
        calendariosPane.saveSettings();
        empresasPane.saveSettings();
    }
}
