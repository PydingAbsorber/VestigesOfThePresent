package com.pyding.vp.mixin;

import com.pyding.vp.item.ChaosOrb;
import com.pyding.vp.item.CorruptFragment;
import com.pyding.vp.item.CorruptItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin<T extends AbstractContainerMenu> extends Screen {

    protected AbstractContainerScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null)
            return;
        ItemStack carried = player.containerMenu.getCarried();
        if (carried.isEmpty() || !(carried.getItem() instanceof CorruptFragment || carried.getItem() instanceof CorruptItem || carried.getItem() instanceof ChaosOrb))
            return;
        AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>) (Object) this;
        for (Slot slot : screen.getMenu().slots) {
            if (isPointInRegion(slot.x, slot.y, 16, 16, mouseX, mouseY, screen)) {
                ItemStack hovered = slot.getItem();
                if (!hovered.isEmpty()) {
                    guiGraphics.renderTooltip(mc.font, hovered, mouseX, mouseY);
                }
                break;
            }
        }
    }

    private boolean isPointInRegion(int slotX, int slotY, int width, int height,
                                    double mouseX, double mouseY, AbstractContainerScreen<?> screen) {
        int guiLeft = screen.getGuiLeft();
        int guiTop = screen.getGuiTop();
        mouseX -= guiLeft;
        mouseY -= guiTop;
        return mouseX >= slotX && mouseX < slotX + width && mouseY >= slotY && mouseY < slotY + height;
    }
}
