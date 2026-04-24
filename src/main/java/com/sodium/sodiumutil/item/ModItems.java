package com.sodium.sodiumutil.item;

import com.sodium.sodiumutil.SodiumUtilMod;
import com.sodium.sodiumutil.item.custom.GpsCompassItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SodiumUtilMod.MODID);
    public static final DeferredItem<Item> GPS_COMPASS_ITEM = ITEMS.register("gps_compass_item",
            ()->new GpsCompassItem(new Item.Properties()));
}
