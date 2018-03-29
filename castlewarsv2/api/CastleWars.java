package scripts.castlewarsv2.api;

import org.tribot.api2007.Equipment;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

public class CastleWars {
	
	public static final RSArea ZAMORAK_WAITING_ROOM = new RSArea(new RSTile(2411,9533), new RSTile(2429,9514)),
			SARADOMIN_WAITING_ROOM = new RSArea(new RSTile(2370,9482), new RSTile(2391,9496)),
			CASTLE_WARS_LOBBY = new RSArea(new RSTile[]{new RSTile(2437,3098), new RSTile(2444,3098), new RSTile(2444,3082), new RSTile(2437,3082)});
	
	public static final RSTile SARADOMIN_TILE = new RSTile(2438,3096,0),
			ZAMORAK_TILE = new RSTile(2438,3084),
			GUTHIX_TILE = new RSTile(2438,3090),
			CASTLE_WARS_LOBBY_TILE = new RSTile(2441, 3090, 0);
	
	public static final int ZAMORAK_CAPE_ID = 4042,
			SARADOMIN_CAPE_ID = 4041,
			ZAMORAK_LADDER_ID = 6281,
			SARADOMIN_LADDER_ID = 6280,
			TICKET_ID = 4067,
			WAITING_ROOM_SARADOMIN_PORTAL = 4389,
			WAITING_ROOM_ZAMORAK_PORTAL = 4390,
			SARADOMIN_PORTAL = 4387,
			GUTHIX_PORTAL = 4408,
			ZAMORAK_PORTAL = 4388;
	
	public enum Teams {
		ZAMORAK,
		SARADOMIN,
		GUTHIX;
	}

	public static boolean canFindPortal(Teams team) {
		int portalId = 0;
		switch (team) {
		case GUTHIX:
			portalId = GUTHIX_PORTAL;
			break;
		case SARADOMIN:
			portalId = SARADOMIN_PORTAL;
			break;
		case ZAMORAK:
			portalId = ZAMORAK_PORTAL;
			break;
		}
		return findObject(portalId) != null;
	}
	
	public static boolean canFindWaitingRoomPortal() {
		return findObject(WAITING_ROOM_SARADOMIN_PORTAL) != null || findObject(WAITING_ROOM_ZAMORAK_PORTAL) != null;
	}
	
	public static boolean wearingCwarsCape() {
		RSItem[] capes = Equipment.find("Hooded cloak");
		return capes.length > 0 && capes[0] != null;
	}
	
	public static boolean inWaitingRoom() {
		return wearingCwarsCape()
				&& (ZAMORAK_WAITING_ROOM.contains(Player.getPosition())
						|| SARADOMIN_WAITING_ROOM.contains(Player.getPosition())
						|| canFindWaitingRoomPortal());
	}
	
	public static RSObject getTeamPortal(Teams team) {
		int portalId = 0;
		switch (team) {
		case GUTHIX:
			portalId = GUTHIX_PORTAL;
			break;
		case SARADOMIN:
			portalId = SARADOMIN_PORTAL;
			break;
		case ZAMORAK:
			portalId = ZAMORAK_PORTAL;
			break;
		}
		RSObject[] portal = Objects.findNearest(15, portalId);
		if (portal.length > 0 && portal[0] != null)
			return portal[0];
		return null;
	}
	
	public static RSTile getTeamTile(Teams team) {
		switch (team) {
		case GUTHIX:
			return GUTHIX_TILE;
		case SARADOMIN:
			return SARADOMIN_TILE;
		case ZAMORAK:
			return ZAMORAK_TILE;
		}
		return null;
	}
	
	public static Teams getTeam() {
		RSItem[] capes = Equipment.find("Hooded cloak");
		if (capes.length > 0 && capes[0] != null) {
			if (capes[0].getID() == SARADOMIN_CAPE_ID) {
				return Teams.SARADOMIN;
			}
			if (capes[0].getID() == ZAMORAK_CAPE_ID) {
				return Teams.ZAMORAK;
			}
		}
		return null;
	}
	
	public static int getLadderId() {
		RSItem[] capes = Equipment.find("Hooded cloak");
		if (capes.length > 0 && capes[0] != null) {
			if (capes[0].getID() == SARADOMIN_CAPE_ID)
				return SARADOMIN_LADDER_ID;
			else if (capes[0].getID() == ZAMORAK_CAPE_ID)
				return ZAMORAK_LADDER_ID;
		}
		return 0;
	}
	
	public static RSObject getLadder() {
		RSObject[] ladder = Objects.find(15, getLadderId());
		
		if (ladder.length > 0)
			return ladder[0];
		
		return null;
	}
	
	public static boolean inGame() {
		return wearingCwarsCape() && !inWaitingRoom();
	}
	
	public static boolean canEnterStartedGame() {
		try {
			return Interfaces.get(219, 0).getChild(0).getText().equals("There's a free space, do you want to join?")
					&& Integer.parseInt(Interfaces.get(131, 0).getText()
							.substring(Interfaces.get(131, 0).getText().indexOf(":") + 1).trim()) >= 20;
		} catch (Exception e) {}
		return false;
	}

	public static boolean enterStartedGame() {
		try {
			return Interfaces.get(219, 0).getChild(1).click("Continue");
		} catch (Exception e) {}
		return false;
	}
	
	public static int getSaradominScore() {
		try {
			String interfaceText = Interfaces.get(58,4).getText();
			return Integer.parseInt(interfaceText.substring(0, interfaceText.indexOf(':')).trim());
		}
		catch (Exception e) {}
		return 0;
	}
	
	public static int getZamorakScore() {
		try {
			String interfaceText = Interfaces.get(58,3).getText();
			return Integer.parseInt(interfaceText.substring(interfaceText.indexOf('=')).trim());
		}
		catch (Exception e) {}
		return 0;
	}
	
	public static Teams getOpponentTeam() {
		RSItem[] capes = Equipment.find("Hooded cloak");
		if (capes.length > 0 && capes[0] != null) {
			if (capes[0].getID() == SARADOMIN_CAPE_ID)
				return Teams.ZAMORAK;
			else if (capes[0].getID() == ZAMORAK_CAPE_ID)
				return Teams.SARADOMIN;
		}
		return null;
	}
	
	@SuppressWarnings("incomplete-switch")
	public static int getScore(Teams team) {
		if (team != null) {
			switch (team) {
			case SARADOMIN:
				return getSaradominScore();
			case ZAMORAK:
				return getZamorakScore();
			}
		}
		return 0;
	}
	
	public static int getScore(boolean ourTeam) {
		if (ourTeam)
			return getScore(getTeam());
		else
			return getScore(getOpponentTeam());
	}
	
	public static RSObject findObject(int id) {
		RSObject[] o = Objects.findNearest(15, id);
		if (o.length > 0 && o[0] != null) {
			return o[0];
		}
		return null;
	}

}
