package net.mcreator.kineticpixelfix.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;

public class IncompletedartItem extends Item {
   public IncompletedartItem() {
      super(new Properties().stacksTo(1).rarity(Rarity.COMMON));
   }
}
