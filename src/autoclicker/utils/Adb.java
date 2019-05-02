/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoclicker.utils;

import java.util.ArrayList;

/**
 *
 * @author yuuma0317
 */
public class Adb {
    public static final String BIN_PATH = "./adb.exe";
    public static final String DEBUG_BIN_PATH = "/Users/yama/Library/Android/sdk/platform-tools/adb";
    public static String[] devices() {
        ArrayList<String> devices   = new ArrayList<>();
        ProcessResults    psResults = ProcessExecuter.exec(DEBUG_BIN_PATH, "devices");
        
        for(String row: psResults.result.split("\n")) {
            String[] datas = row.split("\t");
            
            if(datas.length == 2 && !datas[1].equals("offline"))
                devices.add(row.split("\t")[0]); //device name                
        }
        
            String[] devicesArray = devices.toArray(new String[devices.size()]);
            return devicesArray;
    }
    
    public static ProcessResults connect(String ip) {
        return ProcessExecuter.exec(DEBUG_BIN_PATH, "connect", ip);
    }
    
    public static ProcessResults getevent() {
        return ProcessExecuter.exec(DEBUG_BIN_PATH, "getevent");
    }
    
    public static boolean isConnected(Device device) {
        ProcessResults psResults = ProcessExecuter.exec(DEBUG_BIN_PATH, "devices");
        
        for(String row: psResults.result.split("\n")) {
            String[] datas = row.split("\t");

            if(datas.length == 2 && datas[1].trim().equals("device")) {
                if ((device.ip + ":5555").equals(datas[0])) {
                    return true;
                }
            }
                             
        }
        return false;
    }
}
