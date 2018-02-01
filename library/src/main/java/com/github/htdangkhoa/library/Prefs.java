package com.github.htdangkhoa.library;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.securepreferences.SecurePreferences;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by dangkhoa on 2/1/18.
 */

public class Prefs {
    private static SecurePreferences securePreferences;
    private static SharedPreferences sharedPreferences;
    private static Context context;
    String password;
    String filename;

    private static final String LENGTH = "#LENGTH";
    private static final String REGEX_STRING_LIST = "‚‗‗‗‗‗‚";

    /**
     * Set context.
     * @param context
     * */
    public Prefs setContext(Context context) {
        this.context = context;
        return this;
    }

    /**
     * Set password.
     * @param password
     * */
    public Prefs setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * Set file name.
     * @param filename
     * */
    public Prefs setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    /**
     * Build.
     * */
    public Prefs build() {
        if (context == null) throw new RuntimeException("Context is being null, please call `setContext(Context)` before.");

        if (password != null) {
            if (filename == null) filename = context.getPackageName();

            securePreferences = new SecurePreferences(context, password, filename);
        } else {
            securePreferences = new SecurePreferences(context);
        }
        sharedPreferences = securePreferences;

        return this;
    }

    /**
     * Change password preferences.
     * @param newPassword
     * */
    public static void changePassword(String newPassword) throws GeneralSecurityException {
        securePreferences.handlePasswordChange(newPassword, context);
        sharedPreferences = securePreferences;
    }

    /**
     * Put boolean.
     * @param key
     * @param value
     * */
    public static void putBoolean(@NonNull String key, boolean value) {
        getEditor(sharedPreferences)
                .putBoolean(key, value)
                .apply();
    }

    /**
     * Put string.
     * @param key
     * @param value
     * */
    public static void putString(@NonNull String key, String value) {
        getEditor(sharedPreferences)
                .putString(key, value)
                .apply();
    }

    /**
     * Put int.
     * @param key
     * @param value
     * */
    public static void putInt(@NonNull String key, int value) {
        getEditor(sharedPreferences)
                .putInt(key, value)
                .apply();
    }

    /**
     * Put long.
     * @param key
     * @param value
     * */
    public static void putLong(@NonNull String key, long value) {
        getEditor(sharedPreferences)
                .putLong(key, value)
                .apply();
    }

    /**
     * Put float.
     * @param key
     * @param value
     * */
    public static void putFloat(@NonNull String key, float value) {
        getEditor(sharedPreferences)
                .putFloat(key, value)
                .apply();
    }

    /**
     * Put string set.
     * @param key
     * @param value
     * */
    public static void putStringSet(@NonNull String key, final Set<String> value) {
        getEditor(sharedPreferences)
                .putStringSet(key, value)
                .apply();
    }

    /**
     * Put ordered string set.
     * @param key
     * @param value
     * */
    public static void putOrderedStringSet(String key, Set<String> value) {
        final SharedPreferences.Editor editor = getEditor(sharedPreferences);
        int stringSetLength = 0;
        if (sharedPreferences.contains(key + LENGTH)) {
            // First read what the value was
            stringSetLength = sharedPreferences.getInt(key + LENGTH, -1);
        }
        editor.putInt(key + LENGTH, value.size());
        int i = 0;
        for (String aValue : value) {
            editor.putString(key + "[" + i + "]", aValue);
            i++;
        }
        for (; i < stringSetLength; i++) {
            // Remove any remaining values
            editor.remove(key + "[" + i + "]");
        }
        editor.apply();
    }

    /**
     * Put list string.
     * @param key
     * @param value
     * */
    private static void putListString(@NonNull String key, @NonNull ArrayList<String> value) {
        String[] strings = value.toArray(new String[value.size()]);
        putString(key, TextUtils.join(REGEX_STRING_LIST, strings));
    }

    /**
     * Put list object.
     * @param key
     * @param value
     * */
    public static void putListObject(@NonNull String key, List<Object> value) {
        Gson gson = new Gson();

        ArrayList<String> objects = new ArrayList<>();
        for (Object o : value) {
            objects.add(gson.toJson(o));
        }

        putListString(key, objects);
    }

    /**
     * Get boolean.
     * @param key
     * @param defaultValue
     * */
    public static Boolean getBoolean(@NonNull String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * Get string.
     * @param key
     * @param defaultValue
     * */
    public static String getString(@NonNull String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    /**
     * Get int.
     * @param key
     * @param defaultValue
     * */
    public static int getInt(@NonNull String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    /**
     * Get long.
     * @param key
     * @param defaultValue
     * */
    public static long getLong(@NonNull String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    /**
     * Get float.
     * @param key
     * @param defaultValue
     * */
    public static float getFloat(@NonNull String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    /**
     * Get string set.
     * @param key
     * @param defaultValue
     * */
    public static Set<String> getStringSet(@NonNull String key, Set<String> defaultValue) {
        return sharedPreferences.getStringSet(key, defaultValue);
    }

    /**
     * Get list string.
     * @param key
     * */
    private static List<String> getListString(@NonNull String key) {
        String value = getString(key, null);

        if (value != null) {
            return new ArrayList<String>(
                    Arrays.asList(TextUtils.split(value, REGEX_STRING_LIST)));
        }

        return null;
    }

    /**
     * Get list object.
     * @param key
     * @param aClass
     * @param defaultValue
     * */
    public static List<?> getListObject(@NonNull String key, @NonNull Class<?> aClass, List<?> defaultValue) {
        Gson gson = new Gson();
        List<String> strings = getListString(key);
        List<Object> objects = new ArrayList<Object>();

        if (strings == null) return defaultValue;

        for (String s : strings) {
            Object value = gson.fromJson(s, aClass);
            objects.add(value);
        }
        return objects;
    }

    /**
     * Get all.
     * */
    public static Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }

    /**
     * Contains.
     * @param key
     * */
    public static boolean contains(@NonNull String key) {
        return sharedPreferences.contains(key);
    }

    /**
     * Remove.
     * @param key
     * */
    public static void remove(@NonNull String key) {
        SharedPreferences.Editor editor = getEditor(sharedPreferences);
        if (sharedPreferences.contains(key + LENGTH)) {
            // Workaround for pre-HC's lack of StringSets
            int stringSetLength = sharedPreferences.getInt(key + LENGTH, -1);
            if (stringSetLength >= 0) {
                editor.remove(key + LENGTH);
                for (int i = 0; i < stringSetLength; i++) {
                    editor.remove(key + "[" + i + "]");
                }
            }
        }
        editor.remove(key).apply();
    }

    /**
     * Clear
     * */
    public static void clear() {
        getEditor(sharedPreferences).clear().apply();
    }

    /**
     * Get editor.
     * @param preferences
     * */
    public static SharedPreferences.Editor getEditor(SharedPreferences preferences) {
        return preferences.edit();
    }

    /**
     * Get shared preferences.
     * */
    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    /**
     * Register on prefs change listener.
     * @param prefsListener
     * */
    public static void registerOnPrefsChangeListener(@NonNull final PrefsListener prefsListener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                prefsListener.onPrefsChanged(sharedPreferences, key);
            }
        });
    }

    /**
     * Unregister on prefs change listener.
     * @param prefsListener
     * */
    public static void unregisterOnPrefsChangeListener(@NonNull final PrefsListener prefsListener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                prefsListener.onPrefsChanged(sharedPreferences, key);
            }
        });
    }
}