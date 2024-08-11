package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.registries.RegisterItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemStackedOnOtherEvent;

public class MendingNeurogelItem extends Item {

    public MendingNeurogelItem(Properties p) {
        super(p);
    }

    public static void handleItem(ItemStackedOnOtherEvent event){
        ItemStack carried = event.getCarriedItem();
        ItemStack stackedOn = event.getStackedOnItem();

        if(carried.is(RegisterItems.MENDING_NEUROGEL.get()) && stackedOn.isDamaged()){
            carried.shrink(1);
            int damage = stackedOn.getDamageValue();
            stackedOn.setDamageValue(Math.max(damage - 200, 0));
        }
    }



    //Repairs items by 200 durability points when applied
    //Can be applied to items you wouldn't be able to otherwise mend, such as those with conflicting enchants, or those that don't accept mending

}
