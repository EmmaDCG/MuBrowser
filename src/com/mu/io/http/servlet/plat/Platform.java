package com.mu.io.http.servlet.plat;

import com.mu.config.Global;
import com.mu.game.model.unit.player.Player;
import com.mu.io.http.servlet.plat.baidu.BaiDuPayServlet;
import com.mu.io.http.servlet.plat.baidu.BaiduRoleServlet;
import com.mu.io.http.servlet.plat.g2.G2RoleBatchServlet;
import com.mu.io.http.servlet.plat.g2.G2RoleServlet;
import com.mu.io.http.servlet.plat.ku7.Ku7PayServlet;
import com.mu.io.http.servlet.plat.ku7.Ku7RoleServlet;
import com.mu.io.http.servlet.plat.qq.OpenBlueVipServlet;
import com.mu.io.http.servlet.plat.qq.QQPayServlet;
import com.mu.io.http.servlet.plat.small.SmallPayServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Platform {
   public static final int Plat_Baidu = 1;
   public static final int Plat_Tieba = 2;
   public static final int Plat_G2 = 3;
   public static final int Plat_Ku7 = 4;

   public static void createPlatServlet(ServletContextHandler context) {
      switch(Global.getParserID()) {
      case 1:
      default:
         break;
      case 2:
         initBaiduServlet(context);
         break;
      case 3:
         initG2Servlet(context);
         break;
      case 4:
         initKu7Servlet(context);
         break;
      case 5:
         initQQServlet(context);
         break;
      case 6:
         initSmallServlet(context);
      }

   }

   private static void initBaiduServlet(ServletContextHandler context) {
      context.addServlet(new ServletHolder(new BaiDuPayServlet()), "/pay");
      context.addServlet(new ServletHolder(new BaiduRoleServlet()), "/role");
   }

   private static void initG2Servlet(ServletContextHandler context) {
      context.addServlet(new ServletHolder(new G2RoleServlet()), "/role");
      context.addServlet(new ServletHolder(new G2RoleBatchServlet()), "/batch");
   }

   private static void initKu7Servlet(ServletContextHandler context) {
      context.addServlet(new ServletHolder(new Ku7RoleServlet()), "/role");
      context.addServlet(new ServletHolder(new Ku7PayServlet()), "/pay");
   }

   private static void initSmallServlet(ServletContextHandler context) {
      context.addServlet(new ServletHolder(new SmallPayServlet()), "/pay");
   }

   private static void initQQServlet(ServletContextHandler context) {
      context.addServlet(new ServletHolder(new QQPayServlet()), "/pay");
      context.addServlet(new ServletHolder(new OpenBlueVipServlet()), "/bluevip");
   }

   public static final void playerEnterGame(Player player) {
      switch(Global.getPlatID()) {
      case 1:
         baiduPlayerEnterGame(player);
      default:
      }
   }

   private static void baiduPlayerEnterGame(Player player) {
   }
}
