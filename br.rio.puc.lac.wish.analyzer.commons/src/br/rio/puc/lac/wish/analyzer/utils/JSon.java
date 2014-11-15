package br.rio.puc.lac.wish.analyzer.utils;

import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

/**
 * 
 * 
 * 
 * @author Marcelo Malcher
 */
public class JSon {

  /**
   * 
   * @param <T>
   * @param theClass
   * @param theObject
   * @return
   */
  public static <T> String toJSONString(Class<T> theClass, T theObject) {
    return toJSONString(theClass, theObject, null);
  }

  /**
   * 
   * @param <T>
   * @param theClass
   * @param theObject
   * @param classMap
   * @return
   */
  public static <T> String toJSONString(Class<T> theClass, T theObject,
    Map classMap) {
    JsonConfig jsonConfig = new JsonConfig();
    jsonConfig.setRootClass(theClass);
    if (classMap != null) {
      jsonConfig.setClassMap(classMap);
    }
    JSONObject jsonObj =
      (JSONObject) JSONSerializer.toJSON(theObject, jsonConfig);
    return jsonObj.toString();
  }

  /**
   * @param <T>
   * @param jsonString
   * @param theClass
   * @return
   */
  public static <T> T getFromJSONString(String jsonString, Class<T> theClass) {
    return getFromJSONString(jsonString, theClass, null);
  }

  /**
   * @param <T>
   * @param jsonString
   * @param theClass
   * @param classMap
   * @return
   */
  public static <T> T getFromJSONString(String jsonString, Class<T> theClass,
    Map classMap) {
    JSON json = JSONSerializer.toJSON(jsonString);
    JsonConfig jsonConfig = new JsonConfig();
    jsonConfig.setRootClass(theClass);
    if (classMap != null) {
      jsonConfig.setClassMap(classMap);
    }
    Object obj = JSONSerializer.toJava(json, jsonConfig);
    return theClass.cast(obj);
  }
}
