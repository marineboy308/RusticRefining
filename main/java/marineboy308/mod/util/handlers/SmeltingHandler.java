package marineboy308.mod.util.handlers;

import marineboy308.mod.init.BlockInit;
import marineboy308.mod.init.ItemInit;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class SmeltingHandler {

	public static void registerSmelting() {
		GameRegistry.addSmelting(ItemInit.DUST_IRON, new ItemStack(Items.IRON_INGOT), 1.0F);
		GameRegistry.addSmelting(ItemInit.DUST_GOLD, new ItemStack(Items.GOLD_INGOT), 1.5F);
		GameRegistry.addSmelting(ItemInit.DUST_DIAMOND, new ItemStack(Items.DIAMOND), 2.0F);
		GameRegistry.addSmelting(ItemInit.DUST_EMERALD, new ItemStack(Items.EMERALD), 3.0F);
		
		GameRegistry.addSmelting(ItemInit.PILE_SAND, new ItemStack(Blocks.GLASS_PANE, 4), 1.0F);
		
		GameRegistry.addSmelting(BlockInit.ORE_COPPER, new ItemStack(ItemInit.INGOT_COPPER), 3.5F);
		GameRegistry.addSmelting(BlockInit.ORE_MAGNESIUM, new ItemStack(ItemInit.INGOT_MAGNESIUM), 3.5F);
	}
}
