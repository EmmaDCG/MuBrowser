package com.mu.io.http.servlet.plat.g2;

import com.mu.config.Global;
import com.mu.db.Pool;
import com.mu.utils.MD5;
import flexjson.JSONSerializer;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class G2RoleBatchServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      PrintWriter writer = null;

      try {
         request.setCharacterEncoding("UTF-8");
         response.setCharacterEncoding("utf-8");
         response.setContentType("text/html;charset=UTF-8");
         writer = response.getWriter();
         String sid = request.getParameter("server_id");
         String time = request.getParameter("time");
         String sign = request.getParameter("sign");
         if (!MD5.md5s("get_player_info_" + time + Global.getLoginKey()).equals(sign)) {
            writer.write("-3");
         } else {
            writer.write(this.getBatchRoleInfo(request.getParameter("users"), Integer.parseInt(sid)));
         }
      } catch (Exception var10) {
         if (writer != null) {
            writer.write("-5");
         }

         var10.printStackTrace();
      } finally {
         if (writer != null) {
            writer.close();
         }

      }

   }

   private String getBatchRoleInfo(String users, int sid) {
      String[] usr = users.split(",");
      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < usr.length; ++i) {
         if (i != 0) {
            sb.append(",");
         }

         sb.append("'");
         sb.append(usr[i]);
         sb.append("'");
      }

      String sql = "select role_name,role_level,war_comment,user_name from mu_role where server_id = " + sid + " and user_name in (" + sb.toString() + ")";
      HashMap map = new HashMap();
      Connection conn = null;

      try {
         conn = Pool.getConnection();
         Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery(sql);

         while(rs.next()) {
            String userName = rs.getString("user_name");
            String name = rs.getString("role_name");
            int level = rs.getInt("role_level");
            int zdl = rs.getInt("war_comment");
            ArrayList list = (ArrayList)map.get(userName);
            if (list == null) {
               list = new ArrayList();
               map.put(userName, list);
            }

            HashMap subMap = new HashMap();
            subMap.put("level", level);
            subMap.put("name", name);
            subMap.put("toptype", "战评");
            subMap.put("topvalue", zdl);
            list.add(subMap);
         }

         rs.close();
         st.close();
      } catch (Exception var19) {
         var19.printStackTrace();
      } finally {
         Pool.closeConnection(conn);
      }

      if (map.size() == 0) {
         return "-7";
      } else {
         JSONSerializer json = new JSONSerializer();
         String s = json.deepSerialize(map);
         map.clear();
         map = null;
         return s;
      }
   }

   protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      this.doGet(req, resp);
   }
}
