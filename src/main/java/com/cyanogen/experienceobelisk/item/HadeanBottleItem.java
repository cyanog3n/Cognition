package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.registries.RegisterItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HadeanBottleItem extends Item implements ICapabilityProvider {

    public HadeanBottleItem(Properties p) {
        super(p);
    }

    //-----------BEHAVIOR-----------//

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean b) {

        //20 second recharge time = 2.5mB/t refill

        if(level.getGameTime() % 2 == 0){
            fill(5);
        }

        super.inventoryTick(stack, level, entity, i, b);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace(), 1);
        Level level = context.getLevel();

        if(level.getBlockState(pos).isAir() && getRefillProgress() == 1){
            level.setBlockAndUpdate(pos, Blocks.LAVA.defaultBlockState());
            drain(1000);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.useOn(context);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0xff731c;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return (int) (13.0f * getRefillProgress());
    }

    //-----------FLUID HANDLER-----------//

    protected FluidHandlerItemStack fluidHandler = fluidHandler();

    private FluidHandlerItemStack fluidHandler() {
        return new FluidHandlerItemStack(ItemStack.EMPTY, 1000){
            @Override
            public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
                return stack.getFluid().isSame(Fluids.LAVA);
            }
        };
    }

    public void fill(int amount){
        fluidHandler.fill(new FluidStack(Fluids.LAVA, amount), IFluidHandler.FluidAction.EXECUTE);
    }

    public void drain(int amount){
        fluidHandler.drain(new FluidStack(Fluids.LAVA, amount), IFluidHandler.FluidAction.EXECUTE);
    }

    public double getRefillProgress(){
        return fluidHandler.getFluid().getAmount() / 1000f;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        return fluidHandler.getCapability(capability, facing);
    }
}
