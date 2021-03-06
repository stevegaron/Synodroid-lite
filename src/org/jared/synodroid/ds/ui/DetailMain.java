/**
 * Copyright 2010 Eric Taix Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.jared.synodroid.ds.ui;


import org.jared.synodroid.ds.R;
import org.jared.synodroid.ds.Synodroid;
import org.jared.synodroid.ds.action.DetailTaskAction;
import org.jared.synodroid.ds.action.SynoAction;
import org.jared.synodroid.ds.data.Task;
import org.jared.synodroid.ds.ui.SynodroidFragment;
import org.jared.synodroid.ds.adapter.DetailAdapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * This activity displays a task's details
 * 
 * @author Eric Taix (eric.taix at gmail.com)
 */
public class DetailMain extends SynodroidFragment{
	// The adapter for general informations
	DetailAdapter genAdapter;
	// The task to retrieve details from
	private Task task;
	
	private Activity a;
	
	public void finish(){
		getActivity().finish();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// ignore orientation change
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onResume(){
		super.onResume();
		Synodroid app = (Synodroid) a.getApplication();
		try{
			if (app.DEBUG) Log.v(Synodroid.DS_TAG,"DetailMain: Resuming server.");
		}catch (Exception ex){/*DO NOTHING*/}
		
		SynoAction detailAction = new DetailTaskAction(task);
		app.executeAsynchronousAction(this, detailAction, false);
		app.setRecurrentAction(this, detailAction);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		a = this.getActivity();
		try{
			if (((Synodroid)((DetailActivity)a).getApplication()).DEBUG) Log.v(Synodroid.DS_TAG,"DetailMain: Creating detail main fragment.");
		}catch (Exception ex){/*DO NOTHING*/}
		
		// Get the details intent
		Intent intent = a.getIntent();
		task = (Task) intent.getSerializableExtra("org.jared.synodroid.ds.Details");

		// Build the general tab
		View v = inflater.inflate(R.layout.detail_main, null);

		ListView genListView = (ListView) v.findViewById(android.R.id.list);
		genAdapter = new DetailAdapter(this);
		genListView.setAdapter(genAdapter);
		genListView.setOnItemClickListener(genAdapter);
		View empty = v.findViewById(android.R.id.empty);
		genListView.setEmptyView(empty);
		setRetainInstance(true);
		return v;
	}

	@Override
	public void handleMessage(Message msgP) {
		((DetailActivity)a).handleMessage(msgP);
	}
}
