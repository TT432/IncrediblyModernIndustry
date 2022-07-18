package dustw.imi.blockentity;

import dustw.imi.blockentity.base.ModBaseMenuBlockEntity;
import dustw.imi.blockentity.component.ChangeListenerEnergyStorage;
import dustw.imi.blockentity.reg.ModBlockEntities;
import dustw.imi.menu.ElectricFurnaceMenu;
import dustw.imi.utils.ItemHandlerUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
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

import java.util.List;

/**
 * @author DustW
 */
public class ElectricFurnaceBlocKEntity extends ModBaseMenuBlockEntity {
    public static final int MAX_ENERGY = 20_0000;

    public ElectricFurnaceBlocKEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.ELECTRIC_FURNACE.get(), pWorldPosition, pBlockState);
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
    SmeltingRecipe _recipe;

    public SmeltingRecipe getRecipe() {
        return recipe.isEmpty() ? null : _recipe == null ?
                _recipe = (SmeltingRecipe) getLevel().getRecipeManager().byKey(new ResourceLocation(recipe.get())).get() : _recipe;
    }

    @Getter
    ChangeListenerEnergyStorage energy = new ChangeListenerEnergyStorage(MAX_ENERGY);

    @Override
    protected void registerSyncData(SyncDataManager manager) {
        manager.add(craftTick = new IntSyncData("craftTick", 0, true));
        manager.add(maxCraftTick = new IntSyncData("maxCraftTick", 0, true));
        manager.add(recipe = new StringSyncData("recipe", "", true));
        manager.add(input = new ItemStackHandlerSyncData("input", 1, true));
        manager.add(output = new ItemStackHandlerSyncData("output", 1, true));
    }

    @Override
    protected void tick() {
        super.tick();

        if (level != null && !level.isClientSide) {
            if (getRecipe() == null) {
                findRecipe();
            }
            else if (canCraft(getRecipe())) {
                craftTick.reduce(1, 0);

                if (craftTick.get() == 0) {
                    finishRecipeWithItem();
                }
            }
            else {
                finishRecipe();
            }
        }
        else {
            craftChangedListener();
        }
    }

    void findRecipe() {
        List<SmeltingRecipe> allRecipes = level.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING);

        for (SmeltingRecipe recipe : allRecipes) {
            if (canCraft(recipe)) {
                setRecipe(recipe);
            }
        }
    }

    private void setRecipe(@NotNull SmeltingRecipe recipe) {
        this._recipe = recipe;
        this.recipe.set(recipe.getId().toString());

        int craftTick = recipe.getCookingTime() / 10;
        this.maxCraftTick.set(craftTick);
        this.craftTick.set(craftTick);
    }

    boolean canCraft(SmeltingRecipe recipe) {
        return recipe.getIngredients().get(0).test(input.get().getStackInSlot(0)) &&
                energy.getEnergyStored() >= getRequiredEnergy(recipe) &&
                output.get().insertItem(0, recipe.getResultItem().copy(), true).isEmpty();
    }

    private void finishRecipe() {
        this._recipe = null;
        this.recipe.set("");
        this.maxCraftTick.set(0);
        this.craftTick.set(0);
    }

    private void finishRecipeWithItem() {
        input.get().extractItem(0, 1, false);
        output.get().insertItem(0, getRecipe().getResultItem().copy(), false);
        energy.extractEnergy(getRequiredEnergy(getRecipe()), false);

        finishRecipe();
    }

    private int getRequiredEnergy(SmeltingRecipe recipe) {
        return recipe.getCookingTime() * 80;
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

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ElectricFurnaceMenu(pContainerId, pPlayerInventory, this);
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

    @Override
    public List<ItemStack> getDrops() {
        return ItemHandlerUtils.from(input.get(), output.get());
    }
}
