package scripts.crabs.utils;

import scripts.crabs.data.Variables;

/**
 * 
 * @author Nate
 *
 */
public class MessageUtility {
	
	public static void handleServerMessage(String message) {
		if (message.contains("You drink some of your"))
			Variables.get().drankPotion = true;
		else if (message.contains("You eat"))
			Variables.get().ateFood = true;
	}

}
