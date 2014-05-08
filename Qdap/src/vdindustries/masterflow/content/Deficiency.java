package vdindustries.masterflow.content;


public class Deficiency {
	
	
	/* Attribute tags */
	public static final String	FLOORID		= "floorID";
	public static final String	FLOORIMAGE	= "floorsrc";
	public static final String	TRADE		= "type";
	public static final String	ROOMNO		= "no";
	public static final String	ROOMPLAN	= "roomplan";
	public static final String	ROOMIMAGE	= "roomsrc";
	public static final String	REPORTID	= "reportID";
	public static final String	XCOORD		= "x";
	public static final String	YCOORD		= "y";
	
	/* Element tags */
//	public static final String	ELEMENT		= "deficiency";
	public static final String	ROOM		= "room";
	public static final String	DESCRIPTION	= "description";
	public static final String	OBJECT		= "object";
	public static final String	ITEM		= "item";
	public static final String	VERB		= "verb";
	public static final String	DIRECTION	= "direction";
	public static final String	LOCATION	= "location";
	public static final String	COMPLETED	= "completed";
	public static final String	PRIORITY	= "priority";
	public static final String	FLOORPLAN	= "floorplan";
	
	
	
	String						trade;							// unnecessary?
																
	String						reportID;						// Maybe have first three characters of ID from project name?
	boolean						completed;
	boolean						priority;
	int							X, Y;
	String						object, item, verb, direction, location;
	
	String						roomNo;
	String						floor;
	
	
	
	public Deficiency() {
	
	}
	
	
	public Deficiency(String reportID, int x, int y,
						String object, String item,
						String verb, String direction,
						String location) {
	
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
	
		return "reportID: " + reportID + " roomNo: " + roomNo +
				">> " + object + " " + item + " " + verb + " " +
				direction + " " + location + " << " +
				" at (" + X + ", " + Y + ")";
	}
	
}