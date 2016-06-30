package com.eggheadgames.soundpool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;
import java.util.Map;

public final class SoundUtil {

    private Map<Integer, Integer> cachedInitializedSounds;

    private SoundPool soundPool;

    /**
     * Call this method at the beginning of app to restore old state of SoundPool
     *
     * @param context
     */
    public void init(Context context) {
        enableSound(context, SoundPreferences.isSoundEnabled(context));
    }

    /**
     * Call this method to change SoundPool state
     *
     * @param context
     */
    public void toogle(Context context) {
        enableSound(context, !SoundPreferences.isSoundEnabled(context));
    }

    /**
     * @param context context to get AudioManager
     * @param enable  toggle state sound off\on
     */
    @SuppressLint("UseSparseArrays")
    @SuppressWarnings("deprecation")
    private void enableSound(Context context, boolean enable) {
        SoundPreferences.setSoundEnableState(context, enable);

        if (enable) {
            if (soundPool == null) {
                soundPool = new SoundPool(9, AudioManager.STREAM_MUSIC, 0);
                cachedInitializedSounds = new HashMap<>();

                final Context applicationContext = context.getApplicationContext();

                cachedInitializedSounds.put(SoundConstants.SOUND_CLEAR_SQUARE, soundPool.load(applicationContext, R.raw.clear_square, 1));
                cachedInitializedSounds.put(SoundConstants.SOUND_ENTER_LETTER, soundPool.load(applicationContext, R.raw.enter_letter, 1));
                cachedInitializedSounds.put(SoundConstants.SOUND_UNDO, soundPool.load(applicationContext, R.raw.undo, 1));
                cachedInitializedSounds.put(SoundConstants.SOUND_ERROR, soundPool.load(applicationContext, R.raw.error, 1));
                cachedInitializedSounds.put(SoundConstants.SOUND_SELECT_SQUARE, soundPool.load(applicationContext, R.raw.select_square, 1));
                cachedInitializedSounds.put(SoundConstants.SOUND_SELECT_CATEGORY_OR_PUZZLE, soundPool.load(applicationContext, R.raw.select_category_or_puzzle, 1));
                cachedInitializedSounds.put(SoundConstants.SOUND_SUCCESS, soundPool.load(applicationContext, R.raw.success, 1));
                cachedInitializedSounds.put(SoundConstants.SOUND_ENTER_MORE, soundPool.load(applicationContext, R.raw.o, 1));
            }
        } else {
            disableSound();
        }
    }

    public void disableSound() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }

        if (cachedInitializedSounds != null) {
            cachedInitializedSounds.clear();
            cachedInitializedSounds = null;
        }
    }

    /**
     * Play an already prepared sound sample
     *
     * @param context this is context
     * @param id      sound id
     */
    public void playSound(Context context, int id) {
        if (soundPool == null || cachedInitializedSounds == null || !cachedInitializedSounds.containsKey(id)) {
            return;
        }

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume / maxVolume;

        soundPool.play(cachedInitializedSounds.get(id), volume, volume, 1, 0, 1f);
    }
}