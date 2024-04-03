package com.pyding.vp.capability;

import com.pyding.vp.item.ModItems;
import com.pyding.vp.item.artifacts.Vestige;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.ItemAnimationPacket;
import com.pyding.vp.network.packets.LorePacket;
import com.pyding.vp.network.packets.SendPlayerCapaToClient;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AutoRegisterCapability
public class PlayerCapabilityVP {
    public static int totalVestiges = 20;
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

    private String dimensions = "";
    private String dimensionsDir = "";
    private boolean debug = false;

    private String effects = "";
    private String bosses = "";

    private static final Pattern PATTERN = Pattern.compile("minecraft:(\\w+)");
    private Set<String> biomeNames = new HashSet<>();

    public void addDimension(Player player,String dim, String nameSpace){
        if(!dimensions.contains(dim)){
            dimensions += dim + ",";
            dimensionsDir += nameSpace + ",";
            sync(player);
        }
    }

    public String getDimensions(){
        return dimensions;
    }

    public List<String> getDimensionList(){
        List<String> dimList = new ArrayList<>();
        for(String name: dimensions.split(",")){
            dimList.add(name);
        }
        return dimList;
    }

    public List<String> getDimensionDirList(){
        List<String> dimList = new ArrayList<>();
        for(String name: dimensionsDir.split(",")){
            dimList.add(name);
        }
        return dimList;
    }

    public List<String> getRandomDimension(){
        Random random = new Random();
        int numba = random.nextInt(getDimensionList().size());
        List<String> list = new ArrayList<>();
        list.add(getDimensionDirList().get(numba));
        list.add(getDimensionList().get(numba));
        return list;
    }

