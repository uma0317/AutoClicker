/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoclicker.Register;

import autoclicker.Components.DataPanel;
import autoclicker.utils.Adb;
import autoclicker.utils.Device;
import autoclicker.utils.ProcessResults;
import autoclicker.utils.Utils;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

/**
 *
 * @author yuuma0317
 */
public class MainPanel extends JPanel implements ActionListener{
    DataPanel dataPanel;
    JList     deviceList;
    JButton   registerButton;
    public MainPanel() {
        setLayout(new BorderLayout(20, 20));
        
        dataPanel      = new DataPanel(this);
        deviceList     = new JList(Adb.devices());
        registerButton = new JButton("登録");
        
        registerButton.addActionListener(this);
        
        deviceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        add(dataPanel, BorderLayout.NORTH);
        add(deviceList, BorderLayout.CENTER);
        add(registerButton, BorderLayout.SOUTH);
    }

    public void updateList() {
        deviceList.setListData(Adb.devices());
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
        String deviceName, registerName, ip;
        
        if(deviceList.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "端末を選択してください");
            return;
        }
        
        deviceName   = (String)deviceList.getSelectedValue();
        registerName = dataPanel.getRegisterName();
        ip           = dataPanel.getIP();
        
        ProcessResults p     = Adb.connect(ip);
        Device        device = new Device(deviceName, registerName, ip);
        
        Utils.saveDevice(device);
    }
}