package vdindustries.networking;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import vdindustries.Categories;
import vdindustries.content.DeficiencyParser;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;


//public class DownloadImages extends AsyncTask<Map<String, String>, Integer, Boolean> {
//	
//	
//	
//	private Map<String, Bitmap>	floorImages;
//	private Map<String, Bitmap>	roomImages;
//	
//	
//	public DownloadImages(Map<String, Bitmap> floorImgs, Map<String, Bitmap> roomImgs) {
//	
//		floorImages = floorImgs;
//		roomImages = roomImgs;
//	}
//	
//	
//	@Override protected Boolean doInBackground(Map<String, String>... params) {
//	
//		Map<String, String> floorUris = params[0];
//		Map<String, String> roomUris = params[1];
//		
//		Iterator<Entry<String, String>> it_flr = floorUris.entrySet().iterator();
//		while (it_flr.hasNext()) {
//			
//			Map.Entry<String, String> pairs = (Map.Entry<String, String>) it_flr.next();
////		        System.out.println(pairs.getKey() + " = " + pairs.getValue());
//			InputStream is;
//			try {
////				is = (InputStream) new URL(pairs.getValue()).getContent();
//				is = (InputStream) new URL("http://pbs.twimg.com/media/A97F0KzCAAAWU-0.jpg:large").getContent();
//			} catch (IOException e) {
//				e.printStackTrace();
//				is = null;
//			}
//			
//			floorImages.put(pairs.getKey(), DeficiencyParser.loadImageFromUri(is));
//			it_flr.remove(); // avoids a ConcurrentModificationException
//		}
//		
//		Iterator<Entry<String, String>> it_rm = roomUris.entrySet().iterator();
//		while (it_rm.hasNext()) {
//			
//			Map.Entry<String, String> pairs = (Map.Entry<String, String>) it_rm.next();
////		        System.out.println(pairs.getKey() + " = " + pairs.getValue());
//			InputStream is;
//			try {
//				is = (InputStream) new URL(pairs.getValue()).getContent();
////				is = (InputStream) new URL("https://pbs.twimg.com/media/AlGdUPnCIAAUwnK.jpg").getContent();
//			} catch (IOException e) {
//				e.printStackTrace();
//				is = null;
//			}
//			
//			roomImages.put(pairs.getKey(), DeficiencyParser.loadImageFromUri(is));
//			it_rm.remove(); // avoids a ConcurrentModificationException
//		}
//		
//		return true;
//	}
//	
//	
//	@Override protected void onProgressUpdate(Integer... progress) {
//	
////      setProgressPercent(progress[0]);
//	}
//	
//	
//	@Override protected void onPostExecute(Boolean result) {
//	
//		super.onPostExecute(result);
//		// After completing http call
//		// will close this activity and lauch main activity
//		Intent i = new Intent(LoadingScreen.context, Categories.class);
//		LoadingScreen.context.startActivity(i);
//		
//		// close this activity
//		LoadingScreen.done();
//	}
//	
//}
