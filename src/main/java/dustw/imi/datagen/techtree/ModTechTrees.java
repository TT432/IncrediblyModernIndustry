package dustw.imi.datagen.techtree;

import dustw.imi.datapack.techtree.TechEntry;
import lombok.AllArgsConstructor;
import net.minecraft.resources.ResourceLocation;
import tt432.millennium.utils.json.JsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author DustW
 */
@AllArgsConstructor
public abstract class ModTechTrees {
    String subPath;

    private final Map<ResourceLocation, Map.Entry<String, String>> entries = new HashMap<>();

    public Map<ResourceLocation, Map.Entry<String, String>> getEntries() {
        addEntries();
        return entries;
    }

    protected abstract void addEntries();

    protected final TechEntry addEntry(TechEntry entry) {
        entries.put(entry.getIdentifier(), new HashMap.SimpleEntry<>(baseRecipe(entry), subPath));
        return entry;
    }

    protected String baseRecipe(TechEntry entry) {
        return JsonUtils.INSTANCE.pretty.toJson(JsonUtils.INSTANCE.noExpose.toJsonTree(entry));
    }
}
