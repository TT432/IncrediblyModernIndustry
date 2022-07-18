package dustw.imi.datapack.techtree;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dustw.imi.Imi;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tt432.millennium.utils.json.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author DustW
 **/
public class TechTreeManager extends SimpleJsonResourceReloadListener implements IForgeRegistryEntry<TechTreeManager> {
    private static final TechTreeManager INSTANCE = new TechTreeManager();

    public static final Codec<TechTreeManager> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(Codec.STRING.fieldOf("name").forGetter(TechTreeManager::save))
                    .apply(instance, TechTreeManager::load));

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

    private Map<ResourceLocation, JsonElement> jsonElementMap = new HashMap<>();

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> jsonElementMap,
                         @NotNull ResourceManager manager, @NotNull ProfilerFiller profiler) {
        this.jsonElementMap = jsonElementMap;

        load();
    }

    String save() {
        return JsonUtils.INSTANCE.noExpose.toJson(jsonElementMap);
    }

    static TechTreeManager load(String json) {
        instance().jsonElementMap = JsonUtils.INSTANCE.noExpose.fromJson(json,
                TypeToken.getParameterized(Map.class, ResourceLocation.class, JsonElement.class).getType());
        return load();
    }

    static TechTreeManager load() {
        instance().rootEntries.clear();
        instance().byName.clear();
        instance().byLevel.clear();

        instance().jsonElementMap.forEach((k, v) -> {
            var entry = JsonUtils.INSTANCE.noExpose.fromJson(v, TechEntry.class);

            if (entry.getIdentifier() == null) {
                throw new JsonParseException("can't found identifier in file : " + k.toString());
            }
            else if (instance().byName.containsKey(entry.getIdentifier())) {
                throw new JsonParseException("cannot use the same name, file : " + k.toString());
            }

            if (entry.getPrerequisite() == null) {
                if (entry.getTechLevel() > 0) {
                    throw new JsonParseException("root entry's level must be 0, file : " + k.toString());
                }

                instance().rootEntries.add(entry);
            }

            instance().byName.put(entry.identifier, entry);
            instance().byLevel.computeIfAbsent(entry.techLevel, i -> new ArrayList<>()).add(entry);

            entry.initTranslatableComponents();
        });

        instance().byName.values().forEach(entry -> {
            ResourceLocation prerequisiteName = entry.getPrerequisite();

            if (prerequisiteName != null) {
                TechEntry prerequisite = instance().byName.get(prerequisiteName);
                entry.setPrerequisiteInstance(prerequisite);

                if (prerequisite.getChildren() == null) {
                    prerequisite.setChildren(new ArrayList<>());
                }

                prerequisite.getChildren().add(entry);
            }
        });

        return instance();
    }

    static final ResourceLocation NAME = new ResourceLocation(Imi.MOD_ID, "tech_tree_manager");
    public static final ResourceKey<Registry<TechTreeManager>> REGISTRY_KEY = ResourceKey
            .createRegistryKey(NAME);

    @Override
    public TechTreeManager setRegistryName(ResourceLocation name) {
        return instance();
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return NAME;
    }

    @Override
    public Class<TechTreeManager> getRegistryType() {
        return TechTreeManager.class;
    }
}
