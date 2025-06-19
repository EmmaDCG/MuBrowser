package com.mu.game.model.drop;

import com.mu.game.model.drop.model.DropModel;
import com.mu.game.model.drop.model.MonsterDrop;
import com.mu.game.model.drop.model.WellDropManager;
import com.mu.game.model.drop.rara.BossRaraDrop;
import com.mu.game.model.drop.rara.RaraDrop;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.item.model.ItemColor;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.map.Map;
import com.mu.game.model.unit.monster.Monster;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.drop.AroundDropItem;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemDropManager {
   private static Logger logger = LoggerFactory.getLogger(SystemDropManager.class);
   private static HashMap worldDayCount = new HashMap();

   public static void addWorldCount(DropModel model, int count) {
      int value = 0;
      int dropID = model.getDropID();
      if (model.getServerMaxCountPerDay() != -1) {
         if (worldDayCount.containsKey(dropID)) {
            value = ((Integer)worldDayCount.get(dropID)).intValue();
         }

         value += count;
         worldDayCount.put(dropID, value);
      }

   }

   public static int getWorldDayCount(int dropID) {
      return worldDayCount.containsKey(dropID) ? ((Integer)worldDayCount.get(dropID)).intValue() : 0;
   }

   public static void dayClear() {
      worldDayCount.clear();
   }

   public static void createMoenyDrop(ItemDataUnit unit, HashMap itemList, int protecteTime) {
      if (itemList != null && unit != null) {
         int count = unit.getCount();
         if (count >= 1) {
            int floor = 1;
            if (count > 100000000) {
               floor = 20;
            } else if (count > 50000000) {
               floor = 10;
            } else if (count > 10000000) {
               floor = 5;
            }

            for(int itemCount = count / floor; count > 0; count -= itemCount) {
               if (count < itemCount * 2) {
                  itemCount = count;
               }

               ItemDataUnit tmpUnit = unit.cloneUnit();
               tmpUnit.setCount(itemCount);
               Item item = ItemTools.createItem(2, tmpUnit);
               if (item != null) {
                  itemList.put(item, protecteTime);
               }
            }

         }
      }
   }

   public static void dropWhenMonsterBeKill(Monster monster, Player player, int delay, boolean self) {
      if (monster != null && player != null && monster.canDrop(player)) {
         if (monster.getDrops() != null) {
            Map map = monster.getMap();
            if (map.getDropItemSize() < 800) {
               HashMap itemList = new HashMap();
               Iterator var7 = monster.getDrops().iterator();

               while(true) {
                  while(var7.hasNext()) {
                     MonsterDrop drop = (MonsterDrop)var7.next();
                     switch(drop.getType()) {
                     case 1:
                        if (monster.getBossRank() == 2) {
                           if (player.getLevel() - 100 <= monster.getLevel() || !monster.hasDropPunish()) {
                              drop.getDropItems(player, itemList, monster.getTemplateId());
                           }
                        } else if (player.getLevel() - 50 <= monster.getLevel() || !monster.hasDropPunish()) {
                           drop.getDropItems(player, itemList, monster.getTemplateId());
                        }
                        break;
                     case 2:
                        drop.getDropItems(player, itemList, monster.getTemplateId());
                     }
                  }

                  if (itemList.size() < 1) {
                     return;
                  }

                  Point point = monster.getActualPosition();
                  if (map.isBlocked(point.x, point.y)) {
                     logger.error("monster(" + monster.getName() + ") is {} ({}, {}), ({}, {})", new Object[]{monster.getName(), point.x, point.y, monster.getBornPoint().x, monster.getBornPoint().y});
                  }

                  boolean broadcast = monster.isBoss();
                  doDrop(itemList, map, player, monster.getActualPosition(), 1, monster.getDropProtectedTime(), delay, WellDropManager.spellName(monster, map), WellDropManager.spellNewName(monster, map), broadcast, self);
                  if (monster.isBoss() && !self) {
                     long now = System.currentTimeMillis();
                     ArrayList tmpList = new ArrayList();
                     Iterator it = itemList.keySet().iterator();

                     while(it.hasNext()) {
                        Item item = (Item)it.next();
                        if (item.getQuality() >= ItemColor.COLOR_GREEN.getIdentity()) {
                           BossRaraDrop bd = new BossRaraDrop(item, player.getID(), player.getName(), now, monster.getName(), map.getName());
                           tmpList.add(bd);
                        }
                     }

                     if (tmpList.size() > 0) {
                        RaraDrop.addRaraDrop(tmpList);
                        tmpList.clear();
                     }

                     tmpList = null;
                  }

                  itemList.clear();
                  return;
               }
            }
         }
      }
   }

   private static void doDrop(HashMap itemMap, Map map, Player player, Point point, int dropRule, int protectedTime, int delay, String reason, String newReason, boolean broadcast, boolean self) {
      List dropList = new ArrayList();
      HashSet dString = new HashSet();
      int i = 0;

      for(Iterator it = itemMap.entrySet().iterator(); it.hasNext(); ++i) {
         Entry entry = (Entry)it.next();
         Item item = (Item)entry.getKey();
         int pt = ((Integer)entry.getValue()).intValue();
         pt = pt == -1 ? protectedTime : pt;
         Point p = getPoint(map, point, dString, i);
         String ds = map.getTileX(p.x) + "_" + map.getTileY(p.y);
         dString.add(ds);
         DropItem di = new DropItem(map, item, p.x, p.y, player, dropRule);
         di.setProtectedTime(protectedTime);
         dropList.add(di);
      }

      dString.clear();
      Iterator var22 = dropList.iterator();

      while(var22.hasNext()) {
         DropItem item = (DropItem)var22.next();
         item.andToMapAndEffect();
      }

      AroundDropItem.sendToMap(dropList, player, self);
      if (broadcast && !self) {
         WellDropManager.broastcast(dropList, player, reason, newReason);
      }

      dropList.clear();
   }

   public static Point getPoint(Map map, Point point, HashSet dString, int index) {
      Point p = map.searchDropPoint(point.x, point.y, dString, -1);
      if (p == null) {
         p = map.searchDropPoint(point.x, point.y, dString, index);
      }

      if (p == null) {
         p = new Point(point);
      }

      if (map.isBlocked(p.x, p.y)) {
         logger.error("SystemDropManager... (%d, %d)", p.x, p.y);
      }

      return p;
   }

   public static void forceDrop(List unitList, Player player, Point point, int... objs) {
      int protectedTime = 30000;
      if (objs != null && objs.length > 0) {
         protectedTime = objs[0];
      }

      HashMap itemList = new HashMap();
      Iterator var7 = unitList.iterator();

      while(true) {
         while(var7.hasNext()) {
            ItemDataUnit unit = (ItemDataUnit)var7.next();
            if (ItemModel.getModel(unit.getModelID()).isMoney()) {
               createMoenyDrop(unit, itemList, protectedTime);
            } else {
               for(int count = 1; count <= unit.getCount(); ++count) {
                  ItemDataUnit tmpUnit = unit.cloneUnit();
                  tmpUnit.setCount(1);
                  Item item = ItemTools.createItem(3, tmpUnit);
                  itemList.put(item, protectedTime);
               }
            }
         }

         doDrop(itemList, player.getMap(), player, point, 1, protectedTime, 0, "", "", true, true);
         itemList.clear();
         return;
      }
   }
}
