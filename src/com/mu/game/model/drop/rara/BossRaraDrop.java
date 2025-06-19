package com.mu.game.model.drop.rara;

import com.mu.config.MessageText;
import com.mu.game.model.chat.link.CharactorLink;
import com.mu.game.model.chat.link.ItemLink;
import com.mu.game.model.item.Item;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.utils.Time;

public class BossRaraDrop extends RaraDrop {
   private String bossName;
   private String mapName;

   public BossRaraDrop(Item item, long playerId, String playerName, long dropTime, String bossName, String mapName) {
      super(item, playerId, playerName, dropTime);
      this.bossName = bossName;
      this.mapName = mapName;
   }

   public String toLinkStr() {
      String content = MessageText.getText(11003);
      content = content.replace("%b%", this.bossName);
      content = content.replace("%m%", this.mapName);
      content = content.replace("%t%", Time.getTimeStr(this.getTime(), "MM-dd HH:mm:ss"));
      return content;
   }

   public void writeDetail(WriteOnlyPacket packet) throws Exception {
      String content = this.toLinkStr();
      CharactorLink playerLink = new CharactorLink(0, this.getPlayerId(), this.getPlayerName(), 0, false);
      ItemLink itemLink = new ItemLink(1, this.getItem().getID(), this.getItem().getName(), this.getItem().getQuality(), false);
      content = content.replace("%p%", playerLink.getContent());
      content = content.replace("%i%", itemLink.getContent());
      packet.writeUTF(content);
      packet.writeByte(2);
      packet.writeByte(playerLink.getType());
      playerLink.writeDetail(packet);
      packet.writeByte(itemLink.getType());
      itemLink.writeDetail(packet);
   }

   @Override
   public int compareTo(Object o) {
      return 0;
   }
}
