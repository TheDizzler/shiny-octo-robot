package vdindustries;

import vdindustries.Qdap.R;
import vdindustries.networking.*;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends ActionBarActivity {
	
	EditText				user, password, project;
	
	public static Boolean	loggedIn	= false;
	
	public String			xmlFile;
	
	
	@Override protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
//		user = (EditText) findViewById(R.id.editUser);
//		password = (EditText) findViewById(R.id.editPassword);
//		project = (EditText) findViewById(R.id.editProject);
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
				.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	
	@Override public boolean onCreateOptionsMenu(Menu menu) {
	
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	
	@Override public boolean onOptionsItemSelected(MenuItem item) {
	
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/** A placeholder fragment containing a simple view. */
	public static class PlaceholderFragment extends Fragment {
		
		public PlaceholderFragment() {
		
		}
		
		@Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
											Bundle savedInstanceState) {
		
			View rootView = inflater.inflate(R.layout.fragment_login,
				container, false);
			return rootView;
		}
	}
	
	/** Connect to webserver. */
	public void login(View view) {
	
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		
		ConnectivityManager connectivityManager = (ConnectivityManager)
													this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = null;
		if (connectivityManager != null) {
			networkInfo =
							connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		}
		
		if (!wifi.isWifiEnabled()) {
			
			Toast.makeText(this, "Please enable Wifi", Toast.LENGTH_LONG).show();
			
		} else if (networkInfo == null || !networkInfo.isConnected()) {
			
			Toast.makeText(this, "Please check your network connection", Toast.LENGTH_LONG).show();
			
		} else {
			
			user = (EditText) findViewById(R.id.editUser);
			password = (EditText) findViewById(R.id.editPassword);
			project = (EditText) findViewById(R.id.editProject);
			
			new ConnectToServer(this, user.getText().toString(),
				password.getText().toString(), project.getText().toString())
				.execute("http://qdap.ca/android_check.php");
		}
	}
	
	public void sync(View view) {
	
		project = (EditText) findViewById(R.id.editProject);
		
		Intent intent = new Intent(this, Categories.class);
		intent.putExtra("test", false);
		intent.putExtra("xmlFile", xmlFile);
		intent.putExtra("projname", project.getText().toString());
		this.startActivity(intent);
		
	}
	
	
	public void testProjectSetup(View view) {
	
		Intent intent = new Intent(this, Categories.class);
		intent.putExtra("test", true);
		this.startActivity(intent);
		
	}
	
}
//
///** Required to connect and download from server. */
//class DownloadFromServer extends AsyncTask<String, Integer, String> {
//	
//	private String				user, password, project;
//	private List<NameValuePair>	nvp;
//	private InputStream			is;
//	private Login				context;
//	
//	
//	
//	public DownloadFromServer(Login login, String user, String pw, String proj) {
//	
//		this.user = user;
//		this.password = pw;
//		this.project = proj;
//		this.context = login;
//		if (project.length() == 0)
//			project = "ERROR";
//	}
//	
//	
//	@Override protected String doInBackground(String... params) {
//	
//		nvp = new ArrayList<NameValuePair>();
//		//Pass user message with Index Value "UserMessage"
////		nvp.add(new BasicNameValuePair("UserMessage", "Is that you server? It's me, Android"));
//		nvp.add(new BasicNameValuePair("username", user));
//		nvp.add(new BasicNameValuePair("password", password));
//		nvp.add(new BasicNameValuePair("project", project));
//		try {
//			HttpClient httpClient = new DefaultHttpClient();
//			
//			HttpPost httpPost = new HttpPost(params[0]);
//			
//			// Pass nameValuePairs to server
//			httpPost.setEntity(new UrlEncodedFormEntity(nvp));
//			
//			HttpResponse response = httpClient.execute(httpPost);
//			HttpEntity entity = response.getEntity();
//			is = entity.getContent();
//			Log.d("<<<-http_log_tag->>>", "Succes in Http Connection");
//			
//			
//		} catch (Exception e) {
//			Log.e("<<<-http_log_tag->>>", "Error in Http Connection" + e.toString());
//		}
//		
//		String result = "";
//		// convert response to string
//		try {
//			BufferedReader reader = new BufferedReader(
//				new InputStreamReader(is, "iso-8859-1"), 1000);
//			StringBuilder sb = new StringBuilder();
//			String line = null;
//			while ((line = reader.readLine()) != null) {
//				sb.append(line);
//			}
//			is.close();
//			
//			result = sb.toString();
//			
//		} catch (Exception e) {
//			Log.e("log_tag", "Error converting result " + e.toString());
//		}
//		
//		return result;
//	}
//	
//	protected void onProgressUpdate(Integer... progress) {
//	
////        setProgressPercent(progress[0]);
//	}
//	
//	
//	protected void onPostExecute(String result) {
//	
//		Button sync = (Button) context.findViewById(R.id.sync_button);
//		
////		Toast.makeText(context,
////			result, Toast.LENGTH_LONG).show();
//		
//		if (result.equalsIgnoreCase("Nonexistent")) {
//			
//			String msg;
//			if (project.equalsIgnoreCase("ERROR"))
//				msg = "Please enter a valid project";
//			else
//				msg = "Project " + project + " doesn't exist";
//			
//			Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
//			
//			sync.setVisibility(Button.INVISIBLE);
//			
//		} else if (result.equalsIgnoreCase("")) {
//			
//			Toast.makeText(context, "Error connecting to network", Toast.LENGTH_LONG).show();
//			sync.setVisibility(Button.INVISIBLE);
//			
//		} else if (!result.equalsIgnoreCase("failure")) {
////			context.loggedIn = true;
//			Toast.makeText(context,
//				"Project " + project + " loading....", Toast.LENGTH_LONG).show();
//			context.xmlFile = result;
//			sync.setVisibility(Button.VISIBLE);
//			
//		} else {
//			
//			Toast.makeText(context,
//				"Login unsuccessful. Try again", Toast.LENGTH_LONG).show();
//			sync.setVisibility(Button.INVISIBLE);
//		}
//	}
//}
