package scripts.crabs.paint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.tribot.api.Timing;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;

import scripts.crabs.data.Variables;

/**
 * 
 * @author Nate
 *
 */
public class Paint {
	
	private static final Color color1 = new Color(82, 82, 82, 200);
	private static final Color color2 = new Color(52, 52, 52, 170);
	private static final Color color3 = new Color(226, 219, 217);
	private static final BasicStroke stroke1 = new BasicStroke(1);
	private static final BasicStroke stroke2 = new BasicStroke(3);
	private static final Image skillsSelected = getImage("http://i.imgur.com/4ojZqTk.png");
	private static final Image close = getImage("http://i.imgur.com/sRDxzxX.png");
	private static final Image skillsUnselected = getImage("http://i.imgur.com/DYHUQYc.png");
	private static final Image infoUnselected = getImage("http://i.imgur.com/IEizXgf.png");
	private static final Image infoSelected = getImage("http://i.imgur.com/AhyX8Wy.png");
	
	private static final RenderingHints aa = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
	
	public static final Rectangle hideArea = new Rectangle(13, 317, 20, 20);
	
	public static FontMetrics fontMetrics;
	
	public static void paint(Graphics g) {
		
		Graphics2D gg = (Graphics2D)g;
		gg.setRenderingHints(aa);
		
		List<String> skillsList = getCurrentCombatXP();
		String[] skills = skillsList.toArray(new String[skillsList.size()]);
		if (!Variables.get().hidePaint) {
			drawTitle(gg);
			drawSideBar(gg);
			switch (Variables.get().tab) {
			case INFO:
				drawInfo(gg);
				break;
			case SKILLS:
				drawSkills(gg, skills);
				break;
			}
		}
		drawClose(gg);
	}

	/**
	 * Checks to see if any combat skill has higher XP than it did when the script was started.
	 * 
	 * @return List of all skills that have higher XP than when the script was started.
	 */
	private static List<String> getCurrentCombatXP() {
		List<String> skillsList = new LinkedList<String>();
		if (Skills.getXP(SKILLS.ATTACK) > Variables.get().attXp) 
			skillsList.add("Attack");
		if (Skills.getXP(SKILLS.STRENGTH) > Variables.get().strXp) 
			skillsList.add("Strength");
		if (Skills.getXP(SKILLS.DEFENCE) > Variables.get().defXp) 
			skillsList.add("Defence");
		if (Skills.getXP(SKILLS.RANGED) > Variables.get().rangeXp) 
			skillsList.add("Ranged");
		if (Skills.getXP(SKILLS.MAGIC) > Variables.get().mageXp) 
			skillsList.add("Magic");
		if (Skills.getXP(SKILLS.HITPOINTS) > Variables.get().hpXp) 
			skillsList.add("Hitpoints");
		return skillsList;
	}

	private static void drawInfo(Graphics2D g) {
		g.setColor(color2);
		g.fillRect(110, 352, 300, 22);
		g.fillRect(110, 377, 300, 22);
		g.fillRect(110, 402, 300, 22);
		g.fillRect(110, 427, 300, 22);
		g.setColor(color3);
		g.setFont(new Font("Garamond", Font.BOLD, 20));
		fontMetrics = g.getFontMetrics();
		g.drawString("Status: ", 130, 370);
		g.drawString(Variables.get().status, 410 - fontMetrics.stringWidth(Variables.get().status) - 20, 370);
		g.drawString("Total Runtime: ", 130, 395);
		g.drawString(Timing.msToString(Timing.timeFromMark(Variables.get().startTime)),
				410 - fontMetrics.stringWidth(Timing.msToString(Timing.timeFromMark(Variables.get().startTime))) - 20, 395);
		if (Variables.get().useFood) {
			g.drawString("Eating next at:", 130, 420);
			g.drawString(String.valueOf(Variables.get().eatAtHP), 410 - fontMetrics.stringWidth(String.valueOf(Variables.get().eatAtHP)) - 20, 420);
			
		}
		else
			g.drawString("We are not using food.", 130, 420);
		if (Variables.get().drinkPotions) {
			g.drawString("Drinking potion at:", 130, 445);
			g.drawString(String.valueOf(Variables.get().potionHandler.getNextDrinkLevel()),
					410 - fontMetrics.stringWidth(String.valueOf(Variables.get().potionHandler.getNextDrinkLevel())) - 20, 445);
		}
		else
			g.drawString("We are not using potions.", 130, 445);
	}


