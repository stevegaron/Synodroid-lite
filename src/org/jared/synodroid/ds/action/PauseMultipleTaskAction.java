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

/**
 * Stop a torrent from downloading
 * 
 * @author Eric Taix (eric.taix at gmail dot com)
 */
public class PauseMultipleTaskAction implements SynoAction {

	// The torrent to stop
	private List<Task> tasks;
	private String taskids;

	public PauseMultipleTaskAction(List<Task> tasksP) {
		tasks = tasksP;
		taskids = "";
		for (Task task : tasks) {
			if (!taskids.equals("")){
				taskids += ":";
			}
			taskids += ""+task.taskId;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.common.SynoAction#execute(org.jared.synodroid.ds.TorrentListActivity, org.jared.synodroid.ds.common.SynoServer)
	 */
	public void execute(ResponseHandler handlerP, SynoServer serverP) throws Exception {
		serverP.getDSMHandlerFactory().getDSHandler().stop(taskids);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.common.SynoAction#getName()
	 */
	public String getName() {
		return "Stop task " + taskids;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.common.SynoAction#getToastId()
	 */
	public int getToastId() {
		return R.string.action_pausing_multiple;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.common.SynoAction#isToastable()
	 */
	public boolean isToastable() {
		return true;
	}

	/**
	 * @return the task
	 */
	public Task getTask() {
		return null;
	}

}