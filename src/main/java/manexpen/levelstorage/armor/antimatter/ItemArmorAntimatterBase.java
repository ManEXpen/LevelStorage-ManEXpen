package manexpen.levelstorage.armor.antimatter;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import manexpen.levelstorage.ClientProxy;
import manexpen.levelstorage.api.IKeyHandler;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;

import java.util.HashMap;
import java.util.List;

/**
 * Created by manex on 2016/10/21.
 */
public abstract class ItemArmorAntimatterBase extends ItemArmor implements ISpecialArmor, IElectricItem, IKeyHandler {

    public static int RENDER_ID;
    public static final int HELMET = 0;
    public static final int CHESTPLATE = 1;
    public static final int LEGGINGS = 2;
    public static final int BOOTS = 3;
    public static final int ENERGY_PER_DAMAGE = 1000;
    public static final int STORAGE = 120000000;
    public static final int FLYING_ENERGY_PER_TICK = 512;
    public static final int FOOD_COST = 10000;
    public static final int RAY_COST = 100;
    public static final int ENTITY_HIT_COST = 10000;
    public static final int EU_PER_TELEPORT = 300000;
    public static final int EU_PER_TICK_WATERWALK = 100;

    public static HashMap<Entity, Boolean> onGroundMap = new HashMap<>();

    protected ItemArmorAntimatterBase(int armorType) {
        super(ArmorMaterial.DIAMOND, RENDER_ID, armorType);
        this.setMaxDamage(27);
        this.setNoRepair();
        this.setCreativeTab(ClientProxy.CreativeTab);
        this.setMaxStackSize(1);
    }

    public abstract void onArmorTick(World world, EntityPlayer player, ItemStack itemStack);

    protected abstract double getBaseAbsorptionRatio();

    @SideOnly(Side.CLIENT)
    public abstract void registerIcons(IIconRegister par1IconRegister);

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return this.armorType == CHESTPLATE;
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
        return 4;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return 100000000;
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        if (source.isUnblockable()) return new ISpecialArmor.ArmorProperties(0, 0.0D, 0);

        double absorptionRatio = getBaseAbsorptionRatio() * getDamageAbsorptionRatio();
        int energyPerDamage = ENERGY_PER_DAMAGE;

        int damageLimit = (int) (25 * ElectricItem.manager.getCharge(armor) / ENERGY_PER_DAMAGE);

        return new ISpecialArmor.ArmorProperties(0, absorptionRatio, damageLimit);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        if (ElectricItem.manager.getCharge(armor) >= ENERGY_PER_DAMAGE) {
            return (int) Math.round(20.0D * getBaseAbsorptionRatio() * getDamageAbsorptionRatio());
        }
        return 0;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        ElectricItem.manager.discharge(stack, damage * ENERGY_PER_DAMAGE, 2147483647, true, false, false);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return "minecraft:textures/armor/antimatter_layer" + "_" + (this.armorType == 2 ? "2" : "1") + ".png";
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean p_77624_4_) {
        list.add((int) ElectricItem.manager.getCharge(itemStack) + "/" + (int) this.getMaxCharge(itemStack));
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        ItemStack stack = new ItemStack(this, 1);
        ElectricItem.manager.charge(stack, STORAGE, STORAGE, true, false);
        list.add(stack);
        list.add(new ItemStack(this, 1, this.getMaxDamage()));
    }

    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.epic;
    }

    protected double getDamageAbsorptionRatio() {
        return this.armorType == CHESTPLATE ? 1.1D : 1.0D;
    }
}
