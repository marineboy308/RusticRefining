package marineboy308.mod.util.handlers;

import marineboy308.mod.init.ItemInit;
import net.minecraft.item.Item;

public class UpgradeHandler {

	public static boolean isItemUpgrade(Item item) {
		if(item == ItemInit.UPGRADE_SPEED_1) {
    		return true;
    	} else if(item == ItemInit.UPGRADE_SPEED_2) {
    		return true;
    	} else if(item == ItemInit.UPGRADE_SPEED_3) {
    		return true;
    	} else if(item == ItemInit.UPGRADE_DOUBLE_1) {
    		return true;
    	} else if(item == ItemInit.UPGRADE_DOUBLE_2) {
    		return true;
    	} else if(item == ItemInit.UPGRADE_DOUBLE_3) {
    		return true;
    	} else if(item == ItemInit.UPGRADE_CHANCE_1) {
    		return true;
    	} else if(item == ItemInit.UPGRADE_CHANCE_2) {
    		return true;
    	} else if(item == ItemInit.UPGRADE_CHANCE_3) {
    		return true;
    	} else if(item == ItemInit.UPGRADE_CHANCE_4) {
    		return true;
    	}
		return false;
	}
	
	public static boolean isItemBlockUpgrade(Item item) {
		if (item == ItemInit.BLOCKUPGRADE_IRON) {
			return true;
		} else if (item == ItemInit.BLOCKUPGRADE_GOLD) {
			return true;
		} else if (item == ItemInit.BLOCKUPGRADE_DIAMOND) {
			return true;
		} else if (item == ItemInit.BLOCKUPGRADE_EMERALD) {
			return true;
		}
		return false;
	}
	
	public static boolean canUpgradeBlock(Item item, int level) {
		if (item == ItemInit.BLOCKUPGRADE_IRON && level == 0) {
			return true;
		} else if (item == ItemInit.BLOCKUPGRADE_GOLD && level == 1) {
			return true;
		} else if (item == ItemInit.BLOCKUPGRADE_DIAMOND && level == 2) {
			return true;
		} else if (item == ItemInit.BLOCKUPGRADE_EMERALD && level == 3) {
			return true;
		}
		return false;
	}
	
	public static Item[] dropUpgrades(int level) {
		Item[] upgrades = new Item[] {};
		if (level == 1) {
			upgrades = new Item[] {ItemInit.BLOCKUPGRADE_IRON};
		} else if (level == 2) {
			upgrades = new Item[] {ItemInit.BLOCKUPGRADE_IRON,ItemInit.BLOCKUPGRADE_GOLD};
		} else if (level == 3) {
			upgrades = new Item[] {ItemInit.BLOCKUPGRADE_IRON,ItemInit.BLOCKUPGRADE_GOLD,ItemInit.BLOCKUPGRADE_DIAMOND};
		} else if (level == 4) {
			upgrades = new Item[] {ItemInit.BLOCKUPGRADE_IRON,ItemInit.BLOCKUPGRADE_GOLD,ItemInit.BLOCKUPGRADE_DIAMOND,ItemInit.BLOCKUPGRADE_EMERALD};
		}
		return upgrades;
	}
}
