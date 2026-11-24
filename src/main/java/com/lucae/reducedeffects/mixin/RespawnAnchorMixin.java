package com.lucae.reducedeffects.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Verhindert Respawn Anchor Explosion im Overworld/End
 * Stattdessen wird der Block direkt entfernt/gesetzt ohne Explosion
 */
@Mixin(RespawnAnchorBlock.class)
public class RespawnAnchorMixin {

	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	private void onRespawnAnchorUse(BlockState state, Level level, BlockPos pos,
									 Player player, InteractionHand hand, BlockHitResult hit,
									 CallbackInfoReturnable<InteractionResult> cir) {
		if (!level.isClientSide()) {
			ItemStack itemStack = player.getItemInHand(hand);

			// Wenn Glowstone benutzt wird im Overworld/End
			if (itemStack.is(Items.GLOWSTONE) && !level.dimensionType().natural()) {
				// Explosion verhindern - stattdessen Block direkt brechen/entfernen
				// Setze Block auf Luft ohne Explosion
				level.destroyBlock(pos, false, player);
				cir.setReturnValue(InteractionResult.SUCCESS);
			}
		}
	}

	@Inject(method = "charge", at = @At("HEAD"), cancellable = true)
	private void onRespawnAnchorCharge(BlockState state, Level level, BlockPos pos,
										Player player, CallbackInfoReturnable<InteractionResult> cir) {
		if (!level.isClientSide() && !level.dimensionType().natural()) {
			// Im Overworld/End: Keine Explosion, Block wird einfach entfernt
			level.destroyBlock(pos, false, player);
			cir.setReturnValue(InteractionResult.SUCCESS);
		}
	}
}