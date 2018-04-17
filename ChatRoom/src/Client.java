import java.net.*;
import java.util.*;
import java.io.*;

public class Client{

	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private String name;
	private GUI window;
	private Server server;
		
	public Client(Socket socket,String name,Server server) {
		this.socket = socket;
		this.name = name;
		this.server = server;
		System.out.println("client");
		init();
		window = new GUI(name,this);
		window.setEditable(true);
	}
	
	public void run() throws IOException {

		out.println("Hello from " + name + "\n");
		out.flush();
		
		while(socket.isConnected()){
			String message = in.readLine();
			//if(message.contains("u123"))
				//window.updateUsers(message);
			//else
				//window.showMessage(message);
		}

		in.close();
		out.close();
		try{
			socket.close();
		}catch(IOException e){}
	}
	
	private void init(){
		
		try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out =  new PrintWriter(socket.getOutputStream(), true);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void disconnect(){
		
		try{
			in.close();
			out.close();
			socket.close();
		}catch(IOException e){}
	}
	
	public void showInChat(String message){
		
		window.append(message);
	}
	
	public String getName(){
		return name;
	}
	
	public void updateUsers(ArrayList<String> list){
		
		window.updateUsers(list);
	}
	
	public ArrayList<Client> getClients(){
		
		return server.getClients();
	}
	
	public static void main(String args[]) throws IOException{
		
		Socket socket = new Socket("localhost",9009);
	}

}
