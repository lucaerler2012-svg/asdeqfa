package com.lucae.reducedeffects.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

/**
 * Reduziert die Feuer-Höhe und verlangsamt die Ausbreitung
 */
@Mixin(BaseFireBlock.class)
public class FireBlockMixin {

	@Inject(method = "getHeight", at = @At("HEAD"), cancellable = true)
	private void reduceFireHeight(BlockState state, Level level, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
		// Feuer-Höhe von normalerweise 15 auf 8 reduzieren (ca. halb so hoch)
		cir.setReturnValue(8);
	}

	@Inject(method = "getFlammability", at = @At("HEAD"), cancellable = true)
	private void reduceFlammability(BlockState state, Level level, BlockPos pos, Direction face, CallbackInfoReturnable<Integer> cir) {
		// Entzündbarkeit leicht reduzieren
		cir.setReturnValue(cir.getReturnValue() / 2);
	}

	@Inject(method = "getFireSpreadSpeed", at = @At("HEAD"), cancellable = true)
	private void reduceFireSpreadSpeed(CallbackInfoReturnable<Integer> cir) {
		// Feuer-Ausbreitungsgeschwindigkeit verlangsamen (Standard ist 30)
		cir.setReturnValue(60); // Doppelt so langsam
	}
}