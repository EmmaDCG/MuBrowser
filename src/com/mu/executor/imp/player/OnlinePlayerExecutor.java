package com.mu.executor.imp.player;

import com.mu.db.manager.PlayerDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class OnlinePlayerExecutor extends Executable {
   public OnlinePlayerExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      PlayerDBManager.savePlayerVolatileInfo(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) {
      try {
         Player player = (Player)obj[0];
         packet.writeLong(player.getID());
         packet.writeLong(player.getCurretntExp());
         packet.writeInt(player.getWorldMapID());
         packet.writeInt(player.getWorldMapPoint().x);
         packet.writeInt(player.getWorldMapPoint().y);
         packet.writeLong(player.getLogoutTime());
         packet.writeInt(player.getSd());
         packet.writeInt(player.getTodayOnlineTime());
         packet.writeInt(player.getHp());
         packet.writeInt(player.getMp());
         packet.writeInt(player.getVIPExp());
         packet.writeByte(player.getPkModelIDToDB());
         packet.writeInt(player.getEvil());
         packet.writeInt(player.getMoney());
         packet.writeInt(player.getAg());
         packet.writeInt(player.getTotalOnlineTime());
         packet.writeInt(player.getStatValue(StatEnum.DOMINEERING));
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
