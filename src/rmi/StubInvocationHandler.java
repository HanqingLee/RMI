package rmi;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;

import java.lang.reflect.Method;
import java.net.Socket;
import java.net.UnknownHostException;

//This is the proxy class. In this class the proxy get the invoke information and send them to the remote server to invoke the remote object.
public class StubInvocationHandler implements InvocationHandler, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String IP_adr;
	int Port;
	int Obj_Key;

	public StubInvocationHandler(String ip_adr, int port, int obj_key) {
		this.IP_adr = ip_adr;
		this.Port = port;
		this.Obj_Key = obj_key;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws UnknownHostException, IOException, ClassNotFoundException {
		// 1. instantiate the communication module using attributes here.
		// 2. pack the parameters abtained here and Obj_Key to a TranSegment object and pass it to server.
		// 3. receive the result or error info passed from remote server and return it.
		Socket sock = new Socket(IP_adr, Port);
		CommunicationModule cm = new CommunicationModule();

		TranSegment seg = new TranSegment(Obj_Key, method.getName(), args);

		boolean s = cm.SendObj(sock, seg);
		if (s) {
			//Get the invoking result returned from server end
			Object result = cm.RecObj(sock);
			return result;
		}
		return null;
	}
}
