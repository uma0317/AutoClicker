/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoclicker;

import autoclicker.Record.RecordPanel;
import autoclicker.utils.DailyTask;
import autoclicker.utils.Device;
import autoclicker.utils.JobTask;
import autoclicker.utils.MyTime;
import autoclicker.utils.NextDayTask;
import autoclicker.utils.Utils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author yuuma0317
 */
public class MainPanel extends JPanel implements ActionListener {
    JButton           registerButton, recordButton, updateButton, schedulerButton;
    JobTask[]         jobThreads;
    ArrayList<Device> devices = new ArrayList<>();
    
    public MainPanel() {
        registerButton  = new JButton("端末登録");
        recordButton    = new JButton("操作記録");
        updateButton    = new JButton("端末情報更新");
        schedulerButton = new JButton("管理");
        devices         = Utils.getDeviceFromFile();
        
        registerButton.addActionListener(this);
        recordButton.addActionListener(this);
        updateButton.addActionListener(this);
        schedulerButton.addActionListener(this);
        
        add(registerButton);
        add(recordButton);
        add(updateButton);
        add(schedulerButton);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        SeparateFrame sf;
        if (event.getSource() == registerButton) {
            sf = new SeparateFrame(new autoclicker.Register.MainPanel(), "端末登録");
        } else if(event.getSource() == recordButton){
            sf = new SeparateFrame(new RecordPanel(), "操作記録");
        } else if(event.getSource() == updateButton){
            sf = new SeparateFrame(new autoclicker.DeviceInfo.MainPanel(), "端末情報");
        } else if(event.getSource() == schedulerButton){
            sf = new SeparateFrame(new autoclicker.Manage.MainPanel(), "管理");
        }
    }
    
    //毎日実行する関数
    public void dailyRun() {
        System.out.println("Run daily task");
        SimpleDateFormat sdf   = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Timer            timer = new Timer(false);
        TimerTask        task  = new NextDayTask(this);
        LocalDateTime    d     = LocalDateTime.now();
        int              year  = d.getYear();
        int              month = d.getMonthValue();
        int              day   = d.getDayOfMonth();
        
        //今日が月の最後の場合
        if (String.valueOf(day).equals(d.with(d.getMonth()).with(lastDayOfMonth()).toString())) {
            day = 1;
            if (month == 12) {
                year = d.getYear() + 1;
                month = 1;
            } else {
                month += 1;
            }
        } else {
            day += 1;
        }

        try {
            timer.schedule(task, sdf.parse(year + "/" + month + "/" + day + " 00:00:00"));
            System.out.println(year + "/" + month + "/" + day + " 00:00:00に実行");
        } catch (ParseException ex) {
            Logger.getLogger(DailyTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //土曜と日曜日なら定期実行しない
        int dayOfWeek = d.getDayOfWeek().getValue();
        if(dayOfWeek == 6 || dayOfWeek == 7) {
            return;
        }
        
        //デバイスごとの定期実行設定
        Timer     timer2;
        TimerTask task2;
        int       hour, minutes;
        String    monthStr, dayStr, hourStr, minutesStr;
        
        for (Device device: devices) {
            if (device.times == null) {
                continue;
            }
            for (MyTime time: device.times) {
                timer2  = new Timer(false);
                task2   = new JobTask(device);
                hour    = time.hour;
                minutes = time.minute;
                
                hourStr    = formatNum(hour);
                minutesStr = formatNum(minutes);
                monthStr   = formatNum(month);
                dayStr     = formatNum(day);
                
                try {
                    Date date = sdf.parse(year + "/" + monthStr + "/" + dayStr + " " + hourStr + ":" + minutesStr + ":11");
                    LocalDateTime converted = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    int diff = d.compareTo(converted);
                    
                    //プログラム実行時より設定時間が前ならスケジュールに追加しない
                    if (diff <= 0) {
                        String t = year + "/" + monthStr + "/" + dayStr + " " + hourStr + ":" + minutesStr + ":00";
                        timer2.schedule(task2, sdf.parse(t));
                        System.out.println(t + "に設定しました");
                    }
                    
                } catch (ParseException ex) {
                    Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
        
            }

        }
    }
    
    public String formatNum(int n) {
        if (n < 10) {
            return "0" + String.valueOf(n);
        } else {
            return String.valueOf(n);
        }
    }
}
