package dustw.imi.blockentity.component;

import lombok.Setter;
import net.minecraft.nbt.Tag;
import net.minecraftforge.energy.EnergyStorage;

import java.util.function.Consumer;

/**
 * @author DustW
 **/
public class ChangeListenerEnergyStorage extends EnergyStorage {
    @Setter
    Consumer<ChangeListenerEnergyStorage> changeListener;

    public ChangeListenerEnergyStorage(int capacity) {
        super(capacity);
    }

    public ChangeListenerEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public ChangeListenerEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public ChangeListenerEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int i = super.receiveEnergy(maxReceive, simulate);

        if (!simulate) {
            onChanged();
        }

        return i;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int i = super.extractEnergy(maxExtract, simulate);

        if (!simulate) {
            onChanged();
        }

        return i;
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        super.deserializeNBT(nbt);
        onChanged();
    }

    private void onChanged() {
        if (changeListener != null) {
            changeListener.accept(this);
        }
    }
}
