package org.jared.synodroid.ds.ui;

import java.util.List;

import org.jared.synodroid.ds.R;
import org.jared.synodroid.ds.Synodroid;
import org.jared.synodroid.ds.action.AddPwTaskAction;
import org.jared.synodroid.ds.action.AddTaskAction;
import org.jared.synodroid.ds.action.ClearAllTaskAction;
import org.jared.synodroid.ds.action.EnumShareAction;
//import org.jared.synodroid.ds.action.GetDirectoryListShares;
import org.jared.synodroid.ds.action.ResumeAllAction;
import org.jared.synodroid.ds.action.StopAllAction;
import org.jared.synodroid.ds.adapter.TaskAdapter;
//import org.jared.synodroid.ds.data.DSMVersion;
import org.jared.synodroid.ds.data.DSMVersion;
import org.jared.synodroid.ds.data.Task;
import org.jared.synodroid.ds.utils.ActivityHelper;
import org.jared.synodroid.ds.ui.DownloadPreferenceActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Front-door {@link Activity} that displays high-level features the schedule application offers to
 * users. Depending on whether the device is a phone or an Android 3.0+ tablet, different layouts
 * will be used. For example, on a phone, the primary content is a {@link DashboardFragment},
 * whereas on a tablet, both a {@link DashboardFragment} and a {@link TagStreamFragment} are
 * displayed.
 */
public class HomeActivity extends BaseActivity {
	private static final String PREFERENCE_AUTO = "auto";
	private static final String PREFERENCE_AUTO_CREATENOW = "auto.createnow";
	private static final String PREFERENCE_FULLSCREEN = "general_cat.fullscreen";
	private static final String PREFERENCE_SHOW_GET_STARTED = "general_cat.show_get_started";
	private static final String PREFERENCE_GENERAL = "general_cat";
	
	private static final int CONNECTION_DIALOG_ID = 1;
	public static final int NO_SERVER_DIALOG_ID = 2;
	private static final int ADD_DOWNLOAD = 3;
	
	//private TagStreamFragment mTagStreamFragment;
    @Override
	public boolean onSearchRequested() {
    	showSearchActivity();
    	return true;
	}
   
    @Override
	public void onConfigurationChanged(Configuration newConfig) {
		// ignore orientation change
		super.onConfigurationChanged(newConfig);
	}
    
    public void updateActionBarTitle(String title, boolean is_secure){
    	ActivityHelper ah = getActivityHelper();
    	if (ah != null) ah.setActionBarTitle(title, is_secure);
    }
    
