package dustw.imi.modernui;

import icyllis.modernui.widget.TextView;
import net.minecraft.world.entity.player.Inventory;

/**
 * @author DustW
 **/
public class UiUtils {
    public static TextView invName(Inventory inventory) {
        var invName = new TextView();
        invName.setText(inventory.getDisplayName().getString());
        invName.setTextSize(16);
        invName.setTextColor(0xFF808080);
        return invName;
    }
}
