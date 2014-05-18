package vdindustries.networking;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import vdindustries.Categories;
import vdindustries.Qdap.R;
import vdindustries.content.Deficiency;
import vdindustries.content.DeficiencyParser;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

/** Code from:
 * http://www.androidhive.info/2013/07/how-to-implement-android-splash-screen-2/
 */
public class LoadingScreen extends ActionBarActivity {
	
	public static Context		context;
	private AssetManager		assetMan;
	private DeficiencyParser	parser;
	
	
	@Override protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		
		context = this;
		
		assetMan = getAssets();
		Intent intent = getIntent();
		
		
		
		if (intent.getBooleanExtra("test", true)) {
			
			Toast.makeText(this, "Test Project loaded", Toast.LENGTH_LONG).show();
			/* Create test project using xml from storage */
			parser = new DeficiencyParser(assetMan, true);
			
			Intent i = new Intent(this, Categories.class);
			startActivity(i);
			finish();
			
		} else {
			try {
				
				
				String xml = intent.getStringExtra("xmlFile");
				String projname = intent.getStringExtra("projname") + ".xml";
//			Toast.makeText(this, xml, Toast.LENGTH_LONG).show();
				
				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new
					File(getFilesDir() + File.separator + projname)));
				bufferedWriter.write(xml);
				bufferedWriter.close();
				
				
				DocumentBuilder db = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
				
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(xml));
				Document xmlDoc = db.parse(is);
				
				parser = new DeficiencyParser(xmlDoc, projname, assetMan, this);
				getImagesFromUri();
//				new DownloadImages(parser.floorImages, parser.roomImages).execute(parser.floorUris, parser.roomUris);
			} catch (ParserConfigurationException | SAXException | IOException | NullPointerException e) {
				
				Toast.makeText(this,
					"Error loading project" + "\n\nLoading default Test Project instead",
					Toast.LENGTH_LONG).show();
				/* Create test project using xml assets. */
				parser = new DeficiencyParser(assetMan, false);
				
				Intent i = new Intent(this, Categories.class);
				startActivity(i);
				finish();
			}
		}
	}
	
	
	private void getImagesFromUri() {
	
		parser.floorImages = new HashMap<String, Bitmap>();
		parser.roomImages = new HashMap<String, Bitmap>();
		
		// Parse address of images
		/* Map<floorID, uri> */
		Map<String, String> floorUris = new HashMap<String, String>();
		/* Map<roomNo, uri> */
		Map<String, String> roomUris = new HashMap<String, String>();
		
		for (int i = 0; i < parser.listFloorNodes.getLength(); ++i) {
			
			Element floor = ((Element) parser.listFloorNodes.item(i));
			Element floorplan = ((Element) floor.getElementsByTagName("floorplan").item(0));
			floorUris.put(floor.getAttribute(Deficiency.FLOORID),
				floorplan.getAttribute(Deficiency.FLOORIMAGE));
		}
		
		for (int i = 0; i < parser.listRoomNodes.getLength(); ++i) {
			Element room = ((Element) parser.listRoomNodes.item(i));
			Element roomplan = ((Element) room.getElementsByTagName("roomplan").item(0));
			roomUris.put(room.getAttribute(Deficiency.ROOMNO),
				roomplan.getAttribute(Deficiency.ROOMIMAGE));
		}
		
		// Download Images
		new DownloadImages(parser.floorImages, parser.roomImages).execute(floorUris, roomUris);
	}
	
	private class DownloadImages extends AsyncTask<Map<String, String>, Integer, Boolean> {
		
		
		
		private Map<String, Bitmap>	floorImages;
		private Map<String, Bitmap>	roomImages;
		
		
		public DownloadImages(Map<String, Bitmap> floorImgs, Map<String, Bitmap> roomImgs) {
		
			floorImages = floorImgs;
			roomImages = roomImgs;
		}
		
		
		@Override protected Boolean doInBackground(Map<String, String>... params) {
		
			Map<String, String> floorUris = params[0];
			Map<String, String> roomUris = params[1];
			
			Iterator<Entry<String, String>> it_flr = floorUris.entrySet().iterator();
			while (it_flr.hasNext()) {
				
				Map.Entry<String, String> pairs = (Map.Entry<String, String>) it_flr.next();
//			        System.out.println(pairs.getKey() + " = " + pairs.getValue());
				InputStream is;
				try {
					is = (InputStream) new URL("http://" + pairs.getValue()).getContent();
//					is = (InputStream) new URL("http://pbs.twimg.com/media/A97F0KzCAAAWU-0.jpg:large").getContent();
				} catch (IOException e) {
					e.printStackTrace();
					is = null;
				}
				
				floorImages.put(pairs.getKey(), DeficiencyParser.loadImageFromUri(is));
				it_flr.remove(); // avoids a ConcurrentModificationException
			}
			
			Iterator<Entry<String, String>> it_rm = roomUris.entrySet().iterator();
			while (it_rm.hasNext()) {
				
				Map.Entry<String, String> pairs = (Map.Entry<String, String>) it_rm.next();
//			        System.out.println(pairs.getKey() + " = " + pairs.getValue());
				InputStream is;
				try {
					is = (InputStream) new URL("http://" + pairs.getValue()).getContent();
//					is = (InputStream) new URL("https://pbs.twimg.com/media/AlGdUPnCIAAUwnK.jpg").getContent();
				} catch (IOException e) {
					e.printStackTrace();
					is = null;
				}
//				Toast.makeText(context, is.toString(), Toast.LENGTH_LONG).show();
				roomImages.put(pairs.getKey(), DeficiencyParser.loadImageFromUri(is));
				it_rm.remove(); // avoids a ConcurrentModificationException
			}
			
			return true;
		}
		
		
		@Override protected void onProgressUpdate(Integer... progress) {
		
//	      setProgressPercent(progress[0]);
			Toast.makeText(context, progress[0], Toast.LENGTH_LONG).show();
		}
		
		
		@Override protected void onPostExecute(Boolean result) {
		
			super.onPostExecute(result);
			// After completing http call
			// will close this activity and lauch main activity
			Intent i = new Intent(LoadingScreen.this, Categories.class);
			startActivity(i);
			
			// close this activity
			finish();
		}
		
	}
	
}
