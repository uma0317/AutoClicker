/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoclicker;

import autoclicker.Register.MainPanel;
import javax.swing.JFrame;

/**
 *
 * @author yuuma0317
 */
public class TestFrame extends JFrame {
    public static void main(String[] args) {
        TestFrame f = new TestFrame();
        MainPanel p = new MainPanel();
        
        f.add(p);
        
        f.setSize(500,500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
