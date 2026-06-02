package net.mcreator.kineticpixelfix.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;

public class BarrelItem extends Item {
   public BarrelItem() {
      super(new Properties().stacksTo(64).rarity(Rarity.COMMON));
   }
}
