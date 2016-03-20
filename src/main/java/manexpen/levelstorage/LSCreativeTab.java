package manexpen.levelstorage;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class LSCreativeTab extends CreativeTabs{

	public LSCreativeTab() {
		super(LevelStorage.MODNAME);
	}

	@Override
	public Item getTabIconItem() {
		return ItemList.AntimatterChestPlate;
	}

}
