/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoclicker.Components;

import autoclicker.Register.MainPanel;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author yuma
 */

public class DataPanel extends JPanel {
    NameRow nameRow;
    ipRow   ipRow;
    
    public DataPanel(MainPanel dp) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        nameRow = new NameRow(dp);
        ipRow   = new ipRow();
        
        add(nameRow);
        add(ipRow);
    }
    
    public String getRegisterName() {
        return nameRow.field.getText();
    }
    
    public String getIP() {
        return ipRow.field.getText();
    }
}

class NameRow extends JPanel implements ActionListener{
    public MainPanel dp;
    public JTextField          field;
    public JLabel              label;
    public JButton             updateButton;
    
    public NameRow(MainPanel dp) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 20));
        
        this.dp      = dp;
        label        = new JLabel("登録名");
        field        = new JTextField(10);
        updateButton = new JButton("リスト更新");
        
        updateButton.addActionListener(this);
        
        add(label);
        add(field);
        add(updateButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dp.updateList();
    }
}

class ipRow extends JPanel {
    public JTextField field;
    public JLabel     label;
    
    public ipRow() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 20));
        
        label = new JLabel("IPアドレス");
        field = new JTextField(10);
        
        add(label);
        add(field);
    }
}