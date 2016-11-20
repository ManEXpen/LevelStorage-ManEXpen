package manexpen.levelstorage.armor.antimatter;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import manexpen.levelstorage.LSKeyboard;
import manexpen.levelstorage.api.EnumKey;
import manexpen.levelstorage.packet.LSPacketHandler;
import manexpen.levelstorage.render.CallMWRay;
import manexpen.levelstorage.util.ResourceParameter;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenDesert;
import packet.MessageKeyPressed;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by manex on 2016/10/21.
 */
public class ItemArmorAntimatterHelmet extends ItemArmorAntimatterBase {
    private static HashMap<Integer, Integer> potionRemovalCost = new HashMap<>();

    public ItemArmorAntimatterHelmet() {
        super(HELMET);
        setUnlocalizedName("ArmorAntiHelmet");
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        helmetFunctions(world, player, itemStack);
    }

    @Override
    protected double getBaseAbsorptionRatio() {
        return 0.15;
    }

    @Override
    public void registerIcons(IIconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon(ResourceParameter.ANTIMATTER_HELMET_TEXTURE);
    }

    @Override
    public void onPressedKey() {
    }

    @Override
    public void onRecieveKeyPacket(World world, EntityPlayer player, ItemStack itemStack, EnumKey keyType) {
        if (keyType == EnumKey.RAY_SHOOT) firingRay(world, player, itemStack);
    }

    //TODO: キーボード処理Client・Server
    private void helmetFunctions(World world, EntityPlayer player, ItemStack itemStack) {

        if (!world.isRemote) {
            charge(itemStack, player, world);

            if (player.getFoodStats().getFoodLevel() < 18) {
                if (ElectricItem.manager.canUse(itemStack, FOOD_COST)) {
                    ElectricItem.manager.use(itemStack, FOOD_COST, player);
                    player.getFoodStats().addStats(20, 20);
                }
            }
            if (player.getAir() < 100) {
                player.setAir(200);
            }
            LinkedList<PotionEffect> lk = new LinkedList(player.getActivePotionEffects());
            for (PotionEffect effect : lk) {
                int id = effect.getPotionID();

                Integer cost = potionRemovalCost.get(id);

                if (cost != null) {
                    cost = cost * (effect.getAmplifier() + 1);

                    if (ElectricItem.manager.canUse(itemStack, cost)) {
                        ElectricItem.manager.use(itemStack, cost, null);
                        player.removePotionEffect(id);
                    }
                }
            }
        } else {
            if (LSKeyboard.BEAM_SHOOT_KEY.getIsKeyPressed())
                LSPacketHandler.INSTANCE.sendToServer(new MessageKeyPressed(EnumKey.RAY_SHOOT));
        }
    }

    private void charge(ItemStack itemStack, EntityPlayer player, World world) {
        int toCharge = 0;

        if (isSunVisible(player.worldObj, (int) player.posX,
                (int) player.posY + 1, (int) player.posZ)) {
            toCharge += 2048;
        }

        for (ItemStack is : player.inventory.armorInventory) {
            if (is == null)
                continue;
            if (is.getItem() instanceof IElectricItem) {
                toCharge -= ElectricItem.manager.charge(is, toCharge, 4, true, false);
                if (toCharge == 0)
                    break;
            }
        }
        for (ItemStack is : player.inventory.mainInventory) {
            if (is == null)
                continue;
            if (is.getItem() instanceof IElectricItem) {
                toCharge -= ElectricItem.manager.charge(is, toCharge, 4, true, false);
                if (toCharge == 0)
                    break;
            }
        }
    }

    public void firingRay(World world, EntityPlayer player, ItemStack itemStack) {
        if (ElectricItem.manager.canUse(itemStack, RAY_COST)) {
            ElectricItem.manager.use(itemStack, RAY_COST, player);
            CallMWRay.shotMW(world, player);
        }
    }

    private static boolean isSunVisible(World world, int x, int y, int z) {
        return (world.isDaytime())
                && (!world.provider.hasNoSky)
                && (world.canBlockSeeTheSky(x, y, z))
                && (((world.getWorldChunkManager().getBiomeGenAt(x, z) instanceof BiomeGenDesert)) || ((!world
                .isRaining()) && (!world.isThundering())));
    }


    static {
        potionRemovalCost.put(Potion.poison.id, 10000);
        potionRemovalCost.put(Potion.wither.id, 25000);
        potionRemovalCost.put(Potion.blindness.id, 10000);
        potionRemovalCost.put(Potion.hunger.id, 1000);
    }
}
