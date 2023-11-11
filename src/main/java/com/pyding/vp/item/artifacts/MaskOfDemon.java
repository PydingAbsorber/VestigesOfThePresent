package com.pyding.vp.item.artifacts;

import com.pyding.vp.item.ModCreativeModTab;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MaskOfDemon extends Artifact{
    public MaskOfDemon(){
        super();
    }

    @Override
    public void setVestigeNumber(int number) {
        number = 5;
        super.setVestigeNumber(number);
    }

    @Override
    public void setColor(ChatFormatting color) {
        color = ChatFormatting.AQUA;
        super.setColor(color);
    }

    @Override
    public void setPassiveText(List<String> text) {
        text =new ArrayList<>();
        text.add("Ужасающая аура уменьшает лечение владельца и всех окружающих на 1% за каждый 1% отсутствующего хп");
        super.setPassiveText(text);
    }

    @Override
    public void setSpecialText(List<String> text, int charges, int cd) {
        text =new ArrayList<>();
        charges = 1;
        cd = 1;
        text.add(String.valueOf(Component.literal("aboba")));
        text.add("Постепенно сжирает хп вплоть до 1, увеличивая атрибут атаки на 4% за каждый 1% отсутвующего хп.(Переключаемый)");
        super.setSpecialText(text, charges, cd);
    }

    @Override
    public void setUltimateText(List<String> text, int charges, int cd) {
        text =new ArrayList<>();
        charges = 1;
        cd = 60;
        text.add("Накладывает на себя и на всех существ в радиусе долг лечения на 300% от Макс хп владельца.");
        text.add("(долг лечения запрещает лечиться, пока не будет исчерпан, каждое лечение будет его уменьшать 1 к 1).");
        text.add("При текущем хп меньше 50% также наносит 1000% урона.");
        super.setUltimateText(text, charges, cd);
    }

    @Override
    public void setLastInfo(String damageType, String getting) {
        damageType = "Сквозь броню";
        getting = "Убить 100 существ в аду, имея не больше 1 хп";
        super.setLastInfo(damageType, getting);
    }

    @Override
    public void setStellar(String text, ItemStack stack) {
        text = "Пассив сильнее в 1.5 раза. Срез лечения не выше 99%";
        super.setStellar(text, stack);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        if(player.getHealth() <= 19)
            this.setStellar("", stack);
        /*AttributeInstance attribute = player.getAttribute(Attributes.ATTACK_DAMAGE);
        float multiplier = (float) ((((player.getHealth()/player.getMaxHealth()) * 0.4) + 1)*attribute.getValue());
        UUID uuid = UUID.fromString("794251c7-77e4-4959-8dbe-071afb64a4c9");
        AttributeModifier modifier = new AttributeModifier(uuid.toString(), multiplier, AttributeModifier.Operation.ADDITION);
        AttributeModifier modifierMinus = new AttributeModifier(uuid.toString(), -multiplier, AttributeModifier.Operation.ADDITION);
        if(attribute.hasModifier(modifier)) {
            attribute.addTransientModifier(modifierMinus);
            attribute.removeModifier(uuid);
        }
        attribute.addTransientModifier(modifier);*/
        if(isSpecialActive && player.getHealth() > player.getMaxHealth()*0.1) {
            if(player.tickCount % 5 == 0) {
                player.setHealth((float) (player.getHealth() - player.getMaxHealth() * 0.01));
            }
        }
        super.curioTick(slotContext, stack);
    }

    @Override
    public int setSpecialActive(long seconds, Player player) {
        if(this.isSpecialActive) {
            this.isSpecialActive = false;
            seconds = 0;
        }
        return super.setSpecialActive(seconds, player);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level) {
        super.doSpecial(seconds, player, level);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        super.doUltimate(seconds, player, level);
    }
}
