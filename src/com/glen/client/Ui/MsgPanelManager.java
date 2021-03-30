package com.glen.client.Ui;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class MsgPanelManager {
    private Map<String,JTextPane> panelMap = new HashMap<>();

    public JTextPane getJTextPanel(String userId){
        JTextPane jtp = panelMap.get(userId);
        if(null==jtp) {
            jtp = new JTextPane();
            panelMap.put(userId,jtp);
            System.out.println("newJTextPane");
        }

        return jtp;
    }


}
