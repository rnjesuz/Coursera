# Environment setup with coursier
## Installing coursier
Follow the [coursier native launcher installation guide](https://get-coursier.io/docs/cli-installation.html#native-launcher) specific to your platform:

* [Windows](https://get-coursier.io/docs/cli-installation.html#windows)
* [macOS](https://get-coursier.io/docs/cli-installation.html#linux-macos)
* [macOS with brew](https://get-coursier.io/docs/cli-installation.html#macos-brew-based-installation)
* [Unix, Linux and macOS](https://get-coursier.io/docs/cli-installation.html#linux-macos)

## Setup with coursier
The following command downloads and installs the [Scala Build Tool (sbt)](https://get-coursier.io/docs/cli-installation.html#linux-macos) as well as [Adopt OpenJDK 11](https://adoptopenjdk.net/index.html?variant=openjdk11&jvmVariant=hotspot), the Java Development Kit version 11, which is required for the assignments:

`cs setup --jvm adopt:11`

In case you did not add `cs` to your `PATH`, you need to go to the directory that contains cs from a terminal:

* Windows: Open the Command prompt and go to the directory which contains `cs` using the `dir` command like so: `dir PATH\TO\cs`
    Run `.\cs setup --jvm adopt:11`

* macOS and Linux: open the Terminal and go to the directory which contains `cs` using the `cd` command: `cd path/to/cs`
    Run `./cs setup --jvm adopt:11`

# SBT Tutorial
We use **sbt for building, testing, running and submitting assignments**. This tutorial explains all sbt commands that you will use during our class. The [Tools Setup](https://www.coursera.org/learn/progfun1/supplement/BNOBK/tools-setup) page explains how to install sbt.

The following page introduces:

1. Basic sbt tasks useful for any Scala developer. For more details about SBT or their commands, we highly recommend you to check the [SBT Reference Manual](http://www.scala-sbt.org/0.13/docs/Getting-Started.html) or this [SBT tutorial](https://github.com/shekhargulati/52-technologies-in-2016/blob/master/02-sbt/README.md) made by the Scala Community.

2. A way to submit your assignments to our Coursera graders with SBT.

## The basics
### Base or project's root directory
In sbt’s terminology, the “base or project's root directory” is the directory containing the project. So if you go into any SBT project, you'll see a `build.sbt` declared in the top-level directory that is, the base directory.

### Source code
Source code can be placed in the project’s base directory as with hello/hw.scala. However, most people don’t do this for real projects; too much clutter.

SBT uses the same directory structure as [Maven](https://maven.apache.org/) for source files by default (all paths are relative to the base directory):
1
```
src/
  main/
    resources/
       <files to include in main jar here>
    scala/
       <main Scala sources>
    java/
       <main Java sources>
  test/
    resources
       <files to include in test jar here>
    scala/
       <test Scala sources>
    java/
       <test Java sources>
```
Other directories in src/ will be ignored. Additionally, all hidden directories will be ignored.

### SBT build definition [files](http://www.scala-sbt.org/0.13/docs/Directories.html#sbt+build+definition+files)

You’ve already seen build.sbt in the project’s base directory. Other sbt files appear in a project subdirectory.

The project folder can contain .scala files, which are combined with .sbt files to form the complete build definition. See [organizing the build](https://www.scala-sbt.org/1.x/docs/Organizing-Build.html) for more.
```
build.sbt
project/
  Build.scala
```
You may see .sbt files inside project/ but they are not equivalent to .sbt files in the project’s base directory. Explaining this will come [later](http://www.scala-sbt.org/0.13/docs/Organizing-Build.html), since you’ll need some background information first.

## SBT tasks
### Starting up sbt

In order to start sbt, open a terminal ("Command Prompt" in Windows) and navigate to the directory of the assignment you are working on (where the build.sbt file is). Typing sbt will open the sbt command prompt.
```shell
# This is the shell of the operating system
$ cd /path/to/parprog-project-directory
$ sbt
# This is the sbt shell
>
```
SBT commands are executed inside the SBT shell. Don't try to execute them in the Scala REPL, because they won't work.

### Running the Scala Interpreter inside SBT
The Scala interpreter is different than the SBT command line.
However, you can start the **Scala interpreter inside sbt** using the `console` task. The interpreter (also called REPL, for "read-eval-print loop") is useful for trying out snippets of Scala code. When the REPL is executed from SBT, all your code in the SBT project will also be loaded and you will be able to access it from the interpreter. That's why the Scala REPL can only start up if there are no compilation errors in your code.

In order to quit the interpreter and get back to sbt, type _<Ctrl+D>_.
```shell
sbt:progfun1-example> console
[info] compiling 1 Scala source to /../example/target/scala-3.0.0/classes ...

scala> println("Oh, hai!")  # This is the Scala REPL, type some Scala code 
Oh, hai!

scala> val l = List(1, 2, 3)
val l: List[Int] = List(1, 2, 3)

scala> val squares = l.map(x => x * x)
val squares: List[Int] = List(1, 4, 9)

scala>                     # Type [ctrl-d] to exit the Scala REPL
[success] Total time: 20 s, completed Jun 9, 2021 11:02:31 AM
> 
```

### Compiling your Code
The compile task will compile the source code of the assignment which is located in the directory src/main/scala.
```shell
> compile
[info] Compiling 4 Scala sources to /Users/aleksandar/example/target/scala-3.0.0/classes...
[success] Total time: 1 s, completed Jun 9, 2021, 11:04:46 PM
> 
```
If the source code contains errors, the error messages from the compiler will be displayed.

## Testing your Code
The directory src/test/scala contains unit tests for the project. In order to run these tests in sbt, you can use the test command.
```shell
sbt:example> test
[info] compiling 1 Scala source to /tmp/tmp.MZL9ct7CXo/example/target/scala-3.0.0/test-classes ...
example.ListsSuite:
  + one plus one is two (0pts) 0.019s
==> X example.ListsSuite.one plus one is three (0pts)?  0.015s munit.FailException: /tmp/tmp.MZL9ct7CXo/example/src/test/scala/example/ListsSuite.scala:24 assertion failed
23:  test("one plus one is three (0pts)?") {
24:    assert(1 + 1 == 3) // This assertion fails! Go ahead and fix it.
25:  }
    at munit.FunSuite.assert(FunSuite.scala:11)
    at example.ListsSuite.$init$$$anonfun$2(ListsSuite.scala:16)
==> X example.ListsSuite.details why one plus one is not three (0pts)  0.012s munit.ComparisonFailException: /tmp/tmp.MZL9ct7CXo/example/src/test/scala/example/ListsSuite.scala:60
59:  test("details why one plus one is not three (0pts)") {
60:    assertEquals(1 + 1, 3) // Fix me, please!
61:  }
values are not the same
=> Obtained
2
=> Diff (- obtained, + expected)
-2
+3
    at munit.FunSuite.assertEquals(FunSuite.scala:11)
    at example.ListsSuite.$init$$$anonfun$3(ListsSuite.scala:60)
  + intNotZero throws an exception if its argument is 0 0.0s
  + sum of a few numbers (10pts) 0.002s
  + max of a few numbers (10pts) 0.001s
[error] Failed: Total 6, Failed 2, Errors 0, Passed 4
[error] Failed tests:
[error]         example.ListsSuite
[error] (Test / test) sbt.TestsFailedException: Tests unsuccessful
[error] Total time: 1 s, completed Jun 9, 2021, 8:21:41 AM
```

### Running your Code
If your project has an object with a main method (or an object extending the trait App), then you can run the code in sbt easily by typing run. In case sbt finds multiple main methods, it will ask you which one you'd like to execute.

```shell
> run

Multiple main classes detected, select one to run:
 [1] example.Lists
 [2] example.M2

Enter number: 1

[info] Running example.Lists 
main method!
[success] Total time: 33 s, completed Jun 9, 2021 10:25:06 PM
>
```