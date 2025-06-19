package com.mu.io.http.servlet.plat.ku7;

import com.mu.config.Global;
import com.mu.db.Pool;
import com.mu.utils.MD5;
import flexjson.JSONSerializer;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
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

public class Ku7RoleServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;
   private static String sqlGetRoles = "select role_name,role_level,total_online_time from mu_role where user_name = ? and server_id = ?";

   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      PrintWriter writer = null;

      try {
         req.setCharacterEncoding("UTF-8");
         resp.setCharacterEncoding("utf-8");
         resp.setContentType("text/html;charset=UTF-8");
         writer = resp.getWriter();
         writer.write(getRoleInfo(req));
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         if (writer != null) {
            writer.close();
         }

      }

   }

   public static String createErrorMsg(int code) {
      HashMap map = new HashMap();
      map.put("retcode", code);
      JSONSerializer json = new JSONSerializer();
      String s = json.serialize(map);
      map.clear();
      map = null;
      return s;
   }

   private static String getRoleInfo(HttpServletRequest request) {
      Connection conn = null;

      try {
         String var16;
         try {
            String api_key = request.getParameter("api_key");
            String user_id = request.getParameter("user_id");
            String server_id = request.getParameter("server_id");
            String timestamp = request.getParameter("timestamp");
            String sign = request.getParameter("sign");
            if (api_key != null && user_id != null && server_id != null && timestamp != null && sign != null) {
               user_id = URLDecoder.decode(user_id, "utf-8");
               String tmp = Global.getKey("cert") + "api_key" + api_key + "server_id" + server_id + "timestamp" + timestamp + "user_id" + user_id;
               if (!MD5.md5s(tmp).toUpperCase().equals(sign.toUpperCase())) {
                  var16 = createErrorMsg(3);
                  return var16;
               } else {
                  conn = Pool.getConnection();
                  PreparedStatement ps = conn.prepareStatement(sqlGetRoles);
                  ps.setString(1, user_id);
                  ps.setInt(2, Integer.parseInt(server_id));
                  ResultSet rs = ps.executeQuery();
                  HashMap mapRole = null;

                  while(rs.next()) {
                     if (mapRole == null) {
                        mapRole = new HashMap();
                     }

                     String roleName = rs.getString("role_name");
                     int roleLevel = rs.getInt("role_level");
                     mapRole.put("charname", URLEncoder.encode(roleName, "utf-8"));
                     mapRole.put("level", roleLevel);
                  }

                  rs.close();
                  ps.close();
                  if (mapRole == null) {
                     var16 = createErrorMsg(1);
                     return var16;
                  } else {
                     HashMap map = new HashMap();
                     ArrayList list = new ArrayList();
                     list.add(mapRole);
                     map.put("retcode", Integer.valueOf(0));
                     map.put("userInfo", list);
                     JSONSerializer json = new JSONSerializer();
                     String s = json.deepSerialize(map);
                     list.clear();
                     mapRole.clear();
                     map.clear();
                     var16 = s;
                     return var16;
                  }
               }
            } else {
               var16 = createErrorMsg(4);
               return var16;
            }
         } catch (Exception var19) {
            var19.printStackTrace();
            var16 = createErrorMsg(2);
            return var16;
         }
      } finally {
         Pool.closeConnection(conn);
      }
   }

   protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      this.doGet(req, resp);
   }
}
