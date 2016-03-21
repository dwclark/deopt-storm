# Deopt Storm

*deopt-storm* shows (I hope) a deoptimization storm when groovy is launched with invokedynamic compilation enabled

# Tools Needed

* An indy compilation enabled installation of groovy. [The Groovy Indy Page](http://groovy-lang.org/indy.html) has good instructions on how to set this up. I installed a fresh version of groovy and then ran the bash script on the page against it. Finally, I set the bin directory to the head of my PATH to use this version of groovy.
* VisualVM. If you are on Linux/open-jdk you will have to install it using your package manager. Otherwise it probably came with your Oracle JDK.
* VisualVM plugins. You want to install the Threads-Inspector and the Tracer-JVM Probes plugins for VisualVM.
* 
# Running

* `cd` to the appropriate directory.
* Execute in normal mode by using this command: `groovy deopt.groovy <number_threads> <iterations>`
    * Program will pause to give you a chance to attach any tools, attach VisualVM now.
    * Select the `Tracer` tab, click on the `JIT Compiler` check box, then click `Start`
    * Hit enter in the command line window.
    * You should see the following. In the Tracer tab the compiler activity will die down to 0-1% within a few seconds. In the Threads tab you will see threads prefixed with "deopt-" be asleep 99% of the time.
* Execute in indy mode by using this command: `groovy --indy deopt.groovy <number_threads> <iterations>`
    * Follow the instructions for normal mode
    * This time you should see the compiler thread use a significant percent of the CPU. On my 8 core machine it bounces up to 800% at times. It will die down to 0% for short periods of time, but will usually bounce back up to using a significant percentage of the CPU.
    * In the threads tab most "deopt-" threads will be in the Runnable state most of the time, suggesting they are waiting on the compiler thread.
* On my machine, the following command usually shows the problem: `groovy --indy deopt.groovy 1000 10000000`.

# Issues
* **If you have problems loading Tracer-JVM Probes** all is not lost. For instance my Mac laptop will mysteriously refuse to load the JVM probes, including the JIT compiler probe. If this is the case simply switch to the MBeans tab and watch the java.lang -> Compilation -> TotalCompilationTime value instead.
    * For non-indy runs this value should reach a steady state.
    * For indy runs this value will continue to rise. The value will repeatedly plateau for a few seconds and then jump up suddenly.
