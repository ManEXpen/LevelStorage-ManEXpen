package manexpen.levelstorage.armor.antimatter;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import ic2.api.item.ElectricItem;
import ic2.api.util.Keys;
import manexpen.levelstorage.LSKeyboard;
import manexpen.levelstorage.api.BootsFlyingEvent;
import manexpen.levelstorage.api.EnumKey;
import manexpen.levelstorage.packet.LSPacketHandler;
import manexpen.levelstorage.util.EntityPlayerHelper;
import manexpen.levelstorage.util.nbt.NBTHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import packet.MessageKeyPressed;

/**
 * Created by manex on 2016/10/21.
 */
public class ItemArmorAntimatterBoots extends ItemArmorAntimatterBase {
    private float jumpCharge = 0;
    public boolean repelFlag = false;

    public ItemArmorAntimatterBoots() {
        super(BOOTS);
        setUnlocalizedName("ArmorAntiBoots");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEntityLivingFallEvent(LivingFallEvent event) {
        if ((!event.entityLiving.worldObj.isRemote)
                && ((event.entity instanceof EntityPlayer))) {
            EntityPlayer entity = (EntityPlayer) event.entity;
            ItemStack armor = entity.getCurrentArmor(1);

            if ((armor != null) && (armor.getItem() instanceof ItemArmorAntimatterBoots)) {
                int fallDamage = Math.max((int) event.distance - 3 - 7, 0);
                int energyCost = ENERGY_PER_DAMAGE * fallDamage;

                if (energyCost <= ElectricItem.manager.getCharge(armor)) {
                    ElectricItem.manager.discharge(armor, energyCost,
                            2147483647, true, false, false);

                    event.setCanceled(true);
                }
            }
        }
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (!world.isRemote) {
            jumpBooster(world, player, itemStack);
            fly(FLYING_ENERGY_PER_TICK, world, player, itemStack);
            HighSpeedDown(world, player, itemStack);
            repelPlayer(world, player, itemStack);
        }
    }

    @Override
    protected double getBaseAbsorptionRatio() {
        return 0;
    }

    //TODO: 要編集（弾きON・OFFの切り替え）
    @Override
    public void onPressedKey() {
        if (LSKeyboard.CHANGE_REPEL_MODE.getIsKeyPressed()) {
            LSPacketHandler.INSTANCE.sendToServer(new MessageKeyPressed(EnumKey.CHANGE_REPEL_MODE));
        }
    }

    @Override
    public void onRecieveKeyPacket(World world, EntityPlayer player, ItemStack itemStack, EnumKey keyType) {
        if (keyType == EnumKey.CHANGE_REPEL_MODE) repelFlagChange(player, itemStack);
    }

    @Override
    public void registerIcons(IIconRegister par1IconRegister) {

    }

    private void jumpBooster(World world, EntityPlayer player, ItemStack itemStack) {
        boolean boostKey = Keys.instance.isBoostKeyDown(player);

        if (!onGroundMap.containsKey(player))
            onGroundMap.put(player, true);
        if (!world.isRemote) {
            boolean wasOnGround = !onGroundMap.containsKey(player) || onGroundMap.get(player);

            if ((wasOnGround) && (!player.onGround) && (Keys.instance.isJumpKeyDown(player))
                    && (Keys.instance.isBoostKeyDown(player))) {
                ElectricItem.manager.use(itemStack, 4000, null);
            }
            onGroundMap.remove(player);
            onGroundMap.put(player, player.onGround);
        } else {
            if ((ElectricItem.manager.canUse(itemStack, 4000)) && (player.onGround))
                jumpCharge = 2.0F;

            if ((player.motionY >= 0.0D) && (jumpCharge > 0.0F) && (!player.isInWater())) {
                if ((Keys.instance.isJumpKeyDown(player) && (Keys.instance.isBoostKeyDown(player)))) {
                    if (jumpCharge == 2.0F) {
                        player.motionX *= 5.0D;
                        player.motionZ *= 5.0D;
                    }

                    player.motionY += jumpCharge * 0.3F;
                    jumpCharge = ((float) (jumpCharge * 0.75D));
                } else if (jumpCharge < 1.0F) {
                    jumpCharge = 0.0F;
                }

            }
        }
    }

    private void fly(int energy, World world, EntityPlayer player, ItemStack itemStack) {
        if (ElectricItem.manager.canUse(itemStack, energy)) {
            player.capabilities.allowFlying = true;
            if (player.capabilities.isFlying) {
                if (!world.isRemote) {
                    if (!player.capabilities.isCreativeMode) {
                        ElectricItem.manager.use(itemStack, energy, player);

                        MinecraftForge.EVENT_BUS.post(new BootsFlyingEvent(player, itemStack));
                    }
                }
            }
        }
    }

    private void HighSpeedDown(World world, EntityPlayer player, ItemStack itemstack) {
        if (world.isRemote) {
            if (player.isSneaking() && Keys.instance.isBoostKeyDown(player) && !player.onGround
                    && player.motionY <= 0) {
                player.motionY *= 2;
                EntityPlayerHelper.repelEntityInRange(world, player, 3.5F);
            }
        }
    }

    private void repelPlayer(World world, EntityPlayer player, ItemStack itemStack) {
        if (((ItemArmorAntimatterBoots) itemStack.getItem()).repelFlag) {
            EntityPlayerHelper.repelEntityInRange(world, player, 10);
        }
    }

    public void repelFlagChange(EntityPlayer player, ItemStack itemStack) {
        ((ItemArmorAntimatterBoots) itemStack.getItem()).repelFlag = NBTHelper.NBTReadFromBootsIsRepelEnable(itemStack);
        ((ItemArmorAntimatterBoots) itemStack.getItem()).repelFlag = !((ItemArmorAntimatterBoots) itemStack.getItem()).repelFlag;
        player.addChatComponentMessage(new ChatComponentText("現在のモード: " + ((ItemArmorAntimatterBoots) itemStack.getItem()).repelFlag));
        NBTHelper.NBTWriteToBoots(itemStack, ((ItemArmorAntimatterBoots) itemStack.getItem()).repelFlag);
    }

    public void boostFlying(World world, EntityPlayer player, ItemStack itemStack) {
        float boost = 0.44f;
        player.moveFlying(0.0F, 1.0F, boost);
        //本当は鯖側でしか動かないからこの判定いらない
        if (!world.isRemote) {
            ElectricItem.manager.use(itemStack, FLYING_ENERGY_PER_TICK * 3, player);
        }
    }
}
