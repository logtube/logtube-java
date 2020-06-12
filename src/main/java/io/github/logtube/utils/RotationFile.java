package io.github.logtube.utils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class RotationFile {

    private static final String ROT = "ROT";

    @NotNull
    private final String filename;

    @NotNull
    private final Set<String> marks = new HashSet<>();


    public RotationFile(@NotNull String filename) {
        this.filename = filename;
    }

    @NotNull
    public String getFilename() {
        return filename;
    }

    @NotNull
    public Set<String> getMarks() {
        return marks;
    }

    @Override
    public String toString() {
        return "RotationFile{" +
                "filename='" + filename + '\'' +
                ", marks=" + marks +
                '}';
    }

    /**
     * 从一组原始文件中，将日志文件和切割后文件进行分组
     *
     * @param files 原始文件
     * @return 分组后的日志文件
     */
    public static HashMap<String, RotationFile> fromFiles(@NotNull Set<String> files) {
        HashMap<String, RotationFile> result = new HashMap<>();
        files.forEach((f) -> {
            String[] recs = recoverFilename(f);
            RotationFile rf = result.get(recs[0]);
            if (rf == null) {
                rf = new RotationFile(recs[0]);
                result.put(recs[0], rf);
            }
            if (!recs[1].isEmpty()) {
                rf.getMarks().add(recs[1]);
            }
        });
        return result;
    }

    public static String[] recoverFilename(@NotNull String filename) {
        File file = new File(filename);
        String dirname = file.getParent();
        String basename = file.getName();
        String[] split = basename.split("\\.");
        if (split.length < 2) {
            return new String[]{filename, ""};
        }
        if (split[split.length - 2].startsWith(ROT)) {
            String mark = split[split.length - 2].substring(ROT.length());
            StringBuilder newBasename = new StringBuilder();
            for (int i = 0; i < split.length - 2; i++) {
                newBasename.append(split[i]).append(".");
            }
            newBasename.append(split[split.length - 1]);
            return new String[]{Paths.get(dirname, newBasename.toString()).toString(), mark};
        }
        return new String[]{filename, ""};
    }

    public static String deriveFilename(@NotNull String filename, @NotNull String mark) {
        File file = new File(filename);
        String dirname = file.getParent();
        String basename = file.getName();
        String[] split = basename.split("\\.");
        if (split.length < 2) {
            return filename;
        }
        StringBuilder newBasename = new StringBuilder();
        for (int i = 0; i < split.length - 1; i++) {
            newBasename.append(split[i]).append(".");
        }
        newBasename.append(ROT).append(mark).append(".");
        newBasename.append(split[split.length - 1]);
        return Paths.get(dirname, newBasename.toString()).toString();
    }

}
