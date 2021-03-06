package org.jared.synodroid.ds.server;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jared.synodroid.ds.R;
import org.jared.synodroid.ds.Synodroid;
import org.jared.synodroid.ds.ui.HomeActivity;
import org.jared.synodroid.ds.utils.ServiceHelper;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class DownloadIntentService extends IntentService{
	public static String URL = "URL";
	public static String DEBUG = "DEBUG";
	private int DL_ID = 42;
	
	int progress = 0;

	/** 
	 * A constructor is required, and must call the super IntentService(String)
	 * constructor with a name for the worker thread.
	 */
	public DownloadIntentService() {
		super("DownloadIntentService");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * The IntentService calls this method from the default worker thread with
	 * the intent that started the service. When this method returns, IntentService
	 * stops the service, as appropriate.
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		String uri = intent.getStringExtra(URL);
		boolean dbg = intent.getBooleanExtra(DEBUG, false);
		
		String temp[] = uri.split("/");
		String fname = temp[(temp.length) - 1];
		Notification notification = ServiceHelper.getNotificationProgress(this, fname, progress, DL_ID, R.drawable.dl_download);
		
		try {
			URL url = new URL(uri); // you can write here any link
			File path = Environment.getExternalStorageDirectory();
			path = new File(path, "Android/data/org.jared.synodroid.ds/cache/");
			path.mkdirs();
			if (!fname.toLowerCase().endsWith(".torrent") && !fname.toLowerCase().endsWith(".nzb")) {
				fname += ".torrent";
			}
			File file = new File(path, fname);

			long startTime = System.currentTimeMillis();
			try{
				if (dbg) Log.v(Synodroid.DS_TAG, "DownloadIntentService: Downloading " + uri + " to temp folder...");
			}catch (Exception ex){/*DO NOTHING*/}
			try{
				if (dbg) Log.v(Synodroid.DS_TAG, "DownloadIntentService: Temp file destination: " + file.getAbsolutePath());
			}catch (Exception ex){/*DO NOTHING*/}
			/* Open a connection to that URL. */
			HttpURLConnection ucon = (HttpURLConnection) url.openConnection();

			/*
			 * Define InputStreams to read from the URLConnection.
			 */
			InputStream is = ucon.getInputStream();
			
			while (ucon.getResponseCode() == 302){
				ucon = (HttpURLConnection) ucon.getURL().openConnection();
				is = ucon.getInputStream();
			}
			int contentLength = ucon.getContentLength();
			
			/* Convert the Bytes read to a String. */
			FileOutputStream fos = new FileOutputStream(file);
			
			BufferedInputStream bis = new BufferedInputStream(is);
			
	        byte[] buf = new byte[1024];
	        int count = 0;
	        int downloadedSize = 0;
	        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
	        long lastUpdate = 0;
			while ((count = bis.read(buf)) != -1){
	        	out.write(buf, 0, count);
	        	downloadedSize += count;
	        	progress = (int) (((float) downloadedSize/ ((float )contentLength)) * 100);
	        	if (((lastUpdate + 250) < System.currentTimeMillis()) || downloadedSize == contentLength){
	        		ServiceHelper.updateProgress(this, notification, progress, DL_ID);
	        	}
	        }
	        
	        fos.write(out.toByteArray());
	        fos.flush();
			fos.close();
			
			try{
				if (dbg) Log.v(Synodroid.DS_TAG, "DownloadIntentService: Download completed. Elapsed time: " + ((System.currentTimeMillis() - startTime) / 1000) + " sec(s)");
			}catch (Exception ex){/*DO NOTHING*/}
			uri = Uri.fromFile(file).toString();
		} catch (Exception e) {
			try{
				if (dbg) Log.e(Synodroid.DS_TAG, "DownloadIntentService: Download Error.", e);
			}catch (Exception ex){/*DO NOTHING*/}
		} finally{
			ServiceHelper.cancelNotification(this, DL_ID);
		}

		Intent broadcastIntent = new Intent();
		if (uri.startsWith("file")){
			broadcastIntent.setAction(Intent.ACTION_VIEW);
			broadcastIntent.setData(Uri.parse(uri));
		}
		else{
			broadcastIntent.setAction(Intent.ACTION_SEND);
			broadcastIntent.putExtra(Intent.EXTRA_TEXT, uri);
		}
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		broadcastIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		broadcastIntent.setClass(this, HomeActivity.class);
		getApplication().startActivity(broadcastIntent);
	}
}
