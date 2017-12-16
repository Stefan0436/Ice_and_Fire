package com.github.alexthe666.iceandfire.client.model;


import com.github.alexthe666.iceandfire.entity.EntityCyclops;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.enums.EnumHippogryphTypes;
import net.ilexiconn.llibrary.client.model.ModelAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;

public class ModelCyclops extends ModelDragonBase {
    public AdvancedModelRenderer body;
    public AdvancedModelRenderer UpperBody;
    public AdvancedModelRenderer Loin;
    public AdvancedModelRenderer rightleg;
    public AdvancedModelRenderer leftleg;
    public AdvancedModelRenderer Head;
    public AdvancedModelRenderer rightarm;
    public AdvancedModelRenderer leftarm;
    public AdvancedModelRenderer Belly;
    public AdvancedModelRenderer Chest;
    public AdvancedModelRenderer Eye;
    public AdvancedModelRenderer Horn;
    public AdvancedModelRenderer rightear;
    public AdvancedModelRenderer Leftear;
    public AdvancedModelRenderer Jaw;
    public AdvancedModelRenderer topTeethL;
    public AdvancedModelRenderer topTeethR;
    public AdvancedModelRenderer Eye_1;
    public AdvancedModelRenderer Horn2;
    public AdvancedModelRenderer bottomTeethR;
    public AdvancedModelRenderer bottomTeethL;
    public AdvancedModelRenderer rightarm2;
    public AdvancedModelRenderer leftarm2;
    public AdvancedModelRenderer LoinBack;
    public AdvancedModelRenderer rightleg2;
    public AdvancedModelRenderer leftleg2;
    private ModelAnimator animator;

