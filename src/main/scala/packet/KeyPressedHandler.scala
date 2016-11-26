package packet

import cpw.mods.fml.common.network.simpleimpl.{IMessage, IMessageHandler, MessageContext}
import io.netty.buffer.ByteBuf
import manexpen.levelstorage.api.EnumKey
import manexpen.levelstorage.armor.antimatter._

/**
  * Created by manex on 2016/11/17.
  */
class KeyPressedHandler extends IMessageHandler[MessageKeyPressed, IMessage] {

  override def onMessage(message: MessageKeyPressed, ctx: MessageContext): IMessage = {

    val world = ctx.getServerHandler.playerEntity.worldObj
    val player = ctx.getServerHandler.playerEntity
    val enumKey = message.keyType


    //なぜか最後まで子クラスにキャストしないとNoSuchMethodErrorで落ちる
    player.inventory.armorInventory
      .withFilter(_ != null)
      .foreach(itemStack =>
        itemStack.getItem match {
          case _: ItemArmorAntimatterHelmet => itemStack.getItem.asInstanceOf[ItemArmorAntimatterHelmet].onRecieveKeyPacket(world, player, itemStack, enumKey)
          case _: ItemArmorAntimatterChestPlate => itemStack.getItem.asInstanceOf[ItemArmorAntimatterChestPlate].onRecieveKeyPacket(world, player, itemStack, enumKey)
          case _: ItemArmorAntimatterLeggings => itemStack.getItem.asInstanceOf[ItemArmorAntimatterLeggings].onRecieveKeyPacket(world, player, itemStack, enumKey)
          case _: ItemArmorAntimatterBoots => itemStack.getItem.asInstanceOf[ItemArmorAntimatterBoots].onRecieveKeyPacket(world, player, itemStack, enumKey)
          case _ =>
        })
    null
  }
}

class MessageKeyPressed extends IMessage {
  var keyType: EnumKey = _

  def this(keyType: EnumKey) = {
    this
    this.keyType = keyType
  }

  override def toBytes(buf: ByteBuf): Unit = {
    buf.writeInt(keyType.keyType)
  }

  override def fromBytes(buf: ByteBuf): Unit = {
    this.keyType = EnumKey.getKeyByID(buf.readInt())
  }
}
