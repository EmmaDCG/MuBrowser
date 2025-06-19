package com.mu.io.game.packet.imp.init;

import com.mu.db.manager.ItemDBManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemSaveAide;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.item.container.Container;
import com.mu.game.model.item.operation.ItemOperation;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.Iterator;
import java.util.List;

public class InitItems extends ReadAndWritePacket {
   public InitItems(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public static WriteOnlyPacket initItem(long roleID) {
      List itemList = ItemDBManager.initItemList(roleID);
      WriteOnlyPacket ii = new InitItems(itemList);
      itemList.clear();
      return ii;
   }

   public InitItems(List itemList) {
      super(20000, (byte[])null);

      try {
         this.writeShort(itemList.size());
         Iterator var3 = itemList.iterator();

         while(var3.hasNext()) {
            ItemSaveAide item = (ItemSaveAide)var3.next();
            writeItemSaveAide(this, item);
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void writeItemSaveAide(WriteOnlyPacket packet, ItemSaveAide item) throws Exception {
      packet.writeLong(item.getItemId());
      packet.writeInt(item.getModelID());
      packet.writeByte(item.getQuality());
      packet.writeInt(item.getCount());
      packet.writeShort(item.getSlot());
      packet.writeByte(item.getContainerType());
      packet.writeByte(item.getStarLevel());
      packet.writeByte(item.getSocket());
      packet.writeBoolean(item.isBind());
      packet.writeInt(item.getMoney());
      packet.writeByte(item.getMoneyType());
      packet.writeInt(item.getStarUpTimes());
      packet.writeByte(item.getOnceMaxStarLevel());
      packet.writeLong(item.getExpireTime());
      packet.writeShort(item.getDurability());
      packet.writeUTF(item.getBasisStats());
      packet.writeUTF(item.getOtherStats());
      packet.writeUTF(item.getStones());
      packet.writeUTF(item.getRunes());
      packet.writeByte(item.getZhuijiaLevel());
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int size = this.readShort();

      for(int i = 0; i < size; ++i) {
         ItemSaveAide isa = ItemSaveAide.createSaveAide((ReadAndWritePacket)this);
         Item item = ItemTools.loadItem(isa);
         if (item != null) {
            Container storage = player.getContainer(item.getContainerType());
            if (storage != null) {
               storage.loadItem(item);
               if (storage.getType() == 1) {
                  ItemOperation.specialActionWhenAdd(player, item.getModel(), 1, item.isBind(), 20);
               }
            }
         }
      }

      player.getEquipment().onLoadApplyEquipmentStats();
   }
}
