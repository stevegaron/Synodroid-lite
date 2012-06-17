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

import org.jared.synodroid.ds.server.SynoServer;
import org.jared.synodroid.ds.protocol.ResponseHandler;
import org.jared.synodroid.ds.adapter.TaskAdapter;

import org.jared.synodroid.ds.data.Task;

/**
 * Retrieve alternatively all task and one detail from the server
 * 
 * @author Eric Taix
 * 
 */
public class GetAllAndOneDetailTaskAction implements SynoAction {

	// GetAll action
	private GetAllTaskAction getAllAction;
	// Detail action
	private DetailTaskAction detailsAction;

	// The task adapter which contains tasks list
	private TaskAdapter taskAdapter;
	// Flag to know if we have to retrieve all tasks or one task detail
	private boolean allTask = true;
	// Index for the next task's detail to retrieve from
	private int taskDetailIndex = 0;
	// The strategy to retrieve the next task
	private NextTaskStrategy strategy = new LastUpdateStrategy();

	/**
	 * Default constructor
	 * 
	 * @param sortAttrP
	 * @param ascendingP
	 */
	public GetAllAndOneDetailTaskAction(String sortAttrP, boolean ascendingP, TaskAdapter adapterP) {
		taskAdapter = adapterP;
		getAllAction = new GetAllTaskAction(sortAttrP, ascendingP);
		detailsAction = new DetailTaskAction(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.action.TaskAction#execute(org.jared.synodroid.ds.common .protocol.ResponseHandler, org.jared.synodroid.ds.common.SynoServer)
	 */
	public void execute(ResponseHandler handlerP, SynoServer serverP) throws Exception {
		// If we have to retrieve all tasks OR if there's no task currently OR if user unchecked 'Show upload' checkbox
		if (allTask || taskAdapter.getCount() == 0 || !serverP.getConnection().showUpload || serverP.isInterrupted()) {
			getAllAction.execute(handlerP, serverP);
		}
		// Get a task's detail
		else {
			// If a task could be found
			Task task = getNextTask();
			if (task != null) {
				detailsAction.setTask(task);
				detailsAction.execute(handlerP, serverP);
				taskDetailIndex = taskAdapter.getTaskList().indexOf(task);
				// Set next task detail
				taskDetailIndex++;
				if (taskDetailIndex >= taskAdapter.getCount()) {
					taskDetailIndex = 0;
				}
			}
			// Otherwise get all tasks
			else {
				getAllAction.execute(handlerP, serverP);
			}
		}
		allTask = !allTask;
	}

	/**
	 * Get the next task
	 * 
	 * @return
	 */
	private Task getNextTask() {
		// Delegate to the strategy
		return strategy.getNextTask(taskAdapter.getTaskList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.action.TaskAction#getName()
	 */
	public String getName() {
		return "Alertnate between retrieving all tasks and one task's details";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.action.TaskAction#getTask()
	 */
	public Task getTask() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.action.TaskAction#getToastId()
	 */
	public int getToastId() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.action.TaskAction#isToastable()
	 */
	public boolean isToastable() {
		return false;
	}

}
