/**
 * 
 */
package org.jared.synodroid.action;

import org.jared.synodroid.server.SynoServer;
import org.jared.synodroid.action.SynoAction;
import org.jared.synodroid.data.Task;
import org.jared.synodroid.protocol.ResponseHandler;
import org.jared.synodroid.ui.DownloadFragment;
import org.jared.synodroid.R;

/**
 * This action just show the details activity
 * 
 * @author Eric Taix
 */
public class ShowDetailsAction implements SynoAction {

	// The task to resume
	private Task task;

	public ShowDetailsAction(Task taskP) {
		task = taskP;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.common.SynoAction#execute(org.jared.synodroid. ds.DownloadActivity, org.jared.synodroid.common.SynoServer)
	 */
	public void execute(ResponseHandler handlerP, SynoServer serverP) throws Exception {
		serverP.fireMessage(handlerP, DownloadFragment.MSG_SHOW_DETAILS, task);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.common.SynoAction#getName()
	 */
	public String getName() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.common.SynoAction#getToastId()
	 */
	public int getToastId() {
		return R.string.action_detailing;
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
