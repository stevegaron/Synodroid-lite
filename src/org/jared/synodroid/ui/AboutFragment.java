package org.jared.synodroid.ui;

import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.BadTokenException;
import android.widget.Button;
import android.widget.TextView;

import org.jared.synodroid.R;
import org.jared.synodroid.Synodroid;
import org.jared.synodroid.utils.EulaHelper;

public class AboutFragment extends Fragment{
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// ignore orientation change
		super.onConfigurationChanged(newConfig);
	}
	
	/**
	 * Activity creation
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		try{
			if (((Synodroid)getActivity().getApplication()).DEBUG) Log.d(Synodroid.DS_TAG,"AboutActivity: Creating about fragment.");
		}catch (Exception ex){/*DO NOTHING*/}
		
		final FragmentActivity aboutActivity = this.getActivity();
		View about = inflater.inflate(R.layout.about, null, false);
		Button eulaBtn = (Button) about.findViewById(R.id.id_eula_view);
		eulaBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Diplay the EULA
				try {
					EulaHelper.showEula(true, aboutActivity);
				} catch (BadTokenException e) {
					// Unable to show dialog probably because intent has been closed. Ignoring...
				}
			}
		});

		String vn = "" + getString(R.string.app_name);
		try {
			PackageInfo pi = aboutActivity.getPackageManager().getPackageInfo(aboutActivity.getPackageName(), 0);
			if (pi != null) {
				vn += " " + pi.versionName;
			}
		} catch (Exception e) {
			try{
				if (((Synodroid)getActivity().getApplication()).DEBUG) Log.e(Synodroid.DS_TAG, "AboutFragment: Error while retrieving package information", e);
			}catch (Exception ex){/*DO NOTHING*/}
		}
		TextView vname = (TextView) about.findViewById(R.id.app_vers_name_text);
		vname.setText(vn);

		TextView message = (TextView) about.findViewById(R.id.about_code);
		message.setText(Html.fromHtml("<a href=\"https://plus.google.com/111893484035545745539\">"+getString(R.string.gplus_title)+"</a>"));
		message.setMovementMethod(LinkMovementMethod.getInstance());
		return about;
	}
}