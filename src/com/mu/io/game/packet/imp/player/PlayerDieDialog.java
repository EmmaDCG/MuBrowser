package com.mu.io.game.packet.imp.player;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.item.GetItemStats;

public class PlayerDieDialog extends WriteOnlyPacket {
   private static final Item revivalItem = ItemTools.createItem(2059, 1, 2);

   public PlayerDieDialog() {
      super(10010);
   }

   public static void open(Player player, String killerName, int CountDown) {
      try {
         PlayerDieDialog pd = new PlayerDieDialog();
         GetItemStats.writeItem(revivalItem, pd);
         pd.writeShort(CountDown);
         player.writePacket(pd);
         pd.destroy();
         pd = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
