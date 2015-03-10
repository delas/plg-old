# Processes and Logs Generator

22 jul 2011 -- University of Padova, Italy

Process Log Generator is a application capable to generate random business processes, starting from some general "complexity paramenters". PLG is also able to "execute" a given process model in order to generate a process log.

This software is designed to help researchers in the construction of a large set of processes and corresponding execution logs. This software is released with a small library which could help in the programmatical creation of processes.

**Attention:** this software is not maintained anymore. A [complete rewriting](https://github.com/delas/plg) of PLG is ongoing.

## Execution
#### Package Requirement

In order to run this software, some used libraries need [Graphviz Dot](http://www.graphviz.org/Download.php) to be
installed in the computer.

#### How to Run the Software

To run the ProcessLogGenerator, there are two options:
- open a terminal and `cd` where the `ProcessLogGenerator.jar` file is located, then run
```bash
$ java -jar ProcessLogGenerator.jar
 ```
- right click the `ProcessLogGenerator.jar` file and click on "*Open with Sun/Oracle
     Java X Runtime*" (where X is your Java distribution number).

## Library Usage Example
Downloading the package, you will find also the small library to build your own process generator in a simple way. The following example illustate a way to construct a process and log:
```java
import java.io.IOException;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.out.XesXmlSerializer;
import it.unipd.math.plg.models.PlgProcess;

public class PlgTest {
    public static void main(String[] args) throws IOException {
        // define the new process and create some random activities
        // with, at most, 3 nested split 
        PlgProcess p = new PlgProcess("test process");
        p.randomize(3);

        // generate 10 process instances
        XLog log = p.generateXESLog(10, 100, 0);

        // serialize the log to the standard output
        XesXmlSerializer serializer = new XesXmlSerializer();
        serializer.serialize(log, System.out);
    }
}
```

## Citation

Please, cite this work as:
* Andrea Burattin and Alessandro Sperduti. "[PLG: a Framework for the Generation of Business Process Models and their Execution Logs](http://andrea.burattin.net/publications/2010-bpi)". In *Proceedings of the 6th International Workshop on Business Process Intelligence* (BPI 2010); Stevens Institute of Technology; Hoboken, New Jersey, USA; September 13, [2010.10.1007/978-3-642-20511-8_20](http://dx.doi.org/10.1007/978-3-642-20511-8_20).

## Disclaimer

The [releases section](https://github.com/delas/plg-old/releases) of this repository contains the entire project history and evolution. Each release provides both a source and a binary package. Please consider as accurate only the binary package.
