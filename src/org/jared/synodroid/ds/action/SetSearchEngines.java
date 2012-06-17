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

import org.jared.synodroid.ds.data.SearchEngine;
import org.jared.synodroid.ds.data.Task;

/**
 * Set the shared directory
 * 
 * @author Eric Taix (eric.taix at gmail.com)
 */
public class SetSearchEngines implements SynoAction {

	private List<SearchEngine> seList;

	/**
	 * Constructor
	 * 
	 * @param taskP
	 * @param filesP
	 * @param seedingRatioP
	 * @param seedingIntervalP
	 */
	public SetSearchEngines(List<SearchEngine> seListP) {
		seList = seListP;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.common.action.SynoAction#execute(org.jared.synodroid.ds.common.protocol.ResponseHandler, org.jared.synodroid.ds.common.SynoServer)
	 */
	public void execute(ResponseHandler handlerP, SynoServer serverP) throws Exception {
		serverP.getDSMHandlerFactory().getDSHandler().setSearchEngines(seList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.common.action.SynoAction#getName()
	 */
	public String getName() {
		return "Setting search engine";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.common.action.SynoAction#getTask()
	 */
	public Task getTask() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jared.synodroid.ds.common.action.SynoAction#getToastId()
	 */
	public int getToastId() {
		return R.string.action_set_searchengine;
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
