package org.jared.synodroid.ds.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.BadTokenException;

import org.jared.synodroid.ds.R;
import org.jared.synodroid.ds.Synodroid;
import org.jared.synodroid.ds.utils.ViewPagerIndicator;
import org.jared.synodroid.ds.utils.EulaHelper;

public class AboutActivity extends BaseActivity{
	private static final String PREFERENCE_FULLSCREEN = "general_cat.fullscreen";
	private static final String PREFERENCE_GENERAL = "general_cat";
	
	MyAdapter mAdapter;
    ViewPager mPager;
    ViewPagerIndicator mIndicator;
    
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// ignore orientation change
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_about, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_licence){
        	// Diplay the EULA
			try {
				EulaHelper.showEula(true, this);
			} catch (BadTokenException e) {
				// Unable to show dialog probably because intent has been closed. Ignoring...
			}
        }
        return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Activity creation
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        setContentView(R.layout.activity_about);
        mAdapter = new MyAdapter(getSupportFragmentManager(), 6, this);
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        // Find the indicator from the layout
        mIndicator = (ViewPagerIndicator)findViewById(R.id.indicator);
        
        // Set the indicator as the pageChangeListener
        mPager.setOnPageChangeListener(mIndicator);
     
        // Initialize the indicator. We need some information here:
        // * What page do we start on.
        // * How many pages are there in total
        // * A callback to get page titles
        mIndicator.init(0, mAdapter.getCount(), mAdapter);
		Resources res = getResources();
		Drawable prev = res.getDrawable(R.drawable.indicator_prev_arrow);
		Drawable next = res.getDrawable(R.drawable.indicator_next_arrow);
		mIndicator.setFocusedTextColor(new int[]{255, 255, 255});
		mIndicator.setUnfocusedTextColor(new int[]{120, 120, 120});
		
		// Set images for previous and next arrows.
		mIndicator.setArrows(prev, next);
		
		mIndicator.setOnClickListener(new OnIndicatorClickListener());

		getActivityHelper().setupActionBar(getString(R.string.menu_about), false);
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getActivityHelper().setupSubActivity();
    }
	
	@Override
	public boolean onSearchRequested() {
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		try{
			if (((Synodroid)getApplication()).DEBUG) Log.v(Synodroid.DS_TAG,"AboutActivity: Resuming about activity.");
		}catch (Exception ex){/*DO NOTHING*/}
		
		// Check for fullscreen
		SharedPreferences preferences = getSharedPreferences(PREFERENCE_GENERAL, Activity.MODE_PRIVATE);
		if (preferences.getBoolean(PREFERENCE_FULLSCREEN, false)) {
			// Set fullscreen or not
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
	}
	
	public static class MyAdapter extends FragmentPagerAdapter implements ViewPagerIndicator.PageInfoProvider{
		int mItemsNum;
		private AboutActivity mCurActivity;
		
		public MyAdapter(FragmentManager pFm, int pItemNum, AboutActivity pCurActivity) {
			super(pFm);
			mItemsNum = pItemNum;
			mCurActivity = pCurActivity;
        }

		@Override
        public int getCount() {
            return mItemsNum;
        }

        @Override
        public Fragment getItem(int position) {
	        switch (position){
	        	case 0:
	        		return new AboutFragment();
	    		case 1:
	    			return new SynologyInfoFragment();
	    		case 2:
	    			return new AddServerFragment();
	    		case 3:
	    			return new AddDownloadFragment();
	    		case 4:
	    			return new SearchEngineFragment();
	    		default:
	    			return new UpgradeProFragment();
	    	}
        }

        public String getTitle(int pos){
        	switch (pos){
	        	case 0:
	        		return mCurActivity.getString(R.string.tab_about);
	    		case 1:
	    			return mCurActivity.getString(R.string.tab_synology);
	    		case 2:
	    			return mCurActivity.getString(R.string.tab_server);
	    		case 3:
	    			return mCurActivity.getString(R.string.tab_download);
	    		case 4:
	    			return mCurActivity.getString(R.string.tab_search);
	    		default:
	    			return mCurActivity.getString(R.string.tab_pro);
        	}
		}

    }
	
	class OnIndicatorClickListener implements ViewPagerIndicator.OnClickListener{
		public void onCurrentClicked(View v) {}
		
		public void onNextClicked(View v) {
			mPager.setCurrentItem(Math.min(mAdapter.getCount() - 1, mIndicator.getCurrentPosition() + 1));
		}

		public void onPreviousClicked(View v) {
			mPager.setCurrentItem(Math.max(0, mIndicator.getCurrentPosition() - 1));
		}
    	
    }
}
