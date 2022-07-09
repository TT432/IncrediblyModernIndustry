package dustw.imi.block;

import dustw.imi.blockentity.BaseChestBlockEntity;
import dustw.imi.blockentity.base.ModBaseMenuBlockEntity;
import dustw.imi.blockentity.reg.ModBlockEntities;
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

/**
 * @author DustW
 **/
public class BaseChestBlock extends Block implements EntityBlock {
    public BaseChestBlock(Properties p_49795_) {
        super(p_49795_);
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
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BaseChestBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pBlockEntityType == ModBlockEntities.BASE_CHEST.get() ? BaseChestBlockEntity::tick : null;
    }
}
