package com.piratesee.crystalline.block.entity;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.piratesee.crystalline.recipe.GemInfusingStationRecipe;
import com.piratesee.crystalline.screen.GemInfusingStationMenu;

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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class GemInfusingStationBlockEntity extends BlockEntity implements MenuProvider {
	public GemInfusingStationBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.GEM_INFUSING_STATION.get(), pos, state);
		this.data = new ContainerData() {
			
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> GemInfusingStationBlockEntity.this.progress;
                    case 1 -> GemInfusingStationBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> GemInfusingStationBlockEntity.this.progress = value;
                    case 1 -> GemInfusingStationBlockEntity.this.maxProgress = value;
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
	private int maxProgress = 57;
	
	private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		}
	};
	
	private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
	
	@Override
	public Component getDisplayName() {
		return Component.literal("Gem Infusing Station");
	}
	
	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
		return new GemInfusingStationMenu(id, inventory, this, this.data);
	}
	
	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return lazyItemHandler.cast();
		}
		return super.getCapability(cap, side);
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
		lazyItemHandler = LazyOptional.of(() -> itemHandler);
	}
	
	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		lazyItemHandler.invalidate();
	}
	
	@Override
	protected void saveAdditional(CompoundTag nbt) {
		nbt.put("inventory", itemHandler.serializeNBT());
		nbt.putInt("gem_infusing_station.progress", this.progress);
		super.saveAdditional(nbt);
	}
	
	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		itemHandler.deserializeNBT(nbt.getCompound("inventory"));
		progress = nbt.getInt("gem_infusing_station.progress");
	}

	public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

	public static void tick(Level level, BlockPos pos, BlockState state, GemInfusingStationBlockEntity pEntity) {
		if(level.isClientSide()) {
			return;
		}
		
		if(hasRecipe(pEntity)) {
			pEntity.progress++;
			System.out.println(pEntity.progress);
			setChanged(level, pos, state);
			
			if(pEntity.progress >= pEntity.maxProgress) {
				craftItem(pEntity);
			}
		} else {
			pEntity.resetProgress();
			setChanged(level, pos, state);
		}
	}

	private void resetProgress() {
		this.progress = 0;
	}

	private static void craftItem(GemInfusingStationBlockEntity pEntity) {
        Level level = pEntity.level;
        SimpleContainer inventory = new SimpleContainer(pEntity.itemHandler.getSlots());
        for (int i = 0; i < pEntity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, pEntity.itemHandler.getStackInSlot(i));
        }

        Optional<GemInfusingStationRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(GemInfusingStationRecipe.Type.INSTANCE, inventory, level);

		
		if(hasRecipe(pEntity)) {
			pEntity.itemHandler.extractItem(1, 1, false);
			pEntity.itemHandler.setStackInSlot(2, new ItemStack(recipe.get().getResultItem().getItem(),
					pEntity.itemHandler.getStackInSlot(2).getCount() + 1));
			pEntity.resetProgress();
			
		}
		
	}

    private static boolean hasRecipe(GemInfusingStationBlockEntity entity) {
    	Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }
        
        Optional<GemInfusingStationRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(GemInfusingStationRecipe.Type.INSTANCE, inventory, level);

        return recipe.isPresent() && canInsertAmountIntoOutputSlot(inventory) &&
                canInsertItemIntoOutputSlot(inventory, recipe.get().getResultItem());
	}

	private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack stack) {
		return inventory.getItem(2).getItem() == stack.getItem() || inventory.getItem(2).isEmpty();
 	}

	private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
	    return inventory.getItem(2).getMaxStackSize() > inventory.getItem(2).getCount();
	}

}
