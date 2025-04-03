package com.pyding.vp.capability;

import com.pyding.vp.item.ModItems;
import com.pyding.vp.item.vestiges.Vestige;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.ItemAnimationPacket;
import com.pyding.vp.network.packets.LorePacket;
import com.pyding.vp.network.packets.SendPlayerCapaToClient;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.LeaderboardUtil;
import com.pyding.vp.util.VPUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.*;
import java.util.regex.Pattern;

@AutoRegisterCapability
public class PlayerCapabilityVP {
    public static int totalVestiges = 24;
    private int[] challenges = new int[this.totalVestiges];
    private String coolDowned = "";
    private String biomesFound = "";
    private String damageDie = "";
    private String rareItems = "";
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
    private String music = "";
    private String templates = "";
    private String sea = "";
    private String friends = "";
    private boolean sleep = false;
    private long vip = 0;
    private long bindTime = 0;
    private long antiTp = 0;
    private double bindX = 0;
    private double bindY = 0;
    private double bindZ = 0;
    private long deathTime = 0;
    private boolean cheating = false;
    private String password = "";

    private static final Pattern PATTERN = Pattern.compile("minecraft:(\\w+)");
    private int pearls = 0;

    public void setSleep(boolean slept){
        sleep = slept;
    }

    public boolean getSleep(){
        return sleep;
    }

    public void addPearl(Player player){
        pearls += 1;
        sync(player);
    }

    public int getPearls(){
        return pearls;
    }

    public void addDimension(Player player,String dim, String nameSpace){
        if(VPUtil.notContains(dimensions,dim)){
            dimensions += dim + ",";
            dimensionsDir += nameSpace + ",";
            sync(player);
        }
    }

    public void addMusic(String musicDisk, Player player){
        if(VPUtil.notContains(music,musicDisk) && !hasCoolDown(22)) {
            this.music += musicDisk + ",";
            setChallenge(22,player);
        }
    }

    public void addTemplate(String template, Player player){
        if(VPUtil.notContains(templates,template) && !hasCoolDown(21)) {
            this.templates += template + ",";
            setChallenge(21,player);
        }
    }


