package net.mcreator.kineticpixelfix.init;

import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;

import net.mcreator.kineticpixelfix.jei_recipes.FirearmworktablerecipeRecipeCategory;
import net.mcreator.kineticpixelfix.jei_recipes.FirearmworktablerecipeRecipe;

import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.IModPlugin;

import java.util.stream.Collectors;
import java.util.Objects;
import java.util.List;

@JeiPlugin
public class KineticPixelFixModJeiPlugin implements IModPlugin {
	public static mezz.jei.api.recipe.RecipeType<FirearmworktablerecipeRecipe> Firearmworktablerecipe_Type = new mezz.jei.api.recipe.RecipeType<>(FirearmworktablerecipeRecipeCategory.UID, FirearmworktablerecipeRecipe.class);

	@Override
	public ResourceLocation getPluginUid() {
		return ResourceLocation.parse("kinetic_pixel_fix:jei_plugin");
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		registration.addRecipeCategories(new FirearmworktablerecipeRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		RecipeManager recipeManager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
		List<FirearmworktablerecipeRecipe> FirearmworktablerecipeRecipes = recipeManager.getAllRecipesFor(FirearmworktablerecipeRecipe.Type.INSTANCE).stream().map(RecipeHolder::value).collect(Collectors.toList());
		registration.addRecipes(Firearmworktablerecipe_Type, FirearmworktablerecipeRecipes);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(KineticPixelFixModBlocks.FIREARMWORKTABLE.get().asItem()), Firearmworktablerecipe_Type);
	}
}