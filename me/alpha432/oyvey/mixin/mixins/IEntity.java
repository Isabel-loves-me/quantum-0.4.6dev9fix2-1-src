package me.alpha432.oyvey.mixin.mixins;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({Entity.class})
public interface IEntity {
  @Accessor("isInWeb")
  boolean getIsInWeb();
}
