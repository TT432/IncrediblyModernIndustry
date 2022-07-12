package dustw.imi.block;

import dustw.imi.block.base.ModBaseEntityBlock;
import dustw.imi.blockentity.BaseChestBlockEntity;
import dustw.imi.blockentity.reg.ModBlockEntities;

/**
 * @author DustW
 **/
public class BaseChestBlock extends ModBaseEntityBlock<BaseChestBlockEntity> {
    public BaseChestBlock(Properties p_49795_) {
        super(p_49795_, ModBlockEntities.BASE_CHEST);
    }
}
