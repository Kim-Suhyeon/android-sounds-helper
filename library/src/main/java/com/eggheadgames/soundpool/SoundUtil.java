package com.eggheadgames.soundpool;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

public final class SoundUtil {

    private SparseIntArray cachedInitializedSounds;
    private SoundPool soundPool;

    /**
     * Call this method at the beginning of app to restore old state of SoundPool
     */
    public void init(Activity activity) {
        setSoundEnabled(activity, SoundPreferences.isSoundEnabled(activity));
    }

    /**
     * @param activity activity to get AudioManager and set Volume Control
     * @param enabled true to turn sound on
     */
    @SuppressWarnings("deprecation")
    public void setSoundEnabled(Activity activity, boolean enabled) {
        SoundPreferences.setSoundEnableState(activity, enabled);

        if (enabled) {
            activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
            if (soundPool == null) {
                soundPool = new SoundPool(9, AudioManager.STREAM_MUSIC, 0);
                cachedInitializedSounds = new SparseIntArray();

                final Context applicationContext = activity.getApplicationContext();

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
            activity.setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            disableSound();
        }
    }

    public boolean isSoundEnabled(Context context) {
        return SoundPreferences.isSoundEnabled(context);
    }

    /**
     * Play an already prepared sound sample
     *
     * @param context this is context
     * @param id      sound id
     */
    public void playSound(Context context, int id) {
        if (soundPool == null || cachedInitializedSounds == null || cachedInitializedSounds.indexOfKey(id) < 0) {
            return;
        }

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume / maxVolume;

        soundPool.play(cachedInitializedSounds.get(id), volume, volume, 1, 0, 1f);
    }

    private void disableSound() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }

        if (cachedInitializedSounds != null) {
            cachedInitializedSounds.clear();
            cachedInitializedSounds = null;
        }
    }
}