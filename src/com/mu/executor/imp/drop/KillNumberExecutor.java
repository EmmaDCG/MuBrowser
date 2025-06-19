package com.mu.executor.imp.drop;

import com.mu.db.manager.DropDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.drop.model.KillRecord;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class KillNumberExecutor extends Executable {
   public KillNumberExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      DropDBManager.saveKillNumber(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      Player player = (Player)obj[0];
      HashMap records = player.getDropManager().getKillRecordMap();
      List krList = new ArrayList();
      Iterator var7 = records.values().iterator();

      KillRecord kr;
      while(var7.hasNext()) {
         kr = (KillRecord)var7.next();
         if (kr.isChange()) {
            krList.add(kr);
         }
      }

      packet.writeLong(player.getID());
      packet.writeShort(krList.size());
      var7 = krList.iterator();

      while(var7.hasNext()) {
         kr = (KillRecord)var7.next();
         packet.writeInt(kr.getTemplateID());
         packet.writeInt(kr.getNumber());
      }

      krList.clear();
      krList = null;
   }

   public static boolean needSave(Player player) {
      HashMap records = player.getDropManager().getKillRecordMap();
      Iterator var3 = records.values().iterator();

      while(var3.hasNext()) {
         KillRecord record = (KillRecord)var3.next();
         if (record.isChange()) {
            return true;
         }
      }

      return false;
   }
}