    public ModelCyclops() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.rightear = new AdvancedModelRenderer(this, 0, 7);
        this.rightear.setRotationPoint(-4.5F, -2.7F, -1.1F);
        this.rightear.addBox(-1.0F, -2.7F, -1.3F, 1, 4, 2, 0.0F);
        this.setRotateAngle(rightear, -0.06981317007977318F, -0.5235987755982988F, 0.0F);
        this.Eye_1 = new AdvancedModelRenderer(this, 8, 6);
        this.Eye_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Eye_1.addBox(-1.5F, -1.5F, -4.6F, 3, 3, 1, 0.0F);
        this.Horn2 = new AdvancedModelRenderer(this, 17, 0);
        this.Horn2.setRotationPoint(0.0F, -5.0F, 0.5F);
        this.Horn2.addBox(-1.0F, -2.9F, -3.3F, 2, 3, 2, 0.0F);
        this.UpperBody = new AdvancedModelRenderer(this, 85, 21);
        this.UpperBody.setRotationPoint(0.0F, -6.0F, 0.5F);
        this.UpperBody.addBox(-6.0F, -13.9F, -4.1F, 12, 14, 9, 0.0F);
        this.topTeethL = new AdvancedModelRenderer(this, 90, 70);
        this.topTeethL.mirror = true;
        this.topTeethL.setRotationPoint(0.0F, 2.3F, 0.3F);
        this.topTeethL.addBox(-0.6F, -0.5F, -6.2F, 5, 1, 6, 0.0F);
        this.body = new AdvancedModelRenderer(this, 88, 46);
        this.body.setRotationPoint(0.0F, -3.5F, 0.0F);
        this.body.addBox(-5.0F, -7.0F, -3.0F, 10, 9, 8, 0.0F);
        this.Belly = new AdvancedModelRenderer(this, 35, 25);
        this.Belly.mirror = true;
        this.Belly.setRotationPoint(0.0F, -2.3F, -0.2F);
        this.Belly.addBox(-4.5F, -2.0F, -4.8F, 9, 13, 3, 0.0F);
        this.setRotateAngle(Belly, 0.045553093477052F, 0.0F, 0.0F);
        this.Loin = new AdvancedModelRenderer(this, 52, 49);
        this.Loin.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Loin.addBox(-5.5F, 0.0F, -4.1F, 11, 16, 5, 0.0F);
        this.leftleg2 = new AdvancedModelRenderer(this, 0, 15);
        this.leftleg2.mirror = true;
        this.leftleg2.setRotationPoint(0.0F, 10.0F, 0.2F);
        this.leftleg2.addBox(-3.0F, 1.0F, -3.0F, 6, 15, 6, 0.0F);
        this.setRotateAngle(leftleg2, 0.0F, 0.0F, 0.017453292519943295F);
        this.bottomTeethL = new AdvancedModelRenderer(this, 90, 70);
        this.bottomTeethL.mirror = true;
        this.bottomTeethL.setRotationPoint(0.0F, 2.3F, 0.3F);
        this.bottomTeethL.addBox(-0.2F, -3.7F, -6.6F, 5, 1, 6, 0.0F);
        this.LoinBack = new AdvancedModelRenderer(this, 49, 45);
        this.LoinBack.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.LoinBack.addBox(-5.5F, 0.0F, -5.6F, 11, 16, 8, 0.0F);
        this.setRotateAngle(LoinBack, 0.0F, -3.141592653589793F, 0.0F);
        this.Leftear = new AdvancedModelRenderer(this, 0, 7);
        this.Leftear.mirror = true;
        this.Leftear.setRotationPoint(4.5F, -2.7F, -1.1F);
        this.Leftear.addBox(0.0F, -2.7F, -1.3F, 1, 4, 2, 0.0F);
        this.setRotateAngle(Leftear, -0.06981317007977318F, 0.5235987755982988F, 0.0F);
        this.Chest = new AdvancedModelRenderer(this, 93, 30);
        this.Chest.mirror = true;
        this.Chest.setRotationPoint(0.0F, -9.7F, -0.1F);
        this.Chest.addBox(-5.0F, -2.0F, -4.8F, 10, 5, 2, 0.0F);
        this.leftarm = new AdvancedModelRenderer(this, 64, 0);
        this.leftarm.mirror = true;
        this.leftarm.setRotationPoint(5.0F, -11.2F, -0.4F);
        this.leftarm.addBox(0.0F, -2.0F, -2.0F, 6, 13, 6, 0.0F);
        this.setRotateAngle(leftarm, 0.045553093477052F, 0.0F, -0.17453292519943295F);
        this.Horn = new AdvancedModelRenderer(this, 29, 0);
        this.Horn.setRotationPoint(0.0F, -5.7F, -1.2F);
        this.Horn.addBox(-1.5F, -5.6F, -2.8F, 3, 3, 3, 0.0F);
        this.setRotateAngle(Horn, 0.4553564018453205F, 0.0F, 0.0F);
        this.Jaw = new AdvancedModelRenderer(this, 90, 80);
        this.Jaw.setRotationPoint(0.0F, 2.3F, 0.3F);
        this.Jaw.addBox(-5.0F, -0.5F, -6.6F, 10, 2, 9, 0.0F);
        this.setRotateAngle(Jaw, 0.091106186954104F, 0.0F, 0.0F);
        this.Eye = new AdvancedModelRenderer(this, 0, 0);
        this.Eye.setRotationPoint(0.0F, -5.1F, -2.3F);
        this.Eye.addBox(-2.5F, -2.0F, -4.4F, 5, 4, 1, 0.0F);
        this.setRotateAngle(Eye, 0.091106186954104F, 0.0F, 0.0F);
        this.rightarm2 = new AdvancedModelRenderer(this, 60, 22);
        this.rightarm2.setRotationPoint(-3.1F, 10.0F, 0.1F);
        this.rightarm2.addBox(-3.0F, -2.0F, -1.7F, 5, 15, 5, 0.0F);
        this.setRotateAngle(rightarm2, -0.08726646259971647F, 0.0F, 0.0F);
        this.leftleg = new AdvancedModelRenderer(this, 0, 45);
        this.leftleg.mirror = true;
        this.leftleg.setRotationPoint(4.0F, 1.2F, 1.0F);
        this.leftleg.addBox(-3.0F, 1.0F, -3.0F, 6, 13, 6, 0.0F);
        this.setRotateAngle(leftleg, 0.0F, 0.0F, -0.017453292519943295F);
        this.Head = new AdvancedModelRenderer(this, 90, 0);
        this.Head.setRotationPoint(0.0F, -16.1F, 0.6F);
        this.Head.addBox(-4.5F, -8.0F, -6.0F, 9, 10, 9, 0.0F);
        this.bottomTeethR = new AdvancedModelRenderer(this, 90, 70);
        this.bottomTeethR.setRotationPoint(0.0F, 2.3F, 0.3F);
        this.bottomTeethR.addBox(-4.6F, -3.7F, -6.6F, 5, 1, 6, 0.0F);
        this.topTeethR = new AdvancedModelRenderer(this, 90, 70);
        this.topTeethR.setRotationPoint(0.0F, 2.3F, 0.3F);
        this.topTeethR.addBox(-4.3F, -0.5F, -6.2F, 5, 1, 6, 0.0F);
        this.rightleg2 = new AdvancedModelRenderer(this, 0, 15);
        this.rightleg2.setRotationPoint(0.0F, 10.0F, 0.2F);
        this.rightleg2.addBox(-3.0F, 1.0F, -3.0F, 6, 15, 6, 0.0F);
        this.setRotateAngle(rightleg2, 0.0F, 0.0F, -0.017453292519943295F);
        this.leftarm2 = new AdvancedModelRenderer(this, 60, 22);
        this.leftarm2.mirror = true;
        this.leftarm2.setRotationPoint(3.1F, 10.0F, -0.1F);
        this.leftarm2.addBox(-2.0F, -2.0F, -1.7F, 5, 15, 5, 0.0F);
        this.setRotateAngle(leftarm2, -0.08726646259971647F, 0.0F, 0.0F);
        this.rightleg = new AdvancedModelRenderer(this, 0, 45);
        this.rightleg.setRotationPoint(-4.0F, 1.2F, 1.0F);
        this.rightleg.addBox(-3.0F, 1.0F, -3.0F, 6, 13, 6, 0.0F);
        this.setRotateAngle(rightleg, 0.0F, 0.0F, 0.017453292519943295F);
        this.rightarm = new AdvancedModelRenderer(this, 64, 0);
        this.rightarm.setRotationPoint(-5.0F, -11.2F, -0.4F);
        this.rightarm.addBox(-6.0F, -2.0F, -2.0F, 6, 13, 6, 0.0F);
        this.setRotateAngle(rightarm, 0.0F, 0.0F, 0.17453292519943295F);
        this.Head.addChild(this.rightear);
        this.Eye.addChild(this.Eye_1);
        this.Horn.addChild(this.Horn2);
        this.body.addChild(this.UpperBody);
        this.Head.addChild(this.topTeethL);
        this.UpperBody.addChild(this.Belly);
        this.body.addChild(this.Loin);
        this.leftleg.addChild(this.leftleg2);
        this.Jaw.addChild(this.bottomTeethL);
        this.Loin.addChild(this.LoinBack);
        this.Head.addChild(this.Leftear);
        this.UpperBody.addChild(this.Chest);
        this.UpperBody.addChild(this.leftarm);
        this.Head.addChild(this.Horn);
        this.Head.addChild(this.Jaw);
        this.Head.addChild(this.Eye);
        this.rightarm.addChild(this.rightarm2);
        this.body.addChild(this.leftleg);
        this.UpperBody.addChild(this.Head);
        this.Jaw.addChild(this.bottomTeethR);
        this.Head.addChild(this.topTeethR);
        this.rightleg.addChild(this.rightleg2);
        this.leftarm.addChild(this.leftarm2);
        this.body.addChild(this.rightleg);
        this.UpperBody.addChild(this.rightarm);
        animator = ModelAnimator.create();
        this.updateDefaultPose();
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        animate((IAnimatedEntity) entity, f, f1, f2, f3, f4, f5);
        this.body.render(f5);
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.resetToDefaultPose();
        setRotationAngles(f, f1, f2, f3, f4, f5, (EntityCyclops) entity);
        this.Loin.rotateAngleX = Math.min(0, Math.min(this.leftleg.rotateAngleX, this.rightleg.rotateAngleX));
        this.LoinBack.rotateAngleX = this.Loin.rotateAngleX - Math.max(this.leftleg.rotateAngleX, this.rightleg.rotateAngleX);
        animator.update(entity);
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, EntityCyclops entity) {
        float speed_walk = 0.2F;
        float speed_idle = 0.05F;
        float degree_walk = 0.75F;
        float degree_idle = 0.5F;
        this.walk(this.rightleg, speed_walk, degree_walk * -0.75F, true, 0, 0F, f, f1);
        this.walk(this.leftleg, speed_walk, degree_walk * -0.75F, false, 0, 0F, f, f1);
        this.walk(this.rightleg2, speed_walk, degree_walk * -0.5F, true, 1, -0.3F, f, f1);
        this.walk(this.leftleg2, speed_walk, degree_walk * -0.5F, false, 1, 0.3F, f, f1);
        this.walk(this.rightarm, speed_walk, degree_walk * -0.75F, false, 0, 0F, f, f1);
        this.walk(this.leftarm, speed_walk, degree_walk * -0.75F, true, 0, 0F, f, f1);
        this.walk(this.rightarm2, speed_walk, degree_walk * -0.5F, false, 1, -0.3F, f, f1);
        this.walk(this.leftarm2, speed_walk, degree_walk * -0.5F, true, 1, 0.3F, f, f1);
        this.swing(this.body, speed_walk, degree_walk * -0.5F, false, 0, 0F, f, f1);
        this.swing(this.UpperBody, speed_walk, degree_walk * -0.25F, true, 0, 0F, f, f1);
        this.swing(this.Belly, speed_walk, degree_walk * -0.25F, false, 0, 0F, f, f1);
        this.walk(this.UpperBody, speed_idle, degree_idle * -0.15F, true, 0F, -0.1F, entity.ticksExisted, 1);
        this.flap(this.leftarm, speed_idle, degree_idle * -0.1F, true, 0, 0F, entity.ticksExisted, 1);
        this.flap(this.rightarm, speed_idle, degree_idle * -0.1F, false, 0, 0F, entity.ticksExisted, 1);
        this.flap(this.leftarm2, speed_idle, degree_idle * -0.1F, true, 0, -0.1F, entity.ticksExisted, 1);
        this.flap(this.rightarm2, speed_idle, degree_idle * -0.1F, false, 0, -0.1F, entity.ticksExisted, 1);

        float f12 = f1 * 0.5F;
        if (f12 < 0.0F) {
            f12 = 0.0F;
        }

        if (f12 > Math.toRadians(10)) {
            f12 = (float) Math.toRadians(10);
        }
        this.body.rotateAngleX = f12;
    }

    @Override
    public void renderStatue() {
        this.resetToDefaultPose();
        this.body.render(0.0625F);
    }
}
