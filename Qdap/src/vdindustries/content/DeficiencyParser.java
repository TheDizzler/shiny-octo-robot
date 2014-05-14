package vdindustries.content;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;



public class DeficiencyParser {
	
	public static InputStream	projectXML;
	public static Element		root;
	public static NodeList		listFloorNodes;
	public static NodeList		listRoomNodes;
	
	public static NodeList		listTrades;
	private static AssetManager	assMan;
	
	
	/** Implementation: DeficiencyParser(getAssets()) */
	public DeficiencyParser(AssetManager am) {
	
		assMan = am;
		try {
			projectXML = assMan.open("testproject.xml");
			
			Document xmlDoc = DocumentBuilderFactory.newInstance().
				newDocumentBuilder().parse(projectXML);
			root = xmlDoc.getDocumentElement();
			root.normalize();
			
			listTrades = root.getElementsByTagName("trade");
			listRoomNodes = root.getElementsByTagName("room");
			listFloorNodes = root.getElementsByTagName("floor");
			
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	
	/** Retrieves number of deficiencies in a room, completed & uncompleted */
	public static int totalDefByUnit(String unit) {
	
		for (int i = 0; i < listRoomNodes.getLength(); ++i) {
			
			Element room = ((Element) listRoomNodes.item(i));
			if (room.getAttribute(Deficiency.FLOORID).equalsIgnoreCase(unit)) {
				
				return room.getElementsByTagName("deficiency").getLength();
			}
		}
		
		return 0;
	}
	
	/** NOT IMPLEMENTED. */
	public static int outstandingDefByUnit(String unit) {
	
		
		return 5;
	}
	
	/** Retrieves a deficiency object from its reportID.
	 * Note: this is likely very inefficient in the case of large buildings. */
	public static Deficiency getDefByID(String reportID) {
	
		NodeList defList = root.getElementsByTagName("deficiency");
		for (int i = 0; i < defList.getLength(); ++i) {
			
			if (((Element) defList.item(i)).
				getAttribute(Deficiency.REPORTID).
				equalsIgnoreCase(reportID)) {
				
				return createDeficiency(defList.item(i));
			}
		}
		return null;
	}
	
	
	/** Builds a list of deficiencies by trade. */
	public static List<Deficiency> getDefsByTrade(String tradeSelected) {
	
		List<Deficiency> defList = new ArrayList<Deficiency>();
		
		// for each <trade> element
		for (int i = 0; i < listTrades.getLength(); ++i) {
			
			// if <trade type = tradeSelected>
			if (((Element) listTrades.item(i)).getAttribute("type").equalsIgnoreCase(tradeSelected)) {
				
				// get a list of all child deficiency elements
				NodeList nodes = ((Element) listTrades.item(i)).getElementsByTagName("deficiency");
				
				for (int j = 0; j < nodes.getLength(); ++j)
					defList.add(createDeficiency(nodes.item(j)));
				
				break;
			}
		}
		
		return defList;
	}
	
	
	public static List<Deficiency> getDefsByRoom(String roomNo) {
	
		List<Deficiency> defList = new ArrayList<Deficiency>();
		
		for (int i = 0; i < listRoomNodes.getLength(); ++i) {
			
			Element roomElem = ((Element) listRoomNodes.item(i));
			if (roomElem.getAttribute(Deficiency.ROOMNO).equalsIgnoreCase(roomNo)) {
				
				NodeList nodes = roomElem.getElementsByTagName("deficiency");
				
				for (int j = 0; j < nodes.getLength(); ++j)
					defList.add(createDeficiency(nodes.item(j)));
				
				break;
			}
		}
		
		return defList;
	}
	
	
	/** Retrieves a list of rooms on a specific floor. */
	public static NodeList getRoomsByFloor(String floorID) {
	
		for (int i = 0; i < listFloorNodes.getLength(); ++i) {
			
			Element current = (Element) listFloorNodes.item(i);
			if (current.getAttribute(Deficiency.FLOORID).equals(floorID)) {
				return current.getElementsByTagName(Deficiency.ROOM);
			}
		}
		return null;
	}
	
	/** Retrieves room plan image file location from it's room number. */
	public static String getRoomImageFile(String roomNo) {
	
		for (int i = 0; i < listRoomNodes.getLength(); ++i) {
			
			Element current = (Element) listRoomNodes.item(i);
			if (current.getAttribute(Deficiency.ROOMNO).equals(roomNo)) {
				
				return ((Element) current.
					getElementsByTagName(Deficiency.ROOMPLAN).item(0)).
					getAttribute(Deficiency.ROOMIMAGE);
			}
		}
		return null;
	}
	
	/** Retrieves floor plan image file location from it's ID. */
	private static String getFloorImageFile(String floorID) {
	
		for (int i = 0; i < listFloorNodes.getLength(); ++i) {
			
			Element current = (Element) listFloorNodes.item(i);
			if (current.getAttribute(Deficiency.FLOORID).equals(floorID)) {
				
				return ((Element) current.
					getElementsByTagName(Deficiency.FLOORPLAN).item(0)).
					getAttribute(Deficiency.FLOORIMAGE);
			}
		}
		return null;
	}
	
	/** Builds a deficiency object from a XML node. */
	private static Deficiency createDeficiency(Node node) {
	
		Deficiency def = new Deficiency();
		Element defElem = (Element) node;
		
		def.reportID = defElem.getAttribute(Deficiency.REPORTID);
		
		def.completed = Boolean.parseBoolean(defElem.getElementsByTagName(Deficiency.COMPLETED).item(
			0).getTextContent());
		def.priority = Boolean.parseBoolean(defElem.getElementsByTagName(Deficiency.PRIORITY).item(
			0).getTextContent());
		Element coor = ((Element) defElem.getElementsByTagName("coordinates").item(0));
		def.X = Integer.parseInt(coor.getAttribute(Deficiency.XCOORD));
		def.Y = Integer.parseInt(coor.getAttribute(Deficiency.YCOORD));
		
		def.object = defElem.getElementsByTagName(Deficiency.OBJECT).item(0).getTextContent();
		def.item = defElem.getElementsByTagName(Deficiency.ITEM).item(0).getTextContent();
		def.verb = defElem.getElementsByTagName(Deficiency.VERB).item(0).getTextContent();
		def.direction = defElem.getElementsByTagName(Deficiency.DIRECTION).item(0).getTextContent();
		def.location = defElem.getElementsByTagName(Deficiency.LOCATION).item(0).getTextContent();
		
		def.trade = ((Element) defElem.getParentNode()).getAttribute(Deficiency.TRADE);
		def.roomNo = ((Element) defElem.getParentNode().getParentNode()).getAttribute(Deficiency.ROOMNO);
		return def;
	}
	
	
	/** Retrieve a list of room IDs by floorID. */
	public static List<String> getRoomIDs(String floorID) {
	
		
		List<String> rooms = new ArrayList<String>();
		
		for (int i = 0; i < listFloorNodes.getLength(); ++i) {
			
			Element current = (Element) listFloorNodes.item(i);
			if (current.getAttribute(Deficiency.FLOORID).equalsIgnoreCase(floorID)) {
				
				NodeList roomNodes = current.getElementsByTagName(Deficiency.ROOM);
				for (int j = 0; j < roomNodes.getLength(); ++j) {
					
					Element node = (Element) roomNodes.item(j);
					rooms.add(node.getAttribute(Deficiency.ROOMNO));
				}
				break;
			}
		}
		return rooms;
	}
	
	
	/** Retrieve a list of floor IDs. */
	public static List<String> getFloorIDs() {
	
		
		List<String> floors = new ArrayList<String>();
		
		for (int i = 0; i < listFloorNodes.getLength(); ++i) {
			
			String floor = ((Element) listFloorNodes.item(i))
				.getAttribute(Deficiency.FLOORID);
			
			floors.add(floor);
		}
		return floors;
	}
	
	
	public static void loadRoomPlan(ImageView image, String roomNo) {
	
		String file = getRoomImageFile(roomNo);
		loadBitmapFromAsset(image, file);
	}
	
	
	
	public static void loadFloorPlan(ImageView image, String floorID) {
	
		String file = getFloorImageFile(floorID);
		loadBitmapFromAsset(image, file);
	}
	
	
	
	private static void loadBitmapFromAsset(ImageView image, String file) {
	
		try {
			InputStream is = assMan.open(file);
			Bitmap bmap = BitmapFactory.decodeStream(is);
//			Matrix matrix = new Matrix();
//			float scale = ((float) 400) / bmap.getWidth();
//			matrix.setScale(1, 1);
//
//			
			image.setImageBitmap(bmap); //CHANGE THIS LINE
//			image.setImageMatrix(matrix);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
	}
	
	/** Loads an image from the assets directory. */
	private static void loadImageFromAsset(ImageView image, String file) {
	
		try {
			InputStream is = assMan.open(file);
			image.setImageDrawable(Drawable.createFromStream(is, null));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	
	/** Load an image from a directory on device.
	 * NOT YET IMPLEMENTED */
	private static Drawable loadImageFromDirectory(String file) {
	
		
//				try {
//				Bitmap bmap = BitmapFactory.decodeFile();
//				} catch (IOException ex) {
//					throw ex;
//				}
		return null;
		
	}
	
	/** Load an image from a URI.
	 * NOT YET IMPLEMENTED */
	private static Drawable loadImageFromUri(String uri) {
	
		
		return null;
	}
	
	
	public static int getImageWidth(String roomNo) {
	
		String file = getRoomImageFile(roomNo);
		Bitmap bmap = null;
		try {
			InputStream is = assMan.open(file);
			bmap = BitmapFactory.decodeStream(is);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		return bmap.getWidth();
	}
	
	
	public static int getImageHeight(String roomNo) {
	
		String file = getRoomImageFile(roomNo);
		Bitmap bmap = null;
		try {
			InputStream is = assMan.open(file);
			bmap = BitmapFactory.decodeStream(is);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		return bmap.getHeight();
	}
	
}
