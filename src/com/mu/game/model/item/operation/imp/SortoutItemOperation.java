package com.mu.game.model.item.operation.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.container.imp.Storage;
import com.mu.game.model.item.operation.ItemOperation;
import com.mu.game.model.item.operation.OperationResult;
import com.mu.io.game.packet.imp.storage.SortoutStorage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SortoutItemOperation extends ItemOperation {
   private int containerType;

   public SortoutItemOperation(int containerType) {
      this.containerType = containerType;
   }

   public OperationResult execute() throws Exception {
      Storage storage = this.getPlayer().getStorage(this.containerType);
      List oldItems = storage.getAllItems();
      if (oldItems.size() > 0) {
         HashMap oldMaps = new HashMap();
         HashMap os = new HashMap();
         Iterator newItems = oldItems.iterator();

         Item moves;
         while(newItems.hasNext()) {
            moves = (Item)newItems.next();
            TempItem temp = new TempItem(moves.getID(), moves.getCount(), moves.getSlot());
            os.put(temp.getItemID(), temp);
            oldMaps.put(moves.getID(), moves);
         }

         oldItems.clear();
         oldItems = null;
         storage.sortOutItems(-1);
         ArrayList moves2 = new ArrayList();
         ArrayList newItems2 = storage.getAllItems();

         Item item;
         for(Iterator var8 = newItems2.iterator(); var8.hasNext(); oldMaps.remove(item.getID())) {
            item = (Item)var8.next();
            TempItem temp = (TempItem)os.remove(item.getID());
            int diffCount = item.getCount() - temp.getCount();
            boolean slotChange = item.getSlot() != temp.getSlot();
            if (diffCount != 0 && slotChange) {
               moves2.add(item);
               this.doUpdateItemAction(item, storage, 13, diffCount, 15, storage.getType());
            } else if (diffCount != 0) {
               this.doUpdateItemAction(item, storage, 1, diffCount, 15, storage.getType());
            } else if (slotChange) {
               moves2.add(item);
               this.doUpdateItemAction(item, storage, 2, diffCount, 15, storage.getType());
            }
         }

         Iterator it = oldMaps.values().iterator();

         while(it.hasNext()) {
            Item item2 = (Item)it.next();
            this.doRemoveItemAction(item2, item2.getCount(), storage, 15);
         }

         SortoutStorage.itemMoveResult(this.containerType, moves2, this.getPlayer());
         newItems2.clear();
         newItems = null;
         moves2.clear();
         moves = null;
         os.clear();
         os = null;
         oldMaps.clear();
         oldMaps = null;
      }

      return new OperationResult(1, -1L);
   }
}
