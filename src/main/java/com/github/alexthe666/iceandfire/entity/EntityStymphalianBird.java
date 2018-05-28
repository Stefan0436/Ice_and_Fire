package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.event.EventLiving;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class EntityStymphalianBird extends EntityCreature implements IAnimatedEntity {

    private static final int FLIGHT_CHANCE_PER_TICK = 100;
    private int animationTick;
    private Animation currentAnimation;
    private static final DataParameter<Integer> VICTOR_ENTITY = EntityDataManager.<Integer>createKey(EntityStymphalianBird.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> FLYING = EntityDataManager.<Boolean>createKey(EntityDragonBase.class, DataSerializers.BOOLEAN);
    private EntityLivingBase victorEntity;
    private boolean isFlying;
    public float flyProgress;
    public BlockPos airTarget;
    private int flyTicks;
    private int launchTicks;
    public static Animation ANIMATION_PECK = Animation.create(20);
    public static Animation ANIMATION_SHOOT_ARROWS = Animation.create(30);
    public static Animation ANIMATION_SPEAK = Animation.create(10);
    public StymphalianBirdFlock flock;
    private boolean aiFlightLaunch = false;
    protected static final Predicate<Entity> STYMPHALIAN_PREDICATE = new Predicate<Entity>() {
        public boolean apply(@Nullable Entity entity) {
            return entity instanceof EntityStymphalianBird;
        }
    };
    private int airBorneCounter;

    public EntityStymphalianBird(World worldIn) {
        super(worldIn);
        this.setSize(1.3F, 1.2F);
    }

    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new StymphalianBirdAIFlee(this, 10));
        this.tasks.addTask(3, new EntityAIAttackMelee(this, 1.5D, false));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new StymphalianBirdAIAirTarget(this));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityLivingBase.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(2, new StymphalianBirdAITarget(this, EntityLivingBase.class, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(Math.min(2048, IceAndFire.CONFIG.stymphalianBirdTargetSearchLength));
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0D);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VICTOR_ENTITY, Integer.valueOf(0));
        this.dataManager.register(FLYING, false);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setInteger("VictorEntity", this.dataManager.get(VICTOR_ENTITY).intValue());
        tag.setBoolean("Flying", this.isFlying());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.setVictorEntity(tag.getInteger("VictorEntity"));
        this.setFlying(tag.getBoolean("Flying"));
    }

    public boolean isFlying() {
        if (world.isRemote) {
            return this.isFlying = this.dataManager.get(FLYING);
        }
        return isFlying;
    }

    public void setFlying(boolean flying) {
        this.dataManager.set(FLYING, flying);
        if (!world.isRemote) {
            this.isFlying = flying;
        }
    }

    public void onDeath(DamageSource cause) {
        if (cause.getTrueSource() != null && cause.getTrueSource() instanceof EntityLivingBase && !world.isRemote) {
            this.setVictorEntity(cause.getTrueSource().getEntityId());
            this.signalVictor();
        }
    }

    private void signalVictor() {
        float d0 = IceAndFire.CONFIG.stymphalianBirdFlockLength;
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, (new AxisAlignedBB(this.posX, this.posY, this.posZ, this.posX + 1.0D, this.posY + 1.0D, this.posZ + 1.0D)).grow(d0, 10.0D, d0), STYMPHALIAN_PREDICATE);
        Collections.sort(list, new EntityAINearestAttackableTarget.Sorter(this));
        if (!list.isEmpty()) {
            Iterator<Entity> itr = list.iterator();
            while (itr.hasNext()) {
                Entity entity = itr.next();
                if(entity instanceof EntityStymphalianBird){
                    EntityStymphalianBird bird = (EntityStymphalianBird)entity;
                    if (this.hasVictorEntity() && this.getVictorEntity() != null) {
                        bird.setVictorEntity(this.getVictorEntity().getEntityId());
                    }
                }

            }
        }
    }

    protected void onDeathUpdate() {
        super.onDeathUpdate();
        if (this.deathTime == 20 && !this.world.isRemote) {
            dropItemAt(new ItemStack(Items.IRON_INGOT, 1 + this.getRNG().nextInt(3)), this.posX, this.posY + 0.5F, this.posZ);
            dropItemAt(new ItemStack(Items.IRON_NUGGET, 1 + this.getRNG().nextInt(3)), this.posX, this.posY + 0.5F, this.posZ);
            dropItemAt(new ItemStack(ModItems.stymphalian_bird_feather, 2 + this.getRNG().nextInt(4)), this.posX, this.posY + 0.5F, this.posZ);

            NonNullList<ItemStack> bronzeItems = OreDictionary.getOres("ingotBronze");
            NonNullList<ItemStack> copperItems = OreDictionary.getOres("ingotCopper");
            if (!bronzeItems.isEmpty()) {
                for (ItemStack bronzeIngot : bronzeItems) {
                    if (bronzeIngot != ItemStack.EMPTY) {
                        ItemStack stack = bronzeIngot.copy();
                        stack.setCount(1 + this.getRNG().nextInt(3));
                        dropItemAt(stack, this.posX, this.posY + 0.5F, this.posZ);
                        break;
                    }
                }
            }
            if (!copperItems.isEmpty()) {
                for (ItemStack copperIngot : copperItems) {
                    if (copperIngot != ItemStack.EMPTY) {
                        ItemStack stack = copperIngot.copy();
                        stack.setCount(1 + this.getRNG().nextInt(3));
                        dropItemAt(stack, this.posX, this.posY + 0.5F, this.posZ);
                        break;
                    }
                }
            }
        }
    }

    @Nullable
    private EntityItem dropItemAt(ItemStack stack, double x, double y, double z) {
        EntityItem entityitem = new EntityItem(this.world, x, y, z, stack);
        entityitem.setDefaultPickupDelay();
        if (captureDrops)
            this.capturedDrops.add(entityitem);
        else
            this.world.spawnEntity(entityitem);
        return entityitem;
    }

    public void setVictorEntity(int entityId) {
        this.dataManager.set(VICTOR_ENTITY, Integer.valueOf(entityId));
    }

    public boolean hasVictorEntity() {
        return ((Integer) this.dataManager.get(VICTOR_ENTITY)).intValue() != 0;
    }

    @Nullable
    public Entity getVictorEntity() {
        if (!this.hasVictorEntity()) {
            return null;
        } else if (this.world.isRemote) {
            if (this.victorEntity != null) {
                return this.victorEntity;
            } else {
                Entity entity = this.world.getEntityByID(((Integer) this.dataManager.get(VICTOR_ENTITY)).intValue());
                if (entity instanceof EntityLivingBase) {
                    this.victorEntity = (EntityLivingBase) entity;
                    return this.victorEntity;
                } else {
                    return null;
                }
            }
        } else {
            return this.world.getEntityByID(((Integer) this.dataManager.get(VICTOR_ENTITY)).intValue());
        }
    }

    public boolean isTargetBlocked(Vec3d target) {
        if (target != null) {
            RayTraceResult rayTrace = world.rayTraceBlocks(new Vec3d(this.getPosition()), target, false);
            if (rayTrace != null && rayTrace.hitVec != null) {
                BlockPos pos = new BlockPos(rayTrace.hitVec);
                if (!world.isAirBlock(pos)) {
                    return true;
                }
                return rayTrace != null && rayTrace.typeOfHit != RayTraceResult.Type.BLOCK;
            }
        }
        return false;
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_PECK);
        }
        return true;
    }


    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(this.flock == null){
            StymphalianBirdFlock otherFlock = StymphalianBirdFlock.getNearbyFlock(this);
            if(otherFlock == null){
                this.flock = StymphalianBirdFlock.createFlock(this);
            }else{
                this.flock = otherFlock;
                this.flock.addToFlock(this);
            }
        }else{
            if(!this.flock.isLeader(this)){
                double dist = this.getDistanceSq(this.flock.getLeader());
                if(dist > 360){
                    this.setFlying(true);
                    this.navigator.clearPath();
                    this.airTarget = StymphalianBirdAIAirTarget.getNearbyAirTarget(this.flock.getLeader());
                    this.aiFlightLaunch = false;
                }else if(!this.flock.getLeader().isFlying()){
                    this.setFlying(false);
                    this.airTarget = null;
                    this.aiFlightLaunch = false;
                }
                if(this.onGround && dist < 40 && this.getAnimation() != ANIMATION_SHOOT_ARROWS){
                    this.setFlying(false);
                }
            }
            this.flock.update();
        }
        if (!world.isRemote && this.getAttackTarget() != null) {
            double dist = this.getDistanceSq(this.getAttackTarget());
            if (this.getAnimation() == ANIMATION_PECK && this.getAnimationTick() == 7) {
                if (dist < 1.5F) {
                    this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
                }
                if (onGround) {
                    this.setFlying(false);
                }
            }
            if (this.getAnimation() != ANIMATION_PECK && this.getAnimation() != ANIMATION_SHOOT_ARROWS && dist > 3 && dist < 225) {
                this.setAnimation(ANIMATION_SHOOT_ARROWS);
            }
            if (this.getAnimation() == ANIMATION_SHOOT_ARROWS) {
                EntityLivingBase target = this.getAttackTarget();
                this.faceEntity(target, 360, 360);
                if (this.isFlying()) {
                    rotationYaw = renderYawOffset;
                    if ((this.getAnimationTick() == 7 || this.getAnimationTick() == 14) && isDirectPathBetweenPoints(this, this.getPositionVector(), target.getPositionVector())) {
                        for (int i = 0; i < 4; i++) {
                            float wingX = (float) (posX + 1.8F * 0.5F * Math.cos((rotationYaw + 180 * (i % 2)) * Math.PI / 180));
                            float wingZ = (float) (posZ + 1.8F * 0.5F * Math.sin((rotationYaw + 180 * (i % 2)) * Math.PI / 180));
                            float wingY = (float) (posY + 1F);
                            double d0 = target.posX - wingX;
                            double d1 = target.getEntityBoundingBox().minY - wingY;
                            double d2 = target.posZ - wingZ;
                            double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
                            EntityStymphalianFeather entityarrow = new EntityStymphalianFeather(world, this);
                            entityarrow.setPosition(wingX, wingY, wingZ);
                            entityarrow.shoot(d0, d1 + d3 * 0.10000000298023224D, d2, 1.6F, (float) (14 - this.world.getDifficulty().getDifficultyId() * 4));
                            this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
                            this.world.spawnEntity(entityarrow);
                        }
                    }
                } else {
                    this.setFlying(true);
                }
            }
        }
        StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(this, StoneEntityProperties.class);
        boolean flying = this.isFlying() && !this.onGround || airBorneCounter > 10 || this.getAnimation() == ANIMATION_SHOOT_ARROWS;
        if (flying && flyProgress < 20.0F) {
            flyProgress += 1F;
        } else if (!flying && flyProgress > 0.0F) {
            flyProgress -= 1F;
        }
        if (!this.isFlying() && this.airTarget != null && this.onGround && !world.isRemote) {
            this.airTarget = null;
        }
        if (this.isFlying() && getAttackTarget() == null) {
            flyAround();
        } else if (getAttackTarget() != null) {
            flyTowardsTarget();
        }
        if(!world.isRemote && this.doesWantToLand() && !aiFlightLaunch && this.getAnimation() != ANIMATION_SHOOT_ARROWS){
            this.setFlying(false);
            this.airTarget = null;

        }
        if(!world.isRemote && this.onGround && this.isFlying() && !aiFlightLaunch && this.getAnimation() != ANIMATION_SHOOT_ARROWS){
            this.setFlying(false);
            this.airTarget = null;
        }
        if ((properties == null || properties != null && !properties.isStone) && !world.isRemote && (this.flock == null || this.flock != null && this.flock.isLeader(this)) && this.getRNG().nextInt(FLIGHT_CHANCE_PER_TICK) == 0 && !this.isFlying() && this.getPassengers().isEmpty() && !this.isChild() && this.onGround) {
            this.setFlying(true);
            this.launchTicks = 0;
            this.flyTicks = 0;
            this.aiFlightLaunch = true;
        }
        if(!world.isRemote) {
            if (aiFlightLaunch && this.launchTicks < 40) {
                this.launchTicks++;
            } else {
                this.launchTicks = 0;
                aiFlightLaunch = false;
            }
            if(this.isFlying()){
                this.flyTicks++;
            }else{
                this.flyTicks = 0;
            }
        }
        if (!this.onGround) {
            airBorneCounter++;
        } else {
            airBorneCounter = 0;
        }
        if(this.getAnimation() == ANIMATION_SHOOT_ARROWS && !this.isFlying() && !world.isRemote){
            this.setFlying(true);
            aiFlightLaunch = true;
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    public boolean isDirectPathBetweenPoints(Entity entity, Vec3d vec1, Vec3d vec2) {
        RayTraceResult movingobjectposition = entity.world.rayTraceBlocks(vec1, new Vec3d(vec2.x, vec2.y + (double) entity.height * 0.5D, vec2.z), false, true, false);
        return movingobjectposition == null || movingobjectposition.typeOfHit != RayTraceResult.Type.BLOCK;
    }

    private boolean isLeaderNotFlying(){
        return this.flock != null && this.flock.getLeader() != null && !this.flock.getLeader().isFlying();
    }
    public void flyAround() {
        if (airTarget != null && this.isFlying()) {
            if (!isTargetInAir() || flyTicks > 6000 || !this.isFlying()) {
                airTarget = null;
            }
            flyTowardsTarget();
        }
    }

    public void flyTowardsTarget() {
        if (airTarget != null && isTargetInAir() && this.isFlying() && this.getDistanceSquared(new Vec3d(airTarget.getX(), this.posY, airTarget.getZ())) > 3) {
            double targetX = airTarget.getX() + 0.5D - posX;
            double targetY = Math.min(airTarget.getY(), 256) + 1D - posY;
            double targetZ = airTarget.getZ() + 0.5D - posZ;
            motionX += (Math.signum(targetX) * 0.5D - motionX) * 0.100000000372529 * getFlySpeed(false);
            motionY += (Math.signum(targetY) * 0.5D - motionY) * 0.100000000372529 * getFlySpeed(true);
            motionZ += (Math.signum(targetZ) * 0.5D - motionZ) * 0.100000000372529 * getFlySpeed(false);
            float angle = (float) (Math.atan2(motionZ, motionX) * 180.0D / Math.PI) - 90.0F;
            float rotation = MathHelper.wrapDegrees(angle - rotationYaw);
            moveForward = 0.5F;
            prevRotationYaw = rotationYaw;
            rotationYaw += rotation;
            if (!this.isFlying()) {
                this.setFlying(true);
            }
        } else {
            this.airTarget = null;
        }
        if (airTarget != null && isTargetInAir() && this.isFlying() && this.getDistanceSquared(new Vec3d(airTarget.getX(), this.posY, airTarget.getZ())) < 3 && this.doesWantToLand()) {
            this.setFlying(false);
        }
    }

    private float getFlySpeed(boolean y){
        float speed = 2;
        if(this.flock != null && !this.flock.isLeader(this) && this.getDistanceSq(this.flock.getLeader()) > 10){
            speed = 4;
        }
        if(this.getAnimation() == ANIMATION_SHOOT_ARROWS && !y){
            speed *= 0.05;
        }
        return speed;
    }

    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    public void setAttackTarget(EntityLivingBase entity) {
        super.setAttackTarget(entity);
        if(this.flock != null && this.flock.isLeader(this) && entity != null){
            this.flock.onLeaderAttack(entity);
        }
    }

    public float getDistanceSquared(Vec3d vec3d) {
        float f = (float) (this.posX - vec3d.x);
        float f1 = (float) (this.posY - vec3d.y);
        float f2 = (float) (this.posZ - vec3d.z);
        return f * f + f1 * f1 + f2 * f2;
    }

    protected boolean isTargetInAir() {
        return airTarget != null && ((world.getBlockState(airTarget).getMaterial() == Material.AIR) || world.getBlockState(airTarget).getMaterial() == Material.AIR);
    }

    public boolean doesWantToLand() {
        if(this.flock != null){
            if(!this.flock.isLeader(this) && this.flock.getLeader() != null){
                return this.flock.getLeader().doesWantToLand();
            }
        }
        return this.flyTicks > 500 || flyTicks > 40 && this.flyProgress == 0;
    }

    @Override
    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, ANIMATION_PECK, ANIMATION_SHOOT_ARROWS, ANIMATION_SPEAK};
    }
}