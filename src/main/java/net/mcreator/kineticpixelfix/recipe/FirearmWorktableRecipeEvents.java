package net.mcreator.kineticpixelfix.recipe;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

public class FirearmWorktableRecipeEvents {

	@SubscribeEvent
	public static void onServerStarted(ServerStartedEvent event) {
		FirearmWorktableRecipeManager.load(event.getServer());
	}

	@SubscribeEvent
	public static void onDatapackSync(OnDatapackSyncEvent event) {
		FirearmWorktableRecipeManager.load(event.getPlayerList().getServer());
	}
}