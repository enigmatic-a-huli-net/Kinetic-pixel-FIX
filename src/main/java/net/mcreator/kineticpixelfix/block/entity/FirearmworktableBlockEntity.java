package net.mcreator.kineticpixelfix.block.entity;
//setStackInSlot
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

import net.mcreator.kineticpixelfix.init.KineticPixelFixModBlockEntities;

public class FirearmworktableBlockEntity extends BlockEntity {
	private final ItemStackHandler inventory = new ItemStackHandler(33) {
		
		@Override
public int getSlotLimit(int slot) {
	return 1;
}
		
	@Override
	protected void onContentsChanged(int slot) {
		setChanged();

		if (level != null) {
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
		}
	}
};

	/** Танк для лавы. Максимальный объём 4000 мБ. */
	private final FluidTank lavaTank = new FluidTank(4000) {
		@Override
		public boolean isFluidValid(FluidStack stack) {
			return stack.getFluid() == Fluids.LAVA;
		}

		@Override
		protected void onContentsChanged() {
			setChanged();
			if (level != null) {
				level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
			}
		}
	};

    private long craftingStartedAt = 0;
	private boolean crafting = false;
	private int craftingProgress = 0;
	private int craftingTime = 0;
	private String activeRecipeId = "";

	public FirearmworktableBlockEntity(BlockPos pos, BlockState state) {
		super(KineticPixelFixModBlockEntities.FIREARMWORKTABLE.get(), pos, state);
	}

	public ItemStackHandler getInventory() {
		return inventory;
	}

	/** Возвращает танк с лавой (для capability и GUI). */
	public FluidTank getLavaTank() {
		return lavaTank;
	}

	/**
	 * Проверяет, есть ли минимум {@code amount} мБ лавы в танке.
	 */
	public boolean hasLava(int amount) {
		FluidStack fluid = lavaTank.getFluid();
		return fluid.getFluid() == Fluids.LAVA && fluid.getAmount() >= amount;
	}

	/**
	 * Потребляет {@code amount} мБ лавы. Возвращает true при успехе.
	 */
	public boolean drainLava(int amount) {
		if (!hasLava(amount)) return false;
		lavaTank.drain(new FluidStack(Fluids.LAVA, amount), IFluidHandler.FluidAction.EXECUTE);
		return true;
	}

	public boolean isCrafting() {
		return crafting;
	}

	public void setCrafting(boolean crafting) {
	this.crafting = crafting;

	if (!crafting) {
		this.craftingProgress = 0;
		this.craftingTime = 0;
		this.craftingStartedAt = 0;
	}

	setChanged();

	if (level != null)
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
}
	
public void startCrafting(int time, String recipeId) {
	this.crafting = true;
	this.craftingProgress = 0;
	this.craftingTime = time;
	this.activeRecipeId = recipeId;
	this.craftingStartedAt = level != null ? level.getGameTime() : 0;
	setChanged();
	playSound("furnace_lever");
    playSound("furnace_door");

	if (level != null)
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
}

public int getRemainingCraftingTicks() {
	if (!crafting)
		return 0;

	if (level != null) {
		int passed = (int) (level.getGameTime() - craftingStartedAt);
		return Math.max(0, craftingTime - passed);
	}

	return Math.max(0, craftingTime - craftingProgress);
}

	public int getCraftingProgress() {
		return craftingProgress;
	}

	public int getCraftingTime() {
		return craftingTime;
	}

	public void setCraftingTime(int craftingTime) {
		this.craftingTime = craftingTime;
		setChanged();
	}

public void resetCrafting() {
	this.crafting = false;
	this.craftingProgress = 0;
	this.craftingTime = 0;
	this.craftingStartedAt = 0;
	this.activeRecipeId = "";

	setChanged();

	if (level != null)
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
}
	
	public static void tick(net.minecraft.world.level.Level level, BlockPos pos, BlockState state, FirearmworktableBlockEntity blockEntity) {
	if (level.isClientSide())
		return;

	if (!blockEntity.crafting)
		return;

	int remaining = blockEntity.getRemainingCraftingTicks();

if (remaining > 0) {
	if (level.getGameTime() % 18 == 0) {
		blockEntity.playSound("bump");
	}
	return;
}

blockEntity.finishCrafting();
}



private void finishCrafting() {
	if (activeRecipeId == null || activeRecipeId.isEmpty()) {
		resetCrafting();
		return;
	}

	for (net.mcreator.kineticpixelfix.recipe.FirearmWorktableRecipe recipe : net.mcreator.kineticpixelfix.recipe.FirearmWorktableRecipeManager.getRecipes()) {
		if (!recipe.getId().toString().equals(activeRecipeId))
			continue;

		if (inventory.getStackInSlot(0).getItem() != recipe.getBlueprint()) {
			resetCrafting();
			return;
		}

		for (java.util.Map.Entry<Integer, net.minecraft.world.item.Item> entry : recipe.getIngredients().entrySet()) {
			int slotId = entry.getKey();

			if (inventory.getStackInSlot(slotId).isEmpty() || inventory.getStackInSlot(slotId).getItem() != entry.getValue()) {
				resetCrafting();
				return;
			}
		}

		for (Integer slotId : recipe.getIngredients().keySet()) {
			inventory.getStackInSlot(slotId).shrink(1);
		}

		inventory.setStackInSlot(0, recipe.getResult().copy());

        playSound("furnace_door");

        resetCrafting();

		String finishedRecipe = activeRecipeId;
System.out.println("Finished firearm worktable recipe: " + finishedRecipe);
		return;
	}

	resetCrafting();
}

private void playSound(String name) {
	if (level == null || level.isClientSide())
		return;

	level.playSound(
			null,
			worldPosition,
			BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("kinetic_pixel_fix:" + name)),
SoundSource.BLOCKS,
1.0F,
1.0F
	);
}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		tag.putLong("CraftingStartedAt", craftingStartedAt);
		tag.put("Inventory", inventory.serializeNBT(registries));
		tag.putBoolean("Crafting", crafting);
		tag.putInt("CraftingProgress", craftingProgress);
		tag.putInt("CraftingTime", craftingTime);
		tag.putString("ActiveRecipeId", activeRecipeId);
		// Сохраняем танк с лавой
		CompoundTag tankTag = new CompoundTag();
		lavaTank.writeToNBT(registries, tankTag);
		tag.put("LavaTank", tankTag);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);

		if (tag.contains("Inventory")) {
			inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
		}
        craftingStartedAt = tag.getLong("CraftingStartedAt");
		crafting = tag.getBoolean("Crafting");
		craftingProgress = tag.getInt("CraftingProgress");
		craftingTime = tag.getInt("CraftingTime");
		activeRecipeId = tag.getString("ActiveRecipeId");
		// Загружаем танк с лавой
		if (tag.contains("LavaTank")) {
			lavaTank.readFromNBT(registries, tag.getCompound("LavaTank"));
		}
	}
}
