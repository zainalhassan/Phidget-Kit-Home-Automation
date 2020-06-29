package Server;

public class motorData {
	
	int motorid;
	String room;
	
	public motorData(int motorid, String room) {
		super();
		this.motorid = motorid;
		this.room = room;
	}

	public int getMotorid() {
		return motorid;
	}

	public void setMotorid(int motorid) {
		this.motorid = motorid;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	@Override
	public String toString() {
		return "motorData [motorid=" + motorid + ", room=" + room + "]";
	}
	

}
