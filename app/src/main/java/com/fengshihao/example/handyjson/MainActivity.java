package com.fengshihao.example.handyjson;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    tm.integers = new Integer[] {2,45,-10};
    tm.names = new LinkedList<>();
    tm.names.add("name 1");
    tm.names.add("name \r2");
    tm.names.add("name \n3");

    tm.subObject = new SubObject();
    tm.subObject.name = "subobject";
    tm.integers = new Integer[] {211,4225,-103};


    String json = TestModelHandy.toJson(tm);
    Log.d("MainActivity", "onCreate: json=" + json);

    JSONObject object;
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


  List<String> names;
  LinkedList<Integer> nums;

  SubObject subObject;
}

@HandyJson
class SubObject {
  @JsonKey("jsonAge")
  int age;
  String name;
  boolean bool;
  Integer[] integers;
}