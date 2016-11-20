package manexpen.levelstorage.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;

public class LSDamageSource extends DamageSource {

    public static LSDamageSource energyField = (new LSDamageSource(
            "energyFieldKill"));
    public static LSDamageSource forcefieldArmor = (new LSDamageSource(
            "forcefieldArmor"));
    public static DamageSource forcefieldArmorInstaKill = (DamageSource) ((new LSDamageSource(
            "player"))).setDamageBypassesArmor();
    public static DamageSource teslaRay = (DamageSource) ((new LSDamageSource(
            "teslaHelmetKill"))).setDamageBypassesArmor();
    public static DamageSource disassembled = (DamageSource) ((new LSDamageSource(
            "disassemble"))).setDamageBypassesArmor();


    private String killMessage;

    @Override
    public IChatComponent func_151519_b(EntityLivingBase ELV) {
        if (ELV instanceof EntityPlayer) {
            String s = ELV.getCommandSenderName();
            return new ChatComponentTranslation(this.killMessage, s);
        }
        return new ChatComponentTranslation(ELV.getCommandSenderName(), new Object[]{ELV.func_145748_c_()});
    }

//	@Override
//	public ChatMessageComponent getDeathMessage(
//	        EntityLivingBase par1EntityLivingBase) {
//		if (par1EntityLivingBase instanceof EntityPlayer)
//			return ChatMessageComponent.createFromText(String.format(
//			        StatCollector.translateToLocal(this.killMessage),
//			        ((EntityPlayer) par1EntityLivingBase).username));
//		return ChatMessageComponent.createFromText(this.killMessage);
//	}

    protected LSDamageSource(String par1Str) {
        super(par1Str);
        this.setKillMessage("death." + par1Str);
    }

    private LSDamageSource setKillMessage(String message) {
        this.killMessage = message;
        return this;
    }
}
