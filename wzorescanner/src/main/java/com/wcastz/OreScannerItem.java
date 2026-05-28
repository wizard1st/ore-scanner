package com.wcastz;

import org.jspecify.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;



public class OreScannerItem extends Item{

    private final Block[] targetOres;
 
    public OreScannerItem(Item.Properties settings,@Nullable Block... targetOres){
        super(settings);
        this.targetOres = targetOres;
        
    }
    // ระบบสแกนหาแร่แบบ Passive เมื่อถือและกดย่อตัว

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        if (slot != EquipmentSlot.MAINHAND) return;
        if (level.getGameTime() % Math.round(level.tickRateManager().tickrate()) != 0) return;
        if (targetOres == null || targetOres.length == 0) return;
        
        if(!level.isClientSide()){
            if (owner instanceof Player player && player.isCrouching()) {
        
        // --- ส่วนที่ปรับปรุง: ยิงสายตาหาบล็อกที่เมาส์ชี้ฝั่ง Server ---
        // ระยะสายตา 5.0 บล็อก (สามารถปรับเลขได้ตามต้องการ)
        double reachDistance = 5.0D;
        net.minecraft.world.phys.Vec3 eyePosition = player.getEyePosition();
        net.minecraft.world.phys.Vec3 lookVector = player.getViewVector(1.0F);
        net.minecraft.world.phys.Vec3 traceEnd = eyePosition.add(lookVector.x * reachDistance, lookVector.y * reachDistance, lookVector.z * reachDistance);

        // ทำการ Raycast บล็อกที่สายตาตัดผ่าน
        net.minecraft.world.level.ClipContext context = new net.minecraft.world.level.ClipContext(
            eyePosition, 
            traceEnd, 
            net.minecraft.world.level.ClipContext.Block.OUTLINE, 
            net.minecraft.world.level.ClipContext.Fluid.NONE, 
            player
        );
        
        net.minecraft.world.phys.BlockHitResult hitResult = level.clip(context);

        // เช็กว่าเมาส์ชี้โดนบล็อกจริงๆ ไหม
        if (hitResult.getType() == net.minecraft.world.phys.HitResult.Type.BLOCK) {
            // ดึงพิกัดของบล็อกที่เมาส์ชี้มาเป็นจุดศูนย์กลางในการสแกนดิ่งลงล่าง
            BlockPos targetedPos = hitResult.getBlockPos();
            
            boolean foundOre = false;
            BlockPos orePos = null;

            // เริ่มสแกนจากบล็อกที่เมาส์ชี้ (หรือใต้บล็อกนั้น 1 บล็อก) ดิ่งลงไปจนถึงก้นโลก (-64)
            int startY = targetedPos.getY();
            int minY = level.getMinY();

            for (int y = startY; y >= minY; y--) {
                // ล็อคพิกัด X, Z ตามจุดที่เมาส์ชี้ แล้วเปลี่ยนแค่ค่า Y ดิ่งลงไป
                BlockPos checkPos = new BlockPos(targetedPos.getX(), y, targetedPos.getZ());
                BlockState state = level.getBlockState(checkPos);

                if (isTargetBlock(state.getBlock())) {
                    foundOre = true;
                    orePos = checkPos;
                    break;
                }
            }

            // 3. ถ้าเจอแร่ใต้บล็อกที่เมาส์ชี้
            if (foundOre && orePos != null) {
                // เล่นเสียงที่ตำแหน่งบล็อกที่เมาส์ชี้
                level.playSound(null, targetedPos.getX() + 0.5, targetedPos.getY() + 0.5, targetedPos.getZ() + 0.5, 
                    SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.6F, 1.2F);

                // เสกพาร์ทิเคิลพุ่งกระจายขึ้นมาจากบล็อกบนพื้นที่ตาเรามองอยู่ ให้เห็นชัดๆ ว่าเจอแร่ใต้บล็อกนี้
                level.sendParticles(
                    ParticleTypes.ENCHANT,
                    targetedPos.getX() + 0.5, targetedPos.getY() + 1.1, targetedPos.getZ() + 0.5, 
                    15,   // จำนวนพาร์ทิเคิล
                    0.2, 0.1, 0.2, // ระยะกระจาย
                    0.1   // ความเร็ว
                );
            }
        }
    }
        }
    }
    private boolean isTargetBlock(Block block) {
        for (Block targetOre : this.targetOres) {
            if (block == targetOre) {
                return true;
            }
        }
        return false;
    }
   
}
