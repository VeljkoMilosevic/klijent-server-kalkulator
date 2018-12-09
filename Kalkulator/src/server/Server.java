package server;  

import java.io.*;
import java.net.*;
import java.util.LinkedList;   
  
public class Server {  
  
	public static LinkedList<ClientServerThread> listOfClients = new LinkedList<ClientServerThread>();
       
	public static void main(String[] args) {       
		   
		System.out.println("Server je poceo sa radom...");
	 try {   
			ServerSocket server = new ServerSocket(9011);    
			   
			while(true) {  
				
				Socket ClientSocket = server.accept();
				ClientServerThread newClient = new ClientServerThread(ClientSocket);
				listOfClients.add(newClient);
				newClient.start();
				 
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}

	}

}
