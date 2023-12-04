package com.pyding.vp.capability;

import com.pyding.vp.item.ModItems;
import com.pyding.vp.item.artifacts.Anemoculus;
import com.pyding.vp.item.artifacts.MaskOfDemon;
import com.pyding.vp.item.artifacts.Vestige;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.SendPlayerCapaToClient;
import com.pyding.vp.network.packets.SendPlayerNbtToClient;
import com.pyding.vp.util.VPUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public class PlayerCapabilityVP {
    public static int totalVestiges = 16;
    private int[] challenges = new int[this.totalVestiges];
    private String coolDowned = "";
    private String biomesFound = "";
    private String damageDie = "";
    private String damageDo = "";
    private String monstersKilled = "";
    private String mobsTamed = "";
    private String foodEaten = "";

    private String loreComplete = "";
    private String tools = "";
    private String flowers = "";

    private Long coolDown = 0L;
    private int chance = 0;
    public void setChance(int number){
        this.chance = number;
    }
    public void setChance(){
        this.chance += 10;
    }
    public int getChance(){
        return chance;
    }

    public void addLore(Player player, int number){
        if(!loreComplete.contains(""+number)){
            loreComplete += number + ",";
            sendLore(player, number);
        }
    }

    public boolean getLore(Player player,int number){
        if(loreComplete.contains(""+number))
            return true;
        return false;
    }

    public void giveVestige(Player player, int vp){
        if(!hasCoolDown(vp)){
            if(player.level.isClientSide)
                return;
            addCoolDown(vp,player);
            player.addItem(vestige(vp));
        } else player.displayClientMessage(Component.literal("Wait more ").append(Component.literal(this.coolDown/1000+" seconds")),true);
    }

    public void addTimeCd(long time){
        this.coolDown = time;
    }

    public long getTimeCd(){
        return this.coolDown;
    }

    public void addCoolDown(int vp,Player player){
        this.coolDowned += vp + ",";
        sync(player);
    }

    public void clearCoolDown(Player player){
        this.coolDowned = "";
        sync(player);
    }

    public boolean hasCoolDown(int vp){
        if(this.coolDowned.contains(""+vp))
            return true;
        return false;
    }
    public void addBiome(String biome, Player player){
        if(!this.biomesFound.contains(biome)) {
            this.biomesFound += biome + ",";
            setChallenge(3,player);
        }
    }

    public void addFlower(String flower, Player player){
        if(!this.flowers.contains(flower)) {
            this.flowers += flower + ",";
            setChallenge(16,player);
        }
    }

    public void addDamageDie(String damage, Player player){
        if(!this.damageDie.contains(damage)) {
            this.damageDie += damage + ",";
            setChallenge(11,player);
        }
    }

    public void addDamageDo(String damage, Player player){
        if(!this.damageDo.contains(damage)) {
            this.damageDo += damage + ",";
            setChallenge(13,player);
        }
    }

    public void addMonsterKill(String monster, Player player){
        if(!this.monstersKilled.contains(monster)) {
            this.monstersKilled += monster + ",";
            setChallenge(2,player);
        }
    }

    public String getMonstersKilled(){
        return monstersKilled;
    }
    public String getBiomesFound(){
        return biomesFound;
    }
    public String getFoodEaten(){
        return foodEaten;
    }
    public String getTools(){
        return tools;
    }
    public String getFlowers(){
        return flowers;
    }
    public void addMobTame(String mob, Player player){
        if(!this.mobsTamed.contains(mob)) {
            this.mobsTamed += mob + ",";
            setChallenge(15,player);
        }
    }

    public void addFood(String food, Player player){
        if(!this.foodEaten.contains(food)) {
            this.foodEaten += food + ",";
            setChallenge(6,player);
        }
    }

    public void addTool(String tool, Player player){
        if(!this.tools.contains(tool)) {
            this.tools += tool + ",";
            setChallenge(10,player);
        }
    }

    public String getMobsTamed(){
        return mobsTamed;
    }

    public void clearAllProgress(Player player){
        this.mobsTamed = "";
        this.monstersKilled = "";
        this.biomesFound = "";
        this.damageDo = "";
        this.damageDie = "";
        this.coolDowned = "";
        this.foodEaten = "";
        this.loreComplete = "";
        this.coolDown = 0L;
        this.tools = "";
        for(int i = 0; i < totalVestiges; i++){
            setChallenge(i,0,player);
        }
        this.chance = 0;
        sync(player);
    }

    public static int getMaximum(int number){
        switch (number){
            case 1:
                return 20;
            case 2:
                return VPUtil.getEntitiesListOfType(MobCategory.MONSTER).size();
            case 3:
                return VPUtil.getBiomes().size();
            case 4:
                return 100;
            case 5:
                return 100;
            case 6:
                return VPUtil.getEdibleItems().size();
            case 7:
                return 15;
            case 8:
                return 10;
            case 9:
                return 16;
            case 10:
                return VPUtil.getTools().size();
            case 11:
                return 24;
            case 12:
                return 13;
            case 13:
                return 24;
            case 14:
                return 6;
            case 15:
                return VPUtil.getEntitiesListOfType(MobCategory.CREATURE).size();
            case 16:
                return VPUtil.getFlowers().size();
        }
        return  0;
    }

    public int getChallenge(int vp) {
        if (vp >= 1 && vp < totalVestiges) {
            return challenges[vp-1];
        } else {
            return 0;
        }
    }

    public void setChallenge(int vp, int value, Player player) {
        if (vp >= 1 && vp < totalVestiges && !hasCoolDown(vp)) {
            challenges[vp-1] = value;
        }
        sync(player);
    }

    public void setChallenge(int vp, Player player) {
        if (vp >= 1 && vp < totalVestiges && !hasCoolDown(vp)) {
            challenges[vp-1] += 1;
        }
        sync(player);
    }

    public void copyNBT(PlayerCapabilityVP source){
        this.challenges = source.challenges;
        this.coolDowned = source.coolDowned;
        this.biomesFound = source.biomesFound;
        this.damageDie = source.damageDie;
        this.damageDo = source.damageDo;
        this.monstersKilled = source.monstersKilled;
        this.foodEaten = source.foodEaten;
        this.loreComplete = source.loreComplete;
        this.coolDown = source.coolDown;
        this.tools = source.tools;
        this.mobsTamed = source.tools;
        this.chance = source.chance;
    }

    public void saveNBT(CompoundTag nbt){
        for(int i = 0; i < totalVestiges; i++){
            nbt.putInt("challenge"+i,challenges[i]);
        }
        nbt.putString("VPCoolDowned",coolDowned);
        nbt.putString("VPBiomesFound",biomesFound);
        nbt.putString("VPDamageDie",damageDie);
        nbt.putString("VPDamageDo",damageDo);
        nbt.putString("VPMonstersKilled",monstersKilled);
        nbt.putString("VPFoodEaten",foodEaten);
        nbt.putString("VPLore",loreComplete);
        nbt.putLong("VPCoolDown",coolDown);
        nbt.putString("VPTools",tools);
        nbt.putString("VPMobs",mobsTamed);
        nbt.putString("VPFlowers",flowers);
        nbt.putInt("VPChance",chance);
    }

    public void loadNBT(CompoundTag nbt){
        for(int i = 0; i < totalVestiges; i++){
            challenges[i] = nbt.getInt("challenge"+i);
        }
        coolDowned = nbt.getString("VPCoolDowned");
        biomesFound = nbt.getString("VPBiomesFound");
        damageDie = nbt.getString("VPDamageDie");
        damageDo = nbt.getString("VPDamageDo");
        monstersKilled = nbt.getString("VPMonstersKilled");
        foodEaten = nbt.getString("VPFoodEaten");
        loreComplete = nbt.getString("VPLore");
        coolDown = nbt.getLong("VPCoolDown");
        tools = nbt.getString("VPTools");
        mobsTamed = nbt.getString("VPMobs");
        flowers = nbt.getString("VPFlowers");
        chance = nbt.getInt("VPChance");
    }

    public CompoundTag getNbt(){
        CompoundTag nbt = new CompoundTag();
        for(int i = 0; i < totalVestiges; i++){
            nbt.putInt("challenge"+i,challenges[i]);
        }
        nbt.putString("VPCoolDowned",coolDowned);
        nbt.putString("VPBiomesFound",biomesFound);
        nbt.putString("VPDamageDie",damageDie);
        nbt.putString("VPDamageDo",damageDo);
        nbt.putString("VPMonstersKilled",monstersKilled);
        nbt.putString("VPFoodEaten",foodEaten);
        nbt.putString("VPLore",loreComplete);
        nbt.putLong("VPCoolDown",coolDown);
        nbt.putString("VPTools",tools);
        nbt.putString("VPMobs",mobsTamed);
        nbt.putString("VPFlowers",flowers);
        nbt.putInt("VPChance",chance);
        return nbt;
    }

    public void sync(Player player){
        if(player.level.isClientSide)
            return;
        ServerPlayer serverPlayer = (ServerPlayer) player;
        System.out.println("in capa");
        System.out.println(this.getNbt());
        PacketHandler.sendToPlayer(new SendPlayerCapaToClient(this.getNbt()),serverPlayer);
        /*CompoundTag nbt = new CompoundTag();
        for(int i = 1; i < challenges.length; i++){
            nbt.putInt("challenge"+i,challenges[i]);
        }
        nbt.putString("VPLore",loreComplete);
        nbt.putString("VPCoolDowned",coolDowned);
        nbt.putLong("VPCoolDown",coolDown);
        nbt.putString("VPBiomesFound",biomesFound);
        nbt.putString("VPMonstersKilled",monstersKilled);
        nbt.putString("VPFoodEaten",foodEaten);
        nbt.putString("VPTools",tools);
        nbt.putString("VPMobs",mobsTamed);
        nbt.putString("VPFlowers",flowers);
        if(player.getPersistentData() != null){
            PacketHandler.sendToAllAround(new SendPlayerNbtToClient(player.getUUID(),nbt),player);
        }*/
    }

    public void sendLore(Player player, int number){
        if(!player.level.isClientSide)
            return;
        if(number == 1)
            player.sendSystemMessage(Component.translatable("vp.lore.1"));
        else if(number == 2)
            player.sendSystemMessage(Component.translatable("vp.lore.2"));
        else if(number == 3) {
            player.sendSystemMessage(Component.translatable("vp.lore.3.1"));
            player.sendSystemMessage(Component.translatable("vp.lore.3.2"));
            player.sendSystemMessage(Component.translatable("vp.lore.3.3"));
            player.sendSystemMessage(Component.translatable("vp.lore.3.4"));
            player.sendSystemMessage(Component.translatable("vp.lore.3.5"));
            player.sendSystemMessage(Component.translatable("vp.lore.3.6"));
        }
        else if(number == 4)
            player.sendSystemMessage(Component.translatable("vp.lore.4"));
        else if(number == 5)
            player.sendSystemMessage(Component.translatable("vp.lore.5"));
        else if(number == 6)
            player.sendSystemMessage(Component.translatable("vp.lore.6"));
        else if(number == 7) {
            player.sendSystemMessage(Component.translatable("vp.lore.7"));
            player.sendSystemMessage(Component.translatable("vp.lore.7.1"));
            player.sendSystemMessage(Component.translatable("vp.lore.7.2"));
            player.sendSystemMessage(Component.translatable("vp.lore.7.3"));
            player.sendSystemMessage(Component.translatable("vp.lore.7.4"));
            player.sendSystemMessage(Component.translatable("vp.lore.7.5"));
            player.sendSystemMessage(Component.translatable("vp.lore.7.6"));
            player.sendSystemMessage(Component.translatable("vp.lore.7.7"));
        }
        else if(number == 8)
            player.sendSystemMessage(Component.translatable("vp.lore.8"));
        else if(number == 9) {
            player.sendSystemMessage(Component.translatable("vp.lore.9.1"));
            player.sendSystemMessage(Component.translatable("vp.lore.9.2"));
            player.sendSystemMessage(Component.translatable("vp.lore.9.3"));
            player.sendSystemMessage(Component.translatable("vp.lore.9.4"));
            player.sendSystemMessage(Component.translatable("vp.lore.9.5"));
            player.sendSystemMessage(Component.translatable("vp.lore.9.6"));
            player.sendSystemMessage(Component.translatable("vp.lore.9.7"));
            player.sendSystemMessage(Component.translatable("vp.lore.9.8"));
            player.sendSystemMessage(Component.translatable("vp.lore.9.9"));
            player.sendSystemMessage(Component.translatable("vp.lore.9.10"));
        }
    }

    public ItemStack vestige(int vp){
        ItemStack stack;
        switch (vp){
            case 1: {
                stack = new ItemStack(ModItems.ANEMOCULUS.get());
                break;
            }
            default: {
                stack = new ItemStack(ModItems.MASK.get());
                break;
            }
        }
        if(Math.random() < (float)getChance()/100){
            Vestige.setStellar(stack);
            setChance(10);
        }
        return stack;
    }
}
