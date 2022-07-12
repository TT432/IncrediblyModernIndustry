package dustw.imi.block.base;

import dustw.imi.blockentity.BaseChestBlockEntity;
import dustw.imi.blockentity.base.ModBaseBlockEntity;
import dustw.imi.blockentity.base.ModBaseMenuBlockEntity;
import icyllis.modernui.forge.MuiForgeApi;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * @author DustW
 **/
public class ModBaseEntityBlock<T extends ModBaseBlockEntity<?, ?>> extends Block implements EntityBlock {
    Supplier<BlockEntityType<T>> type;

    public ModBaseEntityBlock(Properties p_49795_, Supplier<BlockEntityType<T>> type) {
        super(p_49795_);
        this.type = type;
    }

    @Override
    public InteractionResult use(BlockState pState, Level level, @NotNull BlockPos pos, Player player, InteractionHand pHand, BlockHitResult pHit) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof ModBaseMenuBlockEntity e) {
            MuiForgeApi.openMenu(player, e, pos);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return type.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <Z extends BlockEntity> BlockEntityTicker<Z> getTicker(Level pLevel, BlockState pState, BlockEntityType<Z> pBlockEntityType) {
        return pBlockEntityType == type.get() ? BaseChestBlockEntity::tick : null;
    }
}
