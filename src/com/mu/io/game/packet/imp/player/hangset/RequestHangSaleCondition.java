package com.mu.io.game.packet.imp.player.hangset;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.hang.SaleCondition;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.Iterator;
import java.util.List;

public class RequestHangSaleCondition extends ReadAndWritePacket {
   public RequestHangSaleCondition(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      List scList = SaleCondition.getStarLevelCondition();
      this.writeByte(scList.size());
      Iterator var4 = scList.iterator();

      while(var4.hasNext()) {
         String[] sc = (String[])var4.next();
         this.writeUTF(sc[1]);
      }

      List zjList = SaleCondition.getZhuijiaLevelCondition();
      this.writeByte(zjList.size());
      Iterator var5 = zjList.iterator();

      while(var5.hasNext()) {
         String[] zj = (String[])var5.next();
         this.writeUTF(zj[1]);
      }

      player.writePacket(this);
   }
}
