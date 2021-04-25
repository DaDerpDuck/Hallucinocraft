package com.daderpduck.hallucinocraft.items;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class JointItem extends DrugItem {
    public JointItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, World world, LivingEntity entity) {
        if (!world.isClientSide) {
            Vector3d lookVector = entity.getLookAngle();
            ((ServerWorld)world).sendParticles(ParticleTypes.SMOKE, entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ(), 0, lookVector.x, lookVector.y, lookVector.z, 0.1);
        }

        return super.finishUsingItem(itemStack, world, entity);
    }
}
