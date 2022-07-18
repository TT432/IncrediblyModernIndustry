package dustw.imi.item.reg;

import dustw.imi.Imi;
import dustw.imi.block.reg.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author DustW
 **/
public class ModItems {
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, Imi.MOD_ID);

    public static final RegistryObject<BlockItem> BASE_CHEST = block(ModBlocks.BASE_CHEST);
    public static final RegistryObject<BlockItem> TECH_TREE_VIEWER = block(ModBlocks.TECH_TREE_VIEWER);
    public static final RegistryObject<BlockItem> THERMAL = block(ModBlocks.THERMAL);
    public static final RegistryObject<BlockItem> GRINDER = block(ModBlocks.GRINDER);
    public static final RegistryObject<BlockItem> CREATIVE_ENERGY = block(ModBlocks.CREATIVE_ENERGY);


    public static final RegistryObject<Item> IRON_POWDER = powder("iron");
    public static final RegistryObject<Item> GOLD_POWDER = powder("gold");
    public static final RegistryObject<Item> STONE_POWDER = powder("stone");
    public static final RegistryObject<Item> WOOD_POWDER = powder("wood");
    public static final RegistryObject<Item> COAL_POWDER = powder("coal");


    private static Item.Properties base() {
        return new Item.Properties().tab(Imi.TAB);
    }

    private static RegistryObject<BlockItem> block(RegistryObject<? extends Block> block) {
        return REGISTER.register(block.getId().getPath(), () -> new BlockItem(block.get(), base()));
    }

    private static RegistryObject<Item> powder(String name) {
        return REGISTER.register(name + "_powder", () -> new Item(base()));
    }
}
