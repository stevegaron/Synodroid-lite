package org.jared.synodroid.ds.utils;

import android.content.SearchRecentSuggestionsProvider;

public class SynodroidSearchSuggestion extends SearchRecentSuggestionsProvider {
	public final static String AUTHORITY = "org.jared.synodroid.ds.utils.SynodroidSearchSuggestion";
	public final static int MODE = DATABASE_MODE_QUERIES;

	public SynodroidSearchSuggestion() {
		setupSuggestions(AUTHORITY, MODE);
	}
}
