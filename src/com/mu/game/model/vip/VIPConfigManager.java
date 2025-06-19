package com.mu.game.model.vip;

import com.mu.game.model.mall.MallConfigManager;
import com.mu.game.model.mall.MallItemData;
import com.mu.game.model.vip.effect.VIPEffect;
import com.mu.game.model.vip.effect.VIPEffectType;
import com.mu.game.model.vip.effect.VIPEffectValue;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VIPConfigManager {
   private static Logger logger = LoggerFactory.getLogger(VIPConfigManager.class);
   private static final HashMap vipMap = new LinkedHashMap();
   private static final HashMap levelMap = new LinkedHashMap();
   private static final HashMap effectMap = new LinkedHashMap();
   private static VIPLevel levelHead;
   private static VIPLevel levelTail;
   public static final int VIPIngotRate = 10;

   public static void initConfigs(InputStream in) throws BiffException, IOException {
      Workbook wb = null;

      try {
         wb = Workbook.getWorkbook(in);
         Sheet[] sheets = wb.getSheets();
         initVIPDataConfig(sheets[1]);
         initVIPLevelConfig(sheets[2]);
         initVIPEffectConfig(sheets[3]);
      } finally {
         if (wb != null) {
            wb.close();
         }

      }

   }

   private static void initVIPDataConfig(Sheet sheet) {
      for(int i = 2; i <= sheet.getRows(); ++i) {
         String A = sheet.getCell("A" + i).getContents().trim();
         String B = sheet.getCell("B" + i).getContents().trim();
         String C = sheet.getCell("C" + i).getContents().trim();
         String D = sheet.getCell("D" + i).getContents().trim();
         String E = sheet.getCell("E" + i).getContents().trim();
         String F = sheet.getCell("F" + i).getContents().trim();
         String G = sheet.getCell("G" + i).getContents().trim();
         String H = sheet.getCell("H" + i).getContents().trim();
         String I = sheet.getCell("I" + i).getContents().trim();
         String J = sheet.getCell("J" + i).getContents().trim();
         String K = sheet.getCell("K" + i).getContents().trim();
         String L = sheet.getCell("L" + i).getContents().trim();
         String M = sheet.getCell("M" + i).getContents().trim();
         String N = sheet.getCell("N" + i).getContents().trim();
         VIPData data = new VIPData(Integer.parseInt(A), B);
         data.setImage(Integer.parseInt(C));
         data.setSlStr(D);
         data.setCfStr(E);
         data.setQlStr(F);
         data.setMwStr(G);
         data.setXxStr(H);
         data.setExp(Integer.parseInt(I));
         data.setPrice(Integer.parseInt(J));
         data.setTimeStr(K);
         data.setTimeDay(Integer.parseInt(L));
         data.setBaseLv(Integer.parseInt(M));
         data.setItemId(Integer.parseInt(N));
         MallItemData md = MallConfigManager.getData(data.getItemId());
         if (md == null) {
            logger.error("VIPConfigManager init vip data error, not found vip item by id {}", data.getItemId());
         }

         vipMap.put(data.getId(), data);
      }

   }

   private static void initVIPLevelConfig(Sheet sheet) {
      List levelList = new ArrayList();

      int i;
      for(i = 2; i <= sheet.getRows(); ++i) {
         String A = sheet.getCell("A" + i).getContents().trim();
         String B = sheet.getCell("B" + i).getContents().trim();
         VIPLevel vl = new VIPLevel(Integer.parseInt(A), Integer.parseInt(B));
         levelMap.put(vl.getLevel(), vl);
         levelList.add(vl);
      }

      Collections.sort(levelList);

      for(i = 0; i < levelList.size(); ++i) {
         VIPLevel vl = (VIPLevel)levelList.get(i);
         if (i + 1 < levelList.size()) {
            vl.setNext((VIPLevel)levelList.get(i + 1));
         }
      }

      levelHead = (VIPLevel)levelList.get(0);
      levelTail = (VIPLevel)levelList.get(levelList.size() - 1);
      levelList.clear();
      levelList = null;
   }

   private static void initVIPEffectConfig(Sheet sheet) {
      for(int i = 3; i <= sheet.getRows(); ++i) {
         String A = sheet.getCell("A" + i).getContents().trim();
         String B = sheet.getCell("B" + i).getContents().trim();
         String C = sheet.getCell("C" + i).getContents().trim();
         String D = sheet.getCell("D" + i).getContents().trim();
         String E = sheet.getCell("E" + i).getContents().trim();
         String F = sheet.getCell("F" + i).getContents().trim();
         String G = sheet.getCell("G" + i).getContents().trim();
         String H = sheet.getCell("H" + i).getContents().trim();
         String I = sheet.getCell("I" + i).getContents().trim();
         String J = sheet.getCell("J" + i).getContents().trim();
         String K = sheet.getCell("K" + i).getContents().trim();
         String L = sheet.getCell("L" + i).getContents().trim();
         String M = sheet.getCell("M" + i).getContents().trim();
         String N = sheet.getCell("N" + i).getContents().trim();
         String O = sheet.getCell("O" + i).getContents().trim();
         String P = sheet.getCell("P" + i).getContents().trim();
         String Q = sheet.getCell("Q" + i).getContents().trim();
         String R = sheet.getCell("R" + i).getContents().trim();
         String S = sheet.getCell("S" + i).getContents().trim();
         String T = sheet.getCell("T" + i).getContents().trim();
         String U = sheet.getCell("U" + i).getContents().trim();
         String V = sheet.getCell("V" + i).getContents().trim();
         String W = sheet.getCell("W" + i).getContents().trim();
         String X = sheet.getCell("X" + i).getContents().trim();
         String Y = sheet.getCell("Y" + i).getContents().trim();
         String Z = sheet.getCell("Z" + i).getContents().trim();
         String AA = sheet.getCell("AA" + i).getContents().trim();
         String AB = sheet.getCell("AB" + i).getContents().trim();
         String AC = sheet.getCell("AC" + i).getContents().trim();
         String AD = sheet.getCell("AD" + i).getContents().trim();
         String AE = sheet.getCell("AE" + i).getContents().trim();
         String AF = sheet.getCell("AF" + i).getContents().trim();
         String AG = sheet.getCell("AG" + i).getContents().trim();
         String AH = sheet.getCell("AH" + i).getContents().trim();
         String AI = sheet.getCell("AI" + i).getContents().trim();
         String AJ = sheet.getCell("AJ" + i).getContents().trim();
         String AK = sheet.getCell("AK" + i).getContents().trim();
         String AL = sheet.getCell("AL" + i).getContents().trim();
         String AM = sheet.getCell("AM" + i).getContents().trim();
         String AN = sheet.getCell("AN" + i).getContents().trim();
         String AO = sheet.getCell("AO" + i).getContents().trim();
         String AP = sheet.getCell("AP" + i).getContents().trim();
         VIPEffectType vet = VIPEffectType.valueOf(Integer.parseInt(B));
         if (vet == null) {
            logger.error("VIPEffect config error, not found VIPEffectType by id {}", Integer.parseInt(B));
         } else {
            VIPEffect ve = new VIPEffect(A, vet);
            ve.addLevelValue(getLevel(1)).parseConfig(C, D, E, F);
            ve.addLevelValue(getLevel(2)).parseConfig(G, H, I, J);
            ve.addLevelValue(getLevel(3)).parseConfig(K, L, M, N);
            ve.addLevelValue(getLevel(4)).parseConfig(O, P, Q, R);
            ve.addLevelValue(getLevel(5)).parseConfig(S, T, U, V);
            ve.addLevelValue(getLevel(6)).parseConfig(W, X, Y, Z);
            ve.addLevelValue(getLevel(7)).parseConfig(AA, AB, AC, AD);
            ve.addLevelValue(getLevel(8)).parseConfig(AE, AF, AG, AH);
            ve.addLevelValue(getLevel(9)).parseConfig(AI, AJ, AK, AL);
            ve.addLevelValue(getLevel(10)).parseConfig(AM, AN, AO, AP);
            effectMap.put(ve.getType(), ve);
         }
      }

   }

   public static VIPEffectValue getEffectValue(VIPEffectType vet, VIPLevel vl) {
      return ((VIPEffect)effectMap.get(vet)).getValue(vl);
   }

   public static VIPLevel getLevelHead() {
      return levelHead;
   }

   public static VIPLevel getLevel(int level) {
      level = Math.min(Math.max(level, getLevelHead().getLevel()), getLevelTail().getLevel());
      VIPLevel vl = (VIPLevel)levelMap.get(level);
      return vl;
   }

   public static VIPData getVIP(int vipId) {
      return (VIPData)vipMap.get(vipId);
   }

   public static VIPLevel getLevelTail() {
      return levelTail;
   }

   public static int getVIPSize() {
      return vipMap.size();
   }

   public static Iterator getVIPIterator() {
      return vipMap.values().iterator();
   }

   public static int getEffectSize() {
      return effectMap.size();
   }

   public static Iterator getEffectIterator() {
      return effectMap.values().iterator();
   }

   public static int getLevelSize() {
      return levelMap.size();
   }
}
