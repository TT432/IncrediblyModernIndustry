package dustw.imi.datagen;

import dustw.imi.block.reg.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author DustW
 **/
public class ModLootTables extends BaseLootTableProvider {

    public ModLootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void addTables() {
        simple(ModBlocks.THERMAL);
        simple(ModBlocks.TECH_TREE_VIEWER);
        simple(ModBlocks.BASE_CHEST);
    }

    void simple(RegistryObject<? extends Block> block) {
        lootTables.put(block.get(), createSimpleTable(block.getId().getPath(), block.get()));
    }
}