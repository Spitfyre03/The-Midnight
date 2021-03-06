package com.mushroom.midnight.common.block;

import com.mushroom.midnight.client.particle.MidnightParticles;
import com.mushroom.midnight.common.registry.ModEffects;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockDragonNest extends BlockMidnightPlant {
    private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.2, 0.2, 0.2, 0.8, 1.0, 0.8);

    public BlockDragonNest() {
        super(true);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDS;
    }

    @Override
    public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (entity instanceof EntityLivingBase && !entity.world.isRemote && entity.ticksExisted % 20 == 0) {
            ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(ModEffects.DRAGON_GUARD, 100, 0, false, true));
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return canBlockStay(world, pos, state) && state.getBlock().isReplaceable(world, pos);
    }

    @Override
    public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
        return world.getBlockState(pos.up()).isBlockNormalCube();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        super.randomDisplayTick(state, world, pos, rand);
        if (rand.nextInt(10) == 0) {
            Vec3d offset = getOffset(state, world, pos);
            double distX = rand.nextFloat() * 0.6 - 0.3d;
            double posX = pos.getX() + 0.5d + offset.x + distX;
            double posY = pos.getY() + offset.y + Math.abs(distX);
            double posZ = pos.getZ() + 0.5d + offset.z + (rand.nextBoolean() ? distX : -distX);
            MidnightParticles.DRIP.spawn(world, posX, posY, posZ, 0d, 0d, 0d,191, 70, 82);
        }
    }
}
