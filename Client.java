import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class Client implements Runnable{
	
	private Socket socket = null;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private String name = null;
	private GUI gui = null;
	
	private final int PORT = 9051;
	
	public Client() {
		
        try {
        	//build the connection
    		socket = new Socket("localhost", PORT);

    		//build the communication streams
    		out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            //start the thread
            Thread t = new Thread(this);
            t.start();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}      
	}
	
	@Override
	public void run() {

		try{
            String message = "";
            while(socket.isConnected()) {
	            if(socket.isConnected() && (message = in.readLine()) != null){
	            	
	            	//get the user's name
	            	if(message.contains("!user@name!")) {
	            		name = message.substring("!user@name!".length());

	            		//create the gui
	            		gui = new GUI(name, this);
	            		
            		//notify a new user connection and add it's name to the list
	            	}else if (message.contains("!new@user!")) {
	            		
	            		//parse the message(delete the prefix)
	            		message = message.substring("!new@user!".length());
	            		
	            		gui.append(message + " has connected!");
	            		gui.addUser(message);
	            		
            		//fill the online user's list
	            	}else if(message.contains("!online@users!")) {
	            		
	            		//parse the message
	            		message = message.substring("!online@users!".length());
	            		message = message.replace("[", "");
	            		message = message.replace("]", "");
	            		message = message.replaceAll("\\s+","");
	            		
	            		//add all the online users in the list
	            		String[] parts = message.split(",");
	            		for(int i = 0;i < parts.length; ++i) {
	            			gui.addUser(parts[i]);
	            		}
	            		
            		//get a message
	            	}else if(message.contains("!user@message!")) {
	            		
	            		//parse the message
	            		message = message.substring("!user@message!".length());
	            		gui.append(message);
	            		
            		//remove a client after it's disconnect
	            	}else if(message.contains("!remove@contacts!")) {
	            		
	            		//parse the message
	            		message = message.substring("!remove@contacts!".length());
	            		gui.removeUser(message);
	            	}
	            }
            }
        }catch(Exception e){
        	e.printStackTrace();
        	disconnect();
        }
	}
	
	//send a message to the server
	public void sendMessage(String message) {
		
		out.println(message);
		out.flush();
	}
	
	//close the streams and the socket
	public void disconnect() {
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getName() {
		return name;
	}

	public static void main(String args[]) {
		
		new Client();
	}
}
