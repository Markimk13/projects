package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;

public class Overview {
	
	public static final String HOME_PATH = "C:\\Users\\UK\\";
	public static final String IN_DIR = HOME_PATH + "Dropbox\\Hobbys\\Musik\\Charts\\";
	public static final String OUT_DIR = HOME_PATH + "Dropbox\\Hobbys\\Musik\\Next\\";
	
	public static final int[] PLACES = getPlacesFromFile(new File(HOME_PATH + "Dropbox\\Hobbys\\Meine Charts\\places.txt"));
	
	public static void main(String[] args) throws Exception {
		updateCharts();
	}
	
	public static void updateCharts() throws Exception {
		File inputDir = new File(IN_DIR);
		if (inputDir.isDirectory()) {
			File[] files = inputDir.listFiles();
			int fileAmount = files.length;
			for (int i = 0; i < fileAmount; i++) {
				String name = files[i].getName();
				updateMp3(name);
				System.out.println("Updated file \"" + name + "\" (" + (i+1) + "/" + fileAmount + ")");
			}
		}
	}
	
	public static int[] getPlacesFromFile(File file) {
		int[] places = null;
		
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			
			String[] toks = in.readLine().split("\\t");
			places = new int[toks.length];
			for (int i = 0; i < toks.length; i++) {
				places[i] = Integer.parseInt(toks[i]);
			}
			
			in.close();
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		return places;
	}
	
	public static int getPlaceFromTrackNumber(int trackNumber) {
		return (PLACES.length + 1) - (trackNumber - 1) / 10;
	}
	
	public static int getTrackNumberFromPlace(int place) {
		return 10 * (PLACES.length + 1 - place) + 1;
	}
	
	public static boolean updateTag(ID3v2 id3v2Tag) {
		int track = Integer.parseInt(id3v2Tag.getTrack());
		if (track % 10 == 1) {
			int place = PLACES[getPlaceFromTrackNumber(track) - 1];
			if (place != 0) {
				id3v2Tag.setTrack("" + getTrackNumberFromPlace(place));
				return true;
			}
		} else {
			int place = PLACES[0];
			id3v2Tag.setTrack("" + (10 * (PLACES.length - place) + 9));
			return true;
		}
		return false;
	}
	
	public static boolean updateTag(ID3v2 id3v2Tag, int place) {
		if (place > 0 && place <= 40) {
			id3v2Tag.setTrack("" + getTrackNumberFromPlace(place));
			return true;
		}
		return false;
	}
	
	public static void updateMp3(String name) throws Exception {
		String fileName = IN_DIR + name;
		String newFileName = OUT_DIR + name;
		Mp3File mp3file = new Mp3File(fileName);
		
		if (mp3file.hasId3v2Tag()) {
			ID3v2 id3v2Tag = mp3file.getId3v2Tag();
			if (updateTag(id3v2Tag)) {
				mp3file.save(newFileName);
			}
		}
	}
	
	public static void updateMp3(String name, int place) throws Exception {
		String fileName = IN_DIR + name;
		String newFileName = OUT_DIR + name;
		Mp3File mp3file = new Mp3File(fileName);
		
		if (mp3file.hasId3v2Tag()) {
			ID3v2 id3v2Tag = mp3file.getId3v2Tag();
			if (updateTag(id3v2Tag, place)) {
				mp3file.save(newFileName);
			}
		}
	}
}
