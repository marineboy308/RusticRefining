package marineboy308.mod.init;

import java.util.ArrayList;
import java.util.List;

import marineboy308.mod.objects.items.ItemBase;
import marineboy308.mod.objects.items.ItemUpgrade;
import net.minecraft.item.Item;

public class ItemInit {

	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	public static final Item BALL_DIRT = new ItemBase("ball_dirt");
	public static final Item PEBBLE = new ItemBase("pebble");
	public static final Item PILE_SAND = new ItemBase("pile_sand");
	
	public static final Item SHARD_IRON = new ItemBase("shard_iron");
	public static final Item SHARD_GOLD = new ItemBase("shard_gold");
	public static final Item SHARD_DIAMOND = new ItemBase("shard_diamond");
	public static final Item SHARD_EMERALD = new ItemBase("shard_emerald");
	
	public static final Item UPGRADE_SPEED_1 = new ItemUpgrade("upgrade_speed_1","Increases Machine Speed By 25%");
	public static final Item UPGRADE_SPEED_2 = new ItemUpgrade("upgrade_speed_2","Increases Machine Speed By 50%");
	public static final Item UPGRADE_SPEED_3 = new ItemUpgrade("upgrade_speed_3","Increases Machine Speed By 75%");
	
	public static final Item UPGRADE_DOUBLE_1 = new ItemUpgrade("upgrade_double_1","Increases Machine Output By 30%");
	public static final Item UPGRADE_DOUBLE_2 = new ItemUpgrade("upgrade_double_2","Increases Machine Output By 60%");
	public static final Item UPGRADE_DOUBLE_3 = new ItemUpgrade("upgrade_double_3","Increases Machine Output By 90%");
	
	public static final Item UPGRADE_CHANCE_1 = new ItemUpgrade("upgrade_chance_1","Increases Machine Secondary Output Chance By 10%");
	public static final Item UPGRADE_CHANCE_2 = new ItemUpgrade("upgrade_chance_2","Increases Machine Secondary Output Chance By 20%");
	public static final Item UPGRADE_CHANCE_3 = new ItemUpgrade("upgrade_chance_3","Increases Machine Secondary Output Chance By 40%");
	public static final Item UPGRADE_CHANCE_4 = new ItemUpgrade("upgrade_chance_4","Increases Machine Secondary Output Chance By 80%");
}