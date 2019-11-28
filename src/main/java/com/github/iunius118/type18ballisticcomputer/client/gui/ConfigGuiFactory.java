package com.github.iunius118.type18ballisticcomputer.client.gui;

import com.github.iunius118.type18ballisticcomputer.Type18BallisticComputer;
import com.github.iunius118.type18ballisticcomputer.config.BallisticComputerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.List;
import java.util.Set;

public class ConfigGuiFactory implements IModGuiFactory {
    @Override
    public void initialize(Minecraft minecraft) {

    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        List<IConfigElement> elements;
        elements = ConfigElement.from(BallisticComputerConfig.class).getChildElements();

        return new GuiConfig(parentScreen, elements, Type18BallisticComputer.MOD_ID, false, false, Type18BallisticComputer.MOD_NAME);
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }
}
