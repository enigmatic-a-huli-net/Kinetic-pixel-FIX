/*
*    MCreator note: This file will be REGENERATED on each build.
*/
package net.mcreator.kineticpixelfix.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;

import net.mcreator.kineticpixelfix.item.*;
import net.mcreator.kineticpixelfix.KineticPixelFixMod;

public class KineticPixelFixModItems {
	public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(KineticPixelFixMod.MODID);
	public static final DeferredItem<Item> TEST;
	public static final DeferredItem<Item> FIREARMWORKTABLE;
	public static final DeferredItem<Item> BLUEMAP_1;
	public static final DeferredItem<Item> BLUEMAP_2;
	public static final DeferredItem<Item> BLUEMAP_3;
	public static final DeferredItem<Item> BLUEMAP_4;
	public static final DeferredItem<Item> GRAY_COTTON_SEEDS;
	public static final DeferredItem<Item> SMELTER_FIBER;
	public static final DeferredItem<Item> IRONWIRE;
	static {
		TEST = REGISTRY.register("test", TestItem::new);
		FIREARMWORKTABLE = block(KineticPixelFixModBlocks.FIREARMWORKTABLE);
		BLUEMAP_1 = REGISTRY.register("bluemap_1", Bluemap1Item::new);
		BLUEMAP_2 = REGISTRY.register("bluemap_2", Bluemap2Item::new);
		BLUEMAP_3 = REGISTRY.register("bluemap_3", Bluemap3Item::new);
		BLUEMAP_4 = REGISTRY.register("bluemap_4", Bluemap4Item::new);
		GRAY_COTTON_SEEDS = REGISTRY.register("gray_cotton_seeds", GrayCottonSeedsItem::new);
		SMELTER_FIBER = REGISTRY.register("smelter_fiber", SmelterFiberItem::new);
		IRONWIRE = REGISTRY.register("ironwire", IronwireItem::new);
	}
	// Start of user code block custom items
	public static final DeferredItem<Item> BARREL = REGISTRY.register("barrel", BarrelItem::new);
	public static final DeferredItem<Item> STRIKEREXCITER = REGISTRY.register("strikerexciter", StrikerexciterItem::new);
	public static final DeferredItem<Item> PISTONEXCITER = REGISTRY.register("pistonexciter", PistonexciterItem::new);
	public static final DeferredItem<Item> BRASSCOMPRESSIONSHEET = REGISTRY.register("brasscompressionsheet", BrasscompressionplateItem::new);
	public static final DeferredItem<Item> ANDESITEALLOYCOMPRESSIONSHEET = REGISTRY.register("andesitealloycompressionsheet", AndesitealloycompressionplateItem::new);
	public static final DeferredItem<Item> SPECIALSTEELCOMPRESSIONSHEET = REGISTRY.register("specialsteelcompressionsheet", SteelcompressionplateItem::new);
	public static final DeferredItem<Item> SPECIALSTEELINGOT = REGISTRY.register("specialsteelingot", SpecialsteelingotItem::new);
	public static final DeferredItem<Item> INCOMPLETEBARREL = REGISTRY.register("incompletebarrel", IncompletebarrelItem::new);
	public static final DeferredItem<Item> NITROPROPELLANT = REGISTRY.register("nitropropellant", NitropropellantItem::new);
	public static final DeferredItem<Item> INCOMPLETEBRASSCOMPRESSIONSHEET = REGISTRY.register("incompletebrasscompressionsheet", IncompletebrasscompressionsheetItem::new);
	public static final DeferredItem<Item> INCOMPLETEANDESITEALLOYCOMPRESSIONSHEET = REGISTRY.register("incompleteandesitealloycompressionsheet", IncompleteandesitecompressionsheetItem::new);
	public static final DeferredItem<Item> INCOMPLETESPECIALSTEELCOMPRESSIONSHEET = REGISTRY.register("incompletespecialsteelcompressionsheet", IncompletespecialsteelcompressionsheetItem::new);
	public static final DeferredItem<Item> WASTEDBARREL = REGISTRY.register("wastedbarrel", WastedbarrelItem::new);
	public static final DeferredItem<Item> GRAYCOTTON = REGISTRY.register("graycotton", GraycottonfruitItem::new);
	public static final DeferredItem<Item> INCOMPLETEWHISTLENOR = REGISTRY.register("incompletewhistlenor", IncompletewhistleItem::new);
	public static final DeferredItem<Item> INCOMPLETEWHISTLEMAX = REGISTRY.register("incompletewhistlemax", IncompletewhistlemaxItem::new);
	public static final DeferredItem<Item> INCOMPLETESHRAPNEL = REGISTRY.register("incompleteshrapnel", IncompleteshrapnelItem::new);
	public static final DeferredItem<Item> COMPONENTTEMPLATE = REGISTRY.register("componenttemplate", ComponenttemplateItem::new);
	public static final DeferredItem<Item> INCOMPLETEDART = REGISTRY.register("incompletedart", IncompletedartItem::new);
	public static final DeferredItem<Item> AMMUNITIONBOX = REGISTRY.register("ammunitionbox", AmmunitionboxItem::new);
	public static final DeferredItem<Item> GASCYLINDER = REGISTRY.register("gascylinder", GascylinderItem::new);
	public static final DeferredItem<Item> EMPTYGASCYLINDER = REGISTRY.register("emptygascylinder", EmptygascylinderItem::new);
	public static final DeferredItem<Item> INCOMPLETEENDERALLOYCOMPRESSIONSHEET = REGISTRY.register("incompleteenderalloycompressionsheet", IncompleteenderalloycompressionsheetItem::new);
	public static final DeferredItem<Item> BAMBOOSHELL = REGISTRY.register("bambooshell", BambooshellItem::new);
	public static final DeferredItem<Item> ENDERALLOYCOMPRESSIONSHEET = REGISTRY.register("enderalloycompressionsheet", EnderalloycompressionsheetItem::new);
	public static final DeferredItem<Item> ENDERALLOYINGOT = REGISTRY.register("enderalloyingot", EnderalloyingotItem::new);

	// End of user code block custom items
	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block) {
		return block(block, new Item.Properties());
	}

	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block, Item.Properties properties) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), properties));
	}
}