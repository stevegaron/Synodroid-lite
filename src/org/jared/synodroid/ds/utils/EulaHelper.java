/*
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jared.synodroid.ds.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jared.synodroid.ds.R;
import org.jared.synodroid.ds.ui.HomeActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * A helper for showing EULAs and storing a {@link SharedPreferences} bit indicating whether the
 * user has accepted.
 */
public class EulaHelper {
	private static final String ASSET_EULA = "EULA";
	
	public static boolean hasAcceptedEula(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("accepted_eula", false);
    }

    private static void setAcceptedEula(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean("accepted_eula", true).commit();
        ((HomeActivity) context).showDialog(HomeActivity.NO_SERVER_DIALOG_ID);
    }

    /**
     * Show End User License Agreement.
     *
     * @param accepted True IFF user has accepted license already, which means it can be dismissed.
     *                 If the user hasn't accepted, then the EULA must be accepted or the program
     *                 exits.
     * @param activity Activity started from.
     */
    public static void showEula(final boolean accepted, final Activity activity) {
        AlertDialog.Builder eula = new AlertDialog.Builder(activity)
                .setTitle(R.string.eula_title)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setMessage(readEula(activity))
                .setCancelable(accepted);

        if (accepted) {
            // If they've accepted the EULA allow, show an OK to dismiss.
            eula.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        } else {
            // If they haven't accepted the EULA allow, show accept/decline buttons and exit on
            // decline.
            eula
                    .setPositiveButton(R.string.accept,
                            new android.content.DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    setAcceptedEula(activity);
                                    dialog.dismiss();
                                }
                            })
                    .setNegativeButton(R.string.decline,
                            new android.content.DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    activity.finish();
                                }
                            });
        }
        eula.show();
    }
    
    private static CharSequence readEula(Activity activity) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(activity.getAssets().open(ASSET_EULA)));
			String line;
			StringBuilder buffer = new StringBuilder();
			while ((line = in.readLine()) != null)
				buffer.append(line).append('\n');
			return buffer;
		} catch (IOException e) {
			return "";
		} finally {
			closeStream(in);
		}
	}

	/**
	 * Closes the specified stream.
	 * 
	 * @param stream
	 *            The stream to close.
	 */
	private static void closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				// Ignore
			}
		}
	}
}
