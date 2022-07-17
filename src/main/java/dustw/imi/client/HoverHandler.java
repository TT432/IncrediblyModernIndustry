package dustw.imi.client;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.PoseStack;
import dustw.imi.menu.BaseChestMenu;
import dustw.imi.menu.ThermalMenu;
import icyllis.modernui.textmc.FormattedTextWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
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
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null) {
            return;
        }

        int i = (int)(mc.mouseHandler.xpos() * (double)mc.getWindow().getGuiScaledWidth() / (double)mc.getWindow().getScreenWidth());
        int j = (int)(mc.mouseHandler.ypos() * (double)mc.getWindow().getGuiScaledHeight() / (double)mc.getWindow().getScreenHeight());

        AbstractContainerMenu currentMenu = player.containerMenu;

        boolean canRenderHovered = true;

        if (currentMenu instanceof BaseChestMenu || currentMenu instanceof ThermalMenu) {
            ItemStack carried = currentMenu.getCarried();

            if (!carried.isEmpty()) {
                canRenderHovered = false;

                mc.getItemRenderer().renderGuiItem(carried, i - 9, j - 9);
                
                if (carried.getCount() > 1) {
                    PoseStack poseStack = new PoseStack();
                    poseStack.translate(0, 0, 500);
                    mc.font.draw(poseStack, String.valueOf(carried.getCount()), i, j, 0xFFFFFFFF);
                }
            }
        }

        if (canRenderHovered && hovered && slot != null && !slot.getItem().isEmpty()) {
            RenderTarget mainRenderTarget = mc.getMainRenderTarget();

            ItemStack item = slot.getItem();
            List<Component> tooltipLines = item.getTooltipLines(player, TooltipFlag.Default.NORMAL);

            ForgeHooksClient.onRenderTooltipPre(item, new PoseStack(), i, j,
                    mainRenderTarget.width, mainRenderTarget.height,
                    tooltipLines.stream().map(text -> ClientTooltipComponent.create(new FormattedTextWrapper(text))).collect(Collectors.toList()),
                    null, ForgeHooksClient.getTooltipFont(null, item, mc.font));
        }
    }
}
