package Server;


public class rfidData{
	String tagid;
	int readerid;
	boolean valid;
	String room;
	
	public rfidData(String tagid, int readerid, boolean valid, String room) {
		super();
		this.tagid = tagid;
		this.readerid = readerid;
		this.valid = valid;
		this.room = room;
	}
	
	public String getTagid() {
		return tagid;
	}
	public void setTagid(String tagid) {
		this.tagid = tagid;
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
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}

	@Override
	public String toString() {
		return "rfidData [tagid=" + tagid + ", readerid=" + readerid + ", valid=" + valid + ", room=" + room + "]";
	}	
	
}