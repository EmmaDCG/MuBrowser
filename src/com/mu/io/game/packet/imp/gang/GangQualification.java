package com.mu.io.game.packet.imp.gang;

import com.mu.game.dungeon.DungeonManager;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.gang.GangWarRankInfo;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.utils.Time;
import java.util.ArrayList;
import java.util.Iterator;

public class GangQualification extends ReadAndWritePacket {
   public GangQualification(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      ArrayList list = GangManager.getGangWarRankInfoList(Time.getDayLong());
      boolean isComfirm = false;
      if (list != null && DungeonManager.getLuolanManager().isOnTheMarch()) {
         isComfirm = true;
         this.writeDetail(list);
      } else {
         list = GangManager.getRealtimeInfoList();
         this.writeDetail(list);
      }

      this.writeBoolean(!isComfirm);
      String des = DungeonManager.getLuolanManager().getTemplate().getQualification2();
      if (!isComfirm) {
         des = DungeonManager.getLuolanManager().getTemplate().getQualification1();
         long beginTime = DungeonManager.getLuolanManager().getOpenDate().getTime();
         int leftTime = (int)((beginTime - System.currentTimeMillis()) / 1000L);
         this.writeInt(leftTime);
         Gang gang = this.getPlayer().getGang();
         this.writeInt(gang == null ? 0 : gang.getMemberTotalLevel());
      }

      this.writeUTF(des);
      this.getPlayer().writePacket(this);
   }

   private void writeDetail(ArrayList list) {
      try {
         this.writeByte(list.size());
         Iterator var3 = list.iterator();

         while(var3.hasNext()) {
            GangWarRankInfo info = (GangWarRankInfo)var3.next();
            this.writeDouble((double)info.getId());
            this.writeUTF(info.getName());
            this.writeInt(info.getTotalLevel());
            this.writeShort(info.getFlag());
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
