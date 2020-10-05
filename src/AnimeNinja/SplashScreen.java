package AnimeNinja;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;

public class SplashScreen {

	private static boolean finished = false;
	protected void loadingScreen()
	{
		JWindow window = new JWindow();
    	JLabel loadingLabel = new JLabel(new ImageIcon(SplashScreen.class.getResource("/resources/loading.gif")));
    	window.getContentPane().add(loadingLabel);
    	window.setSize(399, 41);
    	window.setLocationRelativeTo(null);
    	window.setVisible(true);
    	try {
			Thread.sleep(5000);
			window.setVisible(false);
		} catch (InterruptedException e1) {
		}
    	window.getContentPane().remove(loadingLabel);
    	JLabel imgLabel = new JLabel(new ImageIcon(SplashScreen.class.getResource("/resources/splashIcon.gif")));
    	JLabel imgLabel2 = new JLabel(new ImageIcon(SplashScreen.class.getResource("/resources/splashIcon.gif")));
    	window.getContentPane().add(imgLabel);
    	window.setSize(220, 220);
    	window.setLocationRelativeTo(null);
    	window.setLocation(window.getX()+110, window.getY());

    	JWindow window2 = new JWindow();
    	window2.getContentPane().add(imgLabel2);
    	window2.setSize(220, 220);
    	window2.setLocation(window.getX()-220, window.getY());

    	window.setVisible(true);
    	window2.setVisible(true);
    	SoundPlayer soundPlayer = new SoundPlayer();
    	Thread soundThread = new Thread(soundPlayer);
    	soundThread.start();
    	try {
    	    Thread.sleep(5500);
    	    window2.dispose();
    	    window.setVisible(false);
    	    Thread.sleep(500);
    	} catch (InterruptedException e) {
    	}

    	window.setSize(527, 315);
    	window.setLocationRelativeTo(null);
    	imgLabel.setIcon(new ImageIcon(SplashScreen.class.getResource("/resources/logo.png")));
    	window.setVisible(true);
    	try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
		}
    	window.dispose();
	}

	protected void downloadLocation()
	{
		OsDetector.setLookAndFeel();
		JDialog dialog = new JDialog();
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		JPanel contentPanel = new JPanel();
		JTextField dirTextField;
		dialog.setTitle("Anime Ninja");
		dialog.setSize(515, 134);
		dialog.setLocationRelativeTo(null);
		dialog.getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		dialog.getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		dirTextField = new JTextField();
		dirTextField.setBounds(152, 14, 238, 22);
		contentPanel.add(dirTextField);
		dirTextField.setColumns(10);

		JLabel lblDownloadDirectory = new JLabel("Download Directory");
		lblDownloadDirectory.setBounds(10, 17, 145, 14);
		contentPanel.add(lblDownloadDirectory);

		JButton btnBrowse = new JButton("Browse");

		File downloadLog = new File(OsDetector.installationDirectory +File.separator+"Download Location.txt");
		if(!downloadLog.exists())
		{
			try {
				PrintWriter writer = new PrintWriter(downloadLog,"UTF-8");
				writer.println(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath()+File.separator+"Anime Ninja");
				writer.close();
			} catch (FileNotFoundException | UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		FileReader reader;
		try {
			reader = new FileReader(downloadLog, StandardCharsets.UTF_8);
			BufferedReader br = new BufferedReader(reader);
			File defaultDir = new File(br.readLine());
			br.close();
			reader.close();
			dirTextField.setText(defaultDir.getAbsolutePath());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		btnBrowse.setBounds(395, 13, 95, 24);
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath(), FileSystemView.getFileSystemView());
				chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
				chooser.showSaveDialog(null);
				try {
					PrintWriter writer2 = new PrintWriter(downloadLog,"UTF-8");
					writer2.println(chooser.getSelectedFile().getAbsolutePath()+File.separator+"Anime Ninja");
					writer2.close();
				} catch (FileNotFoundException | UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				dirTextField.setText(chooser.getSelectedFile().getAbsolutePath()+File.separator+"Anime Ninja");
			}
		});
		contentPanel.add(btnBrowse);
		JButton btnOk = new JButton("OK");
		btnOk.setBounds(395, 45, 95, 23);
		contentPanel.add(btnOk);
		dialog.setVisible(true);
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				finished = true;
				dialog.dispose();
			}
		});
		dialog.addWindowListener((WindowListener) new WindowAdapter()
		{
		    public void windowClosing(WindowEvent e)
		    {
		    	finished = true;
		    	dialog.dispose();
		    }
		});

		while(!finished)
		{
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		JOptionPane.showMessageDialog(null, "We don't encourage piracy.If you love anime and want to support the industry\nplease,subscribe to the legal sites");
	}
}
