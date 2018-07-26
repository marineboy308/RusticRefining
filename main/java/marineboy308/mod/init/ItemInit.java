package marineboy308.mod.init;

import java.util.ArrayList;
import java.util.List;

import marineboy308.mod.objects.items.ItemBase;
import marineboy308.mod.objects.items.ItemUpgrade;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextFormatting;

public class ItemInit {

	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	public static final Item BALL_DIRT = new ItemBase("ball_dirt");
	public static final Item PEBBLE = new ItemBase("pebble");
	public static final Item PILE_SAND = new ItemBase("pile_sand");
	
	public static final Item DUST_IRON = new ItemBase("dust_iron");
	public static final Item DUST_GOLD = new ItemBase("dust_gold");
	public static final Item DUST_DIAMOND = new ItemBase("dust_diamond");
	public static final Item DUST_EMERALD = new ItemBase("dust_emerald");
	
	public static final Item PILE_IRON = new ItemBase("pile_iron");
	public static final Item PILE_GOLD = new ItemBase("pile_gold");
	public static final Item PILE_DIAMOND = new ItemBase("pile_diamond");
	public static final Item PILE_EMERALD = new ItemBase("pile_emerald");
	
	public static final Item SHARD_IRON = new ItemBase("shard_iron");
	public static final Item SHARD_GOLD = new ItemBase("shard_gold");
	public static final Item SHARD_DIAMOND = new ItemBase("shard_diamond");
	public static final Item SHARD_EMERALD = new ItemBase("shard_emerald");
	
	public static final Item UPGRADE_SPEED_1 = new ItemUpgrade("upgrade_speed_1","Increases Machine Speed By 25%", TextFormatting.GREEN);
	public static final Item UPGRADE_SPEED_2 = new ItemUpgrade("upgrade_speed_2","Increases Machine Speed By 50%", TextFormatting.GREEN);
	public static final Item UPGRADE_SPEED_3 = new ItemUpgrade("upgrade_speed_3","Increases Machine Speed By 75%", TextFormatting.GREEN);
	
	public static final Item UPGRADE_DOUBLE_1 = new ItemUpgrade("upgrade_double_1","Increases Machine Output By 30%", TextFormatting.GREEN);
	public static final Item UPGRADE_DOUBLE_2 = new ItemUpgrade("upgrade_double_2","Increases Machine Output By 60%", TextFormatting.GREEN);
	public static final Item UPGRADE_DOUBLE_3 = new ItemUpgrade("upgrade_double_3","Increases Machine Output By 90%", TextFormatting.GREEN);
	
	public static final Item UPGRADE_CHANCE_1 = new ItemUpgrade("upgrade_chance_1","Increases Machine Secondary Output Chance By 10%", TextFormatting.GREEN);
	public static final Item UPGRADE_CHANCE_2 = new ItemUpgrade("upgrade_chance_2","Increases Machine Secondary Output Chance By 20%", TextFormatting.GREEN);
	public static final Item UPGRADE_CHANCE_3 = new ItemUpgrade("upgrade_chance_3","Increases Machine Secondary Output Chance By 40%", TextFormatting.GREEN);
	public static final Item UPGRADE_CHANCE_4 = new ItemUpgrade("upgrade_chance_4","Increases Machine Secondary Output Chance By 80%", TextFormatting.GREEN);
	
	public static final Item BLOCKUPGRADE_IRON = new ItemUpgrade("blockupgrade_iron","Upgrade Machines To Iron", TextFormatting.AQUA);
	public static final Item BLOCKUPGRADE_GOLD = new ItemUpgrade("blockupgrade_gold","Upgrade Machines From Iron To Gold", TextFormatting.AQUA);
	public static final Item BLOCKUPGRADE_DIAMOND = new ItemUpgrade("blockupgrade_diamond","Upgrade Machines From Gold To Diamond", TextFormatting.AQUA);
	public static final Item BLOCKUPGRADE_EMERALD = new ItemUpgrade("blockupgrade_emerald","Upgrade Machines From Diamond To Emerald", TextFormatting.AQUA);
}
