package com.mu.io.game.packet.imp.player;

import com.mu.game.model.stats.StatList2Client;
import com.mu.game.model.unit.Unit;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.attack.CorrectAttributes;
import com.mu.io.game.packet.imp.map.RemoveUnit;
import com.mu.io.game.packet.imp.pkModel.ChangePkView;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.List;

public class PlayerRevival extends ReadAndWritePacket {
   public PlayerRevival(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public PlayerRevival(long roleID) {
      super(10009, (byte[])null);

      try {
         this.writeDouble((double)roleID);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      if (player.isDie()) {
         int result = player.getItemManager().deleteItemByModel(2059, 1, 20).getResult();
         if (result != 1) {
            SystemMessage.writeMessage(player, 3033);
         } else {
            player.revival();
            this.sendPlaceRevival(player);
         }
      }
   }

   public void sendPlaceRevival(Player player) {
      RemoveUnit ru = new RemoveUnit(new Unit[]{player});
      ChangePkView pkView = new ChangePkView(player, true);
      PlayerRevival revival = new PlayerRevival(player.getID());
      player.getMap().sendPacketToAroundPlayer(revival, player, true);
      List statList = StatList2Client.getDieOrRevivalStats();
      CorrectAttributes.sendWhenChange(player, (List)statList);
      statList.clear();
      revival.destroy();
      revival = null;
      pkView.destroy();
      pkView = null;
      ru.destroy();
      ru = null;
   }

   public static void sendToSelf(Player player) {
      PlayerRevival revival = new PlayerRevival(player.getID());
      player.writePacket(revival);
      revival.destroy();
      revival = null;
   }

   public static void revivalCorrect(Player player) {
      List list = StatList2Client.getPlayerPanelStats();
      PlayerAttributes.sendToClient(player, list);
   }

   public static void revival(Player player) {
      player.revival();
      PlayerRevival revival = new PlayerRevival(player.getID());
      player.writePacket(revival);
      revival.destroy();
      revival = null;
   }
}
