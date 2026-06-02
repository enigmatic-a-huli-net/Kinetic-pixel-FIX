package net.mcreator.kineticpixelfix.client.gui;

import net.neoforged.neoforge.network.PacketDistributor;

import net.mcreator.kineticpixelfix.init.KineticPixelFixModItems;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.GuiGraphics;

import net.mcreator.kineticpixelfix.recipe.FirearmWorktableRecipe;
import net.mcreator.kineticpixelfix.recipe.FirearmWorktableRecipeManager;
import java.util.Map;

import net.mcreator.kineticpixelfix.world.inventory.FirearmworktableguiMenu;
import net.mcreator.kineticpixelfix.network.FirearmworktableguiButtonMessage;
import net.mcreator.kineticpixelfix.init.KineticPixelFixModScreens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.network.chat.Component;
import java.util.List;

public class FirearmworktableguiScreen extends AbstractContainerScreen<FirearmworktableguiMenu> implements KineticPixelFixModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private ImageButton imagebutton_firearm_worktable_lever_gui;
    private int clientCraftingTicks = 0;
    private int getClientCraftTime() {
	for (FirearmWorktableRecipe recipe : FirearmWorktableRecipeManager.getRecipes()) {
		if (menu.getSlots().get(0).getItem().getItem() != recipe.getBlueprint())
			continue;

		boolean matches = true;

		for (Map.Entry<Integer, net.minecraft.world.item.Item> entry : recipe.getIngredients().entrySet()) {
			int slotId = entry.getKey();

			if (menu.getSlots().get(slotId).getItem().getItem() != entry.getValue()) {
				matches = false;
				break;
			}
		}

		if (matches)
			return recipe.getCraftTime();
	}

	return 0;
}
	private static final ResourceLocation BACKGROUND = ResourceLocation.parse("kinetic_pixel_fix:textures/screens/firearmworktablegui.png");
	private static final ResourceLocation IMAGE_0 = ResourceLocation.parse("kinetic_pixel_fix:textures/screens/firearm_worktable_gui.png");
	private static final ResourceLocation IMAGE_1 = ResourceLocation.parse("kinetic_pixel_fix:textures/screens/blueprint1.png");
	private static final ResourceLocation IMAGE_2 = ResourceLocation.parse("kinetic_pixel_fix:textures/screens/blueprint2.png");
	private static final ResourceLocation IMAGE_3 = ResourceLocation.parse("kinetic_pixel_fix:textures/screens/blueprint3.png");
	private static final ResourceLocation IMAGE_4 = ResourceLocation.parse("kinetic_pixel_fix:textures/screens/blueprint4.png");
	private static final ResourceLocation IMAGE_5 = ResourceLocation.parse("kinetic_pixel_fix:textures/screens/firearm_worktable_closed_gui.png");
	private static final ResourceLocation IMAGE_6 = ResourceLocation.parse("kinetic_pixel_fix:textures/screens/lava_tank_beck.png");
	private static final ResourceLocation IMAGE_7 = ResourceLocation.parse("kinetic_pixel_fix:textures/screens/lava.png");
	private static final ResourceLocation IMAGE_8 = ResourceLocation.parse("kinetic_pixel_fix:textures/screens/lava_tank.png");

	/** Максимальный объём танка лавы (мБ). Должен совпадать с FluidTank в BlockEntity. */
	private static final int LAVA_TANK_CAPACITY = 4000;
	/** Высота бара лавы в пикселях. */
	private static final int LAVA_BAR_HEIGHT = 48;
	/** Ширина бара лавы в пикселях. */
	private static final int LAVA_BAR_WIDTH = 16;
	/** Смещение бара относительно leftPos / topPos. Настрой под своё GUI. */
	private static final int LAVA_BAR_X = 198;
	private static final int LAVA_BAR_Y = 8;

	public FirearmworktableguiScreen(FirearmworktableguiMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 224;
		this.imageHeight = 164;
	}

	@Override
	public void updateMenuState(int elementType, String name, Object elementState) {
		menuStateUpdateActive = true;
		menuStateUpdateActive = false;
	}

@Override
public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
	super.render(guiGraphics, mouseX, mouseY, partialTicks);

int remainingTicks = menu.getRemainingCraftingTicks();

