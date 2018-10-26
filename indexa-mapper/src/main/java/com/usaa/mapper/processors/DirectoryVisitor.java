package com.usaa.mapper.processors;

import com.usaa.bank.asset.index.api.domain.asset.artifact.IArtifactIndex;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.IJavaClassDependencyIndex;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.IJavaClassIndex;
import com.usaa.mapper.processors.mappers.ClassMapper;
import com.usaa.mapper.util.IndexerUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipInputStream;


public class DirectoryVisitor implements FileVisitor<Path> {
    private IArtifactIndex artifactIndex;
    private IJavaClassIndex javaClassIndex;
    private IJavaClassDependencyIndex javaClassDependencyIndex;


    public DirectoryVisitor(IArtifactIndex artifactIndex, IJavaClassIndex javaClassIndex, IJavaClassDependencyIndex javaClassDependencyIndex) {
        super();
        this.artifactIndex = artifactIndex;
        this.javaClassIndex = javaClassIndex;
        this.javaClassDependencyIndex = javaClassDependencyIndex;
    }


    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
        if(path == null || path.toString() == ""){
            return FileVisitResult.CONTINUE;
        }
        String classifier = IndexerUtil.getClassifier(path.toFile().toString());
        if (StringUtils.isNotEmpty(classifier)) {
            Path storedPath = path;
            openArchive(storedPath, classifier);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    public void openArchive(Path path, String classifier) {
        if (path != null) {
            if (IndexerUtil.CLASSIFIER_EAR.equals(classifier) || IndexerUtil.CLASSIFIER_WAR.equals(classifier) || IndexerUtil.CLASSIFIER_JAR.equals(classifier)) {
                try (
                        FileInputStream hashFis = new FileInputStream(path.toFile());
                        FileInputStream artifactFis = new FileInputStream(path.toFile());
                        ZipInputStream zis = new ZipInputStream(artifactFis)) {
                        String artifactHash = DigestUtils.sha1Hex(IOUtils.toByteArray(hashFis));
                        new ClassMapper().indexArchiveArtifact(
                            path.toString(),
                            artifactHash,
                            zis,
                            artifactIndex,
                            javaClassIndex,
                            javaClassDependencyIndex);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
