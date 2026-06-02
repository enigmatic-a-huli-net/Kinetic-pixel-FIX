package net.mcreator.kineticpixelfix.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;

public class BambooshellItem extends Item {
   public BambooshellItem() {
      super(new Properties().stacksTo(64).rarity(Rarity.COMMON));
   }
}
