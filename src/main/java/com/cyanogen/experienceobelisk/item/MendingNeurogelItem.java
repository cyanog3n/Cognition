package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.registries.RegisterItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemStackedOnOtherEvent;

public class MendingNeurogelItem extends Item {

    public MendingNeurogelItem(Properties p) {
        super(p);
    }

    public static void handleRepair(ItemStackedOnOtherEvent event){
        ItemStack carried = event.getCarriedItem();
        ItemStack stackedOn = event.getStackedOnItem();

        if(carried.is(RegisterItems.MENDING_NEUROGEL.get()) && stackedOn.isDamaged()){
            carried.shrink(1);
            int damage = stackedOn.getDamageValue();
            stackedOn.setDamageValue(Math.max(damage - 200, 0));
        }
    }




}
