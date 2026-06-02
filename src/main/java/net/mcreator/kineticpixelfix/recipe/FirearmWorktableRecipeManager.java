package net.mcreator.kineticpixelfix.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class FirearmWorktableRecipeManager {
	private static final Map<ResourceLocation, FirearmWorktableRecipe> RECIPES = new HashMap<>();

	public static void load(MinecraftServer server) {
		RECIPES.clear();
		System.out.println("Loading firearm worktable recipes...");

		Map<ResourceLocation, JsonElement> jsons = new HashMap<>();

server.getResourceManager()
		.listResources("recipe/firearm_worktable", path -> path.getPath().endsWith(".json"))
		.forEach((id, resource) -> {
			System.out.println("Found firearm recipe jsons: " + jsons.size());
			try {
				jsons.put(id, com.google.gson.JsonParser.parseReader(resource.openAsReader()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		for (Map.Entry<ResourceLocation, JsonElement> entry : jsons.entrySet()) {
			try {
				JsonObject json = entry.getValue().getAsJsonObject();

				if (!json.has("type") || !json.get("type").getAsString().equals("kinetic_pixel_fix:firearm_worktable"))
					continue;

				ResourceLocation id = entry.getKey();
				Item blueprint = BuiltInRegistries.ITEM.get(ResourceLocation.parse(json.get("blueprint").getAsString()));

				Map<Integer, Item> ingredients = new HashMap<>();
				JsonObject ing = json.getAsJsonObject("ingredients");

				for (String slotText : ing.keySet()) {
					int slot = Integer.parseInt(slotText);
					Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(ing.get(slotText).getAsString()));
					ingredients.put(slot, item);
				}

				JsonObject resultJson = json.getAsJsonObject("result");
				Item resultItem = BuiltInRegistries.ITEM.get(ResourceLocation.parse(resultJson.get("id").getAsString()));
				int count = resultJson.has("count") ? resultJson.get("count").getAsInt() : 1;

				int craftTime = json.has("craft_time") ? json.get("craft_time").getAsInt() : 100;

RECIPES.put(id, new FirearmWorktableRecipe(id, blueprint, ingredients, new ItemStack(resultItem, count), craftTime));
			} catch (Exception e) {
	e.printStackTrace();
}
		}
	}

	public static Iterable<FirearmWorktableRecipe> getRecipes() {
		return RECIPES.values();
	}
}