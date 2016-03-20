package manexpen.levelstorage.entity;

import java.util.List;

import manexpen.levelstorage.LevelStorage;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityElectromagneticBombs extends Entity implements IProjectile {

	protected int xTile = -1;
	protected int yTile = -1;
	protected int zTile = -1;
	protected Block inTile;
	protected int inData;
	protected boolean inGround;

	private int flag = 0;

	/* この弾を撃ったエンティティ */
	public Entity shootingEntity;

	/* 地中・空中にいる時間 */
	protected int ticksInGround;
	protected int ticksInAir;

	/* ダメージの大きさ */
	protected double damage = 15.0D;

	protected int knockbackStrength = 1;

	public EntityElectromagneticBombs(World world) {
		super(world);
		this.renderDistanceWeight = 10.0D;
		this.setSize(0.5F, 0.5F);
		this.damage = 15.0D;
	}

	public EntityElectromagneticBombs(World world, EntityPlayer player, float speed, float speed2, float adjustX,
			float adjustY, float adjustZ) {
		super(world);

		this.renderDistanceWeight = 10.0D;
		this.shootingEntity = player;
		this.yOffset = 0.0F;
		this.setSize(0.5F, 0.5F);

		this.setLocationAndAngles(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ,
				player.prevRotationYaw, player.prevRotationPitch);

		this.posX += -(double) (MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * (1.0F + adjustZ))
				- (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * adjustX);
		this.posY += 0.05000000149011612D + adjustY;
		this.posZ += (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * (1.0F + adjustZ))
				- (double) (MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * adjustX);
		this.setPosition(this.posX, this.posY, this.posZ);

		// 初速度
		this.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI)
				* MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
		this.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI)
				* MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
		this.motionY = (double) (-MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI));

		this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, speed * 1.5F, speed2);
	}

	@Override
	public void setThrowableHeading(double x, double y, double z, float speed, float accuracy) {
		float dirLength = MathHelper.sqrt_double(x * x + y * y + z * z);
		x /= (double) dirLength;
		y /= (double) dirLength;
		z /= (double) dirLength;
		x += this.rand.nextGaussian() * 0.007499999832361937D * (double) accuracy;
		y += this.rand.nextGaussian() * 0.007499999832361937D * (double) accuracy;
		z += this.rand.nextGaussian() * 0.007499999832361937D * (double) accuracy;
		x *= (double) speed;
		y *= (double) speed;
		z *= (double) speed;
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		float f3 = MathHelper.sqrt_double(x * x + z * z);
		this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(x, z) * 180.0D / Math.PI);
		this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(y, (double) f3) * 180.0D / Math.PI);
	}

	@Override
	public void setVelocity(double par1, double par3, double par5) {
		this.motionX = par1;
		this.motionY = par3;
		this.motionZ = par5;

		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
			this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(par1, par5) * 180.0D / Math.PI);
			this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(par3, (double) f) * 180.0D / Math.PI);
			this.prevRotationPitch = this.rotationPitch;
			this.prevRotationYaw = this.rotationYaw;
			this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			this.ticksInGround = 0;
		}
	}

	@Override
	public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
		this.setPosition(par1, par3, par5);
		this.setRotation(par7, par8);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.playSound();

		// 直前のパラメータと新パラメータを一致させているところ。
		// また、速度に応じてエンティティの向きを調整し、常に進行方向に前面が向くようにしている。
		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D
					/ Math.PI);
			this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(this.motionY, (double) f) * 180.0D
					/ Math.PI);
		}

		// 激突したブロックを確認している
		Block i = this.worldObj.getBlock(this.xTile, this.yTile, this.zTile);

		// 空気じゃないブロックに当たった&ブロック貫通エンティティでない時
		if (Blocks.air != i && !this.isPenetrateBlock()) {
			i.setBlockBoundsBasedOnState(this.worldObj, this.xTile, this.yTile, this.zTile);
			AxisAlignedBB axisalignedbb = i.getCollisionBoundingBoxFromPool(this.worldObj, this.xTile, this.yTile,
					this.zTile);

			// 当たり判定に接触しているかどうか
			if (axisalignedbb != null
					&& axisalignedbb.isVecInside(Vec3.createVectorHelper(this.posX, this.posY, this.posZ))) {
				this.inGround = true;
			}
		}

		// 空気じゃないブロックに当たった
		if (this.inGround) {
			Block j = this.worldObj.getBlock(this.xTile, this.yTile, this.zTile);
			int k = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);

			/*
			 * 前のTickに確認した埋まりブロックのIDとメタを照合している。違ったら埋まり状態を解除、一致したら埋まり状態を継続。 /*
			 * 埋まり状態2tick継続でこのエンティティを消す
			 */
			if (j == this.inTile && k == this.inData) {
				++this.ticksInGround;
				// ブロック貫通の場合、20tick（1秒間）はブロック中にあっても消えないようになる。
				int limit = this.isPenetrateBlock() ? 20 : 2;

				if (this.ticksInGround > limit) {
					this.setDead();
				}
			} else// 埋まり状態の解除処理
			{
				this.inGround = false;
				this.motionX *= (double) (this.rand.nextFloat() * 0.1F);
				this.motionY *= (double) (this.rand.nextFloat() * 0.1F);
				this.motionZ *= (double) (this.rand.nextFloat() * 0.1F);
				this.ticksInGround = 0;
				this.ticksInAir = 0;
			}
		} else// 埋まってない時。速度の更新。
		{
			++this.ticksInAir;
			// ブロックとの衝突判定
			Vec3 vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
			Vec3 vec31 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY,
					this.posZ + this.motionZ);
			MovingObjectPosition movingobjectposition = this.worldObj.func_147447_a(vec3, vec31, false, true, true);
			vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
			vec31 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY,
					this.posZ + this.motionZ);

			// ブロック貫通がONの場合、ブロック衝突判定をスキップ
			if (this.isPenetrateBlock()) {
				movingobjectposition = null;
			}

			// ブロックに当たった
			if (movingobjectposition != null) {
				vec31 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord,
						movingobjectposition.hitVec.zCoord);
			}

			// Entityとの衝突判定。
			Entity entity = null;
			List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this,
					this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(4.0D, 4.0D, 4.0D));
			double d0 = 0.0D;
			int l;
			float f1;
			boolean isVillager = false;

			// 4ブロック分の範囲内にいるエンティティ全てに対して繰り返す
			for(Entity entity1 : list) {


				// 発射物自身or発射後5tick以外だとすりぬける
				if (entity1.canBeCollidedWith() && (entity1 != this.shootingEntity || this.ticksInAir >= 5)) {
					f1 = 0.3F;
					AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand((double) f1, (double) f1, (double) f1);
					MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec3, vec31);

					if (movingobjectposition1 != null) {
						double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

						if (d1 < d0 || d0 == 0.0D) {
							entity = entity1;
							Minecraft.getMinecraft().theWorld.newExplosion(Minecraft.getMinecraft().thePlayer, entity.posX, entity.posY, entity.posZ, 40F, false, false);
							d0 = d1;
						}
					}
				}
			}

			// エンティティに当たった
			if (entity != null) {
				movingobjectposition = new MovingObjectPosition(entity);
			}

			/*
			 * 当たったエンティティそれそれについての判定部分。 ここでmovingobjectposition =
			 * nullにすることで特定の種類のエンティティに当たらないようにできる。
			 */
			if (movingobjectposition != null && movingobjectposition.entityHit != null) {
				if (movingobjectposition.entityHit instanceof EntityPlayer) {
					// プレイヤーに当たった時
					EntityPlayer entityplayer = (EntityPlayer) movingobjectposition.entityHit;

					if (entityplayer.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer
							&& !((EntityPlayer) this.shootingEntity).canAttackPlayer(entityplayer)) {
						// PvPが許可されていないと当たらない
						movingobjectposition = null;
					} else if (entityplayer == this.shootingEntity) {
						// 対象が撃った本人の場合も当たらない
						movingobjectposition = null;
					}
				} else if (movingobjectposition.entityHit instanceof EntityTameable
						|| movingobjectposition.entityHit instanceof EntityHorse) {
					// 事故防止の為、EntityTameable（犬や猫などのペット）、馬にも当たらないようにする
					movingobjectposition = null;
				} else if (movingobjectposition.entityHit instanceof EntityVillager) {
					// 村人に当たった場合にフラグがtrueになる
					isVillager = true;
				}
			}

			float f2;
			float f3;

			// 当たったあとの処理
			if (movingobjectposition != null) {
				// エンティティに当たった
				if (movingobjectposition.entityHit != null) {
					// 衝突時の弾の速度を計算
					f2 = MathHelper.sqrt_double(
							this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
					// 速度が大きいほど、ダメージも大きくなる
					int i1 = MathHelper.ceiling_double_int((double) f2 * this.damage);
					// 0~2程度の乱数値を上乗せ
					i1 += this.rand.nextInt(3);

					DamageSource damagesource = null;

					// 別メソッドでダメージソースを確認
					damagesource = this.thisDamageSource(this.shootingEntity);

					// バニラ矢と同様、このエンティティが燃えているなら対象に着火することも出来る
					if (this.isBurning() && !(movingobjectposition.entityHit instanceof EntityEnderman)) {
						movingobjectposition.entityHit.setFire(5);
					}

					if (isVillager) {
						// 対象が村人だった場合の処理
						EntityVillager villager = (EntityVillager) movingobjectposition.entityHit;
						// ダメージに相当する量の回復効果をもたらす
						villager.heal((float) i1);
						// ただしノックバックは有る
						if (this.knockbackStrength > 0) {
							f3 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);

							if (f3 > 0.0F) {
								movingobjectposition.entityHit.addVelocity(
										this.motionX * (double) this.knockbackStrength * 0.6000000238418579D
												/ (double) f3,
										0.1D, this.motionZ * (double) this.knockbackStrength * 0.6000000238418579D
												/ (double) f3);
							}
						} else {
							movingobjectposition.entityHit.hurtResistantTime = 0;
						}
					} else {
						// 村人以外なら、ダメージを与える処理を呼ぶ
						if (movingobjectposition.entityHit.attackEntityFrom(damagesource, (float) i1)) {
							// ダメージを与えることに成功したら以下の処理を行う
							if (movingobjectposition.entityHit instanceof EntityLivingBase) {
								EntityLivingBase entitylivingbase = (EntityLivingBase) movingobjectposition.entityHit;

								// ノックバック
								if (this.knockbackStrength > 0) {
									f3 = MathHelper
											.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);

									if (f3 > 0.0F) {
										movingobjectposition.entityHit.addVelocity(
												this.motionX * (double) this.knockbackStrength * 0.6000000238418579D
														/ (double) f3,
												0.1D, this.motionZ * (double) this.knockbackStrength
														* 0.6000000238418579D / (double) f3);
									}
								} else {
									movingobjectposition.entityHit.hurtResistantTime = 0;
								}

							}

							// 当たったあと、弾を消去する。エンティティ貫通がONの弾種はそのまま残す。
							if (!(movingobjectposition.entityHit instanceof EntityEnderman)
									&& !this.isPenetrateEntity()) {
								this.setDead();
							}
						}
					}

				} else if (!this.isPenetrateBlock()) // エンティティには当たってない。ブロックに当たった。
				{
					this.xTile = movingobjectposition.blockX;
					this.yTile = movingobjectposition.blockY;
					this.zTile = movingobjectposition.blockZ;
					this.inTile = this.worldObj.getBlock(this.xTile, this.yTile, this.zTile);
					this.inData = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
					this.motionX = (double) ((float) (movingobjectposition.hitVec.xCoord - this.posX));
					this.motionY = (double) ((float) (movingobjectposition.hitVec.yCoord - this.posY));
					this.motionZ = (double) ((float) (movingobjectposition.hitVec.zCoord - this.posZ));
					f2 = MathHelper.sqrt_double(
							this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
					this.posX -= this.motionX / (double) f2 * 0.05000000074505806D;
					this.posY -= this.motionY / (double) f2 * 0.05000000074505806D;
					this.posZ -= this.motionZ / (double) f2 * 0.05000000074505806D;
					this.inGround = true;

					if (this.inTile == Blocks.air) {
						this.inTile.onEntityCollidedWithBlock(this.worldObj, this.xTile, this.yTile, this.zTile, this);
					}
				}
			}

			// 改めてポジションに速度を加算。向きも更新。
			this.posX += this.motionX;
			this.posY += this.motionY;
			this.posZ += this.motionZ;
			f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
			this.rotationPitch = (float) (Math.atan2(this.motionY, (double) f2) * 180.0D / Math.PI);

			while (this.rotationPitch - this.prevRotationPitch < -180.0F) {
				this.prevRotationPitch -= 360.0F;
			}

			while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
				this.prevRotationPitch += 360.0F;
			}

			while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
				this.prevRotationYaw -= 360.0F;
			}

			while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
				this.prevRotationYaw += 360.0F;
			}

			this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
			this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;

			// 徐々に減速する
			float f4 = 0.999F;

			// 重力落下
			// 落下速度は別メソッドで設定している。デフォルトでは0.0F。
			f1 = this.fallSpeed();

			// 水中に有る
			if (this.isInWater()) {
				// 泡パーティクルが出る
				for (int j1 = 0; j1 < 4; ++j1) {
					f3 = 0.25F;
					this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double) f3,
							this.posY - this.motionY * (double) f3, this.posZ - this.motionZ * (double) f3,
							this.motionX, this.motionY, this.motionZ);
				}

				// 減速も大きくなる
				f4 = 0.99F;
			}

			this.motionX *= (double) f4;
			this.motionY *= (double) f4;
			this.motionZ *= (double) f4;
			this.motionY -= (double) f1;

			// 一定以上遅くなったら消える
			if (this.worldObj.isRemote && this.motionX * this.motionX + this.motionZ * this.motionZ < 0.08D) {
				this.setDead();
			}

			this.setPosition(this.posX, this.posY, this.posZ);
			this.func_145775_I();
		}
	}

	@Override
	protected boolean canTriggerWalking()
    {
        return false;
    }

	@Override
	public float getShadowSize()
    {
        return 0.0F;
    }

	public void setDamage(float p_70239_1_)
    {
        this.damage = p_70239_1_;
    }

	public double getDamage()
    {
        return this.damage;
    }

	@Override
    public boolean canAttackWithItem()
    {
        return false;
    }

	public float fallSpeed()
    {
    	return 0.0F;
    }

	public float getRenderSize(){return 2;}

    /* ダメージソースのタイプ */
    public DamageSource thisDamageSource(Entity entity)
    {
    	return entity != null ? EntityDamageSource.causeIndirectMagicDamage(entity, this) : DamageSource.magic;
    }

    /* ブロック貫通 */
    public boolean isPenetrateBlock()
    {
    	return false;
    }

    /* エンティティ貫通 */
    public boolean isPenetrateEntity()
    {
    	return true;
    }

    private void playSound(){
    	if(this.flag == 0)
    	this.playSound(LevelStorage.MODNAME.toLowerCase()+":plasma_shotgun_shot", 1.0F, 1.2F / 0.9F);
    	this.flag++;
    }

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		this.xTile = nbt.getShort("xTile");
		this.yTile = nbt.getShort("yTile");
		this.zTile = nbt.getShort("zTile");
		this.inTile = Block.getBlockById(nbt.getInteger("inTile") & 255);
		this.inData = nbt.getByte("inData") & 255;
		this.inGround = nbt.getByte("inGround") == 1;

		if (nbt.hasKey("damage")) {
			this.damage = nbt.getDouble("damage");
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		nbt.setShort("xTile", (short) this.xTile);
		nbt.setShort("yTile", (short) this.yTile);
		nbt.setShort("zTile", (short) this.zTile);
		nbt.setInteger("inTile", Block.getIdFromBlock(this.inTile));
		nbt.setByte("inData", (byte) this.inData);
		nbt.setByte("inGround", (byte) (this.inGround ? 1 : 0));
		nbt.setDouble("damage", this.damage);
	}

	@Override
	protected void entityInit() {
	}

}
