package com.eggheadgames.soundpool;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;
import java.util.Map;

public class SoundUtil {

    private static Map<Integer, Integer> predefinedSoundsKeys = new HashMap<>();
    private static SoundPool soundPool = null;

    private static Map<Integer, Integer> cachedInitializedSounds = new HashMap<>();

    public static void init(Activity context, boolean enabled) {
        if (enabled) {
            if (soundPool != null) {
                return; //Already set up. Nothing more to do here.
            }

            soundPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
            context.setVolumeControlStream(AudioManager.STREAM_MUSIC);

            if (cachedInitializedSounds == null) {
                cachedInitializedSounds = new HashMap<>();
            }

            for (Integer soundKey : predefinedSoundsKeys.keySet()) {
                cachedInitializedSounds.put(soundKey, soundPool.load(context, predefinedSoundsKeys.get(soundKey), 1));
            }

        } else {
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

    public static void add(int soundKey, int soundResourceId) {
        if (!predefinedSoundsKeys.containsKey(soundKey)) {
            predefinedSoundsKeys.put(soundKey, soundResourceId);
        }
    }

    //Play an already prepared sound sample
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
