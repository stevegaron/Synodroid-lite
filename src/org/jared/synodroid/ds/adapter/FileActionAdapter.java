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
package org.jared.synodroid.ds.adapter;

import java.util.List;

import org.jared.synodroid.ds.data.Task;
import org.jared.synodroid.ds.data.TaskFile;
import org.jared.synodroid.ds.R;
import org.jared.synodroid.ds.action.FileActionMenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * An adapter which is able to build an action list according to the task's state
 * 
 * @author Eric Taix (eric.taix at gmail.com)
 */
public class FileActionAdapter extends BaseAdapter {

	// Available actions
	private List<FileActionMenu> actions;
	// The XML view inflater
	private final LayoutInflater inflater;

	/**
	 * Constructor
	 * 
	 * @param ctxP
	 *            The context
	 */
	public FileActionAdapter(Context ctxP, TaskFile fileP, Task taskP, List<TaskFile> files) {
		actions = FileActionMenu.createActions(ctxP, fileP, taskP, files);
		inflater = (LayoutInflater) ctxP.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return actions.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int positionP) {
		return actions.get(positionP);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int positionP) {
		return positionP;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int positionP, View convertViewP, ViewGroup parentP) {
		View view = null;
		if (convertViewP != null) {
			view = convertViewP;
		} else {
			view = inflater.inflate(R.layout.action_template, parentP, false);
		}
		TextView textView = (TextView) view.findViewById(R.id.id_action);
		textView.setEnabled(actions.get(positionP).isEnabled());
		textView.setText(actions.get(positionP).getTitle());
		return view;
	}

}
