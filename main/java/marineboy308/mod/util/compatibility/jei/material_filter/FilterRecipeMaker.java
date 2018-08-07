package marineboy308.mod.util.compatibility.jei.material_filter;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;

import marineboy308.mod.init.ItemInit;
import marineboy308.mod.objects.items.ItemBattery;
import marineboy308.mod.objects.items.ItemFuel;
import marineboy308.mod.recipes.FilterRecipes;
import mezz.jei.api.IJeiHelpers;
import net.minecraft.item.ItemStack;

public class FilterRecipeMaker {

	public static List<FilterRecipe> getRecipes(IJeiHelpers helpers) {
		FilterRecipes instance = FilterRecipes.instance();
		Map<ItemStack,ItemStack> recipes = instance.getFilteringList();
		Map<ItemStack,ItemStack> chanceRecipes = instance.getFilteringChanceItemList();
		List<FilterRecipe> jeiRecipes = Lists.newArrayList();
		
		for(int index = 0; index < ItemInit.ITEMS.size(); index++) {
			if (ItemInit.ITEMS.get(index) instanceof ItemFuel || ItemInit.ITEMS.get(index) instanceof ItemBattery) {
				for(Entry<ItemStack,ItemStack> entry : recipes.entrySet()) {
					for(Entry<ItemStack,ItemStack> ent : chanceRecipes.entrySet()) {
						if (entry.getKey() == ent.getKey()) {
							ItemStack input = entry.getKey();
							ItemStack fuel = new ItemStack(ItemInit.ITEMS.get(index));
							ItemStack output1 = entry.getValue();
							ItemStack output2 = ent.getValue();
							List<ItemStack> inputs = Lists.newArrayList(input,fuel);
							List<ItemStack> outputs = Lists.newArrayList(output1,output2);
							FilterRecipe recipe = new FilterRecipe(inputs,outputs);
							jeiRecipes.add(recipe);
						}
					}
				}
			}
		}
		return jeiRecipes;
	}
}
