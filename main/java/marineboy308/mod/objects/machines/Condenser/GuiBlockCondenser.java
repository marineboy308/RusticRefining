package marineboy308.mod.objects.machines.Condenser;

import marineboy308.mod.util.Reference;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiBlockCondenser extends GuiContainer {

	private static final ResourceLocation TEXTURES = new ResourceLocation(Reference.MOD_ID + ":textures/gui/condenser.png");
	private final InventoryPlayer player;
	private final TileEntityBlockCondenser tileentity;
	
	public GuiBlockCondenser(InventoryPlayer player, TileEntityBlockCondenser tileentity) {
		super(new ContainerBlockCondenser(player, tileentity));
		this.player = player;
		this.tileentity = tileentity;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String tilename = this.tileentity.getDisplayName().getUnformattedText();
		this.fontRenderer.drawString(tilename, (this.xSize /2 - this.fontRenderer.getStringWidth(tilename) / 2) + 18, 8, 4210752);
		this.fontRenderer.drawString(this.player.getDisplayName().getUnformattedText(), 122, this.ySize - 98 + 2, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TEXTURES);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		
		int k = this.getCondensingLeftScaled(24);
		this.drawTexturedModalRect(this.guiLeft + 91, this.guiTop + 35, 176, 0, k + 1, 16);
		int j = this.getCondensingLeftScaled(18);
		this.drawTexturedModalRect(this.guiLeft + 43, this.guiTop + 62 + 17 - j, 176, 18 + 18 - j, 37, j + 1);
		this.drawTexturedModalRect(this.guiLeft + 43, this.guiTop + 4, 176, 38, 37, j + 1);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
	
	private int getCondensingLeftScaled(int pixels) {
		int i = this.tileentity.getField(1);
		int j = this.tileentity.getField(2);
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}
}
