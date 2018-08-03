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
}
