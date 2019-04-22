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
    ListUpdateRow nameRow;
    
    public DataPanel(MainPanel dp) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        nameRow = new ListUpdateRow(dp);
        
        add(nameRow);
    }
}

class ListUpdateRow extends JPanel implements ActionListener{
    public MainPanel dp;
    public JButton             updateButton;
    
    public ListUpdateRow(MainPanel dp) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 20));
        
        this.dp      = dp;
        updateButton = new JButton("リスト更新");
        
        updateButton.addActionListener(this);

        add(updateButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dp.updateList();
    }
}