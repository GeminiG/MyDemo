package com.example.demo.jython;

import org.python.util.PythonInterpreter;

public class JythonDemo {
    public static void main(String []args) {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.exec("days=('Mod', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun')");
        interpreter.exec("print days[1]");
        interpreter.execfile("NetworkX.py");
        interpreter.exec("print 'created by tengxing on 2017.3'");
    }
}
