package dustw.imi.blockentity.reg;

import dustw.imi.Imi;
import dustw.imi.block.reg.ModBlocks;
import dustw.imi.blockentity.BaseChestBlockEntity;
import dustw.imi.blockentity.ThermalBlockEntity;
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
}
