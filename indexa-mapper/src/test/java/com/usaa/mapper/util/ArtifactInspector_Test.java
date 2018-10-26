package com.usaa.mapper.util;


import com.usaa.bank.asset.index.api.domain.asset.gav.ArtifactType;
import com.usaa.bank.asset.index.api.domain.asset.gav.GAV;
import com.usaa.bank.asset.index.api.domain.asset.gav.Version;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.nio.file.Path;

public class ArtifactInspector_Test {

    private Path path;
    private String virtualLocation;
    private Version defaultVersion;
    private GAV gav;
    private GAV comBarGAV;


    @Before
    public void setup() {
        this.path = new File("test").toPath();
        this.virtualLocation = "";
        this.defaultVersion = new Version("defaultVersion");
        this.gav = new GAV("testgroupId", "testartifactId", defaultVersion.toString(), ArtifactType.COMPONENT);
        this.comBarGAV = new GAV("com.bar", "testartifactId", defaultVersion.toString(), ArtifactType.COMPONENT);

    }

    @After
    public void tearDown() {
        System.gc();
    }

    @Test
    public void getArtifactGAV_shouldReturnArtifactGavWhenPathIsValidAndDefaultVersionIsProvided() throws Exception {
        String pathTest = "hub\\testgroupId\\testartifactId.defaultVersion";
        String expectedGAVString = gav.getGroupIdString() + gav.getArtifactIdString() + gav.getVersionString();
        GAV actualGAV = ArtifactInspector.getArtifactGAV(pathTest, defaultVersion);
        String actualGAVString = actualGAV.getGroupIdString() + actualGAV.getArtifactIdString() + actualGAV.getVersionString();
        Assert.assertEquals(expectedGAVString, actualGAVString);

    }


    @Test
    public void getArtifactGAV_shouldReturnArtifactGavWhenPathIsValidAndDefaultVersionIsNull() throws Exception {
        String pathTest = "hub\\testgroupId\\testartifactId";
        String expectedGAVString = gav.getGroupIdString() + gav.getArtifactIdString() + "unknown";
        GAV actualGAV = ArtifactInspector.getArtifactGAV(pathTest, null);
        String actualGAVString = actualGAV.getGroupIdString() + actualGAV.getArtifactIdString() + actualGAV.getVersionString();
        Assert.assertEquals(expectedGAVString, actualGAVString);
    }


    @Test
    public void getArtifactGAV_shouldReturnArtifactGavWhenVirtualLocationIsValidAndDefaultVersionIsProvided() throws Exception {
        this.virtualLocation = "hub\\com\\bar\\testartifactId.jar";
        GAV testGAV = ArtifactInspector.getArtifactGAV(virtualLocation, defaultVersion);
        String theComBarGAV = comBarGAV.getGroupIdString() + comBarGAV.getArtifactId() + comBarGAV.getVersionString();
        String theTestGAV = testGAV.getGroupIdString() + testGAV.getArtifactId() + testGAV.getVersionString();
        Assert.assertEquals(theTestGAV, theComBarGAV);
    }


    @Test
    public void getArtifactGAV_shouldReturnArtifactGavWhenVirtualLocationContainsPeriodAndDefaultVersionIsProvided() throws Exception {
        this.virtualLocation = "hub\\com\\bar\\testartifactId";
        GAV testGAV = ArtifactInspector.getArtifactGAV(virtualLocation, defaultVersion);
        String theComBarGAV = comBarGAV.getGroupIdString() + comBarGAV.getArtifactId() + comBarGAV.getVersionString();
        String theTestGAV = testGAV.getGroupIdString() + testGAV.getArtifactId() + testGAV.getVersionString();
        Assert.assertEquals(theTestGAV, theComBarGAV);
    }


    @Test
    public void getArtifactGAV_shouldNotReturnArtifactGavWhenVirtualLocationIsNullAndDefaultVersionIsValid() throws Exception {
        String virtualLocation = null;
        Assert.assertNull(ArtifactInspector.getArtifactGAV(virtualLocation, defaultVersion));
    }


    @Test
    public void parseVersionNrOffPath_shouldReturnTheVersionNumberOfTheFileWhenGivenAVirtualLocationWhichContainsVersionNumber() throws Exception {
        this.virtualLocation = "hub\\test\\apps\\test_comm-1.2.3456.jar";
        String expectedVersionNumber = "1.2.3456";
        Assert.assertEquals(expectedVersionNumber, ArtifactInspector.parseVersionNrOffPath(virtualLocation));
    }


