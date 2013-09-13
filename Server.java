import java.io.File;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private final static int PORT = 2013;
	private Socket client;
	private ServerSocket server;
	private ObjectInputStream ois;
	private RandomAccessFile raf;

	public Server() throws Exception {
		try {
			try {
				server = new ServerSocket(PORT);
				while (true) {
					client = server.accept();
					ois = new ObjectInputStream(client.getInputStream());
					String fileName = ois.readUTF();
					long fileLength = ois.readLong();
					raf = new RandomAccessFile(new File(fileName), "rw");
					int translen = 0;
					System.out.println("begin receive file<" + fileName
							+ ">,size<" + fileLength + ">");
					while (true) {
						int read = 0;
						Data data = (Data) ois.readObject();
						if (data == null)
							break;
						read = data.getBuff().length;
						translen += read;
						System.out.println("recevie file percent" + 100
								* (translen / fileLength));
						raf.seek(data.getStartPos());
						raf.write(data.getBuff());
					}
					System.out.println("receiver success");
					raf.close();
					client.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (ois != null)
					ois.close();
				if (raf != null)
					raf.close();
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
