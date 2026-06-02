/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.kineticpixelfix.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import net.mcreator.kineticpixelfix.KineticPixelFixMod;

public class KineticPixelFixModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY =
			DeferredRegister.create(Registries.CREATIVE_MODE_TAB, KineticPixelFixMod.MODID);

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> KINETICPIXEL =
			REGISTRY.register("kineticpixel", () -> CreativeModeTab.builder()
					.title(Component.translatable("item_group.kinetic_pixel_fix.kineticpixel"))
					.icon(() -> new ItemStack(KineticPixelFixModItems.BARREL.get()))
					.displayItems((parameters, tabData) -> {
						tabData.accept(KineticPixelFixModItems.AMMUNITIONBOX.get());
						tabData.accept(KineticPixelFixModItems.ANDESITEALLOYCOMPRESSIONSHEET.get());
						tabData.accept(KineticPixelFixModItems.BAMBOOSHELL.get());
						tabData.accept(KineticPixelFixModItems.BARREL.get());
						tabData.accept(KineticPixelFixModItems.BRASSCOMPRESSIONSHEET.get());
						tabData.accept(KineticPixelFixModItems.COMPONENTTEMPLATE.get());
						tabData.accept(KineticPixelFixModItems.EMPTYGASCYLINDER.get());
						tabData.accept(KineticPixelFixModItems.ENDERALLOYCOMPRESSIONSHEET.get());
						tabData.accept(KineticPixelFixModItems.ENDERALLOYINGOT.get());
						tabData.accept(KineticPixelFixModItems.GASCYLINDER.get());
						tabData.accept(KineticPixelFixModItems.GRAYCOTTON.get());

						tabData.accept(KineticPixelFixModItems.NITROPROPELLANT.get());
						tabData.accept(KineticPixelFixModItems.PISTONEXCITER.get());
						tabData.accept(KineticPixelFixModItems.SPECIALSTEELINGOT.get());
						tabData.accept(KineticPixelFixModItems.SPECIALSTEELCOMPRESSIONSHEET.get());
						tabData.accept(KineticPixelFixModItems.STRIKEREXCITER.get());
						tabData.accept(KineticPixelFixModItems.WASTEDBARREL.get());
						tabData.accept(KineticPixelFixModItems.GRAY_COTTON_SEEDS.get());
						tabData.accept(KineticPixelFixModItems.SMELTER_FIBER.get());
						tabData.accept(KineticPixelFixModItems.IRONWIRE.get());

                        tabData.accept(KineticPixelFixModItems.BLUEMAP_1.get());
                        tabData.accept(KineticPixelFixModItems.BLUEMAP_2.get());
                        tabData.accept(KineticPixelFixModItems.BLUEMAP_3.get());
                        tabData.accept(KineticPixelFixModItems.BLUEMAP_4.get());

                        tabData.accept(KineticPixelFixModBlocks.FIREARMWORKTABLE.get().asItem());
					})
					.build());
}