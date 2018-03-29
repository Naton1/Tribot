package scripts.castlewarsv2.paint;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import scripts.castlewarsv2.data.Variables;

public class Paint {
	
	public static final Rectangle hideArea = new Rectangle(25, 350, 20, 20);
	private static final Font font = new Font("Copperplate Gothic Bold", Font.BOLD, 20);
	private static final Image img = getImage("http://i.imgur.com/kLIg51f.png");
	private static final Image close = getImage("http://i.imgur.com/sRDxzxX.png");
	private static final RenderingHints aa = new 
			RenderingHints(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
	
	public static void paint(Graphics g) {
		Graphics2D gg = (Graphics2D)g;
		gg.setRenderingHints(aa);
		gg.setColor(Color.WHITE);
		gg.setFont(font);
		if (Variables.get().paintEnabled) {
			gg.drawImage(img.getScaledInstance(505, 130, 0), 8, 345, null);
			gg.drawString(Variables.get().status, 100, 390);
			gg.drawString("Time Running: " + Variables.get().runTimeString, 100, 415);
			gg.drawString("Tickets Earned: " + Variables.get().numTickets + " (" + Variables.get().ticketsPerHour + ")", 100, 440);
		}
		g.drawImage(close.getScaledInstance(20, 20, 0), 25, 350, null);
	}
	
	private static Image getImage(String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {
			return null;
		}
	}
	

}
