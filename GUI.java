import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GUI {

	private JFrame frame;
	private JButton sendButton;
	private JTextArea textField;
	private JTextArea textArea;
	private JList<String> onlineUsers;
	private JLabel onlineOnes;
	private String name;
	
	private Client client;
	
	private DefaultListModel<String> listModel;
	
	public GUI(String name, Client client) {
		this.name = name;
		this.client = client;
		
		frame = new JFrame(name + "`s chat");
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLayout(null);
		frame.setResizable(false);
		
		addElements();
		actionListenerManager();
		frame.setVisible(true);
	}
	
	//add the chat's element
	private void addElements(){
		
		sendButton = new JButton();
		sendButton.setBounds(400, 395, 80, 60);
		sendButton.setText("SEND");
		sendButton.setEnabled(true);
		sendButton.setBackground(Color.GRAY);
		frame.add(sendButton);
		
		textField = new JTextArea();
		textField.setText("Insert text here");
		textField.setEditable(true);
		JScrollPane scroll2 = new JScrollPane(textField);
		scroll2.setBounds(5, 395, 390, 60);
		frame.add(scroll2);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setBounds(5,5,395,370);
		frame.add(scroll);
		
		onlineOnes = new JLabel();
		onlineOnes.setText("Online users");
		onlineOnes.setBounds(408, 8, 80, 10);
		frame.add(onlineOnes);
		
		listModel = new DefaultListModel<>();
		
		onlineUsers = new JList<>(listModel);
		onlineUsers.setBounds(405,30,80,200);
		onlineUsers.setBackground(Color.GRAY.brighter());
		frame.add(onlineUsers);
	}
	
	public void setEditable(boolean b){
		textField.setEditable(b);
	}
	
	public void setTitle(String title){
		frame.setTitle(title);
	}
	
	//add action listeners 
	private void actionListenerManager(){
		
		sendButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//get the message and clear de text field
				String message = textField.getText();
				textField.setText("");
				
				//show the message and send it
				append(name + " : " + message);	

				//get the message's destination
				int[] indexes = onlineUsers.getSelectedIndices();	
				
				String selectedUsers = "[";
				if(indexes.length > 0) {
					for(int i = 0;i < indexes.length; ++i) {
						selectedUsers += indexes[i];
						if(i + 1 != indexes.length) {
							selectedUsers += ",";
						}
					}
				}else {
					selectedUsers += "!all@users!";
				}
				selectedUsers += "]";
				
				client.sendMessage("!user@message!" + selectedUsers + name + " : " +  message);
			}
		}
		);
		
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.out.println("Clientul " + name + " a iesit");
				//client.sendMessage("!user@disconnect!" + "[" + name + "]" + name + " has disconnected!");
				
				frame.setVisible(false);
				frame.dispose();
				client.disconnect();
				System.exit(0);
			}
		}
		);
	}
	
	//show the online users
	public void updateUsers(ArrayList<String> list){
		
		listModel.removeAllElements();
		
		int len = list.size();
		
		for(int i = 0;i < len; ++i)
			listModel.addElement(list.get(i));
	}
	
	//append a message
	public void append(String message){
		
		textArea.append(message + "\n");
	}
	
	//add an user
	public void addUser(String name) {
		listModel.addElement(name);
	}
	
	public void removeUser(String name) {
		
		for(int i = 0;i < listModel.size(); ++i) {
			if((listModel.get(i)).equals(name)) {
				listModel.remove(i);
			}
		}
	}
}
