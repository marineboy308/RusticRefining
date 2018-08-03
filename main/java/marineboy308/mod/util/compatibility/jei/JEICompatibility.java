package marineboy308.mod.util.compatibility.jei;

import java.util.IllegalFormatException;

import marineboy308.mod.objects.machines.MaterialFilter.ContainerBlockFilter;
import marineboy308.mod.objects.machines.MaterialFilter.GuiBlockFilter;
import marineboy308.mod.util.compatibility.jei.material_filter.FilterRecipeCategory;
import marineboy308.mod.util.compatibility.jei.material_filter.FilterRecipeMaker;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.util.text.translation.I18n;

@SuppressWarnings("deprecation")
@JEIPlugin
public class JEICompatibility implements IModPlugin {

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		final IJeiHelpers helpers = registry.getJeiHelpers();
		final IGuiHelper gui = helpers.getGuiHelper();
		
		registry.addRecipeCategories(new FilterRecipeCategory(gui));
	}
	
	@Override
	public void register(IModRegistry registry) {
		final IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IRecipeTransferRegistry recipeTransfer = registry.getRecipeTransferRegistry();
		
		registry.addRecipes(FilterRecipeMaker.getRecipes(jeiHelpers), RecipeCategories.FILTER);
		registry.addRecipeClickArea(GuiBlockFilter.class, 62, 32, 32, 14, RecipeCategories.FILTER);
		recipeTransfer.addRecipeTransferHandler(ContainerBlockFilter.class, RecipeCategories.FILTER, 0, 0, 3, 36);
	}
	
	public static String translateToLocal(String key) {
		if (I18n.canTranslate(key)) return I18n.translateToLocal(key);
		else return I18n.translateToFallback(key);
	}
	
	public static String translateToLocalFormatted(String key, Object... format) {
		String s = translateToLocal(key);
		try {
			return String.format(s, format);
		} catch(IllegalFormatException e) {
			return "Format Error: " + s;
		}
	}
}
