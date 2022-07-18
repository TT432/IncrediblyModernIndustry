package dustw.imi.datagen;

import dustw.imi.Imi;
import dustw.imi.block.reg.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

/**
 * @author DustW
 **/
public class ModItemModels extends ItemModelProvider {

    public ModItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Imi.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //base();
        block(ModBlocks.THERMAL);
        block(ModBlocks.TECH_TREE_VIEWER);
        block(ModBlocks.BASE_CHEST);
        block(ModBlocks.GRINDER);
        block(ModBlocks.CREATIVE_ENERGY);
    }

    void block(RegistryObject<? extends Block> block) {
        withExistingParent(block.getId().getPath(), blockName(block.getId()));
    }

    ResourceLocation blockName(ResourceLocation block) {
        return new ResourceLocation(block.getNamespace(), "block/" + block.getPath());
    }

    void base() {
        ForgeRegistries.ITEMS.forEach(item -> {
            if (item.getRegistryName().getNamespace().equals(modid)) {
                if (item instanceof BlockItem) {
                    withExistingParent(item.getRegistryName().getPath(), modLoc("block/" + item.getRegistryName().getPath()));
                }
                else {
                    simpleTexture(() -> item);
                }
            }
        });
    }

    void simpleTexture(Supplier<Item> itemSupplier) {
        String name = itemSupplier.get().getRegistryName().getPath();

        try {
            ResourceLocation texture = modLoc("item/" + name);

            if (existingFileHelper.exists(texture, ModelProvider.TEXTURE)) {
                singleTexture(name, mcLoc("item/generated"), "layer0", texture);
            }
        }
        catch (Exception ignored) {}
    }
}