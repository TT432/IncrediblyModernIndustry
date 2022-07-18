package dustw.imi.blockentity;

import dustw.imi.blockentity.base.ModBaseMenuBlockEntity;
import dustw.imi.blockentity.component.ChangeListenerEnergyStorage;
import dustw.imi.blockentity.reg.ModBlockEntities;
import dustw.imi.menu.GrinderMenu;
import dustw.imi.recipe.GrinderRecipe;
import dustw.imi.recipe.reg.ModRecipeTypes;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tt432.millennium.sync.SyncDataManager;
import tt432.millennium.sync.object.ItemStackHandlerSyncData;
import tt432.millennium.sync.object.StringSyncData;
import tt432.millennium.sync.primitive.IntSyncData;

import java.util.Optional;

/**
 * @author DustW
 */
public class GrinderBlockEntity extends ModBaseMenuBlockEntity {
    public static final int MAX_ENERGY = 20_0000;

    public GrinderBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.GRINDER.get(), pWorldPosition, pBlockState);
    }

    @Getter
    ItemStackHandlerSyncData input;
    @Getter
    ItemStackHandlerSyncData output;

    @Getter
    IntSyncData craftTick;
    @Getter
    IntSyncData maxCraftTick;

    StringSyncData recipe;
    GrinderRecipe _recipe;

    public GrinderRecipe getRecipe() {
        return recipe.isEmpty() ? null : _recipe == null ?
                _recipe = (GrinderRecipe) getLevel().getRecipeManager().byKey(new ResourceLocation(recipe.get())).get() : _recipe;
    }

    @Getter
    ChangeListenerEnergyStorage energy = new ChangeListenerEnergyStorage(MAX_ENERGY);

    @Override
    protected void tick() {
        super.tick();

        if (getLevel() != null && !getLevel().isClientSide) {
            GrinderRecipe recipe = getRecipe();

            if (recipe == null) {
                findRecipe();
            }
            else if (canCraft(recipe)) {
                craftTick.reduce(1, 0);

                if (craftTick.get() == 0) {
                    finishCraftWithOutput();
                }
            }
            else {
                finishCraft();
            }
        }
        else {
            craftChangedListener();
        }
    }

    boolean findRecipe() {
        if (getLevel() != null) {
            Optional<GrinderRecipe> first = getLevel().getRecipeManager()
                    .getAllRecipesFor(ModRecipeTypes.GRINDER.get())
                    .stream()
                    .filter(this::canCraft)
                    .findFirst();

            if (first.isPresent()) {
                GrinderRecipe recipe = first.get();

                _recipe = recipe;
                this.recipe.set(recipe.getId().toString());
                maxCraftTick.set(recipe.getCraftTick());
                craftTick.set(recipe.getCraftTick());

                return true;
            }
        }

        return false;
    }

    boolean canCraft(GrinderRecipe recipe) {
        return recipe.getInput().test(input.get().getStackInSlot(0)) &&
                energy.getEnergyStored() >= recipe.getRequiredEnergy() &&
                output.get().insertItem(0, recipe.getResultItem(), true).isEmpty();
    }

    void finishCraft() {
        _recipe = null;
        recipe.set("");
        maxCraftTick.set(0);
        craftTick.set(0);
    }

    void finishCraftWithOutput() {
        output.get().insertItem(0, getRecipe().getResultItem(), false);
        input.get().extractItem(0, 1, false);
        energy.extractEnergy(getRecipe().getRequiredEnergy(), false);
        finishCraft();
    }

    @Setter
    Runnable craftTickChangedListener;

    int lastCraftTick;

    void craftChangedListener() {
        if (craftTick.get() != lastCraftTick) {
            lastCraftTick = craftTick.get();

            if (craftTickChangedListener != null) {
                craftTickChangedListener.run();
            }
        }
    }

    @Override
    protected void registerSyncData(SyncDataManager manager) {
        manager.add(craftTick = new IntSyncData("craftTick", 0, true));
        manager.add(maxCraftTick = new IntSyncData("maxCraftTick", 0, true));
        manager.add(recipe = new StringSyncData("recipe", "", true));
        manager.add(input = new ItemStackHandlerSyncData("input", 1, true));
        manager.add(output = new ItemStackHandlerSyncData("output", 1, true));
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new GrinderMenu(pContainerId, pPlayerInventory, this);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        LazyOptional<T> result;

        if (side == Direction.DOWN) {
            result = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(output::get));

        }
        else {
            result = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(input::get));

        }

        if (result.isPresent()) {
            return result;
        }

        return CapabilityEnergy.ENERGY.orEmpty(cap, LazyOptional.of(() -> energy));
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag result = super.getUpdateTag();

        result.put("energy", energy.serializeNBT());

        return result;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);

        tag.put("energy", energy.serializeNBT());
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        energy.deserializeNBT(tag.get("energy"));
    }
}
