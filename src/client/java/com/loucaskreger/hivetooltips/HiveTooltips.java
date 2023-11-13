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
    private static final int MAX_HONEY_LEVEL = 5;

    @Override
    public void onInitializeClient() {
        ItemTooltipCallback.EVENT.register(this::onItemTooltip);
    }

    private void onItemTooltip(ItemStack stack, TooltipContext context, List<Text> lines) {
//		Get the number of bees and honey level from the itemstack nbt data
        if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof BeehiveBlock) {
            var hiveNbt = stack.getNbt();
            if (hiveNbt == null)
                return;

            if (hiveNbt.contains("BlockEntityTag")) {
                var blockEntityTag = hiveNbt.getCompound("BlockEntityTag");
                if (blockEntityTag.contains("Bees")) {
                    var bees = blockEntityTag.getList("Bees", NbtElement.COMPOUND_TYPE);
                    lines.add(Text.literal(String.format("(%d/%d) Bees", bees.size(), MAX_BEES)).setStyle(Style.EMPTY.withColor(0x347debff)));
                }
            }
            if (hiveNbt.contains("BlockStateTag")) {
                var blockStateTag = hiveNbt.getCompound("BlockStateTag");
                if (blockStateTag.contains("honey_level")) {
                    // Empty honey levels are stored as an int https://bugs.mojang.com/browse/MC-179531
                    String honeyLevel = blockStateTag.getString("honey_level");
                    if (honeyLevel.isEmpty())
                        honeyLevel = "" + blockStateTag.getInt("honey_level");
                    lines.add(Text.literal(String.format("(%s/%d) Honey", honeyLevel, MAX_HONEY_LEVEL)).setStyle(Style.EMPTY.withColor(0xFFFFFF00)));
                }
            }
        }
    }
}