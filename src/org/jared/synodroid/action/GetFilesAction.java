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

import java.util.List;

import org.jared.synodroid.server.SynoServer;
import org.jared.synodroid.ui.DownloadFragment;
import org.jared.synodroid.protocol.ResponseHandler;
import org.jared.synodroid.R;

import org.jared.synodroid.data.Task;
import org.jared.synodroid.data.TaskFile;
import org.jared.synodroid.data.TaskFilesContainer;

/**
 * Retrieve task's files
 * 
 * @author Eric Taix (eric.taix at gmail dot com)
 */
public class GetFilesAction implements SynoAction {

	// The torrent to resume
	private Task task;
	private int LIMIT_PAR_REQUEST = 25;

	public GetFilesAction(Task taskP) {
		task = taskP;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.common.SynoAction#execute(org.jared.synodroid.ds.TorrentListActivity, org.jared.synodroid.common.SynoServer)
	 */
	public void execute(ResponseHandler handlerP, SynoServer serverP) throws Exception {
		int start = 0;
		TaskFilesContainer container = serverP.getDSMHandlerFactory().getDSHandler().getFiles(task, start, LIMIT_PAR_REQUEST);
		int total = container.getTotalFiles();
		if (total > LIMIT_PAR_REQUEST) {
			int nbLoop = (total - 1) / 25;
			for (int iLoop = 0; iLoop < nbLoop; iLoop++) {
				start += LIMIT_PAR_REQUEST;
				// Retrieve other taks part
				TaskFilesContainer secondaryContainer = serverP.getDSMHandlerFactory().getDSHandler().getFiles(task, start, LIMIT_PAR_REQUEST);
				List<TaskFile> tasks = secondaryContainer.getTasks();
				// Add them to the main container
				container.getTasks().addAll(tasks);
			}
		}
		serverP.fireMessage(handlerP, DownloadFragment.MSG_DETAILS_FILES_RETRIEVED, container.getTasks());
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.common.SynoAction#getName()
	 */
	public String getName() {
		return "Get task's files " + task.taskId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.common.SynoAction#getToastId()
	 */
	public int getToastId() {
		return R.string.action_files;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.common.SynoAction#isToastable()
	 */
	public boolean isToastable() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.action.TaskAction#getTask()
	 */
	public Task getTask() {
		return task;
	}

}
