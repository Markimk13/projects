package data;

import java.io.IOException;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

import core.Overview;
import io.TagEditor;

public class Song {
	
	public static void updateMp3(String name) throws UnsupportedTagException, InvalidDataException, IOException, NotSupportedException {
		String fileName = Overview.IN_DIR + name;
		String newFileName = Overview.OUT_DIR + name;
		Mp3File mp3file = new Mp3File(fileName);
		
		if (mp3file.hasId3v2Tag()) {
			ID3v2 id3v2Tag = mp3file.getId3v2Tag();
			if (TagEditor.updateTagPlace(id3v2Tag)) {
				mp3file.save(newFileName);
			}
		}
	}
	
	public static void updateMp3(String name, int place) throws UnsupportedTagException, InvalidDataException, IOException, NotSupportedException {
		String fileName = Overview.IN_DIR + name;
		String newFileName = Overview.OUT_DIR + name;
		Mp3File mp3file = new Mp3File(fileName);
		
		if (mp3file.hasId3v2Tag()) {
			ID3v2 id3v2Tag = mp3file.getId3v2Tag();
			if (TagEditor.updateTagPlace(id3v2Tag, place)) {
				mp3file.save(newFileName);
			}
		}
	}
	
	public static void updateMp3Manual(String name, int trackNumber) throws UnsupportedTagException, InvalidDataException, IOException, NotSupportedException {
		String fileName = Overview.IN_DIR + name;
		String newFileName = Overview.OUT_DIR + name;
		Mp3File mp3file = new Mp3File(fileName);
		
		if (mp3file.hasId3v2Tag()) {
			ID3v2 id3v2Tag = mp3file.getId3v2Tag();
			if (TagEditor.updateTagTrackNumber(id3v2Tag, trackNumber)) {
				mp3file.save(newFileName);
			}
		}
	}
	
	public static void lastRelease(String name, int release) throws UnsupportedTagException, InvalidDataException, IOException, NotSupportedException {
		String fileName = Overview.IN_DIR + name;
		String newFileName = Overview.OUT_DIR + name;
		Mp3File mp3file = new Mp3File(fileName);
		
		if (mp3file.hasId3v2Tag()) {
			ID3v2 id3v2Tag = mp3file.getId3v2Tag();
			if (TagEditor.lastRelease(id3v2Tag, release)) {
				mp3file.save(newFileName);
			}
		}
	}
	
	public static void setValues(String name, int trackNumber, String track, String artists) throws UnsupportedTagException, InvalidDataException, IOException, NotSupportedException {
		String fileName = Overview.IN_DIR + name;
		String newFileName = Overview.OUT_DIR + name;
		Mp3File mp3file = new Mp3File(fileName);
		
		if (mp3file.hasId3v2Tag()) {
			ID3v2 id3v2Tag = mp3file.getId3v2Tag();
			if (TagEditor.setValues(id3v2Tag, trackNumber, track, artists, Overview.ALBUM)) {
				mp3file.save(newFileName);
			}
		}
	}
}
