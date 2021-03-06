package com.mushroom.midnight.common.block;

import com.mushroom.midnight.client.IModelProvider;
import com.mushroom.midnight.common.registry.ModBlocks;
import com.mushroom.midnight.common.registry.ModFluids;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockDarkWater extends BlockMixableFluid implements IModelProvider {
    public BlockDarkWater() {
        super(ModFluids.DARK_WATER, Material.WATER);
        this.setLightOpacity(3);
    }

    @Override
    public Vec3d getFogColor(World world, BlockPos pos, IBlockState state, Entity entity, Vec3d originalColor, float partialTicks) {
        return new Vec3d(0.2, 0.1, 0.5);
    }

    @Nullable
    @Override
    protected IBlockState getMixState(IBlockState otherState) {
        if (otherState.getMaterial() == Material.LAVA) {
            return ModBlocks.TRENCHSTONE.getDefaultState();
        } else if (otherState.getBlock() != this && otherState.getMaterial() == Material.WATER) {
            return Blocks.AIR.getDefaultState();
        }
        return null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
        return MapColor.BLUE;
    }
}
