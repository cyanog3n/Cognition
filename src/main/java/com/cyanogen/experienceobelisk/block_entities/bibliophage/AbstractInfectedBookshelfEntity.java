package com.cyanogen.experienceobelisk.block_entities.bibliophage;

import com.cyanogen.experienceobelisk.registries.RegisterBlocks;
import com.cyanogen.experienceobelisk.registries.RegisterItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractInfectedBookshelfEntity extends AbstractInfectiveEntity {

    public AbstractInfectedBookshelfEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    int timeTillSpawn = -99; //the current time in ticks until the bookshelf is due to spawn an orb
    int spawnDelayMin; //the minimum spawn delay for the bookshelf
    int spawnDelayMax; //the maximum spawn delay for the bookshelf
    int orbValue; //the value of orbs to spawn
    int spawns; //the number of times a bookshelf can spawn an orb before decaying
    int decayValue = 0; //the number of times a bookshelf has spawned an orb
    double infectivity = 0.02; //the chance for a bookshelf to infect another adjacent bookshelf every second
    boolean redstoneEnabled = false; //whether or not the bookshelf is sensitive to redstone. Disabled ookshelves will not infect adjacents, produce XP, or decay

    //-----------BEHAVIOR-----------//

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {

        boolean hasSignal = level.hasNeighborSignal(pos);

        if(blockEntity instanceof AbstractInfectedBookshelfEntity bookshelf && (!bookshelf.redstoneEnabled || hasSignal)){

            if(bookshelf.decayValue >= bookshelf.spawns){
                bookshelf.decay(level, pos);
            }
            else{

                if(bookshelf.timeTillSpawn == -99){
                    bookshelf.resetSpawnDelay();
                }
                else if(bookshelf.timeTillSpawn <= 0){
                    bookshelf.handleExperience(level, pos);
                    bookshelf.incrementDecayValue();
                    bookshelf.resetSpawnDelay();
                }
                else{
                    bookshelf.decrementSpawnDelay();
                }

                if(level.getGameTime() % 20 == 0 && Math.random() <= bookshelf.infectivity){
                    bookshelf.infectAdjacent(level, pos);
                }

            }

        }

    }

    public void resetSpawnDelay(){
        int delay = (int) (spawnDelayMin + Math.floor((spawnDelayMax - spawnDelayMin) * Math.random()));

        if(isAdjacentTo(RegisterBlocks.INSIGHTFUL_AGAR.get())){
            delay = (int) (delay * 0.8);
        }

        this.timeTillSpawn = delay;
        this.setChanged();
    }

    public void decrementSpawnDelay(){
        this.timeTillSpawn -= 1;
        this.setChanged();
    }

    public boolean isAdjacentTo(Block block){
        Level level = getLevel();
        BlockPos pos = getBlockPos();

        for(BlockPos adjacent : getAdjacents(pos)){
            if(level != null && level.getBlockState(adjacent).is(block)){
                return true;
            }
        }
        return false;
    }

    public void handleExperience(Level level, BlockPos pos){

        int value = orbValue;

        if(!level.isClientSide){

            if(isAdjacentTo(RegisterBlocks.EXTRAVAGANT_AGAR.get())){
                value = (int) (value * 1.25);
            }

            ServerLevel server = (ServerLevel) level;
            ExperienceOrb orb = new ExperienceOrb(server, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, value);
            orb.setDeltaMovement(0,0,0);

            server.addFreshEntity(orb);
        }
    }

    public void incrementDecayValue(){
        this.decayValue += 1;
        this.setChanged();
    }

    public void decay(Level level, BlockPos pos){

        setDisabled(true);

        if(!level.isClientSide){
            ServerLevel server = (ServerLevel) level;
            ItemStack forgottenDust = new ItemStack(RegisterItems.FORGOTTEN_DUST.get(), 4);
            Block.popResource(server, pos, forgottenDust);
        }
        level.playSound(null, pos, SoundEvents.WART_BLOCK_BREAK, SoundSource.BLOCKS, 1f,1f); //play break sound
        level.levelEvent(null, 2001, pos, Block.getId(RegisterBlocks.FORGOTTEN_DUST_BLOCK.get().defaultBlockState())); //spawn destroy particles

        this.setRemoved();
        level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
    }

    public boolean toggleActivity(){
        this.redstoneEnabled = !this.redstoneEnabled;
        this.setChanged();

        return this.redstoneEnabled;
    }

    public void setDisabled(boolean disabled){
        this.redstoneEnabled = disabled;
        this.setChanged();
    }

    public boolean getRedstoneEnabled(){
        return this.redstoneEnabled;
    }

    public int getDecayValue(){
        return this.decayValue;
    }

    public int getOrbValue(){
        return this.orbValue;
    }

    public int getSpawns(){
        return this.spawns;
    }

    //-----------NBT-----------//

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);

        this.decayValue = tag.getInt("DecayValue");
        this.timeTillSpawn = tag.getInt("SpawnDelay");
        this.redstoneEnabled = tag.getBoolean("isRedstoneControllable");
    }

    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);

        tag.putInt("DecayValue", decayValue);
        tag.putInt("SpawnDelay", timeTillSpawn);
        tag.putBoolean("isRedstoneControllable", redstoneEnabled);
    }

    @Override
    public CompoundTag getUpdateTag()
    {
        CompoundTag tag = super.getUpdateTag();

        tag.putInt("DecayValue", decayValue);
        tag.putInt("SpawnDelay", timeTillSpawn);
        tag.putBoolean("isRedstoneControllable", redstoneEnabled);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }



}
