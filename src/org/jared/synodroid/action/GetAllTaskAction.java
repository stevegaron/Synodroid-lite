/**
 * 
 */
package org.jared.synodroid.action;

import java.util.List;

import org.jared.synodroid.server.SynoServer;
import org.jared.synodroid.protocol.ResponseHandler;
import org.jared.synodroid.ui.DownloadFragment;

import org.jared.synodroid.data.Task;
import org.jared.synodroid.data.TaskContainer;

/**
 * Retrieve all task from the servers
 * 
 * @author Eric Taix
 * 
 */
public class GetAllTaskAction implements SynoAction {

	// The number of tasks to return by request (this is a hard coded limit due to the synology server)
	private final int LIMIT_PAR_REQUEST = 25;

	// The name of the sorted attribut
	private String sortAttr;
	// Is the sort ascending ?
	private boolean ascending = true;

	/**
	 * Default constructor
	 * 
	 * @param sortAttrP
	 * @param ascendingP
	 */
	public GetAllTaskAction(String sortAttrP, boolean ascendingP) {
		sortAttr = sortAttrP;
		ascending = ascendingP;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.action.TaskAction#execute(org.jared.synodroid.common.protocol.ResponseHandler, org.jared.synodroid.common.SynoServer)
	 */
	public void execute(ResponseHandler handlerP, SynoServer serverP) throws Exception {
		int start = 0;
		TaskContainer container = serverP.getDSMHandlerFactory().getDSHandler().getAllTask(start, LIMIT_PAR_REQUEST, sortAttr, ascending);
		int total = container.getTotalTasks();
		if (total > LIMIT_PAR_REQUEST) {
			int nbLoop = (total - 1) / 25;
			for (int iLoop = 0; iLoop < nbLoop; iLoop++) {
				start += LIMIT_PAR_REQUEST;
				// Retrieve other taks part
				TaskContainer secondaryContainer = serverP.getDSMHandlerFactory().getDSHandler().getAllTask(start, LIMIT_PAR_REQUEST, sortAttr, ascending);
				List<Task> tasks = secondaryContainer.getTasks();
				// Add them to the main container
				container.getTasks().addAll(tasks);
			}
		}
		serverP.fireMessage(handlerP, DownloadFragment.MSG_TASKS_UPDATED, container);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.action.TaskAction#getName()
	 */
	public String getName() {
		return "Retrieving all tasks";
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
