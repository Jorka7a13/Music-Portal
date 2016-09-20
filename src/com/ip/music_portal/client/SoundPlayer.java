package com.ip.music_portal.client;

import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.allen_sauer.gwt.voices.client.handler.PlaybackCompleteEvent;
import com.allen_sauer.gwt.voices.client.handler.SoundHandler;
import com.allen_sauer.gwt.voices.client.handler.SoundLoadStateChangeEvent;

public class SoundPlayer {
	
	private SoundController soundController = new SoundController();
	private Sound sound;
	
	public void addURLToMp3(String url) {
		sound = soundController.createSound(Sound.MIME_TYPE_AUDIO_MPEG_MP3, url);
	}
	
	public void playSound() {
		sound.addEventHandler(new SoundHandler() {
			
			public void onPlaybackComplete(PlaybackCompleteEvent event) {}
			
			public void onSoundLoadStateChange(SoundLoadStateChangeEvent event) {
				sound.play();			
			}			
		});
	}
	
	public void stopSound() {
		sound.stop();
	}
}
