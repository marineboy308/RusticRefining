package marineboy308.mod.container.gui;

import marineboy308.mod.container.ContainerBlockFilter;
import marineboy308.mod.tileentity.TileEntityBlockFilter;
import marineboy308.mod.util.Reference;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiBlockFilter extends GuiContainer {

	private static final ResourceLocation TEXTURES = new ResourceLocation(Reference.MOD_ID + ":textures/gui/block_filter.png");
	private final InventoryPlayer player;
	private final TileEntityBlockFilter tileentity;
	
	public GuiBlockFilter(InventoryPlayer player, TileEntityBlockFilter tileentity) {
		super(new ContainerBlockFilter(player, tileentity));
		this.player = player;
		this.tileentity = tileentity;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String tilename = this.tileentity.getDisplayName().getUnformattedText();
		this.fontRenderer.drawString(tilename, (this.xSize /2 - this.fontRenderer.getStringWidth(tilename) / 2), 8, 4210752);
		this.fontRenderer.drawString(this.player.getDisplayName().getUnformattedText(), 122, this.ySize - 114 + 2, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TEXTURES);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		
		int k = this.getFilteringLeftScaled(24);
		this.drawTexturedModalRect(this.guiLeft + 78, this.guiTop + 26, 176, 0, k + 1, 16);	
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
	
	private int getFilteringLeftScaled(int pixels) {
		int i = this.tileentity.getField(1);
		int j = this.tileentity.getField(2);
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}
}