	private static void drawTitle(Graphics2D g) {
		g.setFont(new Font("Century Gothic", Font.BOLD, 30));
		GradientPaint gp = new GradientPaint(100, 75, Color.WHITE, 50, 50, Color.GRAY, true);
		g.setPaint(gp);
		g.drawString("nSand Crabs by Naton", 75, 335);
	}


	public enum Tabs { 
		INFO {
			@Override
			public Rectangle get() {
				return new Rectangle(8,345,30,30);
			}
		},
		SKILLS {
			@Override
			public Rectangle get() {
				return new Rectangle(8,375,30,30);
			}
		};
		public abstract Rectangle get();
	}

	private static void drawSideBar(Graphics2D g) {
		g.setColor(color1);
		g.fillRect(8, 345, 503, 130);
		g.setStroke(stroke1);
		g.drawRect(8, 345, 503, 130);
		g.setColor(color2);
		g.fillRect(8, 345, 30, 128);
		g.setStroke(stroke2);
		g.drawRect(8, 345, 30, 128);
		g.drawImage(Variables.get().tab == Tabs.INFO ? infoSelected.getScaledInstance(30, 30, 0) : infoUnselected.getScaledInstance(30, 30, 0), 8, 345, null);
		g.drawImage(Variables.get().tab == Tabs.SKILLS ? skillsSelected.getScaledInstance(30, 30, 0) : skillsUnselected.getScaledInstance(30, 30, 0), 8, 375, null);
	}
	
	private static void drawClose(Graphics2D g) {
		g.drawImage(close.getScaledInstance(20, 20, 0), 13, 317, null);
	}
	
	private static void drawSkills(Graphics2D g, String[] skills) {
		int timesPainted = 0;
		for (String s : skills) {
			g.setColor(color2);
			g.fillRect(50, 360 + timesPainted * 22, 440, 20);
			g.setColor(Color.red);
			g.fillRect(130, 360 + timesPainted * 22, 140, 20);
			g.setColor(Color.green);
			int percentTilNextLvl = Skills.getPercentToNextLevel(SKILLS.valueOf(s.toUpperCase()));
			int greenBarWidth = (int) (140 * (percentTilNextLvl / 100.0));
			g.fillRect(130, 360 + timesPainted * 22, greenBarWidth, 20);
			g.setFont(new Font("Garamond" , Font.BOLD, 16));
			g.setColor(Color.white);
			g.drawString(s, 60, 375 + timesPainted * 22);
			g.setFont(new Font("Garamond" , Font.PLAIN, 15));
			g.setColor(Color.black);
			g.drawString(String.valueOf(percentTilNextLvl) + "% to level " + (1 + Skills.getActualLevel(SKILLS.valueOf(s.toUpperCase()))), 152, 375 + timesPainted * 22);
			g.setColor(Color.white);
			long xpGained = Skills.getXP(SKILLS.valueOf(s.toUpperCase())) - getStartXp(s);
			int levelsGained = Skills.getLevelByXP(Skills.getXP(SKILLS.valueOf(s.toUpperCase()))) - Skills.getLevelByXP(getStartXp(s));
			g.drawString("XP Gained: " + xpGained + " (" + xpGained * (long)3600000/ Timing.timeFromMark(Variables.get().startTime) + ")" + " (" + levelsGained + ")", 290, 375 + timesPainted * 22);
			timesPainted++;
		}
	}
	
	/**
	 * Gets the starting XP of a skill, recorded when the script began.
	 * 
	 * @param skill The skill to get the starting XP of
	 * @return The starting XP of the chosen skill
	 */
	private static int getStartXp(String skill) {
		switch (skill) {
		case "Defence":
			return Variables.get().defXp;
		case "Attack":
			return Variables.get().attXp;
		case "Strength":
			return Variables.get().strXp;
		case "Hitpoints":
			return Variables.get().hpXp;
		case "Magic":
			return Variables.get().mageXp;
		case "Ranged":
			return Variables.get().rangeXp;
		}
		return 0;
	}
	
	/**
	 * Gets an image.
	 * @param url The URL to an image.
	 * @return The image of the URL provided.
	 */
	public static Image getImage(String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {
			return null;
		}
	}
}
