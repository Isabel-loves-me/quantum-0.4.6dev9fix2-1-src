package me.alpha432.oyvey.util;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

public class Wrapper {
  public static Minecraft mc = Minecraft.getMinecraft();
  
  public static FontRenderer fontRenderer = mc.fontRenderer;
  
  public static EntityPlayerSP getPlayer() {
    return (getMinecraft()).player;
  }
  
  public static Minecraft getMinecraft() {
    return mc;
  }
  
  public static World getWorld() {
    return (World)(getMinecraft()).world;
  }
  
  public static int getKey(String keyname) {
    return Keyboard.getKeyIndex(keyname.toUpperCase());
  }
  
  public static FontRenderer fr = (Minecraft.getMinecraft()).fontRenderer;
  
  public static volatile Wrapper INSTANCE = new Wrapper();
  
  public Minecraft mc() {
    return Minecraft.getMinecraft();
  }
  
  public EntityPlayerSP player() {
    return (INSTANCE.mc()).player;
  }
  
  public WorldClient world() {
    return (INSTANCE.mc()).world;
  }
  
  public GameSettings mcSettings() {
    return (INSTANCE.mc()).gameSettings;
  }
  
  public FontRenderer fontRenderer() {
    return (INSTANCE.mc()).fontRenderer;
  }
  
  public void sendPacket(Packet packet) {
    (player()).connection.sendPacket(packet);
  }
  
  public PlayerControllerMP controller() {
    return (INSTANCE.mc()).playerController;
  }
  
  public void swingArm() {
    player().swingArm(EnumHand.MAIN_HAND);
  }
  
  public void attack(Entity entity) {
    controller().attackEntity((EntityPlayer)player(), entity);
  }
  
  public void copy(String content) {
    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(content), null);
  }
}
