package marineboy308.mod.objects.storage.StorageCell;

import marineboy308.mod.util.Reference;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiBlockStorageCell extends GuiContainer {

	private static final ResourceLocation TEXTURES = new ResourceLocation(Reference.MOD_ID + ":textures/gui/cell_storage.png");
	private final InventoryPlayer player;
	private final TileEntityBlockStorageCell tileentity;
	
	public GuiBlockStorageCell(InventoryPlayer player, TileEntityBlockStorageCell tileentity) {
		super(new ContainerBlockStorageCell(player, tileentity));
		this.player = player;
		this.tileentity = tileentity;
		this.xSize = 220;
		this.ySize = 186;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String tilename = this.tileentity.getDisplayName().getUnformattedText();
		this.fontRenderer.drawString(tilename, (this.xSize /2 - this.fontRenderer.getStringWidth(tilename) / 2) - 19, 8, 4210752);
		this.fontRenderer.drawString(this.player.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TEXTURES);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		
		int i = tileentity.getField(0);
		if (i == 0) {
			this.drawTexturedModalRect(this.guiLeft + 7, this.guiTop + 35, 0, 186, 180, 18);
			this.drawTexturedModalRect(this.guiLeft + 7, this.guiTop + 53, 0, 186, 180, 18);
			this.drawTexturedModalRect(this.guiLeft + 7, this.guiTop + 71, 0, 186, 180, 18);
		} else if (i == 1) {
			this.drawTexturedModalRect(this.guiLeft + 7, this.guiTop + 53, 0, 186, 180, 18);
			this.drawTexturedModalRect(this.guiLeft + 7, this.guiTop + 71, 0, 186, 180, 18);
		} else if (i == 2) {
			this.drawTexturedModalRect(this.guiLeft + 7, this.guiTop + 71, 0, 186, 180, 18);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