    public void updateActionBarTitleOCL(android.view.View.OnClickListener ocl){
    	ActivityHelper ah = getActivityHelper();
    	if (ah != null) ah.setTitleOnClickListener(ocl);
    }
    /**
	 * Create the connection and error dialogs
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		super.onCreateDialog(id);
		Dialog dialog = null;
		switch (id) {
		// The connection dialog
		case CONNECTION_DIALOG_ID:
			dialog = new ProgressDialog(this);
			dialog.setTitle("");
			dialog.setCancelable(false);
			((ProgressDialog) dialog).setMessage(getString(R.string.connect_connecting2));
			((ProgressDialog) dialog).setIndeterminate(true);
			break;
		// No server have been yet configured
		case NO_SERVER_DIALOG_ID:
			AlertDialog.Builder builderNoServer = new AlertDialog.Builder(this);
			builderNoServer.setTitle(R.string.dialog_title_information);
			builderNoServer.setMessage(getString(R.string.no_server_configured));
			builderNoServer.setCancelable(true);
			builderNoServer.setPositiveButton(getString(R.string.button_yesplease), new OnClickListener() {
				// Launch the Preference activity
				public void onClick(DialogInterface dialogP, int whichP) {
					okToCreateAServer();
				}
			});
			builderNoServer.setNegativeButton(getString(R.string.button_nothanks), new OnClickListener() {
				// Launch the Preference activity
				public void onClick(DialogInterface dialogP, int whichP) {
					FragmentManager fm = getSupportFragmentManager();
					DownloadFragment fragment_download = (DownloadFragment) fm.findFragmentById(R.id.fragment_download);
					fragment_download.alreadyCanceled = true;
				}
			});
			dialog = builderNoServer.create();
			break;
		case ADD_DOWNLOAD:
			AlertDialog.Builder add_download = new AlertDialog.Builder(this);
			add_download.setTitle(R.string.menu_add);
			LayoutInflater inflater = getLayoutInflater();
			View v = inflater.inflate(R.layout.add_download, null);
			final EditText edt = (EditText) v.findViewById(R.id.add_url);
			edt.setText("");
			final EditText user = (EditText) v.findViewById(R.id.username);
			user.setText("");
			final EditText pass = (EditText) v.findViewById(R.id.pass);
			pass.setText("");
			final ImageView exp_col = (ImageView) v.findViewById(R.id.exp_col);
			final LinearLayout creds = (LinearLayout) v.findViewById(R.id.credentials);
			final RelativeLayout adv = (RelativeLayout) v.findViewById(R.id.adv_settings);
			adv.setOnClickListener(new android.view.View.OnClickListener(){
				@Override
				public void onClick(View v) {
					if (creds.getVisibility() == View.GONE){
						exp_col.setImageResource(R.drawable.ic_colapse);
						creds.setVisibility(View.VISIBLE);
					}
					else{
						creds.setVisibility(View.GONE);
						exp_col.setImageResource(R.drawable.ic_expand);
					}
				}
				
			});
			
			try{
				if (((Synodroid)getApplication()).getServer().getDsmVersion().smallerThen(DSMVersion.VERSION3_1)){
					adv.setVisibility(View.GONE);
				}
			}
			catch (NullPointerException e){}
			
			add_download.setView(v);
			add_download.setCancelable(true);
			add_download.setPositiveButton(getString(R.string.menu_add), new OnClickListener() {
				// Launch the Preference activity
				public void onClick(DialogInterface dialogP, int whichP) {
					try{
						if (((Synodroid)getApplication()).DEBUG) Log.i(Synodroid.DS_TAG, "HomeActivity: Adding url:" + edt.getText().toString());
					}catch (Exception ex){/*DO NOTHING*/}
					Synodroid app = (Synodroid) getApplication();
					FragmentManager fm = getSupportFragmentManager();
			        try{
			        	DownloadFragment fragment_download = (DownloadFragment) fm.findFragmentById(R.id.fragment_download);
			        	Uri uri = Uri.parse(edt.getText().toString());
			        	
			        	if (!uri.toString().startsWith("http://") && !uri.toString().startsWith("https://") && !uri.toString().startsWith("ftp://") && !uri.toString().startsWith("file://") && !uri.toString().startsWith("magnet:")){
			        		uri = Uri.parse("http://"+uri.toString());
			        	}
			        	if (uri.toString().startsWith("http://magnet/")){
			        		uri = Uri.parse(uri.toString().replace("http://magnet/", "magnet:"));
			        	}
			        	else if (uri.toString().startsWith("https://magnet/")){
			        		uri = Uri.parse(uri.toString().replace("https://magnet/", "magnet:"));
			        	}
			        	
			        	if (!user.getText().toString().equals("") || !pass.getText().toString().equals("")){
			        		app.executeAsynchronousAction(fragment_download, new AddPwTaskAction(uri, user.getText().toString(), pass.getText().toString(), true, false), true);
			        	}
			        	else{
			        		app.executeAsynchronousAction(fragment_download, new AddTaskAction(uri, true, false), true);
			        	}
			        }
					catch (Exception e){
						//Cannot clear all when download fragment not accessible.
						try{
							if (((Synodroid)getApplication()).DEBUG) Log.e(Synodroid.DS_TAG, "HomeActivity: App tried to call add download when download fragment hidden.");
						}catch (Exception ex){/*DO NOTHING*/}
					}
					removeDialog(ADD_DOWNLOAD);
				}
			});
			add_download.setNegativeButton(getString(android.R.string.cancel), new OnClickListener() {
				// Launch the Preference activity
				public void onClick(DialogInterface dialogP, int whichP) {
					removeDialog(ADD_DOWNLOAD);
				}
			});
			dialog = add_download.create();
			break;
		}
		return dialog;
	}
	
	/**
	 * The user agree to create a new as no server has been configured or no server is suitable for the current connection
	 */
	private void okToCreateAServer() {
		final SharedPreferences preferences = getSharedPreferences(PREFERENCE_AUTO, Activity.MODE_PRIVATE);
		preferences.edit().putBoolean(PREFERENCE_AUTO_CREATENOW, true).commit();
		showPreferenceActivity();
	}
	
	/**
	 * Show the preference activity
	 */
	private void showSearchActivity() {
		Intent next = new Intent();
		next.setClass(this, SearchActivity.class);
		next.putExtra("start_search", true);
		startActivity(next);
	}
	
	/**
	 * Show the preference activity
	 */
	private void showPreferenceActivity() {
		Intent next = new Intent();
		next.setClass(this, DownloadPreferenceActivity.class);
		startActivity(next);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		((Synodroid) getApplication()).pauseServer();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		
		SharedPreferences preferences = getSharedPreferences(PREFERENCE_GENERAL, Activity.MODE_PRIVATE);
        
        if (preferences.getBoolean(PREFERENCE_SHOW_GET_STARTED, true)) {
			Intent next = new Intent();
			next.setClass(HomeActivity.this, GetStartedActivity.class);
			startActivity(next);
		}
        
		try{
			if (((Synodroid)getApplication()).DEBUG) Log.v(Synodroid.DS_TAG,"HomeActivity: Resuming home activity.");
		}catch (Exception ex){/*DO NOTHING*/}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onNewIntent(android.content.Intent)
	 */
	@Override
	protected void onNewIntent(Intent intentP) {
		super.onNewIntent(intentP);
		setIntent(intentP);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		if (hasFocus) {
			SharedPreferences preferences = getSharedPreferences(PREFERENCE_GENERAL, Activity.MODE_PRIVATE);
			if (preferences.getBoolean(PREFERENCE_FULLSCREEN, false)) {
				// Set fullscreen or not
				getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
			} else {
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			}

		}
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getActivityHelper().setupActionBar(getString(R.string.app_name), true);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getActivityHelper().setupHomeActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.refresh_menu_items, menu);
        getMenuInflater().inflate(R.menu.default_menu_items, menu);
        getMenuInflater().inflate(R.menu.download_menu_items, menu);
    	super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
        	try{
        		if (((Synodroid)getApplication()).DEBUG) Log.v(Synodroid.DS_TAG,"HomeActivity: Menu refresh selected.");
        	}catch (Exception ex){/*DO NOTHING*/}
        	
            triggerRefresh();
            return true;
        }
        else if (item.getItemId() == R.id.menu_search){
        	try{
        		if (((Synodroid)getApplication()).DEBUG) Log.v(Synodroid.DS_TAG,"HomeActivity: Menu search selected.");
        	}catch (Exception ex){/*DO NOTHING*/}
        	
        	showSearchActivity();
        }
        /*else if (item.getItemId() == R.id.menu_add){
        	try{
        		if (((Synodroid)getApplication()).DEBUG) Log.v(Synodroid.DS_TAG,"HomeActivity: Menu add selected.");
        	}catch (Exception ex){}
        	
            showDialog(ADD_DOWNLOAD);
        }*/
		else if (item.getItemId() == R.id.menu_preferences){
			try{
				if (((Synodroid)getApplication()).DEBUG) Log.v(Synodroid.DS_TAG,"HomeActivity: Menu preference selected.");
			}catch (Exception ex){/*DO NOTHING*/}
        	
            showPreferenceActivity();
		}
		else if (item.getItemId() == R.id.menu_share){
			try{
				if (((Synodroid)getApplication()).DEBUG) Log.v(Synodroid.DS_TAG,"HomeActivity: Menu get share list selected.");
			}catch (Exception ex){/*DO NOTHING*/}
        	
            Synodroid app = (Synodroid) getApplication();
			FragmentManager fm = getSupportFragmentManager();
	        try{
	        	DownloadFragment fragment_download = (DownloadFragment) fm.findFragmentById(R.id.fragment_download);
	        	/*if (app.getServer().getDsmVersion().greaterThen(DSMVersion.VERSION3_0)){
	        		app.executeAsynchronousAction(fragment_download, new GetDirectoryListShares(null), false);
	        	}
	        	else{*/
	        		app.executeAsynchronousAction(fragment_download, new EnumShareAction(), false);
	        	//}
	        }
			catch (Exception e){
				try{
					if (((Synodroid)getApplication()).DEBUG) Log.e(Synodroid.DS_TAG, "HomeActivity: App tried to call get share when download fragment hidden.");
				}catch (Exception ex){/*DO NOTHING*/}
			}
		}
		else if (item.getItemId() == R.id.menu_clear_all){
			try{
				if (((Synodroid)getApplication()).DEBUG) Log.v(Synodroid.DS_TAG,"HomeActivity: Menu clear all completed selected.");
			}catch (Exception ex){/*DO NOTHING*/}
        	
            Synodroid app = (Synodroid) getApplication();
			FragmentManager fm = getSupportFragmentManager();
	        try{
	        	DownloadFragment fragment_download = (DownloadFragment) fm.findFragmentById(R.id.fragment_download);
	        	app.executeAction(fragment_download, new ClearAllTaskAction(), false);
	        }
			catch (Exception e){
				try{
					if (((Synodroid)getApplication()).DEBUG) Log.e(Synodroid.DS_TAG, "HomeActivity: App tried to call clear all when download fragment hidden.");
				}catch (Exception ex){/*DO NOTHING*/}
			}
		}
		else if (item.getItemId() == R.id.menu_pause_all){
			try{
				if (((Synodroid)getApplication()).DEBUG) Log.v(Synodroid.DS_TAG,"HomeActivity: Menu pause all selected.");
			}catch (Exception ex){/*DO NOTHING*/}
        	
            Synodroid app = (Synodroid) getApplication();
			FragmentManager fm = getSupportFragmentManager();
	        try{
	        	DownloadFragment fragment_download = (DownloadFragment) fm.findFragmentById(R.id.fragment_download);
	        	List<Task> tasks = ((TaskAdapter) fragment_download.taskView.getAdapter()).getTaskList();
	    		app.executeAction(fragment_download, new StopAllAction(tasks), false);
	        }
			catch (Exception e){
				try{
					if (((Synodroid)getApplication()).DEBUG) Log.e(Synodroid.DS_TAG, "HomeActivity: App tried to call pause all when download fragment hidden.");
				}catch (Exception ex){/*DO NOTHING*/}
			}
		}
		else if (item.getItemId() == R.id.menu_revert){
			try{
				if (((Synodroid)getApplication()).DEBUG) Log.v(Synodroid.DS_TAG,"HomeActivity: Menu resume all selected.");
			}catch (Exception ex){/*DO NOTHING*/}
        	
            Synodroid app = (Synodroid) getApplication();
			FragmentManager fm = getSupportFragmentManager();
	        try{
	        	DownloadFragment fragment_download = (DownloadFragment) fm.findFragmentById(R.id.fragment_download);
	        	List<Task> tasks = ((TaskAdapter) fragment_download.taskView.getAdapter()).getTaskList();
	    		app.executeAction(fragment_download, new ResumeAllAction(tasks), false);
	        }
			catch (Exception e){
				try{
					if (((Synodroid)getApplication()).DEBUG) Log.e(Synodroid.DS_TAG, "HomeActivity: App tried to call resume all when download fragment hidden.");
				}catch (Exception ex){/*DO NOTHING*/}
			}
		}
		else if (item.getItemId() == R.id.menu_about){
			try{
				if (((Synodroid)getApplication()).DEBUG) Log.v(Synodroid.DS_TAG,"HomeActivity: Menu about selected.");
			}catch (Exception ex){/*DO NOTHING*/}
        	
            // Starting new intent
			Intent next = new Intent();
			next.setClass(this, AboutActivity.class);
			startActivity(next);
		}
        return super.onOptionsItemSelected(item);
    }

    private void triggerRefresh() {
    	try{
    		if (((Synodroid)getApplication()).DEBUG) Log.v(Synodroid.DS_TAG,"HomeActivity: Forcing task list refresh.");
    	}catch (Exception ex){/*DO NOTHING*/}
    	
    	((Synodroid) getApplication()).forceRefresh();
    }

    public void updateRefreshStatus(boolean refreshing) {
        getActivityHelper().setRefreshActionButtonCompatState(refreshing);
    }
}
