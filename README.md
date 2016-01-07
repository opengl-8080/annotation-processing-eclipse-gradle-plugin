# annotation-processing-eclipse-gradle-plugin
Gradle plugin that generate eclipse configuration for Pluggable Annotation Processing API.

## Installation
See [Gradle - Plugin: com.github.opengl-BOBO.annotation-processing-eclipse](https://plugins.gradle.org/plugin/com.github.opengl-BOBO.annotation-processing-eclipse)

## What is this?
This plugin modify `eclipse` and `cleanEclipse` tasks.

This plugin enable **Annotation Processing** configurations for Eclipse.

## Configuration
**build.gradle**

```groovy
ape {
    processorJarPattern = /processor-jar-pattern.*\.jar/
}
```

`processorJarPattern` option is regular expression to find a jar file including annotation processor class.

This plugin find a jar file in `compile` configurations.

You can set closure that returns a `File` object into `processorJarPattern` option.

```groovy
ape {
    processorJarPattern = {
        return configurations.compile.find {it.name =~ /processor-jar-pattern.*\.jar/}
    }
}
```

## Modified Tasks
### eclipse
You run `eclipse` task then this task generates Annotation Processing configurations (`.factorypath` and `.settings/org.eclipse.jdt.apt.core.prefs`).

And this task also appends a source folder (`.apt_generated/`) into a `.classpath` file.

### cleanEclipse
You run 'cleanEclipse' task then this task remove all generated files (`.factorypath`, `.settings/org.eclipse.jdt.apt.core.prefs`)

## 日本語の説明
### インストール方法
[Gradle - Plugin: com.github.opengl-BOBO.annotation-processing-eclipse](https://plugins.gradle.org/plugin/com.github.opengl-BOBO.annotation-processing-eclipse)

このページに設定方法が載っているので、そちらを参照ください。

### これはなに？
`eclipse` タスクを実行したら、注釈処理を有効にする設定ファイルを自動生成するプラグインです。

### 設定
**build.gradle**

```groovy
ape {
    processorJarPattern = /processor-jar-pattern.*\.jar/
}
```

`processorJarPattern` オプションに正規表現を設定します。
この正規表現は、注釈処理を行うクラスを含んだ jar ファイルを見つけるために使用されます。

なお、 jar ファイルはコンフィギュレーションの `compile` から検索します。

`processorJarPattern` には、注釈処理を含んだ jar を返すクロージャを設定することもできます。

```groovy
ape {
    processorJarPattern = {
        return configurations.compile.find {it.name =~ /processor-jar-pattern.*\.jar/}
    }
}
```

この場合、クロージャが `return` した `File` オブジェクトが注釈処理用の jar として使用されます。

### 修正されるタスク
#### eclipse
`eclipse` タスクを実行すると、以下の注釈処理用の設定ファイルが自動生成されます。

- `.factorypath`
- `.settings/org.eclipse.jdt.apt.core.prefs`

また、ソースフォルダとして `.apt_generated/` が追加されます。

#### cleanEclipse
`cleanEclipse` タスクを実行すると、 `eclipse` タスクで追加したファイル（`.factorypath` と `.settings/org.eclipse.jdt.apt.core.prefs`）を全て削除します。
