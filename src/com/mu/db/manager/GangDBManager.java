package com.mu.db.manager;

import com.mu.config.BroadcastManager;
import com.mu.config.Global;
import com.mu.db.Pool;
import com.mu.game.IDFactory;
import com.mu.game.dungeon.DungeonManager;
import com.mu.game.model.gang.ApplyInfo;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.gang.GangMember;
import com.mu.game.model.gang.GangNotice;
import com.mu.game.model.gang.GangWarRankInfo;
import com.mu.game.model.gang.RedPacket;
import com.mu.game.model.gang.RedPacketInfo;
import com.mu.game.model.gang.RedPacketReceiveRecord;
import com.mu.game.model.task.target.TargetType;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.game.model.unit.player.Profession;
import com.mu.io.game.packet.imp.gang.GangPlayerAttr;
import com.mu.io.game.packet.imp.gang.GangPlayerIn;
import com.mu.io.game.packet.imp.player.ChangePlayerGangName;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.Time;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

public class GangDBManager {
   private static final String sqlCreateGang = "{ call create_gang(?,?,?,?,?,?,?)}";
   private static final String sqlUpdateDes = "update mu_gang set gang_des = ?,des_editor = ? ,des_edit_time = ? where gang_id = ?";
   private static final String sqlGetAllGang = "select * from mu_gang";
   private static final String sqlGetAllMembers = "select a.role_id,a.gang_id,a.gang_post,a.war_post,a.vip_tag,a.cur_contribution,a.his_contribution, b.role_level,b.role_name,b.profession,b.profession_level,b.user_name,b.server_id, b.logout_time from mu_gang_member a,mu_role b where a.role_id = b.role_id";
   private static final String sqlGetAllApplys = "select a.role_id,a.gang_id,a.apply_time,a.vip_tag, b.role_level,b.role_name,b.profession,b.user_name from mu_gang_apply a,mu_role b where a.role_id = b.role_id";
   private static final String sqlInsertApply = "replace into mu_gang_apply values (?,?,?,?)";
   private static final String sqlDelteApply = "delete from mu_gang_apply where role_id = ? and gang_id = ?";
   private static final String sqlClearPlayerApply = "delete from mu_gang_apply where role_id = ?";
   private static final String sqlDelteNotice = "delete from mu_gang_notice where gang_id = ? and info_time <= ?";
   private static final String sqlInsertNotice = "insert into mu_gang_notice values (?,?,?)";
   private static final String sqlGetAllNotice = "select * from mu_gang_notice";
   private static final String sqlInsertMember = "replace into mu_gang_member values (?,?,?,?,?,?,?,?)";
   private static final String sqlDelteMember = "delete from mu_gang_member where role_id = ?";
   private static final String sqlDelteGang = "delete from mu_gang where gang_id = ?";
   private static final String sqlUpdateLevel = "update mu_gang set gang_level = ? where gang_id = ?";
   private static final String sqlInsertWelfare = "replace into mu_gang_welfare_logs values (?,?)";
   private static final String sqlGetWelfare = "select * from mu_gang_welfare_logs where receive_time = ?";
   private static final String sqlUpdatePost = "update mu_gang_member set gang_post = ? where role_id = ? ";
   private static final String sqlUpdateGangWarQualification = "replace into mu_gang_war_qualification values(?,?,?,?,?)";
   private static final String sqlinitGangWarQualification = "select * from mu_gang_war_qualification where war_date = ?";
   private static final String sqlGetWarReceive = "select * from mu_gang_war_receive_logs where receive_time = ?";
   private static final String sqlInsertWarReceive = "replace into mu_gang_war_receive_logs values (?,?)";
   private static final String sqlUpdateWarPost = "update mu_gang_member set war_post = ? where role_id = ?";
   private static final String sqlUpdateVipTag = "update mu_gang_member set vip_tag = ? where role_id = ?";
   private static final String sqlClearWarPost = "update mu_gang_member set war_post = 0 where gang_id = ?";
   private static final String updateVictoryGang = "update mu_gang set is_war_winner = ? where gang_id = ?";
   private static final String saveRedPacket = "insert into role_red_packet values (?,?,?,?,?,?,?,?,?,?)";
   private static final String updateRedPacket = "update role_red_packet set left_ingot = ? , is_end = ? where packet_id = ?";
   private static final String saveRedPacketReceive = "insert into role_red_packet_receive values (?,?,?,?,?,?,?)";
   private static final String getRedPacket = "select a.*,b.role_name from role_red_packet a,mu_role b where a.is_end = 0 and a.role_id = b.role_id";
   private static final String getRedPacketReceive = "select a.packet_id,a.role_id,a.receive_time,a.receive_day,a.receive_ingot,b.gang_id,c.role_name from role_red_packet_receive a,role_red_packet b,mu_role c where a.packet_id = b.packet_id and b.is_end = 0 and a.role_id = c.role_id";
   private static final String getRoleBindSendTimes = "select red_id, count(1) as num  from role_red_packet where user_name = ? and server_id = ? and packet_type = 0 group by red_id";
   private static final String getTodayBindReceiveTimes = "select count(1) from role_red_packet_receive where role_id = ? and receive_day = ? and red_type = 0";
   private static final String saveSummonRecord = "replace into gang_summon_record values (?,?,?,?)";
   private static final String sqlgetSummonRecord = "select * from gang_summon_record where save_day = ?";

