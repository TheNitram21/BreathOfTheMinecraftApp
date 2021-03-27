package de.onlinehome.mann.martin.botm;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.blogspot.debukkitsblog.net.Datapackage;

public class BreathOfTheMinecraft {
	
	JFrame frame;
	JPanel panel;
	
	JButton creditsButton;
	JButton screenshotsButton;
	JButton newsButton;
	
	JLabel credits;
	JLabel screenshots;
	JLabel news;
	
	String version = "Beta 1.0";
	
	public static void main(String[] args) {
		new BreathOfTheMinecraft();
	}
	
	public BreathOfTheMinecraft() {
		frame = new JFrame("Breath of the Minecraft");
		panel = new JPanel();
		
		creditsButton = createButton("Credits", 10, 10, 80, 30);
		screenshotsButton = createButton("Screenshots", 100, 10, 110, 30);
		newsButton = createButton("News", 220, 10, 80, 30);
		
		credits = new JLabel("<html><body>Die Breath of the Minecraft-App wurde von Nitram21 programmiert.<br>Version: " + version + "</body></html>");
		screenshots = new JLabel();
		news = new JLabel();
		
		ImageIcon icon = new ImageIcon("textures/BotMIcon.png");

		frame.add(creditsButton);
		frame.add(credits);
		frame.add(screenshotsButton);
		frame.add(screenshots);
		frame.add(newsButton);
		frame.add(news);
		frame.add(panel);

		frame.setBounds(10, 10, 1200, 650);
		
		credits.setBounds(10, 40, 600, 80);
		screenshots.setBounds(frame.getWidth() / 2 - 684 / 2, frame.getHeight() / 2 - 354 / 2, 684, 354);
		news.setBounds(10, 40, 600, 100);
		
		frame.setIconImage(icon.getImage());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel.setOpaque(true);
		panel.setBackground(Color.DARK_GRAY);
		
		credits.setVisible(true);
		screenshots.setVisible(true);
		
		frame.setVisible(true);
	}
	
	public JButton createButton(String text, int x, int y, int width, int height) {
		JButton button = new JButton(text);
		button.setBounds(x, y, width, height);
		button.addActionListener(new ButtonClickListener());
		button.setOpaque(true);
		button.setBackground(Color.GRAY);
		button.setVisible(true);
		return button;
	}
	
	public class ButtonClickListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == creditsButton) {
				credits.setVisible(true);
				screenshots.setVisible(false);
				news.setVisible(false);
			} else if(e.getSource() == screenshotsButton) {
				credits.setVisible(false);
				screenshots.setVisible(true);
				news.setVisible(false);
				
				BotMClient client = new BotMClient("botmserver.ddnss.de", 7921);
				Datapackage result = client.sendMessage(new Datapackage("FETCH_SCREENSHOTS"));
				
				if(result.get(0).equals("ERROR")) {
					screenshots.setIcon(null);
					screenshots.setText("An error occured whilst trying to fetch current screenshots.");
				} else {
					String fileName = "BreathOfTheMinecraftAppScreenshots.jpg";
					
					File file = new File(fileName);
					Datapackage byteDatapackage = (Datapackage) result.get(1);
					byte[] bytes = (byte[]) byteDatapackage.get(1);
					
					System.out.println(bytes);
					
					try (FileOutputStream writer = new FileOutputStream(file)) {
						writer.write(bytes);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					
					screenshots.setIcon(new ImageIcon("BreathOfTheMinecraftAppScreenshots.jpg"));
					screenshots.setText(null);
				}
			} else if(e.getSource() == newsButton) {
				credits.setVisible(false);
				screenshots.setVisible(false);
				news.setVisible(true);
				
				BotMClient client = new BotMClient("botmserver.ddnss.de", 7921);
				Datapackage result = client.sendMessage(new Datapackage("FETCH_NEWS"));
				
				if(result.get(0).equals("ERROR")) {
					news.setText("An error occured whilst trying to fetch current news.");
				} else {
					Datapackage newsDatapackage = (Datapackage) result.get(1);
					String newsText = (String) newsDatapackage.get(1);
					
					news.setText(newsText);
				}
			}
		}
		
	}
	
}