package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.event.events.PushEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Velocity extends Module {
  private static Velocity INSTANCE = new Velocity();
  
  public Setting<Boolean> knockBack = register(new Setting("KnockBack", Boolean.valueOf(true)));
  
  public Setting<Boolean> noPush = register(new Setting("NoPush", Boolean.valueOf(true)));
  
  public Setting<Float> horizontal = register(new Setting("Horizontal", Float.valueOf(0.0F), Float.valueOf(0.0F), Float.valueOf(100.0F)));
  
  public Setting<Float> vertical = register(new Setting("Vertical", Float.valueOf(0.0F), Float.valueOf(0.0F), Float.valueOf(100.0F)));
  
  public Setting<Boolean> explosions = register(new Setting("Explosions", Boolean.valueOf(true)));
  
  public Setting<Boolean> bobbers = register(new Setting("Bobbers", Boolean.valueOf(true)));
  
  public Setting<Boolean> water = register(new Setting("Water", Boolean.valueOf(false)));
  
  public Setting<Boolean> blocks = register(new Setting("Blocks", Boolean.valueOf(false)));
  
  public Setting<Boolean> ice = register(new Setting("Ice", Boolean.valueOf(false)));
  
  public Velocity() {
    super("Velocity", "Allows you to control your velocity", Module.Category.MOVEMENT, true, false, false);
    setInstance();
  }
  
  public static Velocity getINSTANCE() {
    if (INSTANCE == null)
      INSTANCE = new Velocity(); 
    return INSTANCE;
  }
  
  private void setInstance() {
    INSTANCE = this;
  }
  
  public void onUpdate() {
    if (IceSpeed.getINSTANCE().isOff() && ((Boolean)this.ice.getValue()).booleanValue()) {
      Blocks.ICE.slipperiness = 0.6F;
      Blocks.PACKED_ICE.slipperiness = 0.6F;
      Blocks.FROSTED_ICE.slipperiness = 0.6F;
    } 
  }
  
  public void onDisable() {
    if (IceSpeed.getINSTANCE().isOff()) {
      Blocks.ICE.slipperiness = 0.98F;
      Blocks.PACKED_ICE.slipperiness = 0.98F;
      Blocks.FROSTED_ICE.slipperiness = 0.98F;
    } 
  }
  
  @SubscribeEvent
  public void onPacketReceived(PacketEvent.Receive event) {
    if (event.getStage() == 0 && mc.player != null) {
      SPacketEntityVelocity velocity;
      if (((Boolean)this.knockBack.getValue()).booleanValue() && event.getPacket() instanceof SPacketEntityVelocity && (velocity = (SPacketEntityVelocity)event.getPacket()).getEntityID() == mc.player.entityId) {
        if (((Float)this.horizontal.getValue()).floatValue() == 0.0F && ((Float)this.vertical.getValue()).floatValue() == 0.0F) {
          event.setCanceled(true);
          return;
        } 
        velocity.motionX = (int)(velocity.motionX * ((Float)this.horizontal.getValue()).floatValue());
        velocity.motionY = (int)(velocity.motionY * ((Float)this.vertical.getValue()).floatValue());
        velocity.motionZ = (int)(velocity.motionZ * ((Float)this.horizontal.getValue()).floatValue());
      } 
      Entity entity;
      SPacketEntityStatus packet;
      if (event.getPacket() instanceof SPacketEntityStatus && ((Boolean)this.bobbers.getValue()).booleanValue() && (packet = (SPacketEntityStatus)event.getPacket()).getOpCode() == 31 && entity = packet.getEntity((World)mc.world) instanceof EntityFishHook) {
        EntityFishHook fishHook = (EntityFishHook)entity;
        if (fishHook.caughtEntity == mc.player)
          event.setCanceled(true); 
      } 
      if (((Boolean)this.explosions.getValue()).booleanValue() && event.getPacket() instanceof SPacketExplosion) {
        SPacketExplosion velocity_ = (SPacketExplosion)event.getPacket();
        velocity_.motionX *= ((Float)this.horizontal.getValue()).floatValue();
        velocity_.motionY *= ((Float)this.vertical.getValue()).floatValue();
        velocity_.motionZ *= ((Float)this.horizontal.getValue()).floatValue();
      } 
    } 
  }
  
  @SubscribeEvent
  public void onPush(PushEvent event) {
    if (event.getStage() == 0 && ((Boolean)this.noPush.getValue()).booleanValue() && event.entity.equals(mc.player)) {
      if (((Float)this.horizontal.getValue()).floatValue() == 0.0F && ((Float)this.vertical.getValue()).floatValue() == 0.0F) {
        event.setCanceled(true);
        return;
      } 
      event.x = -event.x * ((Float)this.horizontal.getValue()).floatValue();
      event.y = -event.y * ((Float)this.vertical.getValue()).floatValue();
      event.z = -event.z * ((Float)this.horizontal.getValue()).floatValue();
    } else if (event.getStage() == 1 && ((Boolean)this.blocks.getValue()).booleanValue()) {
      event.setCanceled(true);
    } else if (event.getStage() == 2 && ((Boolean)this.water.getValue()).booleanValue() && mc.player != null && mc.player.equals(event.entity)) {
      event.setCanceled(true);
    } 
  }
}
