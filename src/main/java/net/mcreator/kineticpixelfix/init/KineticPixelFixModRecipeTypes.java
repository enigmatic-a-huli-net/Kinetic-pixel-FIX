package net.mcreator.kineticpixelfix.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.ModList;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.IEventBus;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.kineticpixelfix.jei_recipes.FirearmworktablerecipeRecipe;

@EventBusSubscriber
public class KineticPixelFixModRecipeTypes {
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, "kinetic_pixel_fix");
	public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, "kinetic_pixel_fix");

	@SubscribeEvent
	public static void register(FMLConstructModEvent event) {
		IEventBus bus = ModList.get().getModContainerById("kinetic_pixel_fix").get().getEventBus();
		event.enqueueWork(() -> {
			RECIPE_TYPES.register(bus);
			SERIALIZERS.register(bus);
RECIPE_TYPES.register("firearm_worktable", () -> FirearmworktablerecipeRecipe.Type.INSTANCE);
SERIALIZERS.register("firearm_worktable", () -> FirearmworktablerecipeRecipe.Serializer.INSTANCE);
		});
	}
}