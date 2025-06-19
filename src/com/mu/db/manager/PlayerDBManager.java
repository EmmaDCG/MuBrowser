package com.mu.db.manager;

import com.mu.config.Global;
import com.mu.config.VariableConstant;
import com.mu.db.Pool;
import com.mu.executor.Executor;
import com.mu.executor.imp.drop.KillNumberExecutor;
import com.mu.executor.imp.skill.SaveSkillWhenOffLineExecutor;
import com.mu.game.CenterManager;
import com.mu.game.IDFactory;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.map.MapConfig;
import com.mu.game.model.map.MapData;
import com.mu.game.model.properties.levelData.PlayerLevelData;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.Profession;
import com.mu.game.model.unit.player.RoleInfo;
import com.mu.game.model.unit.player.User;
import com.mu.game.model.unit.player.dailyreceive.DailyReceiveLog;
import com.mu.game.model.unit.player.dailyreceive.DailyReceiveLogManager;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.sys.ListPacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import com.mu.utils.Time;
import com.mu.utils.buffer.BufferReader;
import com.mu.utils.buffer.BufferWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerDBManager {
   private static final String sqlSavePlayerVolatileInfo = "update mu_role set current_exp = ?,map_id = ?,last_x =? ,last_y = ?,logout_time = ?,current_sd = ?,today_online_time = ?,current_hp = ?,current_mp = ?, vip_exp=?,pk_status=?,evil=?,money=?,current_ag = ?,total_online_time = ?,zdl= ? where role_id = ?";
   private static final String sqlGetRoleList = "select * from mu_role where user_name = ? and delete_status = 0 and server_id = ? order by logout_time desc, role_level desc, create_time limit 5";
   private static final String sqlGetAllRoleList = "select * from mu_role where user_name = ? and delete_status = 0  order by  logout_time desc,role_level desc, create_time limit 5";
   private static final String sqlCheckName = "select count(role_id) from mu_role where lower(role_name) = lower(?) ";
   private static final String sqlCheckRoleNum = "select count(user_name) from mu_role where user_name = ? and server_id = ? and delete_status = 0 ";
   private static final String sqlGetRole = "select * from mu_role where role_id = ?";
   private static final String sqlUpdateVIPLevel = "update mu_role set vip_level = ? where role_id = ?";
   private static final String sqlInsertProtectionSkill = "replace into mu_protection_skill values(?,?,?,?)";
   private static final String sqlSearchMoney = "SELECT money FROM mu_role WHERE role_id = ?";
   private static final String sqlSaveMoney = "UPDATE mu_role SET money = ? WHERE role_id = ?";
   private static final String sqlSaveIngot = "update mu_user set ingot = ? where user_name = ? and server_id = ?";
   private static final String sqlSaveIngot_offLine = "update mu_user set ingot = ingot + ? where user_name = ? and server_id = ?";
   private static final String sqlSaveBindIngot = "UPDATE mu_role SET bind_ingot = ? WHERE role_id = ?";
   private static final String sqlCheckPwd = "select count(1) from mu_user where user_name = ? and user_pwd = ?";
   private static final String sqlSaveSetup = "update mu_role set system_setup = ? where role_id = ?";
   private static final String sqlInsertKillBossRecord = "insert into mu_kill_boss_record values (?,?,?,?)";
   private static final String sqlGetPlayerView = "select role_view from role_view where role_id = ?";
   private static final String sqlSaveView = "replace into role_view values(?,?)";
   private static final String sqlSavePreview = "update mu_role set finish_preview = ? where role_id = ?";
   private static final String saveFunctionOpen = "replace into role_funtion_open_time values (?,?,?)";
   private static final String getFunctionOpen = "select function_id,open_time from role_funtion_open_time where role_id = ?";
   private static final String sqlUpdatePoChange = "UPDATE mu_role SET potential = ?,potential_strength = ? ,potential_dexterity = ? ,potential_constitution = ?, potential_intellingence = ?,basis_strength = ? ,basis_dexterity = ? ,basis_constitution = ?,  basis_intellingence = ?  WHERE role_id = ?";
   private static final String sqlUpdateLevelChange = "UPDATE mu_role SET role_level = ?,basis_strength = ?, basis_dexterity = ?,basis_constitution = ?,basis_intellingence = ?,basis_max_hp = ?,basis_max_mp = ? ,basis_max_sd = ?,basis_max_ap = ?, basis_max_atk = ?,basis_min_atk = ?,basis_def = ?,basis_hit = ? ,basis_avd = ?,potential= ?,levelup_time = ? WHERE role_id = ?";
   private static final String sqlUpdateTransfer = "UPDATE mu_role SET profession_level = ? ,potential = ? WHERE role_id = ?";
   private static final String sqlSaveWarComment = "update mu_role set war_comment = ?,war_comment_time = ? where role_id = ?";
   private static final String sqlDeleteRole = "update mu_role set delete_status = 1 where user_name = ? and role_id = ?";
   private static final String sqlUpdateRedeemPoint = "UPDATE mu_role SET redeem_point = ? WHERE role_id = ?";
   private static final String sqlArrowGuide = "select arrow_id,arrow_times from mu_guide_logs where role_id = ?";
   private static final String sqlSaveGuide = "replace into mu_guide_logs values (?,?,?)";
   private static final String sqlGetSnReceive = "select gift_id from role_sn_recieve_logs where role_id = ?";
   private static final String sqlSaveSn = "replace into role_sn_recieve_logs values (?,?)";
   private static final String sqlSaveDailyLogs = "replace into role_daily_receive_logs values (?,?,?,?,?)";
   private static final String sqlGetDailyLogs = "select * from role_daily_receive_logs where role_id = ? and save_day = ?";
   private static final String saveEnemy = "replace into role_enemy values (?,?,?)";
   private static final String deleteEnemy = "delete from role_enemy where role_id = ? and enemy_id = ?";
   private static final String deleteAllEnemy = "delete from role_enemy where role_id = ?";
   private static final String sqlOffLineUpdateIngot = "update mu_user set ingot = ingot + ? where user_name = ? and server_id = ?";
   private static final String sqlSaveSevenDay = "REPLACE INTO role_sevenday_treasure VALUES(?,?,?)";
   private static final String sqlSearchSevenDay = "SELECT * FROM role_sevenday_treasure WHERE role_id = ?";
   private static final Logger logger = LoggerFactory.getLogger(PlayerDBManager.class);
   private static final String sqlInsertRole = "insert mu_role (role_id,user_name,role_name,server_id,role_header,profession,current_hp,current_mp,map_id,last_x,last_y,create_time,basis_strength,basis_dexterity,basis_constitution,basis_intellingence) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
   private static final String insertName = "insert into be_used_names values (?)";

   public static ArrayList getRoleInfoList(User user) {
      ArrayList list = new ArrayList();
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = null;
         if (!Global.isDebug() && !Global.isShowAllRoles()) {
            ps = conn.prepareStatement("select * from mu_role where user_name = ? and delete_status = 0 and server_id = ? order by logout_time desc, role_level desc, create_time limit 5");
            ps.setString(1, user.getName());
            ps.setInt(2, user.getServerID());
         } else {
            ps = conn.prepareStatement("select * from mu_role where user_name = ? and delete_status = 0  order by  logout_time desc,role_level desc, create_time limit 5");
            ps.setString(1, user.getName());
         }

         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            long id = rs.getLong("role_id");
            String name = rs.getString("role_name");
            int sex = rs.getInt("gender");
            int header = rs.getInt("role_header");
            int level = rs.getInt("role_level");
            int mapID = rs.getInt("map_id");
            int proType = rs.getInt("profession");
            int proLevel = rs.getInt("profession_level");
            int currentHp = rs.getInt("current_hp");
            int currentMp = rs.getInt("current_mp");
            int currentSd = rs.getInt("current_sd");
            int currentAg = rs.getInt("current_ag");
            int currentAp = rs.getInt("current_ap");
            int basisSTR = rs.getInt("basis_strength");
            int basisDEX = rs.getInt("basis_dexterity");
            int basisCON = rs.getInt("basis_constitution");
            int basisINT = rs.getInt("basis_intellingence");
            int basisMaxHp = rs.getInt("basis_max_hp");
            int basisMaxMp = rs.getInt("basis_max_mp");
            int basisMaxSD = rs.getInt("basis_max_sd");
            int basisMaxAG = rs.getInt("basis_max_ag");
            int basisMaxAP = rs.getInt("basis_max_ap");
            int basisMaxATK = rs.getInt("basis_max_atk");
            int basisMinATK = rs.getInt("basis_min_atk");
            int basisDEF = rs.getInt("basis_def");
            int basisHit = rs.getInt("basis_hit");
            int basisAVD = rs.getInt("basis_avd");
            int potential = rs.getInt("potential");
            int poSTR = rs.getInt("potential_strength");
            int poDEX = rs.getInt("potential_dexterity");
            int poCON = rs.getInt("potential_constitution");
            int poINT = rs.getInt("potential_intellingence");
            int pkStatus = rs.getInt("pk_status");
            int evil = rs.getInt("evil");
            int money = rs.getInt("money");
            byte[] setup = rs.getBytes("system_setup");
            int redeemPoint = rs.getInt("redeem_point");
            int bindIngot = rs.getInt("bind_ingot");
            RoleInfo info = new RoleInfo();
            info.setID(id);
            info.setGender(sex);
            info.setHeader(header);
            info.setLevel(level);
            info.setMapID(mapID);
            info.setName(name);
            info.setProType(proType);
            info.setProLevel(proLevel);
            info.setCurrentHp(currentHp);
            info.setCurrentMp(currentMp);
            info.setCurrentSd(currentSd);
            info.setCurrentAg(currentAg);
            info.setCurrentAp(currentAp);
            info.setBasisSTR(basisSTR);
            info.setBasisDEX(basisDEX);
            info.setBasisCON(basisCON);
            info.setBasisINT(basisINT);
            info.setBasisMaxHp(basisMaxHp);
            info.setBasisMaxMp(basisMaxMp);
            info.setBasisMaxSD(basisMaxSD);
            info.setBasisMaxAG(basisMaxAG);
            info.setBasisMaxAP(basisMaxAP);
            info.setBasisMaxATK(basisMaxATK);
            info.setBasisMinATK(basisMinATK);
            info.setBasisDEF(basisDEF);
            info.setBasisHit(basisHit);
            info.setBasisAVD(basisAVD);
            info.setPotential(potential);
            info.setPoSTR(poSTR);
            info.setPoDEX(poDEX);
            info.setPoCON(poCON);
            info.setPoINT(poINT);
            info.setPkStatus(pkStatus);
            info.setEvil(evil);
            info.setMoney(money);
            info.setSetup(setup);
            info.setRedeemPoint(redeemPoint);
            info.setBindIngot(bindIngot);
            HashMap itemMap = ItemDBManager.getItemByType(id, 0);
            Iterator it = itemMap.values().iterator();

            while(it.hasNext()) {
               Item item = (Item)it.next();
               if (item.getSlot() != 13) {
                  info.calExternal(item);
               }
            }

            itemMap.clear();
            itemMap = null;
            list.add(info);
         }

         rs.close();
         ps.close();
      } catch (Exception var51) {
         var51.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return list;
   }

   public static void savePlayerVolatileInfo(Game2GatewayPacket packet) {
      Connection conn = Pool.getConnection();

      try {
         long rid = packet.readLong();
         long exp = packet.readLong();
         int mapID = packet.readInt();
         int x = packet.readInt();
         int y = packet.readInt();
         long logOutTime = packet.readLong();
         int sd = packet.readInt();
         int todayOnlineTime = packet.readInt();
         int hp = packet.readInt();
         int mp = packet.readInt();
         int vipExp = packet.readInt();
         int pkStatus = packet.readByte();
         int evil = packet.readInt();
         int money = packet.readInt();
         int ag = packet.readInt();
         int totalOnlineTime = packet.readInt();
         int zdl = packet.readInt();
         PreparedStatement ps = conn.prepareStatement("update mu_role set current_exp = ?,map_id = ?,last_x =? ,last_y = ?,logout_time = ?,current_sd = ?,today_online_time = ?,current_hp = ?,current_mp = ?, vip_exp=?,pk_status=?,evil=?,money=?,current_ag = ?,total_online_time = ?,zdl= ? where role_id = ?");
         ps.setLong(1, exp);
         ps.setInt(2, mapID);
         ps.setInt(3, x);
         ps.setInt(4, y);
         ps.setLong(5, logOutTime);
         ps.setInt(6, sd);
         ps.setInt(7, todayOnlineTime);
         ps.setInt(8, hp);
         ps.setInt(9, mp);
         ps.setInt(10, vipExp);
         ps.setInt(11, pkStatus);
         ps.setInt(12, evil);
         ps.setInt(13, money);
         ps.setInt(14, ag);
         ps.setInt(15, totalOnlineTime);
         ps.setInt(16, zdl);
         ps.setLong(17, rid);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var26) {
         var26.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static int offLineUpdateIngot(String userName, int ingot, int serverId) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("update mu_user set ingot = ingot + ? where user_name = ? and server_id = ?");
         ps.setInt(1, ingot);
         ps.setString(2, userName);
         ps.setInt(3, serverId);
         int result = ps.executeUpdate();
         ps.close();
         if (result < 1) {
            return 0;
         }

         return 1;
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return -1;
   }

   public static void saveProtectionSkill(Game2GatewayPacket packet) {
      Connection conn = Pool.getConnection();

      try {
         long rid = packet.readLong();
         int id = packet.readUnsignedShort();
         int rank = packet.readUnsignedShort();
         int level = packet.readUnsignedShort();
         PreparedStatement ps = conn.prepareStatement("replace into mu_protection_skill values(?,?,?,?)");
         ps.setLong(1, rid);
         ps.setInt(2, id);
         ps.setInt(3, rank);
         ps.setInt(4, level);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var11) {
         var11.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void saveRedeemPoint(Game2GatewayPacket packet) {
      Connection conn = Pool.getConnection();

      try {
         long rid = packet.readLong();
         int redeemPoint = packet.readInt();
         PreparedStatement ps = conn.prepareStatement("UPDATE mu_role SET redeem_point = ? WHERE role_id = ?");
         ps.setInt(1, redeemPoint);
         ps.setLong(2, rid);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void saveArrowGuide(Game2GatewayPacket packet) {
      Connection conn = Pool.getConnection();

      try {
         long rid = packet.readLong();
         int id = packet.readUnsignedByte();
         int times = packet.readUnsignedByte();
         PreparedStatement ps = conn.prepareStatement("replace into mu_guide_logs values (?,?,?)");
         ps.setLong(1, rid);
         ps.setInt(2, id);
         ps.setInt(3, times);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void updateVIPLevel(long roleId, int vipLevel) {
      Connection conn = Pool.getConnection();

      try {
         PreparedStatement ps = conn.prepareStatement("update mu_role set vip_level = ? where role_id = ?");
         ps.setInt(1, vipLevel);
         ps.setLong(2, roleId);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static ArrayList getGuideList(long roleId) {
      Connection conn = Pool.getConnection();
      ArrayList list = new ArrayList();

      try {
         PreparedStatement ps = conn.prepareStatement("select arrow_id,arrow_times from mu_guide_logs where role_id = ?");
         ps.setLong(1, roleId);
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            list.add(new int[]{rs.getInt("arrow_id"), rs.getInt("arrow_times")});
         }

         rs.close();
         ps.close();
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return list;
   }

   public static void savePotentialChange(Game2GatewayPacket packet) {
      Connection conn = Pool.getConnection();

      try {
         long rid = packet.readLong();
         int potential = packet.readInt();
         int poSTR = packet.readInt();
         int poDEX = packet.readInt();
         int poCON = packet.readInt();
         int poINT = packet.readInt();
         int basisSTR = packet.readInt();
         int basisDEX = packet.readInt();
         int basisCON = packet.readInt();
         int basisINT = packet.readInt();
         PreparedStatement ps = conn.prepareStatement("UPDATE mu_role SET potential = ?,potential_strength = ? ,potential_dexterity = ? ,potential_constitution = ?, potential_intellingence = ?,basis_strength = ? ,basis_dexterity = ? ,basis_constitution = ?,  basis_intellingence = ?  WHERE role_id = ?");
         ps.setInt(1, potential);
         ps.setInt(2, poSTR);
         ps.setInt(3, poDEX);
         ps.setInt(4, poCON);
         ps.setInt(5, poINT);
         ps.setInt(6, basisSTR);
         ps.setInt(7, basisDEX);
         ps.setInt(8, basisCON);
         ps.setInt(9, basisINT);
         ps.setLong(10, rid);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var17) {
         var17.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void saveFunctionOpen(long rid, int functionId, long time) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("replace into role_funtion_open_time values (?,?,?)");
         ps.setLong(1, rid);
         ps.setInt(2, functionId);
         ps.setLong(3, time);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static ArrayList getFunctionOpen(long rid) {
      ArrayList list = new ArrayList();
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select function_id,open_time from role_funtion_open_time where role_id = ?");
         ps.setLong(1, rid);
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            int id = rs.getInt("function_id");
            long time = rs.getLong("open_time");
            list.add(new long[]{(long)id, time});
         }

         rs.close();
         ps.close();
      } catch (Exception var12) {
         var12.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return list;
   }

   public static void saveWhenTransfer(Game2GatewayPacket packet) {
      Connection conn = Pool.getConnection();

      try {
         long rid = packet.readLong();
         int potential = packet.readInt();
         int proLevel = packet.readByte();
         PreparedStatement ps = conn.prepareStatement("UPDATE mu_role SET profession_level = ? ,potential = ? WHERE role_id = ?");
         ps.setInt(1, proLevel);
         ps.setInt(2, potential);
         ps.setLong(3, rid);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static boolean checkPwd(String userName, String pwd) {
      Connection conn = Pool.getConnection();
      boolean b = false;

      try {
         PreparedStatement ps = conn.prepareStatement("select count(1) from mu_user where user_name = ? and user_pwd = ?");
         ps.setString(1, userName);
         ps.setString(2, pwd);
         ResultSet rs = ps.executeQuery();
         if (rs.next()) {
            int count = rs.getInt(1);
            b = count > 0;
         }

         rs.close();
         ps.close();
         boolean var8 = b;
         return var8;
      } catch (Exception var11) {
         var11.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return false;
   }

   public static void savePlayerIngot(Game2GatewayPacket packet) {
      try {
         String userName = packet.readUTF();
         int ingot = packet.readInt();
         int serverId = packet.readInt();
         savePlayerIngot(userName, ingot, serverId);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void savePlayerIngot(String userName, int ingot, int serverId) {
      Connection conn = Pool.getConnection();

      try {
         PreparedStatement ps = conn.prepareStatement("update mu_user set ingot = ? where user_name = ? and server_id = ?");
         ps.setInt(1, ingot);
         ps.setString(2, userName);
         ps.setInt(3, serverId);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void saveEnemy(long rid, long enemyId, int killTimes) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("replace into role_enemy values (?,?,?)");
         ps.setLong(1, rid);
         ps.setLong(2, enemyId);
         ps.setInt(3, killTimes);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void deleteEnemy(long rid, long enemyId) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = null;
         if (enemyId < 0L) {
            ps = conn.prepareStatement("delete from role_enemy where role_id = ?");
            ps.setLong(1, rid);
         } else {
            ps = conn.prepareStatement("delete from role_enemy where role_id = ? and enemy_id = ?");
            ps.setLong(1, rid);
            ps.setLong(2, enemyId);
         }

         ps.executeUpdate();
         ps.close();
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void saveOffLinePlayerMoney(long roleID, int addMoney) {
      Connection conn = Pool.getConnection();

      try {
         int money = 0;
         PreparedStatement ps = conn.prepareStatement("SELECT money FROM mu_role WHERE role_id = ?");
         ps.setLong(1, roleID);
         ResultSet rs = ps.executeQuery();
         if (rs.next()) {
            money = rs.getInt(1);
         }

         rs.close();
         ps.close();
         money += Integer.MAX_VALUE - money > addMoney ? addMoney : Integer.MAX_VALUE - money;
         PreparedStatement updatePs = conn.prepareStatement("UPDATE mu_role SET money = ? WHERE role_id = ?");
         updatePs.setInt(1, money);
         updatePs.setLong(2, roleID);
         updatePs.executeUpdate();
         updatePs.close();
      } catch (Exception var11) {
         var11.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void saveOffLinePlayerIngot(String userName, int serverId, int addIngot) {
      Connection conn = Pool.getConnection();

      try {
         PreparedStatement updatePs = conn.prepareStatement("update mu_user set ingot = ingot + ? where user_name = ? and server_id = ?");
         updatePs.setInt(1, addIngot);
         updatePs.setString(2, userName);
         updatePs.setInt(3, serverId);
         updatePs.executeUpdate();
         updatePs.close();
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void savePlayerBindIngot(Game2GatewayPacket packet) {
      Connection conn = Pool.getConnection();

      try {
         int bindIngot = packet.readInt();
         long roleID = packet.readLong();
         PreparedStatement ps = conn.prepareStatement("UPDATE mu_role SET bind_ingot = ? WHERE role_id = ?");
         ps.setInt(1, bindIngot);
         ps.setLong(2, roleID);
         ps.execute();
         ps.close();
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void savePreview(long rid, int preview) {
      Connection conn = Pool.getConnection();

      try {
         PreparedStatement ps = conn.prepareStatement("update mu_role set finish_preview = ? where role_id = ?");
         ps.setInt(1, preview);
         ps.setLong(2, rid);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void saveLevelChange(Game2GatewayPacket packet) {
      Connection conn = Pool.getConnection();

      try {
         long rid = packet.readLong();
         int level = packet.readShort();
         int basisSTR = packet.readInt();
         int basisDEX = packet.readInt();
         int basisCON = packet.readInt();
         int basisINT = packet.readInt();
         int basisMaxHp = packet.readInt();
         int basisMaxMp = packet.readInt();
         int basisMaxSD = packet.readInt();
         int basisMaxAp = packet.readInt();
         int basisMaxAtk = packet.readInt();
         int basisMinAtk = packet.readInt();
         int basisDef = packet.readInt();
         int basisHit = packet.readInt();
         int basisAvd = packet.readInt();
         int potential = packet.readInt();
         long levelUpTime = packet.readLong();
         PreparedStatement ps = conn.prepareStatement("UPDATE mu_role SET role_level = ?,basis_strength = ?, basis_dexterity = ?,basis_constitution = ?,basis_intellingence = ?,basis_max_hp = ?,basis_max_mp = ? ,basis_max_sd = ?,basis_max_ap = ?, basis_max_atk = ?,basis_min_atk = ?,basis_def = ?,basis_hit = ? ,basis_avd = ?,potential= ?,levelup_time = ? WHERE role_id = ?");
         ps.setInt(1, level);
         ps.setInt(2, basisSTR);
         ps.setInt(3, basisDEX);
         ps.setInt(4, basisCON);
         ps.setInt(5, basisINT);
         ps.setInt(6, basisMaxHp);
         ps.setInt(7, basisMaxMp);
         ps.setInt(8, basisMaxSD);
         ps.setInt(9, basisMaxAp);
         ps.setInt(10, basisMaxAtk);
         ps.setInt(11, basisMinAtk);
         ps.setInt(12, basisDef);
         ps.setInt(13, basisHit);
         ps.setInt(14, basisAvd);
         ps.setInt(15, potential);
         ps.setLong(16, levelUpTime);
         ps.setLong(17, rid);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var25) {
         var25.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void savePlayerVolatileInfo() {
      if (CenterManager.getOnlinePlayerSize() != 0) {
         Connection conn = null;
         PreparedStatement ps = null;

         try {
            conn = Pool.getConnection();
            ps = conn.prepareStatement("update mu_role set current_exp = ?,map_id = ?,last_x =? ,last_y = ?,logout_time = ?,current_sd = ?,today_online_time = ?,current_hp = ?,current_mp = ?, vip_exp=?,pk_status=?,evil=?,money=?,current_ag = ?,total_online_time = ?,zdl= ? where role_id = ?");
            Iterator it = CenterManager.getAllPlayerMap().values().iterator();

            while(it.hasNext()) {
               Player p = (Player)it.next();
               if (!p.isDestroy()) {
                  ps.setLong(1, p.getCurretntExp());
                  ps.setInt(2, p.getWorldMapID());
                  ps.setInt(3, p.getWorldMapPoint().x);
                  ps.setInt(4, p.getWorldMapPoint().y);
                  ps.setLong(5, p.getLogoutTime());
                  ps.setInt(6, p.getSd());
                  ps.setInt(7, p.getTodayOnlineTime());
                  ps.setInt(8, p.getHp());
                  ps.setInt(9, p.getMp());
                  ps.setInt(10, p.getVIPExp());
                  ps.setInt(11, p.getPkModelIDToDB());
                  ps.setInt(12, p.getEvil());
                  ps.setInt(13, p.getMoney());
                  ps.setInt(14, p.getAg());
                  ps.setInt(15, p.getTotalOnlineTime());
                  ps.setInt(16, p.getStatValue(StatEnum.DOMINEERING));
                  ps.setLong(17, p.getID());
                  ps.addBatch();
               }
            }

            ps.executeBatch();
         } catch (Exception var7) {
            var7.printStackTrace();
         } finally {
            Pool.closeStatment(ps);
            Pool.closeConnection(conn);
         }

      }
   }

   private static boolean checkName(Connection conn, String name) {
      try {
         PreparedStatement ps = conn.prepareStatement("select count(role_id) from mu_role where lower(role_name) = lower(?) ");
         ps.setString(1, name);
         ResultSet rs = ps.executeQuery();
         int count = 0;
         if (rs.next()) {
            count = rs.getInt(1);
         }

         rs.close();
         ps.close();
         return count <= 0;
      } catch (Exception var5) {
         var5.printStackTrace();
         return false;
      }
   }

   private static boolean checkRoleNum(Connection conn, String userName, int serverID) {
      try {
         PreparedStatement ps = conn.prepareStatement("select count(user_name) from mu_role where user_name = ? and server_id = ? and delete_status = 0 ");
         ps.setString(1, userName);
         ps.setInt(2, serverID);
         ResultSet rs = ps.executeQuery();
         int count = 0;
         if (rs.next()) {
            count = rs.getInt(1);
         }

         rs.close();
         ps.close();
         return count <= 2;
      } catch (Exception var6) {
         var6.printStackTrace();
         return false;
      }
   }

   private static int checkBeforeCreateRole(String name, Player player) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         if (!checkName(conn, name)) {
            return 1002;
         }

         if (!checkRoleNum(conn, player.getUserName(), player.getUser().getServerID())) {
            return 1001;
         }
      } catch (Exception var7) {
         var7.printStackTrace();
         return 2;
      } finally {
         Pool.closeConnection(conn);
      }

      return 1;
   }

   public static long[] createRole(Player player, int profession, int hair, String name) {
      long[] result = new long[]{1L, -1L};
      Connection conn = null;

      long[] var23;
      try {
         String finalName = VariableConstant.NamePrefix.replace("%", String.valueOf(player.getUser().getServerID())) + name;
         int checkResult = checkBeforeCreateRole(finalName, player);
         if (checkResult == 1) {
            PlayerLevelData levelData = PlayerLevelData.getLevelData(profession, 1);
            MapData md = MapConfig.getMapData(10001);
            conn = Pool.getConnection();
            conn.setAutoCommit(false);
            int proId = Profession.getProID(profession, 0);
            PreparedStatement psRole = conn.prepareStatement("insert mu_role (role_id,user_name,role_name,server_id,role_header,profession,current_hp,current_mp,map_id,last_x,last_y,create_time,basis_strength,basis_dexterity,basis_constitution,basis_intellingence) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            long id = IDFactory.getRoleID();
            psRole.setLong(1, id);
            psRole.setString(2, player.getUserName());
            psRole.setString(3, finalName);
            psRole.setInt(4, player.getUser().getServerID());
            psRole.setInt(5, ((Profession)Profession.getProfessions().get(proId)).getHeader());
            psRole.setInt(6, profession);
            psRole.setInt(7, 2000);
            psRole.setInt(8, 1000);
            psRole.setInt(9, md.getMapID());
            psRole.setInt(10, md.getDefaultX());
            psRole.setInt(11, md.getDefaultY());
            psRole.setLong(12, System.currentTimeMillis());
            psRole.setInt(13, levelData.getStr());
            psRole.setInt(14, levelData.getDex());
            psRole.setInt(15, levelData.getCon());
            psRole.setInt(16, levelData.getIntell());
            psRole.executeUpdate();
            PreparedStatement psName = conn.prepareStatement("insert into be_used_names values (?)");
            psName.setString(1, name);
            psName.executeUpdate();
            conn.commit();
            psRole.close();
            psName.close();
            result[1] = id;
            ArrayList equipList = Global.getDefaultEquipment(profession);
            if (equipList != null && equipList.size() > 0) {
               Iterator var17 = equipList.iterator();

               while(var17.hasNext()) {
                  int[] in = (int[])var17.next();
                  int model = in[0];
                  int slot = in[1];
                  int star = in[2];
                  if (ItemModel.getModel(model) != null) {
                     Item d1 = ItemTools.createItem(model, 1, 1);
                     d1.setContainerType(0);
                     d1.setSlot(slot);
                     d1.setStarLevel(star);
                     ItemDBManager.insertItem(id, d1);
                  }
               }
            }

            var23 = result;
            return var23;
         }

         result[0] = (long)checkResult;
         var23 = result;
      } catch (Exception var34) {
         try {
            if (conn != null) {
               conn.rollback();
            }
         } catch (Exception var33) {
            var33.printStackTrace();
         }

         var34.printStackTrace();
         result[0] = 2L;
         var23 = result;
         return var23;
      } finally {
         try {
            if (conn != null) {
               conn.setAutoCommit(true);
            }
         } catch (Exception var32) {
            var32.printStackTrace();
         }

         Pool.closeConnection(conn);
      }

      return var23;
   }

   public static byte[] getRoleInfoBytes(long rid) {
      Connection conn = Pool.getConnection();
      BufferWriter writer = new BufferWriter();
      byte[] bytes = null;

      try {
         PreparedStatement ps = conn.prepareStatement("select * from mu_role where role_id = ?");
         ps.setLong(1, rid);
         ResultSet rs = ps.executeQuery();
         if (rs.next()) {
            writer.writeLong(rid);
            writer.writeUTF(rs.getString("role_name"));
            writer.writeByte(rs.getInt("gender"));
            writer.writeInt(rs.getInt("role_header"));
            writer.writeShort(rs.getInt("role_level"));
            writer.writeByte(rs.getInt("profession"));
            writer.writeByte(rs.getInt("profession_level"));
            writer.writeShort(rs.getInt("map_id"));
            writer.writeInt(rs.getInt("last_x"));
            writer.writeInt(rs.getInt("last_y"));
            writer.writeLong(rs.getLong("current_exp"));
            writer.writeInt(rs.getInt("current_hp"));
            writer.writeInt(rs.getInt("current_mp"));
            writer.writeInt(rs.getInt("current_sd"));
            writer.writeInt(rs.getInt("current_ag"));
            writer.writeInt(rs.getInt("current_ap"));
            writer.writeByte(rs.getInt("vip_level"));
            writer.writeInt(rs.getInt("vip_exp"));
            writer.writeLong(rs.getLong("logout_time"));
            writer.writeInt(rs.getInt("today_online_time"));
            writer.writeInt(rs.getInt("total_online_time"));
            int basisSTR = rs.getInt("basis_strength");
            int basisDEX = rs.getInt("basis_dexterity");
            int basisCON = rs.getInt("basis_constitution");
            int basisINT = rs.getInt("basis_intellingence");
            int basisMaxHp = rs.getInt("basis_max_hp");
            int basisMaxMp = rs.getInt("basis_max_mp");
            int basisMaxSD = rs.getInt("basis_max_sd");
            int basisMaxAG = rs.getInt("basis_max_ag");
            int basisMaxAP = rs.getInt("basis_max_ap");
            int basisMaxATK = rs.getInt("basis_max_atk");
            int basisMinATK = rs.getInt("basis_min_atk");
            int basisDEF = rs.getInt("basis_def");
            int basisHit = rs.getInt("basis_hit");
            int basisAVD = rs.getInt("basis_avd");
            int potential = rs.getInt("potential");
            int poSTR = rs.getInt("potential_strength");
            int poDEX = rs.getInt("potential_dexterity");
            int poCON = rs.getInt("potential_constitution");
            int poINT = rs.getInt("potential_intellingence");
            int pkStatus = rs.getInt("pk_status");
            int evil = rs.getInt("evil");
            int money = rs.getInt("money");
            int warComment = rs.getInt("war_comment");
            long warCommentTime = rs.getLong("war_comment_time");
            int finishPreview = rs.getInt("finish_preview");
            byte[] setup = rs.getBytes("system_setup");
            int redeemPoint = rs.getInt("redeem_point");
            int bindIngot = rs.getInt("bind_ingot");
            writer.writeInt(basisSTR);
            writer.writeInt(basisDEX);
            writer.writeInt(basisCON);
            writer.writeInt(basisINT);
            writer.writeInt(basisMaxHp);
            writer.writeInt(basisMaxMp);
            writer.writeInt(basisMaxSD);
            writer.writeInt(basisMaxAG);
            writer.writeInt(basisMaxAP);
            writer.writeInt(basisMaxATK);
            writer.writeInt(basisMinATK);
            writer.writeInt(basisDEF);
            writer.writeInt(basisHit);
            writer.writeInt(basisAVD);
            writer.writeInt(potential);
            writer.writeInt(poSTR);
            writer.writeInt(poDEX);
            writer.writeInt(poCON);
            writer.writeInt(poINT);
            writer.writeByte(pkStatus);
            writer.writeInt(evil);
            writer.writeInt(money);
            if (setup == null) {
               writer.writeShort(0);
            } else {
               writer.writeShort(setup.length);
               writer.writeBytes(setup);
            }

            writer.writeShort(warComment);
            writer.writeLong(warCommentTime);
            writer.writeShort(finishPreview);
            writer.writeInt(redeemPoint);
            writer.writeInt(bindIngot);
            writer.writeUTF(Global.getLocalHost());
            writer.writeInt(Global.getGamePort());
            bytes = writer.toByteArray();
         }

         rs.close();
         ps.close();
      } catch (Exception var39) {
         var39.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
         writer.destroy();
      }

      return bytes;
   }

   public static void saveWarComment(Game2GatewayPacket packet) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         long id = packet.readLong();
         int wc = packet.readShort();
         long time = packet.readLong();
         PreparedStatement ps = conn.prepareStatement("update mu_role set war_comment = ?,war_comment_time = ? where role_id = ?");
         ps.setInt(1, wc);
         ps.setLong(2, time);
         ps.setLong(3, id);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var11) {
         var11.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void saveSetup(Game2GatewayPacket packet) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         long id = packet.readLong();
         int size = packet.readShort();
         byte[] bytes = new byte[size];
         packet.readBytes(bytes);
         PreparedStatement ps = conn.prepareStatement("update mu_role set system_setup = ? where role_id = ?");
         ps.setBytes(1, bytes);
         ps.setLong(2, id);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static RoleInfo getRoleInfo(long rid) {
      return getRoleInfo(getRoleInfoBytes(rid));
   }

   public static RoleInfo getRoleInfo(byte[] bytes) {
      if (bytes == null) {
         return null;
      } else {
         BufferReader reader = new BufferReader(bytes);
         RoleInfo info = null;

         try {
            info = new RoleInfo();
            info.setID(reader.readLong());
            info.setName(reader.readUTF());
            info.setGender(reader.readByte());
            info.setHeader(reader.readInt());
            info.setLevel(reader.readUnsignedShort());
            info.setProType(reader.readByte());
            info.setProLevel(reader.readByte());
            info.setMapID(reader.readUnsignedShort());
            info.setX(reader.readInt());
            info.setY(reader.readInt());
            info.setCurrentExp(reader.readLong());
            info.setCurrentHp(reader.readInt());
            info.setCurrentMp(reader.readInt());
            info.setCurrentSd(reader.readInt());
            info.setCurrentAg(reader.readInt());
            info.setCurrentAp(reader.readInt());
            info.setVipLevel(reader.readUnsignedByte());
            info.setVipExp(reader.readInt());
            long lastLogoutTime = reader.readLong();
            info.setLogoutTime(lastLogoutTime);
            int todayOnlineTime = reader.readInt();
            int totalOnlineTime = reader.readInt();
            if (Time.isSameDay(lastLogoutTime, System.currentTimeMillis())) {
               info.setTodayOnlineTime(todayOnlineTime);
            } else {
               info.setTodayOnlineTime(0);
               info.setTodayOnlineClearTime(todayOnlineTime);
            }

            info.setTotalOnlineTime(totalOnlineTime);
            info.setBasisSTR(reader.readInt());
            info.setBasisDEX(reader.readInt());
            info.setBasisCON(reader.readInt());
            info.setBasisINT(reader.readInt());
            info.setBasisMaxHp(reader.readInt());
            info.setBasisMaxMp(reader.readInt());
            info.setBasisMaxSD(reader.readInt());
            info.setBasisMaxAG(reader.readInt());
            info.setBasisMaxAP(reader.readInt());
            info.setBasisMaxATK(reader.readInt());
            info.setBasisMinATK(reader.readInt());
            info.setBasisDEF(reader.readInt());
            info.setBasisHit(reader.readInt());
            info.setBasisAVD(reader.readInt());
            info.setPotential(reader.readInt());
            info.setPoSTR(reader.readInt());
            info.setPoDEX(reader.readInt());
            info.setPoCON(reader.readInt());
            info.setPoINT(reader.readInt());
            info.setPkStatus(reader.readByte());
            info.setEvil(reader.readInt());
            info.setMoney(reader.readInt());
            int setupSize = reader.readShort();
            byte[] setup = null;
            if (setupSize > 0) {
               byte[] setup2 = new byte[setupSize];
               reader.readBytes(setup2);
               info.setSetup(setup2);
            }

            info.setWarComment(reader.readShort());
            info.setWarCommentTime(reader.readLong());
            info.setFinishPreivew(reader.readShort());
            info.setRedeemPoint(reader.readInt());
            info.setBindIngot(reader.readInt());
            info.setRemoteHost(reader.readUTF());
            info.setRemotePort(reader.readInt());
         } catch (Exception var12) {
            var12.printStackTrace();
         } finally {
            reader.destroy();
         }

         return info;
      }
   }

   public static void insertKillBossRecord(int bossId, long killerID, String killerName) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("insert into mu_kill_boss_record values (?,?,?,?)");
         ps.setInt(1, bossId);
         ps.setLong(2, killerID);
         ps.setString(3, killerName);
         ps.setLong(4, System.currentTimeMillis());
         ps.executeUpdate();
         ps.close();
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static boolean deleteRole(String userName, long rid) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("update mu_role set delete_status = 1 where user_name = ? and role_id = ?");
         ps.setString(1, userName);
         ps.setLong(2, rid);
         ps.executeUpdate();
         ps.close();
         return true;
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return false;
   }

   public static ArrayList getSnLogs(long rid) {
      Connection conn = null;
      ArrayList list = new ArrayList();

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select gift_id from role_sn_recieve_logs where role_id = ?");
         ps.setLong(1, rid);
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            list.add(rs.getInt("gift_id"));
         }

         rs.close();
         ps.close();
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return list;
   }

   public static boolean saveSnLogs(long rid, int id) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("replace into role_sn_recieve_logs values (?,?)");
         ps.setLong(1, rid);
         ps.setInt(2, id);
         ps.executeUpdate();
         ps.close();
         return true;
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return false;
   }

   public static byte[] getRoleView(long rid) {
      Connection conn = null;
      byte[] bytes = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select role_view from role_view where role_id = ?");
         ps.setLong(1, rid);
         ResultSet rs = ps.executeQuery();
         if (rs.next()) {
            bytes = rs.getBytes("role_view");
         }

         rs.close();
         ps.close();
         byte[] var7 = bytes;
         return var7;
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return null;
   }

   public static void saveRoleView(long rid, byte[] bytes) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("replace into role_view values(?,?)");
         ps.setLong(1, rid);
         ps.setBytes(2, bytes);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void saveDailyLogs(Game2GatewayPacket packet) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("replace into role_daily_receive_logs values (?,?,?,?,?)");
         long rid = packet.readLong();
         int type = packet.readShort();
         int times = packet.readShort();
         long day = packet.readLong();
         int hour = packet.readByte();
         ps.setLong(1, rid);
         ps.setInt(2, type);
         ps.setInt(3, times);
         ps.setLong(4, day);
         ps.setInt(5, hour);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var13) {
         var13.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static ArrayList getDailyReceiveLog(long rid) {
      Connection conn = null;
      ArrayList list = new ArrayList();

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select * from role_daily_receive_logs where role_id = ? and save_day = ?");
         ps.setLong(1, rid);
         ps.setLong(2, Time.getDayLong());
         ResultSet rs = ps.executeQuery();

         while(true) {
            int type;
            int times;
            long day;
            int hour;
            long sDay;
            do {
               if (!rs.next()) {
                  rs.close();
                  ps.close();
                  return list;
               }

               type = rs.getInt("type_id");
               times = rs.getInt("times");
               day = rs.getLong("save_day");
               hour = rs.getInt("save_hour");
               sDay = DailyReceiveLogManager.getReceiveDay(type);
            } while(sDay > 0L && sDay != day);

            DailyReceiveLog log = new DailyReceiveLog(type);
            log.setDay(day);
            log.setHour(hour);
            log.setTimes(times);
            list.add(log);
         }
      } catch (Exception var17) {
         var17.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return list;
   }

   public static void saveSevenDayTreasure(Game2GatewayPacket packet) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("REPLACE INTO role_sevenday_treasure VALUES(?,?,?)");
         long rid = packet.readLong();
         int loginDays = packet.readInt();
         String foundIndexes = packet.readUTF();
         ps.setLong(1, rid);
         ps.setInt(2, loginDays);
         ps.setString(3, foundIndexes);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static String[] searchSevenDayTreasure(long roleID) {
      String[] data = new String[]{"0", ""};
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("SELECT * FROM role_sevenday_treasure WHERE role_id = ?");
         ps.setLong(1, roleID);
         ResultSet rs = ps.executeQuery();
         if (rs.next()) {
            int loginDays = rs.getInt("login_days");
            String foundIndexes = rs.getString("found_indexes");
            data[0] = "" + loginDays;
            data[1] = foundIndexes;
         }

         rs.close();
         ps.close();
      } catch (Exception var11) {
         var11.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return data;
   }

   public static void saveForDestroy(Player player, Channel channel) {
      try {
         ListPacket lp = new ListPacket();
         lp.addPacket(Executor.SaveOnlinePlayer.toPacket(player));
         if (player.getShortcut().isChange()) {
            lp.addPacket(Executor.SaveShortcut.toPacket(player));
         }

         if (player.getDropManager().isChange()) {
            lp.addPacket(Executor.DropDayCount.toPacket(player));
         }

         if (KillNumberExecutor.needSave(player)) {
            lp.addPacket(Executor.KillNumber.toPacket(player));
         }

         if (SaveSkillWhenOffLineExecutor.needSaveSkills(player)) {
            lp.addPacket(Executor.SaveSkillWhenOffline.toPacket(player));
         }

         lp.addPacket(Executor.SaveBuffWhenOffLine.toPacket(player));
         lp.addPacket(Executor.SaveUser.toPacket(player.getUser()));
         lp.addPacket(Executor.SaveSetup.toPacket(player));
         if (player.getGameHang().isUpdate()) {
            lp.addPacket(Executor.SaveHangset.toPacket(player));
         }

         lp.addPacket(Executor.SavePlayerView.toPacket(player));
         lp.addPacket(Executor.SaveLogInOut.toPacket(player, Integer.valueOf(3)));
         if (player.getBackpack().getLimit() < 98) {
            lp.addPacket(Executor.UpdateStoragePage.toPacket(player, player.getBackpack()));
         }

         if (player.getSevenManager().isSave()) {
            lp.addPacket(Executor.SaveSevenDayTreasure.toPacket(player));
         }

         if (player.getSpiritManager().needToSave()) {
            lp.addPacket(Executor.SaveSpirit.toPacket(player));
         }

         WriteOnlyPacket achPacket = player.getAchievementManager().createOfflinePacket();
         if (achPacket != null) {
            lp.addPacket(achPacket);
         }

         switch(player.getDestroyType()) {
         case 3:
            lp.addPacket(Executor.ReLoginInterServer.toPacket(player.getUserName()));
            break;
         case 4:
            lp.addPacket(Executor.ToRemoteServer.toPacket(player, Integer.valueOf(2)));
            break;
         case 5:
            lp.addPacket(Executor.ToRemoteServer.toPacket(player, Integer.valueOf(3)));
            break;
         default:
            lp.addPacket(Executor.CloseGameChannel.toPacket());
         }

         channel.write(lp.toBuffer());
         lp.destroy();
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
