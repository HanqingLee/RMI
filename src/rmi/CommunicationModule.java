package rmi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class CommunicationModule {
	
	public boolean SendObj(Socket sock, Object seg) throws IOException {
		boolean success = false;
		try {
			OutputStream o = sock.getOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(o));
			out.writeObject(seg);
			out.flush();
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}

	public Object RecObj(Socket sock) throws IOException, ClassNotFoundException {
		InputStream i = sock.getInputStream();
		ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(i));
		Object rec = in.readObject();
		return rec;
	}
}
