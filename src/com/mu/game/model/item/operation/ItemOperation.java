package com.mu.game.model.item.operation;

import com.mu.db.log.atom.ItemLogAtom;
import com.mu.executor.imp.log.ItemLogExecutor;
import com.mu.game.model.equip.compositenew.CompositeGuideModel;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.action.ItemAction;
import com.mu.game.model.item.container.Container;
import com.mu.game.model.item.container.imp.Storage;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.packet.ItemPacketService;
import com.mu.game.model.restrict.Restriction;
import com.mu.game.model.task.target.TargetType;
import com.mu.game.model.transaction.TransactionManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.extarget.ExtargetManager;
import com.mu.game.model.unit.player.tips.SystemFunctionTipConfig;
import com.mu.io.game.packet.imp.equip.DurabilityChange;
import com.mu.io.game.packet.imp.item.AddItem;
import com.mu.io.game.packet.imp.item.DeleteItem;
import com.mu.io.game.packet.imp.item.UpdateCount;
import com.mu.io.game.packet.imp.player.tips.SystemFunctionTip;
import com.mu.io.game.packet.imp.sys.RightMessage;

public abstract class ItemOperation {
   public static final int UseType_Common = 0;
   public static final int UseType_GetUse = 1;
   public static final int UseType_GetUseInBackpack = 2;
   private Player player = null;

   public abstract OperationResult execute() throws Exception;

   public void destroy() {
      this.player = null;
   }

   protected void doAddToContainerAction(Item item, int slot, Container container, int source) {
      if (this.needToCloseTransaction(source) && Restriction.isInTranction(item)) {
         TransactionManager.closeTransaction(this.player.getID());
      }

      item.setHide(false);
      container.putToContainerBySlot(item, slot);
      ItemPacketService.noticeGatewayAddItem(this.player, item);
      AddItem.sendToClient(this.player, item);
      switch(container.getType()) {
      case 1:
         this.player.getTaskManager().onEventCheckSpecify(TargetType.SpecifyType.CollectionItem, item.getModelID());
         this.player.getTaskManager().onEventCheckMoreSpecify(TargetType.MoreSpecifyType.ITEM);
         itemUsageTips(this.player, item);
         RightMessage.pushItemRightMessage(this.player, item);
         ExtargetManager.checkCollect(this.player, item);
         if (container.getVacantSize() < 3) {
            SystemFunctionTip.sendToClient(this.player, 5, Integer.valueOf(-1));
         }

         if (this.isNeedToLog(source)) {
            ItemLogExecutor.createLogPacket(this.getPlayer(), item, ItemLogAtom.Type_Add, item.getCount(), source);
         }

         CompositeGuideModel.doGuide(this.player, item);
      case 4:
      default:
         break;
      case 14:
         if (this.isNeedToLog(source)) {
            ItemLogExecutor.createLogPacket(this.getPlayer(), item, ItemLogAtom.Type_Add, item.getCount(), source);
         }
      }

   }

   public boolean needOpenPanel(int source) {
      switch(source) {
      case 12:
      case 15:
      case 16:
      case 20:
         return false;
      case 13:
      case 14:
      case 17:
      case 18:
      case 19:
      default:
         return true;
      }
   }

   public boolean isNeedToLog(int source) {
      switch(source) {
      case 12:
      case 15:
      case 16:
      case 17:
         return false;
      case 13:
      case 14:
      default:
         return true;
      }
   }

   public boolean needToCloseTransaction(int source) {
      switch(source) {
      case 4:
         return false;
      default:
         return true;
      }
   }

   public boolean isTransaction(int source) {
      return source == 4;
   }

   public static boolean itemUsageTips(Player player, Item item, int... forceTipType) {
      SystemFunctionTipConfig.itemFunctionTips(player, item);
      return false;
   }

