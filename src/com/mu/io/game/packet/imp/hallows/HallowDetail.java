package com.mu.io.game.packet.imp.hallows;

import com.mu.config.MessageText;
import com.mu.game.model.hallow.HallowManager;
import com.mu.game.model.hallow.model.HallowModel;
import com.mu.game.model.hallow.model.PartModel;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.GetItemStats;

public class HallowDetail extends ReadAndWritePacket {
   public HallowDetail(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public HallowDetail() {
      super(20602, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      this.setData(player);
      player.writePacket(this);
   }

   public void setData(Player player) {
      try {
         HallowManager manager = player.getHallowsManager();
         HallowModel model = HallowModel.getModel(manager.getRank());
         this.writeUTF(model.getName());
         this.writeByte(model.getRank());
         this.writeShort(model.getView3D());
         this.writeByte(model.getScale());
         this.writeInt(manager.getCurPartModel().getDomineering());
         this.writeInt(manager.getLevel());
         this.writeInt(model.getMaxLevel());
         PartModel partModel = null;
         int canRankUp = manager.canRankUp();
         this.writeBoolean(canRankUp == 1);
         if (canRankUp == 1) {
            partModel = PartModel.getPartModel(manager.getRank() + 1, 0);
            this.writeUTF(partModel.getShowStat());
         } else {
            int canLevelUp = manager.canLevelUp(true);
            this.writeBoolean(canLevelUp == 1);
            if (canLevelUp == 1) {
               partModel = PartModel.getPartModel(manager.getRank(), manager.getLevel() + 1);
               this.writeUTF(partModel.getShowStat());
               GetItemStats.writeItem(partModel.getItem(), this);
            } else {
               String des = null;
               if (manager.getLevel() < manager.getModel().getMaxLevel()) {
                  des = MessageText.getText(canLevelUp);
               } else if (canRankUp == 23403) {
                  des = MessageText.getText(23406).replace("%s%", "" + manager.getModel().getTreasureLevel());
               } else {
                  des = MessageText.getText(canRankUp);
               }

               this.writeUTF(des);
            }
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public static void sendToClient(Player player) {
      try {
         HallowDetail hd = new HallowDetail();
         hd.setData(player);
         player.writePacket(hd);
         hd.destroy();
         hd = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
