package com.mu.io.game.packet.imp.dungeon;

import com.mu.db.manager.RecoverDBManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.offline.PlayerDunRecover;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.ArrayList;
import java.util.Iterator;

public class DungeonInitRecover extends ReadAndWritePacket {
   public DungeonInitRecover(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public DungeonInitRecover() {
      super(12024, (byte[])null);
   }

   public static DungeonInitRecover createDungeonInitRecover(long rid) {
      ArrayList listToday = RecoverDBManager.getRecoeverList(rid);
      ArrayList listLast = RecoverDBManager.getLastRecoverList(rid);
      DungeonInitRecover dr = new DungeonInitRecover();

      try {
         dr.writeByte(listToday.size());
         Iterator var6 = listToday.iterator();

         PlayerDunRecover recover;
         while(var6.hasNext()) {
            recover = (PlayerDunRecover)var6.next();
            dr.writeByte(recover.getDunId());
            dr.writeLong(recover.getRecoverDay());
            dr.writeByte(recover.getRecoverTimes());
            dr.writeByte(recover.getRemainderTimes());
         }

         dr.writeByte(listLast.size());
         var6 = listLast.iterator();

         while(var6.hasNext()) {
            recover = (PlayerDunRecover)var6.next();
            dr.writeByte(recover.getDunId());
            dr.writeLong(recover.getRecoverDay());
            dr.writeByte(recover.getRecoverTimes());
            dr.writeByte(recover.getRemainderTimes());
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

      listToday.clear();
      listToday = null;
      listLast.clear();
      listLast = null;
      return dr;
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int sizeToday = this.readByte();

      for(int i = 0; i < sizeToday; ++i) {
         PlayerDunRecover recover = new PlayerDunRecover(this.readUnsignedByte());
         recover.setRecoverDay(this.readLong());
         recover.setRecoverTimes(this.readUnsignedByte());
         recover.setRemainderTimes(this.readUnsignedByte());
         player.getOffLineManager().addRecover(recover);
      }

      int sizeLast = this.readByte();

      for(int i = 0; i < sizeLast; ++i) {
         PlayerDunRecover recover = new PlayerDunRecover(this.readUnsignedByte());
         recover.setRecoverDay(this.readLong());
         recover.setRecoverTimes(this.readUnsignedByte());
         recover.setRemainderTimes(this.readUnsignedByte());
         player.getOffLineManager().addLastRecover(recover);
      }

   }
}
