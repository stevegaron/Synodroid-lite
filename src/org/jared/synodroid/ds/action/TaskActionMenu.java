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

import java.util.ArrayList;
import java.util.List;

import org.jared.synodroid.ds.action.DeleteTaskAction;
import org.jared.synodroid.ds.action.PauseTaskAction;
import org.jared.synodroid.ds.action.ResumeTaskAction;
import org.jared.synodroid.ds.action.SynoAction;
import org.jared.synodroid.ds.data.Task;
import org.jared.synodroid.ds.R;

import android.content.Context;

/**
 * An utility class which declare all available actions for a task
 * 
 * @author eric.taix at gmail.com
 */
public class TaskActionMenu {

	// The task associated to this action
	private Task task;
	// The text to display
	private String title;
	// The action to execute if this TaskAction is selected
	private SynoAction action;
	// Flag to know if this action is eanbled
	private boolean enabled;

	/**
	 * Generate a list of actions according to a task's state
	 * 
	 * @param taskP
	 * @return
	 */
	public static List<TaskActionMenu> createActions(Context ctxP, Task taskP) {
		ArrayList<TaskActionMenu> result = new ArrayList<TaskActionMenu>();
		switch (taskP.getStatus()) {
		case TASK_DOWNLOADING:
			result.add(new TaskActionMenu(taskP, ctxP.getString(R.string.action_pause), new PauseTaskAction(taskP), true));
			result.add(new TaskActionMenu(taskP, ctxP.getString(R.string.action_delete), new DeleteTaskAction(taskP), true));
			break;
		case TASK_PRE_SEEDING:
		case TASK_SEEDING:
			result.add(new TaskActionMenu(taskP, ctxP.getString(R.string.action_pause), new PauseTaskAction(taskP), true));
			result.add(new TaskActionMenu(taskP, ctxP.getString(R.string.action_cancel), new DeleteTaskAction(taskP), true));
			break;
		case TASK_PAUSED:
			result.add(new TaskActionMenu(taskP, ctxP.getString(R.string.action_resume), new ResumeTaskAction(taskP), true));
			result.add(new TaskActionMenu(taskP, ctxP.getString(R.string.action_delete), new DeleteTaskAction(taskP), true));
			break;
		case TASK_ERROR:
		case TASK_ERROR_DEST_NO_EXIST:
		case TASK_ERROR_DEST_DENY:
		case TASK_ERROR_QUOTA_REACHED:
		case TASK_ERROR_TIMEOUT:
		case TASK_ERROR_EXCEED_MAX_FS_SIZE:
		case TASK_ERROR_BROKEN_LINK:
		case TASK_ERROR_DISK_FULL:
		case TASK_ERROR_EXCEED_MAX_TEMP_FS_SIZE:
		case TASK_ERROR_EXCEED_MAX_DEST_FS_SIZE:
		case TASK_ERROR_TORRENT_DUPLICATE:
		case TASK_ERROR_NAME_TOO_LONG_ENCRYPTION:
		case TASK_ERROR_NAME_TOO_LONG:
		case TASK_ERROR_FILE_NO_EXIST:
		case TASK_ERROR_REQUIRED_PREMIUM:
		case TASK_ERROR_NOT_SUPPORT_TYPE:
		case TASK_ERROR_FTP_ENCRYPTION_NOT_SUPPORT_TYPE:
		case TASK_ERROR_EXTRACT_FAIL:
		case TASK_ERROR_EXTRACT_WRONG_PASSWORD:
		case TASK_ERROR_EXTRACT_INVALID_ARCHIVE:
		case TASK_ERROR_EXTRACT_QUOTA_REACHED:
		case TASK_ERROR_EXTRACT_DISK_FULL:
		case TASK_ERROR_REQUIRED_ACCOUNT:
		case TASK_ERROR_TORRENT_INVALID:
		case TASK_UNKNOWN:
			result.add(new TaskActionMenu(taskP, ctxP.getString(R.string.action_retry), new ResumeTaskAction(taskP), true));
			result.add(new TaskActionMenu(taskP, ctxP.getString(R.string.action_delete), new DeleteTaskAction(taskP), true));
			break;
		case TASK_FINISHED:
			result.add(new TaskActionMenu(taskP, ctxP.getString(R.string.action_resume), new ResumeTaskAction(taskP), true));
		case TASK_FINISHING:
			result.add(new TaskActionMenu(taskP, ctxP.getString(R.string.action_clear), new DeleteTaskAction(taskP), true));
			break;
		case TASK_FILEHOSTING_WAITING:
		case TASK_EXTRACTING:
		case TASK_HASH_CHECKING:
		case TASK_WAITING:
			result.add(new TaskActionMenu(taskP, ctxP.getString(R.string.action_pause), new PauseTaskAction(taskP), true));
			result.add(new TaskActionMenu(taskP, ctxP.getString(R.string.action_delete), new DeleteTaskAction(taskP), true));
			break;
		}
		return result;
	}

	/**
	 * Private constructor to avoid instanciation
	 * 
	 * @param taskP
	 */
	private TaskActionMenu(Task taskP, String titleP, SynoAction actionP, boolean enabledP) {
		task = taskP;
		title = titleP;
		action = actionP;
		enabled = enabledP;
	}

	/**
	 * @return the task
	 */
	public Task getTask() {
		return task;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the action
	 */
	public SynoAction getAction() {
		return action;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

}
