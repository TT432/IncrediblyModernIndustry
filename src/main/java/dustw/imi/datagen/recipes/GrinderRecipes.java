package dustw.imi.datagen.recipes;

import dustw.imi.Imi;
import dustw.imi.item.reg.ModItems;
import dustw.imi.recipe.GrinderRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
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

        addRecipe("iron_powder",
                Ingredient.of(Items.RAW_IRON),
                new ItemStack(ModItems.IRON_POWDER.get(), 2),
                100, 8000);

        addRecipe("gold_powder",
                Ingredient.of(Items.RAW_GOLD),
                new ItemStack(ModItems.GOLD_POWDER.get(), 2),
                100, 8000);

        addRecipe("stone_powder",
                Ingredient.of(Items.STONE),
                new ItemStack(ModItems.STONE_POWDER.get(), 2),
                100, 8000);

        addRecipe("wood_powder",
                Ingredient.of(ItemTags.LOGS),
                new ItemStack(ModItems.WOOD_POWDER.get(), 4),
                100, 8000);

        addRecipe("coal_powder",
                Ingredient.of(ItemTags.COALS),
                new ItemStack(ModItems.COAL_POWDER.get(), 4),
                100, 8000);
    }

    void addRecipe(String name, Ingredient input, ItemStack output, int craftTick, int requiredEnergy) {
        GrinderRecipe recipe = new GrinderRecipe(input, output, craftTick, requiredEnergy);
        recipe.type = "imi:grinder";
        addRecipe(new ResourceLocation(Imi.MOD_ID, name), baseRecipe(recipe), "grinder");
    }
}
