package dustw.imi.datagen;

import dustw.imi.block.reg.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author DustW
 **/
public class ModBlockStates extends BlockStateProvider {

    public ModBlockStates(DataGenerator gen, ExistingFileHelper helper) {
        super(gen, DataGenerators.MOD_ID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        //simpleBlock(AvarusBlocks.RED_DIRT.get());
        //simpleBlock(AvarusBlocks.GREEN_DIRT.get());
        //simpleBlock(AvarusBlocks.BLUE_DIRT.get());
        //simpleBlock(AvarusBlocks.YELLOW_DIRT.get());

        simpleSideBlock(ModBlocks.BASE_CHEST);
        simpleSideBlock(ModBlocks.TECH_TREE_VIEWER);
        simpleSideBlock(ModBlocks.THERMAL);
        simpleSideBlock(ModBlocks.CREATIVE_ENERGY);
        simpleSideBlock(ModBlocks.GRINDER);
        simpleSideBlock(ModBlocks.ELECTRIC_FURNACE);
    }

    public static final ResourceLocation TOP = new ResourceLocation(DataGenerators.MOD_ID, "block/top");

    void simpleSideBlock(RegistryObject<? extends Block> block) {
        String name = block.getId().getPath();
        ResourceLocation texture = blockTexture(block.get());
        BlockModelBuilder model = models().cubeBottomTop(name, texture, TOP, TOP);

        getVariantBuilder(block.get()).partialState().setModels(new ConfiguredModel(model));
    }

    ResourceLocation withSuffix(ResourceLocation rl, String suffix) {
        return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
    }
}