/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoclicker.utils;

import autoclicker.MainPanel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yuma
 */
public class DailyTask extends TimerTask{
    ArrayList<Device> devices = new ArrayList<>();
    
    public DailyTask(ArrayList<Device> devices) {
        this.devices = devices;
    }
    
    @Override
    public void run() {
        for (Device device: devices) {
            ProcessResults connectPsResults = ProcessExecuter.exec("./adb.exe", "connect", device.ip);
            System.out.println(connectPsResults.result.split(" ")[0]);
            if (connectPsResults.result.split(" ")[0].equals("connected") || connectPsResults.result.split(" ")[0].equals("already")) {
            }
            System.out.println(connectPsResults.result);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//            Timer timer = new Timer(false);
            LocalDateTime d = LocalDateTime.now();
            
            String year = null, month = null, day = null, hour = null, minutes = null;
            
            for (MyTime time: device.times) {
                Timer timer = new Timer(false);
                TimerTask task = new JobTask(device);

                if (time.hour < 10) {
                    hour = "0" + String.valueOf(time.hour);
                } else {
                    hour = String.valueOf(time.hour);
                }
                
                if (time.minute < 10) {
                    minutes = "0" + String.valueOf(time.minute);
                } else {
                    minutes = String.valueOf(time.minute);
                }
                
                if (d.getMonthValue() < 10) {
                    month = "0" + String.valueOf(d.getMonthValue());
                } else {
                    month = String.valueOf(d.getMonthValue());
                }
                
                if (d.getDayOfMonth() < 10) {
                    day = "0" + String.valueOf(d.getDayOfMonth());
                } else {
                    day = String.valueOf(d.getDayOfMonth());
                }
                try {
                    Date date = sdf.parse(d.getYear() + "/" + month + "/" + day + " " + hour + ":" + minutes + ":00");
                    LocalDateTime converted = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    int diff = d.compareTo(converted);
                    if (diff <= 0) {
                        timer.schedule(task, sdf.parse(d.getYear() + "/" + month + "/" + day + " " + hour + ":" + minutes + ":00"));
                        System.out.println(d.getYear() + "/" + month + "/" + day + " " + hour + ":" + minutes + ":00" + "に設定しました");
         
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                
            
        }
    }
}
