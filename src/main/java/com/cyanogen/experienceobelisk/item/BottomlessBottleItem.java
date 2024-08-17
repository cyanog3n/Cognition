package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.registries.RegisterItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BottomlessBottleItem extends Item implements ICapabilityProvider{

    public BottomlessBottleItem(Properties p) {
        super(p);
    }

    //-----------BEHAVIOR-----------//

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean b) {
        this.fill();




        super.inventoryTick(stack, level, entity, i, b);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace(), 1);
        Level level = context.getLevel();
        System.out.println(pos);

        if(level.getBlockState(pos).isAir()){
            level.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.useOn(context);
    }

    //-----------FLUID HANDLER-----------//

    protected FluidHandlerItemStack fluidHandler = fluidHandler();

    private FluidHandlerItemStack fluidHandler() {
        return new FluidHandlerItemStack(new ItemStack(Items.DEAD_BUSH), 1000){
            @Override
            public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
                return stack.getFluid().isSame(Fluids.WATER);
            }
        };
    }

    public void fill(){
        fluidHandler.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        return fluidHandler.getCapability(capability, facing);
    }
}
