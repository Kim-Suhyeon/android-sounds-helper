package com.eggheadgames.soundpool;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;
import java.util.Map;

public class SoundUtil {

    private static Map<Integer, Integer> sounds;
    private static SoundPool soundPool = null;
    private static Activity context;

    public static void init(Activity context, boolean enabled) {
        SoundUtil.context = context;
        if (enabled) {
            if (soundPool != null) {
                return; //Already set up. Nothing more to do here.
            }

            soundPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
            sounds = new HashMap<>();

            soundPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);

            context.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        } else {
            if (soundPool != null) {
                soundPool.release();
                soundPool = null;
            }
            if (sounds != null) {
                sounds.clear();
                sounds = null;
            }
        }
    }

    public static void add(int soundId, int soundResourceId) {
        if (sounds != null && soundPool != null) {
            sounds.put(soundId, soundPool.load(context, soundResourceId, 1));
        }
    }

    //Play an already prepared sound sample
    public static void playSound(Context context, int id) {
        if (soundPool == null || sounds == null || !sounds.containsKey(id)) {
            return;
        }

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume / maxVolume;

        soundPool.play(sounds.get(id), volume, volume, 1, 0, 1f);
    }


}
