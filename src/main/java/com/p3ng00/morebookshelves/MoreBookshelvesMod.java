package com.p3ng00.morebookshelves;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;

public class MoreBookshelvesMod implements ModInitializer {

    @Override
    public void onInitialize() {

        Pair<String, Boolean>[] types = new Pair[] {

            new Pair<>("spruce", true),
            new Pair<>("birch", true),
            new Pair<>("jungle", true),
            new Pair<>("acacia", true),
            new Pair<>("dark_oak", true),
            new Pair<>("crimson", false),
            new Pair<>("warped", false)

        };

        for (Pair<String, Boolean> type : types) {

            Block bookshelf = new Block(AbstractBlock.Settings.copy(Blocks.BOOKSHELF));
            Identifier id = new Identifier("morebookshelves", String.format("%s_bookshelf", type.getLeft()));
            Registry.register(Registry.BLOCK, id, bookshelf);
            Registry.register(Registry.ITEM, id, new BlockItem(bookshelf, new Item.Settings().group(ItemGroup.DECORATIONS)));

            if (type.getRight()) {

                FuelRegistry.INSTANCE.add(bookshelf, 300);
                FlammableBlockRegistry.getDefaultInstance().add(bookshelf, 30, 20);

            }

        }

    }

}
