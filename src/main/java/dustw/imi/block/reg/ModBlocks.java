package dustw.imi.block.reg;

import dustw.imi.Imi;
import dustw.imi.block.*;
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
            () -> new BaseChestBlock(stone()));

    public static final RegistryObject<TechTreeViewer> TECH_TREE_VIEWER = REGISTER.register("tech_tree_viewer",
            () -> new TechTreeViewer(stone()));

    public static final RegistryObject<ThermalBlock> THERMAL = REGISTER.register("thermal",
            () -> new ThermalBlock(stone()));

    public static final RegistryObject<GrinderBlock> GRINDER = REGISTER.register("grinder",
            () -> new GrinderBlock(stone()));

    public static final RegistryObject<CreativeEnergyBlock> CREATIVE_ENERGY = REGISTER.register("creative_energy",
            () -> new CreativeEnergyBlock(stone()));

    public static final RegistryObject<ElectricFurnaceBlock> ELECTRIC_FURNACE = REGISTER.register("electric_furnace",
            () -> new ElectricFurnaceBlock(stone()));

    static BlockBehaviour.Properties stone() {
        return BlockBehaviour.Properties.of(Material.STONE);
    }
}
