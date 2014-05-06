package vdindustries.networking;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import vdindustries.masterflow.R;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class ConnectActivity extends ActionBarActivity {
	
	Button							connect;
	TextView						status;
	static InputStream				is;
	static ArrayList<NameValuePair>	nvp;
	
	
	
	public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect);
		
		connect = (Button) findViewById(R.id.connBtn);
		status = (TextView) findViewById(R.id.tvStatus);
		
		connect.setOnClickListener(new OnClickListener() {
			
			@Override public void onClick(View v) {
			
				new DownloadFromServer(nvp, is, status).execute("http://qdap.ca/connectionTest.php");
			}
		});
	}
	
	public void onActivityResult(int requestcode, int resultCode, Intent data) {
	
		if (resultCode == RESULT_OK) {
			status.setText("connected!....?");
		}
	}
}

/** Required to download from server. */
class DownloadFromServer extends AsyncTask<String, Integer, String> {
	
	private InputStream	is;
	private ArrayList<NameValuePair>	nvp;
	private TextView	status;

	
	public DownloadFromServer(ArrayList<NameValuePair> nvp, InputStream is, TextView status) {
	
		this.nvp = nvp;
		this.is = is;
		this.status = status;
	}

	
	@Override protected String doInBackground(String... params) {
	
		nvp = new ArrayList<NameValuePair>();
		//Pass user message with Index Value "UserMessage"
		nvp.add(new BasicNameValuePair("UserMessage", "Is that you server? It's me, Android"));
		
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
//        showDialog("Downloaded " + result + " bytes");
        status.setText(result);
     
    }

	
	
}