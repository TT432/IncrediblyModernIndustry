package dustw.imi.blockentity;

import dustw.imi.blockentity.base.ModBaseMenuBlockEntity;
import dustw.imi.blockentity.component.ChangeListenerEnergyStorage;
import dustw.imi.blockentity.reg.ModBlockEntities;
import dustw.imi.menu.ThermalMenu;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * @author DustW
 **/
public class ThermalBlockEntity extends ModBaseMenuBlockEntity<ThermalBlockEntity.SaveObject, ThermalBlockEntity.SyncObject> {

    public static final int MAX_ENERGY = 200_0000;
    public static final int ENERGY_PRE_TICK = 40;

    @Getter
    final ChangeListenerEnergyStorage energyStorage = new ChangeListenerEnergyStorage(MAX_ENERGY);

    public ThermalBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.THERMAL.get(), pWorldPosition, pBlockState);
    }

    @Override
    protected void tick() {
        super.tick();

        if (!getLevel().isClientSide) {
            boolean burning = getSync().burnTick > 0;
            boolean notFull = energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored();

            if (!burning && notFull) {
                ItemStack stack = getBurnItem().getStackInSlot(0);

                if (!stack.isEmpty()) {
                    int burnTime = ForgeHooks.getBurnTime(stack, null);
                    getSync().setBurnTick(burnTime);
                    getSync().setMaxBurnTick(burnTime);
                    getBurnItem().extractItem(0, 1, false);

                    setChanged();
                }
                else if (getSync().getMaxBurnTick() != 0) {
                    getSync().setMaxBurnTick(0);
                }
            } else if (burning && notFull) {
                getSync().burnTick = Math.max(0, getSync().burnTick - 1);
                energyStorage.receiveEnergy(ENERGY_PRE_TICK, false);

                setChanged();
            }
        }

        burnTickChanged();
    }

    @Override
    protected SyncObject registerSyncObject() {
        return new SyncObject();
    }

    @Override
    protected Class<SyncObject> getSyncObjectClass() {
        return SyncObject.class;
    }

    @Override
    protected SaveObject registerSaveObject() {
        return new SaveObject();
    }

    @Override
    protected Class<SaveObject> getSaveObjectClass() {
        return SaveObject.class;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ThermalMenu(pContainerId, pPlayerInventory, this);
    }

    @Getter
    private final ItemStackHandler burnItem = new ItemStackHandler(1) {
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return ForgeHooks.getBurnTime(stack, null) > 0;
        }
    };

    @Data
    public static class SaveObject {

    }

    @Setter
    Consumer<ThermalBlockEntity> burnTickChangeListener;

    @Data
    public static class SyncObject {
        int burnTick;
        int maxBurnTick;
    }

    int lastBurnTick;

    void burnTickChanged() {
        if (getSync().burnTick != lastBurnTick) {
            lastBurnTick = getSync().burnTick;

            if (burnTickChangeListener != null) {
                burnTickChangeListener.accept(this);
            }
        }
    }

    @Override
    protected boolean needSaveSyncObject() {
        return true;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        LazyOptional<T> tLazyOptional = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> getBurnItem()));
        return tLazyOptional.isPresent() ? tLazyOptional : CapabilityEnergy.ENERGY.orEmpty(cap, LazyOptional.of(() -> energyStorage));
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag updateTag = super.getUpdateTag();
        updateTag.put("energy", energyStorage.serializeNBT());
        return updateTag;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("energy", energyStorage.serializeNBT());
        tag.put("items", burnItem.serializeNBT());
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        energyStorage.deserializeNBT(tag.get("energy"));

        if (!isSyncTag(tag)) {
            burnItem.deserializeNBT(tag.getCompound("items"));
        }
    }
}
