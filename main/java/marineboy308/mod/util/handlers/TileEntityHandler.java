package marineboy308.mod.util.handlers;

import marineboy308.mod.objects.blocks.tileentity.TileEntityOreOxidizable;
import marineboy308.mod.objects.machines.BatteryCharger.TileEntityBlockCharger;
import marineboy308.mod.objects.machines.MaterialFilter.TileEntityBlockFilter;
import marineboy308.mod.objects.storage.BatteryCell.TileEntityBlockCell;
import marineboy308.mod.objects.storage.StorageCell.TileEntityBlockStorageCell;
import marineboy308.mod.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityHandler {

	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityBlockFilter.class, new ResourceLocation(Reference.MOD_ID + ":block_filter"));
		GameRegistry.registerTileEntity(TileEntityBlockCharger.class, new ResourceLocation(Reference.MOD_ID + ":block_charger"));
		GameRegistry.registerTileEntity(TileEntityBlockCell.class, new ResourceLocation(Reference.MOD_ID + ":block_cell"));
		GameRegistry.registerTileEntity(TileEntityOreOxidizable.class, new ResourceLocation(Reference.MOD_ID + ":ore_oxidizable"));
		GameRegistry.registerTileEntity(TileEntityBlockStorageCell.class, new ResourceLocation(Reference.MOD_ID + ":block_cell_storage"));
	}
}
