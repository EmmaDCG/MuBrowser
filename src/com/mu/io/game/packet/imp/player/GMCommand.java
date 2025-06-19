package com.mu.io.game.packet.imp.player;

import com.mu.config.Global;
import com.mu.db.Pool;
import com.mu.db.log.IngotChangeType;
import com.mu.game.IDFactory;
import com.mu.game.dungeon.DungeonManager;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemSaveAide;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.map.Map;
import com.mu.game.model.map.MapConfig;
import com.mu.game.model.properties.levelData.PlayerLevelData;
import com.mu.game.model.task.Task;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.Time;
import com.mu.utils.Tools;
import java.awt.Point;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.UUID;

public class GMCommand extends ReadAndWritePacket {
   public GMCommand(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      if (Global.isCanGMCommand()) {
         Player player = this.getPlayer();
         String[] cmd = this.readUTF().trim().substring(1).split(" ");
         doCommand(cmd, player);
      }
   }

   private static void doCommand(String[] str, Player player) {
      String var2;
      switch((var2 = str[0]).hashCode()) {
      case 3191:
         if (var2.equals("cz")) {
            cz(player, str);
         }
         break;
      case 3419:
         if (var2.equals("kf")) {
            kf(player);
         }
         break;
      case 3820:
         if (var2.equals("xd")) {
            washPotential(player);
         }
         break;
      case 98399:
         if (var2.equals("cfb")) {
            exitDungeon(player);
         }
         break;
      case 105026:
         if (var2.equals("jbz")) {
            addBindIngot(player, str);
         }
         break;
      case 105072:
         if (var2.equals("jdj")) {
            addItem(player, str);
         }
         break;
      case 105126:
         if (var2.equals("jfb")) {
            enterDungeon(player, str);
         }
         break;
      case 105250:
         if (var2.equals("jjb")) {
            addMoney(player, str);
         }
         break;
      case 105254:
         if (var2.equals("jjf")) {
            addRedeemPoint(player, str);
         }
         break;
      case 105258:
         if (!var2.equals("jjj")) {
            ;
         }
         break;
      case 105273:
         if (var2.equals("jjy")) {
            addExp(player, str);
         }
         break;
      case 105763:
         if (var2.equals("jzs")) {
            addIngot(player, str);
         }
         break;
      case 107868:
         if (var2.equals("map")) {
            enterMap(player, str);
         }
         break;
      case 113721:
         if (var2.equals("sdj")) {
            deleteItems(player);
         }
         break;
      case 3069711:
         if (var2.equals("czbz")) {
            czbz(player);
         }
         break;
      case 3084677:
         if (var2.equals("djts")) {
            levelUp(player);
         }
         break;
      case 3278279:
         if (var2.equals("jzcz")) {
            player.getUser().addPay(Integer.parseInt(str[1]), System.currentTimeMillis());
         }
         break;
      case 3278412:
         if (var2.equals("jzhd")) {
            try {
               ActivityManager.reloadAllActivity();
            } catch (Exception var5) {
               var5.printStackTrace();
            }
         }
         break;
      case 3289330:
         if (var2.equals("kfsj")) {
            changeKfsj(player, str);
         }
         break;
      case 3561275:
         if (var2.equals("tjrw")) {
            Task task = (Task)player.getTaskManager().getCurrentTaskMap().get(Integer.parseInt(str[1]));
            if (task != null) {
               task.forceComplete();
               player.getTaskManager().submit(task.getId(), true);
            }
         }
         break;
      case 109750367:
         if (var2.equals("ssydj")) {
            deleteAllItems(player);
         }
         break;
      case 110808629:
         if (var2.equals("txjjy")) {
            player.getTanXianManager().addExp(Integer.parseInt(str[1]));
         }
      }

   }

   private static void enterMap(Player player, String[] str) {
      Map defaultMap = MapConfig.getDefaultMap(Integer.parseInt(str[1]));
      if (defaultMap != null) {
         if (str.length < 4) {
            player.switchMap(defaultMap, defaultMap.getDefaultPoint());
         } else {
            player.switchMap(defaultMap, new Point(Integer.parseInt(str[2]), Integer.parseInt(str[3])));
         }
      }

   }

   private static void changeKfsj(Player player, String[] kfsj) {
      try {
         DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         df.parse(kfsj[1] + " " + kfsj[2]);
         Connection conn = Pool.getGlobalConnection();
         String sql = "update muuser.mu_server set open_time = ? where id = ?";
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setString(1, kfsj[1] + " " + kfsj[2]);
         ps.setInt(2, Global.getServerID());
         ps.executeUpdate();
         ps.close();
         conn.close();
      } catch (Exception var6) {
         var6.printStackTrace();
         SystemMessage.writeMessage(player, "开服时间设置错误", -1);
      }

   }

