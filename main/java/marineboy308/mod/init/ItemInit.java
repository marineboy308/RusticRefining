package marineboy308.mod.init;

import java.util.ArrayList;
import java.util.List;

import marineboy308.mod.objects.items.ItemBase;
import net.minecraft.item.Item;

public class ItemInit {

	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	public static final Item BALL_DIRT = new ItemBase("ball_dirt");
	public static final Item PEBBLE = new ItemBase("pebble");
	
	public static final Item SHARD_IRON = new ItemBase("shard_iron");
	public static final Item SHARD_GOLD = new ItemBase("shard_gold");
	public static final Item SHARD_DIAMOND = new ItemBase("shard_diamond");
	public static final Item SHARD_EMERALD = new ItemBase("shard_emerald");
}
