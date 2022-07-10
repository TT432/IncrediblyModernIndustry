package dustw.imi.mixin;

import dustw.imi.modernui.IMenuScreen;
import icyllis.modernui.fragment.Fragment;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nonnull;

/**
 * @author DustW
 **/
@Mixin(targets = "icyllis.modernui.forge.MenuScreen", remap = false)
public abstract class MixinMenuScreen implements IMenuScreen {
    @Shadow @Nonnull public abstract Fragment getFragment();

    @NotNull
    @Override
    public Fragment getFragmentImi() {
        return getFragment();
    }
}
