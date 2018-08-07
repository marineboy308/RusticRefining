package marineboy308.mod.init;

import java.util.ArrayList;
import java.util.List;

import marineboy308.mod.objects.blocks.BlockBase;
import marineboy308.mod.objects.blocks.BlockOre;
import marineboy308.mod.objects.blocks.BlockOreOxidizable;
import marineboy308.mod.objects.machines.BatteryCharger.BlockCharger;
import marineboy308.mod.objects.machines.MaterialFilter.BlockFilter;
import marineboy308.mod.objects.storage.BatteryCell.BlockCell;
import marineboy308.mod.objects.storage.StorageCell.BlockStorageCell;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockInit {
	
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	//Blocks
	public static final Block CONDENSED_DIRT = new BlockBase("condensed_dirt",Material.ROCK,SoundType.STONE,1.0F,5.0F);
	
	//Ores
	public static final Block ORE_COPPER = new BlockOreOxidizable("ore_copper",Material.ROCK,SoundType.STONE,3.0F,15.0F);
	public static final Block ORE_COPPER_OXIDIZED = new BlockOre("ore_copper_oxidized",Material.ROCK,SoundType.STONE,2.0F,10.0F);
	public static final Block ORE_MAGNESIUM = new BlockOre("ore_magnesium",Material.ROCK,SoundType.STONE,3.0F,15.0F);
	
	//Machines
	public static final Block MATERIAL_FILTER = new BlockFilter("material_filter",true,true,true);
	public static final Block CHARGER_BATTERY = new BlockCharger("charger_battery",true,true,true);
	
	//Storage
	public static final Block CELL_BATTERY = new BlockCell("cell_battery",false,true,false);
	public static final Block CELL_STORAGE = new BlockStorageCell("cell_storage",false,true,false);
}