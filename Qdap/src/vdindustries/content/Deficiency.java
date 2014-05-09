package vdindustries.content;

public class Deficiency {

	String trade; // unnecessary?

	String reportID; // Maybe have first three characters of ID from project
						// name?
	boolean completed;
	boolean priority;
	int X, Y;
	String object, item, verb, direction, location;

	String roomNo;
	String floor;

	public static final String ID = "reportID";
	public static final String XCOORD = "x";
	public static final String YCOORD = "y";
	public static final String OBJECT = "object";
	public static final String ITEM = "item";
	public static final String VERB = "verb";
	public static final String DIRECTION = "direction";
	public static final String LOCATION = "location";
	public static final String COMPLETED = "completed";
	public static final String PRIORITY = "priority";

	public static final String ELEMENT = "deficiency";
	public static final String DESCRIPTION = "description";

	public Deficiency() {

	}

	public Deficiency(String reportID, int x, int y, String object,
			String item, String verb, String direction, String location) {

		this.reportID = reportID;
		this.X = x;
		this.Y = y;
		this.object = object;
		this.item = item;
		this.verb = verb;
		this.direction = direction;
		this.location = location;
	}

	public String toString() {

		return "reportID: " + reportID + "\t" + object
				+ " " + item + " " + verb + " " + direction + " " + location;
	}
	
	public boolean getCompleted(){
		return completed;
	}

}
