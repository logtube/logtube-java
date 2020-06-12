package io.github.logtube.utils;

import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;

public class RotationFileTest {

    @Test
    public void fromFiles() {
        HashSet<String> files = new HashSet<>();
        files.add(Paths.get("xlog", "test.file.name.ROT00001.log").toString());
        files.add(Paths.get("xlog", "test.file.name.ROT00002.log").toString());
        files.add(Paths.get("xlog", "test.file.name.ROT00003.log").toString());
        files.add(Paths.get("xlog", "test.file.name.log").toString());
        files.add(Paths.get("others", "test.file.name2.ROT2020-06-12.log").toString());
        files.add(Paths.get("others", "test.file.name2.ROT2020-06-11.log").toString());
        files.add(Paths.get("others", "test.file.name2.ROT2020-06-10.log").toString());
        files.add(Paths.get("others", "test.file.name2.log").toString());
        files.add(Paths.get("others", "wired-something.log").toString());
        HashMap<String, RotationFile> rotationFiles = RotationFile.fromFiles(files);
        System.out.println(rotationFiles);
    }

    @Test
    public void recoverFilename() {
        String[] rec1 = RotationFile.recoverFilename(Paths.get("others", "test.file.name2.ROT2020-06-10.log").toString());
        Assert.assertEquals(
                Paths.get("others", "test.file.name2.log").toString(),
                rec1[0]
        );
        Assert.assertEquals(
                "2020-06-10",
                rec1[1]
        );
    }

    @Test
    public void deriveFilename() {
        Assert.assertEquals(
                Paths.get("others", "test.file.name2.ROT2020-06-10.log").toString(),
                RotationFile.deriveFilename(Paths.get("others", "test.file.name2.log").toString(), "2020-06-10")
        );
    }
}