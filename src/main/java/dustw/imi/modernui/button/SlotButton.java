package dustw.imi.modernui.button;

import icyllis.modernui.widget.RadioButton;
import lombok.AllArgsConstructor;
import net.minecraft.world.inventory.Slot;

/**
 * @author DustW
 **/
@AllArgsConstructor
public class SlotButton extends RadioButton {
    Slot slot;

    @Override
    public void onHoverChanged(boolean hovered) {
        super.onHoverChanged(hovered);

        HoverHandler.hovered = hovered;

        if (hovered) {
            HoverHandler.slot = slot;
        }
        else {
            HoverHandler.slot = null;
        }
    }
}