    @Test
    public void parseVersionNrOffPath_shouldReturnUnknownStringWhenVirtualLocationDoesNotExist() throws Exception {
        this.virtualLocation = "thisVirtualLocationDoesNotExist";
        Assert.assertEquals("unknown", ArtifactInspector.parseVersionNrOffPath(virtualLocation));
    }


    @Test
    public void parseVersionNrOffPath_shouldReturnUnknownStringWhenFilenameDoesNotContainVersionNumber() throws Exception {
        Assert.assertEquals("unknown", ArtifactInspector.parseVersionNrOffPath(virtualLocation));
    }


    @Test
    public void parseArtifactIdOffPath_shouldReturnTheExpectedArtifactIDWhenGivenAPathToAFile() throws Exception {
        String fileLocation = "C:\\test\\apps\\test_comm.jar";
        this.path = new File(fileLocation).toPath();
        String expectedArtifactId = "test_comm.jar";
        Assert.assertEquals(expectedArtifactId, ArtifactInspector.parseArtifactIdOffPath(path));
    }

    @Test
    public void parseArtifactIdOffPath_shouldReturnEmptyStringWhenPathIsNull() throws Exception {
        String test = null;
        Assert.assertEquals("", ArtifactInspector.parseArtifactIdOffPath(test));
    }


    @Test
    public void parseArtifactIdOffPath_shouldReturnTheExpectedArtifactIDWhenGivenADirectVirtualLocation() throws Exception {
        this.virtualLocation = "\\test_comm.jar";
        String expectedArtifactId = "test_comm.jar";
        Assert.assertEquals(expectedArtifactId, ArtifactInspector.parseArtifactIdOffPath(virtualLocation));
    }


    @Test
    public void parseArtifactIdOffPath_shouldReturnTheExpectedArtifactIDWhenGivenAVirtualLocation() throws Exception {
        this.virtualLocation = "\\hub\\apps\\test_comm.jar";
        String expectedArtifactId = "test_comm.jar";
        Assert.assertEquals(expectedArtifactId, ArtifactInspector.parseArtifactIdOffPath(virtualLocation));
    }
    @Test
    public void parseArtifactIdOffPath_shouldReturnEmptyStringWhenVirtualLocationIsNull() throws Exception {
        this.virtualLocation = null;
        Assert.assertEquals("", ArtifactInspector.parseArtifactIdOffPath(virtualLocation));
    }


    @Test
    public void parseGroupIdOffPath_shouldReturnGroupIDWhenGivenAPathString() throws Exception {
        String pathTest = "hub\\testgroupId\\test";

        Assert.assertEquals(gav.getGroupIdString(), ArtifactInspector.parseGroupIdOffPath(pathTest));
    }

    @Test
    public void parseGroupIdOffPath_shouldReturnGroupIDWhenGivenAVirtualDirectory() throws Exception {
        this.virtualLocation = "hub\\test\\apps\\test_comm-1.2.3456.jar";
        String expectedGroupID = "test.apps";
        Assert.assertEquals(expectedGroupID, ArtifactInspector.parseGroupIdOffPath(virtualLocation));
    }

    @Test
    public void parseGroupIdOffPath_shouldReturnGroupIDWhenGivenAVirtualDirectoryContainingConfigDir() throws Exception {
        this.virtualLocation = "hub\\test\\apps\\test_comm-1.2.3456.jar";
        String expectedGroupID = "test.apps";
        Assert.assertEquals(expectedGroupID, ArtifactInspector.parseGroupIdOffPath(virtualLocation));
    }

    @Test
    public void parseGroupIdOffPath_shouldReturnGroupIDWhenGivenAVirtualDirectoryContainingScriptsDir() throws Exception {
        this.virtualLocation = "hub\\test\\apps\\test_comm-1.2.3456.jar";
        String expectedGroupID = "test.apps";
        Assert.assertEquals(expectedGroupID, ArtifactInspector.parseGroupIdOffPath(virtualLocation));
    }
    @Test
    public void parseGroupIdOffPath_shouldReturnEmptyStringWhenVirtualDirectoryIsEmptyString() throws Exception {
        this.virtualLocation = "";
        Assert.assertEquals("", ArtifactInspector.parseGroupIdOffPath(virtualLocation));
    }

}