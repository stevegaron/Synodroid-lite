package org.jared.synodroid.ds.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.jared.synodroid.ds.Synodroid;
import org.jared.synodroid.ds.action.AddTaskAction;
import org.jared.synodroid.ds.protocol.ResponseHandler;
import org.jared.synodroid.ds.ui.DownloadFragment;
import org.jared.synodroid.ds.ui.SearchFragment;

public class TorrentDownloadAndAdd extends AsyncTask<String, Void, Uri> {
	private Fragment currentFragment = null;
	private Activity a;
	
	public TorrentDownloadAndAdd (Fragment fragment){
		currentFragment = fragment;
		a = currentFragment.getActivity();
	}
	
	@Override
	protected void onPreExecute() {
		Message msg = new Message();
		msg.what = ResponseHandler.MSG_TASK_DL_WAIT;
		((ResponseHandler)currentFragment).handleReponse(msg);
	}

	@Override
	protected Uri doInBackground(String... params) {
		try {
			Uri uri = Uri.parse(params[0]);
			return fixUri(uri);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	protected void onPostExecute(Uri uri) {
		boolean out_url = false;
		if (uri != null) {
			if (!uri.toString().startsWith("file:")) {
				out_url = true;
			}
			AddTaskAction addTask = new AddTaskAction(uri, out_url, false);
			Synodroid app = (Synodroid) a.getApplication();
			if (currentFragment instanceof SearchFragment){
				app.executeAction((SearchFragment)currentFragment, addTask, true);
			}
			else if (currentFragment instanceof DownloadFragment){
				app.executeAction((DownloadFragment)currentFragment, addTask, true);
			}
		}
	}
	

	private Uri fixUri(Uri uri) {
		try {
			URL url = new URL(uri.toString()); // you can write here any link
			File path = Environment.getExternalStorageDirectory();
			path = new File(path, "Android/data/org.jared.synodroid.ds/cache/");
			path.mkdirs();
			String temp[] = uri.toString().split("/");
			String fname = temp[(temp.length) - 1];
			if (!fname.toLowerCase().endsWith(".torrent") && !fname.toLowerCase().endsWith(".nzb")) {
				fname += ".torrent";
			}
			File file = new File(path, fname);

			long startTime = System.currentTimeMillis();
			try{
				if (((Synodroid)a.getApplication()).DEBUG) Log.v(Synodroid.DS_TAG, "Downloading " + uri.toString() + " to temp folder...");
			}catch (Exception ex){/*DO NOTHING*/}
			try{
				if (((Synodroid)a.getApplication()).DEBUG) Log.v(Synodroid.DS_TAG, "Temp file destination: " + file.getAbsolutePath());
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
			
			BufferedInputStream bis = new BufferedInputStream(is);

			/*
			 * Read bytes to the Buffer until there is nothing more to read(-1).
			 */
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			/* Convert the Bytes read to a String. */
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baf.toByteArray());
			fos.close();
			try{
				if (((Synodroid)a.getApplication()).DEBUG) Log.v(Synodroid.DS_TAG, "Download completed. Elapsed time: " + ((System.currentTimeMillis() - startTime) / 1000) + " sec(s)");
			}catch (Exception ex){/*DO NOTHING*/}
			uri = Uri.fromFile(file);
		} catch (Exception e) {
			try{
				if (((Synodroid)a.getApplication()).DEBUG) Log.e(Synodroid.DS_TAG, "Download Error.", e);
			}catch (Exception ex){/*DO NOTHING*/}
			try{
				if (((Synodroid)a.getApplication()).DEBUG) Log.i(Synodroid.DS_TAG, "Letting the NAS do the heavy lifting...");
			}catch (Exception ex){/*DO NOTHING*/}
		}
		return uri;
	}
}

