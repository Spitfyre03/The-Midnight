package com.mushroom.midnight.common.registry;

import com.mushroom.midnight.Midnight;
import com.mushroom.midnight.common.entity.EntityBladeshroomCap;
import com.mushroom.midnight.common.entity.EntityRift;
import com.mushroom.midnight.common.entity.EntityThrownGeode;
import com.mushroom.midnight.common.entity.creature.EntityCrystalBug;
import com.mushroom.midnight.common.entity.creature.EntityDeceitfulSnapper;
import com.mushroom.midnight.common.entity.creature.EntityHunter;
import com.mushroom.midnight.common.entity.creature.EntityNightStag;
import com.mushroom.midnight.common.entity.creature.EntityNova;
import com.mushroom.midnight.common.entity.creature.EntityPenumbrian;
import com.mushroom.midnight.common.entity.creature.EntitySkulk;
import com.mushroom.midnight.common.entity.creature.EntityRifter;
import com.mushroom.midnight.common.entity.creature.EntityStinger;
import com.mushroom.midnight.common.entity.creature.EntityTreeHopper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

@Mod.EventBusSubscriber(modid = Midnight.MODID)
public class ModEntities {
    private static int currentEntityId;

    @SubscribeEvent
    public static void onRegisterEntities(RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().registerAll(
                EntityEntryBuilder.create()
                        .entity(EntityRift.class)
                        .factory(EntityRift::new)
                        .id(new ResourceLocation(Midnight.MODID, "rift"), currentEntityId++)
                        .name(Midnight.MODID + ".rift")
                        .tracker(1024, 20, false)
                        .build(),
                EntityEntryBuilder.create()
                        .entity(EntityRifter.class)
                        .factory(EntityRifter::new)
                        .id(new ResourceLocation(Midnight.MODID, "rifter"), currentEntityId++)
                        .name(Midnight.MODID + ".rifter")
                        .tracker(80, 3, true)
                        .egg(0x384740, 0x5E8C6C)
                        .build(),
                EntityEntryBuilder.create()
                        .entity(EntityHunter.class)
                        .factory(EntityHunter::new)
                        .id(new ResourceLocation(Midnight.MODID, "hunter"), currentEntityId++)
                        .name(Midnight.MODID + ".hunter")
                        .tracker(80, 2, true)
                        .egg(0x2C3964, 0x19203A)
                        .build(),
                EntityEntryBuilder.create()
                        .entity(EntityBladeshroomCap.class)
                        .factory(EntityBladeshroomCap::new)
                        .id(new ResourceLocation(Midnight.MODID, "bladeshroom_cap"), currentEntityId++)
                        .name(Midnight.MODID + ".bladeshroom_cap")
                        .tracker(64, 1, true)
                        .build(),
                EntityEntryBuilder.create()
                        .entity(EntityNova.class)
                        .factory(EntityNova::new)
                        .id(new ResourceLocation(Midnight.MODID, "nova"), currentEntityId++)
                        .name(Midnight.MODID + ".nova")
                        .tracker(80, 2, true)
                        .egg(0x932C3B, 0x47415E)
                        .build(),
                EntityEntryBuilder.create()
                        .entity(EntityCrystalBug.class)
                        .factory(EntityCrystalBug::new)
                        .id(new ResourceLocation(Midnight.MODID, "crystal_bug"), currentEntityId++)
                        .name(Midnight.MODID + ".crystal_bug")
                        .tracker(80, 3, false)
                        .egg(0x4B2277, 0xAF27E0)
                        .build(),
                EntityEntryBuilder.create()
                        .entity(EntityPenumbrian.class)
                        .factory(EntityPenumbrian::new)
                        .id(new ResourceLocation(Midnight.MODID, "penumbrian"), currentEntityId++)
                        .name(Midnight.MODID + ".penumbrian")
                        .tracker(80, 3, true)
                        //.egg(0x8CDCC9, 0x456B6F)
                        .build(),
                EntityEntryBuilder.create()
                        .entity(EntityTreeHopper.class)
                        .factory(EntityTreeHopper::new)
                        .id(new ResourceLocation(Midnight.MODID, "tree_hopper"), currentEntityId++)
                        .name(Midnight.MODID + ".tree_hopper")
                        .tracker(80, 3, true)
                        //.egg(0x6E5A84, 0x303847)
                        .build(),
                EntityEntryBuilder.create()
                        .entity(EntityStinger.class)
                        .factory(EntityStinger::new)
                        .id(new ResourceLocation(Midnight.MODID, "stinger"), currentEntityId++)
                        .name(Midnight.MODID + ".stinger")
                        .tracker(80, 3, true)
                        .egg(0x291E23, 0x4D6A43)
                        .build(),
                EntityEntryBuilder.create()
                        .entity(EntityNightStag.class)
                        .factory(EntityNightStag::new)
                        .id(new ResourceLocation(Midnight.MODID, "nightstag"), currentEntityId++)
                        .name(Midnight.MODID + ".nightstag")
                        .tracker(80, 3, true)
                        .egg(0x946CC2, 0x221F1D)
                        .build(),
                EntityEntryBuilder.create()
                        .entity(EntityDeceitfulSnapper.class)
                        .factory(EntityDeceitfulSnapper::new)
                        .id(new ResourceLocation(Midnight.MODID, "deceitful_snapper"), currentEntityId++)
                        .name(Midnight.MODID + ".deceitful_snapper")
                        .tracker(80, 3, true)
                        .egg(0x45404E, 0x906F99)
                        .build(),
                EntityEntryBuilder.create()
                        .entity(EntitySkulk.class)
                        .factory(EntitySkulk::new)
                        .id(new ResourceLocation(Midnight.MODID, "skulk"), currentEntityId++)
                        .name(Midnight.MODID + ".skulk")
                        .tracker(80, 3, true)
                        .egg(0x4B5065, 0x4E268A)
                        .build(),
                EntityEntryBuilder.create()
                        .entity(EntityThrownGeode.class)
                        .factory(EntityThrownGeode::new)
                        .id(new ResourceLocation(Midnight.MODID, "thrown_geode"), currentEntityId++)
                        .name(Midnight.MODID + ".thrown_geode")
                        .tracker(64, 10, true)
                        .build()
        );

        EntitySpawnPlacementRegistry.setPlacementType(EntityHunter.class, EntityLiving.SpawnPlacementType.IN_AIR);
        EntitySpawnPlacementRegistry.setPlacementType(EntityCrystalBug.class, EntityLiving.SpawnPlacementType.IN_AIR);
        EntitySpawnPlacementRegistry.setPlacementType(EntityNova.class, EntityLiving.SpawnPlacementType.IN_AIR);
        EntitySpawnPlacementRegistry.setPlacementType(EntityDeceitfulSnapper.class, EntityLiving.SpawnPlacementType.IN_WATER);
    }
}
