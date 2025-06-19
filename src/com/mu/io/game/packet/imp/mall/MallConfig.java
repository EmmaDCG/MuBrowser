package com.mu.io.game.packet.imp.mall;

import com.mu.game.model.mall.MallConfigManager;
import com.mu.game.model.mall.MallItemData;
import com.mu.game.model.mall.MallLabelData;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.List;

public class MallConfig extends ReadAndWritePacket {
   public MallConfig(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      sendMsgMallConfig(this.getPlayer());
   }

   public static void sendMsgMallConfig(Player player) {
      try {
         MallConfig packet = new MallConfig(45101, (byte[])null);
         MallLabelData[] labelArr = MallConfigManager.getLabelArr();
         List cxList = MallConfigManager.getCXList(player.getProType());
         packet.writeByte(labelArr.length);

         int i;
         for(i = 0; i < labelArr.length; ++i) {
            List list = labelArr[i].getItemList();
            packet.writeUTF(labelArr[i].getName());
            packet.writeByte(labelArr[i].getPriceType());
            packet.writeByte(list.size());

            for(int j = 0; j < list.size() && j < 32767; ++j) {
               MallItemData mid = (MallItemData)list.get(j);
               GetItemStats.writeItem(mid.getItem(), packet);
               packet.writeInt(mid.getPrice1());
               packet.writeByte(mid.getVipLevel());
            }
         }

         packet.writeByte(cxList == null ? 0 : cxList.size());

         for(i = 0; cxList != null && i < cxList.size() && i < 32767; ++i) {
            MallItemData mid = (MallItemData)cxList.get(i);
            GetItemStats.writeItem(mid.getItem(), packet);
         }

         player.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }
}
