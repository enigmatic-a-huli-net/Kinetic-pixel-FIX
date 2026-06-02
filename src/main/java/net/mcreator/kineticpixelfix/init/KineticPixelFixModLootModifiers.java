package net.mcreator.kineticpixelfix.init;

import com.mojang.serialization.MapCodec;

import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.mcreator.kineticpixelfix.KineticPixelFixMod;
import net.mcreator.kineticpixelfix.loot.AddChestLootModifier;

import java.util.function.Supplier;

public class KineticPixelFixModLootModifiers {
	public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> REGISTRY =
			DeferredRegister.create(net.neoforged.neoforge.registries.NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, KineticPixelFixMod.MODID);

	public static final Supplier<MapCodec<AddChestLootModifier>> ADD_CHEST_LOOT =
			REGISTRY.register("add_chest_loot", () -> AddChestLootModifier.CODEC);
}