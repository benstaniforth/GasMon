package org.softwire.training.gasmon.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;

public class Utilities {

    public static void writeToFile(String averageAsString, File file) throws IOException {
        FileWriter writer = new FileWriter(file, true);
        writer.write(averageAsString + "\n");
        writer.close();
    }
}
