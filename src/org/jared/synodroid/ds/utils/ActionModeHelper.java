package org.jared.synodroid.ds.utils;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.View;

import org.jared.synodroid.ds.Synodroid;
import org.jared.synodroid.ds.action.DeleteMultipleTaskAction;
import org.jared.synodroid.ds.action.PauseMultipleTaskAction;
import org.jared.synodroid.ds.action.ResumeMultipleTaskAction;
import org.jared.synodroid.ds.data.Task;
import org.jared.synodroid.ds.ui.BaseActivity;
import org.jared.synodroid.ds.ui.DownloadFragment;

public class ActionModeHelper{
	public boolean terminating = false;
	DownloadFragment mCurrentFragment = null;
	ActivityHelper mCurrentActivityHelper = null;
	
	public static ActionModeHelper createInstance() {
        return UIUtils.isHoneycomb() ?
                new ActionModeHelperHoneycomb() :
                new ActionModeHelper();
    }
	
	public void stopActionMode(){
		if (mCurrentActivityHelper != null){
			terminating = true;
    		mCurrentFragment.resetChecked();
    		mCurrentActivityHelper.stopActionMode();
			mCurrentFragment = null;
			mCurrentActivityHelper = null;
            terminating = false;
		}
	}
	
	private List<Task> countSelected(){
		List<Task> t_list= new ArrayList<Task>();
    	for ( int i = mCurrentFragment.checked_tasks.size() -1 ; i >= 0 ; i--){
    		t_list.add(mCurrentFragment.checked_tasks.get(i));
    	}
		return t_list;
	}
	
	public void startActionMode(DownloadFragment fragment){
		if (mCurrentActivityHelper != null){
			return;
		}
		terminating = false;
		mCurrentFragment = fragment;
		mCurrentActivityHelper = ((BaseActivity) fragment.getActivity()).getActivityHelper();
		
		final Synodroid app = (Synodroid) mCurrentFragment.getActivity().getApplication();
    	
		View.OnClickListener cancelClickListener = new View.OnClickListener() {
            public void onClick(View view) {
            	stopActionMode();
            }
        };
        View.OnClickListener clearClickListener = new View.OnClickListener() {
            public void onClick(View view) {
            	try{
            		if (app.DEBUG) Log.v(Synodroid.DS_TAG, "ActionModeHelper: Action Mode clear clicked.");
            	}catch (Exception ex){/*DO NOTHING*/}
            	app.executeAction(mCurrentFragment, new DeleteMultipleTaskAction(countSelected()), false);
            	stopActionMode();
            	app.forceRefresh();
            }
        };
        View.OnClickListener resumeClickListener = new View.OnClickListener() {
            public void onClick(View view) {
            	try{
            		if (app.DEBUG) Log.v(Synodroid.DS_TAG, "ActionModeHelper: Action Mode resume clicked.");
            	}catch (Exception ex){/*DO NOTHING*/}
            	app.executeAction(mCurrentFragment, new ResumeMultipleTaskAction(countSelected()), false);
            	stopActionMode();
            	app.forceRefresh();
            }
        };
        View.OnClickListener pauseClickListener = new View.OnClickListener() {
            public void onClick(View view) {
            	try{
            		if (app.DEBUG) Log.v(Synodroid.DS_TAG, "ActionModeHelper: Action Mode pause clicked.");
            	}catch (Exception ex){/*DO NOTHING*/}
            	app.executeAction(mCurrentFragment, new PauseMultipleTaskAction(countSelected()), false);
            	stopActionMode();
            	app.forceRefresh();
            }
        };
		
		mCurrentActivityHelper.startActionMode(mCurrentFragment, cancelClickListener, clearClickListener,
				resumeClickListener, pauseClickListener);
	}
	
	public void setTitle(String title){
		if (mCurrentActivityHelper != null){
			mCurrentActivityHelper.setActionModeTitle(title);
		}
	}
}