   private static void addItem(Player player, String[] objs) {
      if (objs.length >= 2) {
         int modelId = Integer.parseInt(objs[1]);
         ItemModel model = ItemModel.getModel(modelId);
         int starLevel = 0;
         boolean isBind = false;
         int socket = 0;
         String stats = "";
         int zjLevel = 0;
         int count = 1;
         if (model != null) {
            if (objs.length >= 3) {
               count = Integer.parseInt(objs[2]);
               if (model.isEquipment()) {
                  count = 1;
               }
            }

            if (objs.length >= 4) {
               isBind = Integer.parseInt(objs[3]) == 1;
            }

            if (objs.length >= 5) {
               starLevel = Integer.parseInt(objs[4]);
            }

            if (objs.length >= 6) {
               socket = Integer.parseInt(objs[5]);
            }

            if (objs.length >= 7) {
               zjLevel = Integer.parseInt(objs[6]);
            }

            if (objs.length >= 8) {
               stats = objs[7];
            }

            ItemSaveAide is = new ItemSaveAide(IDFactory.getItemID(), modelId, model.getQuality(), count, 0, 1, starLevel, socket, isBind, model.getPrice(), 1, 0, 0, -1L, model.getDurability(), "", stats, "", "", zjLevel);
            Item item = ItemTools.loadItem(is);
            player.getItemManager().addItem(item, 32);
         }
      }
   }

   private static void addExp(Player player, String[] values) {
      long exp = Long.parseLong(values[1]);
      if (exp >= 0L) {
         PlayerManager.addExp(player, exp, -1L);
      }
   }

   private static void addRedeemPoint(Player player, String[] values) {
      int value = Integer.parseInt(values[1]);
      if (value >= 0) {
         PlayerManager.addRedeemPoints(player, value);
      }
   }

   private static void washPotential(Player player) {
      PlayerManager.washPotential(player, false);
   }

   private static void czbz(Player player) {
      DungeonManager.getLuolanManager().resetLuolan();
   }

   private static void cz(Player player, String[] values) {
      int money = Integer.parseInt(values[1]);
      if (money >= 0 && money <= 100000) {
         PlayerManager.pay(UUID.randomUUID().toString(), player.getUserName(), player.getUser().getServerID(), money, money * 10, Time.getTimeStr(), "", 1, IngotChangeType.GMPay.getType());
      }
   }

   private static void addMoney(Player player, String[] values) {
      int money = Integer.parseInt(values[1]);
      if (money >= 0) {
         PlayerManager.addMoney(player, money);
      }
   }

   private static void kf(Player player) {
      player.toRemoteServer();
   }

   private static void levelUp(Player player) {
      if (player.getLevel() < PlayerManager.getMaxLevel(player)) {
         long exp = PlayerLevelData.getNeedExp(player.getLevel());
         PlayerManager.addExp(player, exp, -1L);
      }
   }

   private static void exitDungeon(Player player) {
      if (player.isInDungeon()) {
         player.getDungeonMap().getDungeon().exitForInitiative(player, true);
      }

   }

   private static void deleteItems(Player player) {
      Iterator var2 = player.getBackpack().getAllItems().iterator();

      while(var2.hasNext()) {
         Item item = (Item)var2.next();
         player.getItemManager().deleteItem(item, 1);
      }

   }

   private static void enterDungeon(Player player, String[] str) {
      try {
         DungeonManager.createAndEnterDungeon(player, Integer.parseInt(str[1]));
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   private static void addIngot(Player player, String[] cmd) {
      if (cmd.length == 2) {
         if (Tools.isNumber(cmd[1].trim())) {
            long ingot = Long.parseLong(cmd[1].trim());
            if (ingot < 2147483647L && (long)player.getIngot() + ingot < 2147483647L) {
               PlayerManager.addIngot(player, (int)ingot, IngotChangeType.GM.getType());
            }
         }
      }
   }

   private static void addBindIngot(Player player, String[] cmd) {
      if (cmd.length == 2) {
         if (Tools.isNumber(cmd[1].trim())) {
            long ingot = Long.parseLong(cmd[1].trim());
            if (ingot < 2147483647L && (long)player.getIngot() + ingot < 2147483647L) {
               PlayerManager.addBindIngot(player, (int)ingot, IngotChangeType.GM.getType());
            }
         }
      }
   }

   private static void deleteAllItems(Player player) {
      deleteItems(player);
      Iterator var2 = player.getEquipment().getAllItems().iterator();

      while(var2.hasNext()) {
         Item item = (Item)var2.next();
         player.getItemManager().deleteItem(item, 1);
      }

   }
}
