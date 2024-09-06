package main;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

	Clip clip;
	URL soundURL[] = new URL[30];

	public Sound() {

		soundURL[0] = getClass().getResource("/sound/menu_selection_click.wav");
		soundURL[1] = getClass().getResource("/sound/coin.wav");
		soundURL[2] = getClass().getResource("/sound/beep.wav");
		soundURL[3] = getClass().getResource("/sound/unlock.wav");
		soundURL[4] = getClass().getResource("/sound/fanfare.wav");
		soundURL[5] = getClass().getResource("/sound/overworld.wav");
		soundURL[6] = getClass().getResource("/sound/title.wav");
		soundURL[7] = getClass().getResource("/sound/decline.wav");
		soundURL[8] = getClass().getResource("/sound/battle.wav");
		soundURL[9] = getClass().getResource("/sound/church.wav");
		soundURL[10] = getClass().getResource("/sound/town.wav");
		soundURL[11] = getClass().getResource("/sound/slash.wav");
		soundURL[12] = getClass().getResource("/sound/hurt.wav");
		soundURL[13] = getClass().getResource("/sound/game_over.wav");
		soundURL[14] = getClass().getResource("/sound/cursor.wav");
		soundURL[15] = getClass().getResource("/sound/heal.wav");
		soundURL[16] = getClass().getResource("/sound/denied.wav");
		soundURL[17] = getClass().getResource("/sound/equip.wav");
		soundURL[18] = getClass().getResource("/sound/encounter.wav");
		soundURL[19] = getClass().getResource("/sound/city3.wav");
		soundURL[20] = getClass().getResource("/sound/speak.wav");
		soundURL[21] = getClass().getResource("/sound/speak1.wav");
		soundURL[22] = getClass().getResource("/sound/dungeon.wav");
		soundURL[23] = getClass().getResource("/sound/mute.wav");
		soundURL[24] = getClass().getResource("/sound/boss.wav");
		soundURL[25] = getClass().getResource("/sound/opening.wav");
	}

	public void setFile(int i) {

		try {

			AudioInputStream ais = AudioSystem.	getAudioInputStream(soundURL[i]);
			clip = AudioSystem.getClip();
			clip.open(ais);

		}catch(Exception e) {
			e.printStackTrace();

		}

	}
	public void play() {

		clip.start();

	}
	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	public void stop() {

		clip.stop();

	}
}
