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
        try {
            Process connectPs = new ProcessBuilder("./adb.exe", "connect", device.ip).start();
            connectPs.waitFor();
            if(connectPs.exitValue() == 0) {
                System.out.println("connected: " + device.ip);
            }
            connectPs.destroy();

            ProcessResults macroExecPsResults = ProcessExecuter.exec("./adb.exe", "-s", device.deviceName, "shell", "sh", "/sdcard/" + device.deviceName + ".txt");
            System.out.println(macroExecPsResults.result);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("完了");
    }
}
