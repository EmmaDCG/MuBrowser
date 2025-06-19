package com.mu.utils;

import com.mu.db.manager.GlobalLogDBManager;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import jxl.Sheet;
import jxl.Workbook;

public class DFA {
   private static List arrt = new ArrayList();
   private static DFA.Node rootNode = new DFA.Node();

   private static ArrayList searchWord(String content) {
      int a = 0;
      char[] chars = content.toCharArray();
      DFA.Node node = rootNode;
      StringBuffer word = new StringBuffer();

      ArrayList words;
      for(words = new ArrayList(); a < chars.length; ++a) {
         node = findNode(node, chars[a]);
         if (node == null) {
            node = rootNode;
            a -= word.length();
            word.setLength(0);
         } else if (node.flag == 1) {
            word.append(chars[a]);
            words.add(word.toString());
            a = a - word.length() + 1;
            word.setLength(0);
            node = rootNode;
         } else {
            word.append(chars[a]);
         }
      }

      node = null;
      word = null;
      chars = null;
      return words;
   }

   public static String getDFAStr(String content) {
      ArrayList list = searchWord(content);

      for(int i = 0; i < list.size(); ++i) {
         String src = (String)list.get(i);
         StringBuffer sb = new StringBuffer();

         for(int j = 0; j < src.length(); ++j) {
            sb.append("*");
         }

         content = content.replace(src, sb.toString());
         sb = null;
      }

      list.clear();
      list = null;
      return content;
   }

   public static boolean hasKeyWords(String content) {
      int a = 0;
      char[] chars = content.toCharArray();
      DFA.Node node = rootNode;
      StringBuffer word = new StringBuffer();

      try {
         for(; a < chars.length; ++a) {
            node = findNode(node, chars[a]);
            if (node == null) {
               node = rootNode;
               a -= word.length();
               word.setLength(0);
            } else {
               if (node.flag == 1) {
                  return true;
               }

               word.append(chars[a]);
            }
         }

         return false;
      } finally {
         node = null;
         word = null;
         Object var8 = null;
      }
   }

   public static synchronized void initial(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet sheet = wb.getSheet(1);
      int rows = sheet.getRows();

      for(int i = 1; i < rows; ++i) {
         arrt.add(sheet.getCell(0, i).getContents().trim());
      }

      ArrayList list = GlobalLogDBManager.getBadWordList();
      Iterator var6 = list.iterator();

      while(var6.hasNext()) {
         String s = (String)var6.next();
         arrt.add(s.trim());
      }

      rootNode = new DFA.Node();
      createTree();
      arrt.clear();
      list.clear();
   }

   private static void createTree() {
      Iterator var1 = arrt.iterator();

      while(var1.hasNext()) {
         String str = (String)var1.next();
         char[] chars = str.toCharArray();
         if (chars.length > 0) {
            insertNode(rootNode, chars, 0);
         }
      }

   }

   private static void insertNode(DFA.Node node, char[] cs, int index) {
      DFA.Node n = findNode(node, cs[index]);
      if (n == null) {
         n = new DFA.Node();
         node.nodes.put(cs[index], n);
      }

      if (index == cs.length - 1) {
         n.flag = 1;
      }

      ++index;
      if (index < cs.length) {
         insertNode(n, cs, index);
      }

   }

   private static DFA.Node findNode(DFA.Node node, char c) {
      HashMap nodes = node.nodes;
      return (DFA.Node)nodes.get(c);
   }

   private static class Node {
      public int flag = 0;
      public HashMap nodes = new HashMap();
   }
}
