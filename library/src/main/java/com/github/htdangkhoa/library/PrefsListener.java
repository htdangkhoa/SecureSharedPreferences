package com.github.htdangkhoa.library;

import android.content.SharedPreferences;

/**
 * Created by dangkhoa on 2/1/18.
 */

public interface PrefsListener {
    void onPrefsChanged(SharedPreferences sharedPreferences, String key);
}
