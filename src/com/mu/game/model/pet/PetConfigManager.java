package com.mu.game.model.pet;

import com.mu.game.model.stats.StatEnum;
import com.mu.utils.CommonRegPattern;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PetConfigManager {
   private static Logger logger = LoggerFactory.getLogger(PetConfigManager.class);
   private static final HashMap rankMap = new LinkedHashMap();
   private static final HashMap attributeMap = new LinkedHashMap();
   private static final HashMap levelMap = new LinkedHashMap();
   private static PetRank rankHead;
   private static PetItemData openItem;
   private static String riseAttributeTip1;
   private static String riseAttributeTip2;
   private static long diedRevivalSecond;
   public static int MAX_LEVEL;
   public static int MAX_OWNER_DISTANCE;
   public static StatEnum[] ALL_PROPERTIES;

   public static void initConfigs(InputStream in) throws BiffException, IOException {
      Workbook wb = null;

      try {
         wb = Workbook.getWorkbook(in);
         Sheet[] sheets = wb.getSheets();
         initPetGlobalConfig(sheets[1]);
         initPetRankConfig(sheets[2]);
         initPetLevelConfig(sheets[3]);
         initPetPropertyDataConfig(sheets[4]);
         initPetAttributeConfig(sheets[5]);
         initPetRankAttributeLimitConfig(sheets[7]);
      } finally {
         if (wb != null) {
            wb.close();
         }

      }

   }

   private static void initPetGlobalConfig(Sheet sheet) {
      String A = sheet.getCell("A2").getContents().trim();
      String B = sheet.getCell("B2").getContents().trim();
      String C = sheet.getCell("C2").getContents().trim();
      String D = sheet.getCell("D2").getContents().trim();
      String E = sheet.getCell("E2").getContents().trim();
      String F = sheet.getCell("F2").getContents().trim();
      String G = sheet.getCell("G2").getContents().trim();
      Matcher m = CommonRegPattern.PATTERN_INT.matcher(A);
      m.find();
      int modelId = Integer.parseInt(m.group());
      m.find();
      int count = Integer.parseInt(m.group());
      openItem = new PetItemData(modelId, count);
      riseAttributeTip1 = D;
      riseAttributeTip2 = E;
      diedRevivalSecond = (long)Integer.parseInt(F);
      MAX_OWNER_DISTANCE = Integer.parseInt(G);
   }

   private static void initPetRankConfig(Sheet sheet) {
      PetRank last = null;

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
         String O = sheet.getCell("O" + i).getContents().trim();
         PetRank data = new PetRank();
         data.setRank(Integer.parseInt(A));
         data.setName(B);
         data.setColor(Integer.parseInt(C));
         data.setModel(Integer.parseInt(D));
         data.setScale(Integer.parseInt(E));
         data.setIcon(Integer.parseInt(F));
         data.setPropertiesStr(G);
         data.setRiseExpendStr(H);
         data.setLuckyStr(I);
         data.setRiseLevel(Integer.parseInt(J));
         data.setSkill(Integer.parseInt(K));
         data.setAttack(Integer.parseInt(L));
         data.setTemplateId(Integer.parseInt(M));
         data.setAttackDistance(Integer.parseInt(N));
         data.setPlayerZDL(Integer.parseInt(O));
         rankMap.put(data.getRank(), data);
         if (last == null) {
            rankHead = data;
         } else {
            last.setNext(data);
         }

         last = data;
      }

   }

   private static void initPetLevelConfig(Sheet sheet) {
      int max_level = 0;

      for(int i = 2; i <= sheet.getRows(); ++i) {
         String A = sheet.getCell("A" + i).getContents().trim();
         String B = sheet.getCell("B" + i).getContents().trim();
         int level = Integer.parseInt(A);
         if (level != i - 1) {
            logger.error("Pet level config error, level exp skip {}", i - 2);
         }

         levelMap.put(level, Long.parseLong(B));
         max_level = Math.max(max_level, level);
      }

      MAX_LEVEL = max_level;
   }

   private static void initPetPropertyDataConfig(Sheet sheet) {
      for(int i = 2; i <= sheet.getRows(); ++i) {
         String A = sheet.getCell("A" + i).getContents().trim();
         String B = sheet.getCell("B" + i).getContents().trim();
         String C = sheet.getCell("C" + i).getContents().trim();
         String D = sheet.getCell("D" + i).getContents().trim();
         String E = sheet.getCell("E" + i).getContents().trim();
         String F = sheet.getCell("F" + i).getContents().trim();
         PetRank rank = getRank(Integer.parseInt(A));
         PetPropertyData data = new PetPropertyData(rank, Integer.parseInt(B));
         data.setMonsterStar(Integer.parseInt(C));
         data.setMonsterLevel(Integer.parseInt(D));
         data.setPropertyListStr(E);
         data.setPetZDL(Integer.parseInt(F));
         rank.addPropertyData(data);
         if (ALL_PROPERTIES == null) {
            StatEnum[] properties = (StatEnum[])data.getPropertyList().keySet().toArray(new StatEnum[0]);
            int n = 1;
            ALL_PROPERTIES = new StatEnum[data.getPropertyList().size() + 1];
            ALL_PROPERTIES[0] = StatEnum.DOMINEERING;
            System.arraycopy(properties, 0, ALL_PROPERTIES, n, properties.length);
         }
      }

   }

   private static void initPetAttributeConfig(Sheet sheet) {
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
         int id = Integer.parseInt(J);
         PetAttributeData attribute = (PetAttributeData)attributeMap.get(id);
         if (attribute == null) {
            attribute = new PetAttributeData(id, StatEnum.find(Integer.parseInt(A)), B);
            attribute.setOpenRank(Integer.parseInt(C));
            attribute.setOpenDesc(D);
            attributeMap.put(id, attribute);
         }

         attribute.addLevelStr(Integer.parseInt(E), Integer.parseInt(F), PetItemData.parseValueFormStr(G), H, Integer.parseInt(I));
      }

   }

   private static void initPetRankAttributeLimitConfig(Sheet sheet) {
      for(int i = 2; i <= sheet.getRows(); ++i) {
         String A = sheet.getCell("A" + i).getContents().trim();
         String B = sheet.getCell("B" + i).getContents().trim();
         String C = sheet.getCell("C" + i).getContents().trim();
         PetRank rank = (PetRank)rankMap.get(Integer.parseInt(A));
         Integer id = Integer.parseInt(B);
         PetAttributeData attribute = (PetAttributeData)attributeMap.get(id);
         rank.addAttributeLimit(attribute, Integer.parseInt(C));
      }

   }

   public static long getExp(int level) {
      return levelMap.get(level) == null ? 2147483647L : ((Long)levelMap.get(level)).longValue();
   }

   public static PetRank getRank(int rank) {
      return (PetRank)rankMap.get(rank);
   }

   public static PetAttributeData getAttribute(int id) {
      return (PetAttributeData)attributeMap.get(id);
   }

   public static int getRankSize() {
      return rankMap.size();
   }

   public static int getAttributeSize() {
      return attributeMap.size();
   }

   public static Iterator getAttributeIterator() {
      return attributeMap.values().iterator();
   }

   public static PetItemData getOpenItem() {
      return openItem;
   }

   public static PetRank getRankHead() {
      return rankHead;
   }

   public static String getRiseAttributeTip1() {
      return riseAttributeTip1;
   }

   public static String getRiseAttributeTip2() {
      return riseAttributeTip2;
   }

   public static long getDiedRevivalSecond() {
      return diedRevivalSecond;
   }
}
