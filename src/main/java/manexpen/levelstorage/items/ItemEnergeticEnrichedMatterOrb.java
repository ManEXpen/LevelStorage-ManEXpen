package manexpen.levelstorage.items;

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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemEnergeticEnrichedMatterOrb extends Item implements IElectricItem, IHasRecipe {
    public static final int TIER = 4;
    public static final int STORAGE = 40 * 1000 * 1000;

    public ItemEnergeticEnrichedMatterOrb() {
        this.setMaxDamage(27);
        this.setNoRepair();
        this.setCreativeTab(ClientProxy.CreativeTab);
        this.setMaxStackSize(1);
    }

    @Override
    public void addCraftingRecipe() {
        GameRegistry.addRecipe(new ItemStack(ItemList.EEMatterOrb),
                "XYX",
                "YZY",
                "XYX",
                'X', lapotronCrystal,
                'Y', ItemList.SuperConductorParts,
                'Z', IridiumPlate);
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return true;
    }

    @Override
    public Item getChargedItem(ItemStack itemStack) {
        return this;
    }

    @Override
    public Item getEmptyItem(ItemStack itemStack) {
        return this;
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return STORAGE;
    }

    @Override
    public int getTier(ItemStack itemStack) {
        return TIER;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return 100000;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon(ResourceParameter.getTexturePathFor("itemEnergeticEnrichedMatterOrb"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.epic;
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        ItemStack var4 = new ItemStack(this, 1);
        ElectricItem.manager.charge(var4, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false);
        par3List.add(var4);
        par3List.add(new ItemStack(this, 1, this.getMaxDamage()));
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean p_77624_4_) {
        list.add((int) ElectricItem.manager.getCharge(itemStack) + "/" + (int) this.getMaxCharge(itemStack));
    }

}
