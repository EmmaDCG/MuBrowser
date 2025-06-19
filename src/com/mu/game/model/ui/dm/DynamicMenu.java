package com.mu.game.model.ui.dm;

import com.mu.game.model.fo.FunctionOpen;
import com.mu.game.model.fo.FunctionOpenManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.sys.OpenMenuPanel;

public abstract class DynamicMenu {
   protected int icons = -1;
   private int id;
   private String name;
   private int type = 1;
   private int position = 1;
   private int row;
   protected int[] panelId;
   protected String tips = "";
   private int hg = 1;
   private int sg = 1;
   private int showLevel = 1;
   private int openFunctionId = -1;

   public DynamicMenu(int menuId) {
      this.id = menuId;
   }

   public int getRow() {
      return this.row;
   }

   public void setRow(int row) {
      this.row = row;
   }

   public int getIcons() {
      return this.icons;
   }

   public void setIcons(int icons) {
      this.icons = icons;
   }

   public int getHg() {
      return this.hg;
   }

   public void setHg(int hg) {
      this.hg = hg;
   }

   public int getSg() {
      return this.sg;
   }

   public void setSg(int sg) {
      this.sg = sg;
   }

   public String getTips() {
      return this.tips;
   }

   public void setTips(String tips) {
      this.tips = tips;
   }

   public String getName() {
      return this.name;
   }

   public int[] getPanelId() {
      return this.panelId;
   }

   public void setPanelId(int[] panelId) {
      this.panelId = panelId;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }

   public int getPosition() {
      return this.position;
   }

   public void setPosition(int position) {
      this.position = position;
   }

   public int getOpenFunctionId() {
      return this.openFunctionId;
   }

   public void setOpenFunctionId(int openFunctionId) {
      this.openFunctionId = openFunctionId;
   }

   public int getShowLevel() {
      return this.showLevel;
   }

   public void setShowLevel(int showLevel) {
      this.showLevel = showLevel;
   }

   public int getId() {
      return this.id;
   }

   public boolean isShow(Player player) {
      if (this.showLevel > player.getLevel()) {
         return false;
      } else if (this.openFunctionId != -1) {
         FunctionOpen fo = FunctionOpenManager.getFunctionOpen(this.openFunctionId);
         return fo == null ? false : fo.isOpen(player);
      } else {
         return true;
      }
   }

   public abstract int getShowNumber(Player var1);

   public abstract boolean hasEffect(Player var1, int var2);

   public void onClick(Player player) {
      OpenMenuPanel.open(player, this.panelId[0], this.panelId[1]);
   }
}
