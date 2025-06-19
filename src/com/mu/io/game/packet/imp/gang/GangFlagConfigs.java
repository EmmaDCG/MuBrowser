package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.GangManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class GangFlagConfigs extends ReadAndWritePacket {
   public GangFlagConfigs(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      HashMap flagMap = GangManager.getFlagMap();
      this.writeByte(flagMap.size());
      Iterator it = flagMap.entrySet().iterator();

      while(it.hasNext()) {
         Entry entry = (Entry)it.next();
         this.writeShort(((Integer)entry.getKey()).intValue());
      }

      this.writeInt(GangManager.getCreateNeedMoney());
      this.getPlayer().writePacket(this);
   }
}
