package dustw.imi.blockentity.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import org.jetbrains.annotations.NotNull;
import tt432.millennium.sync.SyncDataManager;

import java.util.List;

/**
 * @author DustW
 **/
public abstract class ModBaseBlockEntity extends BlockEntity {
    SyncDataManager manager = new SyncDataManager();

    public ModBaseBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
        registerSyncData(manager);
    }

    protected abstract void registerSyncData(SyncDataManager manager);

    public static final String SYNC_SIGN = "sync";

    protected boolean isSyncTag(CompoundTag tag) {
        return tag.contains(SYNC_SIGN);
    }

    CompoundTag setSyncTag(CompoundTag tag) {
        tag.putBoolean(SYNC_SIGN, true);
        return tag;
    }

    @Override
    public CompoundTag getUpdateTag() {
        var result = super.getUpdateTag();

        manager.save(result, true, nextForce);

        if (nextForce) {
            nextForce = false;
        }

        if (!result.isEmpty()) {
            setSyncTag(result);
        }

        return result;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);

        manager.save(tag, false, true);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        if (isSyncTag(tag)) {
            manager.load(tag, true);
        }
        else {
            manager.load(tag, false);
            nextForce = true;
        }
    }

    public void sync(Level level) {
        if (!level.isClientSide) {
            ClientboundBlockEntityDataPacket p = ClientboundBlockEntityDataPacket.create(this);
            ((ServerLevel)this.level).getChunkSource().chunkMap.getPlayers(new ChunkPos(getBlockPos()), false)
                    .forEach(k -> k.connection.send(p));
        }
    }

    boolean nextForce;
    int forceTick;

    protected void tick() {
        if (level != null && !level.isClientSide) {
            if (forceTick++ > 60) {
                forceTick = 0;
                nextForce = true;
            }

            sync(level);
        }
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T t) {
        ((ModBaseBlockEntity) t).tick();
    }

    public abstract List<ItemStack> getDrops();

    protected final void energyOutputTick(EnergyStorage selfStorage) {
        if (getLevel() != null && !getLevel().isClientSide) {
            for (Direction value : Direction.values()) {
                BlockEntity blockEntity = getLevel().getBlockEntity(getBlockPos().offset(value.getNormal()));

                if (blockEntity != null) {
                    blockEntity.getCapability(CapabilityEnergy.ENERGY, value.getOpposite()).ifPresent(energy -> {
                        int max = energy.receiveEnergy(Integer.MAX_VALUE, true);
                        energy.receiveEnergy(selfStorage.extractEnergy(max, false), false);
                    });
                }
            }
        }
    }
}
