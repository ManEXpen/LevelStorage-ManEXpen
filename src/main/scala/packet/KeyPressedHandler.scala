package packet

import cpw.mods.fml.common.network.simpleimpl.{IMessage, IMessageHandler, MessageContext}
import io.netty.buffer.ByteBuf
import manexpen.levelstorage.api.{EnumKey, IKeyHandler}

/**
  * Created by manex on 2016/11/17.
  */
class KeyPressedHandler extends IMessageHandler[MessageKeyPressed, IMessage] {
  override def onMessage(message: MessageKeyPressed, ctx: MessageContext): IMessage = {
    var cnt = 0
    ctx.getServerHandler.playerEntity.inventory.armorInventory.withFilter(stack => stack != null && stack.getItem.isInstanceOf[IKeyHandler]).foreach(stack => {
      println(stack.toString + cnt)
      cnt = cnt + 1
      //stack.getItem.asInstanceOf[IKeyHandler].onRecieveKeyPacket(ctx.getServerHandler.playerEntity.worldObj, ctx.getServerHandler.playerEntity, stack, message.keyType)
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
