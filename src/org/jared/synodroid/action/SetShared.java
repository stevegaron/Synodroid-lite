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
package org.jared.synodroid.action;

import org.jared.synodroid.server.SynoServer;
import org.jared.synodroid.protocol.ResponseHandler;
import org.jared.synodroid.R;

import org.jared.synodroid.data.Task;

/**
 * Set the shared directory
 * 
 * @author Eric Taix (eric.taix at gmail.com)
 */
public class SetShared implements SynoAction {

	// The shared directory to set
	private String sharedDirectory;
	// The task
	private Task task;

	/**
	 * Constructor
	 * 
	 * @param taskP
	 * @param filesP
	 * @param seedingRatioP
	 * @param seedingIntervalP
	 */
	public SetShared(Task taskP, String sharedDirP) {
		task = taskP;
		sharedDirectory = sharedDirP;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.common.action.SynoAction#execute(org.jared.synodroid.common.protocol.ResponseHandler, org.jared.synodroid.common.SynoServer)
	 */
	public void execute(ResponseHandler handlerP, SynoServer serverP) throws Exception {
		serverP.getDSMHandlerFactory().getDSHandler().setSharedDirectory(task, sharedDirectory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.common.action.SynoAction#getName()
	 */
	public String getName() {
		return "Setting shared directory";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.common.action.SynoAction#getTask()
	 */
	public Task getTask() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.common.action.SynoAction#getToastId()
	 */
	public int getToastId() {
		return R.string.action_set_shared;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.common.action.SynoAction#isToastable()
	 */
	public boolean isToastable() {
		return true;
	}

}
