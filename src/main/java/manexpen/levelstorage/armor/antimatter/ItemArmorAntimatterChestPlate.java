package manexpen.levelstorage.armor.antimatter;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import ic2.api.item.ElectricItem;
import manexpen.levelstorage.api.EnumKey;
import manexpen.levelstorage.util.CommonHelper;
import manexpen.levelstorage.util.LSDamageSource;
import manexpen.levelstorage.util.ResourceParameter;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;

/**
 * Created by manex on 2016/10/21.
 */
public class ItemArmorAntimatterChestPlate extends ItemArmorAntimatterBase {
    private static final int KEEPAWAY_DISTANCE = 16;
    private static final int ENERGY_PER_HIT = 1000;

    public ItemArmorAntimatterChestPlate() {
        super(CHESTPLATE);
        setUnlocalizedName("ArmorAntiChestPlate");
        FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (!world.isRemote) player.extinguish();
    }

    @Override
    protected double getBaseAbsorptionRatio() {
        return 0.4;
    }

    @Override
    public void onPressedKey() {

    }

    @Override
    public void onRecieveKeyPacket(World world, EntityPlayer player, ItemStack itemStack, EnumKey keyType) {

    }

    @Override
    public void registerIcons(IIconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon(ResourceParameter.ANTIMATTER_CHESTPLATE_TEXTURE);
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayer)
            return;
        if (!((event.entityLiving instanceof EntityMob) || (event.entityLiving instanceof EntitySlime)))
            return;
        List<EntityPlayer> playerList = event.entityLiving.worldObj.playerEntities;
        for (EntityPlayer ep : playerList) {
            ItemStack armor = ep.inventory.armorInventory[2];
            if (armor == null)
                continue;
            if (!(armor.getItem() instanceof ItemArmorAntimatterChestPlate))
                continue;
            int distance = CommonHelper.getDistance(ep.posX, ep.posY, ep.posZ,
                    event.entityLiving.posX, event.entityLiving.posY,
                    event.entityLiving.posZ);
            if (distance < KEEPAWAY_DISTANCE) {
                if (ElectricItem.manager.canUse(armor, ENERGY_PER_HIT)) {
                    if (!event.entityLiving.worldObj.isRemote)
                        ElectricItem.manager.use(armor, ENERGY_PER_HIT, ep);
                    event.entityLiving.attackEntityFrom(
                            LSDamageSource.forcefieldArmorInstaKill, 20.0F);
                }
            }
        }
    }

    @SubscribeEvent
    public void onHurt(LivingHurtEvent event) {
        if (event.entityLiving.worldObj.isRemote) return;
        if (!(event.entityLiving instanceof EntityPlayer))
            return;
        EntityPlayer ep = (EntityPlayer) event.entityLiving;
        ItemStack armor = ep.inventory.armorInventory[2];
        if (armor == null)
            return;
        if (!(armor.getItem() instanceof ItemArmorAntimatterChestPlate))
            return;
        int toBeUsed = (int) (event.ammount * ENERGY_PER_DAMAGE);
        if (ElectricItem.manager.canUse(armor, toBeUsed)) {
            if (!event.entityLiving.worldObj.isRemote)
                ElectricItem.manager.use(armor, toBeUsed, ep);
            event.setCanceled(true);
        }
    }

}
