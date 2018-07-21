package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.mpatric.mp3agic.ID3v2;

import io.Overview;

public class TagEditor {
	
	private static int[] getPlacesFromFile(File file) {
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
	
	private static int[] getPlaces() {
		return getPlacesFromFile(new File(Overview.getSetting(Overview.PLACES)));
	}

	public static int getPlaceFromTrackNumber(int trackNumber) {
		return (getPlaces().length + 1) - (trackNumber - 1) / 10;
	}
	
	public static int getTrackNumberFromPlace(int place) {
		return 10 * (getPlaces().length + 1 - place) + 1;
	}
	
	public static boolean updateTagPlace(ID3v2 id3v2Tag) {
		int track = Integer.parseInt(id3v2Tag.getTrack());
		if (track % 10 == 1) {
			int place = getPlaces()[getPlaceFromTrackNumber(track) - 1];
			if (place != 0) {
				id3v2Tag.setTrack("" + getTrackNumberFromPlace(place));
				return true;
			}
		} else {
			int place = getPlaces()[0];
			id3v2Tag.setTrack("" + (10 * (getPlaces().length - place) + 9));
			return true;
		}
		return false;
	}
	
	public static boolean updateTagPlace(ID3v2 id3v2Tag, int place) {
		if (place > 0 && place <= 40) {
			id3v2Tag.setTrack("" + getTrackNumberFromPlace(place));
			return true;
		}
		return false;
	}
	
	public static boolean updateTagTrackNumber(ID3v2 id3v2Tag, int trackNumber) {
		id3v2Tag.setTrack("" + trackNumber);
		return true;
	}

	public static boolean lastRelease(ID3v2 id3v2Tag, int release) {
		int track = Integer.parseInt(id3v2Tag.getTrack());
		if (track % 10 == 1) {
			int place = getPlaces()[getPlaceFromTrackNumber(track) - 1];
			if (place == 0) {
				id3v2Tag.setTrack("" + (1000 * release + Integer.parseInt(id3v2Tag.getTrack())));
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean setValues(ID3v2 id3v2Tag, int trackNumber, String track, String[] artists, String album) {
		if (trackNumber != 0) {
			id3v2Tag.setTrack("" + trackNumber);
		}
		if (!track.isEmpty()) {
			id3v2Tag.setTitle(track);
		}
		if (!artists[0].isEmpty()) {
			String artistValue = artists[0];
			for (int i = 1; i < artists.length; i++) {
				artistValue += "/" + artists[i];
			}
			id3v2Tag.setArtist(artistValue);
		}
		if (!album.isEmpty()) {
			id3v2Tag.setAlbum(album);
		}
		return true;
	}
}
