/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoclicker.utils;

import autoclicker.MainPanel;
import java.util.TimerTask;

/**
 *
 * @author yuma
 */
public class NextDayTask extends TimerTask{
    MainPanel mp;
    public NextDayTask(MainPanel mp) {
        this.mp = mp;
    }

    @Override
    public void run() {
        mp.dailyRun();
    }
    
}
