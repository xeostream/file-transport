import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

public class Client extends Socket {
	private final static String SERVER_IP = "127.0.0.1";
	private final static int SERVER_PORT = 2013;
	private Socket client;
	private FileInputStream fis;
	private DataOutputStream dos;

	public Client() {
		try {
			try {
				client = new Socket(SERVER_IP, SERVER_PORT);
				File file = new File("test.txt");
				fis = new FileInputStream(file);
				dos = new DataOutputStream(client.getOutputStream());

				dos.writeUTF(file.getName());
				dos.flush();
				dos.writeLong(file.length());
				dos.flush();
				byte[] sendBytes = new byte[1024];
				int length = 0;
				while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
					dos.write(sendBytes, 0, length);
					dos.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fis != null)
					fis.close();
				if (dos != null)
					dos.close();
				client.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new Client();
	}
}
