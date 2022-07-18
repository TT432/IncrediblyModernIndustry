package dustw.imi.recipe;

import com.google.gson.annotations.Expose;
import dustw.imi.Imi;
import dustw.imi.recipe.reg.ModRecipeTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import tt432.millennium.recipes.base.BaseRecipe;
import tt432.millennium.recipes.base.Recipe;

/**
 * @author DustW
 */
@EqualsAndHashCode(callSuper = true)
@Recipe(Imi.MOD_ID + ":grinder")
@AllArgsConstructor
@Data
public class GrinderRecipe extends BaseRecipe<Container> {
    @Expose Ingredient input;
    @Expose ItemStack output;
    @Expose int craftTick;
    @Expose int requiredEnergy;

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, input);
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.GRINDER.get();
    }
}
