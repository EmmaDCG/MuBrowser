package com.mu.io.http.servlet;

import com.mu.config.Global;
import com.mu.game.CenterManager;
import com.mu.utils.Tools;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MuServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;
   private static final int Type_CheckCanSwitchServer = 1;

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      PrintWriter writer = null;

      try {
         request.setCharacterEncoding("UTF-8");
         response.setCharacterEncoding("utf-8");
         response.setContentType("text/html;charset=UTF-8");
         writer = response.getWriter();
         int type = Tools.getInterParameter(request, "type");
         switch(type) {
         case 1:
            writer.write(checkCanSwitch(request));
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         if (writer != null) {
            writer.close();
         }

      }

   }

   private static String checkCanSwitch(HttpServletRequest request) {
      try {
         int result = 1;
         if (!Global.isInterServiceServer()) {
            result = 6;
         } else if (!request.getParameter("version").equals(Global.getVersion())) {
            result = 4;
         } else if (CenterManager.getOnlinePlayerSize() > 2000) {
            result = 5;
         }

         return String.valueOf(result);
      } catch (Exception var2) {
         var2.printStackTrace();
         return "2";
      }
   }

   protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      this.doGet(req, resp);
   }
}
