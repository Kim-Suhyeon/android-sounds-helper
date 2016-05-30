package com.eggheadgames.soundpool;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;
import java.util.Map;

public final class SoundUtil {

    private static Map<Integer, Integer> predefinedSoundsKeys = new HashMap<>();
    private static SoundPool soundPool = null;

    private static Map<Integer, Integer> cachedInitializedSounds;

    private SoundUtil() {
    }

    /**
     * Init sound pool
     *
     * @param activity
     */
    public static void init(Activity activity) {
        if (soundPool != null) {
            return;
        }

        soundPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        cachedInitializedSounds = new HashMap<>();

        add(SoundConstants.SOUND_CLEAR_SQUARE, R.raw.clear_square);
        add(SoundConstants.SOUND_ENTER_LETTER, R.raw.enter_letter);
        add(SoundConstants.SOUND_UNDO, R.raw.undo);
        add(SoundConstants.SOUND_ERROR, R.raw.error);
        add(SoundConstants.SOUND_SELECT_SQUARE, R.raw.select_square);
        add(SoundConstants.SOUND_SELECT_CATEGORY_OR_PUZZLE, R.raw.select_category_or_puzzle);
        add(SoundConstants.SOUND_SUCCESS, R.raw.success);

        for (Map.Entry<Integer, Integer> entry : predefinedSoundsKeys.entrySet()) {
            cachedInitializedSounds.put(entry.getKey(), soundPool.load(activity.getApplicationContext(),
                    entry.getValue(), 1));
        }
    }

    /**
     * Kill sound poll and clear cached sounds list.
     */
    @SuppressWarnings("PMD.NonThreadSafeSingleton")
    public static void kill() {
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
     * Toggle sound pool
     *
     * @param activity
     * @param enable   - true - activate, false - deactivate
     */
    public static void toggle(Activity activity, boolean enable) {
        if (enable) {
            init(activity);
        } else {
            kill();
        }
    }

    /**
     * Function to add new sound to predefined list. Need to call init after new item or list of items was added.
     *
     * @param soundKey        - int id of sound. please do not use 0-6, they are predefined.
     * @param soundResourceId - sound resource id
     */
    public static void add(int soundKey, int soundResourceId) {
        if (!predefinedSoundsKeys.containsKey(soundKey)) {
            predefinedSoundsKeys.put(soundKey, soundResourceId);
        }
    }

    /**
     * Play an already prepared sound sample
     *
     * @param context
     * @param id      - sound id
     */
    public static void playSound(Context context, int id) {
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