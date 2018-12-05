package com.fengshihao.example.handyjson;

import java.util.ArrayList;
import java.util.LinkedList;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.fengshihao.handyjson.Exclude;
import com.fengshihao.handyjson.HandyJson;
import com.fengshihao.handyjson.JsonKey;


public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    TestModel tm = new TestModel();
    tm.age = 10;
    tm.name = "hello {handy} json";
    tm.mMems = new int[3];
    tm.checked = true;
    String json = HandyTestModel.toJson(tm);
    Log.d("MainActivity", "onCreate: json=" + json);
  }
}

@HandyJson
class TestModel {
  @JsonKey("jsonAge")
  int age;
  String name;
  boolean bool;

  @Exclude
  public boolean checked;

  int[] mMems;
  float[] floats;
  double[] doubles;

  @Exclude
  private int privateIgnore;

  Integer[] integers;


  ArrayList<String> names;
  LinkedList<Integer> nums;
}