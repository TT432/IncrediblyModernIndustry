package dustw.imi.block;

import dustw.imi.block.base.ModBaseEntityBlock;
import dustw.imi.blockentity.ThermalBlockEntity;
import dustw.imi.blockentity.reg.ModBlockEntities;

/**
 * @author DustW
 **/
public class ThermalBlock extends ModBaseEntityBlock<ThermalBlockEntity> {
    public ThermalBlock(Properties p_49795_) {
        super(p_49795_, ModBlockEntities.THERMAL);
    }
}
