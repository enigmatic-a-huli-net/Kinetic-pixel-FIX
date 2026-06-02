package net.mcreator.kineticpixelfix.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;

public class IncompletebarrelItem extends Item {
   public IncompletebarrelItem() {
      super(new Properties().stacksTo(1).rarity(Rarity.COMMON));
   }
}
