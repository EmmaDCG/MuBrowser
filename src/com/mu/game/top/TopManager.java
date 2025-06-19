package com.mu.game.top;

import com.mu.config.MessageText;
import com.mu.db.manager.TopDBManager;
import com.mu.game.CenterManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import jxl.Sheet;
import jxl.Workbook;

public class TopManager {
   public static final int PageSize = 10;
   public static final int Top_Level = 0;
   public static final int Top_Pet = 1;
   public static final int Top_WarComment = 2;
   public static final int Top_Zdl = 3;
   private static CopyOnWriteArrayList levelList = new CopyOnWriteArrayList();
   private static CopyOnWriteArrayList petList = new CopyOnWriteArrayList();
   private static CopyOnWriteArrayList warCommentList = new CopyOnWriteArrayList();
   private static CopyOnWriteArrayList zdlList = new CopyOnWriteArrayList();
   private static HashMap viewMap = new HashMap();
   private static HashMap topRewardMap = new HashMap();

   public static void init() throws Exception {
      levelList = TopDBManager.getLevelTopList();
      petList = TopDBManager.getPetTopList();
      warCommentList = TopDBManager.getWarCommentTop();
      zdlList = TopDBManager.getZdlTopList();
      DungeonTopManager.restBigdevilList();
   }

