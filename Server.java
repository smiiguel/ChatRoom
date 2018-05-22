import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Server implements Runnable{

	private ArrayList<Communication> clients;
	private ArrayList<String> names;
	private ServerSocket server = null;
	private int port;
	
	public Server(int port) {
		
		this.port = port;
		
		try {
			server = new ServerSocket(port);
			
			clients = new ArrayList<Communication>();
			names = new ArrayList<String>();
			
			Thread t = new Thread(this);
			t.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		
		try{
			System.out.println("Waiting for connections...");
			while(true){
				Socket socket = server.accept();
				System.out.println("Connection done");
				
				Communication communication = new Communication(socket, this);
				String name = JOptionPane.showInputDialog(null, "Set your nickname", "");
				names.add(name);
				
				//send the user name to the client
				communication.sendMessage("!user@name!" + name);
				
				clients.add(communication);
				
				//notify the user's connection
				notifyUserConnection(name);		
				updateUserContacts(names, communication);
			}
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("Server couldn t run");
		}
	}
	
	public void removeName(String name) {
		
		for(int i = 0;i < names.size(); ++i) {
			if((names.get(i)).equals(name)) {
				names.remove(i);
				clients.remove(i);
			}
		}
		
		for(int i = 0;i < clients.size(); ++i) {
			clients.get(i).sendMessage("!remove@contact!" + name);
		}
	}
	
	//notify the user's connection 
	private void notifyUserConnection(String name){
		
		int len = clients.size();
		
		for(int i = 0;i < len - 1; ++i){
			clients.get(i).sendMessage("!new@user!" + name);
		}
	}
	
	//update the users list
	private void updateUserContacts(ArrayList<String> names, Communication c) {
		
		System.out.println(names.toString());
		c.sendMessage("!online@users!" + names.toString());
	}
	
	//get communication streams list
	public ArrayList<Communication> getCommunicationStreams(){
		return clients;
	}
	
	public static void main(String args[]) {
		
		int port = 9051;
		Server server = new Server(port);
	}
}
