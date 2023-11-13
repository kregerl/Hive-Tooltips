package com.loucaskreger.hivetooltips;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.List;

public class HiveTooltips implements ClientModInitializer {
	private static final int MAX_BEES = 3;

	@Override
	public void onInitializeClient() {
		ItemTooltipCallback.EVENT.register(this::onItemTooltip);
	}

	private void onItemTooltip(ItemStack stack, TooltipContext context, List<Text> lines) {
//		Get the number of bees from the itemstack nbt data
		if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof BeehiveBlock) {
			var hiveNbt = stack.getNbt();
			if (hiveNbt != null && hiveNbt.contains("BlockEntityTag")) {
				var blockEntityTag = hiveNbt.getCompound("BlockEntityTag");
				if (blockEntityTag.contains("Bees")) {
					var bees = blockEntityTag.getList("Bees", NbtElement.COMPOUND_TYPE);
					lines.add(Text.literal(String.format("(%d/%d) Bees", bees.size(), MAX_BEES)).setStyle(Style.EMPTY.withColor(0x347debff)));
				}
			}
		}
	}
}