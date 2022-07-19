package dustw.imi.datagen.techtree;

import com.google.common.collect.ImmutableMap;
import dustw.imi.Imi;
import dustw.imi.datapack.techtree.TechEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

/**
 * @author DustW
 */
public class BaseTechTree extends ModTechTrees {
    public BaseTechTree() {
        super("base");
    }

    @Override
    protected void addEntries() {
        TechEntry stones = addEntry(TechEntry.builder()
                .techLevel(0)
                .identifier(name("stones"))
                .name("imi.tech.stones")
                .description("imi.tech.stones.description")
                .required(ImmutableMap.of(Ingredient.of(Tags.Items.STONE), 64)).build());

        TechEntry ironIngots = addEntry(TechEntry.builder()
                .techLevel(0)
                .prerequisite(stones.getIdentifier())
                .identifier(name("iron_ingots"))
                .name("imi.tech.iron_ingots")
                .description("imi.tech.iron_ingots.description")
                .required(ImmutableMap.of(Ingredient.of(Items.IRON_INGOT), 64)).build());
    }

    ResourceLocation name(String path) {
        return new ResourceLocation(Imi.MOD_ID, path);
    }
}
