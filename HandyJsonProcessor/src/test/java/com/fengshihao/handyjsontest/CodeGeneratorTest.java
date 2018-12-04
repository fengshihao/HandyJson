package com.fengshihao.handyjsontest;

import org.junit.Test;

import com.fengshihao.handyjson.Exclude;
import com.fengshihao.handyjson.HandyJson;
import com.fengshihao.handyjson.JsonKey;

public class CodeGeneratorTest {


  @Test
  public void test() {
    TestModel tm = new TestModel();
    tm.age = 10;
    tm.name = "hello handy json";
    tm.mMems = new int[3];
    tm.checked = true;
    String json = HandyTestModel.toJson(tm);
    System.out.print(json);
  }
}


@HandyJson
class TestModel {
  @JsonKey("jsonAge")
  int age;

  @Exclude
  public boolean checked;
  String name;
  int[] mMems;

  @Exclude
  private int privateIgnore;
}