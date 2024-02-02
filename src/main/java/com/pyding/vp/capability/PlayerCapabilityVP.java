package com.pyding.vp.capability;

import com.pyding.vp.item.ModItems;
import com.pyding.vp.item.artifacts.Vestige;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.SendPlayerCapaToClient;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AutoRegisterCapability
public class PlayerCapabilityVP {
    public static int totalVestiges = 16;
    private int[] challenges = new int[this.totalVestiges];
    private String coolDowned = "";
    private String biomesFound = "";
    private String damageDie = "";
    private String damageDo = "";
    private String monstersKilled = "";
    private String creaturesKilledAir = "";
    private String mobsTamed = "";
    private String foodEaten = "";

    private String loreComplete = "";
    private String tools = "";
    private String flowers = "";

    private Long coolDown = 0L;

    private String cats = "";
    private String goldenItems = "";
    private int chance = 0;
    private String randomEntity = "";
    private long chaosTime = 0;

    public long getCoolDown(){
        return coolDown;
    }
    private String commonChallenges = "";
    private String stellarChallenges = "";

    private static final Pattern PATTERN = Pattern.compile("minecraft:(\\w+)");
    private Set<String> biomeNames = new HashSet<>();

    public void filterBiome(String name,Player player) {
        Matcher matcher = PATTERN.matcher(name);
        while (matcher.find()) {
            String biomeName = matcher.group(1);
            if (!biomeNames.contains(biomeName) && !biomeName.contains("worldgen")) {
                biomeNames.add(biomeName);
                setChallenge(3, getBiomeSize(), player);
            }
        }
    }
    public long getChaosTime(){
        return chaosTime;
    }
    public void setChaosTime(long number,Player player){
        chaosTime = number;
        sync(player);
    }
    public void filterBiome(String name) {
        Matcher matcher = PATTERN.matcher(name);
        while (matcher.find()) {
            String biomeName = matcher.group(1);
            if (!biomeNames.contains(biomeName) && !biomeName.contains("worldgen")) {
                biomeNames.add(biomeName);
            }
        }
    }

    public int getBiomeSize(){
        filterBiome(biomesFound);
        int result = Arrays.asList(biomeNames.toArray()).size();
        return result;
    }
    public void setChance(int number){
        this.chance = number;
    }
    public void setChance(){
        this.chance += 10;
    }
    public int getChance(){
        return chance;
    }
    public void addCommonChallenge(Player player, int number){
        if (commonChallenges.isEmpty())
            addLore(player,3);
        if(getCommonChallenges() >= 1)
            addLore(player,4);
        if(!commonChallenges.contains(number+"")){
            commonChallenges += number + ",";
            sync(player);
        }
    }

    public void addStellarChallenge(Player player, int number){
        if(stellarChallenges.isEmpty())
            addLore(player,7);
        if(!commonChallenges.contains(number+"")){
            commonChallenges += number + ",";
            if(getStellarChallenges() == totalVestiges)
                addLore(player,9);
            sync(player);
        }
    }

    public int getCommonChallenges(){
        int challs = 0;
        for(String ignored : commonChallenges.split(","))
            challs++;
        return challs;
    }

    public int getStellarChallenges(){
        int challs = 0;
        for(String ignored : stellarChallenges.split(","))
            challs++;
        return challs;
    }

    public void addLore(Player player, int number){
        if(!loreComplete.contains(number+"")){
            loreComplete += number + ",";
            sendLore(player, number);
            sync(player);
        }
    }

    public boolean getLore(Player player,int number){
        if(loreComplete.contains(number+""))
            return true;
        return false;
    }
    public void setRandomEntity(EntityType type,Player player){
        randomEntity = type.getDescriptionId();
        sync(player);
    }
    public String getRandomEntity(){
        return randomEntity;
    }

    public void giveVestige(Player player, int vp){
        if(!hasCoolDown(vp)){
            if(player.level.isClientSide)
                return;
            setChallenge(vp,0,player);
            addCoolDown(vp,player);
            clearProgress(vp,player);
            player.addItem(vestige(vp, player));
        }
    }

    public void addTimeCd(long time, Player player){
        this.coolDown = time;
        sync(player);
    }

