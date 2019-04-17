/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoclicker.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author yuuma0317
 */
public class ProcessExecuter {
    public ProcessExecuter() {
        
    }

    /**
     *
     * @param commands
     * @return
     */
    public static ProcessResults exec(String ... cmdarr) {
        ProcessResults results = new ProcessResults();
        try {
            Process           process = new ProcessBuilder(cmdarr).start();
            InputStreamReader isr     = new InputStreamReader(process.getInputStream(), "UTF-8");
            BufferedReader    reader  = new BufferedReader(isr);
            StringBuilder     builder = new StringBuilder();
            int               c;
            
            while ((c = reader.read()) != -1) {
                builder.append((char)c);
            }

            results.result = builder.toString();
            results.code   = process.waitFor();
            
            process.destroy();
        } catch (IOException | InterruptedException e) {
            results.result = e.toString();
            results.code   = 1;
            
            e.printStackTrace();
        }
        
        return results;
    } 
    
    public static void main(String[] args) {
        ProcessExecuter pe       = new ProcessExecuter();
        String[]        cmdarray = {"/Users/yama/Library/Android/sdk/platform-tools/adb", "devices"};
        ProcessResults  results  = pe.exec("/Users/yama/Library/Android/sdk/platform-tools/adb", "devices");
        
        System.out.println(results.result);
    }
}
