package com.mu.io.game.packet.imp.init;

import com.mu.db.manager.GangDBManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.redpacket.RedPacketManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.utils.Time;
import java.util.ArrayList;
import java.util.Iterator;

public class InitRedPacket extends ReadAndWritePacket {
   public InitRedPacket(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public InitRedPacket() {
      super(10051, (byte[])null);
   }

   public static InitRedPacket createInitRedPacket(long rid, String userName, int serverId) {
      InitRedPacket ip = new InitRedPacket();

      try {
         ArrayList list = GangDBManager.getRoleSendBindPacketTimes(userName, serverId);
         ip.writeByte(list.size());
         Iterator var7 = list.iterator();

         while(var7.hasNext()) {
            int[] in = (int[])var7.next();
            ip.writeByte(in[0]);
            ip.writeShort(in[1]);
         }

         int count = GangDBManager.getRoleTodayBindPacketReceiveTimes(rid, Time.getDayLong());
         ip.writeByte(count);
         list.clear();
      } catch (Exception var8) {
         var8.printStackTrace();
      }

      return ip;
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      RedPacketManager manager = player.getRedPacketManager();
      int size = this.readByte();

      for(int i = 0; i < size; ++i) {
         int id = this.readByte();
         int times = this.readUnsignedShort();
         manager.addBindSendTimes(id, times);
      }

      manager.setTodayBindRedReceive(this.readUnsignedByte());
   }
}
