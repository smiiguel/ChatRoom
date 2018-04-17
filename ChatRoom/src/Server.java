import java.util.*;

import javax.swing.JOptionPane;

import java.net.*;
import java.io.*;

public class Server implements Runnable{

	private ArrayList<String> userNames;
	private ArrayList<Client> clients;
	
	
	public Server() {
		
		userNames = new ArrayList<String>();
		clients = new ArrayList<Client>();
	}
	
	public void run(){
		
		ServerSocket server = null;
		
		try{
			server = new ServerSocket(9009);
			System.out.println("Waiting for connections...");
			while(true){
				Socket socket = server.accept();
				System.out.println("Connection done");
				
				String name = JOptionPane.showInputDialog(null, "Set your nickname", "");
				userNames.add(name);
				
				Client client = new Client(socket,name,this);
				
				clients.add(client);
				addUser(client);
			}
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("Server couldn t run");
		}
	}
	
	public ArrayList<Client> getClients(){
		
		return clients;
	}
	
	private void addUser(Client client){
		
		int len = clients.size();
		
		for(int i = 0;i < len; ++i){
			Client aux = clients.get(i);
			if(!aux.equals(client))
				aux.showInChat("INFO : " + client.getName() + " has connected\n");
			aux.updateUsers(userNames);
			
		}
	}
	
	public static void main(String args[]){
		
		Server server = new Server();
		Thread thread = new Thread(server);
		thread.start();
	}

}
