package test;

import rmi.RegistryServer;

public class Server {
	public Server(){}
	
	public static void main(String args[]){
		try{
			//create remote object
			RMIPracticeImpl robj = new RMIPracticeImpl();
			
			//bind the registry with name
			RegistryServer registry = new RegistryServer(2017);
		    registry.bind("RMI_HH", robj);
		    System.out.println("RMI_HH Server is ready to listen...");
		} catch (Exception e){
			System.err.println("Server exception thrown: " + e.toString());
		    e.printStackTrace();
		}
	}
}
