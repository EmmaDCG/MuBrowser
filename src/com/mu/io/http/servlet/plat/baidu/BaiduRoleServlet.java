package com.mu.io.http.servlet.plat.baidu;

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

public class BaiduRoleServlet extends HttpServlet {
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

   public static String createErrorMsg(String code, String msg) {
      HashMap map = new HashMap();
      map.put("errCode", code);

      try {
         map.put("errMsg", URLEncoder.encode(msg, "utf-8"));
      } catch (Exception var5) {
         var5.printStackTrace();
      }

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
               if (!MD5.md5s(tmp).equals(sign.toLowerCase())) {
                  var16 = createErrorMsg("ERROR_-100", "传入参数不符合规则");
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
                     int time = rs.getInt("total_online_time");
                     mapRole.put("roleName", URLEncoder.encode(roleName, "utf-8"));
                     mapRole.put("roleLevel", String.valueOf(roleLevel));
                     mapRole.put("onlineTime", String.valueOf(time / 60));
                  }

                  rs.close();
                  ps.close();
                  if (mapRole == null) {
                     var16 = createErrorMsg("ERROR_-1406", "角色不存在");
                     return var16;
                  } else {
                     HashMap map = new HashMap();
                     ArrayList list = new ArrayList();
                     list.add(mapRole);
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
               var16 = createErrorMsg("ERROR_-100", "传入参数不符合规则");
               return var16;
            }
         } catch (Exception var19) {
            var19.printStackTrace();
            var16 = createErrorMsg("ERROR_-1", "系统内部错误");
            return var16;
         }
      } finally {
         Pool.closeConnection(conn);
      }
   }

   protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      this.doGet(req, resp);
   }

   public static void main(String[] args) {
      String key = "8a9a538c4fb63449014fee1035624692";
      String user_name = "athen";
      String server_id = "1";
      String time = String.valueOf(System.currentTimeMillis() / 1000L);
      String apiKey = "apiKey";
      String s = key + "api_key" + apiKey + "server_id" + server_id + "timestamp" + time + "user_id" + user_name;
      System.out.println(s);
      String sign = MD5.md5s(s).toUpperCase();
      String ip = "42.51.9.56";
      System.out.println(ip + ":7517/role?" + "api_key=" + apiKey + "&user_id=" + user_name + "&server_id=" + server_id + "&timestamp=" + time + "&sign=" + sign);
   }
}
