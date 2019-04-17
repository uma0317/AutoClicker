/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoclicker.Record;

import autoclicker.utils.Adb;
import autoclicker.utils.Device;
import autoclicker.utils.ProcessExecuter;
import autoclicker.utils.ProcessResults;
import autoclicker.utils.Utils;
import autoclicker.Components.DataPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

/**
 *
 * @author yuuma0317
 */
public class RecordPanel extends JPanel implements ActionListener{
    DataPanel         dataPanel;
    JList             deviceList;
    JButton           recordButton, stopButton;
    GetEventThread    getEventThread;
    ArrayList<String> attachedDevices = new ArrayList<>();
            
    public RecordPanel() {
        setLayout(new BorderLayout(20, 20));
        
        File dir = new File("devices");
        
        File[] deviceFiles = dir.listFiles();
        for(File deviceFile: deviceFiles) {
            if(deviceFile.getName().contains(".bin"))
                attachedDevices.add(deviceFile.getName().split(".bin")[0]);
        }
        
        deviceList = new JList(attachedDevices.toArray(new String[attachedDevices.size()]));
        recordButton = new JButton("操作記録");
        stopButton = new JButton("停止");
        
        recordButton.addActionListener(this);
        stopButton.addActionListener(this);
        
        deviceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        add(deviceList, BorderLayout.CENTER);
        add(recordButton, BorderLayout.SOUTH);
    }

    public void updateList() {
        deviceList.setListData(Adb.devices());
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String deviceName = null;
        Device device = null;
        
        if(deviceList.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "端末を選択してください");
            return;
        }
        
        deviceName = (String) deviceList.getSelectedValue();
        device     = Utils.getDeviceByName(deviceName);

        if(e.getSource() == recordButton) {
            getEventThread = new GetEventThread(device);
            getEventThread.start();
            remove(recordButton);
            add(stopButton, BorderLayout.SOUTH);
        } else {
//            try {
//                Process process = new ProcessBuilder("./adb.exe", "disconnect", device.ip).start();
//                process.waitFor();
//                process.destroy();
//
//                System.out.println("disconnect: " + process.exitValue());
//                Process connectPs = new ProcessBuilder("./adb.exe", "connect", device.ip).start();
//                connectPs.waitFor();
//                if(connectPs.exitValue() == 0) {
//                    System.out.println("connected: " + device.ip);
//                }
//                connectPs.destroy();
                ProcessResults getPsNumResults = ProcessExecuter.exec("./adb.exe", "-s", device.deviceName, "shell", "ps", "|", "grep", "getevent");
                Pattern p = Pattern.compile("[0-9]+");
                Matcher m = p.matcher(getPsNumResults.result);
                if(m.find()) {
                    String psNum = m.group();
                    System.out.println(psNum);
                    ProcessResults killPsResults = ProcessExecuter.exec("./adb.exe", "-s", device.deviceName, "shell", "kill", psNum);
                    ProcessResults pushPsResults = ProcessExecuter.exec("./adb.exe", "-s", device.deviceName, "push", "records/" + device.deviceName + ".txt", "/sdcard/" + device.deviceName + ".txt");
                    System.out.println("psuh: " + pushPsResults.result);  
                }

//            } catch (InterruptedException ex) {
//                Logger.getLogger(RecordPanel.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (IOException ex) {
//                Logger.getLogger(RecordPanel.class.getName()).log(Level.SEVERE, null, ex);
//            }
            remove(stopButton);
            add(recordButton, BorderLayout.SOUTH);
        }

        revalidate();
        repaint();
    }
}

class GetEventThread extends Thread {
    String            result;
    Device            device;
    Process           process;
    InputStreamReader isr;
    BufferedReader    reader;
    
    public GetEventThread(Device device) {
        this.device = device;
    }
    
    public void run() {
        System.out.println("start");
        try {
//            Process connectPs = new ProcessBuilder("./adb.exe", "connect", device.ip).start();
//            connectPs.waitFor();
//            if(connectPs.exitValue() == 0) {
//                System.out.println("connected: " + device.ip);
//            }
//            connectPs.destroy();
            
            int           exitCode = 1;
            StringBuilder builder  = new StringBuilder();

            for (int i = 0; i < 10 && exitCode == 1; i++) {
                process = new ProcessBuilder("./adb.exe", "-s", device.deviceName, "shell", "getevent").start();
                isr    = new InputStreamReader(process.getInputStream(), "UTF-8");
                reader = new BufferedReader(isr);
                builder = new StringBuilder();
                
                long   end    = 0, start = System.currentTimeMillis(), diff = 0;
                double millis = 0.0;
                int    c;
                
                while ((c = reader.read()) != -1) {
                    if(c == '\n') {
                        end   = System.currentTimeMillis();
                        diff  = end - start;
                        start = System.currentTimeMillis();
                    }
                    if(diff > 1000) {
                        double     millisToSeconds = diff / 1000.0;
                        BigDecimal x               = new BigDecimal(millisToSeconds);
                        
                        x = x.setScale(3, BigDecimal.ROUND_HALF_UP);
                        System.out.println("sleep: " + x);
                        
                        builder.append("\nsleep " + x);
                        diff = 0;
                    }
                    builder.append((char)c);
                }
                
                exitCode = process.waitFor();
                
                System.out.println("getevent: " + exitCode);
                process.destroy();
            }
            
            FileWriter  file = new FileWriter("records/" + device.deviceName + ".txt");
            PrintWriter pw   = new PrintWriter(new BufferedWriter(file));
            
            pw.println(Utils.convertGetEventToSendEvent(builder.toString()));
            pw.close();
            
            System.out.println("success");
            System.out.println("getevent thread finish: " + process.exitValue());
        } catch (IOException error) {
            error.printStackTrace();
        } catch (InterruptedException ex) {
            Logger.getLogger(GetEventThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}

