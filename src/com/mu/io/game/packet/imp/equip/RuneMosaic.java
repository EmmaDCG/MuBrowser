package com.mu.io.game.packet.imp.equip;

import com.mu.config.MessageText;
import com.mu.executor.imp.log.ItemForgingExecutor;
import com.mu.game.model.equip.rune.RuneForgingData;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemRune;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.Rnd;
import java.util.Iterator;

public class RuneMosaic extends ReadAndWritePacket {
   int mosaicCount = 0;

   public RuneMosaic(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long itemID = (long)this.readDouble();
      long runeID = (long)this.readDouble();
      Item item = StrengthEquipment.getForgingItem(player, itemID);
      Item runeItem = player.getBackpack().getItemByID(runeID);
      int result = this.canMosaic(player, item, runeItem);
      boolean sucess = false;
      if (result == 1) {
         RuneForgingData data = RuneForgingData.getData(this.mosaicCount);
         int rnd = Rnd.get(100000);
         if (rnd <= data.getRate()) {
            player.getItemManager().addRune(item, runeItem);
            sucess = true;
         } else {
            player.getItemManager().deleteItem(runeItem, 1, 21);
            SystemMessage.writeMessage(player, MessageText.getText(5050), 5050);
         }

         PlayerManager.reduceMoney(player, data.getMoney());
         ItemForgingExecutor.logFoging(player, item, item.getStarLevel(), 3, sucess, 0, data.getMoney());
      }

      this.writeDouble((double)itemID);
      this.writeBoolean(result == 1);
      this.writeBoolean(sucess);
      player.writePacket(this);
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

   }

   private int canMosaic(Player player, Item item, Item runeItem) {
      if (item != null && item.getCount() >= 1) {
         if (item.isTimeLimited()) {
            return 5068;
         } else if (runeItem != null && runeItem.getCount() >= 1) {
            if (!RuneForgingData.canForging(item.getItemType())) {
               return 5013;
            } else if (runeItem.getItemType() != 161) {
               return 5014;
            } else {
               int canMosaicCount = RuneForgingData.getItemMaxRuneCount(item.getStarLevel());
               if (canMosaicCount < 1) {
                  return 5015;
               } else if (item.getRunes().size() >= canMosaicCount) {
                  return 5033;
               } else {
                  Iterator var6 = item.getRunes().iterator();

                  while(var6.hasNext()) {
                     ItemRune rune = (ItemRune)var6.next();
                     if (rune.getModelID() == runeItem.getModelID()) {
                        return 5017;
                     }
                  }

                  this.mosaicCount = item.getRunes().size() + 1;
                  RuneForgingData data = RuneForgingData.getData(this.mosaicCount);
                  return !PlayerManager.hasEnoughMoney(player, data.getMoney()) ? 1011 : 1;
               }
            }
         } else {
            return 3002;
         }
      } else {
         return 3002;
      }
   }
}
