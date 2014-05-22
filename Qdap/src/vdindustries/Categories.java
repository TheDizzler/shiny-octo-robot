package vdindustries;

import java.io.InputStream;

import vdindustries.Qdap.R;
import vdindustries.checklists.CheckListsActivity;
import vdindustries.content.DeficiencyParser;
import vdindustries.reportsview.TradeListActivity;
import android.content.Intent;
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

	public static DeficiencyParser parser;
	public static InputStream projectXML;

	static View rootView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_categories);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.categories, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

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

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

			rootView = inflater.inflate(R.layout.fragment_categories, container, false);

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

	/** Temp for testing image parsing */
	public void notes(View view) {

		Toast.makeText(this, "This feature is not yet implemented", Toast.LENGTH_SHORT).show();
	}

	/** This has got to change */
	// public static void defParse() {
	//
	// if (DeficiencyParser.fromAssets)
	// parser = new DeficiencyParser(assetMan, true);
	// else {
	// Document xmlDoc = DeficiencyParser.xmlDoc;
	// String projName = DeficiencyParser.projectName;
	// parser = new DeficiencyParser(xmlDoc, projName, assetMan);
	// }
	// }

}
