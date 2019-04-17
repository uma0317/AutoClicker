/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoclicker;

import javax.swing.JPanel;
import javax.swing.JFrame;

/**
 *
 * @author yuuma0317
 */
public class SeparateFrame extends JFrame {
    public SeparateFrame(JPanel panel, String name) {
        super(name);
        add(panel);
        
        setSize(500,500);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
