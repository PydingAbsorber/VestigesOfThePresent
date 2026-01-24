package com.pyding.vp.mixin;


import com.pyding.vp.util.VPUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.MobBucketItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemUtils.class)
public abstract class BucketMixin {

    @Inject(method = "createFilledResult(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/item/ItemStack;", at = @At("HEAD"))
    private static void onBucket(ItemStack stack1, Player player, ItemStack stack2, boolean wtf, CallbackInfoReturnable<ItemStack> cir) {
        if(stack1.getItem() instanceof BucketItem && stack2.getItem() instanceof MobBucketItem mobBucketItem){
            EntityType<?> type = ((BucketVzlom)mobBucketItem).getFishSup().get();
            String fish = mobBucketItem.getDescriptionId();
            Entity entity = VPUtil.ray(player,2,10,true).get(0);
            if(entity instanceof Axolotl axolotl)
                fish = axolotl.getVariant().getName();
            else if(entity instanceof TropicalFish tropicalFish)
                fish = tropicalFish.getVariant().getSerializedName();
            VPUtil.getCap(player).addSea(fish,player);
        }
    }
}