   public static void initWarReceiveLogs() {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select * from mu_gang_war_receive_logs where receive_time = ?");
         ps.setLong(1, Time.getDayLong());
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            long rid = rs.getLong("role_id");
            long day = rs.getLong("receive_time");
            GangManager.addWarReceiveLog(rid, day);
         }

         rs.close();
         ps.close();
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void insertWarReceiveLog(long rid) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("replace into mu_gang_war_receive_logs values (?,?)");
         ps.setLong(1, rid);
         ps.setLong(2, Time.getDayLong());
         ps.executeUpdate();
         ps.close();
      } catch (Exception var7) {
         var7.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void initWelfareLogs() {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select * from mu_gang_welfare_logs where receive_time = ?");
         ps.setLong(1, Time.getDayLong());
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            long rid = rs.getLong("role_id");
            long day = rs.getLong("receive_time");
            GangManager.addWelfareReceiveLog(rid, day);
         }

         rs.close();
         ps.close();
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static boolean createGang(Player player, String name, int flag) {
      Connection conn = null;
      CallableStatement cs = null;

      try {
         conn = Pool.getConnection();
         cs = conn.prepareCall("{ call create_gang(?,?,?,?,?,?,?)}");
         long gangId = IDFactory.getGangID();
         long createTime = System.currentTimeMillis();
         cs.setLong(1, gangId);
         cs.setLong(2, player.getID());
         cs.setString(3, name);
         cs.setLong(4, createTime);
         cs.setInt(5, flag);
         cs.setString(6, player.getUser().getBlueVip().getTag());
         cs.registerOutParameter(7, 4);
         cs.execute();
         long result = (long)cs.getInt(7);
         if (result == 1L) {
            Gang gang = new Gang(gangId, name);
            GangManager.addGang(gang);
            GangMember member = new GangMember(player.getID(), gangId);
            gang.setCreateTime(createTime);
            gang.setFlagId(flag);
            member.setPost(2);
            member.setGangId(gangId);
            member.setLevel(player.getLevel());
            member.setName(player.getName());
            member.setProfession(player.getProfessionID());
            member.setUserName(player.getUserName());
            member.setBlueTag(player.getUser().getBlueVip().getTag());
            gang.addMember(member);
            gang.setMasterId(player.getID());
            GangManager.addGang(gang);
            GangManager.addMember(member);
            GangPlayerAttr.pushAttr(player);
            GangPlayerIn.pushPlayerIn(gang, player.getID());
            GangManager.removeApplyForJoinGang(player.getID());
            BroadcastManager.broadcastGangCreate(gang.getName(), gang.getId());
            PlayerManager.reduceMoney(player, GangManager.getCreateNeedMoney());
            player.getTaskManager().onEventCheckValue(TargetType.ValueType.JiaRuZhanMeng);
            ChangePlayerGangName.change(player);
            return true;
         }

         if (result == -1L) {
            SystemMessage.writeMessage(player, 9005);
         }

         return false;
      } catch (Exception var16) {
         var16.printStackTrace();
      } finally {
         Pool.closeStatment(cs);
         Pool.closeConnection(conn);
      }

      return false;
   }

   public static void updateGangDes(long gangId, String des, long editorId, long editTime) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("update mu_gang set gang_des = ?,des_editor = ? ,des_edit_time = ? where gang_id = ?");
         ps.setString(1, des);
         ps.setLong(2, editorId);
         ps.setLong(3, editTime);
         ps.setLong(4, gangId);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var12) {
         var12.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void deleteGang(long gangId) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("delete from mu_gang where gang_id = ?");
         ps.setLong(1, gangId);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var7) {
         var7.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   private static void loadGangs() {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery("select * from mu_gang");

         while(rs.next()) {
            long gangId = rs.getLong("gang_id");
            String gangName = rs.getString("gang_name");
            int gangLevel = rs.getInt("gang_level");
            String des = rs.getString("gang_des");
            long editor = rs.getLong("des_editor");
            long createTime = rs.getLong("create_time");
            long editTime = rs.getLong("des_edit_time");
            int flag = rs.getInt("gang_flag");
            boolean isWinner = rs.getInt("is_war_winner") == 1;
            long contribution = rs.getLong("contribution");
            long hisContribution = rs.getLong("his_contribution");
            if (isWinner) {
               GangManager.setWarVictorGang(gangId);
            }

            Gang gang = new Gang(gangId, gangName);
            gang.setLevel(gangLevel);
            gang.setDescription(des);
            gang.setDesEditor(editor);
            gang.setDesEditTime(editTime);
            gang.setCreateTime(createTime);
            gang.setFlagId(flag);
            gang.setContribution(contribution);
            gang.setHisContribution(hisContribution);
            GangManager.addGang(gang);
         }

         rs.close();
         st.close();
      } catch (Exception var24) {
         var24.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   private static void loadRedPacket() {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery("select a.*,b.role_name from role_red_packet a,mu_role b where a.is_end = 0 and a.role_id = b.role_id");

         while(true) {
            long pid;
            long rid;
            long gangId;
            int redId;
            int type;
            long time;
            String left;
            String name;
            String userName;
            int serverId;
            Gang gang;
            do {
               do {
                  if (!rs.next()) {
                     rs.close();
                     st.close();
                     return;
                  }

                  pid = rs.getLong("packet_id");
                  rid = rs.getLong("role_id");
                  gangId = rs.getLong("gang_id");
                  redId = rs.getInt("red_id");
                  type = rs.getInt("packet_type");
                  time = rs.getLong("packet_time");
                  left = rs.getString("left_ingot");
                  name = rs.getString("role_name");
                  userName = rs.getString("user_name");
                  serverId = rs.getInt("server_id");
                  gang = GangManager.getGang(gangId);
               } while(gang == null);
            } while(left.equals("0"));

            RedPacket rp = new RedPacket();
            rp.setGangId(gangId);
            rp.setPacketId(pid);
            rp.setRedId(redId);
            rp.setRedType(type);
            rp.setRoleId(rid);
            rp.setRoleName(name);
            rp.setSendTime(time);
            rp.setUserName(userName);
            rp.setServerId(serverId);
            RedPacketInfo info = GangManager.getRedPacketInfo(redId);
            rp.setTotalIngot(info.getIngot());
            String[] s = left.split(",");

            for(int i = 0; i < s.length; ++i) {
               rp.addLeftIngit(Integer.parseInt(s[i]));
            }

            gang.addRedPacket(rp);
         }
      } catch (Exception var25) {
         var25.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   private static void initSummonRecord() {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select * from gang_summon_record where save_day = ?");
         long today = Time.getDayLong();
         ps.setLong(1, today);
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            long gangId = rs.getLong("gang_id");
            int bossId = rs.getInt("boss_id");
            int summonTimes = rs.getInt("summon_times");
            Gang gang = GangManager.getGang(gangId);
            if (gang != null) {
               gang.addSummonTimes(bossId, summonTimes);
            }
         }

         rs.close();
         ps.close();
      } catch (Exception var13) {
         var13.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   private static void loadRedPacketReceive() {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery("select a.packet_id,a.role_id,a.receive_time,a.receive_day,a.receive_ingot,b.gang_id,c.role_name from role_red_packet_receive a,role_red_packet b,mu_role c where a.packet_id = b.packet_id and b.is_end = 0 and a.role_id = c.role_id");

         while(rs.next()) {
            long pid = rs.getLong("packet_id");
            long rid = rs.getLong("role_id");
            long gangId = rs.getLong("gang_id");
            long time = rs.getLong("receive_time");
            long day = rs.getLong("receive_day");
            int ingot = rs.getInt("receive_ingot");
            String name = rs.getString("role_name");
            Gang gang = GangManager.getGang(gangId);
            if (gang != null) {
               RedPacket rp = gang.getRedPacket(pid);
               if (rp != null) {
                  RedPacketReceiveRecord record = new RedPacketReceiveRecord();
                  record.setPacketId(pid);
                  record.setReceiveDay(day);
                  record.setReceiveIngot(ingot);
                  record.setReceiveTime(time);
                  record.setRid(rid);
                  record.setRoleName(name);
                  rp.addRecord(record);
               }
            }
         }

         rs.close();
         st.close();
      } catch (Exception var21) {
         var21.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   private static void loadGangMembers() {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery("select a.role_id,a.gang_id,a.gang_post,a.war_post,a.vip_tag,a.cur_contribution,a.his_contribution, b.role_level,b.role_name,b.profession,b.profession_level,b.user_name,b.server_id, b.logout_time from mu_gang_member a,mu_role b where a.role_id = b.role_id");

         while(rs.next()) {
            long roleId = rs.getLong("role_id");
            long gangId = rs.getLong("gang_id");
            int post = rs.getInt("gang_post");
            int roleLevel = rs.getInt("role_level");
            String roleName = rs.getString("role_name");
            int profession = rs.getInt("profession");
            int professionLevel = rs.getInt("profession_level");
            String userName = rs.getString("user_name");
            long offlineTime = rs.getLong("logout_time");
            int warPost = rs.getInt("war_post");
            String tag = rs.getString("vip_tag");
            int curContribution = rs.getInt("cur_contribution");
            int hisContribution = rs.getInt("his_contribution");
            int sid = rs.getInt("server_id");
            GangMember member = new GangMember(roleId, gangId);
            member.setName(roleName);
            member.setPost(post);
            member.setUserName(userName);
            member.setLevel(roleLevel);
            member.setProfession(Profession.getProID(profession, professionLevel));
            member.setOfflineTime(offlineTime);
            member.setWarPost(warPost == 1 ? 0 : warPost);
            member.setBlueTag(tag);
            member.setCurContribution(curContribution);
            member.setHisContribution(hisContribution);
            member.setSid(sid);
            Gang gang = GangManager.getGang(gangId);
            if (gang != null) {
               gang.addMember(member);
               if (post == 2) {
                  gang.setMasterId(roleId);
               }

               GangManager.addMember(member);
            }
         }

         rs.close();
         st.close();
      } catch (Exception var25) {
         var25.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   private static void loadGangNotcie() {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery("select * from mu_gang_notice");

         while(rs.next()) {
            long gangId = rs.getLong("gang_id");
            long time = rs.getLong("info_time");
            String des = rs.getString("info_des");
            GangNotice notice = new GangNotice();
            notice.setGangId(gangId);
            notice.setTime(time);
            notice.setDetail(des);
            Gang gang = GangManager.getGang(gangId);
            if (gang != null) {
               gang.addNoticeFromDB(notice);
            }
         }

         rs.close();
         st.close();
      } catch (Exception var13) {
         var13.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   private static void loadGangApply() {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery("select a.role_id,a.gang_id,a.apply_time,a.vip_tag, b.role_level,b.role_name,b.profession,b.user_name from mu_gang_apply a,mu_role b where a.role_id = b.role_id");

         while(rs.next()) {
            long roleId = rs.getLong("role_id");
            long gangId = rs.getLong("gang_id");
            long applyTime = rs.getLong("apply_time");
            int roleLevel = rs.getInt("role_level");
            String roleName = rs.getString("role_name");
            int profession = rs.getInt("profession");
            String userName = rs.getString("user_name");
            String tag = rs.getString("vip_tag");
            Gang gang = GangManager.getGang(gangId);
            if (gang != null) {
               ApplyInfo info = new ApplyInfo();
               info.setApplyTime(applyTime);
               info.setLevel(roleLevel);
               info.setName(roleName);
               info.setProfession(profession);
               info.setRoleId(roleId);
               info.setUserName(userName);
               info.setVipTag(tag);
               gang.addApplyInfo(info);
               GangManager.addApplyGang(roleId, gangId);
            }
         }

         rs.close();
         st.close();
      } catch (Exception var19) {
         var19.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void insertApply(long rid, long gangId, long time, String tag) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("replace into mu_gang_apply values (?,?,?,?)");
         ps.setLong(1, rid);
         ps.setLong(2, gangId);
         ps.setLong(3, time);
         ps.setString(4, tag);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var12) {
         var12.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void deletApply(long rid, long gangId) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("delete from mu_gang_apply where role_id = ? and gang_id = ?");
         ps.setLong(1, rid);
         ps.setLong(2, gangId);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void deletApply(ArrayList idList, long gangId) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("delete from mu_gang_apply where role_id = ? and gang_id = ?");
         Iterator var7 = idList.iterator();

         while(var7.hasNext()) {
            long id = ((Long)var7.next()).longValue();
            ps.setLong(1, id);
            ps.setLong(2, gangId);
            ps.addBatch();
         }

         ps.executeBatch();
         ps.close();
         idList.clear();
      } catch (Exception var11) {
         var11.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void clearPlayerApply(long rid) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("delete from mu_gang_apply where role_id = ?");
         ps.setLong(1, rid);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var7) {
         var7.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void deleteNotice(long gangId, long time) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("delete from mu_gang_notice where gang_id = ? and info_time <= ?");
         ps.setLong(1, gangId);
         ps.setLong(2, time);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void deleteMember(long rid) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("delete from mu_gang_member where role_id = ?");
         ps.setLong(1, rid);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var7) {
         var7.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void deleteMember(ArrayList membersId) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("delete from mu_gang_member where role_id = ?");
         Iterator var5 = membersId.iterator();

         while(var5.hasNext()) {
            long l = ((Long)var5.next()).longValue();
            ps.setLong(1, l);
            ps.addBatch();
            ps.executeBatch();
         }

         ps.close();
         membersId.clear();
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void insertNotice(GangNotice notice) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("insert into mu_gang_notice values (?,?,?)");
         ps.setLong(1, notice.getGangId());
         ps.setLong(2, notice.getTime());
         ps.setString(3, notice.getDetail());
         ps.executeUpdate();
         ps.close();
      } catch (Exception var6) {
         var6.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void inertMember(GangMember member) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("replace into mu_gang_member values (?,?,?,?,?,?,?,?)");
         ps.setLong(1, member.getId());
         ps.setLong(2, member.getGangId());
         ps.setInt(3, member.getPost());
         ps.setLong(4, System.currentTimeMillis());
         ps.setInt(5, 1);
         ps.setString(6, member.getBlueTag());
         ps.setInt(7, member.getCurContribution());
         ps.setInt(8, member.getHisContribution());
         ps.executeUpdate();
         ps.close();
      } catch (Exception var6) {
         var6.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void insertWelfareLog(long rid) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("replace into mu_gang_welfare_logs values (?,?)");
         ps.setLong(1, rid);
         ps.setLong(2, Time.getDayLong());
         ps.executeUpdate();
         ps.close();
      } catch (Exception var7) {
         var7.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void updateGangLevel(long gangId, int level) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("update mu_gang set gang_level = ? where gang_id = ?");
         ps.setInt(1, level);
         ps.setLong(2, gangId);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void updatePost(long rid, int post) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("update mu_gang_member set gang_post = ? where role_id = ? ");
         ps.setInt(1, post);
         ps.setLong(2, rid);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void insertGangWarQualification(long date, long[] gangs) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("replace into mu_gang_war_qualification values(?,?,?,?,?)");
         ps.setLong(1, date);
         ps.setLong(2, gangs[0]);
         ps.setLong(3, gangs[1]);
         ps.setLong(4, gangs[2]);
         ps.setLong(5, gangs[3]);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void initGangWarQualification() {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select * from mu_gang_war_qualification where war_date = ?");
         ps.setLong(1, DungeonManager.getLuolanManager().getOpenDay());
         ResultSet rs = ps.executeQuery();
         if (rs.next()) {
            long g1 = rs.getLong("gang1");
            long g2 = rs.getLong("gang2");
            long g3 = rs.getLong("gang3");
            GangWarRankInfo info1 = GangManager.createGangWarRankInfo(g1);
            GangWarRankInfo info2 = GangManager.createGangWarRankInfo(g2);
            GangWarRankInfo info3 = GangManager.createGangWarRankInfo(g3);
            if (info1 == null || info2 == null || info3 == null) {
               GangManager.confirmRank();
            }
         }

         rs.close();
         ps.close();
      } catch (Exception var15) {
         var15.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void updateVipTag(long rid, String tag) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("update mu_gang_member set vip_tag = ? where role_id = ?");
         ps.setString(1, tag);
         ps.setLong(2, rid);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void updateWarPost(long rid, int post) {
      if (post != 1) {
         Connection conn = null;

         try {
            conn = Pool.getConnection();
            PreparedStatement ps = conn.prepareStatement("update mu_gang_member set war_post = ? where role_id = ?");
            ps.setInt(1, post);
            ps.setLong(2, rid);
            ps.executeUpdate();
            ps.close();
         } catch (Exception var8) {
            var8.printStackTrace();
         } finally {
            Pool.closeConnection(conn);
         }

      }
   }

   public static void updateGangWinner(long gangId, boolean isVictory) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("update mu_gang set is_war_winner = ? where gang_id = ?");
         ps.setInt(1, isVictory ? 1 : 0);
         ps.setLong(2, gangId);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void clearWarPost(long gangId) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("update mu_gang_member set war_post = 0 where gang_id = ?");
         ps.setLong(1, gangId);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var7) {
         var7.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static synchronized void updateRedPacket(long packetId, String left, boolean isEnd) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("update role_red_packet set left_ingot = ? , is_end = ? where packet_id = ?");
         ps.setString(1, left);
         ps.setInt(2, isEnd ? 1 : 0);
         ps.setLong(3, packetId);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static synchronized void saveRedPacket(long packetId, long rid, long gangId, int redId, int type, long time, String leftIngot, int end, String userName, int serverId) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("insert into role_red_packet values (?,?,?,?,?,?,?,?,?,?)");
         ps.setLong(1, packetId);
         ps.setLong(2, rid);
         ps.setString(3, userName);
         ps.setInt(4, serverId);
         ps.setLong(5, gangId);
         ps.setInt(6, redId);
         ps.setInt(7, type);
         ps.setLong(8, time);
         ps.setString(9, leftIngot);
         ps.setInt(10, end);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var19) {
         var19.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static void saveRedPacketReceive(long packetId, long rid, int redId, int type, long time, long day, int ingot) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("insert into role_red_packet_receive values (?,?,?,?,?,?,?)");
         ps.setLong(1, packetId);
         ps.setLong(2, rid);
         ps.setInt(3, redId);
         ps.setInt(4, type);
         ps.setLong(5, time);
         ps.setLong(6, day);
         ps.setInt(7, ingot);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var16) {
         var16.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static ArrayList getRoleSendBindPacketTimes(String userName, int serverId) {
      Connection conn = null;
      ArrayList list = new ArrayList();

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select red_id, count(1) as num  from role_red_packet where user_name = ? and server_id = ? and packet_type = 0 group by red_id");
         ps.setString(1, userName);
         ps.setInt(2, serverId);
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            int id = rs.getInt("red_id");
            int num = rs.getInt("num");
            list.add(new int[]{id, num});
         }

         rs.close();
         ps.close();
      } catch (Exception var11) {
         var11.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return list;
   }

   public static void saveSummonRecord(long gangId, int bossId, int times, long saveDay) {
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("replace into gang_summon_record values (?,?,?,?)");
         ps.setLong(1, gangId);
         ps.setInt(2, bossId);
         ps.setLong(3, saveDay);
         ps.setInt(4, times);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var11) {
         var11.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }

   public static int getRoleTodayBindPacketReceiveTimes(long rid, long today) {
      Connection conn = null;
      int count = 0;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select count(1) from role_red_packet_receive where role_id = ? and receive_day = ? and red_type = 0");
         ps.setLong(1, rid);
         ps.setLong(2, today);
         ResultSet rs = ps.executeQuery();
         if (rs.next()) {
            count = rs.getInt(1);
         }

         rs.close();
         ps.close();
      } catch (Exception var11) {
         var11.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return count;
   }

   public static void initGangs() {
      if (!Global.isInterServiceServer()) {
         loadGangs();
         loadGangMembers();
         loadGangApply();
         loadGangNotcie();
         initWelfareLogs();
         initGangWarQualification();
         loadRedPacket();
         loadRedPacketReceive();
         initSummonRecord();
      }
   }
}