    public long getTimeCd(){
        return this.coolDown;
    }

    public void addCoolDown(int vp,Player player){
        this.coolDowned += vp + ",";
        sync(player);
    }

    public String getDamageDie(){
        return damageDie;
    }
    public String getDamageDo(){
        return damageDo;
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
    public void addGold(String gold, Player player){
        if(!this.goldenItems.contains(gold)) {
            this.goldenItems += gold + ",";
        }
    }
    public void addCat(String cat, Player player){
        if(!this.cats.contains(cat)) {
            this.cats += cat + ",";
            setChallenge(8,player);
        }
    }
    public void addBiome(String biome, Player player){
        if(!this.biomesFound.contains(biome)) {
            this.biomesFound += biome + ",";
            filterBiome(biome,player);
        }
    }

    public void addFlower(String flower, Player player){
        if(!this.flowers.contains(flower)) {
            this.flowers += flower + ",";
            setChallenge(16, player);
        }
    }

    public void addCreatureKilledAir(String name, Player player){
        if(!this.creaturesKilledAir.contains(name)) {
            this.creaturesKilledAir += name + ",";
            setChallenge(1,player);
        }
    }

    public void addDamageDie(String damage, Player player){
        if(!this.damageDie.contains(damage)) {
            this.damageDie += damage + ",";
            setChallenge(11,player);
        }
    }

    public void addDamageDo(DamageSource source, Player player){
        String damage = "";
        if(source.isBypassArmor())
            damage += "bypassArmor,";
        if(source.isDamageHelmet())
            damage += "damageHelmet,";
        if(source.isBypassEnchantments())
            damage += "bypassEnchantments,";
        if(source.isExplosion())
            damage += "explosion,";
        if(source.isBypassInvul())
            damage += "bypassInvul,";
        if(source.isBypassMagic())
            damage += "bypassMagic,";
        if(source.isFall())
            damage += "fall,";
        if(source.isFire())
            damage += "fire,";
        if(source.isMagic())
            damage += "magic,";
        if(source.isNoAggro())
            damage += "noAggro,";
        if(source.isProjectile())
            damage += "projectile,";
        for(String damageName: damage.split(",")) {
            if (!this.damageDo.contains(damageName)) {
                this.damageDo += damageName + ",";
                setChallenge(13, player);
            }
        }
    }

    public void addMonsterKill(String monster, Player player){
        if(!this.monstersKilled.contains(monster)) {
            this.monstersKilled += monster + ",";
            setChallenge(2,player);
        }
    }

    public double getGoldenChance(){
        double chance = 10;
        for (String gold: goldenItems.split(","))
            chance += 1;
        return chance;
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
    public void clearProgress(int vp,Player player){
        switch (vp){
            case 1:{
                creaturesKilledAir = "";
                break;
            }
            case 2:{
                monstersKilled = "";
                break;
            }
            case 3:{
                biomesFound = "";
                biomeNames.clear();
                break;
            }
            case 6:{
                foodEaten = "";
                break;
            }
            case 8:{
                cats = "";
                break;
            }
            case 9:{
                goldenItems = "";
                break;
            }
            case 10:{
                tools = "";
                break;
            }
            case 11:{
                damageDie = "";
                break;
            }
            case 13:{
                damageDo = "";
                break;
            }
            case 14:{
                chaosTime = System.currentTimeMillis();
                randomEntity = VPUtil.getRandomEntity().getDescriptionId();
                break;
            }
            case 15:{
                mobsTamed = "";
                break;
            }
            case 16:{
                flowers = "";
                break;
            }
            default: break;
        }
        sync(player);
    }

    public void failChallenge(int vp,Player player){
        setChallenge(vp,0,player);
        clearProgress(vp,player);
        addCoolDown(vp,player);
    }

    public void clearAllProgress(Player player){
        mobsTamed = "";
        monstersKilled = "";
        biomesFound = "";
        biomeNames.clear();
        damageDo = "";
        damageDie = "";
        coolDowned = "";
        foodEaten = "";
        loreComplete = "";
        coolDown = 0L;
        tools = "";
        flowers = "";
        cats = "";
        goldenItems = "";
        for(int i = 1; i < totalVestiges+1; i++){
            setChallenge(i,0,player);
        }
        chance = 10;
        chaosTime = System.currentTimeMillis();
        randomEntity = VPUtil.getRandomEntity().getDescriptionId();
        commonChallenges = "";
        stellarChallenges = "";
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
                return 11;
            case 9:
                return 8;
            case 10:
                return VPUtil.getTools().size();
            case 11:
                return 24;
            case 12:
                return 10;
            case 13: {
                int max = 0;
                for(String ignored : VPUtil.damageSubtypes().split(","))
                    max++;
                return max;
            }
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
        if(vp == 3){
            return getBiomeSize();
        }
        else if (vp >= 1 && vp <= totalVestiges) {
            return challenges[vp-1];
        } else {
            return 0;
        }
    }

    public void setChallenge(int vp, int value, Player player) {
        if (vp >= 1 && vp <= totalVestiges && !hasCoolDown(vp)) {
            challenges[vp-1] = value;
        } //else player.sendSystemMessage(Component.literal("Wait more ").append(Component.literal((VPUtil.formatMilliseconds(4*24*60*60*1000-(System.currentTimeMillis() - this.getTimeCd()))))));
        sync(player);
    }

    public void setChallenge(int vp, Player player) {
        if (vp >= 1 && vp <= totalVestiges && !hasCoolDown(vp)) {
            challenges[vp-1] += 1;
        }
        sync(player);
    }

    public void copyNBT(PlayerCapabilityVP source){
        challenges = source.challenges;
        coolDowned = source.coolDowned;
        biomesFound = source.biomesFound;
        damageDie = source.damageDie;
        damageDo = source.damageDo;
        monstersKilled = source.monstersKilled;
        foodEaten = source.foodEaten;
        loreComplete = source.loreComplete;
        coolDown = source.coolDown;
        tools = source.tools;
        mobsTamed = source.tools;
        chance = source.chance;
        cats = source.cats;
        goldenItems = source.goldenItems;
        chaosTime = source.chaosTime;
        randomEntity = source.randomEntity;
        commonChallenges = source.commonChallenges;
        stellarChallenges = source.stellarChallenges;
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
        nbt.putString("VPCats",cats);
        nbt.putString("VPGold",goldenItems);
        nbt.putString("VPRandomEntity",randomEntity);
        nbt.putLong("VPCT",chaosTime);
        nbt.putString("VPCC",commonChallenges);
        nbt.putString("VPSC",stellarChallenges);
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
        cats = nbt.getString("VPCats");
        goldenItems = nbt.getString("VPGold");
        randomEntity = nbt.getString("VPRandomEntity");
        chaosTime = nbt.getLong("VPCT");
        commonChallenges = nbt.getString("VPCC");
        stellarChallenges = nbt.getString("VPSC");
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
        nbt.putString("VPCats",cats);
        nbt.putString("VPGold",goldenItems);
        nbt.putString("VPRandomEntity",randomEntity);
        nbt.putLong("VPCT",chaosTime);
        nbt.putString("VPCC",commonChallenges);
        nbt.putString("VPSC",stellarChallenges);
        return nbt;
    }

    public void sync(Player player){
        if(player.level.isClientSide)
            return;
        ServerPlayer serverPlayer = (ServerPlayer) player;
        PacketHandler.sendToClient(new SendPlayerCapaToClient(this.getNbt()),serverPlayer);
        /*
        /*
        if(player.getPersistentData() != null){
            PacketHandler.sendToAllAround(new SendPlayerNbtToClient(player.getUUID(),nbt),player);
        }*/
    }

    public void sendLore(Player player, int number){
        if(!player.level.isClientSide)
            return;
        String name = VPUtil.getRainbowString(VPUtil.generateRandomString("entity".length())) + ": ";
        String playerName = player.getDisplayName().getString();
        playerName += ": ";

        if(number == 1)
            player.sendSystemMessage(Component.translatable("vp.lore.1"));
        else if(number == 2)
            player.sendSystemMessage(Component.translatable("vp.lore.2"));
        else if(number == 3) {
            player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.3.1")).withStyle(ChatFormatting.DARK_PURPLE));
            player.sendSystemMessage(Component.literal(playerName).append(Component.translatable("vp.lore.3.2")));
            player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.3.3")).withStyle(ChatFormatting.DARK_PURPLE));
            player.sendSystemMessage(Component.literal(playerName).append(Component.translatable("vp.lore.3.4")));
            player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.3.5")).withStyle(ChatFormatting.DARK_PURPLE));
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
            player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.7.1")).withStyle(ChatFormatting.DARK_PURPLE));
            player.sendSystemMessage(Component.literal(playerName).append(Component.translatable("vp.lore.7.2")));
            player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.7.3")).withStyle(ChatFormatting.DARK_PURPLE));
            player.sendSystemMessage(Component.literal(playerName).append(Component.translatable("vp.lore.7.4")));
            player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.7.5")).withStyle(ChatFormatting.DARK_PURPLE));
            player.sendSystemMessage(Component.translatable("vp.lore.7.6"));
            player.sendSystemMessage(Component.translatable("vp.lore.7.7"));
        }
        else if(number == 8)
            player.sendSystemMessage(Component.translatable("vp.lore.8"));
        else if(number == 9) {
            player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.9.1")).withStyle(ChatFormatting.DARK_PURPLE)); //Component.literal(playerName).append()
            player.sendSystemMessage(Component.literal(playerName).append(Component.translatable("vp.lore.9.2")));
            player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.9.3")).withStyle(ChatFormatting.DARK_PURPLE));
            player.sendSystemMessage(Component.literal(playerName).append(Component.translatable("vp.lore.9.4")));
            player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.9.5")).withStyle(ChatFormatting.DARK_PURPLE));
            player.sendSystemMessage(Component.literal(playerName).append(Component.translatable("vp.lore.9.6")));
            player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.9.7")).withStyle(ChatFormatting.DARK_PURPLE));
            player.sendSystemMessage(Component.literal(playerName).append(Component.translatable("vp.lore.9.8")));
            player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.9.9")).withStyle(ChatFormatting.DARK_PURPLE));
            player.sendSystemMessage(Component.translatable("vp.lore.9.10"));
        }
    }

    public ItemStack vestige(int vp, Player player){
        ItemStack stack;
        switch (vp){
            case 1: {
                stack = new ItemStack(ModItems.ANEMOCULUS.get());
                break;
            }
            case 2: {
                stack = new ItemStack(ModItems.CROWN.get());
                break;
            }
            case 3: {
                stack = new ItemStack(ModItems.ATLAS.get());
                break;
            }
            case 4: {
                stack = new ItemStack(ModItems.KILLER.get());
                break;
            }
            case 5: {
                stack = new ItemStack(ModItems.MASK.get());
                break;
            }
            case 6: {
                stack = new ItemStack(ModItems.DONUT.get());
                break;
            }
            case 7: {
                stack = new ItemStack(ModItems.MARK.get());
                break;
            }
            case 8: {
                stack = new ItemStack(ModItems.EARS.get());
                break;
            }
            case 9: {
                stack = new ItemStack(ModItems.MIDAS.get());
                break;
            }
            case 10: {
                stack = new ItemStack(ModItems.ANOMALY.get());
                break;
            }
            case 11: {
                stack = new ItemStack(ModItems.ARMOR.get());
                break;
            }
            case 12: {
                stack = new ItemStack(ModItems.BOOK.get());
                break;
            }
            case 13: {
                stack = new ItemStack(ModItems.PRISM.get());
                break;
            }
            case 14: {
                stack = new ItemStack(ModItems.CHAOS.get());
                break;
            }
            case 15: {
                stack = new ItemStack(ModItems.DEVOURER.get());
                break;
            }
            case 16: {
                stack = new ItemStack(ModItems.FLOWER.get());
                break;
            }
            default: {
                stack = new ItemStack(ModItems.STELLAR.get());
                break;
            }
        }
        if(Math.random() < (float)getChance()/100){
            Vestige.setStellar(stack);
            setChance(10);
            addStellarChallenge(player,vp);
        } else {
            setChance();
            addCommonChallenge(player,vp);
        }
        return stack;
    }
}
