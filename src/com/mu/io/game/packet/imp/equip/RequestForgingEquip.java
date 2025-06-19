package com.mu.io.game.packet.imp.equip;

import com.mu.game.model.equip.horseFusion.HorseFusionSort;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RequestForgingEquip extends ReadAndWritePacket {
   public static final int Forging_Strength = 0;
   public static final int Forging_Zhuijia = 1;
   public static final int Forging_Stone = 2;
   public static final int Forging_RuneMosaic = 3;
   public static final int Forging_RuneInherite = 4;
   public static final int Forging_HorseFusion = 5;
   public static final int Forging_Upgrade = 6;

   public RequestForgingEquip(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int forgingType = this.readByte();
      List itemList = new ArrayList();
      player.getEquipment().filterForgingItem(itemList, forgingType);
      player.getBackpack().filterForgingItem(itemList, forgingType);
      if (forgingType == 5) {
         Collections.sort(itemList, new HorseFusionSort());
      }

      this.writeByte(forgingType);
      this.writeShort(itemList.size());
      Iterator var5 = itemList.iterator();

      while(var5.hasNext()) {
         Item item = (Item)var5.next();
         this.writeDouble((double)item.getID());
      }

      player.writePacket(this);
      itemList.clear();
      itemList = null;
   }

   public static boolean filter(Item item, int forgingType) {
      if (item.isEquipment() && !item.isTimeLimited()) {
         int result = 1;
         switch(forgingType) {
         case 0:
            result = RequestStrength.canStrength(item);
            break;
         case 1:
            result = RequestZhuija.canZhuijia(item);
            break;
         case 2:
            result = RequestStoneMosaic.canMosaic(item);
            break;
         case 3:
            result = RequestRuneMosaic.canMosaic(item);
            break;
         case 4:
            result = RequestRuneInherit.canInherit(item);
            break;
         case 5:
            result = RequestHorseFuse.canFuse(item);
            break;
         case 6:
            result = RequestUpgrade.canUpgrade(item);
         }

         return result == 1;
      } else {
         return false;
      }
   }

   public static String getForgingName(int type) {
      switch(type) {
      case 0:
         return "强化";
      case 1:
         return "追加";
      case 2:
         return "宝石镶嵌";
      case 3:
      case 4:
         return "符文转移";
      case 5:
         return "坐骑融合";
      case 6:
         return "装备升级";
      default:
         return "锻造";
      }
   }
}
