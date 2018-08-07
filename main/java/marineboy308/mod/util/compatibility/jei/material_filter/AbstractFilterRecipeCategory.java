package marineboy308.mod.util.compatibility.jei.material_filter;

import marineboy308.mod.util.Reference;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractFilterRecipeCategory<T extends IRecipeWrapper> implements IRecipeCategory<T>{

	public static final ResourceLocation TEXTURES = new ResourceLocation(Reference.MOD_ID + ":textures/gui/material_filter.png");
	
	protected static final int input = 0;
	protected static final int output1 = 1;
	protected static final int output2 = 2;
	protected static final int fuel = 3;
	
	protected final IDrawableStatic staticEnergy;
	protected final IDrawableAnimated animatedEnergy;
	protected final IDrawableAnimated animatedFilter;
	protected final IDrawableAnimated animatedArrow;
	
	public AbstractFilterRecipeCategory(IGuiHelper helper) {
		staticEnergy = helper.createDrawable(TEXTURES, 176, 20, 16, 48);
		animatedEnergy = helper.createAnimatedDrawable(staticEnergy, 500, IDrawableAnimated.StartDirection.TOP, true);
		
		
		
		IDrawableStatic staticFilter = helper.createDrawable(TEXTURES, 176, 0, 20, 20);
		animatedFilter = helper.createAnimatedDrawable(staticFilter, 200, IDrawableAnimated.StartDirection.BOTTOM, false);
		IDrawableStatic staticArrow = helper.createDrawable(TEXTURES, 196, 0, 32, 14);
		animatedArrow = helper.createAnimatedDrawable(staticArrow, 200, IDrawableAnimated.StartDirection.LEFT, false);
	}
}
