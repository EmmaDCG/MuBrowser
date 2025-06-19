package com.test;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.SortedMap;

public class TreeMapTest
{
  public TreeMapTest() {}
  
  public static void main(String[] args)
  {
    int count = 100000;
    
    int[] indexs = new int[count];
    for (int i = 0; i < count; i++) {
      indexs[i] = com.mu.utils.Rnd.get(0, count - 1);
    }
    
    long time = System.currentTimeMillis();
    
    HashMap<Integer, Integer> comMaps = new HashMap();
    for (int i = 0; i < count; i++) {
      comMaps.put(Integer.valueOf(i), Integer.valueOf(i));
    }
    
    System.out.println(System.currentTimeMillis() - time);
    time = System.currentTimeMillis();
    
    SortedMap<Integer, Integer> sorts = new java.util.TreeMap();
    for (int i = 0; i < count; i++) {
      sorts.put(Integer.valueOf(i), Integer.valueOf(i));
    }
    
    System.out.println(System.currentTimeMillis() - time);
    time = System.currentTimeMillis();
    
    for (int i = 0; i < count; i++) {
      int index = indexs[i];
      comMaps.get(Integer.valueOf(index));
    }
    
    System.out.println(System.currentTimeMillis() - time);
    time = System.currentTimeMillis();
    
    for (int i = 0; i < count; i++) {
      int index = indexs[i];
      sorts.get(Integer.valueOf(index));
    }
    
    System.out.println(System.currentTimeMillis() - time);
  }
}
