package com.mushroom.midnight.common.entity.creature;

import com.google.common.collect.ImmutableList;
import com.mushroom.midnight.Midnight;
import com.mushroom.midnight.common.capability.RifterCapturable;
import com.mushroom.midnight.common.entity.EntityRift;
import com.mushroom.midnight.common.entity.IRiftTraveler;
import com.mushroom.midnight.common.entity.RiftTravelEntry;
import com.mushroom.midnight.common.entity.RiftVoidTravelEntry;
import com.mushroom.midnight.common.entity.TargetIdleTracker;
import com.mushroom.midnight.common.entity.navigation.CustomPathNavigateGround;
import com.mushroom.midnight.common.entity.task.EntityTaskRifterCapture;
import com.mushroom.midnight.common.entity.task.EntityTaskRifterKeepNearRift;
import com.mushroom.midnight.common.entity.task.EntityTaskRifterMelee;
import com.mushroom.midnight.common.entity.task.EntityTaskRifterReturn;
import com.mushroom.midnight.common.entity.task.EntityTaskRifterTeleport;
import com.mushroom.midnight.common.entity.task.EntityTaskRifterTransport;
import com.mushroom.midnight.common.entity.util.DragSolver;
import com.mushroom.midnight.common.entity.util.EntityReference;
import com.mushroom.midnight.common.event.RifterCaptureEvent;
import com.mushroom.midnight.common.event.RifterReleaseEvent;
import com.mushroom.midnight.common.helper.Helper;
import com.mushroom.midnight.common.network.MessageCaptureEntity;
import com.mushroom.midnight.common.registry.ModEffects;
import com.mushroom.midnight.common.registry.ModLootTables;
import com.mushroom.midnight.common.registry.ModSounds;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class EntityRifter extends EntityMob implements IRiftTraveler, IEntityAdditionalSpawnData {
    public static final float HOME_SCALE_MODIFIER = 1.4F;

    private static final UUID SPEED_MODIFIER_ID = UUID.fromString("3b8cda1f-c11d-478b-98b1-6144940c7ba1");
    private static final AttributeModifier HOME_SPEED_MODIFIER = new AttributeModifier(SPEED_MODIFIER_ID, "home_speed_modifier", 0.15, 2);

    private static final UUID ARMOR_MODIFIER_ID = UUID.fromString("8cea53c5-1b5c-4b7c-9c86-192bf255c3d4");
    private static final AttributeModifier HOME_ARMOR_MODIFIER = new AttributeModifier(ARMOR_MODIFIER_ID, "home_armor_modifier", 2.0, 2);

    private static final UUID ATTACK_MODIFIER_ID = UUID.fromString("0e13d84c-52ed-4335-a284-49596533f445");
    private static final AttributeModifier HOME_ATTACK_MODIFIER = new AttributeModifier(ATTACK_MODIFIER_ID, "home_attack_modifier", 3.0, 2);

    public static final int CAPTURE_COOLDOWN = 15;

    private static final double RIFT_SEARCH_RADIUS = 48.0;
    private static final float DROP_DAMAGE_THRESHOLD = 2.0F;

    private final EntityReference<EntityRift> homeRift;
    private final DragSolver dragSolver;

    private final TargetIdleTracker targetIdleTracker = new TargetIdleTracker(this, 3.0);

    public int captureCooldown;

    public boolean spawnedThroughRift;

    private EntityLivingBase capturedEntity;

    public EntityRifter(World world) {
        super(world);
        this.homeRift = new EntityReference<>(world);
        this.dragSolver = new DragSolver(this);

        float scaleModifier = Helper.isMidnightDimension(world) ? HOME_SCALE_MODIFIER : 1.0F;
        this.setSize(0.6F * scaleModifier, 1.8F * scaleModifier);
        stepHeight = 1f;
    }

    @Override
    protected PathNavigate createNavigator(World world) {
        return new CustomPathNavigateGround(this, world);
    }

    @Override
    public boolean getCanSpawnHere() {
        return getPosition().getY() > world.getSeaLevel() && super.getCanSpawnHere();
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();

        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(0, new EntityTaskRifterReturn(this, 1.3));

        this.tasks.addTask(1, new EntityTaskRifterKeepNearRift(this, 1.0));
        this.tasks.addTask(1, new EntityTaskRifterTeleport(this));

        this.tasks.addTask(2, new EntityTaskRifterTransport(this, 1.0));
        this.tasks.addTask(3, new EntityTaskRifterCapture(this, 1.0));
        this.tasks.addTask(4, new EntityTaskRifterMelee(this, 1.0));

        this.tasks.addTask(4, new EntityAIOpenDoor(this, false));

        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityLivingBase.class, 8.0F));
        this.tasks.addTask(4, new EntityAILookIdle(this));

        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 0.4, 0.005F));

        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, 2, true, false, this::shouldAttack));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityAnimal.class, 4, true, false, this::shouldAttack));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0);
    }

    @Override
    public void onLivingUpdate() {
        if (!this.world.isRemote) {
            this.targetIdleTracker.update();

            this.applyHomeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, HOME_SPEED_MODIFIER);
            this.applyHomeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, HOME_ATTACK_MODIFIER);
            this.applyHomeModifier(SharedMonsterAttributes.ARMOR, HOME_ARMOR_MODIFIER);

            if (this.capturedEntity != null && !this.capturedEntity.isEntityAlive()) {
                this.setCapturedEntity(null);
            }

            if (this.captureCooldown > 0) {
                this.captureCooldown--;
            }

            if (!Helper.isMidnightDimension(this.world)) {
                this.updateHomeRift();
                if (this.ticksExisted % 20 == 0 && !this.homeRift.isPresent()) {
                    this.attackEntityFrom(DamageSource.OUT_OF_WORLD, 2.0F);
                }
            }
        }

        if (!this.world.isRemote || Midnight.proxy.isClientPlayer(this.capturedEntity)) {
            this.dragSolver.solveDrag();
        }

        super.onLivingUpdate();
    }

    public int getTargetIdleTime() {
        return this.targetIdleTracker.getIdleTime();
    }

    public boolean shouldCapture() {
        if (Helper.isMidnightDimension(this.world)) {
            return false;
        }
        return this.homeRift.isPresent();
    }

    private void applyHomeModifier(IAttribute attribute, AttributeModifier modifier) {
        IAttributeInstance instance = this.getEntityAttribute(attribute);
        boolean home = Helper.isMidnightDimension(this.world);
        if (home != instance.hasModifier(modifier)) {
            if (home) {
                instance.applyModifier(modifier);
            } else {
                instance.removeModifier(modifier);
            }
        }
    }

    private void updateHomeRift() {
        if (this.ticksExisted % 20 == 0 && !this.homeRift.isPresent()) {
            AxisAlignedBB searchBounds = this.getEntityBoundingBox().grow(RIFT_SEARCH_RADIUS);
            List<EntityRift> rifts = this.world.getEntitiesWithinAABB(EntityRift.class, searchBounds);
            if (!rifts.isEmpty()) {
                rifts.sort(Comparator.comparingDouble(this::getDistanceSq));
                this.homeRift.set(rifts.get(0));
            }
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        Entity trueSource = source.getTrueSource();

        if (super.attackEntityFrom(source, amount)) {
            if (trueSource instanceof EntityLivingBase && this.shouldAttack(trueSource)) {
                if (this.shouldChangeTarget(this.getAttackTarget(), (EntityLivingBase) trueSource)) {
                    this.setAttackTarget((EntityLivingBase) trueSource);
                }
            }

            if (amount > DROP_DAMAGE_THRESHOLD) {
                this.setCapturedEntity(null);
            }

            return true;
        }

        return false;
    }

    @Override
    protected float getSoundVolume() {
        return 1.5F;
    }

    private boolean shouldAttack(Entity entity) {
        if (entity == null || RifterCapturable.isCaptured(entity)) {
            return false;
        }
        if (entity instanceof EntityAnimal) {
            return !Helper.isMidnightDimension(entity.world);
        }
        if (entity instanceof EntityPlayer && ((EntityPlayer) entity).isPlayerSleeping()) {
            return false;
        }
        return !(entity instanceof EntityRifter);
    }

    private boolean shouldChangeTarget(@Nullable EntityLivingBase from, EntityLivingBase to) {
        if (from == null) {
            return true;
        }
        if (to instanceof EntityPlayer && !(from instanceof EntityPlayer)) {
            return true;
        }
        return to.getHealth() > from.getHealth();
    }

    public void setCapturedEntity(@Nullable EntityLivingBase capturedEntity) {
        this.captureCooldown = CAPTURE_COOLDOWN;

        if (MinecraftForge.EVENT_BUS.post(new RifterReleaseEvent(this, this.capturedEntity))) {
            return;
        }

        if (this.capturedEntity != null) {
            this.resetCapturedEntity(this.capturedEntity);
        }

        if (MinecraftForge.EVENT_BUS.post(new RifterCaptureEvent(this, capturedEntity))) {
            return;
        }

        this.capturedEntity = capturedEntity;
        this.dragSolver.setDragged(capturedEntity);

        if (capturedEntity != null) {
            this.initCapturedEntity(capturedEntity);
        }

        if (!this.world.isRemote) {
            MessageCaptureEntity message = new MessageCaptureEntity(this, capturedEntity);
            Midnight.NETWORK.sendToAllTracking(message, this);
        }
    }

    private void initCapturedEntity(EntityLivingBase capturedEntity) {
        RifterCapturable capability = capturedEntity.getCapability(Midnight.RIFTER_CAPTURABLE_CAP, null);
        if (capability != null) {
            capability.setCaptured(true);
        }
        capturedEntity.addPotionEffect(new PotionEffect(ModEffects.STUNNED, 200, 1, false, false));
        capturedEntity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 400, 2, false, false));
    }

    private void resetCapturedEntity(EntityLivingBase capturedEntity) {
        RifterCapturable capability = capturedEntity.getCapability(Midnight.RIFTER_CAPTURABLE_CAP, null);
        if (capability != null) {
            capability.setCaptured(false);
        }
    }

    public EntityLivingBase getCapturedEntity() {
        return this.capturedEntity;
    }

    public boolean hasCaptured() {
        return this.capturedEntity != null;
    }

    public EntityReference<EntityRift> getHomeRift() {
        return this.homeRift;
    }

    public DragSolver getDragSolver() {
        return this.dragSolver;
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.RIFTER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.RIFTER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.RIFTER_DEATH;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setTag("home_rift", this.homeRift.serialize(new NBTTagCompound()));
        compound.setBoolean("spawned_through_rift", this.spawnedThroughRift);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.homeRift.deserialize(compound.getCompoundTag("home_rift"));
        this.spawnedThroughRift = compound.getBoolean("spawned_through_rift");
    }

    @Override
    public void onEnterRift(EntityRift rift) {
        if (this.capturedEntity != null) {
            this.setCapturedEntity(null);
        }
    }

    @Override
    public RiftTravelEntry createTravelEntry(EntityRift rift) {
        return this.createTravelEntry(this, rift);
    }

    @Override
    public Collection<RiftTravelEntry> getAdditionalTravelers(EntityRift rift) {
        if (this.capturedEntity != null) {
            return ImmutableList.of(this.createTravelEntry(this.capturedEntity, rift));
        }
        return Collections.emptyList();
    }

    private RiftTravelEntry createTravelEntry(Entity entity, EntityRift rift) {
        if (this.shouldTravelDespawn(rift)) {
            return new RiftVoidTravelEntry(entity);
        }
        return new RiftTravelEntry(entity);
    }

    private boolean shouldTravelDespawn(EntityRift rift) {
        return this.spawnedThroughRift && !rift.isEndpointLoaded() && !(this.capturedEntity instanceof EntityPlayer);
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeInt(this.capturedEntity != null ? this.capturedEntity.getEntityId() : -1);
    }

    @Override
    public void readSpawnData(ByteBuf buffer) {
        int capturedId = buffer.readInt();
        if (capturedId != -1) {
            Entity entity = this.world.getEntityByID(capturedId);
            if (entity instanceof EntityLivingBase) {
                this.setCapturedEntity((EntityLivingBase) entity);
            }
        }
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return ModLootTables.LOOT_TABLE_RIFTER;
    }
}
