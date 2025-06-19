package com.mu.game.model.activity.imp.kfhd.boss;

import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.monster.worldboss.WorldBossData;
import com.mu.game.model.unit.monster.worldboss.WorldBossManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.ArrayList;
import java.util.Iterator;

public class ActivityBossElement extends ActivityElement {
   private int header;

   public ActivityBossElement(int id, ActivityBoss ab) {
      super(id, ab);
   }

   public void writeDetail(Player player, WriteOnlyPacket packet) throws Exception {
      packet.writeShort(this.getId());
      WorldBossData wd = WorldBossManager.getBossData(this.getNumerical());
      packet.writeShort(wd.getBossId());
      packet.writeByte(wd.getBossType());
      packet.writeUTF(wd.getName());
      packet.writeShort(this.header);
      ArrayList list = this.getRewardList();
      packet.writeByte(list.size());
      Iterator var6 = list.iterator();

      while(var6.hasNext()) {
         Item item = (Item)var6.next();
         GetItemStats.writeItem(item, packet);
      }

      packet.writeByte(this.getReceiveStatus(player));
   }

   public boolean canReceive(Player player, boolean notice) {
      return !((ActivityBoss)this.getFather()).hasKilled(this.getNumerical(), player.getID()) ? false : super.canReceive(player, notice);
   }

   public int getReceiveStatus(Player player) {
      if (!((ActivityBoss)this.getFather()).hasKilled(this.getNumerical(), player.getID())) {
         return 0;
      } else {
         return this.receiveOverload(player) ? 2 : 1;
      }
   }

   public int getHeader() {
      return this.header;
   }

   public void setHeader(int header) {
      this.header = header;
   }
}
