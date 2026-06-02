package net.mcreator.kineticpixelfix.world.inventory;

import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.inventory.DataSlot;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

import net.mcreator.kineticpixelfix.init.KineticPixelFixModMenus;
import net.mcreator.kineticpixelfix.init.KineticPixelFixModItems;

import java.util.function.Supplier;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class FirearmworktableguiMenu extends AbstractContainerMenu implements KineticPixelFixModMenus.MenuAccessor {
	private net.mcreator.kineticpixelfix.block.entity.FirearmworktableBlockEntity firearmWorktableEntity = null;

	public boolean isCrafting() {
		return firearmWorktableEntity != null && firearmWorktableEntity.isCrafting();
	}

	public void setCrafting(boolean crafting) {
		if (firearmWorktableEntity != null) {
			firearmWorktableEntity.setCrafting(crafting);
		}
	}

	public int getLavaAmount() {
	return syncedLavaAmount;
}

	public int getRemainingCraftingTicks() {
	return syncedRemainingCraftingTicks;
}

	public net.mcreator.kineticpixelfix.block.entity.FirearmworktableBlockEntity getFirearmWorktableEntity() {
		return firearmWorktableEntity;
	}

	public final Map<String, Object> menuState = new HashMap<>() {
		@Override
		public Object put(String key, Object value) {
			if (!this.containsKey(key) && this.size() >= 40)
				return null;
			return super.put(key, value);
		}
	};

	public final Level world;
	public final Player entity;
	public int x, y, z;
	private ContainerLevelAccess access = ContainerLevelAccess.NULL;
	private IItemHandler internal;
	private final Map<Integer, Slot> customSlots = new HashMap<>();
	private boolean bound = false;
	private Supplier<Boolean> boundItemMatcher = null;
	private Entity boundEntity = null;
	private BlockEntity boundBlockEntity = null;

	private int syncedRemainingCraftingTicks = 0;
	private int syncedLavaAmount = 0;

	private boolean hasBlueprint() {
		ItemStack blueprint = this.customSlots.get(0).getItem();

		return blueprint.getItem() == KineticPixelFixModItems.BLUEMAP_1.get()
				|| blueprint.getItem() == KineticPixelFixModItems.BLUEMAP_2.get()
				|| blueprint.getItem() == KineticPixelFixModItems.BLUEMAP_3.get()
				|| blueprint.getItem() == KineticPixelFixModItems.BLUEMAP_4.get();
	}

	public FirearmworktableguiMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
		super(KineticPixelFixModMenus.FIREARMWORKTABLEGUI.get(), id);
		this.entity = inv.player;
		this.world = inv.player.level();
		this.internal = new ItemStackHandler(33);
		BlockPos pos = null;

		if (extraData != null) {
			pos = extraData.readBlockPos();
			this.x = pos.getX();
			this.y = pos.getY();
			this.z = pos.getZ();
			access = ContainerLevelAccess.create(world, pos);
		}

		if (pos != null) {
			if (extraData.readableBytes() == 1) {
				byte hand = extraData.readByte();
				ItemStack itemstack = hand == 0 ? this.entity.getMainHandItem() : this.entity.getOffhandItem();
				this.boundItemMatcher = () -> itemstack == (hand == 0 ? this.entity.getMainHandItem() : this.entity.getOffhandItem());
				IItemHandler cap = itemstack.getCapability(Capabilities.ItemHandler.ITEM);
				if (cap != null) {
					this.internal = cap;
					this.bound = true;
				}
			} else if (extraData.readableBytes() > 1) {
				extraData.readByte();
				boundEntity = world.getEntity(extraData.readVarInt());
				if (boundEntity != null) {
					IItemHandler cap = boundEntity.getCapability(Capabilities.ItemHandler.ENTITY);
					if (cap != null) {
						this.internal = cap;
						this.bound = true;
					}
				}
			} else {
				boundBlockEntity = this.world.getBlockEntity(pos);

				if (boundBlockEntity instanceof net.mcreator.kineticpixelfix.block.entity.FirearmworktableBlockEntity firearmWorktable) {
					this.firearmWorktableEntity = firearmWorktable;
					this.addDataSlot(new DataSlot() {
	@Override
	public int get() {
		return firearmWorktableEntity != null ? firearmWorktableEntity.getRemainingCraftingTicks() : 0;
	}

	@Override
	public void set(int value) {
		syncedRemainingCraftingTicks = value;
	}
});
					this.internal = firearmWorktable.getInventory();
					this.addDataSlot(new DataSlot() {
	@Override
	public int get() {
		return firearmWorktableEntity != null ? firearmWorktableEntity.getLavaTank().getFluidAmount() : 0;
	}

	@Override
	public void set(int value) {
		syncedLavaAmount = value;
	}
});
					this.bound = true;
				} else if (boundBlockEntity instanceof BaseContainerBlockEntity baseContainerBlockEntity) {
					this.internal = new InvWrapper(baseContainerBlockEntity);
					this.bound = true;
				} else if (boundBlockEntity != null) {
					IItemHandler cap = this.world.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
					if (cap != null) {
						this.internal = cap;
						this.bound = true;
					}
				}
			}
		}

		this.customSlots.put(0, this.addSlot(new OneItemSlotItemHandler(internal, 0, 192, 138) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.getItem() == KineticPixelFixModItems.BLUEMAP_1.get()
						|| stack.getItem() == KineticPixelFixModItems.BLUEMAP_2.get()
						|| stack.getItem() == KineticPixelFixModItems.BLUEMAP_3.get()
						|| stack.getItem() == KineticPixelFixModItems.BLUEMAP_4.get();
			}
		}));

		this.customSlots.put(1, this.addSlot(new OneItemSlotItemHandler(internal, 1, 20, 0) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(2, this.addSlot(new OneItemSlotItemHandler(internal, 2, 37, 0) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(3, this.addSlot(new OneItemSlotItemHandler(internal, 3, 54, 0) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(4, this.addSlot(new OneItemSlotItemHandler(internal, 4, 71, 0) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(5, this.addSlot(new OneItemSlotItemHandler(internal, 5, 88, 0) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(6, this.addSlot(new OneItemSlotItemHandler(internal, 6, 105, 0) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(7, this.addSlot(new OneItemSlotItemHandler(internal, 7, 122, 0) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(8, this.addSlot(new OneItemSlotItemHandler(internal, 8, 139, 0) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(9, this.addSlot(new OneItemSlotItemHandler(internal, 9, 20, 17) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(10, this.addSlot(new OneItemSlotItemHandler(internal, 10, 37, 17) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(11, this.addSlot(new OneItemSlotItemHandler(internal, 11, 54, 17) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(12, this.addSlot(new OneItemSlotItemHandler(internal, 12, 71, 17) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(13, this.addSlot(new OneItemSlotItemHandler(internal, 13, 88, 17) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(14, this.addSlot(new OneItemSlotItemHandler(internal, 14, 105, 17) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(15, this.addSlot(new OneItemSlotItemHandler(internal, 15, 122, 17) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(16, this.addSlot(new OneItemSlotItemHandler(internal, 16, 139, 17) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(17, this.addSlot(new OneItemSlotItemHandler(internal, 17, 20, 34) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(18, this.addSlot(new OneItemSlotItemHandler(internal, 18, 37, 34) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(19, this.addSlot(new OneItemSlotItemHandler(internal, 19, 54, 34) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(20, this.addSlot(new OneItemSlotItemHandler(internal, 20, 71, 34) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(21, this.addSlot(new OneItemSlotItemHandler(internal, 21, 88, 34) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(22, this.addSlot(new OneItemSlotItemHandler(internal, 22, 105, 34) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(23, this.addSlot(new OneItemSlotItemHandler(internal, 23, 122, 34) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(24, this.addSlot(new OneItemSlotItemHandler(internal, 24, 139, 34) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(25, this.addSlot(new OneItemSlotItemHandler(internal, 25, 20, 51) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(26, this.addSlot(new OneItemSlotItemHandler(internal, 26, 37, 51) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(27, this.addSlot(new OneItemSlotItemHandler(internal, 27, 54, 51) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(28, this.addSlot(new OneItemSlotItemHandler(internal, 28, 71, 51) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(29, this.addSlot(new OneItemSlotItemHandler(internal, 29, 88, 51) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(30, this.addSlot(new OneItemSlotItemHandler(internal, 30, 105, 51) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(31, this.addSlot(new OneItemSlotItemHandler(internal, 31, 122, 51) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));
		this.customSlots.put(32, this.addSlot(new OneItemSlotItemHandler(internal, 32, 139, 51) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return FirearmworktableguiMenu.this.hasBlueprint() && !FirearmworktableguiMenu.this.isCrafting();
			}
		}));

		for (int si = 0; si < 3; ++si)
			for (int sj = 0; sj < 9; ++sj)
				this.addSlot(new Slot(inv, sj + (si + 1) * 9, 0 + 8 + sj * 18, -2 + 84 + si * 18));
		for (int si = 0; si < 9; ++si)
			this.addSlot(new Slot(inv, si, 0 + 8 + si * 18, -2 + 142));
	}

	@Override
	public void clicked(int slotId, int button, ClickType clickType, Player player) {
		if (this.isCrafting() && slotId >= 0 && slotId < 33)
			return;

		super.clicked(slotId, button, clickType, player);
	}

	@Override
	public boolean stillValid(Player player) {
		if (this.bound) {
			if (this.boundItemMatcher != null)
				return this.boundItemMatcher.get();
			else if (this.boundBlockEntity != null)
				return AbstractContainerMenu.stillValid(this.access, player, this.boundBlockEntity.getBlockState().getBlock());
			else if (this.boundEntity != null)
				return this.boundEntity.isAlive();
		}
		return true;
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		if (this.isCrafting())
			return ItemStack.EMPTY;

		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index < 33) {
				if (!this.moveItemStackTo(itemstack1, 33, this.slots.size(), true))
					return ItemStack.EMPTY;
				slot.onQuickCraft(itemstack1, itemstack);
			} else if (!this.moveItemStackTo(itemstack1, 0, 33, false)) {
				if (index < 33 + 27) {
					if (!this.moveItemStackTo(itemstack1, 33 + 27, this.slots.size(), true))
						return ItemStack.EMPTY;
				} else {
					if (!this.moveItemStackTo(itemstack1, 33, 33 + 27, false))
						return ItemStack.EMPTY;
				}
				return ItemStack.EMPTY;
			}
			if (itemstack1.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(playerIn, itemstack1);
		}
		return itemstack;
	}

	@Override
	protected boolean moveItemStackTo(ItemStack p_38904_, int p_38905_, int p_38906_, boolean p_38907_) {
		boolean flag = false;
		int i = p_38905_;
		if (p_38907_) {
			i = p_38906_ - 1;
		}
		if (p_38904_.isStackable()) {
			while (!p_38904_.isEmpty() && (p_38907_ ? i >= p_38905_ : i < p_38906_)) {
				Slot slot = this.slots.get(i);
				ItemStack itemstack = slot.getItem();
				if (slot.mayPlace(itemstack) && !itemstack.isEmpty() && ItemStack.isSameItemSameComponents(p_38904_, itemstack)) {
					int j = itemstack.getCount() + p_38904_.getCount();
					int k = slot.getMaxStackSize(itemstack);
					if (j <= k) {
						p_38904_.setCount(0);
						itemstack.setCount(j);
						slot.set(itemstack);
						flag = true;
					} else if (itemstack.getCount() < k) {
						p_38904_.shrink(k - itemstack.getCount());
						itemstack.setCount(k);
						slot.set(itemstack);
						flag = true;
					}
				}
				if (p_38907_) {
					i--;
				} else {
					i++;
				}
			}
		}
		if (!p_38904_.isEmpty()) {
			if (p_38907_) {
				i = p_38906_ - 1;
			} else {
				i = p_38905_;
			}
			while (p_38907_ ? i >= p_38905_ : i < p_38906_) {
				Slot slot1 = this.slots.get(i);
				ItemStack itemstack1 = slot1.getItem();
				if (itemstack1.isEmpty() && slot1.mayPlace(p_38904_)) {
					int l = slot1.getMaxStackSize(p_38904_);
					slot1.setByPlayer(p_38904_.split(Math.min(p_38904_.getCount(), l)));
					slot1.setChanged();
					flag = true;
					break;
				}
				if (p_38907_) {
					i--;
				} else {
					i++;
				}
			}
		}
		return flag;
	}

	@Override
	public void removed(Player playerIn) {
		super.removed(playerIn);
		if (!bound && playerIn instanceof ServerPlayer serverPlayer) {
			if (!serverPlayer.isAlive() || serverPlayer.hasDisconnected()) {
				for (int j = 0; j < internal.getSlots(); ++j) {
					playerIn.drop(internal.getStackInSlot(j), false);
					if (internal instanceof IItemHandlerModifiable ihm)
						ihm.setStackInSlot(j, ItemStack.EMPTY);
				}
			} else {
				for (int i = 0; i < internal.getSlots(); ++i) {
					playerIn.getInventory().placeItemBackInInventory(internal.getStackInSlot(i));
					if (internal instanceof IItemHandlerModifiable ihm)
						ihm.setStackInSlot(i, ItemStack.EMPTY);
				}
			}
		}
	}

	@Override
	public Map<Integer, Slot> getSlots() {
		return Collections.unmodifiableMap(customSlots);
	}

	@Override
	public Map<String, Object> getMenuState() {
		return menuState;
	}
	
	private static class OneItemSlotItemHandler extends SlotItemHandler {
	public OneItemSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}
}
}
