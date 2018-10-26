package com.usaa.mapper.util;

import org.junit.Assert;
import org.junit.Test;


public class IndexerUtil_Test {

    @Test
    public void getClassifier_shouldReturnAnEmptyStringWhenPathIsEmpty() throws Exception {
        Assert.assertEquals("", IndexerUtil.getClassifier(""));
    }

    @Test
    public void getClassifier_shouldReturnJARWhenPathIsToAJar() throws Exception {
        Assert.assertEquals(IndexerUtil.CLASSIFIER_JAR, IndexerUtil.getClassifier("test.jar"));
    }

    @Test
    public void getClassifier_shouldReturnEARWhenPathIsToAEar() throws Exception {
        Assert.assertEquals(IndexerUtil.CLASSIFIER_EAR, IndexerUtil.getClassifier("test.ear"));
    }

    @Test
    public void getClassifier_shouldReturnWARWhenPathIsToAWar() throws Exception {
        Assert.assertEquals(IndexerUtil.CLASSIFIER_WAR, IndexerUtil.getClassifier("test.war"));
    }

    @Test
    public void getClassNameFromFullClassName_shouldReturnFullClassNameWhenNameIsNotNullorEmpty() throws Exception {
        String className = "className";
        Assert.assertEquals(className, IndexerUtil.getClassNameFromFullClassName(className));
    }

    @Test
    public void getClassNameFromFullClassName_shouldReturnNullWhenNameIsEmpty() throws Exception {
        Assert.assertNull(IndexerUtil.getClassNameFromFullClassName(""));
    }

    @Test
    public void getClassNameFromFullClassName_shouldReturnNullWhenClassNameIsNull() throws Exception {
        Assert.assertNull(IndexerUtil.getClassNameFromFullClassName(null));
    }

    @Test
    public void getPackageNameFromFullClassName_shouldReturnFullPackageNameWhenNameIsNotNullorEmpty() throws Exception {
        String artifactPath = "http://hub.test:test/Main.class";
        Assert.assertEquals("test/Main", IndexerUtil.getPackageNameFromFullClassName(artifactPath));
    }

    @Test
    public void getPackageNameFromFullClassName_shouldReturnNullWhenNameIsEmpty() throws Exception {
        Assert.assertEquals("", IndexerUtil.getPackageNameFromFullClassName(""));
    }

    @Test
    public void getPackageNameFromFullClassName_shouldReturnNullWhenPackageNameIsNull() throws Exception {
        Assert.assertEquals("", IndexerUtil.getPackageNameFromFullClassName(null));
    }


    @Test
    public void createIndexerUtilisNotNull() {
        IndexerUtil indexerUtil = new IndexerUtil();
        Assert.assertNotNull(indexerUtil);
    }

}

