package dustw.imi.datagen.recipes;

import dustw.imi.Imi;
import dustw.imi.recipe.GrinderRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * @author DustW
 */
public class GrinderRecipes extends ModGenRecipes {
    @Override
    protected void addRecipes() {
        addRecipe("iron_block_to_iron_ingot",
                Ingredient.of(Items.IRON_BLOCK),
                new ItemStack(Items.IRON_INGOT, 9),
                100, 8000);
    }

    void addRecipe(String name, Ingredient input, ItemStack output, int craftTick, int requiredEnergy) {
        GrinderRecipe recipe = new GrinderRecipe(input, output, craftTick, requiredEnergy);
        recipe.type = "imi:grinder";
        addRecipe(new ResourceLocation(Imi.MOD_ID, name), baseRecipe(recipe), "grinder");
    }
}