   public static void initTopReward(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet sheet = wb.getSheet(1);
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int topId = Tools.getCellIntValue(sheet.getCell("A" + i));
         int beginLevel = Tools.getCellIntValue(sheet.getCell("B" + i));
         int endLevel = Tools.getCellIntValue(sheet.getCell("C" + i));
         String attrStr = Tools.getCellValue(sheet.getCell("D" + i));
         String attrDes = Tools.getCellValue(sheet.getCell("E" + i));
         String nextDes = Tools.getCellValue(sheet.getCell("F" + i));
         String nextAttrDes = Tools.getCellValue(sheet.getCell("G" + i));
         int titleId = Tools.getCellIntValue(sheet.getCell("H" + i));
         ArrayList attrList = new ArrayList();
         int j;
         if (attrStr != null && !attrStr.trim().equals("")) {
            String[] tmp = attrStr.split(";");

            for(j = 0; j < tmp.length; ++j) {
               String[] tmp2 = tmp[j].split(",");
               int aid = Integer.parseInt(tmp2[0]);
               attrList.add(new int[]{aid, Integer.parseInt(tmp2[1]), Integer.parseInt(tmp2[2])});
            }
         }

         HashMap map = (HashMap)topRewardMap.get(topId);
         if (map == null) {
            map = new HashMap();
            topRewardMap.put(topId, map);
         }

         for(j = beginLevel; j <= endLevel; ++j) {
            TopRewardInfo ti = new TopRewardInfo();
            ti.setAttrDes(attrDes == null ? "" : attrDes);
            ti.setAttrList(attrList);
            ti.setNextAttrDes(nextAttrDes == null ? "" : nextAttrDes);
            ti.setNextDes(nextDes == null ? "" : nextDes);
            ti.setTitleId(titleId);
            ti.setTop(j);
            ti.setTopId(topId);
            map.put(j, ti);
         }
      }

   }

   public static TopRewardInfo getTopRewardInfo(int topId, int top) {
      if (top <= 100 && top >= 1) {
         TopRewardInfo ti = (TopRewardInfo)((HashMap)topRewardMap.get(topId)).get(top);
         return ti == null ? (TopRewardInfo)((HashMap)topRewardMap.get(topId)).get(Integer.valueOf(0)) : ti;
      } else {
         return (TopRewardInfo)((HashMap)topRewardMap.get(topId)).get(Integer.valueOf(0));
      }
   }

   public static void resetTop() {
      CopyOnWriteArrayList oldLevelList = levelList;
      CopyOnWriteArrayList newLevelList = TopDBManager.getLevelTopList();
      levelList = newLevelList;
      clearAllReward(0, oldLevelList);
      addAllReward(0, levelList);
      oldLevelList.clear();
      oldLevelList = null;
      CopyOnWriteArrayList oldPetlList = petList;
      CopyOnWriteArrayList newPetlList = TopDBManager.getPetTopList();
      petList = newPetlList;
      clearAllReward(1, oldPetlList);
      addAllReward(1, petList);
      oldPetlList.clear();
      oldPetlList = null;
      CopyOnWriteArrayList oldWarlList = warCommentList;
      CopyOnWriteArrayList newWarlList = TopDBManager.getWarCommentTop();
      warCommentList = newWarlList;
      clearAllReward(2, oldWarlList);
      addAllReward(2, warCommentList);
      oldWarlList.clear();
      oldWarlList = null;
      CopyOnWriteArrayList oldZdlList = zdlList;
      CopyOnWriteArrayList newZdlList = TopDBManager.getZdlTopList();
      zdlList = newZdlList;
      clearAllReward(3, oldZdlList);
      addAllReward(3, zdlList);
      oldZdlList.clear();
      oldZdlList = null;
   }

   private static void clearAllReward(int type, CopyOnWriteArrayList list) {
      for(int i = 0; i < list.size(); ++i) {
         TopInfo info = (TopInfo)list.get(i);
         Player player = CenterManager.getPlayerByRoleID(info.getRid());
         if (player != null) {
            try {
               player.removeTopProperties(type);
               TopRewardInfo ti = getTopRewardInfo(type, i + 1);
               if (ti != null && ti.getTitleId() > 0) {
                  player.getTitleManager().deletTitle(ti.getTitleId());
               }
            } catch (Exception var6) {
               var6.printStackTrace();
            }
         }
      }

   }

   private static void addAllReward(int type, CopyOnWriteArrayList list) {
      for(int i = 0; i < list.size(); ++i) {
         TopInfo info = (TopInfo)list.get(i);
         Player player = CenterManager.getPlayerByRoleID(info.getRid());
         TopRewardInfo ti = getTopRewardInfo(type, i + 1);
         if (player != null && ti != null) {
            try {
               player.addTopProperties(ti);
               if (ti.getTitleId() > 0) {
                  player.getTitleManager().gainATitle(ti.getTitleId());
               }
            } catch (Exception var7) {
               var7.printStackTrace();
            }
         }
      }

   }

   public static void checkTopWhenOnline(Player player) {
      int levelTop = getSelfTop(player.getID(), levelList);
      int petTop = getSelfTop(player.getID(), petList);
      int warcommentTop = getSelfTop(player.getID(), warCommentList);
      int zdlTop = getSelfTop(player.getID(), zdlList);
      TopRewardInfo ti;
      if (levelTop != -1) {
         ti = getTopRewardInfo(0, levelTop);
         if (ti != null) {
            player.addTopProperties(ti);
            if (ti.getTitleId() > 0) {
               player.getTitleManager().gainATitle(ti.getTitleId());
            }
         }
      }

      if (petTop != -1) {
         ti = getTopRewardInfo(1, petTop);
         if (ti != null) {
            player.addTopProperties(ti);
            if (ti.getTitleId() > 0) {
               player.getTitleManager().gainATitle(ti.getTitleId());
            }
         }
      }

      if (warcommentTop != -1) {
         ti = getTopRewardInfo(2, warcommentTop);
         if (ti != null) {
            player.addTopProperties(ti);
            if (ti.getTitleId() > 0) {
               player.getTitleManager().gainATitle(ti.getTitleId());
            }
         }
      }

      if (zdlTop != -1) {
         ti = getTopRewardInfo(3, zdlTop);
         if (ti != null) {
            player.addTopProperties(ti);
            if (ti.getTitleId() > 0) {
               player.getTitleManager().gainATitle(ti.getTitleId());
            }
         }
      }

   }

   public static void writeTopList(int type, int page, Player player, WriteOnlyPacket packet) throws Exception {
      CopyOnWriteArrayList list = null;
      switch(type) {
      case 1:
         list = petList;
         break;
      case 2:
         list = warCommentList;
         break;
      case 3:
         list = zdlList;
         break;
      default:
         list = levelList;
      }

      int size = list.size();
      int residue = size % 10;
      int maxPage = size / 10;
      if (residue != 0 || size == 0) {
         ++maxPage;
      }

      int tmpPage = page;
      if (page < 1) {
         tmpPage = 1;
      } else if (page > maxPage) {
         tmpPage = maxPage;
      }

      int begin = 10 * (tmpPage - 1);
      int end = begin + 10;
      if (end > size) {
         end = size;
      }

      packet.writeByte(type);
      packet.writeByte(tmpPage);
      packet.writeByte(maxPage);
      packet.writeByte(end - begin);

      int selfTop;
      for(selfTop = begin; selfTop < end; ++selfTop) {
         TopInfo info = (TopInfo)list.get(selfTop);
         packet.writeDouble((double)info.getRid());
         packet.writeByte(selfTop + 1);
         packet.writeUTF(info.getName());
         packet.writeShort(info.getLevel());
         packet.writeByte(info.getProfessionId());
         packet.writeUTF(info.getvariable());
         int[] icons = info.getVipIcons();
         packet.writeShort(icons[0]);
         packet.writeShort(icons[1]);
      }

      selfTop = getSelfTop(player.getID(), list);
      packet.writeByte(selfTop);
      packet.writeByte(3);

      for(int i = 1; i <= 3; ++i) {
         TopRewardInfo ti = getTopRewardInfo(type, i);
         packet.writeByte(ti.getTitleId());
         TopInfo tInfo = null;
         if (list != null && list.size() >= i) {
            tInfo = (TopInfo)list.get(i - 1);
         }

         packet.writeUTF(tInfo == null ? MessageText.getText(1049) : tInfo.getName());
      }

      TopRewardInfo selfTi = getTopRewardInfo(type, selfTop);
      packet.writeUTF(selfTi.getAttrDes());
      packet.writeUTF(selfTi.getNextDes());
      packet.writeUTF(selfTi.getNextAttrDes());
      packet.writeUTF(MessageText.getText(1050));
      player.writePacket(packet);
   }

   public static byte[] getView(long rid) {
      return (byte[])viewMap.get(rid);
   }

   public static boolean hasView(long rid) {
      return viewMap.containsKey(rid);
   }

   public static void addView(long rid, byte[] bytes) {
      viewMap.put(rid, bytes);
   }

   public static int getSelfTop(long rid, CopyOnWriteArrayList list) {
      for(int i = 0; i < list.size(); ++i) {
         TopInfo info = (TopInfo)list.get(i);
         if (info.getRid() == rid) {
            return i + 1;
         }
      }

      return -1;
   }
}
