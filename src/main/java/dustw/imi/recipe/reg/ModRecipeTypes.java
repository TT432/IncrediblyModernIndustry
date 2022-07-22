package dustw.imi.recipe.reg;

import dustw.imi.Imi;
import dustw.imi.recipe.GrinderRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import tt432.millennium.recipes.base.BaseRecipe;

/**
 * @author DustW
 */
public class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> REGISTER = DeferredRegister.create(Registry.RECIPE_TYPE.key(), Imi.MOD_ID);

    public static final RegistryObject<RecipeType<GrinderRecipe>> GRINDER = register("grinder");

    static <C extends Container, R extends BaseRecipe<C>> RegistryObject<RecipeType<R>> register(String name) {
        return REGISTER.register(name, () -> new RecipeType<>() {
            @Override
            public String toString() {
                return name;
            }
        });
    }
}
