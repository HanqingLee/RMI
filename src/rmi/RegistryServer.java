package rmi;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Registry service operated on server end. This object holds many variables. 
//it records the binding relations of object to a string name, as well as the relationship 
//between Obj_Key to Object. Thus it can achieve many bindings in one registry object.
//The object also keeps a map from Object to its ror object, for convenience of returning the ror when server receives a lookup command.
public class RegistryServer {
	private int port;
	private int count;
	private ConcurrentHashMap<String, Object> BindTable = new ConcurrentHashMap<String, Object>();
	private ConcurrentHashMap<Integer, Object> ObjKey = new ConcurrentHashMap<Integer, Object>();
	private ConcurrentHashMap<Object, RemoteObjectRef> ObjRef = new ConcurrentHashMap<Object, RemoteObjectRef>();

	public RegistryServer(int port) {
		this.port = port;
		try {
			start();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	private void start() throws IOException, ClassNotFoundException {

		// 1. initiate the communication module.
		// 2. initiate server listening
		// 3. when client require lookup, return the relevant ror object
		// When client sends an invoke command, invoke the required method and
		// returns the result.

		ExecutorService tp = Executors.newCachedThreadPool();
		
		ServerSocket server = new ServerSocket(port);
		System.out.println("Registry server started");
		// Start a new thread to initiate listening.
		tp.execute(new Runnable() {
			public void run() {
				while (true) {
					try {
						Socket newsoc = server.accept();
						// When there is a client connected, start another
						// thread in order to fit multi-user implementation.
						tp.execute(new Runnable() {
							public void run() {
								// Command indicates the service the client is looking for.
								CommunicationModule cm = new CommunicationModule();
								Object command;
								try {
									command = cm.RecObj(newsoc);

									if (command instanceof String) {
										// if it is a String, it indicates the servicename
										//Server will finally return a stub object or null if an error occured
										
										String name = (String) command;
										Object o = BindTable.get(name);
										if (o == null) {
											System.out.println("No such stub!");
											cm.SendObj(newsoc, "No such stub!");
											return;
										}
										RemoteObjectRef ror = ObjRef.get(o);

										Object stub = ror.localize(o);
										cm.SendObj(newsoc, stub);
									} else if (command instanceof TranSegment) {
										// Client is requesting a RPC.
										// Server needs get the obj and method the client is calling, and return the client running result.
										TranSegment seg = (TranSegment) command;
										int Obj_Key = seg.getObj_Key();
										String methodName = seg.getMethod();

										Object obj = ObjKey.get(Obj_Key);
										Object[] args = seg.getArgs();

										// Reconstruct the method invoked by client using reflection
										Class<?> parameterTypes[] = new Class[args.length];
										for (int i = 0; i < args.length; i++) {
											parameterTypes[i] = args[i].getClass();
										}
										Method method = obj.getClass().getMethod(methodName, parameterTypes);

										if (method != null && args.length > 0) {
											// invoke and send back the result
											Object result = null;
											Object obj1 = ObjKey.get(Obj_Key);
											try {
												result = method.invoke(obj1, args);
											} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											cm.SendObj(newsoc, result);
										} else {
											System.out.println("Invokation error!");
										}
									} else {
										System.out.println("Unknown Message Type");
										cm.SendObj(newsoc, "Unknown Message Type");
									}
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IllegalArgumentException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (NoSuchMethodException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (SecurityException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (ClassNotFoundException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						});
					} catch (IOException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	// This method binds a specific string name to an object instantiated on
	// server end. when a binding is built, it also builts the other connections
	// and instantiate the ror object.
	public void bind(String name, Object obj) {
		count++;
		BindTable.put(name, obj);
		ObjKey.put(count, obj);
		RemoteObjectRef ror = new RemoteObjectRef("localhost", port, count,
				obj.getClass().getInterfaces()[0].getName());
		ObjRef.put(obj, ror);
		System.out.printf("Success bind the %s and the interface %s\n", name,
				obj.getClass().getInterfaces()[0].getName());
	}
}
