package com.usaa.mapper.processors.mappers;


import com.usaa.bank.asset.index.api.domain.asset.artifact.IArtifact;
import com.usaa.bank.asset.index.api.domain.asset.artifact.IArtifactIndex;
import com.usaa.bank.asset.index.api.domain.asset.gav.GAV;
import com.usaa.bank.asset.index.api.domain.asset.gav.Version;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.HierarchicalPackage;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.IJavaClass;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.IJavaClassDependencyIndex;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.IJavaClassIndex;
import com.usaa.mapper.util.ArtifactInspector;
import com.usaa.mapper.util.IndexerUtil;
import javassist.ClassPool;
import javassist.CtClass;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ClassMapper {

    private static final String JAR_FILE_EXTENSION = ".jar";
    private static final String WAR_FILE_EXTENSION = ".war";
    private static final String EAR_FILE_EXTENSION = ".ear";
    private static final String CLASS_FILE_EXTENSION = ".class";

    public IArtifact indexArchiveArtifact(
            String artifactPath,
            String artifactHash,
            ZipInputStream zipInputStream,
            IArtifactIndex artifactIndex,
            IJavaClassIndex javaClassIndex,
            IJavaClassDependencyIndex javaClassDependencyIndex) {

        System.out.println("processing: " + artifactPath);
        IArtifact artifact = null;

        GAV gav = ArtifactInspector.getArtifactGAV(artifactPath, new Version("unknown"));

        if (gav != null) {
            Map<String, Set<String>> fieldMap = null;
            Set<String> tags = null;
            artifact =
                    artifactIndex.create(
                            artifactHash,
                            artifactPath,
                            gav.getGroupId(),
                            gav.getArtifactId(),
                            gav.getVersion(),
                            gav.getArtifactType(),
                            IndexerUtil.getClassifier(artifactPath),
                            tags,
                            fieldMap);

            if (artifact != null) {
                if (IndexerUtil.CLASSIFIER_JAR.equals(artifact.getClassifier()) || IndexerUtil.CLASSIFIER_WAR.equals(artifact.getClassifier())
                        || IndexerUtil.CLASSIFIER_EAR.equals(artifact.getClassifier())) {
                    try {
                        indexJavaInternals(artifact, artifactIndex, javaClassIndex, javaClassDependencyIndex, zipInputStream);
                    } catch (IOException ioe) {
                        System.out.println("error indexing @indexJavaInternals function: " + ioe.getMessage());
                    }
                }
                System.out.println("indexed artifact with GAV: " + artifact.getGAV().getStringValue() + "with classifier: " + artifact.getClassifier() + " and hash: " + artifact.getHash());
            }
        }

        return artifact;
    }

    public String indexJavaInternals(
            IArtifact artifact,
            IArtifactIndex artifactIndex,
            IJavaClassIndex javaClassIndex,
            IJavaClassDependencyIndex javaClassDependencyIndex,
            ZipInputStream zipInputStream) throws IOException {

        ZipEntry zipEntry = null;
        String indexedArtifactType = null;

        try {
            zipEntry = zipInputStream.getNextEntry();
        } catch (IOException e) {
            System.out.println("IOException while reading next entry in ZipInputStream: " + e.getMessage());
            throw e;
        }

        while (zipEntry != null) {
            if (zipEntry.getName().endsWith(CLASS_FILE_EXTENSION)) {
                indexedArtifactType = CLASS_FILE_EXTENSION;
                ClassIndexing(zipEntry, artifact, javaClassIndex, javaClassDependencyIndex, zipInputStream);

            } else if (zipEntry.getName().toLowerCase().endsWith(JAR_FILE_EXTENSION) || zipEntry.getName().toLowerCase().endsWith(WAR_FILE_EXTENSION)) {
                indexedArtifactType = JAR_FILE_EXTENSION + "or" + WAR_FILE_EXTENSION;
                try {
                    indexArchiveArtifact(
                            artifact.getArtifactPath() + "!" + zipEntry.getName(),
                            DigestUtils.sha1Hex(IOUtils.toByteArray(zipInputStream)),
                            new ZipInputStream(zipInputStream),
                            artifactIndex,
                            javaClassIndex,
                            javaClassDependencyIndex);
                } catch (Exception e) {
                    System.out.println("IOException while indexing the artifact " + e.getMessage());
                    throw e;
                }
            }

            try {
                zipEntry = zipInputStream.getNextEntry();
            } catch (IOException e) {
                System.out.println("IOException while getting next entry of ZIP-Stream -> " + zipEntry + " @path " + artifact.getArtifactPath() + ". Reason: " + e.getMessage());
                throw e;
            }
        }
        return indexedArtifactType;
    }

    private void ClassIndexing(ZipEntry zipEntry, IArtifact artifact,
                               IJavaClassIndex javaClassIndex,
                               IJavaClassDependencyIndex javaClassDependencyIndex, ZipInputStream zipInputStream) throws IOException {


        String ctPackageName = null;
        String ctClassName = null;
        String refClassPackage = null;
        String refClassName = null;
        CtClass ctClass = null;
        final ClassPool classPool = new ClassPool();

                try {
                    ctClass = classPool.makeClass(zipInputStream);
                } catch (IOException e) {
                    throw e;
                }
                ctPackageName = IndexerUtil.getPackageNameFromFullClassName(ctClass.getName());
                ctClassName = IndexerUtil.getClassNameFromFullClassName(ctClass.getName());
                if (StringUtils.isNotEmpty(ctPackageName) && StringUtils.isNotEmpty(ctClassName)) {
                    IJavaClass javaClass =
                            javaClassIndex.create(
                                    artifact.getHash(),
                                    new HierarchicalPackage(ctPackageName),
                                    ctClassName,
                                    (long) 0,
                                    (long) 0,
                                    0,
                                    0);

                    if (ctClass.getRefClasses() != null) {
                        for (Object refClass : ctClass.getRefClasses()) {
                            refClassPackage = IndexerUtil.getPackageNameFromFullClassName(refClass.toString());
                            refClassName = IndexerUtil.getClassNameFromFullClassName(refClass.toString());
                            if (StringUtils.isNotEmpty(refClassPackage) && StringUtils.isNotEmpty(refClassName)) {
                                javaClassDependencyIndex.addDependency(
                                        javaClass.getGUID(),
                                        new HierarchicalPackage(refClassPackage),
                                        refClassName);
                            }
                        }
                    }
                }
    }
}
