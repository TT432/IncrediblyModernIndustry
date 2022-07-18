package dustw.imi.datagen;

import dustw.imi.datagen.recipes.GrinderRecipes;
import dustw.imi.datagen.recipes.ModGenRecipes;
import dustw.imi.item.reg.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author DustW
 **/
public class ModRecipes extends RecipeProvider {

    public ModRecipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    protected List<ModGenRecipes> recipes = new ArrayList<>();

    protected void addCustomRecipes() {
        recipes.add(new GrinderRecipes());
    }

    protected void vanillaRecipes(Consumer<FinishedRecipe> consumer) {
        smelting(consumer, ModItems.IRON_POWDER.get(), Items.IRON_INGOT);
        smelting(consumer, ModItems.GOLD_POWDER.get(), Items.GOLD_INGOT);
    }

    void smelting(Consumer<FinishedRecipe> consumer, Item input, Item output) {
        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(input),
                output,
                .3F,
                200
        ).unlockedBy("has_stone", has(Blocks.DIORITE)).save(consumer);
    }

    @Override
    public void run(HashCache pCache) {
        super.run(pCache);

        recipes.forEach(recipes -> {
            recipes.getRecipes().forEach((name, entry) -> save(pCache, name, entry));
        });
    }

    protected void save(HashCache pCache, ResourceLocation name, Map.Entry<String, String> entry) {
        String json = entry.getKey();
        String subPath = entry.getValue();

        Path path = this.generator.getOutputFolder();

        saveRecipe(pCache, json, path.resolve("data/" + name.getNamespace() + "/recipes/" + subPath + "/" + name.getPath() + ".json"));
    }

    private static void saveRecipe(HashCache pCache, String recipe, Path pPath) {
        try {
            String s1 = SHA1.hashUnencodedChars(recipe).toString();
            if (!Objects.equals(pCache.getHash(pPath), s1) || !Files.exists(pPath)) {
                Files.createDirectories(pPath.getParent());
                BufferedWriter bufferedwriter = Files.newBufferedWriter(pPath);

                try {
                    bufferedwriter.write(recipe);
                } catch (Throwable throwable1) {
                    try {
                        bufferedwriter.close();
                    } catch (Throwable throwable) {
                        throwable1.addSuppressed(throwable);
                    }

                    throw throwable1;
                }

                bufferedwriter.close();
            }

            pCache.putNew(pPath, s1);
        } catch (IOException ignored) {
        }
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        addCustomRecipes();
        vanillaRecipes(consumer);
    }
}
