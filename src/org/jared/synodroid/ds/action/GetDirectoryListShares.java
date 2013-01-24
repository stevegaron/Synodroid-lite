/**
 * Copyright 2010 Eric Taix Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.jared.synodroid.ds.action;

import java.util.List;

import org.jared.synodroid.ds.server.SynoServer;
import org.jared.synodroid.ds.protocol.ResponseHandler;
import org.jared.synodroid.ds.R;

import org.jared.synodroid.ds.data.Folder;
import org.jared.synodroid.ds.data.SharedFolderSelection;
import org.jared.synodroid.ds.data.Task;

/**
 * Enum all shared directories
 * 
 * @author Eric Taix (eric.taix at gmail dot com)
 */
public class GetDirectoryListShares implements SynoAction {

	private String id;
	
	public GetDirectoryListShares(String idP) {
		id = idP;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.common.SynoAction#execute(org.jared.synodroid.ds.TorrentListActivity, org.jared.synodroid.ds.common.SynoServer)
	 */
	public void execute(ResponseHandler handlerP, SynoServer serverP) throws Exception {
		String name = "/";
		if (id == null)
			id = "remote/"+serverP.getDSMHandlerFactory().getDSHandler().getSharedDirectory(false);
		
		if (id.startsWith("remote/")){
			name = id.substring(7);
		}
		
		List<Folder> folders = serverP.getDSMHandlerFactory().getDSHandler().getDirectoryListing(id);
		
		serverP.fireMessage(handlerP, ResponseHandler.MSG_SHARED_DIRECTORIES_RETRIEVED, new SharedFolderSelection(name, folders));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.common.SynoAction#getName()
	 */
	public String getName() {
		return "List directories";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.common.SynoAction#getToastId()
	 */
	public int getToastId() {
		return R.string.action_enum_shared;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.common.SynoAction#isToastable()
	 */
	public boolean isToastable() {
		return false;
	}

	/**
	 * @return the task
	 */
	public Task getTask() {
		return null;
	}

}
