package net.mcreator.kineticpixelfix.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class FirearmWorktableRecipe {

	private final ResourceLocation id;
	private final Item blueprint;
	private final Map<Integer, Item> ingredients;
	private final ItemStack result;
	private final int craftTime;

public FirearmWorktableRecipe(
		ResourceLocation id,
		Item blueprint,
		Map<Integer, Item> ingredients,
		ItemStack result,
		int craftTime
) {
	this.id = id;
	this.blueprint = blueprint;
	this.ingredients = ingredients;
	this.result = result;
	this.craftTime = craftTime;
}

	public ResourceLocation getId() {
		return id;
	}

	public Item getBlueprint() {
		return blueprint;
	}

	public Map<Integer, Item> getIngredients() {
		return ingredients;
	}

	public ItemStack getResult() {
		return result.copy();
	}
	public int getCraftTime() {
	return craftTime;
}
}