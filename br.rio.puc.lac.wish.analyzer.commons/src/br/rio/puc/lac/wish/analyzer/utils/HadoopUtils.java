package br.rio.puc.lac.wish.analyzer.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HadoopUtils {

  /**
   * 
   * @param configuration
   * @param path
   * @return
   */
  public static List<String> readFileAndReturnListOfLines(
    Configuration configuration, Path path) {
    List<String> lines = new ArrayList<String>();
    try {
      FileSystem fileSystem = FileSystem.get(path.toUri(), configuration);
      if (!fileSystem.isDirectory(path)) {
        return null;
      }
      FileStatus[] files = fileSystem.listStatus(path);
      for (FileStatus fs : files) {
        if (!fs.isDir()) {
          BufferedReader reader =
            new BufferedReader(new InputStreamReader(fileSystem.open(fs
              .getPath())));
          String str;
          while ((str = reader.readLine()) != null) {
            lines.add(str);
          }
          reader.close();
        }
      }
      fileSystem.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return lines;
  }

}
