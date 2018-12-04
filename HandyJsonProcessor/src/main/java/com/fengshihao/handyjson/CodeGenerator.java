package com.fengshihao.handyjson;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

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
    StringBuilder code = new StringBuilder("return ").append('"') .append("{");
    for (ClassField f : fields) {
      code.append("\\\"").append(f.key).append("\\\":\" + ")
          .append(ob).append(".").append(f.name);

      i += 1;
      if (i != last) {
        code.append("+ \",\" + \"");
      }
    }
    code.append(" + \"}\"").append('\n');
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
}