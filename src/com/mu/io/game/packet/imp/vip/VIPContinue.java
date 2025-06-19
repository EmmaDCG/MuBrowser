package com.mu.io.game.packet.imp.vip;

import com.mu.game.model.mall.MallConfigManager;
import com.mu.game.model.mall.MallItemData;
import com.mu.game.model.vip.VIPConfigManager;
import com.mu.game.model.vip.VIPData;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.Iterator;

public class VIPContinue extends ReadAndWritePacket {
   public VIPContinue(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      this.writeByte(VIPConfigManager.getVIPSize());
      Iterator it = VIPConfigManager.getVIPIterator();

      while(it.hasNext()) {
         VIPData data = (VIPData)it.next();
         MallItemData md = MallConfigManager.getData(data.getItemId());
         GetItemStats.writeItem(md.getItem(), this);
      }

      this.getPlayer().writePacket(this);
   }
}
