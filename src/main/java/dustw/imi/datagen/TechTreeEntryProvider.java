package dustw.imi.datagen;

import dustw.imi.datagen.techtree.BaseTechTree;
import dustw.imi.datagen.techtree.ModTechTrees;
import lombok.AllArgsConstructor;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author DustW
 */
@AllArgsConstructor
public class TechTreeEntryProvider implements DataProvider {
    DataGenerator generator;

    final List<ModTechTrees> techTrees = new ArrayList<>();

    void addTechTrees() {
        techTrees.add(new BaseTechTree());
    }

    @Override
    public void run(HashCache pCache) {
        addTechTrees();

        techTrees.forEach(techTrees ->
                techTrees.getEntries().forEach((name, entry) ->
                        save(pCache, name, entry)));
    }

    protected void save(HashCache pCache, ResourceLocation name, Map.Entry<String, String> entry) {
        String json = entry.getKey();
        String subPath = entry.getValue();

        Path path = this.generator.getOutputFolder();

        saveTechEntry(pCache, json, path.resolve("data/" + name.getNamespace() + "/tech_tree_entries/" + subPath + "/" + name.getPath() + ".json"));
    }

    private static void saveTechEntry(HashCache pCache, String recipe, Path pPath) {
        try {
            String s1 = SHA1.hashUnencodedChars(recipe).toString();
            if (!Objects.equals(pCache.getHash(pPath), s1) || !Files.exists(pPath)) {
                Files.createDirectories(pPath.getParent());
                BufferedWriter bufferedwriter = Files.newBufferedWriter(pPath);

                try {
                    bufferedwriter.write(recipe);
                } catch (Throwable throwable1) {
                    try {
                        bufferedwriter.close();
                    } catch (Throwable throwable) {
                        throwable1.addSuppressed(throwable);
                    }

                    throw throwable1;
                }

                bufferedwriter.close();
            }

            pCache.putNew(pPath, s1);
        } catch (IOException ignored) {
        }
    }

    @Override
    public String getName() {
        return "Tech Tree Entry Provider";
    }
}
