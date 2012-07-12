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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.BadTokenException;
import android.widget.ImageView;

/**
 * This activity displays a help page
 * 
 * @author Steve Garon (synodroid at gmail dot com)
 */
public class AddDownloadFragment extends Fragment {
	private static final String SYNO_PRO_URL_DL_MARKET = "market://details?id=com.bigpupdev.synodroid";
	
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
			if (((Synodroid)getActivity().getApplication()).DEBUG) Log.v(Synodroid.DS_TAG,"AddDownloadFragment: Creating Add Download fragment");
		}catch (Exception ex){/*DO NOTHING*/}
		
		View addDL = inflater.inflate(R.layout.add_dl, null, false);

		ImageView goPro = (ImageView) addDL.findViewById(R.id.img_play);
		goPro.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent goToMarket = null;
				goToMarket = new Intent(Intent.ACTION_VIEW, Uri.parse(SYNO_PRO_URL_DL_MARKET));
				try {
					startActivity(goToMarket);
				} catch (Exception e) {
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					// By default the message is "Error Unknown"
					builder.setMessage(R.string.err_nomarket_upgrade);
					builder.setTitle(getString(R.string.connect_error_title)).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
					AlertDialog errorDialog = builder.create();
					try {
						errorDialog.show();
					} catch (BadTokenException ex) {
						// Unable to show dialog probably because intent has been closed. Ignoring...
					}
				}

			}
		});
		return addDL;
	}
}
