/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoclicker.utils;

import java.io.Serializable;

/**
 *
 * @author yuuma0317
 */

public class Device implements Serializable{
    public MyTime[] times;
    public String   deviceName;
    public String   ip;
    public boolean  isActive;
    public int      execTimes;
    public Device(String deviceName) {
        this.deviceName   = deviceName;
        times             = null;
        isActive          = false;
        execTimes         = 15;
    }
}