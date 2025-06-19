package com.mu.executor.imp.buff;

import com.mu.db.manager.BuffDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.stats.PersistentState;
import com.mu.game.model.unit.buff.Buff;
import com.mu.game.model.unit.buff.BuffManager;
import com.mu.game.model.unit.buff.model.BuffDBEntry;
import com.mu.game.model.unit.buff.model.BuffModel;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SaveBuffWhenOffLineExecutor extends Executable {
   public SaveBuffWhenOffLineExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      BuffDBManager.saveWhenOffLine(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      Player player = (Player)obj[0];
      BuffManager buffManager = player.getBuffManager();
      long now = System.currentTimeMillis();
      List entries = null;
      Iterator var9 = buffManager.getBuffs().values().iterator();

      while(true) {
         Buff buff;
         BuffModel model;
         do {
            do {
               if (!var9.hasNext()) {
                  packet.writeLong(player.getID());
                  if (entries != null) {
                     packet.writeByte(entries.size());
                     var9 = entries.iterator();

                     while(var9.hasNext()) {
                        BuffDBEntry entry = (BuffDBEntry)var9.next();
                        packet.writeInt(entry.getBuffModelID());
                        packet.writeShort(entry.getLevel());
                        packet.writeLong(entry.getDuration());
                        packet.writeUTF(entry.getPropStr());
                     }

                     entries.clear();
                     entries = null;
                  } else {
                     packet.writeByte(0);
                  }

                  return;
               }

               buff = (Buff)var9.next();
               model = buff.getModel();
            } while(model.isLogouAway());
         } while(buff.isEnd(now) && buff.getPersistentState() != PersistentState.UPDATED);

         BuffDBEntry entry = new BuffDBEntry();
         entry.setBuffModelID(buff.getModelID());
         entry.setLevel(buff.getLevel());
         entry.setDuration(getRemainTime(buff, now));
         entry.setPropStr(buff.getBuffPropStr());
         if (entries == null) {
            entries = new ArrayList();
         }

         entries.add(entry);
      }
   }

   public static long getRemainTime(Buff buff, long now) {
      long remainTime = buff.getEndTime() == -1L ? -1L : buff.getEndTime() - now;
      if (buff.getDuration() != -1L && remainTime < 1L) {
         remainTime = 0L;
      }

      return remainTime;
   }

   public static int getRemainTimeForClient(Buff buff, long now) {
      long remainTime = buff.getEndTime() == -1L ? -1L : buff.getEndTime() - now;
      if (buff.getDuration() != -1L) {
         if (remainTime < 1L) {
            remainTime = 0L;
         } else {
            remainTime /= 1000L;
         }
      }

      return (int)remainTime;
   }
}
