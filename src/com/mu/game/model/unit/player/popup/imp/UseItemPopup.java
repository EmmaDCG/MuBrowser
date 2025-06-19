package com.mu.game.model.unit.player.popup.imp;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;

public class UseItemPopup extends Popup {
   private long itemID = -1L;

   public UseItemPopup(int id, int itemID) {
      super(id);
      this.itemID = (long)itemID;
   }

   public String getTitle() {
      return "弹出测试";
   }

   public String getContent() {
      return "你是否真的想要测试弹出?";
   }

   public String getLeftButtonContent() {
      return "确定";
   }

   public String getRightButtonContent() {
      return "取消";
   }

   public void dealLeftClick(Player player) {
   }

   public void dealRightClick(Player player) {
   }

   public boolean isShowAgain(Player player) {
      return true;
   }

   public int getType() {
      return 1;
   }
}
