/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoclicker.Manage;

import autoclicker.utils.Adb;
import autoclicker.utils.Device;
import autoclicker.utils.MyTime;
import autoclicker.utils.ProcessExecuter;
import autoclicker.utils.ProcessResults;
import autoclicker.utils.Utils;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author yuuma0317
 */
public class MainPanel extends JPanel {
    ListAndInfoPanel listAndInfoPanel;
    ButtonRow        btnRow;
    JButton          updateButton, deleteButton;
    
    public MainPanel() {
        setLayout(new BorderLayout(20, 20));
        
        listAndInfoPanel = new ListAndInfoPanel(this);
        btnRow           = new ButtonRow(listAndInfoPanel);
        
        add(listAndInfoPanel, BorderLayout.CENTER);
        add(btnRow, BorderLayout.SOUTH);
    }
}

class ListAndInfoPanel extends JPanel {
    JScrollPane         deviceListPane   = new JScrollPane();
    ArrayList<String>   devicesArrayList = new ArrayList<>();
    Map<String, Device> devices          = new HashMap<>();
    Rows                infoPanel        = new Rows(this);
    Device              selectedDevice   = null;
    MainPanel           mp;
    ListAndInfoPanel    lp;
    JList               deviceList;
    public ListAndInfoPanel(MainPanel mp) {
        setLayout(new BorderLayout(20, 20));
        
        this.mp  = mp;
        this.lp  = this;
        File dir = new File("devices");
        
        File[] deviceFiles = dir.listFiles();
        for(File deviceFile: deviceFiles) {
            if(deviceFile.getName().contains(".bin")) {
                String fileName = deviceFile.getName().split(".bin")[0];
                devicesArrayList.add(fileName);
                devices.put(fileName, Utils.getDeviceByName(fileName));
            }
        }
        
        deviceList = new JList(devicesArrayList.toArray(new String[devicesArrayList.size()]));
        deviceListPane.setViewportView(deviceList);
        deviceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                
        deviceList.addListSelectionListener((ListSelectionEvent e) -> {
            try {
                String deviceName = (String)deviceList.getSelectedValue();
                selectedDevice    = devices.get(deviceName);
                
                remove(infoPanel);
                infoPanel = new Rows(lp);
                add(infoPanel, BorderLayout.CENTER);
                revalidate();
                repaint();
                
                if (selectedDevice.times == null) {

                } else {
                    
                    for (int i = 0; i < selectedDevice.execTimes; i++) {
                        infoPanel.timeRows.timeRows[i].hoursCombo.setSelectedIndex(selectedDevice.times[i].hour);
                        infoPanel.timeRows.timeRows[i].minutesCombo.setSelectedIndex(selectedDevice.times[i].minute);
                    }
                }
                if (selectedDevice.isActive) {
                    mp.btnRow.checkJob.setSelected(true);
                }
            } catch (java.lang.NullPointerException error) {
                error.printStackTrace();
            }
        });
        
        add(deviceListPane, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.CENTER);
    }
    
    public MyTime[] getTimes() {
        TimeRow[] timeRows = infoPanel.timeRows.timeRows;
        MyTime[]  myTime   = new MyTime[selectedDevice.execTimes];
        
        for(int i = 0; i < selectedDevice.execTimes; i++) {
            myTime[i] = timeRows[i].getTime();
        }
        
        return myTime;
    }
    
    public Device getSelectedDevice() {
        String deviceName = (String) this.deviceList.getSelectedValue();
        Device device     = this.devices.get(deviceName);
        
        return device;
    }
    
    public void updateInfoPanel(int execTImes) {
        selectedDevice.execTimes = execTImes;
        remove(lp.infoPanel);
        infoPanel = new Rows(lp);
        add(lp.infoPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
class Rows extends JPanel {
    JScrollPane      timeRowsPane = new JScrollPane();
    ListAndInfoPanel lp;
    ConnectRow       connectRow;
    TimeRowsPanel    timeRows;
    TimeRow         ipRow;
    
    public Rows(ListAndInfoPanel lp) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        this.lp    = lp;
        connectRow = new ConnectRow(lp);
        add(connectRow);
        
        timeRows = new TimeRowsPanel(lp);
        timeRowsPane.setViewportView(timeRows);
        add(timeRowsPane);
    }
}

class TimeRowsPanel extends JPanel {
    JScrollPane      timeRowsPane = new JScrollPane();
    ListAndInfoPanel lp;
    TimeRow[]        timeRows;
    
    public TimeRowsPanel(ListAndInfoPanel lp) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.lp = lp;
        
        if(lp.selectedDevice == null) return;
        
        timeRows = new TimeRow[lp.selectedDevice.execTimes];
        
        for (int i = 0; i < lp.selectedDevice.execTimes; i++) {
            timeRows[i] = new TimeRow("指定時刻" + (i + 1));
            if (lp.selectedDevice.times.length - 1 >= i) {
                timeRows[i].hoursCombo.setSelectedIndex(lp.selectedDevice.times[i].hour);
                timeRows[i].minutesCombo.setSelectedIndex(lp.selectedDevice.times[i].minute);
            }
            add(timeRows[i]);
        }
    }
}

class ConnectRow extends JPanel implements ActionListener{
    JComboBox        timesCombo = new JComboBox<>();
    ListAndInfoPanel lp;
    JLabel           execLabel;
    JButton          updateButton;
    
