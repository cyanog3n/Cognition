package com.cyanogen.experienceobelisk.network.precision_dispeller;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class UpdateSlots {

    public static int slot;
    public static ItemStack stack;

    public UpdateSlots(int slot, ItemStack stack) {
        UpdateSlots.slot = slot;
        UpdateSlots.stack = stack;
    }

    public UpdateSlots(FriendlyByteBuf buffer) {
        slot = buffer.readInt();
        stack = buffer.readItem();

    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeInt(slot);
        buffer.writeItem(stack);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            assert sender != null;

            sender.containerMenu.getSlot(slot).set(stack);

            success.set(true);

        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }
}
