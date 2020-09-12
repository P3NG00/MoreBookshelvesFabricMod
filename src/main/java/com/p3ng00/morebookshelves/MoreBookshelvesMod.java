package com.p3ng00.morebookshelves;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;

public class MoreBookshelvesMod implements ModInitializer {

    private enum Bookshelf {

        SPRUCE(true),
        BIRCH(true),
        JUNGLE(true),
        ACACIA(true),
        DARK_OAK(true),
        CRIMSON(false),
        WARPED(false);

        public final boolean fuel;

        Bookshelf(boolean fuel) {
            this.fuel = fuel;
        }

    }

    @Override
    public void onInitialize() {

        Arrays.stream(Bookshelf.values()).forEach(type -> {

            Block bookshelf = new Block(AbstractBlock.Settings.copy(Blocks.BOOKSHELF));

            Identifier id = new Identifier("morebookshelves", String.format("%s_bookshelf", type.name().toLowerCase()));

            Registry.register(Registry.BLOCK, id, bookshelf);
            Registry.register(Registry.ITEM, id, new BlockItem(bookshelf, new Item.Settings().group(ItemGroup.DECORATIONS)));

            if (type.fuel)
                FuelRegistry.INSTANCE.add(bookshelf, 300);

        });

    }

}
