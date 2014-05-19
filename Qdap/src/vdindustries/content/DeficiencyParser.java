package vdindustries.content;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import vdindustries.Categories;
import vdindustries.networking.LoadingScreen;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;



public class DeficiencyParser {
	
	public static String				projectName;
	
	public static InputStream			projectXML;
	public static Element				root;
	public static NodeList				listFloorNodes;
	public static NodeList				listRoomNodes;
	public static NodeList				listDeficiencyNodes;
	public static NodeList				listTrades;
	
	public static Map<String, Bitmap>	floorImages;
	public static Map<String, Bitmap>	roomImages;
	
	public static AssetManager			assetMan;
	
	public static Document				xmlDoc;
	private static File					fileXML;
	
	public static boolean				fromAssets;
	
	private static Bitmap				nopic;
	
	private static String				path	= "/Download/";
	private String						file	= "testproject.xml";
	
	
	
	/** Create using a project stored in downloaded from server. */
	public DeficiencyParser(String xml, String projname, AssetManager am, Context context) {
	
		assetMan = am;
		fromAssets = false;
		
		//set the path where we want to save the file
		//in this case, going to save it on the root directory of the
		//sd card.
		File saveDirectory = new File(Environment.getExternalStorageDirectory() + path);
		fileXML = new File(saveDirectory, "biteme");
		
		//this will be used to write the downloaded data into the file we created
		
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileXML));
			bufferedWriter.write(xml);
			bufferedWriter.close();
			projectXML = new FileInputStream(fileXML);
			xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(projectXML);
		} catch (IOException | SAXException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		setup();
		
	}
	
	private static void refresh() {
		
		try {
			projectXML = new FileInputStream(fileXML);
			
			xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(projectXML);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		setup();
	}
	
	/** Internal true: Create using the DEFAULT project stored on internal storage. 
	 * 	Internal false: Creates using the DEFAULT project stored in assets.
	 * 			Implementation: DeficiencyParser(getAssets()) */
	public DeficiencyParser(AssetManager am, Boolean internal) {
	
		assetMan = am;
		
		fileXML = new File(Environment.getExternalStorageDirectory() + path + "testproject.xml");
		
		try {
			
			projectXML = new FileInputStream(fileXML);
			xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(projectXML);
			
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		setup();
		fromAssets = true;
		
	}
	
	
	
	private static void setup() {
	
		try {
			if (nopic == null)
				nopic = BitmapFactory.decodeStream(assetMan.open("images/nopic.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		root = xmlDoc.getDocumentElement();
		root.normalize();
		
		projectName = root.getAttribute("name");
		
		listTrades = root.getElementsByTagName("trade");
		listRoomNodes = root.getElementsByTagName("room");
		listFloorNodes = root.getElementsByTagName("floor");
		listDeficiencyNodes = root.getElementsByTagName("deficiency");
	}
	
	
	/** Retrieves number of deficiencies in a room, completed & uncompleted */
	public static int totalDefsByUnit(String unit) {
	
		for (int i = 0; i < listRoomNodes.getLength(); ++i) {
			
			Element room = ((Element) listRoomNodes.item(i));
			if (room.getAttribute(Deficiency.FLOORID).equalsIgnoreCase(unit)) {
				
				return room.getElementsByTagName("deficiency").getLength();
			}
		}
		
		return 0;
	}
	
	public static int totalDefsByTrade(String trade) {
	
		int total = 0;
		for (int i = 0; i < listTrades.getLength(); ++i) {
			if (((Element) listTrades.item(i)).getAttribute("type").equals(trade)) {
				NodeList myTradesList = ((Element) listTrades.item(i)).getElementsByTagName("deficiency");
				total += myTradesList.getLength();
			}
		}
		return total;
	}
	
	public static int outstandingDefsByTrade(String trade) {
	
		int outstanding = 0;
		for (int i = 0; i < listTrades.getLength(); ++i) {
			if (((Element) listTrades.item(i)).getAttribute("type").equals(trade)) {
				NodeList outstandingTradesList = ((Element) listTrades.item(i)).getElementsByTagName("deficiency");
				for (int j = 0; j < outstandingTradesList.getLength(); ++j) {
					NodeList outstandingTradeDeficiencyList = ((Element) outstandingTradesList.item(j))
						.getElementsByTagName("completed");
					if (((Element) outstandingTradeDeficiencyList.item(0)).getTextContent().equals(
						"false")) {
						outstanding++;
					}
				}
			}
		}
		return outstanding;
	}
	
	/** NOT IMPLEMENTED. */
	public static int outstandingDefsByUnit(String unit) {
	
		
		return -1;
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
		String x = coor.getAttribute(Deficiency.XCOORD);
		if (x.equalsIgnoreCase(""))
			def.X = 0;
		else
			def.X = Integer.parseInt(x);
		String y = coor.getAttribute(Deficiency.YCOORD);
		if (y.equalsIgnoreCase(""))
			def.Y = 0;
		else
			def.Y = Integer.parseInt(y);
		
		if (defElem.getElementsByTagName(Deficiency.OBJECT).item(0) != null) {
			def.object = defElem.getElementsByTagName(Deficiency.OBJECT).item(0).getTextContent();
			def.item = defElem.getElementsByTagName(Deficiency.ITEM).item(0).getTextContent();
			def.verb = defElem.getElementsByTagName(Deficiency.VERB).item(0).getTextContent();
			def.direction = defElem.getElementsByTagName(Deficiency.DIRECTION).item(0).getTextContent();
			def.location = defElem.getElementsByTagName(Deficiency.LOCATION).item(0).getTextContent();
			
			def.trade = ((Element) defElem.getParentNode()).getAttribute(Deficiency.TRADE);
			if (def.trade.equalsIgnoreCase(""))
				def.trade = "Framing";
			def.roomNo = ((Element) defElem.getParentNode().getParentNode()).getAttribute(Deficiency.ROOMNO);
			if (def.roomNo.equalsIgnoreCase("")) {
				Element room = (Element) listRoomNodes.item(0);
				if (room == null)
					return null;
				def.roomNo = room.getAttribute(Deficiency.ROOMNO);
			}
		}
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
	
		
		if (fromAssets)
			loadBitmapFromAsset(image, getRoomImageFile(roomNo));
		else
			image.setImageBitmap(roomImages.get(roomNo));
	}
	
	
	public static void loadFloorPlan(ImageView image, String floorID) {
	
		
		if (fromAssets)
			loadBitmapFromAsset(image, getFloorImageFile(floorID));
		else
			image.setImageBitmap(floorImages.get(floorID));
	}
	
	
	
	private static void loadBitmapFromAsset(ImageView image, String file) {
	
		try {
			InputStream is = assetMan.open(file);
			Bitmap bmap = BitmapFactory.decodeStream(is);
			image.setImageBitmap(bmap);
		} catch (IOException ex) {
			image.setImageBitmap(nopic);
		}
		
	}
	
	
	public static Bitmap loadImmutableBitmap(String roomNo) {
	
		Bitmap immutable;
		
		if (fromAssets) {
			String file = DeficiencyParser.getRoomImageFile(roomNo);
			InputStream is = null;
			try {
				is = DeficiencyParser.assetMan.open(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			immutable = BitmapFactory.decodeStream(is);
			
			loadBitmapFromAsset(immutable, file);
			
		} else {
			immutable = roomImages.get(roomNo).copy(Bitmap.Config.ARGB_8888, true);
		}
		return immutable;
		
	}
	
//	public static void loadRoomPlanBMP(Bitmap image, String roomNo) {
//	
//		String file = DeficiencyParser.getRoomImageFile(roomNo);
//		loadBitmapFromAsset(image, file);
//	}
//	
	
	private static void loadBitmapFromAsset(Bitmap image, String file) {
	
		try {
			InputStream is = assetMan.open(file);
			image = BitmapFactory.decodeStream(is);
		} catch (IOException ex) {
			ex.printStackTrace();
			image = nopic;
		}
		
	}
	
	/** Loads an image from the assets directory. */
	private static void loadImageFromAsset(ImageView image, String file) {
	
		try {
			InputStream is = assetMan.open(file);
			image.setImageDrawable(Drawable.createFromStream(is, null));
		} catch (IOException ex) {
			image.setImageBitmap(nopic);
			ex.printStackTrace();
		}
	}
	
	
	/** Load an image from a directory on device. */
	private static void loadImageFromStorage(ImageView image, InputStream is) {
	
		
		Bitmap bitmap = BitmapFactory.decodeStream(is);
		if (bitmap == null)
			image.setImageBitmap(nopic);
		else
			image.setImageBitmap(bitmap);
	}
	
	/** Load an image from a URI. Used from DownloadImages. */
	public static Bitmap loadImageFromUri(InputStream is) {
	
		Bitmap bmp = BitmapFactory.decodeStream(is);
//		if (bmp == null)
//			bmp = nopic;
		return bmp;
	}
	
	
	public static int getImageWidth(String roomNo) {
	
		String file = getRoomImageFile(roomNo);
		Bitmap bmap = null;
		try {
			InputStream is = assetMan.open(file);
			bmap = BitmapFactory.decodeStream(is);
		} catch (IOException ex) {
			return -1;
		}
		
		return bmap.getWidth();
	}
	
	
	public static int getImageHeight(String roomNo) {
	
		String file = getRoomImageFile(roomNo);
		Bitmap bmap = null;
		try {
			InputStream is = assetMan.open(file);
			bmap = BitmapFactory.decodeStream(is);
		} catch (IOException ex) {
			return -1;
		}
		
		return bmap.getHeight();
	}
	
	
	/** generates a new reportID by taking the project code and adding one to the
	 * total count of deficiencies */
	public static String generateReportId() {
	
		String reportId = projectName;
		int number = listDeficiencyNodes.getLength() + 1;
		String num = Integer.toString(number);
		for (int i = 0; i < 6 - num.length(); ++i) {
			reportId = reportId.concat("0");
		}
		return reportId.concat(num);
	}
	
	public static void setCompletionStatus(String unit, String trade, String reportID, boolean complete) {
	
		// find the room by no
		for (int i = 0; i < listRoomNodes.getLength(); ++i) {
			if (((Element) listRoomNodes.item(i)).getAttribute("no").equals(unit)) {
				Element room = ((Element) listRoomNodes.item(i));
				// find the trade by type
				NodeList roomTrades = room.getElementsByTagName("trade");
				for (int j = 0; j < roomTrades.getLength(); ++j) {
					if (((Element) roomTrades.item(j)).getAttribute("type").equals(trade)) {
						Element curTrade = ((Element) roomTrades.item(j));
						
						// find the deficiency by reportID
						NodeList reportsList = curTrade.getElementsByTagName("deficiency");
						for (int k = 0; k < reportsList.getLength(); ++k) {
							if (((Element) reportsList.item(k)).getAttribute("reportID").equals(
								reportID)) {
								
								// get the node completed
								Element curReport = (Element) reportsList.item(k);
								// set the completed node value
								NodeList attributes = curReport.getElementsByTagName("completed");
								Node complet = attributes.item(0);
								complet.setTextContent((complete) ? "true" : "false");
								
								writeXml();
//								refresh();
								return;
								
							}
						}
					}
				}
			}
		}
	}
	
	/** Edits a deficiency. Called from DeficiencyWheel Save button. */
	public static void editDeficiency(Deficiency editDef) {
	
		NodeList reportsList = listDeficiencyNodes;
		
		for (int g = 0; g < listDeficiencyNodes.getLength(); ++g) {
			// if the report id contained in the deficiency object exists, edit
			// that entry rather than adding a new one
			if (((Element) reportsList.item(g)).getAttribute("reportID").equals(editDef.reportID)) {
				editDefic(editDef, (Element) reportsList.item(g));
				writeXml();
				refresh();
				return;
			}
		}
		
	}
	
	/** Creates a new deficiency. Called from DeficiencyWheel Save button. */
	public static void addNewDeficiency(Deficiency newDefic) {
	
		
//		NodeList floorsList = listFloorNodes;
//		for (int h = 0; h < listFloorNodes.getLength(); ++h) {
//			
//			if (((Element) floorsList.item(h)).getAttribute("floorID").equals(newDefic.floor)) {
//				
//				NodeList roomsList = ((Element) floorsList.item(h)).getElementsByTagName("room");
		NodeList roomsList = listRoomNodes;
		for (int i = 0; i < roomsList.getLength(); ++i) {
			
			if (((Element) roomsList.item(i)).getAttribute("no").equals(newDefic.roomNo)) {
				
				NodeList tradesList = ((Element) roomsList.item(i)).getElementsByTagName("trade");
				
				for (int j = 0; j < tradesList.getLength(); ++j) {
					
					if (((Element) tradesList.item(j)).getAttribute("type").equals(newDefic.trade)) {
						setNewDefic(newDefic, (Element) tradesList.item(j));
						
						writeXml();
						refresh();
						return;
					}
				}
				
				// If this is first deficiency in this trade for the room
				Element newTradeNode = xmlDoc.createElement("trade");
				newTradeNode.setAttribute("type", newDefic.trade);
				((Element) roomsList.item(i)).appendChild(newTradeNode);
				setNewDefic(newDefic, newTradeNode);
				
				writeXml();
				refresh();
				return;
				
			}
		}
	}
	
	
	
	private static void editDefic(Deficiency defic, Element reportNode) {
	
		((Element) reportNode.getElementsByTagName("completed").item(0))
			.setTextContent((defic.completed) ? "true" : "false");
		((Element) reportNode.getElementsByTagName("priority").item(0))
			.setTextContent((defic.priority) ? "true" : "false");
		((Element) reportNode.getElementsByTagName("coordinates").item(0))
			.setAttribute("x", Integer.toString(defic.X));
		((Element) reportNode.getElementsByTagName("coordinates").item(0))
			.setAttribute("y", Integer.toString(defic.Y));
		Element descrip = ((Element) reportNode.getElementsByTagName("description").item(0));
		((Element) descrip.getElementsByTagName("item").item(0)).setTextContent(defic.item);
		((Element) descrip.getElementsByTagName("verb").item(0)).setTextContent(defic.verb);
		((Element) descrip.getElementsByTagName("direction").item(0)).setTextContent(defic.direction);
		((Element) descrip.getElementsByTagName("location").item(0)).setTextContent(defic.location);
		((Element) descrip.getElementsByTagName("object").item(0)).setTextContent(defic.object);
		
		writeXml();
		refresh();
		
		return;
	}
	
	private static void setNewDefic(Deficiency defic, Element tradeNode) {
	
		// create the new deficiency Node and append to the tradeNode Element
		Element deficiencyNode = xmlDoc.createElement("deficiency");
		deficiencyNode.setAttribute("reportID", defic.reportID);
		tradeNode.appendChild(deficiencyNode);
		
		// create and append the deficiency Node child elements
		
		Element completedNode = xmlDoc.createElement("completed");
		completedNode.setTextContent("false");
		deficiencyNode.appendChild(completedNode);
		
		Element priorityNode = xmlDoc.createElement("priority");
		priorityNode.setTextContent((defic.priority) ? "true" : "false");
		deficiencyNode.appendChild(priorityNode);
		
		Element coordinateNode = xmlDoc.createElement("coordinates");
		coordinateNode.setAttribute("y", Integer.toString(defic.Y));
		coordinateNode.setAttribute("x", Integer.toString(defic.X));
		deficiencyNode.appendChild(coordinateNode);
		
		Element descriptionNode = xmlDoc.createElement("description");
		deficiencyNode.appendChild(descriptionNode);
		
		// create and append the description Node child elements
		
		Element objectNode = xmlDoc.createElement("object");
		objectNode.setTextContent(defic.object);
		descriptionNode.appendChild(objectNode);
		
		Element itemNode = xmlDoc.createElement("item");
		itemNode.setTextContent(defic.item);
		descriptionNode.appendChild(itemNode);
		
		Element verbNode = xmlDoc.createElement("verb");
		verbNode.setTextContent(defic.verb);
		descriptionNode.appendChild(verbNode);
		
		Element directionNode = xmlDoc.createElement("direction");
		directionNode.setTextContent(defic.direction);
		descriptionNode.appendChild(directionNode);
		
		Element locationNode = xmlDoc.createElement("location");
		locationNode.setTextContent(defic.location);
		descriptionNode.appendChild(locationNode);
		
	}
	
	/** writes the current DOM doc back to the xml file specified in the
	 * beginning of the class */
	private static void writeXml() {
	
		
		try {
			try {
				if (projectXML != null)
					projectXML.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(xmlDoc);
			StreamResult result = new StreamResult(fileXML);
			transformer.transform(source, result);
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
}
