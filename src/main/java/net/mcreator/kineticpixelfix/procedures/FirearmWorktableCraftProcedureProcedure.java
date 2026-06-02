package net.mcreator.kineticpixelfix.procedures;

import net.mcreator.kineticpixelfix.KineticPixelFixMod;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import net.mcreator.kineticpixelfix.recipe.FirearmWorktableRecipe;
import net.mcreator.kineticpixelfix.recipe.FirearmWorktableRecipeManager;
import net.mcreator.kineticpixelfix.world.inventory.FirearmworktableguiMenu;
import net.mcreator.kineticpixelfix.block.entity.FirearmworktableBlockEntity;

import java.util.Map;

public class FirearmWorktableCraftProcedureProcedure {

	/** Количество миллибакитов лавы, необходимое для запуска крафта. */
	public static final int LAVA_REQUIRED = 1000;

	public static void execute(Player entity) {
		System.out.println("Firearm craft button clicked");

		if (entity == null)
			return;

		if (!(entity.containerMenu instanceof FirearmworktableguiMenu menu))
			return;

		FirearmworktableBlockEntity blockEntity = menu.getFirearmWorktableEntity();
		if (blockEntity == null)
			return;

		if (blockEntity.isCrafting())
			return;

		// Проверяем наличие лавы ДО запуска крафта
		if (!blockEntity.hasLava(LAVA_REQUIRED)) {
			System.out.println("Not enough lava to craft! Need " + LAVA_REQUIRED + " mB, have "
					+ blockEntity.getLavaTank().getFluidAmount() + " mB");
			return;
		}

		for (FirearmWorktableRecipe recipe : FirearmWorktableRecipeManager.getRecipes()) {
			if (matches(menu, recipe)) {
				// Потребляем лаву
				blockEntity.drainLava(LAVA_REQUIRED);
				System.out.println("Consumed " + LAVA_REQUIRED + " mB lava. Remaining: "
						+ blockEntity.getLavaTank().getFluidAmount() + " mB");

				blockEntity.startCrafting(recipe.getCraftTime(), recipe.getId().toString());
				return;
			}
		}
	}

	private static boolean matches(FirearmworktableguiMenu menu, FirearmWorktableRecipe recipe) {
		ItemStack blueprint = menu.getSlots().get(0).getItem();

		System.out.println("Blueprint in slot 0: " + blueprint.getItem());
		System.out.println("Recipe blueprint: " + recipe.getBlueprint());

		if (blueprint.getItem() != recipe.getBlueprint())
			return false;

		for (Map.Entry<Integer, net.minecraft.world.item.Item> entry : recipe.getIngredients().entrySet()) {
			int slotId = entry.getKey();
			Slot slot = menu.getSlots().get(slotId);

			if (slot == null)
				return false;

			ItemStack stack = slot.getItem();

			System.out.println("Slot " + slotId + ": " + stack.getItem() + " / need: " + entry.getValue());

			if (stack.isEmpty())
				return false;

			if (stack.getItem() != entry.getValue())
				return false;
		}

		return true;
	}

	private static void craft(FirearmworktableguiMenu menu, FirearmWorktableRecipe recipe) {
		for (Integer slotId : recipe.getIngredients().keySet()) {
			Slot slot = menu.getSlots().get(slotId);
			if (slot != null) {
				slot.getItem().shrink(1);
				slot.setChanged();
			}
		}

		menu.getSlots().get(0).set(recipe.getResult());
		menu.getSlots().get(0).setChanged();

		System.out.println("Crafted firearm recipe: " + recipe.getId());
	}
}
