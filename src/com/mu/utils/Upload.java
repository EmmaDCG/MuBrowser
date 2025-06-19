package com.mu.utils;

import com.mu.db.Pool;
import com.mu.utils.buffer.BufferWriter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.imageio.ImageIO;
import org.apache.log4j.xml.DOMConfigurator;

public class Upload {
   private static final String sqlInsertMapData = "replace into mu_map values (?,?)";
   private static final String sqlUpdateMapData = "update mu_map set map_data = ? where map_id = ?";
   private static final String sqlUpdateIndexTemplate = "replace into system_script value(?,?,?,?)";

   public static void main(String[] args) {
      DOMConfigurator.configure("configs/log4j.xml");
      uploadMapConfigs();
      uploadMapConfigs();
   }

   private static Connection getGlobalConnection() {
      if (!Pool.isInited()) {
         Pool.init();
      }

      return Pool.getGlobalConnection();
   }

   private static byte[] parseMap(File file) {
      try {
         DataInputStream in = new DataInputStream(new FileInputStream(file));
         BufferWriter writer = new BufferWriter();
         writer.writeInt(in.readInt());
         writer.writeInt(in.readInt());
         writer.writeInt(in.readInt());
         writer.writeInt(in.readInt());
         int length = in.readInt();
         byte[] imageBytes = new byte[length];
         in.readFully(imageBytes, 0, length);
         BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
         int imgWidth = img.getWidth();
         int imgHeight = img.getHeight();
         writer.writeInt(imgWidth);
         writer.writeInt(imgHeight);
         byte[][] blocks = new byte[imgWidth][imgHeight];

         int i;
         for(i = 0; i < imgWidth; ++i) {
            for(int j = 0; j < imgHeight; ++j) {
               int rgb = img.getRGB(i, j);
               int r = rgb >> 16 & 255;
               int g = rgb >> 8 & 255;
               int b = rgb & 255;
               rgb = (r & 255) << 16 | (g & 255) << 8 | (b & 255) << 0;
               blocks[i][j] = (byte)(rgb > 0 ? 1 : 0);
            }
         }

         for(i = 0; i < blocks.length; ++i) {
            byte[] bytes = new byte[blocks[i].length];
            System.arraycopy(blocks[i], 0, bytes, 0, bytes.length);
            writer.writeBytes(bytes);
         }

         in.close();
         byte[] data = Zlib.compressBytes(writer.toByteArray());
         return data;
      } catch (Exception var15) {
         var15.printStackTrace();
         return null;
      }
   }

   private static void updateOrInsertMapData(int mapID, byte[] data, Connection conn) {
      try {
         ByteArrayInputStream in = new ByteArrayInputStream(data);
         PreparedStatement psUpdate = conn.prepareStatement("update mu_map set map_data = ? where map_id = ?");
         psUpdate.setBinaryStream(1, in);
         psUpdate.setInt(2, mapID);
         int result = psUpdate.executeUpdate();
         psUpdate.close();
         if (result == 0) {
            PreparedStatement psInsert = conn.prepareStatement("replace into mu_map values (?,?)");
            psInsert.setInt(1, mapID);
            psInsert.setBinaryStream(2, in);
            psInsert.executeUpdate();
            psInsert.close();
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   private static void updateIndex() {
      Connection conn = getGlobalConnection();

      try {
         String path = "index.html";
         DataInputStream dis = new DataInputStream(new FileInputStream(path));
         byte[] bytes = new byte[dis.available()];
         dis.readFully(bytes, 0, bytes.length);
         PreparedStatement ps = conn.prepareStatement("replace into system_script value(?,?,?,?)");
         ps.setInt(1, 78);
         ps.setString(2, "首页模板");
         ps.setBinaryStream(3, dis);
         ps.setString(4, "xml");
         ps.executeUpdate();
         ps.close();
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         Pool.getConnection();
      }

   }

   private static void uploadMapConfigs() {
      Connection conn = getGlobalConnection();

      try {
         String path = "D:/work/mu_map";
         File dir = new File(path);
         File[] fileList = dir.listFiles();

         for(int i = 0; i < fileList.length; ++i) {
            File file = fileList[i];
            if (!file.isDirectory()) {
               String name = file.getName();
               if (name.endsWith(".m")) {
                  String subName = name.substring(0, name.indexOf(46));
                  if (Tools.isNumber(subName)) {
                     updateOrInsertMapData(Integer.parseInt(subName), parseMap(file), conn);
                  }
               }
            }
         }
      } catch (Exception var11) {
         var11.printStackTrace();
      } finally {
         Pool.getConnection();
      }

   }
}
