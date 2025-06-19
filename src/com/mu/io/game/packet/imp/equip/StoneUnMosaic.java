package com.mu.io.game.packet.imp.equip;

import com.mu.game.model.equip.equipStat.EquipStat;
import com.mu.game.model.equip.newStone.StoneDataManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemStone;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.imp.UnMosaicStonePopup;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class StoneUnMosaic extends ReadAndWritePacket {
   public StoneUnMosaic() {
      super(20204, (byte[])null);
   }

   public StoneUnMosaic(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long itemID = (long)this.readDouble();
      int index = this.readByte();
      Item item = StrengthEquipment.getForgingItem(player, itemID);
      int result = canUnMosaic(player, item, index);
      if (result == 1) {
         ItemStone stone = item.getStoneById(index);
         String name = ItemModel.getModel(stone.getModelID()).getName() + ": " + StoneDataManager.assemStoneStat(EquipStat.getEquipStat(stone.getEquipStatID()));
         UnMosaicStonePopup popup = new UnMosaicStonePopup(player.createPopupID(), itemID, index, name);
         ShowPopup.open(player, popup);
      }

      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

   }

   public static void doUnMosaic(Player player, long itemID, int index) {
      Item item = StrengthEquipment.getForgingItem(player, itemID);
      int result = canUnMosaic(player, item, index);
      if (result == 1) {
         result = player.getItemManager().updateItemDeleteStone(item, index).getResult();
      }

      sendToClient(player, itemID, result, index);
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

   }

   public static int canUnMosaic(Player player, Item item, int index) {
      if (item != null && item.getCount() >= 1) {
         if (index != -1) {
            ItemStone stone = item.getStoneById(index);
            if (stone == null) {
               return 5023;
            }
         }

         return 1;
      } else {
         return 3002;
      }
   }

   public static void sendToClient(Player player, long itemID, int result, int index) {
      try {
         StoneUnMosaic sum = new StoneUnMosaic();
         sum.writeBoolean(result == 1);
         sum.writeDouble((double)itemID);
         if (result == 1) {
            sum.writeByte(index);
         }

         player.writePacket(sum);
         sum.destroy();
         sum = null;
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }
}
