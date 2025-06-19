package com.mu.config;

import java.io.InputStream;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class VariableConstant {
   public static int MaxNameSize = 7;
   public static int Max_Level_2Limit = 9999;
   public static int Max_Level_2Exp = 1;
   public static String NamePrefix = "s%.";
   public static int MaxAG = 100;
   public static int Pk_Level = 60;
   public static int DefaultDeathDelay = 600;
   public static int MaxAP = 20000;
   public static int MaxGangNameSize = 6;
   public static int Strength_Lucky = 5000;
   public static int Lucky_Attack = 5000;
   public static int MinLevelToAddPotential = 30;
   public static int PK_Evil_Red = 1;
   public static int Pk_Evil_Max = 600;
   public static int Pk_Evil_Minute = 1;
   public static int Pk_Evil_Every = 60;
   public static float ATK_SPEED_COE = 0.01F;
   public static final int InspireLifeBuff = 80006;
   public static final int InspirehurtBuff = 80007;
   public static final int Fly_Item = 2000;
   public static final int WashPotentialItem = 2143;
   public static final int MoneyItemID = 2015;
   public static int maxStatNameLength = 2;
   public static int statNameNumber = 2;

   public static void init(InputStream in) throws Exception {
      SAXBuilder sb = new SAXBuilder();
      Document doc = sb.build(in);
      Element root = doc.getRootElement();
      MaxNameSize = root.getChild("MaxNameSize").getAttribute("value").getIntValue();
      NamePrefix = root.getChild("NamePrefix").getAttribute("value").getValue();
      MaxGangNameSize = root.getChild("MaxGangNameSize").getAttribute("value").getIntValue();
   }
}
