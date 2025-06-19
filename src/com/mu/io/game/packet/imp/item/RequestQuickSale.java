package com.mu.io.game.packet.imp.item;

import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RequestQuickSale extends ReadAndWritePacket {
   public RequestQuickSale(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      List itemList = new ArrayList();
      player.getBackpack().getQuickSaleEquip(player, itemList, 24, player.getGameHang().getHangSale());
      int money = 0;
      this.writeShort(itemList.size());

      Item item;
      for(Iterator var5 = itemList.iterator(); var5.hasNext(); money += item.getMoney()) {
         item = (Item)var5.next();
         this.writeDouble((double)item.getID());
      }

      this.writeInt(money);
      player.writePacket(this);
      itemList.clear();
      itemList = null;
   }
}
