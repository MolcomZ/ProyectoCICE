package util;

import com.alee.laf.text.WebTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * WebTextField que lo único que hace es cambiar el foco al siguiente componente cuando se pulsa <i>enter</i>.
 * Para capturar el texto añadir un focuslistener en lugar de actionlistener.
 */
public class WebTextFieldEnterFocus extends WebTextField {
    public WebTextFieldEnterFocus(){
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transferFocus();
            }
        });
    }
}
