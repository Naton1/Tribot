package scripts.castlewarsv2.gui;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import scripts.castlewarsv2.api.CastleWars.Teams;
import scripts.castlewarsv2.api.CastleWarsSettings;
import scripts.napi.BugReportFrame;

@SuppressWarnings("serial")
public class GUI extends JFrame {

	private JPanel contentPane;
	private boolean open;
	
	private boolean sleep;
	private Teams team;
	
	public boolean isOpen() {
		return open;
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		
		open = true;
		
		setSize(375,300);
		setResizable(false);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setTitle("nCastle Wars GUI");
		setVisible(true);
		setBounds(100, 100, 375, 298);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnSubmitABug = new JMenu("Suggestion/Bug Report");
		menuBar.add(mnSubmitABug);
		
		JMenuItem mntmSubmitABug = new JMenuItem("Submit a Suggestion/Report a Bug");
		mntmSubmitABug.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new BugReportFrame("nCastle Wars");
			}
		});
		mnSubmitABug.add(mntmSubmitABug);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(5, 1, 0, 0));
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1);
		
		JLabel lblNcastleWars = new JLabel("nCastle Wars");
		lblNcastleWars.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 24));
		panel_1.add(lblNcastleWars);
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2);
		
		JLabel lblSelectYourTeam = new JLabel("Select your team:");
		lblSelectYourTeam.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		panel_2.add(lblSelectYourTeam);
		
		JPanel panel_3 = new JPanel();
		contentPane.add(panel_3);
		panel_3.setLayout(new GridLayout(1, 3, 0, 0));
		
		JPanel panel_5 = new JPanel();
		panel_3.add(panel_5);
		
		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Guthix (Recommended)", "Zamorak", "Saradomin"}));
		panel_5.add(comboBox);
		
		JPanel panel_7 = new JPanel();
		contentPane.add(panel_7);
		panel_7.setLayout(new GridLayout(0, 1, 0, 0));
		
		JCheckBox chckbxSleepBeforeEntering = new JCheckBox("Sleep before using portal/ladder (Recommended)");
		chckbxSleepBeforeEntering.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		chckbxSleepBeforeEntering.setHorizontalAlignment(SwingConstants.CENTER);
		panel_7.add(chckbxSleepBeforeEntering);
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		
		JButton btnStart = new JButton("Start");
		btnStart.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String teamString = (String) comboBox.getSelectedItem();
				switch (teamString) {
				case "Guthix (Recommended)":
					team = Teams.GUTHIX;
					break;
				case "Zamorak":
					team = Teams.ZAMORAK;
					break;
				case "Saradomin":
					team = Teams.SARADOMIN;
					break;
				}
				sleep = chckbxSleepBeforeEntering.isSelected();
				open = false;
				setVisible(false);
			}
		});
		panel.add(btnStart);
	}
	
	public CastleWarsSettings getSettings() {
		return new CastleWarsSettings(this.team, this.sleep);
	}

}
