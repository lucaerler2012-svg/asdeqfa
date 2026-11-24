package com.lucae.reducedeffects.client.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Macht Feuer-Partikel langsamer und niedriger
 */
@Mixin(FlameParticle.class)
public class FlameParticleMixin {

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onFlameParticleCreated(ClientLevel level, double x, double y, double z,
										 double xSpeed, double ySpeed, double zSpeed,
										 CallbackInfo ci) {
		FlameParticle particle = (FlameParticle) (Object) this;

		// Partikel-Geschwindigkeit reduzieren (langsamere Animation)
		particle.xd *= 0.3;
		particle.yd *= 0.3;
		particle.zd *= 0.3;

		// Lebensdauer leicht erhöhen damit es nicht zu schnell verschwindet
		particle.lifetime = (int) (particle.lifetime * 1.5);
	}

	@Inject(method = "getQuadSize", at = @At("HEAD"), cancellable = true)
	private void reduceParticleSize(float scale, CallbackInfoReturnable<Float> cir) {
		// Partikel-Größe reduzieren (kleinere Flammen)
		cir.setReturnValue(cir.getReturnValue() * 0.6f);
	}
}