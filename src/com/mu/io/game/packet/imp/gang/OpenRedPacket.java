package com.mu.io.game.packet.imp.gang;

import com.mu.config.Global;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.gang.RedPacket;
import com.mu.game.model.gang.RedPacketReceiveRecord;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.utils.MD5;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class OpenRedPacket extends ReadAndWritePacket {
   public OpenRedPacket(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public OpenRedPacket() {
      super(10640, (byte[])null);
   }

   public static void writePacketInfo(RedPacket rp, Player player) {
      try {
         OpenRedPacket op = new OpenRedPacket();
         op.writeByte(GangManager.getDailyMaxBindRedReceive() - player.getRedPacketManager().getTodayBindRedReceive());
         op.writeDouble((double)rp.getPacketId());
         op.writeByte(rp.getRedType());
         op.writeUTF(rp.getRoleName());
         op.writeDouble((double)rp.getSendTime());
         op.writeByte(rp.getLeftSize());
         op.writeBoolean(rp.hasReceived(player));
         op.writeInt(rp.getTotalIngot());
         ConcurrentHashMap recordMap = rp.getRecordMap();
         op.writeByte(recordMap.size());
         Iterator it = recordMap.values().iterator();

         while(it.hasNext()) {
            RedPacketReceiveRecord record = (RedPacketReceiveRecord)it.next();
            op.writeDouble((double)record.getRid());
            op.writeUTF(record.getRoleName());
            op.writeInt(record.getReceiveIngot());
            op.writeDouble((double)record.getReceiveTime());
         }

         player.writePacket(op);
         op.destroy();
         op = null;
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Gang gang = player.getGang();
      long id = (long)this.readDouble();
      if (MD5.md5s(id + Global.getGameverifykey()).equals(this.readUTF())) {
         if (gang != null) {
            gang.doOperation(player, 19, id);
         }

      }
   }
}
