/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoclicker;

import autoclicker.utils.Adb;
import autoclicker.utils.ProcessExecuter;
import autoclicker.utils.ProcessResults;
import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 *
 * @author yuuma0317
 */
public class MainFrame extends JFrame{
    public MainFrame(String name) {
        super(name);
        setSize(500, 500);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ProcessResults result = ProcessExecuter.exec(Adb.DEBUG_BIN_PATH, "start-server");
        ProcessResults result2 = ProcessExecuter.exec(Adb.DEBUG_BIN_PATH, "tcpip", "5555");
        
        System.out.println("Start adb Server: " + result.code);
        System.out.println(result.result);
    }
    
    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame("Auto Clicker");
        MainPanel mainPanel = new MainPanel();
        
        mainFrame.add(mainPanel, BorderLayout.CENTER);
        mainFrame.setVisible(true);
        
        mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(WindowEvent winEvt) {
                ProcessResults result = ProcessExecuter.exec(Adb.DEBUG_BIN_PATH, "kill-server");
                System.out.println("Kill Server: " + result.code);
                System.out.println(result.result);
                System.exit(0);
            }
        });
        mainPanel.dailyRun();
    }
}
