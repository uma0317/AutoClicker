/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoclicker.utils;

import autoclicker.Register.MainPanel;
import autoclicker.Record.RecordPanel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author yuuma0317
 */
public class Utils {
    public static Device getDeviceByName(String deviceName) {
        ObjectInputStream objInStream;
        Device device = null;
        
        try {
            objInStream = new ObjectInputStream(new FileInputStream("devices/" + deviceName + ".bin"));
            device      = (Device) objInStream.readObject();
            
            objInStream.close();
        } catch (IOException ex) {
            Logger.getLogger(RecordPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RecordPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return device;
    }
    
    public static ArrayList<Device> getDeviceFromFile() {
        ArrayList<String> deviceNames = new ArrayList<>();
        File              dir         = new File("devices");
        Device            device       = null;
        File[]            deviceFiles = dir.listFiles();
        ArrayList<Device> devices     = new ArrayList<>();
        ObjectInputStream objInStream;
        
        for(File deviceFile: deviceFiles) {
            try {
                objInStream = new ObjectInputStream(new FileInputStream(deviceFile));
                device      = (Device) objInStream.readObject();
                
                objInStream.close();
            } catch (IOException ex) {
                Logger.getLogger(RecordPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(RecordPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            devices.add(device);
        }
        
        return devices;
    }
    
    public static void saveDevice(Device device) {
        try {
            ObjectOutputStream objOutStream = new ObjectOutputStream(new FileOutputStream("devices/" + device.deviceName + ".bin"));
            
            objOutStream.writeObject(device);
            objOutStream.close();
            JOptionPane.showMessageDialog(null, "保存成功");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static String convertGetEventToSendEvent(String letters) {
        String[]      lettersArray = letters.split("\n");
        String        eventNum     = null;
        StringBuilder converted    = new StringBuilder();
        boolean       tapping      = false;
        
        for(String letter: lettersArray) {
            if(letter.split(" ")[0].equals("sleep")) {
                converted.append(letter + "\n");
                continue;
            }

            Pattern eventNumPattern = Pattern.compile("^/dev/input/event([\\d])++");
            Matcher eventNumMatcher = eventNumPattern.matcher(letter);
            
            if(eventNumMatcher.find()) {
                eventNum = eventNumMatcher.group(1);
            }
            
            Pattern p = Pattern.compile("([a-f\\d]+) ([a-f\\d]+) ([a-f\\d]+)");
            Matcher m = p.matcher(letter);

            if (m.find()) {
                long[] codes = new long[3];
                for (int i = 0; i < 3; i++) {
                    codes[i] = Long.parseLong(m.group(i + 1), 16);
                }
                
                if(codes[1] == 57) {
                    tapping = true;
                }
                if(tapping) {
                    converted.append("sendevent /dev/input/event" + eventNum + " " + codes[0] + " " + codes[1] + " " + codes[2] + "\n");
                }
                if(codes[0] == 0 && codes[1] == 0 && codes[2] == 0) {
                    tapping = false;
                }
            }
        }

        return converted.toString();
    }
    
    
}
