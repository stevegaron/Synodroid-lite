/**
 * Copyright 2010 Eric Taix
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 * 
 */
package org.jared.synodroid.ds.action;

import org.jared.synodroid.ds.server.DownloadIntentService;
import org.jared.synodroid.ds.server.SynoServer;
import org.jared.synodroid.ds.server.UploadIntentService;
import org.jared.synodroid.ds.protocol.ResponseHandler;
import org.jared.synodroid.ds.R;
import org.jared.synodroid.ds.Synodroid;

import org.jared.synodroid.ds.data.DSMVersion;
import org.jared.synodroid.ds.data.Task;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;

/**
 * This action upload a file
 * 
 * @author Eric Taix (eric.taix at gmail.com)
 */
public class AddPwTaskAction implements SynoAction {

	private Uri uri;
	private Task task;
	private boolean use_safe;
	private boolean toast = true;
	private String uname;
	private String pass;
	
	/**
	 * Constructor to upload a file defined by an Uri
	 * 
	 * @param uriP
	 * @param outside_url
	 */
	public AddPwTaskAction(Uri uriP, String username, String password, boolean outside_url, boolean use_safeP) {
		uri = uriP;
		task = new Task();
		task.fileName = uri.getLastPathSegment();
		if (task.fileName == null){
			task.fileName = uri.toString();
		}
		task.outside_url = outside_url;
		use_safe = use_safeP;
		uname = username;
		pass = password;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.ds.action.TaskAction#execute(org.jared.synodroid.ds.ds .DownloadActivity, org.jared.synodroid.ds.common.SynoServer)
	 */
	public void execute(ResponseHandler handlerP, SynoServer serverP) throws Exception {
		if (use_safe && serverP.getDsmVersion().smallerThen(DSMVersion.VERSION3_1)){
			//new TorrentDownloadAndAdd((Fragment)handlerP).execute(uri.toString());
			Activity a = ((Fragment)handlerP).getActivity();
			Intent msgIntent = new Intent(a, DownloadIntentService.class);
			msgIntent.putExtra(DownloadIntentService.URL, uri.toString());
			msgIntent.putExtra(DownloadIntentService.DEBUG, ((Synodroid)a.getApplication()).DEBUG);
			a.startService(msgIntent);
		}
		else{
			if (task.outside_url) {
				// Start task using url instead of reading file
				serverP.getDSMHandlerFactory().getDSHandler().uploadUrl(uri, uname, pass);
			} else {
				Activity a = ((Fragment)handlerP).getActivity();
				Intent msgIntent = new Intent(a, UploadIntentService.class);
				msgIntent.putExtra(UploadIntentService.URL, uri.toString());
				if (serverP.getDsmVersion().smallerThen(DSMVersion.VERSION3_1)){
					msgIntent.putExtra(UploadIntentService.DIRECTORY, "");
				}
				else{
					msgIntent.putExtra(UploadIntentService.DIRECTORY, serverP.getDSMHandlerFactory().getDSHandler().getSharedDirectory(false));
				}
				msgIntent.putExtra(UploadIntentService.COOKIES, serverP.getCookies());
				msgIntent.putExtra(UploadIntentService.DSM_VERSION, serverP.getDsmVersion().getTitle());
				msgIntent.putExtra(UploadIntentService.PATH, serverP.getUrl());
				msgIntent.putExtra(UploadIntentService.DEBUG, ((Synodroid)a.getApplication()).DEBUG);
				a.startService(msgIntent);
			}
		}
	}
	
	public void checkToast(SynoServer serverP){
		if (use_safe && serverP.getDsmVersion().smallerThen(DSMVersion.VERSION3_1)){
			toast = false;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.ds.action.TaskAction#getName()
	 */
	public String getName() {
		return "Adding file " + uri;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.ds.action.TaskAction#getTask()
	 */
	public Task getTask() {
		return task;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.ds.action.TaskAction#getToastId()
	 */
	public int getToastId() {
		return R.string.action_adding;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.ds.action.TaskAction#isToastable()
	 */
	public boolean isToastable() {
		return toast;
	}

	public String getUriString(){
		return uri.toString();
	}
}
