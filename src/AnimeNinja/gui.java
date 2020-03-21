package AnimeNinja;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Image;

import javax.swing.border.EmptyBorder;
import java.awt.TextField;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.net.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.Toolkit;

public class gui extends JFrame {

	TextField searchTextField = new TextField();
	JLabel imgLabel = new JLabel();
	JLabel lblNewLabel_6 = new JLabel("Similar Results");
	JComboBox seasonComboBox = new JComboBox();
	JComboBox similarComboBox = new JComboBox();
	DefaultComboBoxModel clearModel;
	JComboBox comboBox,comboBox_2,startComboBox,endComboBox,epComboBox;

	private JProgressBar progressBar = new JProgressBar();
	private   static JPanel contentPane;
	protected static JLabel lblNewLabel_3;
	protected static JLabel lblNewLabel_5;
	protected static JLabel lblNewLabel_7;
	protected static int selectedEpisode;

	private void search() {
		boolean exists = scraper.search(searchTextField.getText());
		if(exists)
		 {
			scraper.onlyOneSimilar = false;
			imgLabel.setIcon(null);
			if(scraper.onlyOneSimilar)
			{
				DefaultComboBoxModel model2 = new DefaultComboBoxModel(scraper.getSeasons().toArray(new String[0]));
				seasonComboBox.setModel(clearModel);
				seasonComboBox.setModel(model2);
			}
			else
			{
				DefaultComboBoxModel model2 = new DefaultComboBoxModel(scraper.getSeasons().toArray(new String[0]));
				seasonComboBox.setModel(clearModel);
				seasonComboBox.setModel(model2);
				if(scraper.showSimilar)
				{
					lblNewLabel_6.setVisible(true);
					similarComboBox.setVisible(true);
					contentPane.add(similarComboBox);
					DefaultComboBoxModel model3 = new DefaultComboBoxModel(scraper.similarTexts.toArray(new String[0]));
					similarComboBox.setModel(clearModel);
					similarComboBox.setModel(model3);
				}
				else
				{
					lblNewLabel_6.setVisible(false);
					similarComboBox.setVisible(false);
				}
			}
		 }
		 else
		 {
			 JOptionPane.showMessageDialog(null, "No result found\nPlease check your spelling", "Anime Ninja V1.0", JOptionPane.INFORMATION_MESSAGE);
		 }
	}

