package dustw.imi.dependencies.jei.category;

import dustw.imi.Imi;
import dustw.imi.dependencies.jei.JeiPlugin;
import dustw.imi.item.reg.ModItems;
import dustw.imi.recipe.GrinderRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * @author DustW
 **/
public class GrinderCategory extends BaseRecipeCategory<GrinderRecipe> {
    protected static final ResourceLocation BACKGROUND =
            new ResourceLocation(Imi.MOD_ID, "textures/gui/jei/grinder.png");

    public GrinderCategory(IGuiHelper helper) {
        super(JeiPlugin.GRINDER,
                helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.GRINDER.get())),
                helper.createDrawable(BACKGROUND, 0, 0, 100, 50));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GrinderRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 13, 18).addIngredients(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 71, 18).addItemStack(recipe.getResultItem());
    }
}
