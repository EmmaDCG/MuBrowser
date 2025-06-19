package com.mu.game.model.item.operation.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.action.ItemAction;
import com.mu.game.model.item.operation.ItemOperation;
import com.mu.game.model.item.operation.OperationResult;
import com.mu.game.model.item.other.ExpiredItemManager;
import com.mu.game.model.task.target.TargetType;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.item.UpdateItemCD;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class UseItemOperation extends ItemOperation {
   private Item item;
   private boolean definite = false;
   private int useNum = 1;
   private int wantedSlot = -1;
   private int result = 1;

   public UseItemOperation(Item item, int useNum, boolean definite) {
      this.item = item;
      this.useNum = useNum;
      this.definite = definite;
   }

   public OperationResult execute() throws Exception {
      int oldCount = this.item.getCount();
      this.result = this.canUse();
      if (this.result == 1) {
         ItemAction action = this.item.getModel().getAction();
         this.result = action.action(this.getPlayer(), this.item, this.useNum, this.definite, this.wantedSlot);
      }

      if (this.result == 1) {
         int cd = this.item.getModel().getCoolTime();
         int actualCd = this.getPlayer().getItemManager().addItemCool(this.item, cd);
         if (cd > 0) {
            UpdateItemCD.sendToClient(this.getPlayer(), this.item.getItemType(), actualCd);
         }

         this.getPlayer().getTaskManager().onEventCheckSpecify(TargetType.SpecifyType.UseItem, this.item.getModelID());
         this.getPlayer().getItemManager().addItemUseCount(this.item.getModelID(), oldCount - this.item.getCount());
      } else {
         boolean handel = false;
         if (this.result == 3044) {
            handel = ExpiredItemManager.handelDoubleClick(this.getPlayer(), this.item);
         }

         if (!handel && needToSendErr(this.getPlayer(), this.result)) {
            SystemMessage.writeMessage(this.getPlayer(), this.result);
         }
      }

      return new OperationResult(this.result, this.item.getID());
   }

   public static boolean needToSendErr(Player player, int result) {
      switch(result) {
      case 3043:
         return false;
      default:
         return true;
      }
   }

   private int canUse() {
      if (this.item == null) {
         return 3002;
      } else if (this.item.getCount() < this.useNum) {
         return 3001;
      } else {
         ItemAction action = this.item.getModel().getAction();
         return action == null ? 3003 : 1;
      }
   }

   public int getWantedSlot() {
      return this.wantedSlot;
   }

   public void setWantedSlot(int wantedSlot) {
      this.wantedSlot = wantedSlot;
   }
}
