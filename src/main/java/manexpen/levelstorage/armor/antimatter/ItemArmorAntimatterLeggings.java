package manexpen.levelstorage.armor.antimatter;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import ic2.api.item.ElectricItem;
import ic2.api.util.Keys;
import manexpen.levelstorage.LSKeyboard;
import manexpen.levelstorage.api.EnumKey;
import manexpen.levelstorage.packet.LSPacketHandler;
import manexpen.levelstorage.util.BlockLocation;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.living.LivingEvent;
import packet.MessageKeyPressed;

import java.util.HashMap;
import java.util.List;

/**
 * Created by manex on 2016/10/21.
 */
public class ItemArmorAntimatterLeggings extends ItemArmorAntimatterBase {
    public static HashMap<EntityPlayer, Integer> speedTickerMap = new HashMap<>();
    private boolean waitJump = false;

    public ItemArmorAntimatterLeggings() {
        super(LEGGINGS);
        setUnlocalizedName("ArmorAntiLeggings");
        FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (!world.isRemote) {
            speedUp(player, itemStack);
            MultipleJump(world, player, itemStack);
        }
    }

    @Override
    protected double getBaseAbsorptionRatio() {
        return 0;
    }


    //TODO: 要編集（テレポートのキーボード処理）
    @Override
    public void onPressedKey() {
        if (LSKeyboard.TELEPORT.getIsKeyPressed())
            LSPacketHandler.INSTANCE.sendToServer(new MessageKeyPressed(EnumKey.TELEPORT));
    }

    @Override
    public void onRecieveKeyPacket(World world, EntityPlayer player, ItemStack itemStack, EnumKey keyType) {
        if (keyType == EnumKey.TELEPORT) teleport(world, player, itemStack);
    }

    @Override
    public void registerIcons(IIconRegister par1IconRegister) {

    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayer)
            return;
        if (!(event.entityLiving instanceof EntityMob))
            return;

        List<EntityPlayer> playerList = event.entityLiving.worldObj.playerEntities;
        for (EntityPlayer ep : playerList) {
            ItemStack armor = ep.inventory.armorInventory[1];
            if (armor == null)
                continue;
            if (!(armor.getItem() instanceof ItemArmorAntimatterLeggings)) {
                if (ep.stepHeight == 1.004F) {
                    ep.stepHeight = 0.5F;
                    return;
                }
            }
            ep.stepHeight = 1.004F;
        }
    }

    private void speedUp(EntityPlayer player, ItemStack itemStack) {
        if (!speedTickerMap.containsKey(player))
            speedTickerMap.put(player, 0);
        float speed = 0.66F;
        if ((ElectricItem.manager.canUse(itemStack, 1000)) && ((player.onGround) || (player.isInWater()))
                && (player.isSprinting())) {
            int speedTicker = speedTickerMap.containsKey(player) ? speedTickerMap.get(player)
                    : 0;
            speedTicker++;

            if (speedTicker >= 10) {
                speedTicker = 0;
                ElectricItem.manager.use(itemStack, 1000, null);
            }
            speedTickerMap.remove(player);
            speedTickerMap.put(player, speedTicker);

            if (player.isInWater()) {
                speed = 0.1F;
                if (Keys.instance.isJumpKeyDown(player))
                    player.motionY += 0.1000000014901161D;
            }

            player.moveFlying(0.0F, 1.0F, speed);
        }
    }

    private void MultipleJump(World world, EntityPlayer player, ItemStack itemstack) {
        if (Keys.instance.isJumpKeyDown(player) && Keys.instance.isBoostKeyDown(player)
                && (ElectricItem.manager.canUse(itemstack, 1000))) {
            if (waitJump) {
                player.motionY = 1;
                player.motionY += 0.5;
                player.motionY *= 2.5D;
                player.motionY = 3.0D;
            }
            ElectricItem.manager.use(itemstack, 250, player);
            waitJump = false;
        } else {
            waitJump = true;
        }
    }

    public void teleport(World world, EntityPlayer player, ItemStack itemStack) {
        if (world.isRemote) return;
        int x = 0, y = 0, z = 0;
        MovingObjectPosition mop = getMovingObjectPositionFromPlayer(world, player, true);
        if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            if (ElectricItem.manager.canUse(itemStack, EU_PER_TELEPORT))
                ElectricItem.manager.use(itemStack, EU_PER_TELEPORT, player);
            x = mop.blockX;
            y = mop.blockY;
            z = mop.blockZ;
            int sideHit = mop.sideHit;
            BlockLocation bl = new BlockLocation(x, y, z);
            bl = bl.move(ForgeDirection.getOrientation(sideHit), 1);
            player.setPositionAndUpdate(bl.getX(), bl.getY(), bl.getZ());
        }
    }

}
