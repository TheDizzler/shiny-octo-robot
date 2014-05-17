package vdindustries;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import vdindustries.Qdap.R;
import vdindustries.checklists.CheckListsActivity;
import vdindustries.content.DeficiencyParser;
import vdindustries.networking.ConnectActivity;
import vdindustries.reportsview.TradeListActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Categories extends ActionBarActivity {
	
	
	public static DeficiencyParser	parser;
	public static InputStream		projectXML;
//	private boolean					fromAssets;
	
	private static AssetManager	assMan;
	
	@Override protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_categories);
		
		assMan = getAssets();
		Intent intent = getIntent();
		
		
		
		if (intent.getBooleanExtra("test", true)) {
			Toast.makeText(this, "Test Project loaded", Toast.LENGTH_LONG).show();
//			fromAssets = false;
			parser = new DeficiencyParser(assMan, true);
			
		} else {
			try {
				
				
				String xml = intent.getStringExtra("xmlFile");
				String projname = intent.getStringExtra("projname");
//			Toast.makeText(this, xml, Toast.LENGTH_LONG).show();
				
				DocumentBuilder db = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
				
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(xml));
				Document xmlDoc = db.parse(is);
				
				parser = new DeficiencyParser(xmlDoc, projname, assMan);
//				fromAssets = false;
			} catch (ParserConfigurationException | SAXException | IOException | NullPointerException e) {
				
				Toast.makeText(this,
					"Error loading project" + "\n\nLoading default Test Project instead",
					Toast.LENGTH_LONG).show();
//				fromAssets = true;
				parser = new DeficiencyParser(assMan, false);
			}
		}
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
				.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
	}
	
	
	@Override public boolean onCreateOptionsMenu(Menu menu) {
	
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.categories, menu);
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
		
			View rootView = inflater.inflate(R.layout.fragment_categories,
				container, false);
			
			TextView projName = (TextView) rootView.findViewById(R.id.project_name_textview);
			projName.setText(DeficiencyParser.projectName);
			
			return rootView;
		}
	}
	
	public void reports(View view) {
	
		Intent intent = new Intent(this, TradeListActivity.class);
		startActivity(intent);
	}
	
	public void checklists(View view) {
	
		Intent intent = new Intent(this, CheckListsActivity.class);
		startActivity(intent);
	}
	
	/** temp for testing connection */
	public void photo(View view) {
	
		Toast.makeText(this, "This feature is not yet implemented", Toast.LENGTH_SHORT).show();
	}
	
	/** Temp for testing image parsing*/
	public void notes(View view) {
	
		Toast.makeText(this, "This feature is not yet implemented", Toast.LENGTH_SHORT).show();
	}
	
	
	public static void defParse() {
	
		parser = new DeficiencyParser(assMan, true);
	}
	
}