    public ConnectRow(ListAndInfoPanel lp) {
        this.lp = lp;
        
        execLabel = new JLabel("実行回数");
        for (int i = 0; i < 99; i++) {
            timesCombo.addItem(i);
        }
        
        updateButton = new JButton("更新");
        updateButton.addActionListener(this);
        
        add(execLabel);
        add(timesCombo);
        add(updateButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MyTime[] myTimes = lp.getTimes();

        lp.selectedDevice.times = myTimes;
        lp.updateInfoPanel((int)timesCombo.getSelectedItem());
    }
}

class TimeRow extends JPanel {
    JComboBox hoursCombo   = new JComboBox<>();
    JComboBox minutesCombo = new JComboBox<>();
    JLabel    label;
    
    public TimeRow(String labelName) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 20));
        
        label = new JLabel(labelName);
        
        int[] hoursArrayList   = new int[24];
        int[] minutesArrayList = new int[60];
        
        for (int i = 0; i < 24; i++) {
            hoursCombo.addItem(i);
        }
        for (int i = 0; i < 60; i++) {
            minutesCombo.addItem(i);
        }
        
        add(label);
        add(hoursCombo);
        add(minutesCombo);
    }
    
    public TimeRow(String labelName, int hour, int minute) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 20));
        
        label = new JLabel(labelName);
        
        int[] hoursArrayList = new int[24];
        int[] minutesArrayList = new int[60];
        
        for (int i = 0; i < 24; i++) {
            hoursCombo.addItem(i);
        }
        for (int i = 0; i < 60; i++) {
            minutesCombo.addItem(i);
        }
        
        hoursCombo.setSelectedIndex(hour);
        minutesCombo.setSelectedIndex(minute);
        
        add(label);
        add(hoursCombo);
        add(minutesCombo);
    }
    
    public MyTime getTime() {
        return new MyTime((int)hoursCombo.getSelectedItem(), (int)minutesCombo.getSelectedItem());
    }
}

class ButtonRow extends JPanel implements ActionListener{
    ListAndInfoPanel lp;
    JCheckBox        checkJob;
    JButton          saveButton;
    JButton          execButton;
    
    public ButtonRow(ListAndInfoPanel lp) {
        this.lp    = lp;
        checkJob   = new JCheckBox("定期実行");
        saveButton = new JButton("保存");
        execButton = new JButton("マクロ確認");
        
        saveButton.addActionListener(this);
        execButton.addActionListener(this);
        
        add(checkJob);
        add(saveButton);
        add(execButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(lp.selectedDevice == null) {
            JOptionPane.showMessageDialog(null, "端末を選択してください");
            return;
        }
        
        if (e.getSource() == execButton) {
            String deviceName = (String) this.lp.deviceList.getSelectedValue();
            Device device     = this.lp.devices.get(deviceName);
            
            ProcessResults macroExecPsResults = ProcessExecuter.exec(Adb.DEBUG_BIN_PATH, "-s", device.deviceName, "shell", "sh", "/sdcard/" + device.deviceName + ".txt");
            System.out.println(macroExecPsResults.result);

        } else if(e.getSource() == saveButton) {
            MyTime[] myTimes    = lp.getTimes();
            String   deviceName = (String) this.lp.deviceList.getSelectedValue();
            Device   device     = this.lp.devices.get(deviceName);
            
            System.out.println(device.deviceName);
            System.out.println(myTimes);
            
            device.times    = myTimes;
            device.isActive = checkJob.isSelected();

            Utils.saveDevice(device);
        }
    }
}