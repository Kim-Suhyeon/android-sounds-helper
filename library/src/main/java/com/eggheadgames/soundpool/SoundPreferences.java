package com.eggheadgames.soundpool;

import android.content.Context;
import android.content.SharedPreferences;

public final class SoundPreferences {

    private static final String SOUND_UTIL_SHARED_PREFERENCES_NAME = "sound_util_shared_preferences";
    private static final String KEY_SOUND_STATE = "sound_state";
    private static final boolean DEFAULT_SOUND_STATE = true;

    private SoundPreferences(){}

    public static void setSoundEnableState(Context context, boolean enabled) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SOUND_UTIL_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(KEY_SOUND_STATE, enabled).apply();
    }

    public static boolean isSoundEnabled(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SOUND_UTIL_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_SOUND_STATE, DEFAULT_SOUND_STATE);
    }


}
