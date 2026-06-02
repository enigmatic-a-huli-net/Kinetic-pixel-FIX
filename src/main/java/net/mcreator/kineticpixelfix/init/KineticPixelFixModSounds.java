/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.kineticpixelfix.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

import net.mcreator.kineticpixelfix.KineticPixelFixMod;

public class KineticPixelFixModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, KineticPixelFixMod.MODID);
	public static final DeferredHolder<SoundEvent, SoundEvent> BLUEPRINT = REGISTRY.register("blueprint", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("kinetic_pixel_fix", "blueprint")));
	public static final DeferredHolder<SoundEvent, SoundEvent> BUMP = REGISTRY.register("bump", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("kinetic_pixel_fix", "bump")));
	public static final DeferredHolder<SoundEvent, SoundEvent> FURNACE_DOOR = REGISTRY.register("furnace_door", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("kinetic_pixel_fix", "furnace_door")));
	public static final DeferredHolder<SoundEvent, SoundEvent> FURNACE_LEVER = REGISTRY.register("furnace_lever", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("kinetic_pixel_fix", "furnace_lever")));
}