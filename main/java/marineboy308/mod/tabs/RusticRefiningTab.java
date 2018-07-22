package marineboy308.mod.tabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RusticRefiningTab extends CreativeTabs {
	
	public RusticRefiningTab(String label) {
		super(label);
		this.setBackgroundImageName("rusticrefining.png");
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(Item.getItemFromBlock(Blocks.SAND));
	}
}
