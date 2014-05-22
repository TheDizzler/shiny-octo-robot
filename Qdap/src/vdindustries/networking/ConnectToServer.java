package vdindustries.networking;

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

import vdindustries.Login;
import vdindustries.Qdap.R;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

/** Required to connect and download from server. */
public class ConnectToServer extends AsyncTask<String, Integer, String> {

	private String user, password, project;
	private List<NameValuePair> nvp;
	private InputStream is;
	private Login context;

	public ConnectToServer(Login login, String user, String pw, String proj) {

		this.user = user;
		this.password = pw;
		this.project = proj;
		this.context = login;
		if (project.length() == 0)
			project = "ERROR";
	}

	@Override
	protected String doInBackground(String... params) {

		nvp = new ArrayList<NameValuePair>();
		// Pass user message with Index Value "UserMessage"
		// nvp.add(new BasicNameValuePair("UserMessage",
		// "Is that you server? It's me, Android"));
		nvp.add(new BasicNameValuePair("username", user));
		nvp.add(new BasicNameValuePair("password", password));
		nvp.add(new BasicNameValuePair("project", project));

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
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 100000);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			is.close();

			result = sb.toString();

		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		return result;
	}

	protected void onProgressUpdate(Integer... progress) {

		// setProgressPercent(progress[0]);
	}

	protected void onPostExecute(String result) {

		Button sync = (Button) context.findViewById(R.id.sync_button);

		// Toast.makeText(context,
		// result, Toast.LENGTH_LONG).show();

		if (result.equalsIgnoreCase("Nonexistent")) {

			String msg;
			if (project.equalsIgnoreCase("ERROR"))
				msg = "Please enter a valid project";
			else
				msg = "Project " + project + " doesn't exist";

			Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

			sync.setVisibility(Button.INVISIBLE);

		} else if (result.equalsIgnoreCase("")) {

			Toast.makeText(context, "Error connecting to network", Toast.LENGTH_LONG).show();
			sync.setVisibility(Button.INVISIBLE);

		} else if (!result.equalsIgnoreCase("failure")) {
			// context.loggedIn = true;
			Toast.makeText(context, "Project " + project + " loading....", Toast.LENGTH_LONG).show();
			context.xmlFile = result;
			sync.setVisibility(Button.VISIBLE);

		} else {

			Toast.makeText(context, "Login unsuccessful. Try again", Toast.LENGTH_LONG).show();
			sync.setVisibility(Button.INVISIBLE);
		}
	}
}