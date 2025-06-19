package com.mu.game.model.unit.material;

import com.mu.game.model.unit.material.requirement.ItemRequirement;
import com.mu.game.model.unit.material.requirement.MaterialRequirement;
import com.mu.game.model.unit.material.requirement.TaskRequirement;
import com.mu.game.model.unit.material.reward.ItemReward;
import com.mu.game.model.unit.material.reward.MaterialReward;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import jxl.Sheet;
import jxl.Workbook;

public class MaterialTemplate {
   private static ConcurrentHashMap templates = new ConcurrentHashMap(16, 0.75F, 2);
   private int templateId;
   private int modelId;
   private int effectId;
   private String name;
   private boolean canDisappear = true;
   private int collectTime = 500;
   private ArrayList collectRequirements = new ArrayList();
   private ArrayList clickRequirements = new ArrayList();
   private ArrayList rewardList = new ArrayList();

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet[] sheets = wb.getSheets();

      for(int k = 1; k < sheets.length; ++k) {
         Sheet sheet = sheets[k];
         int rows = sheet.getRows();

         for(int i = 2; i <= rows; ++i) {
            int id = Tools.getCellIntValue(sheet.getCell("A" + i));
            String name = Tools.getCellValue(sheet.getCell("B" + i));
            int modelId = Tools.getCellIntValue(sheet.getCell("C" + i));
            int effectId = Tools.getCellIntValue(sheet.getCell("D" + i));
            String clickStr = Tools.getCellValue(sheet.getCell("E" + i));
            String collectStr = Tools.getCellValue(sheet.getCell("F" + i));
            String rewardStr = Tools.getCellValue(sheet.getCell("G" + i));
            boolean disappear = Tools.getCellIntValue(sheet.getCell("H" + i)) == 1;
            int collectTime = Tools.getCellIntValue(sheet.getCell("I" + i));
            MaterialTemplate template = new MaterialTemplate(id, modelId, name);
            template.setClickReqStr(clickStr);
            template.setCollectReqStr(collectStr);
            template.setRewardStr(rewardStr);
            template.setEffectId(effectId);
            template.setCanDisappear(disappear);
            template.setCollectTime(collectTime);
            templates.put(id, template);
         }
      }

   }

   public static MaterialTemplate getTemplate(int templateID) {
      return (MaterialTemplate)templates.get(templateID);
   }

   public static boolean hasTemplate(int id) {
      return templates.containsKey(id);
   }

   public MaterialTemplate(int templateId, int modelId, String name) {
      this.templateId = templateId;
      this.modelId = modelId;
      this.name = name;
   }

   public int getEffectId() {
      return this.effectId;
   }

   public void setEffectId(int effectId) {
      this.effectId = effectId;
   }

   public void setModeId(int id) {
      this.modelId = id;
   }

   public int getModelId() {
      return this.modelId;
   }

   public int getTemplateId() {
      return this.templateId;
   }

   public String getName() {
      return this.name;
   }

   public void addClickRequirement(MaterialRequirement req) {
      this.clickRequirements.add(req);
   }

   public void addCollectRequirement(MaterialRequirement req) {
      this.collectRequirements.add(req);
   }

   public void addReward(MaterialReward re) {
      this.rewardList.add(re);
   }

   public ArrayList getCollectRequirements() {
      return this.collectRequirements;
   }

   public ArrayList getClickRequirements() {
      return this.clickRequirements;
   }

   public ArrayList getRewardList() {
      return this.rewardList;
   }

   public boolean isCanDisappear() {
      return this.canDisappear;
   }

   public void setCanDisappear(boolean canDisappear) {
      this.canDisappear = canDisappear;
   }

   public int getCollectTime() {
      return this.collectTime;
   }

   public void setCollectTime(int collectTime) {
      this.collectTime = collectTime;
   }

   public void setClickReqStr(String clickReqStr) {
      if (clickReqStr != null && !clickReqStr.trim().equals("")) {
         String[] tmp = clickReqStr.split(";");

         for(int i = 0; i < tmp.length; ++i) {
            String[] req = tmp[i].split(",");
            if (req.length >= 2) {
               try {
                  int type = Integer.parseInt(req[0].trim());
                  int value = Integer.parseInt(req[1].trim());
                  switch(type) {
                  case 1:
                     ItemRequirement ir = new ItemRequirement(value, 1);
                     this.addClickRequirement(ir);
                     break;
                  case 2:
                     TaskRequirement tr = new TaskRequirement(value);
                     this.addClickRequirement(tr);
                  }
               } catch (Exception var9) {
                  var9.printStackTrace();
               }
            }
         }
      }

   }

   public void setCollectReqStr(String collectReqStr) {
      if (collectReqStr != null && !collectReqStr.trim().equals("")) {
         String[] tmp = collectReqStr.split(";");

         for(int i = 0; i < tmp.length; ++i) {
            String[] req = tmp[i].split(",");
            if (req.length >= 2) {
               try {
                  int type = Integer.parseInt(req[0].trim());
                  int value = Integer.parseInt(req[1].trim());
                  switch(type) {
                  case 1:
                     ItemRequirement ir = new ItemRequirement(value, 1);
                     this.addCollectRequirement(ir);
                     break;
                  case 2:
                     TaskRequirement tr = new TaskRequirement(value);
                     this.addCollectRequirement(tr);
                  }
               } catch (Exception var9) {
                  var9.printStackTrace();
               }
            }
         }
      }

   }

   public void initReqAndRew(Material m) {
      Iterator var3 = this.clickRequirements.iterator();

      MaterialRequirement req;
      while(var3.hasNext()) {
         req = (MaterialRequirement)var3.next();
         m.addClickRequirement(req);
      }

      var3 = this.collectRequirements.iterator();

      while(var3.hasNext()) {
         req = (MaterialRequirement)var3.next();
         m.addCollectRequirement(req);
      }

      var3 = this.rewardList.iterator();

      while(var3.hasNext()) {
         MaterialReward rw = (MaterialReward)var3.next();
         m.addReward(rw);
      }

   }

   public void setRewardStr(String rewardStr) {
      if (rewardStr != null && !rewardStr.trim().equals("")) {
         String[] tmp = rewardStr.split(";");

         for(int i = 0; i < tmp.length; ++i) {
            String[] rew = tmp[i].split(",");
            if (rew.length >= 2) {
               try {
                  int type = Integer.parseInt(rew[0].trim());
                  int value = Integer.parseInt(rew[1].trim());
                  switch(type) {
                  case 1:
                     ItemReward ir = new ItemReward(value, 1);
                     this.addReward(ir);
                  }
               } catch (Exception var8) {
                  var8.printStackTrace();
               }
            }
         }
      }

   }
}
