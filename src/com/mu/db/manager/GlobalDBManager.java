package com.mu.db.manager;

import com.mu.config.Global;
import com.mu.config.MessageText;
import com.mu.config.StatNames;
import com.mu.config.SystemScript;
import com.mu.db.Pool;
import com.mu.db.log.IngotChangeType;
import com.mu.game.dungeon.DungeonTemplateFactory;
import com.mu.game.model.drop.model.DropControlManager;
import com.mu.game.model.drop.model.DropModel;
import com.mu.game.model.equip.equipStat.EquipStatManager;
import com.mu.game.model.equip.zhuijia.ZhuijiaForgingData;
import com.mu.game.model.financing.FinancingConfigManager;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.item.other.ExpiredItemManager;
import com.mu.game.model.mall.MallConfigManager;
import com.mu.game.model.map.MapConfig;
import com.mu.game.model.map.MapData;
import com.mu.game.model.pet.PetConfigManager;
import com.mu.game.model.properties.levelData.PlayerLevelData;
import com.mu.game.model.properties.newPotentail.PotentialData;
import com.mu.game.model.rewardhall.RewardHallConfigManager;
import com.mu.game.model.shield.ShieldConfigManager;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatList2Client;
import com.mu.game.model.tanxian.TanXianConfigManager;
import com.mu.game.model.task.TaskConfigManager;
import com.mu.game.model.transfer.TransferConfigManager;
import com.mu.game.model.unit.CreatureTemplate;
import com.mu.game.model.unit.material.MaterialTemplate;
import com.mu.game.model.unit.monster.worldboss.WorldBossManager;
import com.mu.game.model.unit.skill.model.SkillModel;
import com.mu.game.model.unit.trigger.action.ActionEnum;
import com.mu.game.model.unit.trigger.action.model.TriggerModel;
import com.mu.game.model.vip.VIPConfigManager;
import com.mu.utils.Zlib;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalDBManager {
   private static final Logger logger = LoggerFactory.getLogger(GlobalDBManager.class);
   public static final String sqlSystemScript = "select id,data from system_script order by id";
   public static final String sqlGetSystemScriptData = "select data from system_script where id = ?";
   public static final String sqlGetMapData = "select * from mu_map";
   public static final String sqlGetDungeonTemplate = "select dungeon_id,dungeon_data from dungeon_template";
   public static final String sqlGetOpenTIme = "select open_time from muuser.mu_server where id = " + Global.getServerID();

   public static boolean initData() {
      Connection conn = null;

      try {
         conn = Pool.getGlobalConnection();
         loadGlobalConfigs(conn);
         return true;
      } catch (Exception var5) {
         var5.printStackTrace();
         logger.error("init global data failed...");
      } finally {
         Pool.closeConnection(conn);
      }

      return false;
   }

   private static void loadGlobalConfigs(Connection conn) throws Exception {
      loadSystemScriptData(conn);
      loadMessage();
      loadStatNames();
      StatEnum.init();
      ActionEnum.init();
      IngotChangeType.init();
      loadRolePanelStats();
      StatList2Client.init();
      loadItemData();
      loadForging();
      loadEquipStatRule();
      loadCreatureTemplate();
      loadMaterialTemplate();
      loadDropData();
      loadSkillData();
      loadPlayerLevelData();
      loadMallConfigs();
      loadSystemScript();
      loadMaps(conn);
      WorldBossManager.createBoss();
      loadRewardHallConfigs();
      loadTaskConfigs();
      loadDungeonTemplate(conn);
      loadTriggerData();
      loadDropControlData();
      loadPetConfigs();
      loadTransferConfigs();
      loadShieldConfigs();
      loadVIPConfigs();
      loadTanXianConfigs();
      loadFinancingConfigs();
      TanXianConfigManager.initClues();
      SystemScript.clearScript();
      ItemModel.check();
      ExpiredItemManager.initCheck();
      PotentialData.initCheck();
   }

   private static void loadSystemScriptData(Connection conn) throws Exception {
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery("select id,data from system_script order by id");

      while(rs.next()) {
         int id = rs.getInt("id");
         InputStream in = rs.getBinaryStream("data");
         SystemScript.addScript(id, in);
      }

      rs.close();
      st.close();
   }

   private static void loadMessage() throws Exception {
      InputStream in = SystemScript.getScriptStrem(1);
      MessageText.init(in);
      in.close();
   }

   private static void loadCreatureTemplate() throws Exception {
      InputStream in = SystemScript.getScriptStrem(19);
      CreatureTemplate.initTemplate(in);
      in.close();
   }

   private static void loadMaterialTemplate() throws Exception {
      InputStream in = SystemScript.getScriptStrem(25);
      MaterialTemplate.init(in);
      in.close();
   }

   private static void loadStatNames() throws Exception {
      InputStream in = SystemScript.getScriptStrem(6);
      StatNames.init(in);
      in.close();
   }

   private static void loadRolePanelStats() throws Exception {
      InputStream in = SystemScript.getScriptStrem(16);
      StatList2Client.initXml(in);
      in.close();
   }

   private static void loadPlayerLevelData() throws Exception {
      InputStream in = SystemScript.getScriptStrem(9);
      PlayerLevelData.initLevelData(in);
      in.close();
   }

   private static void loadItemData() throws Exception {
      InputStream in = SystemScript.getScriptStrem(12);
      ItemModel.init(in);
      in.close();
   }

   private static void loadForging() throws Exception {
      InputStream in = SystemScript.getScriptStrem(33);
      ZhuijiaForgingData.init(in);
      in.close();
   }

   private static void loadEquipStatRule() throws Exception {
      InputStream in = SystemScript.getScriptStrem(22);
      EquipStatManager.init(in);
      in.close();
   }

   private static void loadDropData() throws Exception {
      InputStream in = SystemScript.getScriptStrem(27);
      DropModel.init(in);
      in.close();
   }

   private static void loadSkillData() throws Exception {
      InputStream in = SystemScript.getScriptStrem(17);
      SkillModel.init(in);
      in.close();
   }

   private static void loadDropControlData() throws Exception {
      InputStream in = SystemScript.getScriptStrem(31);
      DropControlManager.init(in);
      in.close();
   }

   private static void loadTriggerData() throws Exception {
      InputStream in = SystemScript.getScriptStrem(38);
      TriggerModel.init(in);
      in.close();
   }

   private static void loadTaskConfigs() throws Exception {
      InputStream in = SystemScript.getScriptStrem(18);
      TaskConfigManager.initConfigs(in);
      in.close();
   }

   private static void loadVIPConfigs() throws Exception {
      InputStream in = SystemScript.getScriptStrem(37);
      VIPConfigManager.initConfigs(in);
      in.close();
   }

   private static void loadFinancingConfigs() throws Exception {
      InputStream in = SystemScript.getScriptStrem(63);
      FinancingConfigManager.initConfigs(in);
      in.close();
   }

   private static void loadMallConfigs() throws Exception {
      InputStream in = SystemScript.getScriptStrem(56);
      MallConfigManager.initConfigs(in);
      in.close();
   }

   private static void loadRewardHallConfigs() throws Exception {
      InputStream in = SystemScript.getScriptStrem(57);
      RewardHallConfigManager.initConfigs(in);
      in.close();
   }

   private static void loadPetConfigs() throws Exception {
      InputStream in = SystemScript.getScriptStrem(43);
      PetConfigManager.initConfigs(in);
      in.close();
   }

   private static void loadShieldConfigs() throws Exception {
      InputStream in = SystemScript.getScriptStrem(61);
      ShieldConfigManager.initConfigs(in);
      in.close();
   }

   private static void loadTransferConfigs() throws Exception {
      InputStream in = SystemScript.getScriptStrem(46);
      TransferConfigManager.initConfigs(in);
      in.close();
   }

   private static void loadTanXianConfigs() throws Exception {
      InputStream in = SystemScript.getScriptStrem(89);
      TanXianConfigManager.initConfigs(in);
      in.close();
   }

   private static void loadSystemScript() throws Exception {
      SystemScript.initScript();
   }

   private static void loadMaps(Connection conn) throws Exception {
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery("select * from mu_map");

      while(rs.next()) {
         int id = rs.getInt("map_id");
         InputStream in = rs.getBinaryStream("map_data");
         byte[] bytes = new byte[in.available()];
         in.read(bytes, 0, bytes.length);
         in.close();
         MapData md = MapConfig.getMapData(id);
         if (md != null) {
            md.setConfigData(Zlib.decompressBytes(bytes));
         }
      }

      rs.close();
      st.close();
      MapConfig.initBaseMaps();
   }

   public static InputStream getSystemScriptData(Connection conn, int id) throws Exception {
      InputStream in = null;
      PreparedStatement ps = conn.prepareStatement("select data from system_script where id = ?");
      ps.setInt(1, id);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
         in = rs.getBinaryStream(1);
      }

      rs.close();
      ps.close();
      return in;
   }

   public static InputStream getSystemScriptData(int id) {
      Connection conn = null;
      InputStream in = null;

      try {
         conn = Pool.getGlobalConnection();
         PreparedStatement ps = conn.prepareStatement("select data from system_script where id = ?");
         ps.setInt(1, id);
         ResultSet rs = ps.executeQuery();
         if (rs.next()) {
            in = rs.getBinaryStream(1);
         }

         rs.close();
         ps.close();
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return in;
   }

   public static void getOpenServerTime() {
      Connection conn = Pool.getGlobalConnection();

      try {
         Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery(sqlGetOpenTIme);
         if (rs.next()) {
            String time = rs.getString(1);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Global.setOpenServerTime(df.parse(time));
            System.out.println("reset open server time = " + time);
         }

         rs.close();
         st.close();
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   private static void loadDungeonTemplate(Connection conn) throws Exception {
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery("select dungeon_id,dungeon_data from dungeon_template");

      while(rs.next()) {
         int id = rs.getInt("dungeon_id");
         InputStream in = rs.getBinaryStream("dungeon_data");
         DungeonTemplateFactory.initDungeonTemplate(id, in);
         in.close();
      }

      rs.close();
      st.close();
   }
}
