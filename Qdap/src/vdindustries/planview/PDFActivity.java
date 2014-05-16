package vdindustries.planview;

import vdindustries.Categories.PlaceholderFragment;
import vdindustries.masterflow.R;
import vdindustries.content.DeficiencyParser;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class PDFActivity extends ActionBarActivity {
	
	/*
	@Override protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pdf);
		
		//Settings
	    PDFImage.sShowImages = true; // show images
	    PDFPaint.s_doAntiAlias = true; // make text smooth
	    HardReference.sKeepCaches = true; // save images in cache

	    //Setup webview
	    wv = (WebView)findViewById(R.id.webView1);
	    wv.getSettings().setBuiltInZoomControls(true);//show zoom buttons
	    wv.getSettings().setSupportZoom(true);//allow zoom
	    //get the width of the webview
	    wv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
	    {
	        @Override
	        public void onGlobalLayout()
	        {
	            ViewSize = wv.getWidth();
	            wv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
	        }
	    });

	    pdfLoadImages();//load images
	}

	//Load Images:
	private void pdfLoadImages()
	{
	    try
	    {
	        // run async
	        new AsyncTask<Void, Void, Void>()
	                {
	                    // create and show a progress dialog
	                    ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, "", "Opening...");

	                    @Override
	                    protected void onPostExecute(Void result)
	                    {
	                        //after async close progress dialog
	                        progressDialog.dismiss();
	                    }

	                    @Override
	                    protected Void doInBackground(Void... params)
	                    {
	                        try
	                        {
	                            // select a document and get bytes
	                            File file = new File(Environment.getExternalStorageDirectory().getPath()+"/randompdf.pdf");
	                            RandomAccessFile raf = new RandomAccessFile(file, "r");
	                            FileChannel channel = raf.getChannel();
	                            ByteBuffer bb = ByteBuffer.NEW(channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size()));
	                            raf.close();
	                            // create a pdf doc
	                            PDFFile pdf = new PDFFile(bb);
	                            //Get the first page from the pdf doc
	                            PDFPage PDFpage = pdf.getPage(1, true);
	                            //create a scaling value according to the WebView Width
	                            final float scale = ViewSize / PDFpage.getWidth() * 0.95f;
	                            //convert the page into a bitmap with a scaling value
	                            Bitmap page = PDFpage.getImage((int)(PDFpage.getWidth() * scale), (int)(PDFpage.getHeight() * scale), null, true, true);
	                            //save the bitmap to a byte array
	                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
	                            page.compress(Bitmap.CompressFormat.PNG, 100, stream);
	                            stream.close();
	                            byte[] byteArray = stream.toByteArray();
	                            //convert the byte array to a base64 string
	                            String base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
	                            //create the html + add the first image to the html
	                            String html = "<!DOCTYPE html><html><body bgcolor=\"#7f7f7f\"><img src=\"data:image/png;base64,"+base64+"\" hspace=10 vspace=10><br>";
	                            //loop through the rest of the pages and repeat the above
	                            for(int i = 2; i <= pdf.getNumPages(); i++)
	                            {
	                                PDFpage = pdf.getPage(i, true);
	                                page = PDFpage.getImage((int)(PDFpage.getWidth() * scale), (int)(PDFpage.getHeight() * scale), null, true, true);
	                                stream = new ByteArrayOutputStream();
	                                page.compress(Bitmap.CompressFormat.PNG, 100, stream);
	                                stream.close();
	                                byteArray = stream.toByteArray();
	                                base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
	                                html += "<img src=\"data:image/png;base64,"+base64+"\" hspace=10 vspace=10><br>";
	                            }
	                            html += "</body></html>";
	                            //load the html in the webview
	                            wv.loadDataWithBaseURL("", html, "text/html","UTF-8", "");
	                        }
	                        catch (Exception e)
	                        {
	                            Log.d("error", e.toString());
	                        }
	                            return null;
	                        }
	                }.execute();
	                System.gc();// run GC
	    }
	    catch (Exception e)
	    {
	        Log.d("error", e.toString());
	    }
	}
		
	}
	*/
}