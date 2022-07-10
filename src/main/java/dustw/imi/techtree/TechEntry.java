package dustw.imi.techtree;

import lombok.Data;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/**
 * 表示科技树一个节点的数据
 * @author DustW
 **/
@Data
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

    transient TranslatableComponent nameComponent;
    transient TranslatableComponent descriptionComponent;

    transient TechEntry prerequisiteInstance;
    transient List<TechEntry> children;

    public void initTranslatableComponents() {
        nameComponent = new TranslatableComponent(name);
        descriptionComponent = new TranslatableComponent(description);
    }
}
