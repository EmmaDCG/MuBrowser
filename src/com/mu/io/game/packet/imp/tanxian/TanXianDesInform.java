package com.mu.io.game.packet.imp.tanxian;

import com.mu.game.model.rewardhall.RewardItemData;
import com.mu.game.model.tanxian.TanXianConfigManager;
import com.mu.game.model.tanxian.TanXianData;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.List;

public class TanXianDesInform extends ReadAndWritePacket {
   public TanXianDesInform(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      int id = this.readShort();
      Player player = this.getPlayer();
      TanXianData data = TanXianConfigManager.getData(id);
      if (player != null && data != null) {
         this.writeShort(data.getId());
         this.writeUTF(data.getDescription());
         List list = data.getItemList();
         this.writeByte(list.size());

         for(int i = 0; i < list.size(); ++i) {
            GetItemStats.writeItem(((RewardItemData)list.get(i)).getItem(), this);
         }

         player.writePacket(this);
      }
   }
}
