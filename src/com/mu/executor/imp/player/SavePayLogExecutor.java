package com.mu.executor.imp.player;

import com.mu.db.manager.PayDBManager;
import com.mu.executor.Executable;
import com.mu.executor.Executor;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class SavePayLogExecutor extends Executable {
   public SavePayLogExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      PayDBManager.intoPayLog(packet.readUTF(), packet.readUTF(), packet.readInt(), packet.readFloat(), packet.readInt(), packet.readUTF(), "", 1, "CNY");
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      String orderId = (String)obj[0];
      String uid = (String)obj[1];
      int sid = ((Integer)obj[2]).intValue();
      float money = ((Float)obj[3]).floatValue();
      int gold = ((Integer)obj[4]).intValue();
      String time = (String)obj[5];
      packet.writeUTF(orderId);
      packet.writeUTF(uid);
      packet.writeInt(sid);
      packet.writeFloat(money);
      packet.writeInt(gold);
      packet.writeUTF(time);
   }

   public static void savePayLog(String order, String uid, int sid, float money, int gold, String time, Player player) {
      WriteOnlyPacket packet = Executor.SavePayLog.toPacket(order, uid, sid, money, gold, time);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }
}
