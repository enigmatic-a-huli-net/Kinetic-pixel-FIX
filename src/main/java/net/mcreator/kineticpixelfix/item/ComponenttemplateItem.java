package net.mcreator.kineticpixelfix.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;

public class ComponenttemplateItem extends Item {
   public ComponenttemplateItem() {
      super(new Properties().stacksTo(64).rarity(Rarity.RARE));
   }
}
