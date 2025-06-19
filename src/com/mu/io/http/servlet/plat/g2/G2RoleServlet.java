package com.mu.io.http.servlet.plat.g2;

import com.mu.config.Global;
import com.mu.db.Pool;
import com.mu.game.model.unit.player.Profession;
import com.mu.utils.MD5;
import com.mu.utils.Time;
import flexjson.JSONSerializer;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class G2RoleServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;
   private static final String getRoleInfo = "select role_name,role_level,create_time,profession,profession_level from mu_role where user_name = ? and server_id = ?";

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      PrintWriter writer = null;

      try {
         request.setCharacterEncoding("UTF-8");
         response.setCharacterEncoding("utf-8");
         response.setContentType("text/html;charset=UTF-8");
         writer = response.getWriter();
         String sign = request.getParameter("sign");
         String pid = request.getParameter("pid");
         String sid = request.getParameter("sid");
         String uid = request.getParameter("uid");
         String time = request.getParameter("time");
         String authStr = pid + sid + uid + time + Global.getLoginKey();
         if (Math.abs(Long.parseLong(time) - System.currentTimeMillis() / 1000L) > 900L) {
            writer.write(this.getErrorMessage("请求过期"));
         } else {
            if (MD5.md5s(authStr).toLowerCase().equals(sign)) {
               writer.write(this.getRoleInfo(uid, Integer.parseInt(sid)));
               return;
            }

            writer.write(this.getErrorMessage("MD5验证失败"));
         }
      } catch (Exception var13) {
         if (writer != null) {
            writer.write(this.getErrorMessage("服务器异常"));
         }

         var13.printStackTrace();
         return;
      } finally {
         if (writer != null) {
            writer.close();
         }

      }

   }

   private String getErrorMessage(String msg) {
      return "{\"result\":\"" + msg + "\"}";
   }

   private String getRoleInfo(String username, int serverid) {
      Connection conn = null;
      String s = "";

      try {
         conn = Pool.getConnection();
         PreparedStatement ps = conn.prepareStatement("select role_name,role_level,create_time,profession,profession_level from mu_role where user_name = ? and server_id = ?");
         ps.setString(1, username);
         ps.setInt(2, serverid);
         ResultSet rs = ps.executeQuery();
         ArrayList list = null;

         String name;
         while(rs.next()) {
            name = rs.getString("role_name");
            int level = rs.getInt("role_level");
            String createTime = Time.getTimeStr(rs.getLong("create_time"), "yyyy-MM-dd HH:mm:ss");
            String sex = "m";
            int profession = rs.getInt("profession");
            int professionLevel = rs.getInt("profession_level");
            int proType = Profession.getProID(profession, professionLevel);
            String pName = Profession.getProfession(proType).getProName();
            if (list == null) {
               list = new ArrayList();
            }

            HashMap map = new HashMap();
            map.put("gender", sex);
            map.put("name", URLEncoder.encode(name, "utf-8"));
            map.put("grade", level);
            map.put("profession", URLEncoder.encode(pName, "utf-8"));
            map.put("createDate", createTime);
            map.put("playerforce", Integer.valueOf(0));
            list.add(map);
         }

         if (list != null) {
            HashMap fmap = new HashMap();
            fmap.put("result", "1");
            fmap.put("roleinfo", list);
            JSONSerializer json = new JSONSerializer();
            s = json.deepSerialize(fmap);
            list.clear();
            fmap.clear();
            list = null;
            name = null;
         } else {
            s = this.getErrorMessage("用户不存在或没有角色");
         }

         rs.close();
         ps.close();
      } catch (Exception var20) {
         var20.printStackTrace();
         s = this.getErrorMessage("服务器异常");
      } finally {
         Pool.closeConnection(conn);
      }

      return s;
   }

   protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      this.doGet(req, resp);
   }
}