    public void removeDimension(String name){
        /*String newDim = "";
        for(String dim: dimensions.split(",")) {
            if(!dim.equals(name))
                newDim += dim + ",";
        }
        dimensions = newDim;*/
    }

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
        this.chance += ConfigHandler.COMMON.stellarChanceIncrease.get();
    }
    public int getChance(){
        return chance;
    }
    public void addCommonChallenge(Player player, int number){
        if(getCommonChallenges() >= 1 && getLore(player,3))
            addLore(player,4);
        if (commonChallenges.isEmpty()) {
            addLore(player,3);
        }
        if(!commonChallenges.contains(number+"")){
            commonChallenges += number + ",";
            sync(player);
        }
    }

    public void addStellarChallenge(Player player, int number){
        if(stellarChallenges.isEmpty())
            addLore(player,7);
        if(!stellarChallenges.contains(number+"")){
            stellarChallenges += number + ",";
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
            if(number == 9)
                player.addItem(new ItemStack(ModItems.INFINITE_REFRESHER.get()));
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
            if(player.getCommandSenderWorld().isClientSide)
                return;
            setChallenge(vp,0,player);
            addCoolDown(vp,player);
            clearProgress(vp,player);
            ItemStack stack = vestige(vp, player);
            player.addItem(stack);
            VPUtil.play(player, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE);
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
        if(!hasCoolDown(vp)) {
            this.coolDowned += vp + ",";
            sync(player);
        }
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
        for(String element: coolDowned.split(",")){
            if (element.equals(Integer.toString(vp)))
                return true;
        }
        return false;
    }
    public void addGold(String gold, Player player){
        if(!this.goldenItems.contains(gold)) {
            this.goldenItems += gold + ",";
            sync(player);
        }
    }
    public void addCat(String cat, Player player){
        if(!this.cats.contains(cat)) {
            this.cats += cat + ",";
            setChallenge(8,player);
        }
    }

    public void addEffect(String effect, Player player){
        if(!this.effects.contains(effect)) {
            this.effects += effect + ",";
            setChallenge(17,player);
        }
    }
    public void addBiome(Player player){
        ResourceLocation key = VPUtil.getCurrentBiome(player);
        if(key != null) {
            //String biomeName = key.toDebugFileName();
            String biomeName = key.getPath();
            if (!this.biomesFound.contains(biomeName)) {
                this.biomesFound += biomeName + ",";
                setChallenge(3, player);
            }
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
        for(TagKey<DamageType> key: VPUtil.damageTypes(true)){
            if(source.is(key)) {
                String damageName = key.location().getPath();
                if (!this.damageDo.contains(damageName)) {
                    this.damageDo += damageName + ",";
                    setChallenge(13, player);
                }
            }
        }
    }

    public void addMonsterKill(String monster, Player player){
        if(!this.monstersKilled.contains(monster)) {
            this.monstersKilled += monster + ",";
            setChallenge(2,player);
        }
    }

    public String getBosses(){
        return bosses;
    }

    public void addBossKill(String monster, Player player){
        if(!this.bosses.contains(monster)) {
            this.bosses += monster + ",";
            setChallenge(15,player);
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
    public String getEffects(){
        return effects;
    }
    public void addMobTame(String mob, Player player){
        if(!this.mobsTamed.contains(mob)) {
            this.mobsTamed += mob + ",";
            setChallenge(20,player);
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
                bosses = "";
                break;
            }
            case 16:{
                flowers = "";
                break;
            }
            case 17:{
                effects = "";
                break;
            }
            case 20:{
                mobsTamed = "";
                break;
            }
            default: break;
        }
        setChallenge(vp,0,player);
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
        dimensions = "";
        dimensionsDir = "";
        effects = "";
        bosses = "";
        sync(player);
    }

    public static int getMaximum(int number, Player player){
        int reduce = ConfigHandler.COMMON.getChallengeReduceByNumber(number).get();
        switch (number){
            case 1:
                return 20-reduce;
            case 2:
                return VPUtil.getMonsterClientMax(player).size()-reduce;
            case 3:
                return VPUtil.getBiomesClientMax(player).size()-reduce;
            case 4:
                return 100-reduce;
            case 5:
                return 100-reduce;
            case 6:
                return VPUtil.getEdibleItems().size()-reduce;
            case 7:
                return 15-reduce;
            case 8:
                return 11-reduce;
            case 9:
                return 8-reduce;
            case 10:
                return VPUtil.getTools().size()-reduce;
            case 11:
                return VPUtil.getDamageKinds().size()-reduce;
            case 12:
                return 10-reduce;
            case 13:
                return VPUtil.damageTypes(true).size();
            case 14:
                return 6-reduce;
            case 15:
                return VPUtil.getBossClientMax(player).size()-reduce;
            case 16:
                return VPUtil.getFlowers().size()-reduce;
            case 17:
                return VPUtil.getEffects().size()-reduce;
            case 18:
                return 20-reduce;
            case 19:
                return 1000000-reduce;
            case 20:
                return VPUtil.getEntitiesListOfType(MobCategory.CREATURE).size()-reduce;
        }
        return  0;
    }

    public static void initMaximum(Player player){
        for(int i = 1; i < totalVestiges+1; i++) {
            int reduce = ConfigHandler.COMMON.getChallengeReduceByNumber(i).get();
            switch (i) {
                case 1:
                    player.getPersistentData().putInt("VPMaxChallenge"+i,20 - reduce);
                    break;
                case 2:
                    player.getPersistentData().putInt("VPMaxChallenge"+i,VPUtil.getMonsterClientMax(player).size() - reduce);
                    break;
                case 3:
                    player.getPersistentData().putInt("VPMaxChallenge"+i,VPUtil.getBiomesClientMax(player).size() - reduce);
                    break;
                case 4:
                    player.getPersistentData().putInt("VPMaxChallenge"+i,100 - reduce);
                    break;
                case 5:
                    player.getPersistentData().putInt("VPMaxChallenge"+i,100 - reduce);
                    break;
                case 6:
                    player.getPersistentData().putInt("VPMaxChallenge"+i,VPUtil.getEdibleItems().size() - reduce);
                    break;
                case 7:
                    player.getPersistentData().putInt("VPMaxChallenge"+i,15 - reduce);
                    break;
                case 8:
                    player.getPersistentData().putInt("VPMaxChallenge"+i,11 - reduce);
                    break;
                case 9:
                    player.getPersistentData().putInt("VPMaxChallenge"+i,8 - reduce);
                    break;
                case 10:
                    player.getPersistentData().putInt("VPMaxChallenge"+i,VPUtil.getTools().size() - reduce);
                    break;
                case 11:
                    player.getPersistentData().putInt("VPMaxChallenge"+i,VPUtil.getDamageKinds().size() - reduce);
                    break;
                case 12:
                    player.getPersistentData().putInt("VPMaxChallenge"+i,10 - reduce);
                    break;
                case 13:
                    player.getPersistentData().putInt("VPMaxChallenge"+i,VPUtil.damageTypes(true).size());
                    break;
                case 14:
                    player.getPersistentData().putInt("VPMaxChallenge"+i,6 - reduce);
                    break;
                case 15:
                    player.getPersistentData().putInt("VPMaxChallenge"+i,VPUtil.getBossClientMax(player).size() - reduce);
                    break;
                case 16:
                    player.getPersistentData().putInt("VPMaxChallenge"+i,VPUtil.getFlowers().size() - reduce);
                    break;
                case 17:
                    player.getPersistentData().putInt("VPMaxChallenge"+i,VPUtil.getEffects().size() - reduce);
                    break;
                case 18:
                    player.getPersistentData().putInt("VPMaxChallenge"+i,20 - reduce);
                    break;
                case 19:
                    player.getPersistentData().putInt("VPMaxChallenge"+i,1000000 - reduce);
                    break;
                case 20:
                    player.getPersistentData().putInt("VPMaxChallenge"+i,VPUtil.getEntitiesListOfType(MobCategory.CREATURE).size() - reduce);
                    break;
            }
        }
    }

    public boolean getDebug(){
        return debug;
    }
    public void setDebug(Player player){
        if(debug)
            debug = false;
        else debug = true;
        sync(player);
    }

    public int getChallenge(int vp) {
        if (vp >= 1 && vp <= totalVestiges) {
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
        dimensions = source.dimensions;
        dimensionsDir = source.dimensionsDir;
        debug = source.debug;
        effects = source.effects;
        bosses = source.bosses;
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
        nbt.putString("VPDimensions",dimensions);
        nbt.putString("VPDimensionsDir",dimensionsDir);
        nbt.putBoolean("VPDebug",debug);
        nbt.putString("VPEffects",effects);
        nbt.putString("VPBosses",bosses);
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
        dimensions = nbt.getString("VPDimensions");
        dimensionsDir = nbt.getString("VPDimensionsDir");
        debug = nbt.getBoolean("VPDebug");
        effects = nbt.getString("VPEffects");
        bosses = nbt.getString("VPBosses");
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
        nbt.putString("VPDimensions",dimensions);
        nbt.putString("VPDimensionsDir",dimensionsDir);
        nbt.putBoolean("VPDebug",debug);
        nbt.putString("VPEffects",effects);
        nbt.putString("VPBosses",bosses);
        return nbt;
    }

    public void sync(Player player){
        if(player.getCommandSenderWorld().isClientSide)
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
        if(player instanceof ServerPlayer serverPlayer)
            PacketHandler.sendToClient(new LorePacket(number),serverPlayer);
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
            case 17:{
                stack = new ItemStack(ModItems.CATALYST.get());
                break;
            }
            case 18:{
                stack = new ItemStack(ModItems.BALL.get());
                break;
            }
            case 19:{
                stack = new ItemStack(ModItems.TRIGON.get());
                break;
            }
            case 20:{
                stack = new ItemStack(ModItems.SOULBLIGHTER.get());
                break;
            }
            default: {
                stack = new ItemStack(ModItems.STELLAR.get());
                break;
            }
        }
        float stellarChance = (float) getChance();
        if(VPUtil.getSet(player) == 9)
            stellarChance += 5;
        if(Math.random() < stellarChance/100){
            if(stack.getItem() instanceof Vestige vestige){
                vestige.vestigeStack = stack;
            }
            if(getChance() >= 200){
                Vestige.setDoubleStellar(stack);
            }
            Vestige.setStellar(stack);
            setChance(ConfigHandler.COMMON.stellarChanceIncrease.get());
            addStellarChallenge(player,vp);
            if(Math.random() < ConfigHandler.COMMON.refresherChance.get())
                player.addItem(new ItemStack(ModItems.REFRESHER.get()));
        } else {
            setChance();
            addCommonChallenge(player,vp);
        }
        if(player instanceof ServerPlayer serverPlayer)
            PacketHandler.sendToClient(new ItemAnimationPacket(stack),serverPlayer);
        return stack;
    }
}
