package vdindustries.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.widget.ImageView;

public class DeficiencyParser {

	public static InputStream projectXML;
	public static Element root;
	public static NodeList listFloorNodes;
	public static NodeList listRoomNodes;

	public static NodeList listTrades;
	private static AssetManager assMan;

	private static Document xmlDoc;
	private static File fileXML;
	private static OutputStream writeXml;

	// private String filePath = ".//mnt//shared//geny//testproject.xml";

	/** Implementation: DeficiencyParser(getAssets()) */
	public DeficiencyParser(/*AssetManager am*/) {

		//assMan = am;
		try {
			fileXML = new File(Environment.getExternalStorageDirectory()
					+ "/Download/" + "testproject.xml");
			projectXML = null;
			projectXML = new FileInputStream(fileXML);

			// projectXML = assMan.open("testproject.xml");
			// projectXML =
			// assMan.open(Environment.getExternalStorageDirectory().getAbsolutePath()
			// + "//Download//testproject.xml");
			// projectXML = assMan.open(new
			// File("//mnt//shared//geny//testproject.xml"));

			// String filepath = "c:\\file.xml";
			// DocumentBuilderFactory docFactory =
			// DocumentBuilderFactory.newInstance();
			// DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			// Document doc = docBuilder.parse(filepath);

			// Document xmlDoc = DocumentBuilderFactory
			// .newInstance()
			// .newDocumentBuilder()
			// .parse(new File(Environment.getExternalStorageDirectory()
			// .getAbsolutePath() + "/Download/testproject.xml"));
			xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(projectXML);
			root = xmlDoc.getDocumentElement();
			root.normalize();
			listFloorNodes = root.getElementsByTagName("floor");
			listTrades = root.getElementsByTagName("trade");
			listRoomNodes = root.getElementsByTagName("room");

		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	/*
	 * // Builds a list of deficiencies by trade for each room. // not currently
	 * working - needs listRooms public static List<Deficiency>
	 * getByTradeList(String tradeSelected, String room) {
	 * 
	 * List<Deficiency> defList = new ArrayList<Deficiency>();
	 * 
	 * for (int k = 0; k < listRooms.getLength(); ++k) { if (((Element)
	 * listRooms.item(k)).getAttribute("no") .equalsIgnoreCase(room)) {
	 * 
	 * // for each <trade> element NodeList tradeNodes = ((Element)
	 * listRooms.item(k)) .getElementsByTagName("trade"); // if <trade type =
	 * tradeSelected> for (int i = 0; i < tradeNodes.getLength(); ++i) { if
	 * (((Element) tradeNodes.item(i)).getAttribute("type")
	 * .equalsIgnoreCase(tradeSelected)) {
	 * 
	 * // get a list of all child deficiency elements NodeList nodes =
	 * ((Element) tradeNodes.item(i)) .getElementsByTagName("deficiency");
	 * 
	 * for (int j = 0; j < nodes.getLength(); ++j)
	 * defList.add(parseDeficiency(nodes.item(j))); } } } } return defList; }
	 */

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

	/**
	 * Retrieves a deficiency object from its reportID. Note: this is likely
	 * very inefficient in the case of large buildings.
	 */
	public static Deficiency getDefByID(String reportID) {

		NodeList defList = root.getElementsByTagName("deficiency");
		for (int i = 0; i < defList.getLength(); ++i) {

			if (((Element) defList.item(i)).getAttribute(Deficiency.REPORTID)
					.equalsIgnoreCase(reportID)) {

				return parseDeficiency(defList.item(i));
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
			if (((Element) listTrades.item(i)).getAttribute("type")
					.equalsIgnoreCase(tradeSelected)) {

				// get a list of all child deficiency elements
				NodeList nodes = ((Element) listTrades.item(i))
						.getElementsByTagName("deficiency");

				for (int j = 0; j < nodes.getLength(); ++j)
					defList.add(parseDeficiency(nodes.item(j)));

				// break;
			}
		}

		return defList;
	}

	public static List<Deficiency> getDefsByRoom(String roomNo) {

		List<Deficiency> defList = new ArrayList<Deficiency>();

		for (int i = 0; i < listRoomNodes.getLength(); ++i) {

			Element roomElem = ((Element) listRoomNodes.item(i));
			if (roomElem.getAttribute(Deficiency.ROOMNO).equalsIgnoreCase(
					roomNo)) {

				NodeList nodes = roomElem.getElementsByTagName("deficiency");

				for (int j = 0; j < nodes.getLength(); ++j)
					defList.add(parseDeficiency(nodes.item(j)));

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

				return ((Element) current.getElementsByTagName(
						Deficiency.ROOMPLAN).item(0))
						.getAttribute(Deficiency.ROOMIMAGE);
			}
		}
		return null;
	}

	/** Retrieves floor plan image file location from it's ID. */
	private static String getFloorImageFile(String floorID) {

		for (int i = 0; i < listFloorNodes.getLength(); ++i) {

			Element current = (Element) listFloorNodes.item(i);
			if (current.getAttribute(Deficiency.FLOORID).equals(floorID)) {

				return ((Element) current.getElementsByTagName(
						Deficiency.FLOORPLAN).item(0))
						.getAttribute(Deficiency.FLOORIMAGE);
			}
		}
		return null;
	}

	/** Builds a deficiency object from a XML node. */
	private static Deficiency parseDeficiency(Node node) {

		Deficiency def = new Deficiency();
		Element defElem = (Element) node;

		def.reportID = defElem.getAttribute(Deficiency.REPORTID);

		def.completed = Boolean.parseBoolean(defElem

		.getElementsByTagName(Deficiency.COMPLETED).item(0).getTextContent());

		def.priority = Boolean.parseBoolean(defElem
				.getElementsByTagName(Deficiency.PRIORITY).item(0)
				.getTextContent());

		Element coor = ((Element) defElem.getElementsByTagName("coordinates")
				.item(0));

		def.X = Integer.parseInt(coor.getAttribute(Deficiency.XCOORD));
		def.Y = Integer.parseInt(coor.getAttribute(Deficiency.YCOORD));

		def.object = defElem.getElementsByTagName(Deficiency.OBJECT).item(0)
				.getTextContent();
		def.item = defElem.getElementsByTagName(Deficiency.ITEM).item(0)
				.getTextContent();
		def.verb = defElem.getElementsByTagName(Deficiency.VERB).item(0)
				.getTextContent();
		def.direction = defElem.getElementsByTagName(Deficiency.DIRECTION)
				.item(0).getTextContent();
		def.location = defElem.getElementsByTagName(Deficiency.LOCATION)
				.item(0).getTextContent();

		def.trade = ((Element) defElem.getParentNode())
				.getAttribute(Deficiency.TRADE);
		def.roomNo = ((Element) defElem.getParentNode().getParentNode())
				.getAttribute(Deficiency.ROOMNO);
		return def;
	}

	/** Retrieve a list of room IDs by floorID. */
	public static List<String> getRoomIDs(String floorID) {

		List<String> rooms = new ArrayList<String>();

		for (int i = 0; i < listFloorNodes.getLength(); ++i) {

			Element current = (Element) listFloorNodes.item(i);
			if (current.getAttribute(Deficiency.FLOORID).equalsIgnoreCase(
					floorID)) {

				NodeList roomNodes = current
						.getElementsByTagName(Deficiency.ROOM);
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
		try {
			image.setImageBitmap(loadBitmapFromAsset(file));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void loadFloorPlan(ImageView image, String floorID) {

		String file = getFloorImageFile(floorID);
		try {
			image.setImageDrawable(loadImageFromAsset(file)); // CHANGE THIS
																// LINE
			// image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static Bitmap loadBitmapFromAsset(String file) throws IOException {

		InputStream is = assMan.open(file);
		return BitmapFactory.decodeStream(is);
	}

	/**
	 * Load an image from a directory on device. NOT YET IMPLEMENTED
	 */
	private static Drawable loadImageFromDirectory(String file) {

		// try {
		// Bitmap bmap = BitmapFactory.decodeFile();
		// } catch (IOException ex) {
		// throw ex;
		// }
		return null;

	}

	/**
	 * Load an image from a URI. NOT YET IMPLEMENTED
	 */
	private static Drawable loadImageFromUri(String uri) {

		return null;
	}

	/** Loads an image from the assets directory. */
	protected static Drawable loadImageFromAsset(String file)
			throws IOException {

		InputStream is = assMan.open(file);
		return Drawable.createFromStream(is, null);
	}

	public static void setCompletionStatus(String unit, String trade,
			String reportID, boolean complete) {
		// find the room by no
		for (int i = 0; i < listRoomNodes.getLength(); ++i) {
			if (((Element) listRoomNodes.item(i)).getAttribute("no") == unit) {
				Element room = ((Element) listRoomNodes.item(i));
				// find the trade by type
				NodeList roomTrades = room.getElementsByTagName("trade");
				for (int j = 0; j < roomTrades.getLength(); ++j) {
					if (((Element) roomTrades.item(j)).getAttribute("type") == trade) {
						Element curTrade = ((Element) roomTrades.item(j));

						// find the deficiency by reportID
						NodeList reportsList = curTrade
								.getElementsByTagName("deficiency");
						for (int k = 0; k < reportsList.getLength(); ++k) {
							if (((Element) reportsList.item(k))
									.getAttribute("reportID") == reportID) {

								// get the node completed
								Element curReport = (Element) reportsList.item(k);
								// set the completed node value 
								NodeList attributes = curReport.getElementsByTagName("completed");
								Node complet = attributes.item(0);
								complet.setTextContent((complete) ? "true": "false");
								

								// writeXML();
								try{
									try {
										projectXML.close();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									TransformerFactory transformerFactory = TransformerFactory
											.newInstance();
									Transformer transformer = transformerFactory
											.newTransformer();
								//	DOMSource source = null;
									DOMSource source = new DOMSource(xmlDoc);

									// StreamResult result = new
									// StreamResult(new File(filepath));
									//StreamResult result = null;
									 StreamResult result = new StreamResult(
											fileXML);

									// StreamResult result = new
									// StreamResult(projectXML);
									transformer.transform(source, result);

								} catch (TransformerException tfe) {
									tfe.printStackTrace();
								}
								/*
								root = xmlDoc.getDocumentElement();
								root.normalize();
								listFloorNodes = root.getElementsByTagName("floor");
								listTrades = root.getElementsByTagName("trade");
								listRoomNodes = root.getElementsByTagName("room");
								Categories.defParse();
								*/
								return;
								
								//Categories.defParse();
							
								
								/*
								try{
								fileXML = new File(Environment.getExternalStorageDirectory()
										+ "/Download/" + "testproject.xml");
								projectXML = null;
								projectXML = new FileInputStream(fileXML);
								
								xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
										.parse(projectXML);
								root = xmlDoc.getDocumentElement();
								root.normalize();
								listFloorNodes = root.getElementsByTagName("floor");
								listTrades = root.getElementsByTagName("trade");
								listRoomNodes = root.getElementsByTagName("room");
								
							} catch (SAXException | IOException | ParserConfigurationException e) {
								e.printStackTrace();
							}
							return;*/
								/*
								 * try {
								 * 
								 * 
								 * 
								 * fileXML = new
								 * File(Environment.getExternalStorageDirectory
								 * () + "/Download/" + "testproject.xml");
								 * projectXML = null; projectXML = new
								 * FileInputStream(fileXML);
								 * 
								 * DocumentBuilderFactory.newInstance().
								 * newDocumentBuilder() .parse(projectXML);
								 * xmlDoc =
								 * DocumentBuilderFactory.newInstance().
								 * newDocumentBuilder() .parse(projectXML); root  
								 * = xmlDoc.getDocumentElement();
								 * root.normalize(); listFloorNodes =
								 * root.getElementsByTagName("floor");
								 * listTrades =
								 * root.getElementsByTagName("trade");
								 * listRoomNodes =
								 * root.getElementsByTagName("room");
								 * 
								 * } catch (SAXException | IOException |
								 * ParserConfigurationException e) {
								 * e.printStackTrace(); }
								 * 
								 * return; } }
							 */

							}
						}
					}
				}
			}
		}
	}

	/*
	 * public static void writeXML() { try { TransformerFactory
	 * transformerFactory = TransformerFactory .newInstance(); Transformer
	 * transformer = transformerFactory.newTransformer(); DOMSource source = new
	 * DOMSource(xmlDoc); StreamResult result = new StreamResult(new
	 * File(filepath)); transformer.transform(source, result);
	 * 
	 * } catch (TransformerException tfe) { tfe.printStackTrace(); }
	 * 
	 * }
	 */
}
