package mqtt.Data;


public class rfidData{
	String tagid;
	String room;
	int readerid;
	boolean valid;
	
	public rfidData(String tagid, String room, int readerid, boolean valid) {
		super();
		this.tagid = tagid;
		this.room = room;
		this.readerid = readerid;
		this.valid = valid;
	}
	
	public String getTagid() {
		return tagid;
	}
	public void setTagid(String tagid) {
		this.tagid = tagid;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public int getReaderid() {
		return readerid;
	}
	public void setReaderid(int readerid) {
		this.readerid = readerid;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}

	@Override
	public String toString() {
		return "rfidData [tagid=" + tagid + ", room=" + room + ", readerid=" + readerid + ", valid=" + valid + "]";
	}
	
}

