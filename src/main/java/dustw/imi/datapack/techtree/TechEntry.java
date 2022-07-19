package dustw.imi.datapack.techtree;

import lombok.Builder;
import lombok.Data;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表示科技树一个节点的数据
 * @author DustW
 **/
@Data
@Builder
public class TechEntry {
    /** 解锁所需的科技等级 */
    int techLevel;
    /** 解锁所需的前置节点的标识符 */
    ResourceLocation prerequisite;

    /** 唯一标识符 */
    ResourceLocation identifier;

    /** 图标在资源包的位置 */
    ResourceLocation iconPath;
    /** 名称（将使用 TranslatableComponent） */
    String name;
    /** 描述（将使用 TranslatableComponent） */
    String description;

    @Builder.Default Map<Ingredient, Integer> required = new HashMap<>();

    transient TranslatableComponent nameComponent;
    transient TranslatableComponent descriptionComponent;

    transient TechEntry prerequisiteInstance;
    transient List<TechEntry> children;

    public void initTranslatableComponents() {
        nameComponent = new TranslatableComponent(name);
        descriptionComponent = new TranslatableComponent(description);
    }
}
