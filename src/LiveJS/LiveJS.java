/**
 * LiveJS.java
 *
 * @author		Naoto Hieda http://naotohieda.com
 * @modified	2017/Feb/27
 * @version		0.1
 */

package LiveJS;

import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.Invocable;

import java.lang.NoSuchMethodException;
import java.lang.reflect.*;

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import processing.core.*;

public class LiveJS {
  PApplet parent;

  private static ScriptEngineManager engineManager;
  private static ScriptEngine nashorn;

  public static String VERSION = "0.1";

  private static String scriptPath;
  private static boolean first = true;

  private static long prevModified = 0;
  private static byte[] encoded;
  public static String readFile(String path) throws IOException {
    long lastModified = Files.getLastModifiedTime(Paths.get(path)).toMillis();
    if (prevModified < lastModified || encoded == null) {
      encoded = Files.readAllBytes(Paths.get(path));
      System.out.println("updated at " + lastModified);
      prevModified = lastModified;
    }
    return new String(encoded, StandardCharsets.UTF_8);
  }

  public LiveJS(PApplet parent, final String scriptPath) {
    this.parent = parent;
    parent.registerMethod("dispose", this);
    this.scriptPath = scriptPath;

    engineManager = new ScriptEngineManager();
    nashorn = engineManager.getEngineByName("nashorn");

    try {
      Object global = nashorn.eval("this");
      Object jsObject = nashorn.eval("Object");
      // calling Object.bindProperties(global, this);
      // which will "bind" properties of the PApplet object
      ((Invocable)nashorn).invokeMethod(jsObject, "bindProperties", global, (PApplet)parent);
      nashorn.eval("function define(varname, val){if(typeof this[varname] == 'undefined')this[varname] = val;}");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void draw() {
    String jsCode = "";
    try {
      jsCode = readFile(scriptPath);
    }
    catch (IOException e) {
      e.printStackTrace();
    }

    if (first) {
      first = false;
    }
    try {
      nashorn.eval(jsCode);
    }
    catch (ScriptException e) {
      e.printStackTrace();
    }
  }

  public void dispose() {
    // Anything in here will be called automatically when
    // the parent sketch shuts down. For instance, this might
    // shut down a thread used by this library.
  }
}
