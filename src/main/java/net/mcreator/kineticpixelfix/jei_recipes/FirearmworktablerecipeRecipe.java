package net.mcreator.kineticpixelfix.jei_recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.HashMap;
import java.util.Map;

public class FirearmworktablerecipeRecipe implements Recipe<RecipeInput> {
	private final ResourceLocation blueprint;
	private final int craftTime;
	private final Map<Integer, ResourceLocation> ingredients;
	private final ItemStack result;

	public FirearmworktablerecipeRecipe(ResourceLocation blueprint, int craftTime, Map<Integer, ResourceLocation> ingredients, ItemStack result) {
		this.blueprint = blueprint;
		this.craftTime = craftTime;
		this.ingredients = ingredients;
		this.result = result;
	}

	public ResourceLocation getBlueprintId() {
		return blueprint;
	}

	public ItemStack getBlueprintStack() {
		Item item = BuiltInRegistries.ITEM.get(blueprint);
		return item == null ? ItemStack.EMPTY : new ItemStack(item);
	}

	public int getCraftTime() {
		return craftTime;
	}

	public Map<Integer, ResourceLocation> getIngredientMap() {
		return ingredients;
	}

	public ItemStack getStackForSlot(int slot) {
		ResourceLocation id = ingredients.get(slot);
		if (id == null)
			return ItemStack.EMPTY;

		Item item = BuiltInRegistries.ITEM.get(id);
		return item == null ? ItemStack.EMPTY : new ItemStack(item);
	}

	@Override
	public boolean matches(RecipeInput input, Level level) {
		return false;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> list = NonNullList.create();

		Item blueprintItem = BuiltInRegistries.ITEM.get(blueprint);
		if (blueprintItem != null)
			list.add(Ingredient.of(blueprintItem));

		for (int i = 1; i <= 32; i++) {
			ResourceLocation id = ingredients.get(i);
			if (id == null)
				continue;

			Item item = BuiltInRegistries.ITEM.get(id);
			if (item != null)
				list.add(Ingredient.of(item));
		}

		return list;
	}

	@Override
	public ItemStack assemble(RecipeInput input, HolderLookup.Provider holder) {
		return result.copy();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider provider) {
		return result.copy();
	}

	public ItemStack getResultStack() {
		return result.copy();
	}

	@Override
	public RecipeType<?> getType() {
		return Type.INSTANCE;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return Serializer.INSTANCE;
	}

	private static Map<String, ResourceLocation> encodeIngredientMap(Map<Integer, ResourceLocation> map) {
		Map<String, ResourceLocation> encoded = new HashMap<>();
		for (Map.Entry<Integer, ResourceLocation> entry : map.entrySet()) {
			encoded.put(String.valueOf(entry.getKey()), entry.getValue());
		}
		return encoded;
	}

	private static Map<Integer, ResourceLocation> decodeIngredientMap(Map<String, ResourceLocation> map) {
		Map<Integer, ResourceLocation> decoded = new HashMap<>();
		for (Map.Entry<String, ResourceLocation> entry : map.entrySet()) {
			try {
				int slot = Integer.parseInt(entry.getKey());
				if (slot >= 1 && slot <= 32) {
					decoded.put(slot, entry.getValue());
				}
			} catch (NumberFormatException ignored) {
			}
		}
		return decoded;
	}

	public static class Type implements RecipeType<FirearmworktablerecipeRecipe> {
		private Type() {
		}

		public static final RecipeType<FirearmworktablerecipeRecipe> INSTANCE = new Type();
	}

	public static class Serializer implements RecipeSerializer<FirearmworktablerecipeRecipe> {
		public static final Serializer INSTANCE = new Serializer();

		private static final Codec<Map<Integer, ResourceLocation>> INGREDIENTS_CODEC =
				Codec.unboundedMap(Codec.STRING, ResourceLocation.CODEC).xmap(
						FirearmworktablerecipeRecipe::decodeIngredientMap,
						FirearmworktablerecipeRecipe::encodeIngredientMap
				);

		private static final MapCodec<FirearmworktablerecipeRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
				ResourceLocation.CODEC.fieldOf("blueprint").forGetter(FirearmworktablerecipeRecipe::getBlueprintId),
				Codec.INT.optionalFieldOf("craft_time", 200).forGetter(FirearmworktablerecipeRecipe::getCraftTime),
				INGREDIENTS_CODEC.fieldOf("ingredients").forGetter(FirearmworktablerecipeRecipe::getIngredientMap),
				ItemStack.OPTIONAL_CODEC.fieldOf("result").forGetter(FirearmworktablerecipeRecipe::getResultStack)
		).apply(builder, FirearmworktablerecipeRecipe::new));

		public static final StreamCodec<RegistryFriendlyByteBuf, FirearmworktablerecipeRecipe> STREAM_CODEC =
				StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

		@Override
		public MapCodec<FirearmworktablerecipeRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, FirearmworktablerecipeRecipe> streamCodec() {
			return STREAM_CODEC;
		}

		private static FirearmworktablerecipeRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
			ResourceLocation blueprint = buf.readResourceLocation();
			int craftTime = buf.readVarInt();

			int size = buf.readVarInt();
			Map<Integer, ResourceLocation> ingredients = new HashMap<>();
			for (int i = 0; i < size; i++) {
				int slot = buf.readVarInt();
				ResourceLocation id = buf.readResourceLocation();
				ingredients.put(slot, id);
			}

			ItemStack result = ItemStack.STREAM_CODEC.decode(buf);
			return new FirearmworktablerecipeRecipe(blueprint, craftTime, ingredients, result);
		}

		private static void toNetwork(RegistryFriendlyByteBuf buf, FirearmworktablerecipeRecipe recipe) {
			buf.writeResourceLocation(recipe.blueprint);
			buf.writeVarInt(recipe.craftTime);

			buf.writeVarInt(recipe.ingredients.size());
			for (Map.Entry<Integer, ResourceLocation> entry : recipe.ingredients.entrySet()) {
				buf.writeVarInt(entry.getKey());
				buf.writeResourceLocation(entry.getValue());
			}

			ItemStack.STREAM_CODEC.encode(buf, recipe.result);
		}
	}
}
