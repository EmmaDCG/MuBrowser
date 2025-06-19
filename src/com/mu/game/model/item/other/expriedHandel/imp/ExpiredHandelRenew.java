package com.mu.game.model.item.other.expriedHandel.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.other.expriedHandel.ExpiredHandel;
import com.mu.game.model.mall.ShortcutBuy;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.imp.RenewPopup;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;

public class ExpiredHandelRenew extends ExpiredHandel {
   private int shortcutBuyID;
   private String shortcutBuyDes;

   public ExpiredHandelRenew(int modelID, int type, int shortcutBuyID, String shortcutBuyDes) {
      super(modelID, type);
      this.shortcutBuyID = shortcutBuyID;
      this.shortcutBuyDes = shortcutBuyDes;
   }

   public boolean doubleClickHandel(Player player, Item item) {
      RenewPopup pop = new RenewPopup(player.createPopupID(), this.getShortcutBuyID(), this.getShortcutBuyDes());
      ShowPopup.open(player, pop);
      return true;
   }

   public void instantCheckHandel(Player player, Item item, boolean firstEnterMap) {
   }

   public void initCheck() throws Exception {
      if (!ShortcutBuy.hasShortcutBuy(this.shortcutBuyID)) {
         throw new Exception("过期处理 - 续费快捷购买ID不存在");
      }
   }

   public int getShortcutBuyID() {
      return this.shortcutBuyID;
   }

   public void setShortcutBuyID(int shortcutBuyID) {
      this.shortcutBuyID = shortcutBuyID;
   }

   public String getShortcutBuyDes() {
      return this.shortcutBuyDes;
   }

   public void setShortcutBuyDes(String shortcutBuyDes) {
      this.shortcutBuyDes = shortcutBuyDes;
   }
}
