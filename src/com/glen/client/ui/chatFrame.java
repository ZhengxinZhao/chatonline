package com.glen.client.ui;

import javax.swing.*;
import java.awt.*;

public class chatFrame extends JFrame {
    /**聊天对方的信息Label*/
    private JLabel otherInfoLbl;
    /** 当前用户信息Lbl */
    private JLabel currentUserLbl;
    /**聊天信息列表区域*/
    public static JTextArea msgListArea;
    /**要发送的信息区域*/
    public static JTextArea sendArea;
    /** 在线用户列表 */
    public static JList onlineList;
    /** 在线用户数统计Lbl */
    public static JLabel onlineCountLbl;


    /** 私聊复选框 */
    public JCheckBox rybqBtn;
    public void init() {
        this.setTitle("JQ聊天室");
        this.setSize(550, 500);
        this.setResizable(false);
        this.setLocationRelativeTo(null);


        //左边主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        //右边用户面板
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BorderLayout());

        // 创建一个分隔窗格
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                mainPanel, userPanel);
        splitPane.setDividerLocation(380);
        splitPane.setDividerSize(10);
        splitPane.setOneTouchExpandable(true);
        this.add(splitPane, BorderLayout.CENTER);

        //左上边信息显示面板
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        //右下连发送消息面板
        JPanel sendPanel = new JPanel();
        sendPanel.setLayout(new BorderLayout());

// 创建一个分隔窗格
        JSplitPane splitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                infoPanel, sendPanel);
        splitPane2.setDividerLocation(300);
        splitPane2.setDividerSize(1);
        mainPanel.add(splitPane2, BorderLayout.CENTER);

        otherInfoLbl = new JLabel("当前状态：群聊中...");
        infoPanel.add(otherInfoLbl, BorderLayout.NORTH);

        msgListArea = new JTextArea();
        msgListArea.setLineWrap(true);
        infoPanel.add(new JScrollPane(msgListArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        JPanel tempPanel = new JPanel();
        tempPanel.setLayout(new BorderLayout());
        sendPanel.add(tempPanel, BorderLayout.NORTH);

        // 聊天按钮面板
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        tempPanel.add(btnPanel, BorderLayout.CENTER);

        //字体按钮
        JButton fontBtn = new JButton(new ImageIcon("images/font.png"));
        fontBtn.setMargin(new Insets(0,0,0,0));
        fontBtn.setToolTipText("设置字体和格式");
        btnPanel.add(fontBtn);

        //表情按钮
        JButton faceBtn = new JButton(new ImageIcon("images/sendFace.png"));
        faceBtn.setMargin(new Insets(0,0,0,0));
        faceBtn.setToolTipText("选择表情");
        btnPanel.add(faceBtn);

        //发送文件按钮
        JButton shakeBtn = new JButton(new ImageIcon("images/shake.png"));
        shakeBtn.setMargin(new Insets(0,0,0,0));
        shakeBtn.setToolTipText("向对方发送窗口振动");
        btnPanel.add(shakeBtn);

        //发送文件按钮
        JButton sendFileBtn = new JButton(new ImageIcon("images/sendPic.png"));
        sendFileBtn.setMargin(new Insets(0,0,0,0));
        sendFileBtn.setToolTipText("向对方发送文件");
        btnPanel.add(sendFileBtn);

        //私聊按钮
        rybqBtn = new JCheckBox("私聊");
        tempPanel.add(rybqBtn, BorderLayout.EAST);

        //要发送的信息的区域
        sendArea = new JTextArea();
        sendArea.setLineWrap(true);
        sendPanel.add(new JScrollPane(sendArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        // 聊天按钮面板
        JPanel btn2Panel = new JPanel();
        btn2Panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.add(btn2Panel, BorderLayout.SOUTH);
        JButton closeBtn = new JButton("关闭");
        closeBtn.setToolTipText("退出整个程序");
        btn2Panel.add(closeBtn);
        JButton submitBtn = new JButton("发送");
        submitBtn.setToolTipText("按Enter键发送消息");
        btn2Panel.add(submitBtn);
        sendPanel.add(btn2Panel, BorderLayout.SOUTH);

        //在线用户列表面板
        JPanel onlineListPane = new JPanel();
        onlineListPane.setLayout(new BorderLayout());
        onlineCountLbl = new JLabel("在线用户列表(1)");
        onlineListPane.add(onlineCountLbl, BorderLayout.NORTH);

        //当前用户面板
        JPanel currentUserPane = new JPanel();
        currentUserPane.setLayout(new BorderLayout());
        currentUserPane.add(new JLabel("当前用户"), BorderLayout.NORTH);

        // 右边用户列表创建一个分隔窗格
        JSplitPane splitPane3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                onlineListPane, currentUserPane);
        splitPane3.setDividerLocation(340);
        splitPane3.setDividerSize(1);
        userPanel.add(splitPane3, BorderLayout.CENTER);

//        //获取在线用户并缓存
//        DataBuffer.onlineUserListModel = new OnlineUserListModel(DataBuffer.onlineUsers);
//        //在线用户列表
//        onlineList = new JList(DataBuffer.onlineUserListModel);
//        onlineList.setCellRenderer(new MyCellRenderer());
        //设置为单选模式
//        onlineList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        onlineListPane.add(new JScrollPane(onlineList,
//                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
//                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        //当前用户信息Label
        currentUserLbl = new JLabel();
        currentUserPane.add(currentUserLbl);



        this.setVisible(true);
    }
}