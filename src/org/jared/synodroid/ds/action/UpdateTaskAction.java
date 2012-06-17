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

import java.util.List;

import org.jared.synodroid.ds.server.SynoServer;
import org.jared.synodroid.ds.protocol.ResponseHandler;
import org.jared.synodroid.ds.R;

import org.jared.synodroid.ds.data.Task;
import org.jared.synodroid.ds.data.TaskFile;

/**
 * Update a task (files and parameters)
 * 
 * @author Eric Taix (eric.taix at gmail.com)
 */
public class UpdateTaskAction implements SynoAction {

	// The task to update
	private Task task;
	// The file list to update
	private List<TaskFile> files;
	// The seeding ratio (in percentage)
	private int seedingRatio;
	// The seeing interval (in minutes)
	private int seedingInterval;

	/**
	 * Constructor
	 * 
	 * @param taskP
	 * @param filesP
	 * @param seedingRatioP
	 * @param seedingIntervalP
	 */
	public UpdateTaskAction(Task taskP, List<TaskFile> filesP, int seedingRatioP, int seedingIntervalP) {
		task = taskP;
		files = filesP;
		seedingRatio = seedingRatioP;
		seedingInterval = seedingIntervalP;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.common.action.SynoAction#execute(org.jared.synodroid.ds.common.protocol.ResponseHandler, org.jared.synodroid.ds.common.SynoServer)
	 */
	public void execute(ResponseHandler handlerP, SynoServer serverP) throws Exception {
		serverP.getDSMHandlerFactory().getDSHandler().updateTask(task, files, seedingRatio, seedingInterval);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.common.action.SynoAction#getName()
	 */
	public String getName() {
		return "Updating task " + task.taskId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.common.action.SynoAction#getTask()
	 */
	public Task getTask() {
		return task;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.common.action.SynoAction#getToastId()
	 */
	public int getToastId() {
		return R.string.action_updating;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.common.action.SynoAction#isToastable()
	 */
	public boolean isToastable() {
		return true;
	}

}