   protected void doUpdateItemAction(Item item, Container targetContainer, int updateType, int count, int source, int oldContainerType) {
      if (!this.isTransaction(source) && Restriction.isInTranction(item)) {
         TransactionManager.closeTransaction(this.player.getID());
      }

      ItemPacketService.noticeGatewayUpdateItem(this.player, item, updateType);
      switch(updateType) {
      case 1:
      case 13:
         UpdateCount.sendToClient(this.getPlayer(), item);
      case 2:
      case 3:
      case 6:
      default:
         break;
      case 4:
      case 12:
         DeleteItem.sendToClient(this.player, item.getID(), oldContainerType);
         AddItem.sendToClient(this.player, item);
         break;
      case 5:
      case 14:
         if (targetContainer.getType() == 1) {
            CompositeGuideModel.doGuide(this.player, item);
         }
      case 7:
      case 8:
      case 9:
      case 10:
      case 15:
         DeleteItem.sendToClient(this.player, item.getID(), oldContainerType);
         AddItem.sendToClient(this.player, item);
         break;
      case 11:
         DurabilityChange.sendToClient(this.getPlayer(), item);
      }

      if (targetContainer.getType() == 1) {
         if ((updateType == 4 || updateType == 1) && count != 0) {
            this.player.getTaskManager().onEventCheckSpecify(TargetType.SpecifyType.CollectionItem, item.getModelID());
            this.player.getTaskManager().onEventCheckMoreSpecify(TargetType.MoreSpecifyType.ITEM);
         }

         if (oldContainerType != 1) {
            specialActionWhenAdd(this.getPlayer(), item.getModel(), item.getCount(), item.isBind(), source);
            if (oldContainerType == 4 || oldContainerType == 14) {
               SystemFunctionTipConfig.itemFunctionTips(this.player, item);
            }

            CompositeGuideModel.doGuide(this.player, item);
         } else if (updateType == 1) {
            if (this.needOpenPanel(source)) {
               SystemFunctionTipConfig.itemFunctionTips(this.player, item);
            }

            if (count > 0 && this.isNeedToLog(source)) {
               RightMessage.pushItemRightMessage(this.player, item);
               ItemLogExecutor.createLogPacket(this.getPlayer(), item, ItemLogAtom.Type_Add, count, source);
            }
         } else if (updateType == 15) {
            SystemFunctionTipConfig.itemFunctionTips(this.player, item);
         }
      } else if (oldContainerType == 1) {
         this.player.getTaskManager().onEventCheckSpecify(TargetType.SpecifyType.CollectionItem, item.getModelID());
         if (targetContainer.getType() != 1) {
            this.specailActionWhenEnd(item.getModel());
         }
      }

   }

   protected void doRemoveItemAction(Item item, int count, Container container, int source) {
      if (!this.isTransaction(source) && Restriction.isInTranction(item)) {
         TransactionManager.closeTransaction(this.player.getID());
      }

      if (this.isNeedToLog(source)) {
         ItemLogExecutor.createLogPacket(this.getPlayer(), item, ItemLogAtom.Type_Delete, count, source);
      }

      container.reduceItemCount(item, count);
      if (item.getCount() < 1) {
         ItemPacketService.noticeGatewayDeleteItem(this.player, item, source);
         DeleteItem.sendToClient(this.player, item.getID(), item.getContainerType());
      } else {
         ItemPacketService.noticeGatewayUpdateItem(this.player, item, 1);
         UpdateCount.sendToClient(this.getPlayer(), item);
      }

      switch(container.getType()) {
      case 1:
         this.player.getTaskManager().onEventCheckSpecify(TargetType.SpecifyType.CollectionItem, item.getModelID());
         this.specailActionWhenEnd(item.getModel());
         if (item.getModel().getSort() == 3 && SystemFunctionTipConfig.needToPopDrugTip(this.player, item.getItemType())) {
            SystemFunctionTip.sendToClient(this.player, 6, Integer.valueOf(0));
         }
      default:
      }
   }

   public static boolean specialActionWhenAdd(Player player, ItemModel model, int count, boolean isBind, int source) {
      ItemAction action;
      if (model.isnotStorage()) {
         action = model.getAction();
         if (action != null) {
            action.useWhenObtaining(player, model, count, isBind);
         }

         return true;
      } else {
         if (model.getUseType() == 2 && model.getActionID() == 4) {
            action = model.getAction();
            if (action != null) {
               action.useWhenObtaining(player, model, count, isBind);
            }
         }

         return false;
      }
   }

   protected void specailActionWhenEnd(ItemModel model) {
      if (model.getUseType() == 2 && model.getActionID() == 4) {
         if (this.getPlayer().getBackpack().hasEnoughItem(model.getID(), 1)) {
            return;
         }

         ItemAction action = model.getAction();
         if (action != null) {
            action.endAction(this.getPlayer());
         }
      }

   }

   protected void updateItemOtherOperation(Item item, int source, int updateType) {
      Container container = this.getContainer(item.getContainerType());
      this.doUpdateItemAction(item, container, updateType, 0, source, item.getContainerType());
      if (item.getContainerType() == 0) {
         this.player.getEquipment().updateItem(item);
      }

   }

   protected Storage getTargetStorage(int sortId, int source) {
      return (Storage)(source == 13 ? this.player.getTreasureHouse() : this.player.getBackpack());
   }

   public Container getContainer(int type) {
      switch(type) {
      case 0:
         return this.player.getEquipment();
      case 1:
         return this.player.getBackpack();
      case 4:
         return this.getPlayer().getDepot();
      case 14:
         return this.player.getTreasureHouse();
      default:
         return null;
      }
   }

   public Player getPlayer() {
      return this.player;
   }

   public void setPlayer(Player player) {
      this.player = player;
   }
}
