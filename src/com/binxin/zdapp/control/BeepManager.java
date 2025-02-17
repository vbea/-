package com.binxin.zdapp.control;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;

import com.binxin.zdapp.R;

public final class BeepManager {

	private static final String TAG = BeepManager.class.getSimpleName();

	private static final float BEEP_VOLUME = 0.50f;
	private static final long VIBRATE_DURATION = 200L;

	private final Activity activity;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private boolean vibrate;

	public BeepManager(Activity activity) {
		this.activity = activity;
		this.mediaPlayer = null;
		updatePrefs();
	}

	public void updatePrefs() {
		/* 配置修改 */
		SharedPreferences prefs = activity.getSharedPreferences("zdapp",Context.MODE_PRIVATE);
		playBeep = shouldBeep(prefs, activity);
		//vibrate = false;// 不震动
		vibrate = prefs.getBoolean("vibrate",true);
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = buildMediaPlayer(activity);
		}
	}

	public void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) activity
					.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	private static boolean shouldBeep(SharedPreferences prefs, Context activity) {
		/* 配置修改 */
		boolean shouldPlayBeep = prefs.getBoolean("beep",true);// 声音
		// boolean shouldPlayBeep = prefs.getBoolean(
		// PreferencesActivity.KEY_PLAY_BEEP, true);

		if (shouldPlayBeep) {
			// See if sound settings overrides this
			AudioManager audioService = (AudioManager) activity
					.getSystemService(Context.AUDIO_SERVICE);
			if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
				shouldPlayBeep = false;
			}
		}
		return shouldPlayBeep;
	}

	private static MediaPlayer buildMediaPlayer(Context activity) {
		MediaPlayer mediaPlayer = new MediaPlayer();
		//mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setAudioStreamType(AudioManager.MODE_RINGTONE);
		// When the beep has finished playing, rewind to queue up another one.
		mediaPlayer
				.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer player) {
						player.seekTo(0);
					}
				});

		AssetFileDescriptor file = activity.getResources().openRawResourceFd(
				R.raw.beep);
		try {
			mediaPlayer.setDataSource(file.getFileDescriptor(),
					file.getStartOffset(), file.getLength());
			file.close();
			mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
			mediaPlayer.prepare();
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			mediaPlayer = null;
		}
		return mediaPlayer;
	}
}
