package com.p3ng00.morebookshelves.mixin;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Random;

@Mixin(EnchantmentScreenHandler.class)
abstract class EnchantmentScreenHandlerMixin extends ScreenHandler {

    @Overwrite
    public void onContentChanged(Inventory inventory) {

        if (inventory == this.inventory) {

            ItemStack itemStack = inventory.getStack(0);

            if (!itemStack.isEmpty() && itemStack.isEnchantable()) {

                this.context.run((world, blockPos) -> {

                    int i = 0, j;

                    for (j = -1; j <= 1; ++j) {

                        for (int k = -1; k <= 1; ++k) {

                            if ((j != 0 || k != 0) && world.isAir(blockPos.add(k, 0, j)) && world.isAir(blockPos.add(k, 1, j))) {

                                Tag<Block> bookshelves = BlockTags.getTagGroup().getTag(new Identifier("morebookshelves", "bookshelves"));

                                if (world.getBlockState(blockPos.add(k * 2, 0, j * 2)).isIn(bookshelves))
                                    ++i;

                                if (world.getBlockState(blockPos.add(k * 2, 1, j * 2)).isIn(bookshelves))
                                    ++i;

                                if (k != 0 && j != 0) {

                                    if (world.getBlockState(blockPos.add(k * 2, 0, j)).isIn(bookshelves))
                                        ++i;

                                    if (world.getBlockState(blockPos.add(k * 2, 1, j)).isIn(bookshelves))
                                        ++i;

                                    if (world.getBlockState(blockPos.add(k, 0, j * 2)).isIn(bookshelves))
                                        ++i;

                                    if (world.getBlockState(blockPos.add(k, 1, j * 2)).isIn(bookshelves))
                                        ++i;

                                }

                            }

                        }

                    }

                    this.random.setSeed(this.seed.get());

                    for (j = 0; j < 3; ++j) {

                        this.enchantmentPower[j] = EnchantmentHelper.calculateRequiredExperienceLevel(this.random, j, i, itemStack);
                        this.enchantmentId[j] = -1;
                        this.enchantmentLevel[j] = -1;

                        if (this.enchantmentPower[j] < j + 1)
                            this.enchantmentPower[j] = 0;

                    }


                    for (j = 0; j < 3; ++j) {

                        if (this.enchantmentPower[j] > 0) {

                            List<EnchantmentLevelEntry> list = this.generateEnchantments(itemStack, j, this.enchantmentPower[j]);

                            if (list != null && !list.isEmpty()) {

                                EnchantmentLevelEntry enchantmentLevelEntry = list.get(this.random.nextInt(list.size()));
                                this.enchantmentId[j] = Registry.ENCHANTMENT.getRawId(enchantmentLevelEntry.enchantment);
                                this.enchantmentLevel[j] = enchantmentLevelEntry.level;

                            }

                        }

                    }

                    this.sendContentUpdates();

                });

            } else {

                for (int i = 0; i < 3; ++i) {

                    this.enchantmentPower[i] = 0;
                    this.enchantmentId[i] = -1;
                    this.enchantmentLevel[i] = -1;

                }

            }

        }

    }

    protected EnchantmentScreenHandlerMixin(ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @Shadow
    private Inventory inventory;

    @Shadow
    private ScreenHandlerContext context;

    @Shadow
    private Random random;

    @Shadow
    private Property seed;

    @Shadow
    private int[] enchantmentPower;

    @Shadow
    private int[] enchantmentId;

    @Shadow
    private int[] enchantmentLevel;

    @Shadow
    private List<EnchantmentLevelEntry> generateEnchantments(ItemStack stack, int slot, int level) {
        return null;
    }

}
