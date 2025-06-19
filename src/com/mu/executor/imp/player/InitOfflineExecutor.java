package com.mu.executor.imp.player;

import com.mu.db.manager.RecoverDBManager;
import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game.packet.imp.player.offline.InitOfflineSystem;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import com.mu.utils.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class InitOfflineExecutor extends Executable {
   public InitOfflineExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      long rid = packet.readLong();
      long time = packet.readLong();
      Calendar beginCal = Calendar.getInstance();
      beginCal.setTimeInMillis(time);
      beginCal.add(5, -1);
      long begin = Time.getDayLong(beginCal);
      Calendar endCal = Calendar.getInstance();
      endCal.setTimeInMillis(time);
      endCal.add(5, -7);
      long end = Time.getDayLong(endCal);
      InitOfflineSystem is = new InitOfflineSystem();
      ArrayList list = RecoverDBManager.getfinishTimes(rid, end, begin);
      is.writeLong(beginCal.getTimeInMillis());
      is.writeLong(endCal.getTimeInMillis());
      is.writeByte(list.size());
      Iterator var15 = list.iterator();

      while(var15.hasNext()) {
         long[] lo = (long[])var15.next();
         is.writeByte((int)lo[0]);
         is.writeInt((int)lo[1]);
         is.writeLong(lo[2]);
      }

      packet.getG2sChannel().write(is.toBuffer());
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      long rid = ((Long)obj[0]).longValue();
      long inTime = ((Long)obj[1]).longValue();
      packet.writeLong(rid);
      packet.writeLong(inTime);
   }
}
