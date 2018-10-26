package com.usaa.bank.graph.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * Utility class for zip files and zip.
 */
public class ZipUtil {
    private static final int BUFFER_SIZE = 1024;

    /**
     * Gets a collection of zip-entry-names from the given inputStream.
     *
     * @param inputStream - Source ZipInputStream to collect entries.
     * @return - Set of strings representing the entries.
     */
    public static Set<String> getZipEntryNames(InputStream inputStream) {
        return ZipUtil.getZipEntryNames(new ZipInputStream(inputStream));
    }

    /**
     * Gets the set of entry names for the given ZipInputStream.
     *
     * @param zipInputStream - Source ZipInputStream to collect entries
     * @return - Set of strings representing the entries in zip.
     */
    public static Set<String> getZipEntryNames(ZipInputStream zipInputStream) {
        Set<String> zipEntrySet = null;
        if (zipInputStream != null) {
            zipEntrySet = new HashSet<String>();
            ZipEntry entry = null;
            try {
                while ((entry = zipInputStream.getNextEntry()) != null) {
                    zipEntrySet.add(entry.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return zipEntrySet;
    }

    /**
     * Creates a zip file from the given files array.
     *
     * @param zipFilePath - Absolute path to place the zip created.
     * @param files       - Files to be added to the zip
     * @param bufferSize  - Integer specifying the read buffer size of files.
     * @return - A File representing the zipFile created.
     */
    public static File createZipFile(String zipFilePath, File[] files, int bufferSize) {
        File zipFile = null;
        try {
            zipFile = new File(zipFilePath);
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
            ZipUtil.addToZip(zipOutputStream, files, "", new byte[bufferSize]);
            zipOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zipFile;
    }

    /**
     * Adds the files to the outputstream Writes the zip outputstream to the local File system.
     *
     * @param zipOutputStream - Zip Output-Stream that is to be written to the file system
     * @param files           - Files to be added to the Zip.
     * @param zipSubDir       - Zip-subdirectory where zip is placed.
     * @param buffer          -  Integer specifying the read buffer size of files.
     */
    private static void addToZip(ZipOutputStream zipOutputStream, File[] files, String zipSubDir, byte[] buffer) {
        if (zipOutputStream != null && files != null) {
            InputStream entryInputStream = null;
            for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {
                try {
                    if (files[fileIndex].isDirectory()) {
                        zipOutputStream.putNextEntry(new ZipEntry(zipSubDir + "/" + files[fileIndex].getName() + "/"));
                        ZipUtil.addToZip(zipOutputStream, files[fileIndex].listFiles(), zipSubDir + "/" + files[fileIndex].getName(), buffer);
                    } else {
                        try {
                            entryInputStream = new FileInputStream(files[fileIndex]);
                            zipOutputStream.putNextEntry(new ZipEntry(zipSubDir + "/" + files[fileIndex].getName()));
                            int length;
                            while ((length = entryInputStream.read(buffer)) > 0) {
                                zipOutputStream.write(buffer, 0, length);
                            }
                            zipOutputStream.closeEntry();
                            entryInputStream.close();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Unzip the given zipfile to the destination directory.
     *
     * @param zipFile        - Zip file to be un-zipped
     * @param destinationDir - Destination directory
     * @param prepareClean   - Force delete the pre-existing files in the destinationDir.
     */
    public static void unzip(File zipFile, File destinationDir, boolean prepareClean) {
        ZipUtil.unzip(zipFile, destinationDir, prepareClean, BUFFER_SIZE);
    }

    /**
     * Unzip the given zipfile to the destination directory. BufferSize is used to specifying the read buffer size of files.
     *
     * @param zipFile        - Zip file to be un-zipped
     * @param destinationDir - Destination directory
     * @param prepareClean   - Force delete the pre-existing files in the destinationDir.
     * @param bufferSize     - Integer specifying the read buffer size for files from zip.
     */
    public static void unzip(File zipFile, File destinationDir, boolean prepareClean, int bufferSize) {
        byte[] buffer = new byte[bufferSize];
        ZipInputStream zis = null;
        FileOutputStream fos = null;
        try {
            zis = new ZipInputStream(new FileInputStream(zipFile));

            if (destinationDir.exists()) {
                if (prepareClean) {
                    FileUtils.forceDelete(destinationDir);
                    FileUtils.forceMkdir(destinationDir);
                }
            } else {
                FileUtils.forceMkdir(destinationDir);
            }

            File newFile = null;
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                newFile = new File(destinationDir + File.separator + zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    FileUtils.forceMkdir(newFile);
                } else {
                    FileUtils.forceMkdir(new File(newFile.getParent()));
                    fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (zis != null) {
                try {
                    zis.close();
                } catch (IOException ioe) {
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ioe) {
                    //should already be closed under normal conditions
                }
            }
        }
    }

    /**
     * Read the given ZipInputStream as a String.
     *
     * @param zis        - ZipInputStream
     * @param bufferSize - Integer specifying the writing buffer size for files from zip.
     * @return - String representation for the contents of Zip stream.
     * @throws IOException - If an I/O error occurs
     */
    public static String readCurrentEntryAsString(ZipInputStream zis, int bufferSize) throws IOException {
        StringBuffer strBuf = new StringBuffer();
        if (zis != null) {
            byte[] buffer = new byte[bufferSize];
            int read = 0;
            while ((read = zis.read(buffer, 0, bufferSize)) >= 0) {
                strBuf.append(new String(buffer, 0, read));
            }
        }
        return strBuf.toString();
    }

    /**
     * Read the given ZipInputStream as a String.
     *
     * @param zis - ZipInputStream
     * @return - String representation for the contents of Zip stream.
     * @throws IOException - If an I/O error occurs
     */
    public static String readCurrentEntryAsString(ZipInputStream zis) throws IOException {
        return ZipUtil.readCurrentEntryAsString(zis, ZipUtil.BUFFER_SIZE);
    }

    /**
     * Read the given ZipInputStream as a byte array.
     *
     * @param zis        - ZipInputStream
     * @param bufferSize - Integer specifying the writing buffer size for files from zip.
     * @return - Byte Array representation for the contents of Zip stream.
     * @throws IOException - If an I/O error occurs
     */

    public static byte[] readCurrentEntryAsBytes(ZipInputStream zis, int bufferSize) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if (zis != null) {
            IOUtils.copy(zis, baos);
        }
        return baos.toByteArray();
    }

    /**
     * Read the given ZipInputStream as a byte array.
     *
     * @param zis - ZipInputStream
     * @return - Byte array representation for the contents of the Zip stream.
     * @throws IOException - If an I/O error occurs
     */
    public static byte[] readCurrentEntryAsBytes(ZipInputStream zis) throws IOException {
        return ZipUtil.readCurrentEntryAsBytes(zis, ZipUtil.BUFFER_SIZE);
    }
}