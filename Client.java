import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends Socket {
	private final static String SERVER_IP = "127.0.0.1";
	private final static int SERVER_PORT = 2013;
	private final static int THREAD_COUNT = 5;
	private Socket client;
	private FileInputStream fis;
	private ObjectOutputStream oos;

	public Client() {
		try {
			try {
				client = new Socket(SERVER_IP, SERVER_PORT);
				File file = new File("test.txt");
				fis = new FileInputStream(file);
				oos = new ObjectOutputStream(client.getOutputStream());
				
				oos.writeUTF(file.getName());
				oos.flush();
				oos.writeLong(file.length());
				oos.flush();
				long length = file.length() / THREAD_COUNT;
				byte[] sendBytes = new byte[(int) length];
				for (int i = 0; i < THREAD_COUNT; i++) {
					fis.read(sendBytes, 0, sendBytes.length);
					new SendThread(new Data(length * i, sendBytes)).start();
				}
				oos.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fis != null)
					fis.close();
				if (oos != null)
					oos.close();
				client.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class SendThread extends Thread {
		private Data data;
		public SendThread(Data data) {
			this.data = data;
		}
		public void run() {
			try {
				oos.writeObject(data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					oos.close();
				} catch (IOException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		new Client();
	}
}
