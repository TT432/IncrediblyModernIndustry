package dustw.imi.block.reg;

import dustw.imi.Imi;
import dustw.imi.block.BaseChestBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author DustW
 **/
public class ModBlocks {
    public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, Imi.MOD_ID);

    public static final RegistryObject<BaseChestBlock> BASE_CHEST = REGISTER.register("base_chest",
            () -> new BaseChestBlock(BlockBehaviour.Properties.of(Material.STONE)));
}
