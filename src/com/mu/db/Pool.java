package com.mu.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class Pool {
   private static DruidDataSource ddsGame = null;
   private static DruidDataSource ddsLog = null;
   private static DruidDataSource ddsGlobalLog = null;
   private static String globalUrl = null;
   private static String globalUser = null;
   private static String globalPwd = null;
   private static boolean isInited = false;

   public static boolean init() {
      if (isInited) {
         return true;
      } else {
         try {
            SAXBuilder sb = new SAXBuilder();
            Document doc = sb.build(new File("configs/dbpool.xml"));
            Element root = doc.getRootElement();
            List poolList = root.getChildren("pool");
            Iterator var5 = poolList.iterator();

            while(var5.hasNext()) {
               Element poolElement = (Element)var5.next();
               String name = poolElement.getAttributeValue("name");
               if (name.equals("game")) {
                  ddsGame = (DruidDataSource)DruidDataSourceFactory.createDataSource(getPropertiesMap(poolElement));
                  ddsGame.setName("game");
               } else if (name.equals("log")) {
                  ddsLog = (DruidDataSource)DruidDataSourceFactory.createDataSource(getPropertiesMap(poolElement));
                  ddsLog.setName("log");
               } else if (name.equals("globalLog")) {
                  ddsGlobalLog = (DruidDataSource)DruidDataSourceFactory.createDataSource(getPropertiesMap(poolElement));
                  ddsGlobalLog.setName("globalLog");
               } else if (name.equals("global")) {
                  HashMap globalMap = getPropertiesMap(poolElement);
                  globalUrl = (String)globalMap.get("url");
                  globalUser = (String)globalMap.get("username");
                  globalPwd = (String)globalMap.get("password");
               }
            }

            isInited = true;
            return true;
         } catch (Exception var8) {
            var8.printStackTrace();
            return false;
         }
      }
   }

   private static HashMap getPropertiesMap(Element element) throws Exception {
      HashMap map = new HashMap();
      List list = element.getChildren("property");
      Iterator var4 = list.iterator();

      while(var4.hasNext()) {
         Element propertyElement = (Element)var4.next();
         map.put(propertyElement.getAttributeValue("name"), propertyElement.getAttributeValue("value"));
      }

      return map;
   }

   public static Connection getConnection() {
      try {
         return ddsGame.getConnection();
      } catch (SQLException var1) {
         var1.printStackTrace();
         return null;
      }
   }

   public static Connection getLogConnection() {
      try {
         return ddsLog.getConnection();
      } catch (SQLException var1) {
         var1.printStackTrace();
         return null;
      }
   }

   public static Connection getGlobalLogConnection() {
      try {
         return ddsGlobalLog.getConnection();
      } catch (SQLException var1) {
         var1.printStackTrace();
         return null;
      }
   }

   public static void closeResultSet(ResultSet rs) {
      if (rs != null) {
         try {
            rs.close();
         } catch (Exception var2) {
            var2.printStackTrace();
         }
      }

   }

   public static void closeStatment(Statement st) {
      if (st != null) {
         try {
            st.close();
         } catch (Exception var2) {
            var2.printStackTrace();
         }
      }

   }

   public static void closeConnection(Connection conn) {
      if (conn != null) {
         try {
            conn.close();
         } catch (Exception var2) {
            var2.printStackTrace();
         }
      }

   }

   public static boolean isInited() {
      return isInited;
   }

   public static Connection getGlobalConnection() {
      try {
         Class.forName("com.mysql.jdbc.Driver");
         Connection conn = DriverManager.getConnection(globalUrl, globalUser, globalPwd);
         return conn;
      } catch (Exception var1) {
         var1.printStackTrace();
         return null;
      }
   }
}
