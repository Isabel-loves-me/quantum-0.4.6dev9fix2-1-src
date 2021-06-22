package me.alpha432.oyvey.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.GraphicsEnvironment;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.ClientEvent;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FontMod extends Module {
  private static FontMod INSTANCE = new FontMod();
  
  public Setting<String> fontName = register(new Setting("FontName", "Arial", "Name of the font."));
  
  public Setting<Boolean> antiAlias = register(new Setting("AntiAlias", Boolean.valueOf(true), "Smoother font."));
  
  public Setting<Boolean> fractionalMetrics = register(new Setting("Metrics", Boolean.valueOf(true), "Thinner font."));
  
  public Setting<Boolean> customAll = register(new Setting("FullCustom", Boolean.valueOf(true), "Renders font everywhere"));
  
  public Setting<Integer> fontSize = register(new Setting("Size", Integer.valueOf(18), Integer.valueOf(12), Integer.valueOf(30), "Size of the font."));
  
  public Setting<Integer> fontStyle = register(new Setting("Style", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(3), "Style of the font."));
  
  private boolean reloadFont = false;
  
  public FontMod() {
    super("Font", "f", Module.Category.CLIENT, true, false, false);
    setInstance();
  }
  
  public static FontMod getInstance() {
    if (INSTANCE == null)
      INSTANCE = new FontMod(); 
    return INSTANCE;
  }
  
  public static boolean checkFont(String font, boolean message) {
    String[] fonts;
    for (String s : fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
      if (!message && s.equals(font))
        return true; 
      if (message)
        Command.sendMessage(s); 
    } 
    return false;
  }
  
  private void setInstance() {
    INSTANCE = this;
  }
  
  @SubscribeEvent
  public void onSettingChange(ClientEvent event) {
    Setting setting;
    if (event.getStage() == 2 && (setting = event.getSetting()) != null && setting.getFeature().equals(this)) {
      if (setting.getName().equals("FontName") && !checkFont(setting.getPlannedValue().toString(), false)) {
        Command.sendMessage(ChatFormatting.RED + "That font doesnt exist.");
        event.setCanceled(true);
        return;
      } 
      this.reloadFont = true;
    } 
  }
  
  public void onTick() {
    if (this.reloadFont) {
      OyVey.textManager.init(false);
      this.reloadFont = false;
    } 
  }
}
