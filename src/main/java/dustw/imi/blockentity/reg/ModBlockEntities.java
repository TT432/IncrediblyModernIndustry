package dustw.imi.blockentity.reg;

import dustw.imi.Imi;
import dustw.imi.block.reg.ModBlocks;
import dustw.imi.blockentity.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author DustW
 **/
public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> REGISTER =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Imi.MOD_ID);

    public static final RegistryObject<BlockEntityType<BaseChestBlockEntity>> BASE_CHEST =
            REGISTER.register("base_chest", () -> BlockEntityType.Builder
                    .of(BaseChestBlockEntity::new, ModBlocks.BASE_CHEST.get()).build(null));

    public static final RegistryObject<BlockEntityType<ThermalBlockEntity>> THERMAL =
            REGISTER.register("thermal", () -> BlockEntityType.Builder
                    .of(ThermalBlockEntity::new, ModBlocks.THERMAL.get()).build(null));

    public static final RegistryObject<BlockEntityType<GrinderBlockEntity>> GRINDER =
            REGISTER.register("grinder", () -> BlockEntityType.Builder
                    .of(GrinderBlockEntity::new, ModBlocks.GRINDER.get()).build(null));

    public static final RegistryObject<BlockEntityType<CreativeEnergyBlockEntity>> CREATIVE_ENERGY =
            REGISTER.register("creative_energy", () -> BlockEntityType.Builder
                    .of(CreativeEnergyBlockEntity::new, ModBlocks.CREATIVE_ENERGY.get()).build(null));

    public static final RegistryObject<BlockEntityType<ElectricFurnaceBlocKEntity>> ELECTRIC_FURNACE =
            REGISTER.register("electric_furnace", () -> BlockEntityType.Builder
                    .of(ElectricFurnaceBlocKEntity::new, ModBlocks.ELECTRIC_FURNACE.get()).build(null));
}
