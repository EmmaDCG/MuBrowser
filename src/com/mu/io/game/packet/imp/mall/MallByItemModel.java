package com.mu.io.game.packet.imp.mall;

import com.mu.game.model.mall.MallConfigManager;
import com.mu.game.model.mall.MallItemData;
import com.mu.io.game.packet.ReadAndWritePacket;

public class MallByItemModel extends ReadAndWritePacket {
   public MallByItemModel(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      int modelId = this.readInt();
      int count = this.readInt();
      MallItemData data = MallConfigManager.getData(modelId);
      if (data != null) {
         MallShowItem.show(this.getPlayer(), data.getItem().getID(), count);
      }

   }
}
