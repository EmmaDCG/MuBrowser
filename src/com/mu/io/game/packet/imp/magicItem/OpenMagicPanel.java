package com.mu.io.game.packet.imp.magicItem;

import com.mu.game.model.item.box.magic.MagicAtom;
import com.mu.game.model.item.box.magic.MagicManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.Iterator;
import java.util.List;

public class OpenMagicPanel extends ReadAndWritePacket {
   public OpenMagicPanel(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      List showList = MagicManager.getShowItems();
      this.writeByte(showList.size());
      Iterator var4 = showList.iterator();

      while(var4.hasNext()) {
         MagicAtom item = (MagicAtom)var4.next();
         GetItemStats.writeItem(item.getShowItem(), this);
      }

      int ingotItemCount = player.getBackpack().getItemCount(MagicManager.getIngotItemID());
      this.writeInt(ingotItemCount);
      Iterator var5 = MagicManager.getIntegralMap().keySet().iterator();

      while(var5.hasNext()) {
         Integer count = (Integer)var5.next();
         this.writeInt(MagicManager.getEveryIngot() * count.intValue());
      }

      player.writePacket(this);
   }
}
