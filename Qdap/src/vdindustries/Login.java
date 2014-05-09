package vdindustries;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import vdindustries.masterflow.R;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class Login extends ActionBarActivity {
	
	EditText				user, password, project;
	
	public static Boolean	loggedIn	= false;
	
	@Override protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		user = (EditText) findViewById(R.id.editUser);
		password = (EditText) findViewById(R.id.editPassword);
		project = (EditText) findViewById(R.id.editProject);
		
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
	
	//Login to Categories
	public void login(View view) {
	
//		new DownloadFromServer(this, user.getText().toString(), password.getText().toString(), project.getText().toString()).execute("http://qdap.ca/process_android_login.php");
		
			Intent intent = new Intent(this, Categories.class);
			startActivity(intent);
	}
	
}

/** Required to download from server. */
class DownloadFromServer extends AsyncTask<String, Integer, String> {
	
	private String				user, password, project;
	private List<NameValuePair>	nvp;
	private InputStream			is;
	private Context	context;
	
	private static final String	loginSuccessful	= "Login successful";
	
	
	public DownloadFromServer(Context login, String user, String pw, String proj) {
	
		this.user = user;
		this.password = pw;
		this.project = proj;
		this.context = login;
	}
	
	
	@Override protected String doInBackground(String... params) {
	
		nvp = new ArrayList<NameValuePair>();
		//Pass user message with Index Value "UserMessage"
//		nvp.add(new BasicNameValuePair("UserMessage", "Is that you server? It's me, Android"));
		nvp.add(new BasicNameValuePair(user, password));
		
		try {
			HttpClient httpClient = new DefaultHttpClient();
			
			HttpPost httpPost = new HttpPost(params[0]);
			
			// Pass nameValuePairs to server
			httpPost.setEntity(new UrlEncodedFormEntity(nvp));
			
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			Log.d("<<<-http_log_tag->>>", "Succes in Http Connection");
			
		} catch (Exception e) {
			Log.e("<<<-http_log_tag->>>", "Error in Http Connection" + e.toString());
		}
		
		String result = "";
		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(
				new InputStreamReader(is, "iso-8859-1"), 30);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			
			result = sb.toString();
			
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}
		
		return result;
	}
	
	protected void onProgressUpdate(Integer... progress) {
	
//        setProgressPercent(progress[0]);
	}
	
	protected void onPostExecute(String result) {
	
		if (result.equalsIgnoreCase(loginSuccessful)) {
			Login.loggedIn = true;
			Intent intent = new Intent(context, Categories.class);
			context.startActivity(intent);
		}
		
	}
	
	
	
}