package src.main.java.ca.uwo.csd.hackwestern;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Layout {
	public static Component makeTitle(String title) {
		JLabel label = new JLabel(title, JLabel.CENTER);
		label.setFont(new Font("Helvetica", Font.BOLD, 48));
		JPanel panel = new JPanel(new BorderLayout());
		return panel.add(label);
	}
}
