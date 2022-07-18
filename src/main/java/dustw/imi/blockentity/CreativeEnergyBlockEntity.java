package dustw.imi.blockentity;

import dustw.imi.blockentity.base.ModBaseBlockEntity;
import dustw.imi.blockentity.reg.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.energy.CapabilityEnergy;
import tt432.millennium.sync.SyncDataManager;

/**
 * @author DustW
 */
public class CreativeEnergyBlockEntity extends ModBaseBlockEntity {
    public CreativeEnergyBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.CREATIVE_ENERGY.get(), pWorldPosition, pBlockState);
    }

    @Override
    protected void registerSyncData(SyncDataManager manager) {

    }

    @Override
    protected void tick() {
        super.tick();

        if (getLevel() != null && !getLevel().isClientSide) {
            for (Direction value : Direction.values()) {
                BlockEntity blockEntity = getLevel().getBlockEntity(getBlockPos().offset(value.getNormal()));

                if (blockEntity != null) {
                    blockEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(energy -> {
                        energy.receiveEnergy(Integer.MAX_VALUE, false);
                    });
                }
            }
        }
    }
}
