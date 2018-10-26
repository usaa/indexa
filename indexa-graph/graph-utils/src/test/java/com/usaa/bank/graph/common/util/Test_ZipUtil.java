package com.usaa.bank.graph.common.util;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class Test_ZipUtil {
    File zipFile;
    File zipFile1;
    File fileToZip;
    InputStream inputStream;
    ZipInputStream zipInputStream;
    Set<String> filelist = new HashSet<>();


    @Before
    public void setup() throws FileNotFoundException, MalformedURLException {
        fileToZip = new File("src/test/resources/testForZipUtil/fileToBeZipped.txt");
        zipFile1 = new File("src/test/resources/testForZipUtil/testForZipUtil1.zip");
        zipFile = new File("src/test/resources/testForZipUtil/testForZipUtil.zip");
        inputStream = new FileInputStream(zipFile);
        zipInputStream = new ZipInputStream(inputStream);
        filelist.add("readme.txt");

    }

    @Test
    public void getZipEntryNamesGivenInputStreamOfZipFileReturnsSetOfFilesContained(){
        assertEquals(ZipUtil.getZipEntryNames(inputStream), filelist);
    }

    @Test
    public void getZipEntryNamesGivenNullReturnsNull(){
        assertEquals(ZipUtil.getZipEntryNames(null), null);
    }

    @Test
    public void getZipEntryNamesGivenZipInputStreamReturnsSetOfFilesContained(){
        assertEquals(ZipUtil.getZipEntryNames(zipInputStream), filelist);
    }


    @Test
    public void createZipFileGivenNullsReturnsNull(){
        assertNull(ZipUtil.createZipFile(null, null, 0));
    }

    @Test
    public void unZipGivenZipFileDestinationDirAndTrueAndBufferSize(){
        ZipUtil.unzip(zipFile1, new File("src/test/resources/testZipUtil/"), true, 1024);
        assertTrue(new File("src/test/resources/testZipUtil/fileToBeZipped.txt").exists());
    }
    @Test
    public void unZipGivenZipFileDestinationDirAndTrue(){
        ZipUtil.unzip(zipFile1, new File("src/test/resources/testZipUtil/"), true);
        assertTrue(new File("src/test/resources/testZipUtil/fileToBeZipped.txt").exists());
    }
    @Test(expected = NullPointerException.class)
    public void unZipGivenNulls(){
        ZipUtil.unzip(null,null,true, 1024);
        assertTrue(new File("src/test/resources/testZipUtil/fileToBeZipped.txt").exists());
    }

    @Test
    public void readCurrentEntryAsStringGivenZipInputStreamAndBufferSizeReturnsEmpty() throws IOException {
        assertEquals(ZipUtil.readCurrentEntryAsString(zipInputStream, 1024), "");
    }

    @Test
    public void readCurrentEntryAsStringGivenZipInputStreamReturnsEmpty() throws IOException {
        assertEquals(ZipUtil.readCurrentEntryAsString(zipInputStream), "");
    }

    @Test
    public void readCurrentEntryAsStringGivenNullReturnsEmpty() throws IOException {
        assertEquals(ZipUtil.readCurrentEntryAsString(null), "");
    }

    @Test
    public void readCurrentEntryAsBytesGivenZipInputStreamAndBufferSizeReturnsEmpty() throws IOException {
        assertEquals(ZipUtil.readCurrentEntryAsBytes(zipInputStream, 1024).length, 0);
    }

    @Test
    public void readCurrentEntryAsBytesGivenZipInputStreamReturnsEmpty() throws IOException {
        assertEquals(ZipUtil.readCurrentEntryAsBytes(zipInputStream).length, 0);
    }

    @Test
    public void readCurrentEntryAsBytesGivenNullReturnsEmpty() throws IOException {
        assertEquals(ZipUtil.readCurrentEntryAsBytes(null).length, 0);
    }


}
