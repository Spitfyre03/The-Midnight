package com.mushroom.midnight.common.registry;

import net.minecraft.block.SoundType;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.mushroom.midnight.Midnight.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class ModSounds {
    public static final SoundEvent AMBIENT = RegUtil.applyName(new SoundEvent(new ResourceLocation(MODID, "ambient")));
    public static final SoundEvent IDLE = RegUtil.applyName(new SoundEvent(new ResourceLocation(MODID, "idle")));
    public static final SoundEvent CAVE_IDLE = RegUtil.applyName(new SoundEvent(new ResourceLocation(MODID, "cave_idle")));

    public static final SoundEvent MUSIC_GENERIC = RegUtil.applyName(new SoundEvent(new ResourceLocation(MODID, "music_generic")));
    public static final SoundEvent MUSIC_CRYSTAL = RegUtil.applyName(new SoundEvent(new ResourceLocation(MODID, "music_crystal")));

    public static final SoundEvent RIFT_IDLE = RegUtil.applyName(new SoundEvent(new ResourceLocation(MODID, "rift_idle")));
    public static final SoundEvent RIFT_UNSTABLE = RegUtil.applyName(new SoundEvent(new ResourceLocation(MODID, "rift_unstable")));

    public static final SoundEvent RIFTER_AMBIENT = RegUtil.applyName(new SoundEvent(new ResourceLocation(MODID, "rifter_ambient")));
    public static final SoundEvent RIFTER_HURT = RegUtil.applyName(new SoundEvent(new ResourceLocation(MODID, "rifter_hurt")));
    public static final SoundEvent RIFTER_DEATH = RegUtil.applyName(new SoundEvent(new ResourceLocation(MODID, "rifter_death")));

    public static final SoundEvent BLADESHROOM_CAP_SHOOT = RegUtil.applyName(new SoundEvent(new ResourceLocation(MODID, "bladeshroom_cap_shoot")));
    public static final SoundEvent BLADESHROOM_CAP_HIT = RegUtil.applyName(new SoundEvent(new ResourceLocation(MODID, "bladeshroom_cap_hit")));

    public static final SoundEvent MUD_DIG = RegUtil.applyName(new SoundEvent(new ResourceLocation(MODID, "mud_dig")));
    public static final SoundEvent MUD_STEP = RegUtil.applyName(new SoundEvent(new ResourceLocation(MODID, "mud_step")));

    public static final SoundEvent NIGHTSTAG_AMBIENT = RegUtil.applyName(new SoundEvent(new ResourceLocation(MODID, "nightstag_ambient")));
    public static final SoundEvent NIGHTSTAG_HURT = RegUtil.applyName(new SoundEvent(new ResourceLocation(MODID, "nightstag_hurt")));
    public static final SoundEvent NIGHTSTAG_DEATH = RegUtil.applyName(new SoundEvent(new ResourceLocation(MODID, "nightstag_death")));

    public static final SoundEvent CRYSTAL_BUG_FLYING = RegUtil.applyName(new SoundEvent(new ResourceLocation(MODID, "crystal_bug_flying")));
    public static final SoundEvent CRYSTAL_BUG_HURT = RegUtil.applyName(new SoundEvent(new ResourceLocation(MODID, "crystal_bug_hurt")));
    public static final SoundEvent CRYSTAL_BUG_DEATH = RegUtil.applyName(new SoundEvent(new ResourceLocation(MODID, "crystal_bug_death")));

    public static final SoundEvent EGG_CRACKED = RegUtil.applyName(new SoundEvent(new ResourceLocation(MODID, "egg_cracked")));

    public static final SoundType MUD = new SoundType(1.0F, 1.0F, ModSounds.MUD_DIG, ModSounds.MUD_STEP, ModSounds.MUD_DIG, ModSounds.MUD_DIG, ModSounds.MUD_STEP);
    public static final SoundType PILE_OF_EGGS = new SoundType(1.0F, 1.0F, ModSounds.EGG_CRACKED, SoundEvents.BLOCK_STONE_STEP, SoundEvents.BLOCK_STONE_PLACE, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL);

    @SubscribeEvent
    public static void onRegisterSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                AMBIENT,
                IDLE,
                CAVE_IDLE,
                MUSIC_GENERIC,
                MUSIC_CRYSTAL,
                RIFT_IDLE,
                RIFT_UNSTABLE,
                RIFTER_AMBIENT,
                RIFTER_HURT,
                RIFTER_DEATH,
                BLADESHROOM_CAP_SHOOT,
                BLADESHROOM_CAP_HIT,
                MUD_DIG,
                MUD_STEP,
                NIGHTSTAG_AMBIENT,
                NIGHTSTAG_HURT,
                NIGHTSTAG_DEATH,
                CRYSTAL_BUG_FLYING,
                CRYSTAL_BUG_HURT,
                CRYSTAL_BUG_DEATH,
                EGG_CRACKED
        );
    }
}
