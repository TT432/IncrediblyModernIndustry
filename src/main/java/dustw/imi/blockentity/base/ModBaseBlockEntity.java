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
public abstract class ModBaseBlockEntity<SAVE, SYNC> extends BlockEntity {
    public ModBaseBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    @Setter(AccessLevel.PROTECTED)
    private SYNC sync;

    /**
     * 延迟实例化，注册自动同步的对象
     * @return 自动同步的对象
     */
    protected abstract SYNC registerSyncObject();
    protected abstract Class<SYNC> getSyncObjectClass();

    public SYNC getSync() {
        return sync == null ? sync = registerSyncObject() : sync;
    }

    @Setter(AccessLevel.PROTECTED)
    private SAVE save;

    /**
     * 延迟实例化，注册自动保存的对象
     * @return 自动保存的对象
     */
    protected abstract SAVE registerSaveObject();
    protected abstract Class<SAVE> getSaveObjectClass();

    public SAVE getSave() {
        return save == null ? save = registerSaveObject() : save;
    }

    public static final String SYNC_KEY = "auto_sync_object";
    public static final String SAVE_KEY = "auto_save_object";
    public static final String SYNC_SIGN = "sync";

    boolean isSyncTag(CompoundTag tag) {
        return tag.contains(SYNC_SIGN);
    }

    CompoundTag setSyncTag(CompoundTag tag) {
        tag.putBoolean(SYNC_SIGN, true);
        return tag;
    }

    @Override
    public CompoundTag getUpdateTag() {
        var result = super.getUpdateTag();

        if (getSync() != null) {
            result.putString(SYNC_KEY, JsonUtils.INSTANCE.noExpose.toJson(getSync()));
        }

        return setSyncTag(result);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);

        if (getSave() != null) {
            tag.putString(SAVE_KEY, JsonUtils.INSTANCE.noExpose.toJson(getSave()));
        }
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        if (isSyncTag(tag)) {
            if (tag.contains(SYNC_KEY)) {
                setSync(JsonUtils.INSTANCE.noExpose.fromJson(tag.getString(SYNC_KEY), getSyncObjectClass()));
            }
        }
        else {
            if (tag.contains(SAVE_KEY)) {
                setSave(JsonUtils.INSTANCE.noExpose.fromJson(tag.getString(SAVE_KEY), getSaveObjectClass()));
            }
        }
    }

    public void sync(Level level) {
        if (!level.isClientSide) {
            ClientboundBlockEntityDataPacket p = ClientboundBlockEntityDataPacket.create(this);
            ((ServerLevel)this.level).getChunkSource().chunkMap.getPlayers(new ChunkPos(getBlockPos()), false)
                    .forEach(k -> k.connection.send(p));
        }
    }

    protected void tick() {
        if (level != null && !level.isClientSide) {
            sync(level);
        }
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T t) {
        ((ModBaseBlockEntity) t).tick();
    }
}
