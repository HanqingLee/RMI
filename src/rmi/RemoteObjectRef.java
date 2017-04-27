package rmi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class RemoteObjectRef {
	//Used to find remote object from server end
	String IP_adr;
	int Port;
	int Obj_Key;
	String Remote_Interface_Name;

	public RemoteObjectRef(String ip, int port, int obj_key, String riname) {
		IP_adr = ip;
		Port = port;
		Obj_Key = obj_key;
		Remote_Interface_Name = riname;
	}

	//Build up the proxy object and return it to client app.
	Object localize(Object o) throws ClassNotFoundException {
		InvocationHandler handler = new StubInvocationHandler(IP_adr, Port, Obj_Key);
		Object proxy = Proxy.newProxyInstance(o.getClass().getClassLoader(), o.getClass().getInterfaces(), handler);

		return proxy;
	}
}