package com.mu.game.model.unit.player.shortcut;

import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.shortcut.AddShortcut;
import com.mu.io.game.packet.imp.shortcut.DelShortcut;
import com.mu.io.game.packet.imp.shortcut.ExchangeShortcut;
import java.util.HashMap;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Shortcut {
   public static final int Type_Skill = 2;
   public static final int Type_Item = 1;
   private static Logger logger = LoggerFactory.getLogger(Shortcut.class);
   private HashMap entries = new HashMap();
   private Player owner;
   private boolean change = false;

   public Shortcut(Player player) {
      this.owner = player;
   }

   public void loadEntry(ShortcutEntry entry) {
      if (!this.effectPosition(entry.getPosition())) {
         logger.debug("快捷键位置不正确" + entry.getPosition());
      } else {
         if (entry.getType() == 2) {
            if (!this.getOwner().getSkillManager().hasSkill(entry.getModelID())) {
               logger.debug("快捷键没有相应的技能 " + entry.getModelID());
               return;
            }
         } else if (!this.getOwner().getBackpack().hasEnoughItem(entry.getModelID(), 1)) {
            logger.debug("快捷键没有相应的物品 " + entry.getModelID());
            return;
         }

         this.entries.put(entry.getPosition(), entry);
      }
   }

   private boolean effectPosition(int position) {
      return position >= 0 && position < 10;
   }

   public int addSkillEntry(int position, int type, int modelID) {
      if (!this.effectPosition(position)) {
         return 1012;
      } else {
         if (type == 2) {
            if (!this.getOwner().getSkillManager().hasSkill(modelID)) {
               return 8007;
            }
         } else {
            if (!this.getOwner().getBackpack().hasEnoughItem(modelID, 1)) {
               return 3002;
            }

            Item item = this.getOwner().getBackpack().getFirstItemByModelID(modelID);
            if (item.getModel().getSort() != 3) {
               return 1014;
            }
         }

         ShortcutEntry entry = this.getShortcutEntry(position);
         if (entry == null) {
            entry = new ShortcutEntry(type, modelID, position);
         } else {
            entry.setModelID(modelID);
            entry.setType(type);
         }

         this.entries.put(position, entry);
         AddShortcut.sendToClient(this.getOwner(), entry);
         this.change();
         return 1;
      }
   }

   public int delShortcut(int position) {
      if (!this.effectPosition(position)) {
         return 1012;
      } else if (!this.entries.containsKey(position)) {
         return 1013;
      } else {
         this.entries.remove(position);
         this.change();
         DelShortcut.sendToClient(this.getOwner(), position);
         return 1;
      }
   }

   public int changePosition(int firstPosition, int targetPosition) {
      if (this.effectPosition(firstPosition) && this.effectPosition(targetPosition)) {
         if (!this.entries.containsKey(firstPosition)) {
            return 1013;
         } else {
            ShortcutEntry fs = (ShortcutEntry)this.entries.get(firstPosition);
            ShortcutEntry ts = (ShortcutEntry)this.entries.get(targetPosition);
            if (ts == null) {
               this.entries.remove(firstPosition);
               fs.setPosition(targetPosition);
               this.entries.put(targetPosition, fs);
            } else {
               int type = fs.getType();
               int modelID = fs.getModelID();
               fs.setType(ts.getType());
               fs.setModelID(ts.getModelID());
               ts.setType(type);
               ts.setModelID(modelID);
            }

            this.change();
            ExchangeShortcut.sendToClient(this.getOwner(), firstPosition, targetPosition);
            return 1;
         }
      } else {
         return 1012;
      }
   }

   public boolean hasShortCut(int type, int modelID) {
      Iterator var4 = this.entries.values().iterator();

      ShortcutEntry entry;
      do {
         if (!var4.hasNext()) {
            return false;
         }

         entry = (ShortcutEntry)var4.next();
      } while(entry.getType() != type || entry.getModelID() != modelID);

      return true;
   }

   public ShortcutEntry getShortcutEntry(int position) {
      return (ShortcutEntry)this.entries.get(position);
   }

   public void destroy() {
      this.owner = null;
      if (this.entries != null) {
         this.entries.clear();
         this.entries = null;
      }

   }

   public void change() {
      this.change = true;
   }

   public boolean isChange() {
      return this.change;
   }

   public HashMap getEntries() {
      return this.entries;
   }

   public Player getOwner() {
      return this.owner;
   }
}
