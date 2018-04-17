import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUI {

	private JFrame frame;
	private JButton sendButton;
	private JTextArea textField;
	private JTextArea textArea;
	private JTextArea onlineUsers;
	private JLabel onlineOnes;
	private String name;
	private Client client;
	
	public GUI(String name,Client client) {
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
		textField.setEditable(false);
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
		
		onlineUsers = new JTextArea();
		onlineUsers.setBounds(405,30,80,200);
		onlineUsers.setEditable(false);
		onlineUsers.setBackground(Color.GRAY.brighter());
		frame.add(onlineUsers);
	}
	
	public void setEditable(boolean b){
		textField.setEditable(b);
	}
	
	public void setTitle(String title){
		frame.setTitle(title);
	}
	
	private void actionListenerManager(){
		
		sendButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//textArea.append(name + " : " + textField.getText() + "\n");
				showMessage(name + " : " + textField.getText());
				textField.setText("");
			}
		}
		);
		
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				client.disconnect();
				System.exit(0);
			}
		}
		);
	}
	
	public void updateUsers(ArrayList<String> list){
		
		onlineUsers.setText("");
		
		int len = list.size();
		
		for(int i = 0;i < len; ++i)
			onlineUsers.append(list.get(i) + "\n");
	}
	
	public void append(String message){
		
		textArea.append(message + "\n");
	}

	public void showMessage(String message){
		
		int len = client.getClients().size();
		System.out.println(len);
		
		for(int i = 0;i < len; ++i){
			client.getClients().get(i).showInChat(message);
			//System.out.println(client.getClients().get(i).getName());
		}
		//textArea.append(message + "\n");
	}
	
}
