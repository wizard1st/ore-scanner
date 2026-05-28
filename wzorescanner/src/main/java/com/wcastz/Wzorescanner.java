package com.wcastz;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.level.block.Blocks;

import java.util.function.Function;

import org.slf4j.LoggerFactory;

public class Wzorescanner implements ModInitializer {
	public static final String MOD_ID = "wzorescanner";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	//register mod item
	public static <T extends Item> T register(String name,Function<Item.Properties,T> itemFactory, Item.Properties settings){
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Wzorescanner.MOD_ID, name));
		T item = itemFactory.apply(settings.setId(itemKey));
		Registry.register(BuiltInRegistries.ITEM, itemKey, item);
		return item;
	}

	//mod item
	public static final Item ORE_SCANNER_BASE = register("ore_scanner_base",
		(settings) -> new OreScannerItem(settings), new Item.Properties().stacksTo(1));
	public static final Item ORE_SCANNER_COAL = register("coal_ore_scanner",
		(settings) -> new OreScannerItem(settings,Blocks.COAL_ORE,Blocks.DEEPSLATE_COAL_ORE), new Item.Properties().stacksTo(1));
	public static final Item ORE_SCANNER_IRON = register("iron_ore_scanner",
		(settings) -> new OreScannerItem(settings,Blocks.IRON_ORE,Blocks.DEEPSLATE_IRON_ORE), new Item.Properties().stacksTo(1));
	public static final Item ORE_SCANNER_GOLD = register("gold_ore_scanner",
		(settings) -> new OreScannerItem(settings,Blocks.GOLD_ORE,Blocks.DEEPSLATE_GOLD_ORE,Blocks.NETHER_GOLD_ORE), new Item.Properties().stacksTo(1));
	public static final Item ORE_SCANNER_LAPIS = register("lapis_ore_scanner",
		(settings) -> new OreScannerItem(settings,Blocks.LAPIS_ORE,Blocks.DEEPSLATE_LAPIS_ORE), new Item.Properties().stacksTo(1));
	public static final Item ORE_SCANNER_EMERALD = register("emerald_ore_scanner",
		(settings) -> new OreScannerItem(settings,Blocks.EMERALD_ORE,Blocks.DEEPSLATE_EMERALD_ORE), new Item.Properties().stacksTo(1));
	public static final Item ORE_SCANNER_NETHERITE = register("netherite_ore_scanner",
		(settings) -> new OreScannerItem(settings,Blocks.ANCIENT_DEBRIS), new Item.Properties().stacksTo(1));
	public static final Item ORE_SCANNER_DIAMOND = register("diamond_ore_scanner",
		(settings) -> new OreScannerItem(settings,Blocks.DIAMOND_ORE,Blocks.DEEPSLATE_DIAMOND_ORE), new Item.Properties().stacksTo(1));
	
	public static final ResourceKey<CreativeModeTab> SCANNER_CREATIVE_TAB_KEY = ResourceKey.create(
		BuiltInRegistries.CREATIVE_MODE_TAB.key(), Identifier.fromNamespaceAndPath(Wzorescanner.MOD_ID, "ore_scanner_tab")
	);

	public static final CreativeModeTab SCANNER_CREATIVE_TAB = FabricCreativeModeTab.builder()
		.icon(() -> new ItemStack(Wzorescanner.ORE_SCANNER_BASE))
		.title(Component.translatable("wzorescanner.ore_scanner_tab"))
		.displayItems((params, output) -> {
			output.accept(ORE_SCANNER_BASE);
			output.accept(ORE_SCANNER_COAL);
			output.accept(ORE_SCANNER_IRON);
			output.accept(ORE_SCANNER_GOLD);
			output.accept(ORE_SCANNER_LAPIS);
			output.accept(ORE_SCANNER_EMERALD);
			output.accept(ORE_SCANNER_DIAMOND);
			output.accept(ORE_SCANNER_NETHERITE);
		})
		.build();
	
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("onInitialize: " + MOD_ID);
		initializeItem();
	}

	private void initializeItem() {
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, SCANNER_CREATIVE_TAB_KEY, SCANNER_CREATIVE_TAB);
		// CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
		// 		.register((creativeTab) -> creativeTab.accept(ORE_SCANNER_BASE));
	}

}