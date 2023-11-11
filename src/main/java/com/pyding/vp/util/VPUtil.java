package com.pyding.vp.util;

import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.InterModComms;
import top.theillusivec4.curios.api.SlotTypeMessage;

import javax.annotation.Nullable;
import java.util.Random;

public class VPUtil {
    public static void registerCurioType(final String identifier, final int slots, final boolean isHidden, @Nullable final ResourceLocation icon) {
        final SlotTypeMessage.Builder message = new SlotTypeMessage.Builder(identifier);

        message.size(slots);

        if (isHidden) {
            message.hide();
        }

        if (icon != null) {
            message.icon(icon);
        }

        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> message.build());

    }
    public static String getRainbowString(String text) {
        StringBuilder coloredText = new StringBuilder();

        Random random = new Random();
        ChatFormatting[] colors = ChatFormatting.values();

        for (char letter : text.toCharArray()) {
            ChatFormatting color = colors[random.nextInt(colors.length - 1) + 1];
            coloredText.append(color).append(letter);
        }

        return coloredText.toString();
    }

    public static String getDarkString(String text) {
        StringBuilder coloredText = new StringBuilder();
        ChatFormatting[] colors = ChatFormatting.values();

        for (char letter : text.toCharArray()) {
            ChatFormatting color;
            if(Math.random() > 0.5){
                color = ChatFormatting.BLACK;
            } else {
                if(Math.random() > 0.5){
                    color = ChatFormatting.DARK_GRAY;
                }
                else color = ChatFormatting.GRAY;
            }
            coloredText.append(color).append(letter);
        }

        return coloredText.toString();
    }

    public static String generateRandomString(int length) {
        String characters = "≣≤≥≦≧≨≩≪≫≬≭≮≯≰≱≲≳≴≵≶≷≸≹≺≻≼≽≾≿⊀⊁⊂⊃⊄⊅⊆⊇⊈⊉⊊⊋⊌⊍⊎⊏⊐⊑⊒⊓⊔⊕⊖⊗⊘⊙⊚⊛⊜⊝⊞⊟⊠⊡⊢⊣⊤⊥⊦⊧⊨⊩⊪⊫⊬⊭⊮⊯⊰⊱⊲⊳⊴⊵⊶⊷⊸⊹⊺⊻⊼⊽⊾⊿⋀⋁⋂⋃⋄⋅⋆⋇⋈⋉⋊⋋⋌⋍⋎⋏⋐⋑⋒⋓⋔⋕⋖⋗⋘⋙⋚⋛⋜⋝⋞⋟▲△▴▵▶▷▸▹►▻▼▽▾▿◀◁◂◃◄◅◆◇◈◉◊○◌◍◎●◐◑◒◓◔◕◖◗◘◙◚◛◜◝◞◟◠◡◢◣◤◥◦◧◨◩◪◫◬◭◮◯☀☁☂☃☄★☆☇☈☉☊☋☌☍☎☏☐☑☒☓☖☗☚☛☜☝☞☟☠☡☢☣☤☥☦☧☨☩☪☫☬☭☮☯✁✂✃✄✆✇✈✉✌✍✎✏✐✑✒✓✔✕✖✗✘✙✚✛✜✝✞✟✠✡✢✣✤✥✦✧✩✪✫✬✭✮✯✰✱✲✳✴✵✶✷✸✹✺✻✼✽✾✿❀❁❂❃❄❅❆❇❈❉❊❋❍❏❐❑❒❖❡❢❣❤❥❦❧❘❙❚❛❜❝❞➱➲➳➴➵➶➷➸➘➙➚➛➜➝➞➟➠➡➢➣➤➥➦➧➨➩➪➫➬➭➮➯➉➔➹➺➻➼➽➾➿ൠൡ•‣‑‒–—―‖‗‘’‚‛“”„‟†‡‰′″‴‵‶‷‸‹›※‼‽‾‿⁀⁁⁂⁃⁄⁅⁆⁐⁑₰₱₲₳₴₵⃒⃓⃘⃙⃚⃑⃔⃕⃖⃗⃛⃜⃝⃞⃟⃠⃡⃢⃣℀℁ℂ℃℄℅℆ℇ℈℉ℊℋℌℍℎℏℐℑℒℓ℔ℕ№℗℘ℙℚℛℜℝ℞℟℠℡™℣ℤ℥Ω℧ℨ℩KÅℬℭ℮ℯℰℱℲℳℴ⅓⅔⅕⅖⅗⅘⅙⅚⅛⅜⅝⅞⅟ⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩⅪⅫⅬℵℶℷℸℹ℻ⅅⅆⅇⅈⅉ⅍ⅎⅭⅮⅯⅰⅱⅲⅳⅴⅵⅶⅷⅸⅹⅺⅻⅼⅽⅾⅿↀↁↂↄ←↑→↓↔↕↖↗↘↙↚↛↜↝↞↟↠↡↢↣↤↥↦↧↨↩↪↫↬↭↮↯↰↱↲↳↴↵↶↷↸↹↺↻↼↽↾↿⇀⇁⇂⇃⇄⇅⇆⇇⇈⇉⇊⇋⇌⇍⇎⇏⇐⇑⇒⇓⇔⇕⇖⇗⇘⇙⇚⇛⇜⇝⇞⇟⇠⇡⇢⇣⇤⇥⇦⇧⇨⇩⇪∀∁∂∃∄∅∆∇∈∉∊∋∌∍∎∏∐∑−∓∔∕∖∗∘∙√∛∜∝∞∟∠∡∢∣∤∥∦∧∨∩∪∫∬∭∮∯∰∱∲∳∴∵∶∷∸∹∺∻∼∽∾∿≀≁≂≃≄≅≆≇≈≉≊≋≌≍≎≏≐≑≒≓≔≕≖≗≘≙≚≛≜≝≞≟≠≡≢";

        Random random = new Random();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            result.append(characters.charAt(randomIndex));
        }

        return result.toString();
    }

}
