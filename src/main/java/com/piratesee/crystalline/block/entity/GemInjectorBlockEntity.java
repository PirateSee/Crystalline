package com.piratesee.crystalline.block.entity;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.piratesee.crystalline.init.ItemInit;
import com.piratesee.crystalline.networking.ModMessages;
import com.piratesee.crystalline.networking.packet.EnergySyncS2CPacket;
import com.piratesee.crystalline.recipe.GemInjectingRecipe;
import com.piratesee.crystalline.screen.GemInjectorMenu;
import com.piratesee.crystalline.util.ModEnergyStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class GemInjectorBlockEntity extends BlockEntity implements MenuProvider{
	public GemInjectorBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.GEM_INJECTOR.get(), pos, state);
		this.data = new ContainerData() {
			
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> GemInjectorBlockEntity.this.progress;
                    case 1 -> GemInjectorBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> GemInjectorBlockEntity.this.progress = value;
                    case 1 -> GemInjectorBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
		};
	}

	protected final ContainerData data;
	private int progress = 0;
	private int maxProgress = 128;
	
	private final ModEnergyStorage ENERGY_STORAGE = new ModEnergyStorage(65536, 256) {
		@Override
		public void onEnergyChanged() {
			setChanged();
			ModMessages.sendToClients(new EnergySyncS2CPacket(this.energy, getBlockPos()));
		}
	};
	
	private static final int ENERGY_REQ = 32;
	
	private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		}
	};
	
	private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
	
	private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();
	
	@Override
	public Component getDisplayName() {
		return Component.literal("Gem Injector");
	}
	
	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
		return new GemInjectorMenu(id, inventory, this, this.data);
	}
	
	
	
	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if(cap == ForgeCapabilities.ENERGY) {
			return lazyEnergyHandler.cast();
		}
		
		if(cap == ForgeCapabilities.ITEM_HANDLER) {
			return lazyItemHandler.cast();
		}
		return super.getCapability(cap, side);
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
		lazyItemHandler = LazyOptional.of(() -> itemHandler);
		lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
	}
	
	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		lazyItemHandler.invalidate();
		lazyEnergyHandler.invalidate();
	}
	
	@Override
	protected void saveAdditional(CompoundTag nbt) {
		nbt.put("inventory", itemHandler.serializeNBT());
		nbt.putInt("gem_injector.progress", this.progress);
		nbt.putInt("gem_injector.energy", ENERGY_STORAGE.getEnergyStored());
		super.saveAdditional(nbt);
	}
	
	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		itemHandler.deserializeNBT(nbt.getCompound("inventory"));
		progress = nbt.getInt("gem_injector.progress");
		ENERGY_STORAGE.setEnergy(nbt.getInt("gem_injector.energy"));
	}

	public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

	public static void tick(Level level, BlockPos pos, BlockState state, GemInjectorBlockEntity pEntity) {
		if(level.isClientSide()) {
			return;
		}
		
		if(chargerItem(pEntity, 0)) {
			pEntity.ENERGY_STORAGE.receiveEnergy(128, false);
		}
		
		if(hasRecipe(pEntity) && hasEnoughEnergy(pEntity))  {
			pEntity.progress++;
			extractEnergy(pEntity);
			setChanged(level, pos, state);
			
			if(pEntity.progress >= pEntity.maxProgress) {
				craftItem(pEntity);
			}
		} else {
			pEntity.resetProgress();
			setChanged(level, pos, state);
		}
	}

	private static void extractEnergy(GemInjectorBlockEntity pEntity) {
		pEntity.ENERGY_STORAGE.extractEnergy(ENERGY_REQ, false);
		
	}

	private static boolean hasEnoughEnergy(GemInjectorBlockEntity pEntity) {
		return pEntity.ENERGY_STORAGE.getEnergyStored() >= ENERGY_REQ * (pEntity.maxProgress-pEntity.progress);
	}

	private static boolean chargerItem(GemInjectorBlockEntity pEntity, int slot) {
		return pEntity.itemHandler.getStackInSlot(slot).getItem() == ItemInit.DIAMONDMETAL_NUGGET.get();
	}

	private void resetProgress() {
		this.progress = 0;
	}

	private static void craftItem(GemInjectorBlockEntity pEntity) {
        Level level = pEntity.level;
        SimpleContainer inventory = new SimpleContainer(pEntity.itemHandler.getSlots());
        for (int i = 0; i < pEntity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, pEntity.itemHandler.getStackInSlot(i));
        }

        Optional<GemInjectingRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(GemInjectingRecipe.Type.INSTANCE, inventory, level);

		
		if(hasRecipe(pEntity)) {
			pEntity.itemHandler.extractItem(0, 1, false);
			pEntity.itemHandler.extractItem(1, 1, false);
			pEntity.itemHandler.setStackInSlot(2, new ItemStack(recipe.get().getResultItem().getItem(),
			pEntity.itemHandler.getStackInSlot(2).getCount() + 1));
			pEntity.resetProgress();
			
		}
		
	}

    private static boolean hasRecipe(GemInjectorBlockEntity entity) {
    	Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }
        
        Optional<GemInjectingRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(GemInjectingRecipe.Type.INSTANCE, inventory, level);

        return recipe.isPresent() && canInsertAmountIntoOutputSlot(inventory) &&
                canInsertItemIntoOutputSlot(inventory, recipe.get().getResultItem());
	}

	private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack stack) {
		return inventory.getItem(2).getItem() == stack.getItem() || inventory.getItem(2).isEmpty();
 	}

	private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
	    return inventory.getItem(2).getMaxStackSize() > inventory.getItem(2).getCount();
	}

	public IEnergyStorage getEnergyStorage() {
		return ENERGY_STORAGE;
	}

	public void setEnergyLevel(int energy) {
		this.ENERGY_STORAGE.setEnergy(energy);
	}

}
