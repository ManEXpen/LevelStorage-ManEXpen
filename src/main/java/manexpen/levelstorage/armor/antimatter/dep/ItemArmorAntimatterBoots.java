package manexpen.levelstorage.armor.antimatter.dep;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import ic2.api.item.ElectricItem;
import manexpen.levelstorage.ItemList;
import manexpen.levelstorage.api.IHasRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingFallEvent;

public class ItemArmorAntimatterBoots extends ItemArmorAntimatterBase implements IHasRecipe {

    public boolean repelFlag;

    public ItemArmorAntimatterBoots() {
        super(ItemArmorAntimatterBase.BOOTS);
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
    public void addCraftingRecipe() {
        GameRegistry.addRecipe(new ItemStack(ItemList.AntimatterBoots),
                "XYX",
                "XZX",
                "ABA",
                'X', IridiumPlate,
                'Y', rubber,
                'Z', ItemList.LevitationBoots,
                'A', ItemList.AntimatterIPlate,
                'B', ItemList.EEMatterOrb);
    }

}
