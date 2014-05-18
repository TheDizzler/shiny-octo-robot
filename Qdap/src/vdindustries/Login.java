package vdindustries;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import vdindustries.Qdap.R;
import vdindustries.networking.ConnectToServer;
import vdindustries.networking.LoadingScreen;
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
	
//		trythis();
		
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
		
		Intent intent = new Intent(this, LoadingScreen.class);
		intent.putExtra("test", false);
		intent.putExtra("xmlFile", xmlFile);
		intent.putExtra("projname", project.getText().toString());
		this.startActivity(intent);
		
	}
	
	
	public void testProjectSetup(View view) {
	
		Intent intent = new Intent(this, LoadingScreen.class);
		intent.putExtra("test", true);
		this.startActivity(intent);
		
	}
}
