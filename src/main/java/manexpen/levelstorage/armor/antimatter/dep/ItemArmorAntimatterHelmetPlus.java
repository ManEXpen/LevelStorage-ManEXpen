package manexpen.levelstorage.armor.antimatter.dep;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import manexpen.levelstorage.ClientProxy;
import manexpen.levelstorage.ItemList;
import manexpen.levelstorage.api.IHasRecipe;
import manexpen.levelstorage.util.ResourceParameter;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;

import java.util.List;

public class ItemArmorAntimatterHelmetPlus extends ItemArmorAntimatterBase
        implements IElectricItem, ISpecialArmor, IHasRecipe {

    public static final int STORAGE = 120000000;
    public static final int ENERGY_PER_DAMAGE = 1000;

    public ItemArmorAntimatterHelmetPlus() {
        super();

        this.setMaxDamage(27);
        this.setNoRepair();
        this.setCreativeTab(ClientProxy.CreativeTab);
        this.setMaxStackSize(1);
        this.setUnlocalizedName("ItemArmorAntimatterHelmetPlus");
    }

    @Override
    public ISpecialArmor.ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source,
                                                       double damage, int slot) {
        if (source.isUnblockable())
            return new ISpecialArmor.ArmorProperties(0, 0.0D, 0);

        double absorptionRatio = getBaseAbsorptionRatio() * 1.0D;
        int energyPerDamage = ENERGY_PER_DAMAGE;

        int damageLimit = (int) (energyPerDamage > 0 ? 25 * ElectricItem.manager.getCharge(armor) / energyPerDamage
                : 0);

        return new ISpecialArmor.ArmorProperties(0, absorptionRatio, damageLimit);
    }

    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.epic;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon(ResourceParameter.ANTIMATTER_HELMET_TEXTURE);
    }

    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean p_77624_4_) {
        list.add((int) ElectricItem.manager.getCharge(itemstack) + "/" + (int) this.getMaxCharge(itemstack));
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return "minecraft:textures/armor/antimatter_layer" + "_" + (this.armorType == 2 ? "2" : "1") + ".png";
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        ElectricItem.manager.discharge(stack, damage * ENERGY_PER_DAMAGE, 2147483647, true, false, false);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        super.onArmorTick(world, player, itemStack);

        int toCharge = 0;
        toCharge += 532480;

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

    private double getBaseAbsorptionRatio() {
        switch (this.armorType) {
            case 0:
                return 0.15D;
            case 1:
                return 0.4D;
            case 2:
                return 0.3D;
            case 3:
                return 0.15D;
        }
        return 0.0D;
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        if (ElectricItem.manager.getCharge(armor) >= ENERGY_PER_DAMAGE) {
            return (int) Math.round(20.0D * getBaseAbsorptionRatio() * 1.0D);
        }
        return 0;
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        ItemStack var4 = new ItemStack(this, 1);
        ElectricItem.manager.charge(var4, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false);
        par3List.add(var4);
        par3List.add(new ItemStack(this, 1, this.getMaxDamage()));

    }

    @Override
    public void addCraftingRecipe() {
        GameRegistry.addShapelessRecipe(new ItemStack(ItemList.AntimatterHelmetPlus),
                new Object[]{ItemList.AntimatterHelmet, sorlar53});
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        // TODO 自動生成されたメソッド・スタブ
        return false;
    }

    @Override
    public Item getChargedItem(ItemStack itemStack) {
        // TODO 自動生成されたメソッド・スタブ
        return this;
    }

    @Override
    public Item getEmptyItem(ItemStack itemStack) {
        // TODO 自動生成されたメソッド・スタブ
        return this;
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        // TODO 自動生成されたメソッド・スタブ
        return STORAGE;
    }

    @Override
    public int getTier(ItemStack itemStack) {
        // TODO 自動生成されたメソッド・スタブ
        return 4;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        // TODO 自動生成されたメソッド・スタブ
        return 100000000;
    }

}
