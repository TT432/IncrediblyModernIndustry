package dustw.imi.block;

import dustw.imi.menu.TechTreeViewerMenu;
import icyllis.modernui.forge.MuiForgeApi;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

/**
 * @author DustW
 **/
public class TechTreeViewer extends Block {
    public TechTreeViewer(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public InteractionResult use(BlockState pState, Level level, @NotNull BlockPos pos, Player player, InteractionHand pHand, BlockHitResult pHit) {
        if (!level.isClientSide) {
            MuiForgeApi.openMenu(player, (id, inv, p) -> new TechTreeViewerMenu(id, inv));
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
