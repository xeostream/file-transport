import java.io.Serializable;

public class Data implements Serializable {
	private final static long serialVersionUID = 1l;
	private final long startPos;
	private byte[] buff;
	
	public Data(long startPos, byte[] buff) {
		this.startPos = startPos;
		this.buff = buff;
	}
	public long getStartPos() {
		return startPos;
	}
	public byte[] getBuff() {
		return buff;
	}
}
