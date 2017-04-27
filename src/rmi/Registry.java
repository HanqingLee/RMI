package rmi;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

//Registry process on client end.
public class Registry {
	// this object holds the server host, port. It needs these two parameters to
	// instantiate a socket to communicate with server. It provides a fucntion
	// to lookup the service operated on the server.

	private String host;
	private int port;
	private Socket sock;

	public Registry(String ip, int port) throws UnknownHostException, IOException {
		this.host = ip;
		this.port = port;
		sock = new Socket(this.host, this.port);
	}

	// Get the stub from the remote server.
	public Object lookup(String serviceName) throws IOException, ClassNotFoundException {
		// sends the requested service name to server, and server will return the stub object or error message
		
		CommunicationModule cm = new CommunicationModule();
		boolean s = cm.SendObj(sock, serviceName);
		
		if (s) {
			Object stub = cm.RecObj(sock);
			if (stub.getClass().getName().equals("com.sun.proxy.$Proxy0")) 
				return stub;
			else
				System.out.println(stub);
		}
		System.out.println("Lookup Failed! Please retry");
		return null;
	}
}
