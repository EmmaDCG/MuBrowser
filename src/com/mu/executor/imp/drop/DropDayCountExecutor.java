package com.mu.executor.imp.drop;

import com.mu.db.manager.DropDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import java.util.HashMap;
import java.util.Iterator;

public class DropDayCountExecutor extends Executable {
   public DropDayCountExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      DropDBManager.saveRoleDropMap(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      Player player = (Player)obj[0];
      HashMap worldDayCount = player.getDropManager().getPersonDayDropMap();
      packet.writeLong(player.getID());
      packet.writeShort(worldDayCount.size());
      Iterator var6 = worldDayCount.keySet().iterator();

      while(var6.hasNext()) {
         Integer key = (Integer)var6.next();
         int count = key.intValue();
         packet.writeInt(key.intValue());
         packet.writeInt(count);
      }

   }
}
