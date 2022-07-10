package dustw.imi.jei;

import dustw.imi.Imi;
import dustw.imi.modernui.IMenuScreen;
import dustw.imi.modernui.gui.TechTreeGui;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiProperties;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.gui.overlay.GuiProperties;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;

/**
 * @author DustW
 **/
@JeiPlugin
public class ModJeiPlugin implements IModPlugin {
    public static final ResourceLocation UID = new ResourceLocation(Imi.MOD_ID, "plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        try {
            Class<? extends AbstractContainerScreen<?>> screen =
                    (Class<? extends AbstractContainerScreen<?>>) Class.forName("icyllis.modernui.forge.MenuScreen");
            registration.addGuiScreenHandler(screen, this::get);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private IGuiProperties get(AbstractContainerScreen<?> guiScreen) {
        var fragment = ((IMenuScreen) guiScreen).getFragmentImi();

        if (fragment instanceof TechTreeGui) {
            return null;
        }

        return GuiProperties.create(guiScreen);
    }
}
