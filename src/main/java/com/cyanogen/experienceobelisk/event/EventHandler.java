package com.cyanogen.experienceobelisk.event;

import com.cyanogen.experienceobelisk.item.RegeneratingNeurogelItem;
import com.cyanogen.experienceobelisk.item.MendingNeurogelItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ItemStackedOnOtherEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onTooltip(ItemTooltipEvent event){
        HandleTooltip.handleTooltip(event);
    }

    @SubscribeEvent
    public void onItemStackedOnOther(ItemStackedOnOtherEvent event){
        MendingNeurogelItem.handleItem(event);
        RegeneratingNeurogelItem.handleItem(event);
    }

    @SubscribeEvent
    public void onPlayerDestroyItem(PlayerDestroyItemEvent event){
        RegeneratingNeurogelItem.handleDestruction(event);
    }


}
