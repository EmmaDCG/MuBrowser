package com.mu.game.dungeon.imp.plot;

import com.mu.game.dungeon.DungeonTemplate;
import com.mu.game.model.item.Item;
import com.mu.game.model.map.BigMonsterGroup;
import com.mu.game.model.task.PlayerTaskManager;
import com.mu.game.model.task.Task;
import com.mu.game.model.unit.player.Player;
import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;
import jxl.Workbook;

public class PlotTemplate extends DungeonTemplate {
   private HashMap plotLevelMap = new HashMap();
   private int maxLevel = 0;

   public PlotTemplate(Workbook wb) {
      super(8, wb);
   }

   public void init() throws Exception {
      this.initPlotLevel(this.wb.getSheet(2));
      this.initMonsterStar(this.wb.getSheet(3));
      this.initMonsters();
   }

   private void initPlotLevel(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         PlotLevel pl = new PlotLevel(Tools.getCellIntValue(sheet.getCell("A" + i)));
         pl.setTaskId(Tools.getCellIntValue(sheet.getCell("B" + i)));
         pl.setMapId(Tools.getCellIntValue(sheet.getCell("C" + i)));
         pl.setX(Tools.getCellIntValue(sheet.getCell("D" + i)));
         pl.setY(Tools.getCellIntValue(sheet.getCell("E" + i)));
         this.plotLevelMap.put(pl.getPlotId(), pl);
      }

   }

   private void initMonsters() throws Exception {
      Sheet[] sheets = this.wb.getSheets();

      for(int i = 4; i < sheets.length; ++i) {
         Sheet sheet = sheets[i];
         int rows = sheet.getRows();

         for(int j = 2; j <= rows; ++j) {
            PlotMonsterGroup group = new PlotMonsterGroup();
            BigMonsterGroup.parseMonsterGroup(group, sheet, j);
            group.setPlotId(Tools.getCellIntValue(sheet.getCell("T" + j)));
            group.setFace(BigMonsterGroup.parseFace(Tools.getCellValue(sheet.getCell("V" + j))));
            group.setBossRank(Tools.getCellIntValue(sheet.getCell("U" + j)));
            PlotLevel pl = this.getPlotLevel(group.getPlotId());
            if (pl != null) {
               pl.addMonsterGroup(group);
            }

            this.checkMonsterStar(group);
         }
      }

   }

   public int getMaxLevel() {
      return this.maxLevel;
   }

   public int canEnter(Player player, Item ticket, Object... obj) {
      PlotLevel level = (PlotLevel)obj[0];
      int taskId = level.getTaskId();
      PlayerTaskManager tm = player.getTaskManager();
      Task task = tm.getCurZJTask();
      return task != null && task.getId() == taskId && !task.isComplete() ? super.canEnter(player, ticket, obj) : 14037;
   }

   public PlotLevel getPlotLevel(int level) {
      return (PlotLevel)this.plotLevelMap.get(level);
   }

   public boolean showDynamicMenu() {
      return false;
   }
}
