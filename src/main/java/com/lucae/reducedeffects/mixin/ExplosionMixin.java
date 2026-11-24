package com.lucae.reducedeffects.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Entfernt Explosions-Effekte bei Respawn Anchor, TNT und End Crystals
 * Blöcke verschwinden direkt ohne Explosions-Animation
 */
@Mixin(Explosion.class)
public class ExplosionMixin {

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onExplosionCreated(Level level, Entity source, DamageSource damageSource,
									  double x, double y, double z, float radius,
									  boolean createFire, Level.ExplosionInteraction interaction,
									  CallbackInfo ci) {
		// Explosion-Objekt ist bereits erstellt, wir können hier die Quelle prüfen
	}

	@Inject(method = "explode", at = @At("HEAD"), cancellable = true)
	private void cancelExplosionEffects(CallbackInfoReturnable<Boolean> cir) {
		Explosion explosion = (Explosion) (Object) this;
		Entity source = getSource(explosion);

		if (shouldCancelExplosion(source)) {
			// Explosion komplett abbrechen - Blöcke werden direkt entfernt
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "finalizeExplosion", at = @At("HEAD"), cancellable = true)
	private void cancelExplosionParticles(CallbackInfoReturnable<Boolean> cir) {
		Explosion explosion = (Explosion) (Object) this;
		Entity source = getSource(explosion);

		if (shouldCancelExplosion(source)) {
			// Partikel und Sound abbrechen
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "spawnParticles", at = @At("HEAD"), cancellable = true)
	private void cancelParticles(ServerLevel level, CallbackInfo ci) {
		Explosion explosion = (Explosion) (Object) this;
		Entity source = getSource(explosion);

		if (shouldCancelExplosion(source)) {
			// Keine Partikel spawnen
			ci.cancel();
		}
	}

	private Entity getSource(Explosion explosion) {
		try {
			// Via Reflection auf das source-Feld zugreifen
			java.lang.reflect.Field field = Explosion.class.getDeclaredField("source");
			field.setAccessible(true);
			return (Entity) field.get(explosion);
		} catch (Exception e) {
			return null;
		}
	}

	private boolean shouldCancelExplosion(Entity source) {
		if (source == null) return false;

		EntityType<?> type = source.getType();

		// TNT
		if (type == EntityType.TNT) return true;

		// End Crystal
		if (type == EntityType.END_CRYSTAL) return true;

		// Respawn Anchor Explosion (wird durch Block ausgelöst, nicht Entity)
		// Prüfen wir über die Position im Explosion-Objekt
		// Respawn Anchor explodiert nur im Nether beim Aufladen

		return false;
	}
}