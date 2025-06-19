package com.mu.db.manager;

import com.mu.db.Pool;
import com.mu.game.model.unit.player.Profession;
import com.mu.game.top.BigDevilTopInfo;
import com.mu.game.top.LevelTopInfo;
import com.mu.game.top.PetTopInfo;
import com.mu.game.top.WarCommentTopInfo;
import com.mu.game.top.ZdlTopInfo;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.CopyOnWriteArrayList;

public class TopDBManager {
   private static final String getLevelTop = "select role_id,role_name,role_level,profession,profession_level,levelup_time,b.vip_tag from mu_role a,mu_user b where delete_status = 0 and a.user_name = b.user_name and a.server_id = b.server_id order by role_level desc,levelup_time asc limit 100";
   private static final String getWarCommentTop = "select role_id,role_name,role_level,profession,profession_level,war_comment,war_comment_time,b.vip_tag from mu_role a,mu_user b where delete_status = 0 and war_comment > 0 and a.user_name=b.user_name and a.server_id = b.server_id order by war_comment desc limit 100";
   private static final String getPetTop = "select role_id,role_name,role_level,profession,profession_level, pet.*, mu_user.vip_tag from mu_role right join (select roleId, rank, rankTime from role_pet order by rank desc, rankTime asc limit 0, 100) pet on pet.roleId=mu_role.role_id left join mu_user on mu_role.user_name=mu_user.user_name and mu_role.server_id=mu_user.server_id  where delete_status = 0";
   private static final String getZdlTop = "select role_id,role_name,role_level,profession,profession_level,zdl,b.vip_tag from mu_role a,mu_user b where delete_status = 0 and zdl > 0 and a.user_name = b.user_name and a.server_id = b.server_id order by zdl desc limit 100";
   private static final String getBigDevilTop = "select a.role_id,role_name,role_level,profession,profession_level, a.dun_id,a.dun_level,a.max_exp,a.save_time from mu_role right join (select role_id, dun_id, dun_level, max_exp, save_time from mu_bigdevil_top where dun_id = 6 order by max_exp desc, save_time limit 0, 20) a on a.role_id=mu_role.role_id where delete_status = 0";
   private static final String updateBigDevilTop = "INSERT INTO mu_bigdevil_top  VALUES (?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE max_exp =if(max_exp<values(max_exp), values(max_exp), max_exp),save_time =if(max_exp<values(max_exp), values(save_time), save_time)";

   public static CopyOnWriteArrayList getLevelTopList() {
      Connection conn = null;
      CopyOnWriteArrayList list = new CopyOnWriteArrayList();

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select role_id,role_name,role_level,profession,profession_level,levelup_time,b.vip_tag from mu_role a,mu_user b where delete_status = 0 and a.user_name = b.user_name and a.server_id = b.server_id order by role_level desc,levelup_time asc limit 100");
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            long rid = rs.getLong("role_id");
            String name = rs.getString("role_name");
            int level = rs.getInt("role_level");
            int profession = rs.getInt("profession");
            int pl = rs.getInt("profession_level");
            long time = rs.getLong("levelup_time");
            String tag = rs.getString("vip_tag");
            int pid = Profession.getProID(profession, pl);
            LevelTopInfo li = new LevelTopInfo();
            li.setLevel(level);
            li.setLevelUpTime(time);
            li.setName(name);
            li.setProfessionId(pid);
            li.setRid(rid);
            li.setVipTag(tag);
            list.add(li);
         }

