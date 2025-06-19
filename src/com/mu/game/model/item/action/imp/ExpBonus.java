package com.mu.game.model.item.action.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.unit.buff.Buff;
import com.mu.game.model.unit.buff.model.BuffGroup;
import com.mu.game.model.unit.buff.model.BuffModel;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.imp.ItemBuffPriorityPopup;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;
import java.util.Iterator;
import java.util.Map.Entry;

public class ExpBonus extends BuffAccept {
   public ExpBonus(int buffID, int level) {
      super(buffID, level);
   }

   public int privyCondition(Player player, Item item, int useNum, boolean definite) {
      BuffModel model = BuffModel.getModel(this.getBuffID());
      Buff hasBuff = player.getBuffManager().getBuff(this.getBuffID());
      if (hasBuff != null && !model.isOverlap()) {
         return 3038;
      } else {
         if (!definite) {
            BuffGroup group = BuffGroup.getBuffGroup(this.getBuffID());
            if (group != null && group.isNeedToPop()) {
               Iterator it = group.getBuffs().entrySet().iterator();

               while(it.hasNext()) {
                  Entry entry = (Entry)it.next();
                  if (((Integer)entry.getKey()).intValue() != this.getBuffID()) {
                     Buff groupBuff = player.getBuffManager().getBuff(((Integer)entry.getKey()).intValue());
                     if (groupBuff != null) {
                        String content = group.getSeniorPopStr();
                        if (!group.isPriority(this.getBuffID(), groupBuff.getModelID())) {
                           content = group.getLowerPopStr();
                        }

                        ItemBuffPriorityPopup popup = new ItemBuffPriorityPopup(player.createPopupID(), group.getPopTitle(), content, item.getID(), useNum);
                        ShowPopup.open(player, popup);
                        return 3043;
                     }
                  }
               }
            }
         }

         return 1;
      }
   }

   public void useWhenObtaining(Player player, ItemModel model, int count, boolean isBind) {
   }
}
