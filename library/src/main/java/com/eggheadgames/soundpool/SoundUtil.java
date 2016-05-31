package com.eggheadgames.soundpool;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;
import java.util.Map;

public final class SoundUtil {

    private Map<Integer, Integer> predefinedSoundsKeys;
    private SoundPool soundPool;

    private Map<Integer, Integer> cachedInitializedSounds;
    private Context mContext;

    /**
     * @param context context to get AudioManager
     * @param enable  toggle state sound off\on
     */
    @SuppressWarnings("deprecation")
    public void enableSound(Context context, boolean enable) {
        if (enable) {
            mContext = context;
            soundPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
            cachedInitializedSounds = new HashMap<>();
            predefinedSoundsKeys = new HashMap<>();

            add(SoundConstants.SOUND_CLEAR_SQUARE, R.raw.clear_square);
            add(SoundConstants.SOUND_ENTER_LETTER, R.raw.enter_letter);
            add(SoundConstants.SOUND_UNDO, R.raw.undo);
            add(SoundConstants.SOUND_ERROR, R.raw.error);
            add(SoundConstants.SOUND_SELECT_SQUARE, R.raw.select_square);
            add(SoundConstants.SOUND_SELECT_CATEGORY_OR_PUZZLE, R.raw.select_category_or_puzzle);
            add(SoundConstants.SOUND_SUCCESS, R.raw.success);

            for (Map.Entry<Integer, Integer> entry : predefinedSoundsKeys.entrySet()) {
                cachedInitializedSounds.put(entry.getKey(), soundPool.load(context.getApplicationContext(),
                        entry.getValue(), 1));
            }
        } else {
            disableSound();
        }
    }

    public void disableSound() {
        mContext = null;
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }

        if (cachedInitializedSounds != null) {
            cachedInitializedSounds.clear();
            cachedInitializedSounds = null;
        }

        if (predefinedSoundsKeys != null) {
            predefinedSoundsKeys.clear();
            predefinedSoundsKeys = null;
        }
    }

    /**
     * Function to add new sound to predefined list. Need to call init after new item or list of items was added.
     *
     * @param soundKey        - int id of sound. please do not use 0-6, they are predefined.
     * @param soundResourceId - sound resource id
     */
    public void add(int soundKey, int soundResourceId) {
        if (!predefinedSoundsKeys.containsKey(soundKey)) {
            predefinedSoundsKeys.put(soundKey, soundResourceId);
        }
    }

    /**
     * Play an already prepared sound sample
     *
     * @param id - sound id
     */
    public void playSound(int id) {
        if (soundPool == null || cachedInitializedSounds == null || !cachedInitializedSounds.containsKey(id)) {
            return;
        }

        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume / maxVolume;

        soundPool.play(cachedInitializedSounds.get(id), volume, volume, 1, 0, 1f);
    }
}