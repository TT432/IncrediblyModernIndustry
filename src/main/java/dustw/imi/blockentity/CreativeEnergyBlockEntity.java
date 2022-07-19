package dustw.imi.blockentity;

import dustw.imi.blockentity.base.ModBaseBlockEntity;
import dustw.imi.blockentity.reg.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tt432.millennium.sync.SyncDataManager;

import java.util.ArrayList;
import java.util.List;

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

        energyOutputTick(CAP.resolve().get());
    }

    @Override
    public List<ItemStack> getDrops() {
        return new ArrayList<>();
    }

    static final LazyOptional<EnergyStorage> CAP = LazyOptional.of(() -> new EnergyStorage(Integer.MAX_VALUE) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return maxReceive;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return maxExtract;
        }
    });

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return CapabilityEnergy.ENERGY.orEmpty(cap, CAP.cast());
    }
}
