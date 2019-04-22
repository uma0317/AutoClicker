/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoclicker.utils;

import java.io.IOException;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yuuma0317
 */
public class JobTask extends TimerTask {
    Device device;
    public JobTask(Device device) {
        this.device = device;
    }
    public void run() {
        System.out.println("実行");
        
        ProcessResults macroExecPsResults = ProcessExecuter.exec("./adb.exe", "-s", device.deviceName, "shell", "sh", "/sdcard/" + device.deviceName + ".txt");
        System.out.println(macroExecPsResults.result);
 
        System.out.println("完了");
    }
}
