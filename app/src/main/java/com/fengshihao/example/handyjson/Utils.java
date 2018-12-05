package com.fengshihao.example.handyjson;

import java.util.List;


public class Utils {

  public static String toJson(String type, List list) {
    if (list == null) {
      return null;
    }
    if (list.isEmpty()) {
      return "[]";
    }

    StringBuilder str = new StringBuilder("[");
    int size = list.size();
    if (type.endsWith("<java.lang.String>")) {
      for (int i = 0; i < size; i++) {
        String o = (String) list.get(i);
        str.append(toJson(o));
        if (i < size - 1) {
          str.append(',');
        }
      }
    } else if (type.endsWith("<java.lang.Integer>")) {
      for (int i = 0; i < size; i++) {
        Integer o = (Integer) list.get(i);
        str.append(o.toString());
        if (i < size - 1) {
          str.append(',');
        }
      }
    }

    str.append("]");
    return str.toString();
  }


  public static String toJson(String string) {
    if (string == null || string.length() == 0) {
      return "\"\"";
    }

    char c;
    int i;
    int len = string.length();
    StringBuilder sb = new StringBuilder(len + 4);
    String t;

    sb.append('"');
    for (i = 0; i < len; i += 1) {
      c = string.charAt(i);
      switch (c) {
        case '\\':
        case '"':
          sb.append('\\');
          sb.append(c);
          break;
        case '/':
          //                if (b == '<') {
          sb.append('\\');
          //                }
          sb.append(c);
          break;
        case '\b':
          sb.append("\\b");
          break;
        case '\t':
          sb.append("\\t");
          break;
        case '\n':
          sb.append("\\n");
          break;
        case '\f':
          sb.append("\\f");
          break;
        case '\r':
          sb.append("\\r");
          break;
        default:
          if (c < ' ') {
            t = "000" + Integer.toHexString(c);
            sb.append("\\u" + t.substring(t.length() - 4));
          } else {
            sb.append(c);
          }
      }
    }
    sb.append('"');
    return sb.toString();
  }

  public static String arrayToJson(Object obj, String type) {
    if (obj == null) {
      return null;
    }
    return null;
  }

  public static String toJson(boolean[] arr) {
    if (arr == null) {
      return null;
    }

    StringBuilder js = new StringBuilder("[");
    for (int i = 0; i < arr.length; i++) {
      js.append(arr[i]);
      if (i < arr.length - 1) {
        js.append(',');
      }
    }
    js.append("]");
    return js.toString();
  }

  public static String toJson(String[] arr) {
    if (arr == null) {
      return null;
    }

    StringBuilder js = new StringBuilder("[");
    for (int i = 0; i < arr.length; i++) {
      js.append(toJson(arr[i]));
      if (i < arr.length - 1) {
        js.append(',');
      }
    }
    js.append("]");
    return js.toString();
  }

  public static String toJson(int[] arr) {
    if (arr == null) {
      return null;
    }

    StringBuilder js = new StringBuilder("[");
    for (int i = 0; i < arr.length; i++) {
      js.append(arr[i]);
      if (i < arr.length - 1) {
        js.append(',');
      }
    }
    js.append("]");
    return js.toString();
  }

  public static String toJson(double[] arr) {
    if (arr == null) {
      return null;
    }

    StringBuilder js = new StringBuilder("[");
    for (int i = 0; i < arr.length; i++) {
      js.append(arr[i]);
      if (i < arr.length - 1) {
        js.append(',');
      }
    }
    js.append("]");
    return js.toString();
  }

  public static String toJson(float[] arr) {
    if (arr == null) {
      return null;
    }

    StringBuilder js = new StringBuilder("[");
    for (int i = 0; i < arr.length; i++) {
      js.append(arr[i]);
      if (i < arr.length - 1) {
        js.append(',');
      }
    }
    js.append("]");
    return js.toString();
  }

  public static String toJson(char[] arr) {
    if (arr == null) {
      return null;
    }

    StringBuilder js = new StringBuilder("[");
    for (int i = 0; i < arr.length; i++) {
      js.append("\"").append(arr[i]).append("\"");
      if (i < arr.length - 1) {
        js.append(',');
      }
    }
    js.append("]");
    return js.toString();
  }

  public static<T> String toJson(T[] arr) {
    if (arr == null) {
      return null;
    }

    StringBuilder js = new StringBuilder("[");
    for (int i = 0; i < arr.length; i++) {
      js.append(arr[i]);
      if (i < arr.length - 1) {
        js.append(',');
      }
    }
    js.append("]");
    return js.toString();
  }
}
