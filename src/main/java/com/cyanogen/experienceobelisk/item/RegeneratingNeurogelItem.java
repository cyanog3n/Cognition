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

    public static void handleFortification(ItemStackedOnOtherEvent event){
        ItemStack carried = event.getCarriedItem();
        ItemStack stackedOn = event.getStackedOnItem();

        if(carried.is(RegisterItems.REGENERATING_NEUROGEL.get()) && stackedOn.isDamageableItem()){
            carried.shrink(1);
            CompoundTag tag = stackedOn.getOrCreateTag();

            if(tag.contains("ReserveDurability")){
                int regen = tag.getInt("ReserveDurability");
                tag.putInt("ReserveDurability", Math.min(200 + regen, 1600));
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
            int regen = tag.getInt("ReserveDurability");
            int maxDamage = item.getMaxDamage();
            int restore = Math.min(maxDamage, regen);

            if(regen - restore <= 0){
                tag.remove("ReserveDurability");
            }
            else{
                tag.putInt("ReserveDurability", regen - restore);
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
