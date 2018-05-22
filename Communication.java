import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Communication implements Runnable{

	private Socket socket;
	private Server server;
	private PrintWriter out;
	private BufferedReader in;
	
	public Communication(Socket socket, Server server) {
		
		this.socket = socket;
		this.server = server;
		
		try {
			
			//build the communication streams
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {

		try{
            String message = "";
            while(socket.isConnected()) {
	            if((message = in.readLine()) != null){
	            	
	            	//get an user's message
	            	if(message.contains("!user@message!")) {
	            		
	            		//remove the prefix
	            		message = message.substring("!user@message!".length());	            		
	            		ArrayList<Communication> clients = server.getCommunicationStreams();
	            		
	            		//if the message is for everyone(no user selected)
	            		if(message.contains("!all@users!")) {
	            			message = message.substring("!all@users!".length() + 2);
	            			
	            			//send the message to each client
	            			for(int i = 0;i < clients.size(); ++i) {
	            				clients.get(i).sendMessage("!user@message!" + message);
	            			}
	            		}else {
		            		int i = 0;
		            		while(message.charAt(i) != ']') {
		            			++i;
		            		}
		            		String sendTo = message.substring(1, i);
		            		System.out.println("trimit catre " + sendTo);
		            		String[] indexes = sendTo.split(",");
		            		message = message.substring(i + 1);
	            		
		            		//for each user selected send the message to him
	            			for(i = 0;i < indexes.length; ++i) {
	            				clients.get(Integer.parseInt(indexes[i])).sendMessage("!user@message!" + message);
	            			}
	            		}
	            		
	            	//user disconnected
	            	}else if(message.contains("!user@disconnect!")) {
	            		message = message.substring("!user@disconnect!".length());System.out.println(message);
	            		int i = 1;
	            		while(message.charAt(i) != ']') {
	            			++i;
	            		}
	            		
	            		String disconnectedName = message.substring(1, i);
	            		server.removeName(disconnectedName);
	            		
	            		ArrayList<Communication> clients = server.getCommunicationStreams();
	            			
	            		message = message.substring(i + 1);
	            		
	            		for(i = 0;i < clients.size(); ++i) {
	            			clients.get(i).sendMessage("!user@message!" + message);
	            		}
	            	}
	            }
            }
    	}catch(Exception e){
    		disconnect();
        	e.printStackTrace();
    	}
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
	
	//send a message to a client
	public void sendMessage(String message) {

		out.println(message);
		out.flush();
	}
	
	
}
