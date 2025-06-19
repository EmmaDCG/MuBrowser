package com.mu.game.model.transaction;

import com.mu.config.MessageText;
import com.mu.game.CenterManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.io.game.packet.imp.player.tansaction.ClosePanelByServer;
import com.mu.io.game.packet.imp.player.tansaction.DeleteItemByServer;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class TransactionManager {
   public static final int AvailableDistance = 10000;
   private static ConcurrentHashMap requestMap = new ConcurrentHashMap();
   private static ConcurrentHashMap targetMap = new ConcurrentHashMap();
   private static ConcurrentHashMap transActionMap = new ConcurrentHashMap();
   private static ConcurrentHashMap itemMap = new ConcurrentHashMap();

   public static void addRequest(RequestTransaction action) {
      requestMap.put(action.getInitiatorID(), action);
      targetMap.put(action.getTargetID(), action);
   }

   public static RequestTransaction getRequestTranseactionByInitiator(long initiatorID) {
      return (RequestTransaction)requestMap.get(initiatorID);
   }

   public static RequestTransaction getRequestTranseactionByTarget(long targetID) {
      return (RequestTransaction)targetMap.get(targetID);
   }

   public static void addTransaction(long initiatorID, long targetID, Transaction ta) {
      transActionMap.put(initiatorID, ta);
      transActionMap.put(targetID, ta);
   }

   public static Transaction getTransAcTransaction(long id) {
      return (Transaction)transActionMap.get(id);
   }

   public static void addTransactionItem(Item item) {
      itemMap.put(item.getID(), item);
   }

   public static void removeTransactionItem(long id) {
      Item item = (Item)itemMap.remove(id);
      if (item != null) {
         item.destroy();
      }

   }

   public static void closeTransaction(long initiatorOrTargetID) {
      clearTransactionRequest(initiatorOrTargetID);
      Transaction action = (Transaction)transActionMap.remove(initiatorOrTargetID);
      if (action != null) {
         transActionMap.remove(action.getTargetID());
         transActionMap.remove(action.getInitiatorID());
         TransactionList tl1 = action.getInitiatorList();
         Player owner = CenterManager.getPlayerByRoleID(tl1.getPlayerID());
         Item[] items = tl1.getItems();

         for(int i = 0; i < items.length; ++i) {
            if (items[i] != null) {
               itemMap.remove(items[i].getID());
               if (owner != null) {
                  DeleteItemByServer.deletItem(owner, i, items[i].getID());
               }
            }
         }

         TransactionList tl2 = action.getTargetList();
         Player other = CenterManager.getPlayerByRoleID(tl2.getPlayerID());
         items = tl2.getItems();

         for(int i = 0; i < items.length; ++i) {
            if (items[i] != null) {
               itemMap.remove(items[i].getID());
               if (other != null) {
                  DeleteItemByServer.deletItem(other, i, items[i].getID());
               }
            }
         }

         if (owner != null) {
            owner.endTransaction();
         }

         if (other != null) {
            other.endTransaction();
         }

         closePannel(owner, other);
      }
   }

   public static void clearTransactionRequest(long id) {
      RequestTransaction requestAction = (RequestTransaction)requestMap.get(id);
      if (requestAction == null) {
         requestAction = (RequestTransaction)targetMap.get(id);
      }

      if (requestAction != null) {
         clearTransactionRequest(requestAction);
      }

   }

   public static boolean isTransaction(long id) {
      return transActionMap.containsKey(id);
   }

   public static void clearTransactionRequest(RequestTransaction action) {
      requestMap.remove(action.getInitiatorID());
      targetMap.remove(action.getTargetID());
   }

   public static boolean isTransActionItem(long id) {
      return itemMap.containsKey(id);
   }

   private static boolean checkTranseactionData(TransactionList tl1, TransactionList tl2) {
      Item[] items = tl1.getItems();
      Item[] var6 = items;
      int var5 = items.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         Item item = var6[var4];
         if (item != null) {
            Item cloneItem = (Item)itemMap.get(item.getID());
            if (cloneItem == null) {
               return false;
            }

            if (!isTheSameItem(item, cloneItem)) {
               return false;
            }
         }
      }

      Item[] items2 = tl2.getItems();
      Item[] var14 = items2;
      int var13 = items2.length;

      for(var5 = 0; var5 < var13; ++var5) {
         Item item = var14[var5];
         if (item != null) {
            Item cloneItem = (Item)itemMap.get(item.getID());
            if (cloneItem == null) {
               return false;
            }

            if (!isTheSameItem(item, cloneItem)) {
               return false;
            }
         }
      }

      Player p1 = CenterManager.getPlayerByRoleID(tl1.getPlayerID());
      Player p2 = CenterManager.getPlayerByRoleID(tl2.getPlayerID());
      if (p1 != null && p2 != null) {
         if (p1.getIngot() == tl1.getCurrentIngot() && p1.getMoney() == tl1.getCurrentMoney() && p2.getIngot() == tl2.getCurrentIngot() && p2.getMoney() == tl2.getCurrentMoney()) {
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static boolean isTheSameItem(Item item, Item cloneItem) {
      if (item.getModelID() != item.getModelID()) {
         return false;
      } else if (item.getCount() != cloneItem.getCount()) {
         return false;
      } else if (item.getQuality() != cloneItem.getQuality()) {
         return false;
      } else if (item.getStarLevel() != cloneItem.getStarLevel()) {
         return false;
      } else if (item.getSocket() != cloneItem.getSocket()) {
         return false;
      } else if (!item.getStoneStr().equals(cloneItem.getStoneStr())) {
         return false;
      } else if (!item.getBasisStr().equals(cloneItem.getBasisStr())) {
         return false;
      } else if (!item.getRuneStr().equals(cloneItem.getRuneStr())) {
         return false;
      } else {
         return item.isBind() == cloneItem.isBind();
      }
   }

   public static void doTransaction(long id) {
      Transaction action = getTransAcTransaction(id);
      if (action != null) {
         TransactionList tl1 = action.getInitiatorList();
         TransactionList tl2 = action.getTargetList();
         if (tl1.isConfirm && tl2.isConfirm) {
            Player initiator = CenterManager.getPlayerByRoleID(tl1.getPlayerID());
            Player target = CenterManager.getPlayerByRoleID(tl2.getPlayerID());
            if (initiator != null && target != null) {
               if (!checkTranseactionData(tl1, tl2)) {
                  SystemMessage.writeMessage(initiator, 15021);
                  SystemMessage.writeMessage(target, 15021);
                  closeTransaction(initiator.getID());
                  closePannel(initiator, target);
                  return;
               }

               if (checkSpace(tl1.getItems(), initiator, target) != 1 || checkSpace(tl2.getItems(), target, initiator) != 1) {
                  closeTransaction(initiator.getID());
                  return;
               }

               Item[] var10;
               int var9 = (var10 = tl1.getItems()).length;

               Item item;
               int var8;
               Item cloneItem;
               for(var8 = 0; var8 < var9; ++var8) {
                  item = var10[var8];
                  if (item != null) {
                     cloneItem = item.cloneItem(1);
                     target.getItemManager().addItem(cloneItem, 4);
                     initiator.getItemManager().deleteItem(item, 4);
                  }
               }

               var9 = (var10 = tl2.getItems()).length;

               for(var8 = 0; var8 < var9; ++var8) {
                  item = var10[var8];
                  if (item != null) {
                     cloneItem = item.cloneItem(1);
                     initiator.getItemManager().addItem(cloneItem, 4);
                     target.getItemManager().deleteItem(item, 4);
                  }
               }

               if (tl2.getMoney() > 0) {
                  PlayerManager.addMoney(initiator, tl2.getMoney());
                  PlayerManager.reduceMoney(target, tl2.getMoney());
               }

               if (tl1.getMoney() > 0) {
                  PlayerManager.addMoney(target, tl1.getMoney());
                  PlayerManager.reduceMoney(initiator, tl1.getMoney());
               }

               SystemMessage.writeMessage(initiator, 15022);
               SystemMessage.writeMessage(target, 15022);
               closeTransaction(initiator.getID());
               closePannel(initiator, target);
            }
         }

      }
   }

   public static void closePannel(Player owner, Player target) {
      if (owner != null || target != null) {
         if (owner != null) {
            ClosePanelByServer.closePanel(owner, target == null ? MessageText.getText(1020) : target.getName());
         }

         if (target != null) {
            ClosePanelByServer.closePanel(target, owner == null ? MessageText.getText(1020) : owner.getName());
         }

      }
   }

   private static int checkSpace(Item[] items, Player owner, Player target) {
      ArrayList itemList = new ArrayList();
      Item[] var7 = items;
      int var6 = items.length;

      for(int var5 = 0; var5 < var6; ++var5) {
         Item item = var7[var5];
         if (item != null) {
            itemList.add(item);
         }
      }

      int result = 1;
      if (target.getBackpack().getVacantSize() < itemList.size()) {
         result = 2004;
      }

      Iterator var10 = itemList.iterator();

      while(var10.hasNext()) {
         Item item = (Item)var10.next();
         if (item.getCount() < 1) {
            result = 3002;
            break;
         }

         if (!owner.getBackpack().hasItem(item.getID())) {
            result = 3002;
            break;
         }
      }

      if (result != 1) {
         ClosePanelByServer.closePanel(owner, target.getName());
         ClosePanelByServer.closePanel(target, owner.getName());
         SystemMessage.writeMessage(owner, 15023);
         SystemMessage.writeMessage(target, 15024);
      }

      itemList.clear();
      itemList = null;
      return result;
   }
}
