package manexpen.levelstorage.armor.antimatter.dep;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import ic2.api.item.ElectricItem;
import manexpen.levelstorage.ItemList;
import manexpen.levelstorage.api.IHasRecipe;
import manexpen.levelstorage.util.CommonHelper;
import manexpen.levelstorage.util.LSDamageSource;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;


public class ItemArmorAntimatterChestplate extends ItemArmorAntimatterBase implements IHasRecipe {

    public static final int INDEX = ItemArmorAntimatterBase.CHESTPLATE + 1;
    public static final int KEEPAWAY_DISTANCE = 16;
    public static final int ENERGY_PER_HIT = 1000;

    public ItemArmorAntimatterChestplate() {
        super(ItemArmorAntimatterBase.CHESTPLATE);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onUpdate(LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayer)
            return;
        if (!((event.entityLiving instanceof EntityMob) || (event.entityLiving instanceof EntitySlime)))
            return;
        List<EntityPlayer> playerList = event.entityLiving.worldObj.playerEntities;
        for (EntityPlayer ep : playerList) {
            ItemStack armor = ep.inventory.armorInventory[INDEX];
            if (armor == null)
                continue;
            if (!(armor.getItem() instanceof ItemArmorAntimatterChestplate))
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
        ItemStack armor = ep.inventory.armorInventory[INDEX];
        if (armor == null)
            return;
        if (!(armor.getItem() instanceof ItemArmorAntimatterChestplate))
            return;
        int toBeUsed = (int) (event.ammount * ItemArmorAntimatterBase.ENERGY_PER_DAMAGE);
        if (ElectricItem.manager.canUse(armor, toBeUsed)) {
            if (!event.entityLiving.worldObj.isRemote)
                ElectricItem.manager.use(armor, toBeUsed, ep);
            event.setCanceled(true);
        }
    }


    @Override
    public void addCraftingRecipe() {
        GameRegistry.addRecipe(new ItemStack(ItemList.AntimatterChestPlate),
                "XYX",
                "XZX",
                "XAX",
                'X', ItemList.AntimatterIPlate,
                'Y', teslaCoil,
                'Z', ItemList.EnergeticChestplate,
                'A', ItemList.EEMatterOrb);
    }

}
