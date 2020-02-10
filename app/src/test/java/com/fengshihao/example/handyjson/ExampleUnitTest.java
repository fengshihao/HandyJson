package com.fengshihao.example.handyjson;

import org.junit.Test;

import android.support.annotation.NonNull;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
  @Test
  public void addition_isCorrect() throws Exception {
    String json = "{\"name\" : 298}";
    JsonTokensReader reader = new JsonTokensReader(json);
    assertEquals(4, 2 + 2);
  }
}

class JsonTokensReader {
  @NonNull
  private String mData;
  private int mPos;

  public JsonTokensReader(String json) {
    if (json == null) {
      return;
    }
    mData = json;
  }

  public char read() {
    char c = mData.charAt(mPos);
    mPos += 1;
    return c;
  }

  public boolean isOver() {
    return mPos >= mData.length() -1;
  }

  public String getKey() {
    return "";
  }
}