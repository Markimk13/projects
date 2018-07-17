package core;

import java.io.File;
import java.io.IOException;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

import data.Song;

public class Overview {

	public static final String HOME_PATH = "C:\\Users\\UK\\";
	public static final String IN_DIR = HOME_PATH + "Dropbox\\Hobbys\\Musik\\Charts\\";
	public static final String OUT_DIR = HOME_PATH + "Dropbox\\Hobbys\\Musik\\Next\\";
	public static final String ALBUM = "MyCharts";
	
	public static void main(String[] args) {
		// updateCharts();
		// lastReleases(122);
		// editSongs();
	}
	
	private static void editSongs() {
		try {
			Song.setValues("Nexeri ft. Jex - Games (Lyric Video).mp3", 256, "Games", "Nexeri; Jex");
			Song.setValues("Sigrid - High Five (Lyric Video).mp3", 306, "High Five", "Sigrid");
			Song.updateMp3Manual("Prismo - Breathe (Official Lyric Video).mp3", 156);
			Song.updateMp3Manual("Disclosure - Ultimatum (Audio) ft. Fatoumata Diawara.mp3", 206);
			Song.updateMp3Manual("Amadeus - Goodbye.mp3", 6);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void updateCharts() {
		File inputDir = new File(IN_DIR);
		if (inputDir.isDirectory()) {
			File[] files = inputDir.listFiles();
			int fileAmount = files.length;
			for (int i = 0; i < fileAmount; i++) {
				String name = files[i].getName();
				try {
					Song.updateMp3(name);
					System.out.println("(success) Updated file \"" + name + "\" (" + (i+1) + "/" + fileAmount + ")");
					
				} catch (UnsupportedTagException | InvalidDataException | NotSupportedException | IOException utidnsioe) {
					System.out.println("(failure) Updated file \"" + name + "\" (" + (i+1) + "/" + fileAmount + ")");
					utidnsioe.printStackTrace();
				}
			}
		}
	}
	
	public static void lastReleases(int release) {
		File inputDir = new File(IN_DIR);
		if (inputDir.isDirectory()) {
			File[] files = inputDir.listFiles();
			int fileAmount = files.length;
			for (int i = 0; i < fileAmount; i++) {
				String name = files[i].getName();
				try {
					Song.lastRelease(name, release);
					System.out.println("(success) Updated file \"" + name + "\" (" + (i+1) + "/" + fileAmount + ")");
					
				} catch (UnsupportedTagException | InvalidDataException | NotSupportedException | IOException utidnsioe) {
					System.out.println("(failure) Updated file \"" + name + "\" (" + (i+1) + "/" + fileAmount + ")");
					utidnsioe.printStackTrace();
				}
			}
		}
	}
}
