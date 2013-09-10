import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	private final static int PORT = 2013;
	private Socket client;
	private ServerSocket server;
	private DataInputStream dis;
	private FileOutputStream fos;
	
	public Server() throws Exception {
		try {
			try {
				server = new ServerSocket(PORT);
				while (true) {
					client = server.accept();
					dis = new DataInputStream(client.getInputStream());
					String fileName = dis.readUTF();
					long fileLength = dis.readLong();
					fos = new FileOutputStream(new File(fileName));
					byte[] sendBytes = new byte[1024];
					int translen = 0;
					System.out.println("begin receive file<" + fileName + ">,size<" + fileLength + ">");
					while (true) {
						int read = 0;
						read = dis.read(sendBytes);
						if (read == -1)
							break;
						translen += read;
						System.out.println("recevie file percent" + 100 * (translen/fileLength));
						fos.write(sendBytes, 0, read);
						fos.flush();
					}
					System.out.println("receiver success");
					client.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (dis != null)
					dis.close();
				if (fos != null)
					fos.close();
				server.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		new Server();
	}
}
