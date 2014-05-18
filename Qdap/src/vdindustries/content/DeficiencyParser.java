package vdindustries.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import vdindustries.Categories;
import vdindustries.networking.DownloadImages;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.widget.ImageView;



public class DeficiencyParser {
	
	public static String					projectName;
	
	public static InputStream				projectXML;
	public static Element					root;
	public static NodeList					listFloorNodes;
	public static NodeList					listRoomNodes;
	public static NodeList					listDeficiencyNodes;
	public static NodeList					listTrades;
	
	public static Map<String, InputStream>	floorImages;
	public static Map<String, InputStream>	roomImages;
	
	private static AssetManager				assMan;
	
	private static Document					xmlDoc;
	private static File						fileXML;
	
	private static boolean					fromAssets;
	
	private static String path = "/Download/";
	private String file = "testproject.xml"; 
	
	
	/** Internal true: Create using the DEFAULT project stored on internal storage. 
	 * 	Internal false: Creates using the DEFAULT project stored in assets.
	 * 			Implementation: DeficiencyParser(getAssets()) */
	public DeficiencyParser(AssetManager am, Boolean internal) {
	
		assMan = am;
		if (internal) {
			try {
				fileXML = new File(Environment.getExternalStorageDirectory() + "/Download/" + "testproject.xml");
				projectXML = null;
				projectXML = new FileInputStream(fileXML);
				xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
					projectXML);
				setup();
				
			} catch (SAXException | IOException | ParserConfigurationException e) {
				e.printStackTrace();
			}
			
			fromAssets = false;
		} else {
			
			try {
				projectXML = assMan.open("testproject.xml");
				
				xmlDoc = DocumentBuilderFactory.newInstance().
					newDocumentBuilder().parse(projectXML);
				setup();
				
			} catch (SAXException | IOException | ParserConfigurationException e) {
				e.printStackTrace();
			}
			fromAssets = true;
			projectName = root.getAttribute("name");
		}
	}
	
	
	
	/** Creates using the DEFAULT project stored in assets.
	 * Implementation: DeficiencyParser(getAssets()) */
	public DeficiencyParser(AssetManager am) {
	
		assMan = am;
		try {
			projectXML = assMan.open("testproject.xml");
			
			xmlDoc = DocumentBuilderFactory.newInstance().
				newDocumentBuilder().parse(projectXML);
			setup();
			
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		fromAssets = true;
		projectName = root.getAttribute("name");
	}
	
	/** Create using a project stored in internal storage. */
	public DeficiencyParser(Document doc, String projname, AssetManager am) {
	
		assMan = am;
		
		fileXML = new File(Environment.getExternalStorageDirectory() + "/Download/" + projname);
		if (!fileXML.exists()) {
			try {
				fileXML.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		fromAssets = false;
		xmlDoc = doc;
		
		setup();
	}
	
	public DeficiencyParser() {
		try {
			fileXML = new File(Environment.getExternalStorageDirectory() +path + file);
			//projectXML = null;
			projectXML = new FileInputStream(fileXML);
			xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(projectXML);
			root = xmlDoc.getDocumentElement();
			root.normalize();
		//	projectName = "";
			projectName=root.getAttribute("name");
			listFloorNodes = root.getElementsByTagName("floor");
			listTrades = root.getElementsByTagName("trade");
			listRoomNodes = root.getElementsByTagName("room");
			listDeficiencyNodes = root.getElementsByTagName("deficiency");


		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	
	private void setup() {
	
		root = xmlDoc.getDocumentElement();
		root.normalize();
		
		projectName = root.getAttribute("name");
		
		listTrades = root.getElementsByTagName("trade");
		listRoomNodes = root.getElementsByTagName("room");
		listFloorNodes = root.getElementsByTagName("floor");
		listDeficiencyNodes = root.getElementsByTagName("deficiency");
		
		getImagesFromUri();
	}
	
	
	private void getImagesFromUri() {
	
		floorImages = new HashMap<String, InputStream>();
		roomImages = new HashMap<String, InputStream>();
		
		// Parse address of images
		/* Map<floorID, uri> */
		Map<String, String> floorUris = new HashMap<String, String>();
		/* Map<roomNo, uri> */
		Map<String, String> roomUris = new HashMap<String, String>();
		
		for (int i = 0; i < listFloorNodes.getLength(); ++i) {
			Element floor = ((Element) listFloorNodes.item(i));
			floorUris.put(floor.getAttribute(Deficiency.FLOORID),
				floor.getAttribute(Deficiency.FLOORIMAGE));
		}
		for (int i = 0; i < listRoomNodes.getLength(); ++i) {
			Element room = ((Element) listRoomNodes.item(i));
			roomUris.put(room.getAttribute(Deficiency.ROOMNO),
				room.getAttribute(Deficiency.ROOMIMAGE));
		}
		
		// Download Images
		new DownloadImages(floorImages, roomImages).execute(floorUris, roomUris);
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
				
			//	break;
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
		if (fromAssets)
			loadBitmapFromAsset(image, file);
		else
			loadImageFromStorage(image, file);
	}
	
	
	public static void loadFloorPlan(ImageView image, String floorID) {
	
		String file = getFloorImageFile(floorID);
		if (fromAssets)
			loadBitmapFromAsset(image, file);
		else
			loadImageFromStorage(image, file);
	}
	
	
	
	private static void loadBitmapFromAsset(ImageView image, String file) {
	
		try {
			InputStream is = assMan.open(file);
			Bitmap bmap = BitmapFactory.decodeStream(is);
			image.setImageBitmap(bmap);
		} catch (IOException ex) {
			ex.printStackTrace();
			try {
				image.setImageBitmap(BitmapFactory.decodeStream(assMan.open("images/nopic.jpg")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	public static void loadRoomPlanBMP(Bitmap image, String roomNo) {
	
		String file = DeficiencyParser.getRoomImageFile(roomNo);
		loadBitmapFromAsset(image, file);
		
	}
	
	
	private static void loadBitmapFromAsset(Bitmap image, String file) {
	
		try {
			InputStream is = assMan.open(file);
			image = BitmapFactory.decodeStream(is);
//			image.setImageBitmap(bmap);
		} catch (IOException ex) {
			ex.printStackTrace();
			try {
				image = BitmapFactory.decodeStream(assMan.open("images/nopic.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/** Loads an image from the assets directory. */
	private static void loadImageFromAsset(ImageView image, String file) {
	
		try {
			InputStream is = assMan.open(file);
			image.setImageDrawable(Drawable.createFromStream(is, null));
		} catch (IOException ex) {
			try {
				InputStream is = assMan.open("images/nopic.jpg");
				image.setImageBitmap(BitmapFactory.decodeStream(is));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/** Load an image from a directory on device.
	 * NOT YET IMPLEMENTED */
	private static void loadImageFromStorage(ImageView image, String filepath) {
	
		
		Bitmap bitmap = BitmapFactory.decodeFile(filepath);
		if (bitmap == null)
			try {
			//	 InputStream is = assMan.open("images/nopic.jpg");
		InputStream is =	new FileInputStream(new File(Environment.getExternalStorageDirectory() +path+filepath));
				image.setImageBitmap(BitmapFactory.decodeStream(is));
			} catch (IOException e) {
				e.printStackTrace();
			}
		else
			image.setImageBitmap(bitmap);
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
			try {
				bmap = BitmapFactory.decodeStream(assMan.open("images/nopic.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
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
								
								return;
								
							}
						}
					}
				}
			}
		}
	}
	
	
	public static void addNewDeficiency(Deficiency newDefic) {
		NodeList reportsList = listDeficiencyNodes;
		for (int g = 0; g < listDeficiencyNodes.getLength(); ++g) {
			// if the report id contained in the deficiency object exists, edit
			// that entry rather than adding a new one
			if (((Element) reportsList.item(g)).getAttribute("reportID").equals(newDefic.reportID)) {
				editDefic(newDefic, (Element) reportsList.item(g));
				writeXml();
				Categories.defParse();
				return;
			}
		}

		NodeList floorsList = listFloorNodes;
		for (int h = 0; h < listFloorNodes.getLength(); ++h) {
			if (((Element) floorsList.item(h)).getAttribute("floorID").equals(newDefic.floor)) {
				NodeList roomsList = ((Element) floorsList.item(h)).getElementsByTagName("room");
				for (int i = 0; i < roomsList.getLength(); ++i) {
					if (((Element) roomsList.item(i)).getAttribute("no").equals(newDefic.roomNo)) {
						NodeList tradesList = ((Element) roomsList.item(i)).getElementsByTagName("trade");
						for (int j = 0; j < tradesList.getLength(); ++j) {
							if (((Element) tradesList.item(j)).getAttribute("type").equals(
								newDefic.trade)) {
								setNewDefic(newDefic, (Element) tradesList.item(j));
								
								writeXml();
								Categories.defParse();
								return;
							}
						}
						Element newTradeNode = xmlDoc.createElement("trade");
						newTradeNode.setAttribute("type", newDefic.trade);
						((Element) roomsList.item(i)).appendChild(newTradeNode);
						setNewDefic(newDefic, newTradeNode);
						
						writeXml();
						Categories.defParse();
						return;
						
					}
				}
			}
		}
	}
	
	private static void editDefic(Deficiency defic, Element reportNode) {
	
		((Element) reportNode.getElementsByTagName("completed").item(0)).setTextContent((defic.completed) ? "true"
																											: "false");
		((Element) reportNode.getElementsByTagName("priority").item(0)).setTextContent((defic.priority) ? "true"
																										: "false");
		((Element) reportNode.getElementsByTagName("coordinates").item(0)).setAttribute("x",
			Integer.toString(defic.X));
		((Element) reportNode.getElementsByTagName("coordinates").item(0)).setAttribute("y",
			Integer.toString(defic.Y));
		Element descrip = ((Element) reportNode.getElementsByTagName("description").item(0));
		((Element) descrip.getElementsByTagName("item").item(0)).setTextContent(defic.item);
		((Element) descrip.getElementsByTagName("verb").item(0)).setTextContent(defic.verb);
		((Element) descrip.getElementsByTagName("direction").item(0)).setTextContent(defic.direction);
		((Element) descrip.getElementsByTagName("location").item(0)).setTextContent(defic.location);
		((Element) descrip.getElementsByTagName("object").item(0)).setTextContent(defic.object);
		
		writeXml();
		Categories.defParse();
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
	
	// writes the current DOM doc back to the xml file specified in the
	// beginning of the class
	/*
	private static void writeXml() {
	
		if (fileXML == null) {
			String error = "Cannot edit: xml from assets loaded";
			// some kind of error message here
		} else {
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
	*/
	private static void writeXml() {
		try {
			try {
				projectXML.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(xmlDoc);
			StreamResult result = new StreamResult(fileXML);
			transformer.transform(source, result);
//fileXML = null;
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

}
