package com.mu.game.top;

import com.mu.game.CenterManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.bluevip.BlueVip;

public abstract class TopInfo {
   private long rid;
   private String name;
   private int level;
   private int professionId;
   private String vipTag;

   public long getRid() {
      return this.rid;
   }

   public void setRid(long rid) {
      this.rid = rid;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public int getProfessionId() {
      return this.professionId;
   }

   public int[] getVipIcons() {
      Player player = CenterManager.getPlayerByRoleID(this.rid);
      return player != null ? player.getVipIcons() : BlueVip.getBlueIcon(this.vipTag).getIcons();
   }

   public void setVipTag(String vipTag) {
      this.vipTag = vipTag;
   }

   public void setProfessionId(int professionId) {
      this.professionId = professionId;
   }

   public abstract String getvariable();
}
