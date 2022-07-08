package dustw.imi.blockentity.base;

import lombok.AccessLevel;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import tt432.millennium.utils.json.JsonUtils;

/**
 * @author DustW
 **/
public abstract class ModBaseBlockEntity extends BlockEntity {
    public ModBaseBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    @Setter(AccessLevel.PROTECTED)
    public Object sync;

    /**
     * 延迟实例化，注册自动同步的对象
     * @return 自动同步的对象
     */
    protected abstract Object registerSyncObject();

    public Object getSync() {
        return sync == null ? sync = registerSyncObject() : sync;
    }

    public static final String SYNC_KEY = "auto_sync_object";

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);

        if (getSync() != null) {
            tag.putString(SYNC_KEY, JsonUtils.INSTANCE.noExpose.toJson(getSync()));
        }
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        if (tag.contains(SYNC_KEY)) {
            setSync(JsonUtils.INSTANCE.noExpose.fromJson(tag.getString(SYNC_KEY), getSync().getClass()));
        }
    }

    public void sync(Level level) {
        if (!level.isClientSide) {
            ClientboundBlockEntityDataPacket p = ClientboundBlockEntityDataPacket.create(this);
            ((ServerLevel)this.level).getChunkSource().chunkMap.getPlayers(new ChunkPos(getBlockPos()), false)
                    .forEach(k -> k.connection.send(p));
        }
    }

    void tick() {

    }

    public static <T extends ModBaseBlockEntity> void tick(Level level, BlockPos pos, BlockState state, T t) {
        t.tick();
    }
}
