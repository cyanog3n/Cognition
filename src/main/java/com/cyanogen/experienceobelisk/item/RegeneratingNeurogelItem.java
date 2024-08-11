package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.registries.RegisterItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemStackedOnOtherEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

public class RegeneratingNeurogelItem extends Item {

    public RegeneratingNeurogelItem(Properties p) {
        super(p);
    }

    public static void handleItem(ItemStackedOnOtherEvent event){
        ItemStack carried = event.getCarriedItem();
        ItemStack stackedOn = event.getStackedOnItem();
        CompoundTag tag = stackedOn.getOrCreateTag();

        if(carried.is(RegisterItems.REGENERATING_NEUROGEL.get()) && stackedOn.isDamageableItem() && tag.getInt("ReserveDurability") <= 1600){
            carried.shrink(1);

            if(tag.contains("ReserveDurability")){
                int reserveDurability = tag.getInt("ReserveDurability");
                tag.putInt("ReserveDurability", Math.min(200 + reserveDurability, 1600));
            }
            else{
                tag.putInt("ReserveDurability", 200);
            }

        }
    }

    public static void handleDestruction(PlayerDestroyItemEvent event){
        ItemStack item = event.getOriginal().copy();
        CompoundTag tag = item.getOrCreateTag();
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();

        if(tag.contains("ReserveDurability")){
            int reserveDurability = tag.getInt("ReserveDurability");
            int maxDamage = item.getMaxDamage();
            int restore = Math.min(maxDamage, reserveDurability);

            if(reserveDurability - restore <= 0){
                tag.remove("ReserveDurability");
            }
            else{
                tag.putInt("ReserveDurability", reserveDurability - restore);
            }

            item.setDamageValue(maxDamage - restore);

            if(hand != null && player.getItemInHand(hand).isEmpty()){
                player.setItemInHand(hand, item);
            }
            else if(!player.addItem(item)){
                player.drop(item, false);
            }

        }
    }

}
