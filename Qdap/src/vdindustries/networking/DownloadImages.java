package vdindustries.networking;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.os.AsyncTask;


public class DownloadImages extends AsyncTask<Map<String, String>, Integer, Boolean> {
	
	
	
	private Map<String, InputStream>	floorImages;
	private Map<String, InputStream>	roomImages;
	
	
	public DownloadImages(Map<String, InputStream> floorImgs, Map<String, InputStream> roomImgs) {
	
		floorImages = floorImgs;
		roomImages = roomImgs;
	}
	
	
	@Override protected Boolean doInBackground(Map<String, String>... params) {
	
		Map<String, String> floorUris = params[0];
		Map<String, String> roomUris = params[1];
		
		Iterator<Entry<String, String>> it = floorUris.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
//		        System.out.println(pairs.getKey() + " = " + pairs.getValue());
			InputStream is;
			try {
				is = (InputStream) new URL(pairs.getValue()).getContent();
			} catch (IOException e) {
					e.printStackTrace();
//					InputStream is = assMan.open("images/nopic.jpg");
				is = null;
			}
			
			floorImages.put(pairs.getKey(), is);
			it.remove(); // avoids a ConcurrentModificationException
		}
		
		return false;
	}
	
	
	@Override protected void onProgressUpdate(Integer... progress) {
	
//      setProgressPercent(progress[0]);
	}
	
	
	@Override protected void onPostExecute(Boolean bool) {
	
	}
	
}
