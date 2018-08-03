package marineboy308.mod.init;

import java.util.ArrayList;
import java.util.List;

import marineboy308.mod.objects.blocks.BlockBase;
import marineboy308.mod.objects.machines.BatteryCharger.BlockCharger;
import marineboy308.mod.objects.machines.MaterialFilter.BlockFilter;
import marineboy308.mod.objects.storage.BatteryCell.BlockCell;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockInit {
	
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final Block CONDENSED_DIRT = new BlockBase("condensed_dirt", Material.ROCK,SoundType.STONE,1.0F,5.0F);
	
	public static final Block MATERIAL_FILTER = new BlockFilter("material_filter",true,true);
	public static final Block CHARGER_BATTERY = new BlockCharger("charger_battery",true,true);
	
	public static final Block CELL_BATTERY = new BlockCell("cell_battery",false,true);
}