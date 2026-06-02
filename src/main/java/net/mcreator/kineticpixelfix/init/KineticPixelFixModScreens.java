/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.kineticpixelfix.init;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.kineticpixelfix.client.gui.FirearmworktableguiScreen;

import net.createmod.ponder.mixin.client.accessor.ScreenAccessor;

@EventBusSubscriber(Dist.CLIENT)
public class KineticPixelFixModScreens {
	@SubscribeEvent
	public static void clientLoad(RegisterMenuScreensEvent event) {
		event.register(KineticPixelFixModMenus.FIREARMWORKTABLEGUI.get(), FirearmworktableguiScreen::new);
	}

	public interface ScreenAccessor {
		void updateMenuState(int elementType, String name, Object elementState);
	}
}