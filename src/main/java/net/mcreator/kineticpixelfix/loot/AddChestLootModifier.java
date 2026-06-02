package net.mcreator.kineticpixelfix.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.HashSet;
import java.util.Set;

public class AddChestLootModifier extends LootModifier {
	public static final MapCodec<AddChestLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
			LootModifier.codecStart(inst).and(
					ResourceLocation.CODEC.fieldOf("lootTable").forGetter(m -> m.lootTable)
			).apply(inst, AddChestLootModifier::new)
	);

	private static final ThreadLocal<Set<ResourceLocation>> ACTIVE_TABLES =
			ThreadLocal.withInitial(HashSet::new);

	private final ResourceLocation lootTable;

	public AddChestLootModifier(LootItemCondition[] conditions, ResourceLocation lootTable) {
		super(conditions);
		this.lootTable = lootTable;
	}

	@Override
protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
	ResourceLocation itemId = this.lootTable;

	if ("inject".equals(itemId.getPath().split("/")[0])) {
		String itemName = itemId.getPath().substring(itemId.getPath().lastIndexOf("/") + 1);
		itemId = ResourceLocation.fromNamespaceAndPath(itemId.getNamespace(), itemName);
	}

	var item = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(itemId);

	if (item != net.minecraft.world.item.Items.AIR) {
		generatedLoot.add(new ItemStack(item));
	}

	return generatedLoot;
}

	@Override
	public MapCodec<? extends IGlobalLootModifier> codec() {
		return CODEC;
	}
}