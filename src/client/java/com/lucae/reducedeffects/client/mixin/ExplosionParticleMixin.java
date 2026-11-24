package com.lucae.reducedeffects.client.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ExplosionParticle;
import net.minecraft.client.particle.LargeExplosionParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Entfernt Explosions-Partikel auf Client-Seite für TNT, End Crystals, Respawn Anchor
 */
@Mixin(ParticleEngine.class)
public class ExplosionParticleMixin {

	@Inject(method = "createParticle", at = @At("HEAD"), cancellable = true)
	private void cancelExplosionParticles(ClientLevel level, net.minecraft.core.particles.ParticleOptions particleOptions,
										   double x, double y, double z, double xSpeed, double ySpeed, double zSpeed,
										   CallbackInfoReturnable<Particle> cir) {
		// Explosion-Partikel-Typen prüfen
		if (particleOptions.getType() == ParticleTypes.EXPLOSION ||
			particleOptions.getType() == ParticleTypes.LARGE_EXPLOSION ||
			particleOptions.getType() == ParticleTypes.EXPLOSION_EMITTER) {

			// Hier könnten wir die Quelle prüfen, aber auf Client-Seite ist das schwierig
			// Wir blockieren alle Explosions-Partikel für einen "clean" Look
			cir.setReturnValue(null);
		}
	}

	// Alternative: ExplosionParticle direkt mixen
	@Mixin(ExplosionParticle.class)
	public static class ExplosionParticleMixinInner {

		@Inject(method = "<init>", at = @At("HEAD"), cancellable = true)
		private void onExplosionParticleCreated(ClientLevel level, double x, double y, double z,
												 double xSpeed, double ySpeed, double zSpeed,
												 CallbackInfo ci) {
			// Explosion-Partikel gar nicht erst erstellen
			ci.cancel();
		}
	}

	@Mixin(LargeExplosionParticle.class)
	public static class LargeExplosionParticleMixinInner {

		@Inject(method = "<init>", at = @At("HEAD"), cancellable = true)
		private void onLargeExplosionParticleCreated(ClientLevel level, double x, double y, double z,
													  double xSpeed, double ySpeed, double zSpeed,
													  CallbackInfo ci) {
			// Große Explosion-Partikel gar nicht erst erstellen
			ci.cancel();
		}
	}
}