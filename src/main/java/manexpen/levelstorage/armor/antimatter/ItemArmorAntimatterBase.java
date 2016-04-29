package manexpen.levelstorage.armor.antimatter;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import manexpen.levelstorage.ClientProxy;
import manexpen.levelstorage.util.ArmorFunctions;
import manexpen.levelstorage.util.ResourceParameter;
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

public class ItemArmorAntimatterBase extends ItemArmor implements ISpecialArmor, IElectricItem{

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


	protected ItemArmorAntimatterBase() {
		super(ArmorMaterial.DIAMOND, RENDER_ID, 0);
		this.setUnlocalizedName("ItemArmorAntimatterHelmetPlus");
	}

	public ItemArmorAntimatterBase(int armorType) {
		super(ArmorMaterial.DIAMOND, RENDER_ID, armorType);
		this.setMaxDamage(27);
		this.setNoRepair();
		this.setCreativeTab(ClientProxy.CreativeTab);
		this.setMaxStackSize(1);

		switch (armorType) {
		case HELMET:
			this.setUnlocalizedName("ItemArmorAntimatterHelmet");
			break;
		case CHESTPLATE:
			this.setUnlocalizedName("ItemArmorAntimaterChestPlate");
			break;
		case LEGGINGS:
			this.setUnlocalizedName("ItemArmorAntimatterLeggings");
			break;
		case BOOTS:
			this.setUnlocalizedName("ItemArmorAntimatterBoots");
			break;
		}
	}

	public static final int EU_PER_TELEPORT = 300000;
	public static final int EU_PER_TICK_WATERWALK = 100;

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		if (this.armorType == CHESTPLATE)
			ArmorFunctions.extinguish(player, world);
		else if (this.armorType == BOOTS) {
			ArmorFunctions.jumpBooster(world, player, itemStack);
			ArmorFunctions.fly(FLYING_ENERGY_PER_TICK, player, itemStack, world);
			ArmorFunctions.HighSpeedDown(world, player, itemStack);
			ArmorFunctions.repelEntity(world, player, itemStack);
		} else if (this.armorType == LEGGINGS) {
			ArmorFunctions.speedUp(player, itemStack);
			ArmorFunctions.MultipleJump(world, player, itemStack);
			ArmorFunctions.antimatterLeggingsFunctions(world, player, itemStack);
		} else if (this.armorType == HELMET) {
			ArmorFunctions.helmetFunctions(world, player, itemStack, RAY_COST, ENTITY_HIT_COST, FOOD_COST);
		}
	}

	public double getDamageAbsorptionRatio() {
		if (this.armorType == CHESTPLATE)
			return 1.1D;
		return 1.0D;
	}

	public ISpecialArmor.ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source,
			double damage, int slot) {
		if (source.isUnblockable())
			return new ISpecialArmor.ArmorProperties(0, 0.0D, 0);

		double absorptionRatio = getBaseAbsorptionRatio() * getDamageAbsorptionRatio();
		int energyPerDamage = ENERGY_PER_DAMAGE;

		int damageLimit = (int) (energyPerDamage > 0 ? 25 * ElectricItem.manager.getCharge(armor) / energyPerDamage : 0);

		return new ISpecialArmor.ArmorProperties(0, absorptionRatio, damageLimit);
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

	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.epic;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		if (this.armorType == HELMET)
			this.itemIcon = par1IconRegister.registerIcon(ResourceParameter.ANTIMATTER_HELMET_TEXTURE);
		else if (this.armorType == CHESTPLATE)
			this.itemIcon = par1IconRegister.registerIcon(ResourceParameter.ANTIMATTER_CHESTPLATE_TEXTURE);
		else if (this.armorType == LEGGINGS)
			this.itemIcon = par1IconRegister.registerIcon(ResourceParameter.ANTIMATTER_LEGGINGS_TEXTURE);
		else if (this.armorType == BOOTS)
			this.itemIcon = par1IconRegister.registerIcon(ResourceParameter.ANTIMATTER_BOOTS_TEXTURE);

	}

	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean p_77624_4_) {
		list.add((int)ElectricItem.manager.getCharge(itemstack)+"/"+(int)this.getMaxCharge(itemstack));
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		return "minecraft:textures/armor/antimatter_layer" + "_" + (this.armorType == 2 ? "2" : "1") + ".png";
	}

	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
		ElectricItem.manager.discharge(stack, damage * ENERGY_PER_DAMAGE, 2147483647, true, false, false);
	}

	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		if (ElectricItem.manager.getCharge(armor) >= ENERGY_PER_DAMAGE) {
			return (int) Math.round(20.0D * getBaseAbsorptionRatio() * getDamageAbsorptionRatio());
		}
		return 0;
	}

	@Override
	public boolean canProvideEnergy(ItemStack itemStack) {
		if (this.armorType == CHESTPLATE)
			return true;
		else
			return false;
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		ItemStack var4 = new ItemStack(this, 1);
		ElectricItem.manager.charge(var4, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false);
		par3List.add(var4);
		par3List.add(new ItemStack(this, 1, this.getMaxDamage()));

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

}
