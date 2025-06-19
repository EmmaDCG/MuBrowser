package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.gang.RedPacket;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class GetGangRedPacket extends ReadAndWritePacket {
   public GetGangRedPacket(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Gang gang = player.getGang();
      if (gang != null) {
         this.writeByte(GangManager.getDailyMaxBindRedReceive() - player.getRedPacketManager().getTodayBindRedReceive());
         ConcurrentHashMap map = gang.getPacketMap();
         this.writeShort(map.size());
         Iterator it = map.values().iterator();

         while(it.hasNext()) {
            RedPacket rp = (RedPacket)it.next();
            this.writeDouble((double)rp.getPacketId());
            this.writeByte(rp.getRedType());
            this.writeUTF(rp.getRoleName());
            this.writeDouble((double)rp.getSendTime());
            this.writeByte(rp.getLeftSize());
            this.writeBoolean(rp.hasReceived(player));
         }

         player.writePacket(this);
      }
   }
}
