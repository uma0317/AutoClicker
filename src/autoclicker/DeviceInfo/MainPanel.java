/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoclicker.DeviceInfo;

import autoclicker.utils.Device;
import autoclicker.utils.Utils;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author yuuma0317
 */
public class MainPanel extends JPanel {
    ListAndInfoPanel listAndInfoPanel;
    JButton          updateButton;
    JButton          deleteButton;
    ButtonRow        btnRow;
    
    public MainPanel() {
        setLayout(new BorderLayout(20, 20));
        
        listAndInfoPanel = new ListAndInfoPanel();
        btnRow           = new ButtonRow(listAndInfoPanel);
        
        add(listAndInfoPanel, BorderLayout.CENTER);
        add(btnRow, BorderLayout.SOUTH);
    }
}

class ListAndInfoPanel extends JPanel {
    JScrollPane         scrollPane       = new JScrollPane();
    ArrayList<String>   devicesArrayList = new ArrayList<>();
    Map<String, Device> devices          = new HashMap<>();
    Rows                infoPanel        = new Rows();
    JList               deviceList;

    public ListAndInfoPanel() {
        setLayout(new BorderLayout(20, 20));
        File   dir        = new File("devices");
        File[] deviceFiles = dir.listFiles();
        
        for(File deviceFile: deviceFiles) {
            if(deviceFile.getName().contains(".bin")) {
                String fileName = deviceFile.getName().split(".bin")[0];
                devicesArrayList.add(fileName);
                devices.put(fileName, Utils.getDeviceByName(fileName));
            }
        }
        
        deviceList = new JList(devicesArrayList.toArray(new String[devicesArrayList.size()]));
        scrollPane.setViewportView(deviceList);
        deviceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                
        deviceList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    String deviceName     = (String)deviceList.getSelectedValue();
                    Device selectedDevice = devices.get(deviceName);
                    
                    infoPanel.deviceNameRow.deviceNameLabel.setText(selectedDevice.deviceName);
                    infoPanel.registerNameRow.field.setText(selectedDevice.registerName);
                    infoPanel.ipRow.field.setText(selectedDevice.ip);
                } catch (java.lang.NullPointerException error) {
                    error.printStackTrace();
                }
            }
        });
        
        add(scrollPane, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.CENTER);
    }
    
    public String getRegisterName() {
        return infoPanel.registerNameRow.field.getText();
    }
    
    public String getIp() {
        return infoPanel.ipRow.field.getText();
    }
}

class Rows extends JPanel {
    DeviceNameRow deviceNameRow;
    Row           registerNameRow, ipRow;
    
    public Rows() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        deviceNameRow   = new DeviceNameRow();
        registerNameRow = new Row("登録名");
        ipRow           = new Row("IPアドレス");
        
        add(deviceNameRow);
        add(registerNameRow);
        add(ipRow);
    }
}

class DeviceNameRow extends JPanel {
    JLabel label;
    JLabel deviceNameLabel;
    public DeviceNameRow() {
        label           = new JLabel("端末名:");
        deviceNameLabel = new JLabel();
        
        add(label);
        add(deviceNameLabel);
    }
}

class Row extends JPanel {
    JLabel     label;
    JTextField field;
    
    public Row(String labelName) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 20));
        
        label = new JLabel(labelName);
        field = new JTextField(10);
        
        add(label);
        add(field);
    }
}

class ButtonRow extends JPanel implements ActionListener{
    JButton updateButton;
    JButton deleteButton;
    ListAndInfoPanel lp;
    public ButtonRow(ListAndInfoPanel lp) {
        this.lp      = lp;
        updateButton = new JButton("更新");
        deleteButton = new JButton("削除");
        
        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);
        
        add(updateButton);
        add(deleteButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (lp.deviceList.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "端末を選択してください");
            return;
        }
        
        if (e.getSource() == updateButton) {
            String deviceName   = (String) this.lp.deviceList.getSelectedValue();
            Device device       = this.lp.devices.get(deviceName);
            device.registerName = this.lp.getRegisterName();
            device.ip           = this.lp.getIp();

            System.out.println(device.registerName);
            System.out.println(device.ip);
            Utils.saveDevice(device);
        } else {
            String deviceName = (String) this.lp.deviceList.getSelectedValue();

            File file = new File("devices/" + deviceName + ".bin");

            if (file.exists()){
              if (file.delete()){
                JOptionPane.showMessageDialog(null, "端末を削除しました");
              }else{
                JOptionPane.showMessageDialog(null, "端末の削除に失敗しました");
              }
            }else{
              JOptionPane.showMessageDialog(null, "ファイルが見つかりませんでした");
            }

            JFrame f = (JFrame) SwingUtilities.getWindowAncestor(this);
            f.dispose();
        }
    }
}