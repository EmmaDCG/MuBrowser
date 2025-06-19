package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.GangManager;
import com.mu.game.model.gang.RedPacketInfo;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.redpacket.RedPacketManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.ArrayList;
import java.util.Iterator;

public class SelfRedPacket extends ReadAndWritePacket {
   public SelfRedPacket(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      ArrayList bindList = GangManager.getBindRedList();
      ArrayList ingotList = GangManager.getIngotRedList();
      Player player = this.getPlayer();
      RedPacketManager manager = player.getRedPacketManager();
      this.writeByte(bindList.size());
      Iterator var6 = bindList.iterator();

      int[] in;
      RedPacketInfo info;
      while(var6.hasNext()) {
         in = (int[])var6.next();
         info = GangManager.getRedPacketInfo(in[0]);
         this.writeByte(info.getId());
         this.writeByte(info.getType());
         this.writeUTF(info.getName());
         this.writeUTF(info.getDes());
         int left = player.getUser().getTotleRed(info.getId()) - manager.getBindSendTimes(info.getId());
         this.writeShort(left < 0 ? 0 : left);
      }

      this.writeByte(ingotList.size());
      var6 = ingotList.iterator();

      while(var6.hasNext()) {
         in = (int[])var6.next();
         info = GangManager.getRedPacketInfo(in[0]);
         this.writeByte(info.getId());
         this.writeByte(info.getType());
         this.writeUTF(info.getName());
         this.writeUTF(info.getDes());
         this.writeShort(-1);
      }

      player.writePacket(this);
   }
}
