package com.esaych.world.aspects;

import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.esaych.io.AssetLoader;
import com.esaych.io.menus.Options;

public class Music {
	public static float runTime = 0;
	private static MusicState state;
	private static boolean queued = false;
	private static MusicCompletionListener listener = new MusicCompletionListener();
	
	private enum MusicState {
		INTRO, TITLE, GAME, LOOPQUEUE, LOOP, PAUSE, GAMEOVER, EFFECTQUEUE
	}
	
	public static void incrementRunTime(float time) { //Update Method
		//System.out.println(state + " : " + runTime);
		runTime += time;
		if (queued == true && Options.getVolumes()[0])
			switch (state) {
			case INTRO:
				break;
			case TITLE:
				if (runTime % (loopAmount()) < 0.05) {
					resetRunTime();
					stop();
					AssetLoader.intro_music.play();
					AssetLoader.intro_music.setLooping(true);
					AssetLoader.intro_music.setVolume(.5f);
					queued = false;
				}
				break;
			case GAME:
				if (AssetLoader.intro_music.getVolume() == .5f) {
					if (runTime % loopAmount() < 0.05) { // wait till can start music
						resetRunTime();
						volume = .4f;
						AssetLoader.intro_music.setVolume(volume);
						AssetLoader.start_effect.play();
					}
				} else { // decreases volume of original intro loop AFTER start_music has started
					if (runTime > loopAmount() * 2.5 && runTime % loopAmount() < 0.05 && !AssetLoader.start_music.isPlaying()) {
						AssetLoader.start_music.play();
						AssetLoader.start_music.setVolume(.5f);
					}
					volume -= time/4;
					if (volume < 0) {
						volume = 0;
						AssetLoader.intro_music.stop();
						state = MusicState.LOOPQUEUE;
					} else {
						AssetLoader.intro_music.setVolume(volume);
					}
				}
				break;
			case LOOPQUEUE:
				if (runTime > loopAmount() * 2.5 && runTime % loopAmount() < 0.05 && !AssetLoader.start_music.isPlaying()) {
					AssetLoader.start_music.play();
					AssetLoader.start_music.setVolume(.5f);
				}
				if (runTime > 12.1) { // when the start music is ready
					AssetLoader.loop_effect.play();
					state = MusicState.LOOP;
				}
				break;
			case LOOP:
				if (runTime > 13.1) {
					AssetLoader.start_music.stop();
					AssetLoader.loop_music.stop();
					AssetLoader.loop_music.play();
                    if (!listener.isSet) {
                        AssetLoader.loop_music.setOnCompletionListener(listener);
                        listener.isSet = true;
                    }
					AssetLoader.loop_music.setVolume(.5f);
					state = MusicState.EFFECTQUEUE;
					resetRunTime();
				}
				break;
			case EFFECTQUEUE: {
				if (runTime > 60.1) {
					AssetLoader.loop_effect.play();
					queued = false;
				}
				break;
			}
			case PAUSE: 
				volume -= time/4;
				AssetLoader.start_music.setVolume(volume);
				AssetLoader.loop_music.setVolume(volume);
				if (volume < 0) {
					volume = 0;
					queued = false;
					stop();
                    AssetLoader.start_music.setVolume(.5f);
                    AssetLoader.loop_music.setVolume(.5f);
				}
				break;
			case GAMEOVER:
				break;
			default:
				break;
			}
	}
	
	public static void playIntro() { //Called when start menu is loaded
		stop();
		if (Options.getVolumes()[0]) {
			AssetLoader.intro_music.play();
			AssetLoader.intro_music.setLooping(true);
			AssetLoader.intro_music.setVolume(.5f);
		}
		resetRunTime();
		state = MusicState.INTRO;
	}
	
	public static void playGameMusic() { //Called when user hits start/resume
		if (state != MusicState.INTRO) {
            stop();
            resetRunTime();
            if (Options.getVolumes()[0])
                AssetLoader.start_effect.play();
			state = MusicState.LOOPQUEUE;
		} else {
			state = MusicState.GAME;
		}
		queued = true;
	}
	
	public static void playEndMusic() { //Called when user loses last life
		if (Options.getVolumes()[0]) {
			resetRunTime();
			stop();
			AssetLoader.outro_music.play();
			AssetLoader.outro_music.setVolume(.5f);
		}
		state = MusicState.GAMEOVER;
	}
	
	private static float volume;
	
	public static void playPauseMusic() {
		volume = .5f;
		state = MusicState.PAUSE;
		queued = true;
	}
	
	public static void stop() {
		AssetLoader.intro_music.stop();
		AssetLoader.start_music.stop();
		AssetLoader.loop_music.stop();
		AssetLoader.outro_music.stop();
	}
	
	private static void resetRunTime() {
		runTime = 0;
	}
	
	private static float loopAmount() {
		return (float) (60/160.2);
	}

	public static void complete() {
		runTime = 14;
		state = MusicState.LOOP;
		queued = true;
		System.out.println("Complete()");
	}
}

class MusicCompletionListener implements OnCompletionListener {

    public boolean isSet;

	@Override
	public void onCompletion(com.badlogic.gdx.audio.Music music) {
		Music.complete();
	}
	
}