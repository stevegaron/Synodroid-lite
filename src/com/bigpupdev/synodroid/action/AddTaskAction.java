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
package com.bigpupdev.synodroid.action;

import com.bigpupdev.synodroid.server.SynoServer;
import com.bigpupdev.synodroid.protocol.ResponseHandler;
import com.bigpupdev.synodroid.R;

import com.bigpupdev.synodroid.data.Task;

import android.net.Uri;

/**
 * This action upload a file
 * 
 * @author Eric Taix (eric.taix at gmail.com)
 */
public class AddTaskAction implements SynoAction {

	private Uri uri;
	private Task task;

	/**
	 * Constructor to upload a file defined by an Uri
	 * 
	 * @param uriP
	 * @param outside_url
	 */
	public AddTaskAction(Uri uriP, boolean outside_url) {
		uri = uriP;
		task = new Task();
		task.fileName = uri.getLastPathSegment();
		task.outside_url = outside_url;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bigpupdev.synodroid.ds.action.TaskAction#execute(com.bigpupdev.synodroid.ds .DownloadActivity, com.bigpupdev.synodroid.common.SynoServer)
	 */
	public void execute(ResponseHandler handlerP, SynoServer serverP) throws Exception {
		if (task.outside_url) {
			// Start task using url instead of reading file
			serverP.getDSMHandlerFactory().getDSHandler().uploadUrl(uri);
		} else {
			serverP.getDSMHandlerFactory().getDSHandler().upload(uri);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bigpupdev.synodroid.ds.action.TaskAction#getName()
	 */
	public String getName() {
		return "Adding file " + uri;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bigpupdev.synodroid.ds.action.TaskAction#getTask()
	 */
	public Task getTask() {
		return task;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bigpupdev.synodroid.ds.action.TaskAction#getToastId()
	 */
	public int getToastId() {
		return R.string.action_adding;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bigpupdev.synodroid.ds.action.TaskAction#isToastable()
	 */
	public boolean isToastable() {
		return true;
	}

}