    public void addSea(String seaElement, Player player){
        if(VPUtil.notContains(sea,seaElement) && !hasCoolDown(24)) {
            this.sea += seaElement + ",";
            setChallenge(24,player);
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

    public long getChaosTime(){
        return chaosTime;
    }
    public void setChaosTime(long number,Player player){
        chaosTime = number;
        sync(player);
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
            VPUtil.giveStack(stack,player);
            VPUtil.play(player, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE);
            if(!password.isEmpty())
                LeaderboardUtil.addChallenge(player, vp, password);
            if(new Random().nextDouble() < VPUtil.getChance(0.33,player)){
                VPUtil.giveStack(new ItemStack(ModItems.MYSTERY_CHEST.get(),1 + new Random().nextInt(9)),player);
            }
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
    public String getrareItems(){
        return rareItems;
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
        if(VPUtil.notContains(goldenItems,gold)) {
            this.goldenItems += gold + ",";
            sync(player);
        }
    }
    public void addFriend(String friend, Player player){
        if(VPUtil.notContains(friends,friend)) {
            this.friends += friend + ",";
            sync(player);
        }
    }
    public void removeFriend(String friend, Player player){
        List<String> list = new ArrayList<>(List.of(friends.split(",")));
        list.remove(friend);
        friends = "";
        for(String element: list){
            friends += element+",";
        }
        sync(player);
    }
    public String getFriends(){
        return friends;
    }
    public void addCat(String cat, Player player){
        if(VPUtil.notContains(cats,cat) && !hasCoolDown(8)) {
            this.cats += cat + ",";
            setChallenge(8,player);
        }
    }

    public void addEffect(String effect, Player player){
        if(VPUtil.notContains(effects,effect) && !hasCoolDown(17)) {
            this.effects += effect + ",";
            setChallenge(17,player);
        }
    }
    public void addBiome(Player player){
        ResourceLocation key = VPUtil.getCurrentBiome(player);
        if(key != null) {
            //String biomeName = key.toDebugFileName();
            String biomeName = key.getPath();
            if (VPUtil.notContains(biomesFound,biomeName) && !hasCoolDown(3)) {
                this.biomesFound += biomeName + ",";
                setChallenge(3, player);
            }
        }
    }

    public void addFlower(String flower, Player player){
        if(VPUtil.notContains(flowers,flower) && !hasCoolDown(16)) {
            this.flowers += flower + ",";
            setChallenge(16, player);
        }
    }

    public void addCreatureKilledAir(String name, Player player){
        if(VPUtil.notContains(creaturesKilledAir,name) && !hasCoolDown(1)) {
            this.creaturesKilledAir += name + ",";
            setChallenge(1,player);
        }
    }

    public void addDamageDie(String damage, Player player){
        if(VPUtil.notContains(damageDie,damage) && !hasCoolDown(11)) {
            this.damageDie += damage + ",";
            setChallenge(11,player);
        }
    }

    public void addrareItems(String item, Player player){
        if (VPUtil.notContains(rareItems,item) && !hasCoolDown(13)) {
            this.rareItems += item + ",";
            setChallenge(13, player);
        }
    }

    public void addMonsterKill(String monster, Player player){
        if(VPUtil.notContains(monstersKilled,monster) && !hasCoolDown(2)) {
            this.monstersKilled += monster + ",";
            setChallenge(2,player);
        }
    }

    public String getBosses(){
        return bosses;
    }

    public String getMusic(){
        return music;
    }
    public String getTemplates(){
        return templates;
    }
    public String getSea(){
        return sea;
    }

    public void addBossKill(String monster, Player player){
        if(VPUtil.notContains(bosses,monster) && !hasCoolDown(15)) {
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
        if(VPUtil.notContains(mobsTamed,mob) && !hasCoolDown(20)) {
            this.mobsTamed += mob + ",";
            setChallenge(20,player);
        }
    }

    public void addFood(String food, Player player){
        if(VPUtil.notContains(foodEaten,food) && !hasCoolDown(6)) {
            this.foodEaten += food + ",";
            setChallenge(6,player);
        }
    }

    public void addTool(String tool, Player player){
        if(VPUtil.notContains(tools,tool) && !hasCoolDown(10)) {
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
                rareItems = "";
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
            case 21:{
                templates = "";
                break;
            }
            case 22:{
                music = "";
                break;
            }
            case 24:{
                sea = "";
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
        rareItems = "";
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
        music = "";
        templates = "";
        sea = "";
        vip = 0;
        pearls = 0;
        sync(player);
    }

    public static int getMaximum(int number,Player player){
        boolean leaderboard = LeaderboardUtil.isLeaderboardsActive(player);
        if(ConfigHandler.COMMON.reduceChallengesPercent.get()){
            float reduce = 1 - ((float)ConfigHandler.COMMON.getChallengeReduceByNumber(number)/100);
            if(leaderboard && reduce < 0.9)
                reduce = 0.9f;
            switch (number) {
                case 1:
                    return (int) ((float) VPUtil.getEntitiesList().size() / 10 * reduce);
                case 2:
                    return (int) (VPUtil.monsterList.size() * reduce);
                case 3:
                    return (int) (VPUtil.getBiomes().size() * reduce);
                case 4:
                    return (int) (100 * reduce);
                case 5:
                    return (int) (100 * reduce);
                case 6:
                    return (int) (VPUtil.getEdibleItems().size() * reduce);
                case 7:
                    return (int) (15 * reduce);
                case 8:
                    return (int) (11 * reduce);
                case 9:
                    return (int) (8 * reduce);
                case 10:
                    return (int) (20 * reduce);
                case 11:
                    return (int) (VPUtil.getDamageKinds().size() * reduce);
                case 12:
                    return (int) (10 * reduce);
                case 13:
                    return (int) ((float) VPUtil.hashRares.size() * reduce);
                case 14:
                    return (int) (6 * reduce);
                case 15:
                    return (int) (VPUtil.bossList.size() * reduce);
                case 16:
                    return (int) (VPUtil.getFlowers().size() * reduce);
                case 17:
                    return (int) (VPUtil.getEffects().size() * reduce);
                case 18:
                    return (int) (20 * reduce);
                case 19:
                    return (int) (1000000 * reduce);
                case 20:
                    return (int) (VPUtil.getEntitiesListOfType(MobCategory.CREATURE).size() * reduce);
                case 21:
                    return (int) (VPUtil.getTemplates().size() * reduce);
                case 22:
                    return (int) (VPUtil.getMusicDisks().size() * reduce);
                case 23:
                    return (int) (8 * reduce);
                case 24:
                    return (int) (VPUtil.getSeaSize() * reduce);
            }
        } else {
            int reduce = ConfigHandler.COMMON.getChallengeReduceByNumber(number);
            if(leaderboard && reduce > 3)
                reduce = 3;
            switch (number) {
                case 1:
                    return VPUtil.getEntitiesList().size() / 10 - reduce;
                case 2:
                    return VPUtil.monsterList.size() - reduce;
                case 3:
                    return VPUtil.getBiomes().size() - reduce;
                case 4:
                    return 100 - reduce;
                case 5:
                    return 100 - reduce;
                case 6:
                    return VPUtil.getEdibleItems().size() - reduce;
                case 7:
                    return 15 - reduce;
                case 8:
                    return 11 - reduce;
                case 9:
                    return 8 - reduce;
                case 10:
                    return 20 - reduce;
                case 11:
                    return VPUtil.getDamageKinds().size() - reduce;
                case 12:
                    return 10 - reduce;
                case 13:
                    return (int) ((float) VPUtil.hashRares.size() - reduce);
                case 14:
                    return 6 - reduce;
                case 15:
                    return VPUtil.bossList.size() - reduce;
                case 16:
                    return VPUtil.getFlowers().size() - reduce;
                case 17:
                    return VPUtil.getEffects().size() - reduce;
                case 18:
                    return 20 - reduce;
                case 19:
                    return 1000000 - reduce;
                case 20:
                    return VPUtil.getEntitiesListOfType(MobCategory.CREATURE).size() - reduce;
                case 21:
                    return VPUtil.getTemplates().size() - reduce;
                case 22:
                    return VPUtil.getMusicDisks().size() - reduce;
                case 23:
                    return 8 - reduce;
                case 24:
                    return VPUtil.getSeaSize() - reduce;
            }
        }
        return  0;
    }

    public static void initMaximum(Player player){
        Level level = player.getCommandSenderWorld();
        VPUtil.initMonstersAndBosses(player);
        VPUtil.initBiomes(player,level);
        VPUtil.initBuckets();
        player.getPersistentData().putString("VPVortex",VPUtil.filterString(VPUtil.vortexItems().toString()));
        for(int i = 1; i < totalVestiges+1; i++) {
            player.getPersistentData().putInt("VPMaxChallenge"+i,getMaximum(i,player));
        }
        VPUtil.sync(player);
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
        VPUtil.resync(this,player);
    }

    public void copyNBT(PlayerCapabilityVP source){
        challenges = source.challenges;
        coolDowned = source.coolDowned;
        biomesFound = source.biomesFound;
        damageDie = source.damageDie;
        rareItems = source.rareItems;
        monstersKilled = source.monstersKilled;
        foodEaten = source.foodEaten;
        loreComplete = source.loreComplete;
        coolDown = source.coolDown;
        tools = source.tools;
        mobsTamed = source.mobsTamed;
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
        flowers = source.flowers;
        creaturesKilledAir = source.creaturesKilledAir;
        music = source.music;
        templates = source.templates;
        sea = source.sea;
        pearls = source.pearls;
        sleep = source.sleep;
        friends = source.friends;
        vip = source.vip;
        antiTp = source.antiTp;
        bindTime = source.bindTime;
        bindX = source.bindX;
        bindY = source.bindY;
        bindZ = source.bindZ;
        deathTime = source.deathTime;
        cheating = source.cheating;
        password = source.password;
    }

    public void saveNBT(CompoundTag nbt){
        for(int i = 0; i < totalVestiges; i++){
            nbt.putInt("challenge"+i,challenges[i]);
        }
        nbt.putString("VPCoolDowned",coolDowned);
        nbt.putString("VPBiomesFound",biomesFound);
        nbt.putString("VPDamageDie",damageDie);
        nbt.putString("VPrareItems",rareItems);
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
        nbt.putString("VPAir",creaturesKilledAir);
        nbt.putString("VPMusic",music);
        nbt.putString("VPTemplate",templates);
        nbt.putString("VPSea",sea);
        nbt.putString("VPFriends",friends);
        nbt.putBoolean("VPSlept",sleep);
        nbt.putInt("VPPearls",pearls);
        nbt.putLong("VPVIP",vip);
        nbt.putLong("VPAntiTP",antiTp);
        nbt.putLong("VPBind",bindTime);
        nbt.putDouble("VPBindX",bindX);
        nbt.putDouble("VPBindY",bindY);
        nbt.putDouble("VPBindZ",bindZ);
        nbt.putLong("VPDeathTime",deathTime);
        nbt.putBoolean("VPCheating",cheating);
        nbt.putString("VPPassword",password);
    }

    public void loadNBT(CompoundTag nbt){
        for(int i = 0; i < totalVestiges; i++){
            challenges[i] = nbt.getInt("challenge"+i);
        }
        coolDowned = nbt.getString("VPCoolDowned");
        biomesFound = nbt.getString("VPBiomesFound");
        damageDie = nbt.getString("VPDamageDie");
        rareItems = nbt.getString("VPrareItems");
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
        creaturesKilledAir = nbt.getString("VPAir");
        music = nbt.getString("VPMusic");
        templates = nbt.getString("VPTemplate");
        sea = nbt.getString("VPSea");
        friends = nbt.getString("VPFriends");
        pearls = nbt.getInt("VPPearls");
        sleep = nbt.getBoolean("VPSlept");
        vip = nbt.getLong("VPVIP");
        antiTp = nbt.getLong("VPAntiTP");
        bindTime = nbt.getLong("VPBind");
        bindX = nbt.getDouble("VPBindX");
        bindY = nbt.getDouble("VPBindY");
        bindZ = nbt.getDouble("VPBindZ");
        deathTime = nbt.getLong("VPDeathTime");
        cheating = nbt.getBoolean("VPCheating");
        password = nbt.getString("VPPassword");
    }

    public void sync(Player player){
        if(player.getCommandSenderWorld().isClientSide)
            return;
        if(getVip() < System.currentTimeMillis())
            setVip(0);
        ServerPlayer serverPlayer = (ServerPlayer) player;
        CompoundTag nbt = new CompoundTag();
        saveNBT(nbt);
        PacketHandler.sendToClient(new SendPlayerCapaToClient(nbt),serverPlayer);
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
            case 21:{
                stack = new ItemStack(ModItems.RUNE.get());
                break;
            }
            case 22:{
                stack = new ItemStack(ModItems.LYRA.get());
                break;
            }
            case 23:{
                stack = new ItemStack(ModItems.PEARL.get());
                break;
            }
            case 24:{
                stack = new ItemStack(ModItems.WHIRLPOOL.get());
                break;
            }
            default: {
                stack = new ItemStack(ModItems.STELLAR.get());
                break;
            }
        }
        double stellarChance = getChance();
        Random random = new Random();
        if(VPUtil.getSet(player) == 9)
            stellarChance += 5;
        if(getVip() > System.currentTimeMillis())
            stellarChance += 10;
        if(random.nextDouble() < VPUtil.getChance(stellarChance/100,player)){
            if(stellarChance >= 200){
                Vestige.setDoubleStellar(stack,player);
            }
            Vestige.setStellar(stack,player);
            setChance(ConfigHandler.COMMON.stellarChanceIncrease.get());
            addStellarChallenge(player,vp);
            if(random.nextDouble() < VPUtil.getChance(ConfigHandler.COMMON.refresherChance.get(),player))
                VPUtil.giveStack(new ItemStack(ModItems.REFRESHER.get()),player);
        } else {
            setChance();
            addCommonChallenge(player,vp);
        }
        if(player instanceof ServerPlayer serverPlayer)
            PacketHandler.sendToClient(new ItemAnimationPacket(stack),serverPlayer);
        return stack;
    }

    public long getVip() {
        return vip;
    }

    public void setVip(long vip) {
        if(this.vip > 0 && this.vip > System.currentTimeMillis()){
            this.vip += vip-System.currentTimeMillis();
        } else this.vip = vip;
    }

    public long getBindTime() {
        return bindTime;
    }

    public void setBindTime(long bindTime) {
        this.bindTime = bindTime;
    }

    public long getAntiTp() {
        return antiTp;
    }

    public void setAntiTp(long antiTp) {
        this.antiTp = antiTp;
    }

    public double getBindX() {
        return bindX;
    }

    public void setBindX(double bindX) {
        this.bindX = bindX;
    }

    public double getBindY() {
        return bindY;
    }

    public void setBindY(double bindY) {
        this.bindY = bindY;
    }

    public double getBindZ() {
        return bindZ;
    }

    public void setBindZ(double bindZ) {
        this.bindZ = bindZ;
    }

    public long getDeathTime() {
        return deathTime;
    }

    public void setDeathTime(long deathTime) {
        this.deathTime = deathTime;
    }

    public boolean isCheating() {
        return cheating;
    }

    public void setCheating(boolean cheating) {
        this.cheating = cheating;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
