package com.mu.io.game.packet.imp.sys;

import com.mu.game.model.unit.player.Player;
import com.mu.game.qq.pay.QqPayElement;
import com.mu.game.qq.pay.Qqpay;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.Iterator;
import java.util.TreeMap;

public class OpenQqPay extends ReadAndWritePacket {
   public OpenQqPay(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      TreeMap map = Qqpay.getPayMap();
      this.writeByte(map.size());
      Iterator it = map.values().iterator();

      while(it.hasNext()) {
         QqPayElement pe = (QqPayElement)it.next();
         this.writeByte(pe.getId());
         this.writeInt(pe.getIngot());
         this.writeShort(pe.getIcon());
         this.writeInt(pe.getOriginalPrice());
         this.writeInt(pe.getBlueVipProce());
      }

      player.writePacket(this);
   }
}
