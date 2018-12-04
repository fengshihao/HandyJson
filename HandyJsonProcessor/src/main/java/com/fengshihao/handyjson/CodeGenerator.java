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
    this.className = "Handy" + className;
  }

  String toJsonCode(String ob) {
    int last = fields.size();
    int i = 0;

    StringBuilder code = new StringBuilder("String jstr = \"{\";\n");
    for (ClassField f : fields) {
      f.toCode(code, ob);
      i += 1;
      if (i != last) {
        code.append("jstr += \",\" ;");
      }
      code.append("\n");
    }
    code.append("jstr += \"}\";");
    code.append("return jstr");
    return  code.toString();
  }

  void write(Filer filer) throws IOException {
    if (className == null || fields.isEmpty()) {
      return;
    }
    getJavaFile().writeTo(filer);
  }

  JavaFile getJavaFile() {
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

  void beginCheckNull(StringBuilder code, String objectName) {
    if (isArray || isString || isClass) {
      code.append("if (").append(objectName)
          .append(".").append(name).append(" != null) { ");
    }
  }

  void toCode(StringBuilder code, String objectName) {
    beginCheckNull(code, objectName);

    code.append("jstr +=  \"").append(getKeyCode()).append("\" + ");

    if (isString) {
      code.append("com.fengshihao.handyjson.Utils.quote(");
    }
    code.append(objectName).append(".").append(name);
    if (isString) {
      code.append(")");
    }
    code.append(';');
    endCheckNull(code);
    code.append('\n');
  }

  void endCheckNull(StringBuilder code) {
    if (isArray || isString || isClass) {
      code.append("}");
    }
  }
}