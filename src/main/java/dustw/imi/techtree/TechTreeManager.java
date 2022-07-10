package dustw.imi.techtree;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import tt432.millennium.utils.json.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author DustW
 **/
@Mod.EventBusSubscriber
public class TechTreeManager extends SimpleJsonResourceReloadListener {
    private static final TechTreeManager INSTANCE = new TechTreeManager();

    public TechTreeManager() {
        super(JsonUtils.INSTANCE.noExpose, "tech_tree_entries");
    }

    public static TechTreeManager instance() {
        return INSTANCE;
    }

    public static List<TechEntry> rootEntries() {
        return instance().rootEntries;
    }

    public static BiMap<ResourceLocation, TechEntry> byName() {
        return instance().byName;
    }

    private final List<TechEntry> rootEntries = new ArrayList<>();
    private final BiMap<ResourceLocation, TechEntry> byName = HashBiMap.create();
    private final Map<Integer, List<TechEntry>> byLevel = new HashMap<>();

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> jsonElementMap,
                         @NotNull ResourceManager manager, @NotNull ProfilerFiller profiler) {
        rootEntries.clear();
        byName.clear();
        byLevel.clear();

        jsonElementMap.forEach((k, v) -> {
            var entry = JsonUtils.INSTANCE.noExpose.fromJson(v, TechEntry.class);

            if (entry.getIdentifier() == null) {
                throw new JsonParseException("can't found identifier in file : " + k.toString());
            }
            else if (byName.containsKey(entry.getIdentifier())) {
                throw new JsonParseException("cannot use the same name, file : " + k.toString());
            }

            if (entry.getPrerequisite() == null) {
                if (entry.getTechLevel() > 0) {
                    throw new JsonParseException("root entry's level must be 0, file : " + k.toString());
                }

                rootEntries.add(entry);
            }

            byName.put(entry.identifier, entry);
            byLevel.computeIfAbsent(entry.techLevel, i -> new ArrayList<>()).add(entry);

            entry.initTranslatableComponents();
        });

        byName.values().forEach(entry -> {
            ResourceLocation prerequisiteName = entry.getPrerequisite();

            if (prerequisiteName != null) {
                TechEntry prerequisite = byName.get(prerequisiteName);
                entry.setPrerequisiteInstance(prerequisite);

                if (prerequisite.getChildren() == null) {
                    prerequisite.setChildren(new ArrayList<>());
                }

                prerequisite.getChildren().add(entry);
            }
        });
    }

    @SubscribeEvent
    public static void onEvent(AddReloadListenerEvent event) {
        event.addListener(instance());
    }
}
