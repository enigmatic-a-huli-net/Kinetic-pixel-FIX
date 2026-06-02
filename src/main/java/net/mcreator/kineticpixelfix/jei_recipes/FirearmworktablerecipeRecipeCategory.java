package net.mcreator.kineticpixelfix.jei_recipes;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import net.mcreator.kineticpixelfix.init.KineticPixelFixModBlocks;
import net.mcreator.kineticpixelfix.init.KineticPixelFixModJeiPlugin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;

public class FirearmworktablerecipeRecipeCategory implements IRecipeCategory<FirearmworktablerecipeRecipe> {
	public static final ResourceLocation UID = ResourceLocation.parse("kinetic_pixel_fix:firearm_worktable");
	public static final ResourceLocation TEXTURE = ResourceLocation.parse("kinetic_pixel_fix:textures/screens/firearm_worktable_jei.png");

	private final IDrawable background;
	private final IDrawable icon;

	public FirearmworktablerecipeRecipeCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 133);
		this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(KineticPixelFixModBlocks.FIREARMWORKTABLE.get().asItem()));
	}

	@Override
	public mezz.jei.api.recipe.RecipeType<FirearmworktablerecipeRecipe> getRecipeType() {
		return KineticPixelFixModJeiPlugin.Firearmworktablerecipe_Type;
	}

	@Override
	public Component getTitle() {
		return Component.literal("Firearm Worktable");
	}

	@Override
	public IDrawable getIcon() {
		return this.icon;
	}

	@Override
	public int getWidth() {
		return 176;
	}

	@Override
	public int getHeight() {
		return 133;
	}

	@Override
public void draw(FirearmworktablerecipeRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
	this.background.draw(guiGraphics);

	int totalSeconds = (recipe.getCraftTime() + 19) / 20;
	int minutes = totalSeconds / 60;
	int seconds = totalSeconds % 60;

	String timeText = String.format("%02d:%02d", minutes, seconds);

guiGraphics.pose().pushPose();

guiGraphics.pose().scale(0.7F, 0.7F, 1F);

guiGraphics.drawString(
		Minecraft.getInstance().font,
		timeText,
		(int)(114 / 0.8F),
		(int)(120 / 0.8F),
		0xFFFFFF,
		false
);

guiGraphics.pose().popPose();
}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, FirearmworktablerecipeRecipe recipe, IFocusGroup focuses) {
		// slot 0: blueprint
		builder.addSlot(RecipeIngredientRole.INPUT, 16, 98).addItemStack(recipe.getBlueprintStack());

		// slots 1-32: recipe ingredients
		for (int slot = 1; slot <= 32; slot++) {
			ItemStack stack = recipe.getStackForSlot(slot);
			if (stack.isEmpty())
				continue;

			int index = slot - 1;
			int x = 21 + (index % 8) * 17;
			int y = 8 + (index / 8) * 17;

			builder.addSlot(RecipeIngredientRole.INPUT, x, y).addItemStack(stack);
		}

		// slot 33: result
		builder.addSlot(RecipeIngredientRole.OUTPUT, 145, 99).addItemStack(recipe.getResultStack());
	}
}
