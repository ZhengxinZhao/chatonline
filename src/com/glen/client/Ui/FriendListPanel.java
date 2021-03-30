package com.glen.client.Ui;



import com.glen.client.DataBuffer;
import com.glen.client.TcpClient;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.*;


public class FriendListPanel extends JPanel {

	private static final long serialVersionUID = 1L;


    private JList jList;//显示好友列表

	private TcpClient client;


	public FriendListPanel(TcpClient client) {
		this.client = client;

		//背景色，debug
		this.setBackground(Color.GREEN);

		//流式布局
		this.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		//在线状态选择列表
		String[] status = {"在线","离线"};
		JComboBox<String> online_status = new JComboBox<>(status);
		online_status.setSelectedIndex(0);
		online_status.setPreferredSize(new Dimension(63,23));
		this.add(online_status);
		//搜索框
		JTextField jtf_search = new JTextField();
		jtf_search.setPreferredSize(new Dimension( 255-63, 23));
		this.add(jtf_search);
		//搜索按钮
		JButton btn_search = new JButton(new ImageIcon("res/image/friendlist/search.png"));
		btn_search.setPreferredSize(new Dimension( 30, 23));
		this.add(btn_search);

		//显示好友列表
		jList = new JList(DataBuffer.onlineUser.keySet().toArray());
		jList.setCellRenderer(new FriendListCellRenderer());
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.setBackground(Color.gray);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(jList);
		scrollPane.setPreferredSize(new Dimension(290,500));

		this.add(scrollPane);

        //添加鼠标监听事件
        this.addMouseListener(new MouseAdapter() {
        	@Override
            public void mouseReleased(MouseEvent e) {

                //拖拽结束图标恢复
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }  
        	@Override
            public void mousePressed(MouseEvent e) {

            }
        });  
   
        this.addMouseMotionListener(new MouseMotionAdapter() {
        	@Override
            public void mouseDragged(MouseEvent e) {

            }  

        });
	}


	//设置JList监听
	public void setJListListener(ListSelectionListener lsl){ jList.addListSelectionListener(lsl);}
	// 刷新在线好友列表g
    public void updateOnlineUser() {
        client.getOnlineUser();
    }

	public void onOnlineUserChange(){
		Object[] userIds = DataBuffer.onlineUser.keySet().toArray();
		Object[] userIds1 = new Object[userIds.length+1];
		userIds1[0] = "all";
		System.arraycopy(userIds,0,userIds1,1,userIds.length);
		jList.setModel(new AbstractListModel<String>() {
			public int getSize() { return userIds1.length; }
			public String getElementAt(int i) { return (String) userIds1[i]; }
		});
	}







}