	private void seasonSelect() {
		int selectedSeason = comboBox.getSelectedIndex()-1;
		try {
			ImageIcon img = null;
			if(scraper.onlyOneSimilar)
			{
				img = new ImageIcon(scraper.getPhoto(selectedSeason, true).getPath());
				imgLabel.setIcon(img);
				scraper.selectSimilar(selectedSeason);
			}
			else
			{
				img = new ImageIcon(scraper.getPhoto(selectedSeason, false).getPath());
				imgLabel.setIcon(img);
				scraper.selectSeason(selectedSeason);
			}

			ArrayList<String> episodeNames = scraper.getEpisodes();
			DefaultComboBoxModel<String> second_model2 = new DefaultComboBoxModel<String>
			(episodeNames.toArray((new String[0])));
			 epComboBox.setModel(clearModel);
			 epComboBox.setModel(second_model2);

			 DefaultComboBoxModel<String> startModel = new DefaultComboBoxModel<String>(episodeNames.toArray((new String[0])));
			 DefaultComboBoxModel<String> endModel = new DefaultComboBoxModel<String>(episodeNames.toArray((new String[0])));
			 startComboBox.setModel(startModel);
			 endComboBox.setModel(endModel);
			 startComboBox.setSelectedIndex(1);
			 endComboBox.setSelectedIndex(endComboBox.getItemCount()-1);
			 startComboBox.setVisible(false);
			 endComboBox.setVisible(false);
			 startComboBox.setVisible(true);
			 endComboBox.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void similarSelect() {
		int selectedSimilar = comboBox_2.getSelectedIndex()-1;
		ImageIcon img = null;
		img = new ImageIcon(scraper.getPhoto(selectedSimilar, true).getPath());
		imgLabel.setIcon(img);
		scraper.selectSeason(selectedSimilar);
		scraper.selectSimilar(selectedSimilar);
		ArrayList<String> episodeNames = scraper.getEpisodes();
		DefaultComboBoxModel<String> second_model2 = new DefaultComboBoxModel<String>
		(episodeNames.toArray((new String[0])));
		 epComboBox.setModel(clearModel);
		 epComboBox.setModel(second_model2);

		 DefaultComboBoxModel<String> startModel = new DefaultComboBoxModel<String>(episodeNames.toArray((new String[0])));
		 DefaultComboBoxModel<String> endModel = new DefaultComboBoxModel<String>(episodeNames.toArray((new String[0])));
		 startComboBox.setModel(startModel);
		 endComboBox.setModel(endModel);
		 startComboBox.setSelectedIndex(1);
		 endComboBox.setSelectedIndex(endComboBox.getItemCount()-1);
		 startComboBox.setVisible(false);
		 endComboBox.setVisible(false);
		 startComboBox.setVisible(true);
		 endComboBox.setVisible(true);
	}

	public gui() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/appIcon.png")));
		setBackground(Color.DARK_GRAY);
		setTitle("Anime Ninja V1.0");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener((WindowListener) new WindowAdapter()
		{
		    public void windowClosing(WindowEvent e)
		    {
		    	int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit", "Exit",
		    								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		    	if(response == JOptionPane.YES_OPTION)
		    	{
		    		setVisible(false);
		    		dispose();
		    		scraper.close();
		    		downloader.close();
		    		System.exit(0);
		    	}
		    }
		});
		setSize(687, 520);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Anime Name");
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
		lblNewLabel.setBounds(27, 65, 133, 29);
		contentPane.add(lblNewLabel);

		clearModel = new DefaultComboBoxModel();
		seasonComboBox.setModel(clearModel);
		seasonComboBox.setFont(new Font("Tahoma", Font.BOLD, 10));
		seasonComboBox.setBounds(12, 186, 173, 22);
		contentPane.add(seasonComboBox);

		JLabel lblNewLabel_1 = new JLabel("Season");
		lblNewLabel_1.setForeground(Color.RED);
		lblNewLabel_1.setFont(new Font("SansSerif", Font.BOLD, 20));
		lblNewLabel_1.setBounds(27, 146, 89, 31);
		contentPane.add(lblNewLabel_1);

		epComboBox = new JComboBox();
		epComboBox.setModel(clearModel);
		epComboBox.setBounds(12, 283, 113, 22);
		contentPane.add(epComboBox);

		JLabel lblNewLabel_2 = new JLabel("Episode");
		lblNewLabel_2.setForeground(Color.RED);
		lblNewLabel_2.setFont(new Font("SansSerif", Font.BOLD, 20));
		lblNewLabel_2.setBounds(27, 246, 81, 29);
		contentPane.add(lblNewLabel_2);

		searchTextField.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
		searchTextField.setForeground(Color.BLACK);
		searchTextField.setBounds(12, 100, 148, 22);
		contentPane.add(searchTextField);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(Color.WHITE);
		menuBar.setBounds(0, 0, 681, 23);
		contentPane.add(menuBar);

		JMenu mnNewMenu = new JMenu("Options");
		mnNewMenu.setHorizontalAlignment(SwingConstants.RIGHT);
		mnNewMenu.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnNewMenu);

		JMenuItem mntmNewMenuItem = new JMenuItem("Check for updates");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(new URI("https://github.com/khalidwaleed0/Anime_Ninja/releases"));
				} catch (IOException | URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});
		mntmNewMenuItem.setHorizontalAlignment(SwingConstants.LEFT);

		mntmNewMenuItem.setFont(new Font("Segoe UI", Font.BOLD, 15));
		mnNewMenu.add(mntmNewMenuItem);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Contact us");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "Feel free to contact us at :\nkhalidwaleed0@outlook.com\nkemad951@gmail.com"
						, "Contact us", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mntmNewMenuItem_1.setFont(new Font("Segoe UI", Font.BOLD, 15));
		mnNewMenu.add(mntmNewMenuItem_1);

		JButton btnDownload = new JButton("Download" );
		btnDownload.setFont(new Font("SansSerif", Font.BOLD, 19));
		btnDownload.setBounds(234, 345, 128, 39);

		contentPane.add(btnDownload);
		JButton btnSearch = new JButton(new ImageIcon(gui.class.getResource("/searchIcon.jpg")));
		btnSearch.setBounds(166, 100, 19, 22);

		imgLabel.setBounds(444, 50, 207, 265);
		contentPane.add(imgLabel);

		lblNewLabel_6.setForeground(Color.RED);
		lblNewLabel_6.setFont(new Font("SansSerif", Font.BOLD, 20));
		lblNewLabel_6.setBounds(200, 147, 162, 29);
		contentPane.add(lblNewLabel_6);
		lblNewLabel_6.setVisible(false);

		similarComboBox.setBounds(200, 186, 189, 22);
		similarComboBox.setVisible(false);
		contentPane.add(similarComboBox);

		startComboBox = new JComboBox();
		startComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox startBox =  (JComboBox) e.getSource();
			}
		});
		startComboBox.setBounds(465, 395, 81, 22);
		contentPane.add(startComboBox);

		endComboBox = new JComboBox();
		endComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox endBox =  (JComboBox) e.getSource();
			}
		});
		endComboBox.setBounds(570, 395, 81, 22);
		contentPane.add(endComboBox);

		progressBar.setForeground(Color.GREEN);
		progressBar.setEnabled(false);
		progressBar.setBounds(0, 22, 681, 13);
		contentPane.add(progressBar);

		contentPane.add(btnSearch);
		searchTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loading ld = new loading(1);
				ld.execute();
			}
		});
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loading ld = new loading(1);
				ld.execute();
			}
		});

		seasonComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				comboBox =  (JComboBox) event.getSource();
				if((comboBox.getSelectedIndex()) != 0)
				{
					loading ld2 = new loading(2);
					ld2.execute();
				}
			}
		});

		similarComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				comboBox_2 =  (JComboBox) event.getSource();
				if((comboBox_2.getSelectedIndex()) != 0)
				{
					loading ld3 = new loading(3);
					ld3.execute();
				}
			}
		});

		lblNewLabel_3 = new JLabel((String) null);
		lblNewLabel_3.setForeground(Color.WHITE);
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_3.setBounds(12, 405, 287, 31);
		contentPane.add(lblNewLabel_3);

		epComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {

				JComboBox comboBox_1 =  (JComboBox) event.getSource();
			}
		});

		lblNewLabel_5 = new JLabel("Files left");
		lblNewLabel_5.setForeground(Color.WHITE);
		lblNewLabel_5.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_5.setBounds(556, 450, 113, 20);
		contentPane.add(lblNewLabel_5);

		JButton btnDonate = new JButton("Donate");
		btnDonate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(new URI("https://www.paypal.me/khalidwaleed0"));
				} catch (IOException | URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnDonate.setFont(new Font("SansSerif", Font.BOLD, 19));
		btnDonate.setBounds(12, 345, 128, 39);
		contentPane.add(btnDonate);

		JButton btnDownloadAllEpisodes = new JButton("Download All Episodes");
		btnDownloadAllEpisodes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loading ld4 = new loading(4);
				ld4.execute();
				Pattern pat1 = Pattern.compile("(https:\\/\\/mega\\S+)");
				Pattern pat2 = Pattern.compile("(https:\\/\\/drive\\S+)");
				Matcher mat1 = null,mat2=null;
				for(int i=startComboBox.getSelectedIndex()-1 ; i < endComboBox.getSelectedIndex() ; i++)
				{
					mat1 = pat1.matcher(scraper.episodes.get(i).getAttribute("data-links"));
					mat2 = pat2.matcher(scraper.episodes.get(i).getAttribute("data-links"));
					if(mat1.find())
						downloader.megaLinks.add(mat1.group(1));
					if(mat2.find())
						downloader.driveLinks.add(mat2.group(1));
				}
			}
		});
		btnDownloadAllEpisodes.setFont(new Font("SansSerif", Font.BOLD, 15));
		btnDownloadAllEpisodes.setBounds(444, 345, 207, 39);
		contentPane.add(btnDownloadAllEpisodes);

		lblNewLabel_7 = new JLabel("Download Info");
		lblNewLabel_7.setForeground(Color.WHITE);
		lblNewLabel_7.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_7.setBounds(12, 445, 287, 31);
		contentPane.add(lblNewLabel_7);

		JLabel lblFrom = new JLabel("From");
		lblFrom.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblFrom.setForeground(Color.WHITE);
		lblFrom.setBounds(432, 398, 37, 14);
		contentPane.add(lblFrom);

		JLabel lblTo = new JLabel("To");
		lblTo.setForeground(Color.WHITE);
		lblTo.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTo.setBounds(550, 398, 19, 14);
		contentPane.add(lblTo);

		btnDownload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if((epComboBox.getSelectedIndex()) != 0)
				{
					Pattern pat1 = Pattern.compile("(https:\\/\\/mega\\S+)");
					Pattern pat2 = Pattern.compile("(https:\\/\\/drive\\S+)");
					Matcher mat1 = pat1.matcher(scraper.episodes.get(epComboBox.getSelectedIndex()-1).getAttribute("data-links"));
					Matcher mat2 = pat2.matcher(scraper.episodes.get(epComboBox.getSelectedIndex()-1).getAttribute("data-links"));
					if(mat1.find())
						downloader.megaLinks.add(mat1.group(1));
					if(mat2.find())
						downloader.driveLinks.add(mat2.group(1));
					loading ld4 = new loading(4);
					ld4.execute();
				}
			}
		});
		}

	private class loading extends SwingWorker<Void,Void>{
		int opNumber;
		public loading(int opNumber) {
			this.opNumber=opNumber;
		}
		@Override
		protected Void doInBackground() throws Exception {
			progressBar.setIndeterminate(true);
			switch(opNumber)
			{
			case 1:
				search();
				break;
			case 2:
				seasonSelect();
				break;
			case 3:
				similarSelect();
				break;
			case 4:
				Thread.sleep(6000);
				break;
			}
			progressBar.setIndeterminate(false);
			progressBar.setValue(100);
			return null;
		}
	}
}
