package dustw.imi.dependencies.jei;

import dustw.imi.Imi;
import dustw.imi.dependencies.jei.category.GrinderCategory;
import dustw.imi.item.reg.ModItems;
import dustw.imi.menu.GrinderMenu;
import dustw.imi.recipe.GrinderRecipe;
import dustw.imi.recipe.reg.ModRecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;

/**
 * @author DustW
 **/
@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {
    public static final RecipeType<GrinderRecipe> GRINDER =
            new RecipeType<>(new ResourceLocation(Imi.MOD_ID, "grinder"), GrinderRecipe.class);

    protected <C extends Container, T extends Recipe<C>> List<T> getRecipe(net.minecraft.world.item.crafting.RecipeType<T> recipeType) {
        return Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(recipeType);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new GrinderCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(GRINDER, getRecipe(ModRecipeTypes.GRINDER.get()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModItems.GRINDER.get()), GRINDER);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        //registration.addRecipeClickArea(AirCompressorGui.class, 79, 34, 24, 17, GRINDER);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(GrinderMenu.class, GRINDER,
                36, 1, 0, 36);
        //registration.addRecipeTransferHandler(ShakerMenu.class, COCKTAIL,
        //        36, 5, 0, 36);
        //registration.addRecipeTransferHandler(BrewingBarrelMenu.class, BREWING_BARREL,
        //        36, 6, 0, 36);
    }

    public static final ResourceLocation UID = new ResourceLocation(Imi.MOD_ID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }
}
