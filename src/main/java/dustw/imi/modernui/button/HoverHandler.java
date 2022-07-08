package dustw.imi.modernui.button;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.PoseStack;
import icyllis.modernui.textmc.FormattedTextWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author DustW
 **/
@Mod.EventBusSubscriber(Dist.CLIENT)
public class HoverHandler {
    public static boolean hovered;
    public static Slot slot;

    @SubscribeEvent
    public static void onEvent(TickEvent.RenderTickEvent event) {
        if (hovered && slot != null && !slot.getItem().isEmpty()) {
            Minecraft mc = Minecraft.getInstance();
            RenderTarget mainRenderTarget = mc.getMainRenderTarget();

            ItemStack item = slot.getItem();
            List<Component> tooltipLines = item.getTooltipLines(mc.player, TooltipFlag.Default.NORMAL);

            int i = (int)(mc.mouseHandler.xpos() * (double)mc.getWindow().getGuiScaledWidth() / (double)mc.getWindow().getScreenWidth());
            int j = (int)(mc.mouseHandler.ypos() * (double)mc.getWindow().getGuiScaledHeight() / (double)mc.getWindow().getScreenHeight());

            ForgeHooksClient.onRenderTooltipPre(item, new PoseStack(), i, j,
                    mainRenderTarget.width, mainRenderTarget.height,
                    tooltipLines.stream().map(text -> ClientTooltipComponent.create(new FormattedTextWrapper(text))).collect(Collectors.toList()),
                    null, ForgeHooksClient.getTooltipFont(null, item, mc.font));
        }
    }
}
