package marineboy308.mod.util.compatibility;

import marineboy308.mod.init.ItemInit;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryCompatibility {

	public static void register() {
		OreDictionary.registerOre("materialTinyDirt", ItemInit.BALL_DIRT);
		OreDictionary.registerOre("materialTinyGravel", ItemInit.PEBBLE);
		OreDictionary.registerOre("materialTinySand", ItemInit.PILE_SAND);
		
		OreDictionary.registerOre("dustIron", ItemInit.DUST_IRON);
		OreDictionary.registerOre("dustGold", ItemInit.DUST_GOLD);
		OreDictionary.registerOre("dustDiamond", ItemInit.DUST_DIAMOND);
		OreDictionary.registerOre("dustEmerald", ItemInit.DUST_EMERALD);
		
		OreDictionary.registerOre("pileIron", ItemInit.PILE_IRON);
		OreDictionary.registerOre("pileGold", ItemInit.PILE_GOLD);
		OreDictionary.registerOre("pileDiamond", ItemInit.PILE_DIAMOND);
		OreDictionary.registerOre("pileEmerald", ItemInit.PILE_EMERALD);
		
		OreDictionary.registerOre("shardIron", ItemInit.SHARD_IRON);
		OreDictionary.registerOre("shardGold", ItemInit.SHARD_GOLD);
		OreDictionary.registerOre("shardDiamond", ItemInit.SHARD_DIAMOND);
		OreDictionary.registerOre("shardEmerald", ItemInit.SHARD_EMERALD);
	}
}
