package io;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import data.Song;

public class Overview {

	public static final String IN_DIR = "input";
	public static final String OUT_DIR = "output";
	public static final String NEW_DIR = "new";
	public static final String ARCHIVE_DIR = "archive";
	public static final String PLACES = "places";
	public static final String ALBUM = "MyCharts";

	private static final JFrame FRAME = new JFrame("My Music Charts");
	private static final JTextArea LOG = new JTextArea();
	
	public static void main(String[] args) {
		FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FRAME.setVisible(true);
		FRAME.setMinimumSize(new Dimension(800, 600));
		
		JPanel contentPanel = new JPanel();
		FRAME.add(contentPanel);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		
		JPanel settingsPanel = new JPanel();
		contentPanel.add(settingsPanel);
		settingsPanel.setBorder(BorderFactory.createTitledBorder("Settings:"));
		settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
		addFileChooser(IN_DIR, settingsPanel, "Input dir:", true);
		
		JPanel addPanel = new JPanel();
		contentPanel.add(addPanel);
		addPanel.setBorder(BorderFactory.createTitledBorder("Add new song:"));
		addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));
		
		addFileChooser(NEW_DIR, addPanel, "Output dir new song:", true);
		JFileChooser songFileChooser = addFileChooser(null, addPanel, "Song:", false);
		JTextField titleField = addTextField(addPanel, "Title:");
		JTextField trackNumberField = addTextField(addPanel, "Track number:");
		JTextField artistsField = addTextField(addPanel, "Artists:");
		JButton addButton = new JButton("add song");
		addPanel.add(addButton);
		addButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				File songFile = songFileChooser.getSelectedFile();
				if (songFile != null) {
					String songPath = songFile.getPath();
					try {
						String trackNumberString = trackNumberField.getText();
						int trackNumber = !trackNumberString.isEmpty() ? Integer.parseInt(trackNumberString) : 0;
						Song.setValues(songPath, trackNumber, titleField.getText(), artistsField.getText().split("/"));
						log("(success) Added song \"" + songPath + "\"");
					} catch (Exception ex) {
						log("(failure) Couldn't add song \"" + songPath + "\": " + ex.getMessage());
						ex.printStackTrace();
					}
				} else {
					log("No song selected to add.");
				}
			}
		});
		
		JPanel updatePanel = new JPanel();
		contentPanel.add(updatePanel);
		updatePanel.setBorder(BorderFactory.createTitledBorder("Update Charts:"));
		updatePanel.setLayout(new BoxLayout(updatePanel, BoxLayout.Y_AXIS));

		addFileChooser(OUT_DIR, updatePanel, "Output dir charts:", true);
		addFileChooser(ARCHIVE_DIR, updatePanel, "Archive dir:", true);
		addFileChooser(PLACES, updatePanel, "Places file:", false);
		JTextField release = addTextField(updatePanel, "New Release Nr.");
		JButton updateButton = new JButton("update");
		updatePanel.add(updateButton);
		updateButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				updateCharts();
				lastReleases(Integer.parseInt(release.getText()) - 1);
			}
		});
		
		JPanel logPanel = new JPanel(new GridBagLayout());
		contentPanel.add(logPanel);
		logPanel.setBorder(BorderFactory.createTitledBorder("Log:"));
		int width = 700;
		int height = 150;
		Dimension panelSize = new Dimension(width, height + 20);
		logPanel.setMinimumSize(panelSize);
		logPanel.setPreferredSize(panelSize);
		JScrollPane jsp = new JScrollPane(LOG);
		Dimension jspSize = new Dimension(width, height);
		jsp.setMinimumSize(jspSize);
		jsp.setPreferredSize(jspSize);
		jsp.setMaximumSize(jspSize);
		jsp.setVisible(true);
		logPanel.add(jsp);
		FRAME.pack();
		
		LOG.setEditable(false);
		clearLogFile();
	}
	
	private static void log(String text) {
		String logText = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(System.currentTimeMillis())) + " - " + text + System.getProperty("line.separator");
		LOG.append(logText);
		FRAME.pack();
		try {
			File log = new File("data" + File.separator + "log.txt");
			FileWriter out = new FileWriter(log, true);
			out.write(logText);
			out.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	private static void clearLogFile() {
		try {
			File log = new File("data" + File.separator + "log.txt");
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(log), "UTF-8"));
			out.write("");
			out.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	private static JFileChooser addFileChooser(String id, JPanel parent, String name, boolean useDirectories) {
		JPanel chooserPanel = new JPanel(new BorderLayout());
		parent.add(chooserPanel);
		boolean saveSettings = id != null;
		JFileChooser fileChooser = saveSettings ? new JFileChooser() : new JFileChooser(getSetting(IN_DIR));
		chooserPanel.add(new JLabel(name), BorderLayout.WEST);
		JTextField textField = new JTextField();
		chooserPanel.add(textField, BorderLayout.CENTER);

		if (useDirectories) {
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
			
		if (saveSettings) {
			File settingFile = new File("data" + File.separator + id + ".txt");
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(settingFile), "UTF-8"));
				String path = in.readLine();
				in.close();
				textField.setText(path);
				fileChooser.setCurrentDirectory(new File(path).getParentFile());
			} catch (IOException ioe) {
				log("Loading settings failed: " + ioe.getMessage());
			}
		}
		
		
		JButton openButton = new JButton("Browse");
		chooserPanel.add(openButton, BorderLayout.EAST);
		openButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fileChooser.showOpenDialog(FRAME);
				File file = fileChooser.getSelectedFile();
				if (saveSettings) {
					if (file != null) {
						try {
							File settingFile = new File("data" + File.separator + id + ".txt");
							BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(settingFile), "UTF-8"));
							out.write(file.getPath());
							out.close();
						} catch (IOException ioe) {
							log("Saving settings failed: " + ioe.getMessage());
						}
					} else {
						file = null;
						fileChooser.setSelectedFile(null);
					}
				}
				if (file != null) {
					textField.setText(file.getPath());
				}
			}
		});
		
		return fileChooser;
	}
	
	public static String getSetting(String id) {
		String settingsContent = null;
		File settingFile = new File("data" + File.separator + id + ".txt");
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(settingFile), "UTF-8"));
			settingsContent = in.readLine();
			if (new File(settingsContent).isDirectory()) {
				settingsContent += File.separator;
			}
			in.close();
		} catch (IOException ioe) {
			log("Loading settings failed: " + ioe.getMessage());
		}
		
		return settingsContent;
	}
	
	private static JTextField addTextField(JPanel parent, String name) {
		JPanel chooserPanel = new JPanel(new BorderLayout());
		parent.add(chooserPanel);
		chooserPanel.add(new JLabel(name), BorderLayout.WEST);
		JTextField textField = new JTextField();
		chooserPanel.add(textField, BorderLayout.CENTER);
		
		return textField;
	}
	
	public static void updateCharts() {
		File inputDir = new File(getSetting(IN_DIR));
		if (inputDir.isDirectory()) {
			File[] files = inputDir.listFiles();
			int filesAdded = 0;
			for (int i = 0; i < files.length; i++) {
				String name = files[i].getName();
				try {
					filesAdded += Song.updateMp3(name) ? 1 : 0;
					log("(success) Updated file \"" + name + "\" (" + filesAdded + "/" + (i+1) + ")");
					
				} catch (Exception e) {
					log("(failure) Updated file \"" + name + "\" (" + filesAdded + "/" + (i+1) + "): \"" + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void lastReleases(int release) {
		File inputDir = new File(getSetting(IN_DIR));
		if (inputDir.isDirectory()) {
			File[] files = inputDir.listFiles();
			int filesAdded = 0;
			for (int i = 0; i < files.length; i++) {
				String name = files[i].getName();
				try {
					filesAdded += Song.lastRelease(name, release) ? 1 : 0;
					log("(success) Updated file \"" + name + "\" (" + filesAdded + "/" + (i+1) + ")");
					
				} catch (Exception e) {
					log("(failure) Updated file \"" + name + "\" (" + filesAdded + "/" + (i+1) + "): \"" + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
}
