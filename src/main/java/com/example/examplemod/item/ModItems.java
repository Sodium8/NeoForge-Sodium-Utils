package com.example.examplemod.item;

import com.example.examplemod.SodiumUtilMod;
import com.example.examplemod.item.custom.GpsCompassItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SodiumUtilMod.MODID);
    public static final DeferredItem<Item> GPS_COMPASS_ITEM = ITEMS.register("gps_compass_item",
            ()->new GpsCompassItem(new Item.Properties()));
}
