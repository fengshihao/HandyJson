package com.fengshihao.handyjson;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

/**
 * Created by fengshihao on 18-10-16.
 */
class CodeGenerator {
  String originalName;
  String className;
  String packageName;
  List<ClassField> fields = new LinkedList<>();

  CodeGenerator(String packageName, String className) {
    this.packageName = packageName;
    this.originalName = className;
    this.className = className + "Handy";
  }

  String toJsonCode(String ob) {
    int last = fields.size();
    int i = 0;

    StringBuilder code = new StringBuilder("String jstr = \"{\";\n");
    for (ClassField f : fields) {
      i += 1;
      f.toCode(code, ob, i != last);
      code.append("\n");
    }
    code.append(
    "if (jstr.charAt(jstr.length() - 1) == ',') { \n" +
    "    jstr = jstr.substring(0, jstr.length() - 1); \n" +
    "}");
    code.append("\njstr += \"}\";");
    code.append("\nreturn jstr");
    return  code.toString();
  }

  void write(Filer filer) throws IOException {
    if (className == null || fields.isEmpty()) {
      return;
    }
    getJavaFile().writeTo(filer);
  }

  private JavaFile getJavaFile() {
    String argName = "o";
    ParameterSpec parameterSpec = ParameterSpec.builder(
        TypeVariableName.get(originalName), argName).build();

    MethodSpec toJson = MethodSpec.methodBuilder("toJson")
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .returns(String.class)
        .addParameter(parameterSpec)
        .addStatement(toJsonCode(argName))
        .build();

    MethodSpec fromJson = MethodSpec.methodBuilder("fromJson")
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .addParameter(parameterSpec)
        .build();

    TypeSpec handyClass = TypeSpec.classBuilder(className)
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        .addMethod(toJson)
        .addMethod(fromJson)
        .build();

    return JavaFile.builder(packageName, handyClass)
        .build();
  }
}


class ClassField {
  String name;
  String key;
  String typeName;
  boolean isArray;
  boolean isClass;
  boolean isString;

  private String getKeyCode() {
    return "\\\"" + key + "\\\":";
  }

  private void beginCheckNull(StringBuilder code, String objectName) {
    if (isArray || isString || isClass) {
      code.append("if (").append(objectName)
          .append(".").append(name).append(" != null) {\n");
    }
  }

  void toCode(StringBuilder code, String objectName, boolean notLastOne) {
    beginCheckNull(code, objectName);
    code.append("\tjstr +=  \"").append(getKeyCode()).append("\" + ");

    boolean isObj = isString || isArray;
    boolean needEnd = false;
    if (isObj) {
      code.append("com.fengshihao.example.handyjson.Utils.toJson(");
      needEnd = true;
    }
    if (isClass) {
      if (typeName.startsWith("java.")) {
        code.append("com.fengshihao.example.handyjson.Utils.toJson(")
            .append("\"").append(typeName).append("\"").append(", ");
        needEnd = true;
      } else {
        code.append(typeName).append("Handy.toJson(");
        needEnd = true;
      }
    }
    code.append(objectName).append(".").append(name);
    if (needEnd) {
      //code.append(", \"").append(typeName).append("\")");
      code.append(")");
    }
    code.append(';');
    if (notLastOne) {
      code.append("\n\tjstr += \",\" ;");
    }

    endCheckNull(code);
    code.append('\n');
  }

  void endCheckNull(StringBuilder code) {
    if (isArray || isString || isClass) {
      code.append("\n}");
    }
  }
}