package dev.luckynetwork.lib.dependencyhelper.profile;

public class DependencyProfile {

    private final String fileName, fileUrl;

    public DependencyProfile(String fileName, String fileUrl) {
        if (fileName.equals("")) {
            String[] splits = fileUrl.split("/");
            for (String split : splits) {
                if (!split.endsWith(".jar")) {
                    continue;
                }
                fileName = split;
                break;
            }
        }
        if (!fileName.endsWith(".jar")) {
            fileName += ".jar";
        }

        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

}