         rs.close();
         ps.close();
      } catch (Exception var18) {
         var18.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return list;
   }

   public static CopyOnWriteArrayList getZdlTopList() {
      Connection conn = null;
      CopyOnWriteArrayList list = new CopyOnWriteArrayList();

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select role_id,role_name,role_level,profession,profession_level,zdl,b.vip_tag from mu_role a,mu_user b where delete_status = 0 and zdl > 0 and a.user_name = b.user_name and a.server_id = b.server_id order by zdl desc limit 100");
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            long rid = rs.getLong("role_id");
            String name = rs.getString("role_name");
            int level = rs.getInt("role_level");
            int profession = rs.getInt("profession");
            int pl = rs.getInt("profession_level");
            int zdl = rs.getInt("zdl");
            String tag = rs.getString("vip_tag");
            int pid = Profession.getProID(profession, pl);
            ZdlTopInfo li = new ZdlTopInfo();
            li.setLevel(level);
            li.setZdl(zdl);
            li.setName(name);
            li.setProfessionId(pid);
            li.setRid(rid);
            li.setVipTag(tag);
            list.add(li);
         }

         rs.close();
         ps.close();
      } catch (Exception var17) {
         var17.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return list;
   }

   public static CopyOnWriteArrayList getWarCommentTop() {
      CopyOnWriteArrayList list = new CopyOnWriteArrayList();
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select role_id,role_name,role_level,profession,profession_level,war_comment,war_comment_time,b.vip_tag from mu_role a,mu_user b where delete_status = 0 and war_comment > 0 and a.user_name=b.user_name and a.server_id = b.server_id order by war_comment desc limit 100");
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            long rid = rs.getLong("role_id");
            String name = rs.getString("role_name");
            int level = rs.getInt("role_level");
            int profession = rs.getInt("profession");
            int pl = rs.getInt("profession_level");
            int wc = rs.getInt("war_comment");
            long wcTime = rs.getLong("war_comment_time");
            String tag = rs.getString("vip_tag");
            int pid = Profession.getProID(profession, pl);
            WarCommentTopInfo ti = new WarCommentTopInfo();
            ti.setLevel(level);
            ti.setName(name);
            ti.setProfessionId(pid);
            ti.setRid(rid);
            ti.setWarComment(wc);
            ti.setWarCommentTime(wcTime);
            ti.setVipTag(tag);
            list.add(ti);
         }

         rs.close();
         ps.close();
      } catch (Exception var19) {
         var19.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return list;
   }

   public static CopyOnWriteArrayList getPetTopList() {
      Connection conn = null;
      CopyOnWriteArrayList list = new CopyOnWriteArrayList();

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select role_id,role_name,role_level,profession,profession_level, pet.*, mu_user.vip_tag from mu_role right join (select roleId, rank, rankTime from role_pet order by rank desc, rankTime asc limit 0, 100) pet on pet.roleId=mu_role.role_id left join mu_user on mu_role.user_name=mu_user.user_name and mu_role.server_id=mu_user.server_id  where delete_status = 0");
         ResultSet rs = ps.executeQuery();

         while(rs.next()) {
            long rid = rs.getLong("role_id");
            String name = rs.getString("role_name");
            int level = rs.getInt("role_level");
            int profession = rs.getInt("profession");
            int pl = rs.getInt("profession_level");
            int rank = rs.getInt("rank");
            long rankTime = rs.getLong("rankTime");
            String tag = rs.getString("vip_tag");
            int pid = Profession.getProID(profession, pl);
            PetTopInfo pi = new PetTopInfo();
            pi.setLevel(level);
            pi.setName(name);
            pi.setProfessionId(pid);
            pi.setRid(rid);
            pi.setPetRank(rank);
            pi.setRankTime(rankTime);
            pi.setVipTag(tag);
            list.add(pi);
         }

         rs.close();
         ps.close();
      } catch (Exception var19) {
         var19.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return list;
   }

   public static CopyOnWriteArrayList getBigDevilTop() {
      Connection conn = null;
      CopyOnWriteArrayList list = new CopyOnWriteArrayList();

      try {
         conn = Pool.getConnection();
         Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery("select a.role_id,role_name,role_level,profession,profession_level, a.dun_id,a.dun_level,a.max_exp,a.save_time from mu_role right join (select role_id, dun_id, dun_level, max_exp, save_time from mu_bigdevil_top where dun_id = 6 order by max_exp desc, save_time limit 0, 20) a on a.role_id=mu_role.role_id where delete_status = 0");

         while(rs.next()) {
            long rid = rs.getLong("role_id");
            String name = rs.getString("role_name");
            int level = rs.getInt("role_level");
            int profession = rs.getInt("profession");
            int pl = rs.getInt("profession_level");
            long exp = rs.getLong("max_exp");
            long time = rs.getLong("save_time");
            int dunLevel = rs.getInt("dun_level");
            int pid = Profession.getProID(profession, pl);
            BigDevilTopInfo info = new BigDevilTopInfo(exp, time);
            info.setLevel(level);
            info.setName(name);
            info.setProfessionId(pid);
            info.setRid(rid);
            info.setDunId(6);
            info.setDunLevel(dunLevel);
            list.add(info);
         }

         rs.close();
         st.close();
      } catch (Exception var20) {
         var20.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      return list;
   }

   public static void updateBigDevilTop(Game2GatewayPacket packet) {
      Connection conn = null;

      try {
         long rid = packet.readLong();
         String name = packet.readUTF();
         int dunId = packet.readByte();
         int dunLevel = packet.readByte();
         long exp = packet.readLong();
         long time = packet.readLong();
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("INSERT INTO mu_bigdevil_top  VALUES (?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE max_exp =if(max_exp<values(max_exp), values(max_exp), max_exp),save_time =if(max_exp<values(max_exp), values(save_time), save_time)");
         ps.setLong(1, rid);
         ps.setString(2, name);
         ps.setInt(3, dunId);
         ps.setInt(4, dunLevel);
         ps.setLong(5, exp);
         ps.setLong(6, time);
         ps.setLong(7, 0L);
         ps.executeUpdate();
         ps.close();
      } catch (Exception var15) {
         var15.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

   }
}
