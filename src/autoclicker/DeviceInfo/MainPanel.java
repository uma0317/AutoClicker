/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoclicker.DeviceInfo;

import autoclicker.utils.Device;
import autoclicker.utils.Utils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author yuuma0317
 */
public class MainPanel extends JPanel {
    ListAndInfoPanel listAndInfoPanel;
    JButton          updateButton;
    JButton          deleteButton;
    ButtonRow        btnRow;
    
    public MainPanel() {
        setLayout(new BorderLayout(20, 20));
        
        listAndInfoPanel = new ListAndInfoPanel();
        btnRow           = new ButtonRow(listAndInfoPanel);
        
        add(listAndInfoPanel, BorderLayout.CENTER);
        add(btnRow, BorderLayout.SOUTH);
    }
}

class ListAndInfoPanel extends JPanel {
    JScrollPane         scrollPane       = new JScrollPane();
    ArrayList<String>   devicesArrayList = new ArrayList<>();
    Map<String, Device> devices          = new HashMap<>();
    JList               deviceList;

    public ListAndInfoPanel() {
        setLayout(new BorderLayout(20, 20));
        File   dir        = new File("devices");
        File[] deviceFiles = dir.listFiles();
        
        for(File deviceFile: deviceFiles) {
            if(deviceFile.getName().contains(".bin")) {
                String fileName = deviceFile.getName().split(".bin")[0];
                devicesArrayList.add(fileName);
                devices.put(fileName, Utils.getDeviceByName(fileName));
            }
        }
        
        deviceList = new JList(devicesArrayList.toArray(new String[devicesArrayList.size()]));
        scrollPane.setViewportView(deviceList);
        deviceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                
        deviceList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
            }
        });
        
        add(scrollPane, BorderLayout.NORTH);
    }
}

class ButtonRow extends JPanel implements ActionListener{
    JButton deleteButton;
    ListAndInfoPanel lp;
    public ButtonRow(ListAndInfoPanel lp) {
        this.lp      = lp;
        deleteButton = new JButton("削除");
        
        deleteButton.addActionListener(this);
        
        add(deleteButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (lp.deviceList.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "端末を選択してください");
            return;
        }

        String deviceName = (String) this.lp.deviceList.getSelectedValue();

        File file = new File("devices/" + deviceName + ".bin");

        if (file.exists()){
          if (file.delete()){
            JOptionPane.showMessageDialog(null, "端末を削除しました");
          }else{
            JOptionPane.showMessageDialog(null, "端末の削除に失敗しました");
          }
        }else{
          JOptionPane.showMessageDialog(null, "ファイルが見つかりませんでした");
        }

        JFrame f = (JFrame) SwingUtilities.getWindowAncestor(this);
        f.dispose();
    }
}