if (remainingTicks > 0) {
	RenderSystem.enableBlend();
	RenderSystem.defaultBlendFunc();

	guiGraphics.pose().pushPose();
	guiGraphics.pose().translate(0, 0, 400);

	guiGraphics.blit(IMAGE_5, this.leftPos + 0, this.topPos + -8, 0, 0, 224, 172, 224, 172);

	int totalSeconds = (remainingTicks + 19) / 20;
	int minutes = totalSeconds / 60;
	int seconds = totalSeconds % 60;

	String timeText = String.format("%02d:%02d", minutes, seconds);

	guiGraphics.drawString(
		this.font,
		timeText,
        this.leftPos + 113,
        this.topPos + 90,
		0xFFFFFF,
		true
	);

	guiGraphics.pose().popPose();

	RenderSystem.disableBlend();
}

	this.renderTooltip(guiGraphics, mouseX, mouseY);

	// Тултип бара лавы — зона совпадает с IMAGE_6 (фон индикатора)
	int lavaBarAbsX = this.leftPos + 224;
	int lavaBarAbsY = this.topPos + -8;
	if (mouseX >= lavaBarAbsX && mouseX <= lavaBarAbsX + 16
			&& mouseY >= lavaBarAbsY && mouseY <= lavaBarAbsY + 64) {
		int lava = menu.getLavaAmount();
		guiGraphics.renderTooltip(
			this.font,
			List.of(Component.literal("Lava: " + lava + " / " + LAVA_TANK_CAPACITY + " mB")),
			java.util.Optional.empty(),
			mouseX, mouseY
		);
	}
}

	

@Override
protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
	RenderSystem.setShaderColor(1, 1, 1, 1);
	RenderSystem.enableBlend();
	RenderSystem.defaultBlendFunc();


	// === Индикатор лавы: фон → лава (обрезанная снизу вверх) → рамка ===
	{
		int lavaAmount = menu.getLavaAmount();
		final int TEX_H = 64;
		final int TEX_W = 16;
		// fill: 0.0 (пусто) .. 1.0 (полный)
		float fill = Math.min(1.0f, (float) lavaAmount / LAVA_TANK_CAPACITY);
		int filledPx = (int) (TEX_H * fill);
		int srcY = TEX_H - filledPx; // пропускаем пустую верхнюю часть UV
		int screenX = this.leftPos + 224;
		int screenY = this.topPos + -8;

		// 1. Фон (всегда)
		guiGraphics.blit(IMAGE_6, screenX, screenY, 0, 0, TEX_W, TEX_H, TEX_W, TEX_H);
		// 2. Лава — только заполненная часть снизу
		if (filledPx > 0) {
			guiGraphics.blit(IMAGE_7,
				screenX, screenY + srcY,
				0, srcY,
				TEX_W, filledPx,
				TEX_W, TEX_H);
		}
		// 3. Рамка поверх (всегда)
		guiGraphics.blit(IMAGE_8, screenX, screenY, 0, 0, TEX_W, TEX_H, TEX_W, TEX_H);
	}


	guiGraphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
	guiGraphics.blit(IMAGE_0, this.leftPos + 0, this.topPos + -8, 0, 0, 224, 172, 224, 172);

	// Blueprint 1
	if (menu.getSlots().get(0).getItem().getItem() == KineticPixelFixModItems.BLUEMAP_1.get()) {
		guiGraphics.blit(IMAGE_1, this.leftPos + 0, this.topPos + -8, 0, 0, 176, 90, 176, 90);
	}

	// Blueprint 2
	
	if (menu.getSlots().get(0).getItem().getItem() == KineticPixelFixModItems.BLUEMAP_2.get()) {
		guiGraphics.blit(IMAGE_2, this.leftPos + 0, this.topPos + -8, 0, 0, 176, 90, 176, 90);
	}
	

	// Blueprint 3
	
	if (menu.getSlots().get(0).getItem().getItem() == KineticPixelFixModItems.BLUEMAP_3.get()) {
		guiGraphics.blit(IMAGE_3, this.leftPos + 0, this.topPos + -8, 0, 0, 176, 90, 176, 90);
	}
	

	// Blueprint 4
	
	if (menu.getSlots().get(0).getItem().getItem() == KineticPixelFixModItems.BLUEMAP_4.get()) {
		guiGraphics.blit(IMAGE_4, this.leftPos + 0, this.topPos + -8, 0, 0, 176, 90, 176, 90);
	}
	

	// Closed GUI overlay
	

	


	RenderSystem.disableBlend();
}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
	}

	@Override
	public void init() {
		super.init();
		imagebutton_firearm_worktable_lever_gui = new ImageButton(this.leftPos + 178, this.topPos + 44, 44, 86,
				new WidgetSprites(
	ResourceLocation.parse("kinetic_pixel_fix:textures/screens/firearm_worktable_lever_gui.png"),
	ResourceLocation.parse("kinetic_pixel_fix:textures/screens/firearm_worktable_lever_gui.png")
), e -> {
	PacketDistributor.sendToServer(new FirearmworktableguiButtonMessage(0, x, y, z));
}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_firearm_worktable_lever_gui);
	}
}