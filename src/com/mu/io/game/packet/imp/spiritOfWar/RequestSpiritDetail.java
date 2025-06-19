package com.mu.io.game.packet.imp.spiritOfWar;

import com.mu.config.MessageText;
import com.mu.game.model.spiritOfWar.SpiritManager;
import com.mu.game.model.spiritOfWar.SpiritTools;
import com.mu.game.model.spiritOfWar.model.RefineItem;
import com.mu.game.model.spiritOfWar.model.SpiritRankModel;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.Iterator;
import java.util.SortedMap;

public class RequestSpiritDetail extends ReadAndWritePacket {
   public RequestSpiritDetail(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      SpiritManager manager = player.getSpiritManager();
      SpiritRankModel rankModel = SpiritRankModel.getRankModel(manager.getRank());
      this.writeUTF(rankModel.getName());
      this.writeByte(manager.getRank());
      this.writeByte(manager.getLevel());
      this.writeDouble((double)manager.getExp());
      this.writeDouble((double)manager.getModel().getNeedExp());
      this.writeShort(rankModel.getSource());
      this.writeByte(rankModel.getScale());
      this.writeInt(manager.getDomineering());
      this.writeUTF(manager.getStatStr());
      SortedMap items = SpiritTools.getItemMap();
      this.writeByte(items.size());
      Iterator var6 = items.values().iterator();

      while(var6.hasNext()) {
         RefineItem refine = (RefineItem)var6.next();
         GetItemStats.writeItem(refine.getShowItem(), this);
         this.writeInt(manager.getCount(refine.getItemId()));
      }

      int result = manager.canRefine();
      this.writeBoolean(result == 1);
      String des = "";
      if (result == 1) {
         int[] ingots = SpiritTools.getRefineIngot(manager.getIngotRefineCount() + 1);
         des = MessageText.getText(23309);
         des = des.replace("%s%", "" + ingots[0]);
         des = des.replace("%f%", "" + ingots[1]);
      } else {
         des = MessageText.getText(result);
      }

      this.writeUTF(des);
      player.writePacket(this);
   }
}
