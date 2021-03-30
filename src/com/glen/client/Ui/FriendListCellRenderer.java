package com.glen.client.Ui;


import com.glen.client.DataBuffer;

import javax.swing.*;
import java.awt.*;



public class FriendListCellRenderer extends JLabel implements ListCellRenderer<String> {



    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {

        //value为userId
        if(value.equals("all")) setText("all");
        else setText(DataBuffer.onlineUser.get(value));
        return this;
    }
}
