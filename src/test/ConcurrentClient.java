package test;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentClient {
	static int concurrent_num = 500;
	
	public static void main(String args[]) throws InterruptedException {
		ConcurrentClient cc = new ConcurrentClient();
		ExecutorService tp = Executors.newFixedThreadPool(concurrent_num);
		ConcurrentClient.autoregister register = cc.new autoregister();
		for (int i = 0; i < concurrent_num; i++) 
			tp.execute(register);
		
		tp.shutdown();
	}

	class autoregister implements Runnable {
		@Override
		public void run() {
			String username = getRandomString(5);
			String pw = getRandomString(10);

			Client client = new Client();
			client.Register(username, pw);
		}
	}

	public static String getRandomString(int length) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(62);
			buffer.append(str.charAt(number));
		}
		return buffer.toString();
	}
}
