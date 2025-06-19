package com.mu.game.model.unit.robot;

import com.mu.config.Global;
import com.mu.game.model.map.Map;
import com.mu.utils.Tools;
import java.awt.Point;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import jxl.Sheet;
import jxl.Workbook;

public class RobotManager {
   private static HashMap infoMap = new HashMap();
   private static HashMap robotMap = new HashMap();

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet[] sheets = wb.getSheets();

      for(int i = 1; i < sheets.length; ++i) {
         Sheet sheet = sheets[i];
         int mapId = Integer.parseInt(sheet.getName());
         ArrayList list = new ArrayList();
         infoMap.put(mapId, list);
         int rows = sheet.getRows();

         for(int j = 2; j <= rows; ++j) {
            String name = Tools.getCellValue(sheet.getCell("A" + j));
            int pro = Tools.getCellIntValue(sheet.getCell("B" + j));
            String[] p = Tools.getCellValue(sheet.getCell("C" + j)).split(",");
            Point point = new Point(Integer.parseInt(p[0]), Integer.parseInt(p[1]));
            String[] equips = Tools.getCellValue(sheet.getCell("D" + j)).split(";");
            ArrayList listEquip = new ArrayList();

            for(int k = 0; k < equips.length; ++k) {
               String[] e = equips[k].split(",");
               listEquip.add(new int[]{Integer.parseInt(e[0]), Integer.parseInt(e[1]), Integer.parseInt(e[2])});
            }

            String[] skills = Tools.getCellValue(sheet.getCell("E" + j)).split(",");
            ArrayList listSkill = new ArrayList();

            int level;
            for(level = 0; level < skills.length; ++level) {
               listSkill.add(Integer.parseInt(skills[level]));
            }

            level = Tools.getCellIntValue(sheet.getCell("F" + j));
            RobotInfo info = new RobotInfo();
            info.setEquips(listEquip);
            info.setName(name);
            info.setPoint(point);
            info.setSkills(listSkill);
            info.setPro(pro);
            info.setLevel(level);
            list.add(info);
         }
      }

   }

   public static void createRobot(Map map) {
      if (Global.isCreateRobot()) {
         ArrayList list = (ArrayList)infoMap.get(map.getID());
         if (list != null) {
            Iterator var3 = list.iterator();

            while(var3.hasNext()) {
               RobotInfo info = (RobotInfo)var3.next();
               Robot robot = new Robot(info, map);
               map.addPlayer(robot, robot.getBornPoint());
               map.addRobotSize();
               robotMap.put(robot.getID(), robot);
            }

         }
      }
   }

   public static Robot getRobot(long rid) {
      return (Robot)robotMap.get(rid);
   }
}
