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
public class MyTime implements Serializable {
    public int hour;
    public int minute;
    
    public MyTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }
}
