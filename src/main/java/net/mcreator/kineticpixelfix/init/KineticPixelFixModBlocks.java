/*
*    MCreator note: This file will be REGENERATED on each build.
*/
package net.mcreator.kineticpixelfix.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;

import net.minecraft.world.level.block.Block;

import net.mcreator.kineticpixelfix.block.FirearmworktableBlock;
import net.mcreator.kineticpixelfix.KineticPixelFixMod;

public class KineticPixelFixModBlocks {
	public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(KineticPixelFixMod.MODID);
	public static final DeferredBlock<Block> FIREARMWORKTABLE;
	static {
		FIREARMWORKTABLE = REGISTRY.register("firearmworktable", FirearmworktableBlock::new);
	}
	// Start of user code block custom blocks
	public static final DeferredBlock<Block> WILD_GRAY_COTTON = REGISTRY.register("wild_gray_cotton", () -> new net.mcreator.kineticpixelfix.block.WildGrayCottonBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties.of().noCollission()
			.randomTicks().instabreak().sound(net.minecraft.world.level.block.SoundType.CROP).offsetType(net.minecraft.world.level.block.state.BlockBehaviour.OffsetType.XZ).pushReaction(net.minecraft.world.level.material.PushReaction.DESTROY)));
	// End of user code block custom blocks
}