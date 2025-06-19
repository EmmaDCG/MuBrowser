package com.mu.io.game.packet.imp.drop;

import com.mu.game.CenterManager;
import com.mu.game.model.chat.ChatProcess;
import com.mu.game.model.chat.newlink.NewChatLink;
import com.mu.game.model.drop.model.WellDropManager;
import com.mu.game.model.drop.model.WellShowItem;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.Iterator;
import java.util.List;

public class WellDrop extends ReadAndWritePacket {
   public WellDrop(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public WellDrop() {
      super(20305, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      List itemList = WellDropManager.getWellItemList();
      sendToClient(player, itemList, true);
      itemList.clear();
      itemList = null;
   }

   public static void sendToClient(Player player, List itemList, boolean slef) {
      try {
         WellDrop wd = new WellDrop();
         wd.writeByte(itemList.size());
         Iterator var5 = itemList.iterator();

         while(var5.hasNext()) {
            WellShowItem item = (WellShowItem)var5.next();
            NewChatLink[] link = item.getNewLink();
            ChatProcess.writeNewLinkMessage(item.getNewDes(), link, wd);
         }

         if (slef) {
            player.writePacket(wd);
         } else {
            Iterator it = CenterManager.getAllPlayerIterator();

            while(it.hasNext()) {
               Player p = (Player)it.next();
               p.writePacket(wd);
            }
         }

         wd.destroy();
         wd = null;
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }
}
