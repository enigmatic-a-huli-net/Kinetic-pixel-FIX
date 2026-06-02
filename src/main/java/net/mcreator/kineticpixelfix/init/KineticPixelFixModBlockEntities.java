package net.mcreator.kineticpixelfix.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.mcreator.kineticpixelfix.KineticPixelFixMod;
import net.mcreator.kineticpixelfix.block.entity.FirearmworktableBlockEntity;

public class KineticPixelFixModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY =
			DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, KineticPixelFixMod.MODID);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FirearmworktableBlockEntity>> FIREARMWORKTABLE =
			REGISTRY.register("firearmworktable",
					() -> BlockEntityType.Builder.of(
							FirearmworktableBlockEntity::new,
							KineticPixelFixModBlocks.FIREARMWORKTABLE.get()
					).build(null));
}