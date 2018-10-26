package com.usaa.mapper.processors;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.usaa.bank.asset.index.impl.domain.LuceneJavaClassDependencyIndex;
import com.usaa.bank.asset.index.impl.domain.LuceneJavaClassIndex;
import com.usaa.bank.asset.index.impl.domain.LuceneArtifactIndex;
import com.usaa.bank.asset.index.impl.domain.LuceneDependencyIndex;
import com.usaa.bank.graph.lucene.LuceneIndexDAOLocalImpl;


public class Indexer {
    private static final String artifactIndexPath = ".artifactIndex";
    private static final String classIndexPath = ".classIndex";
    private static final String classDependencyIndexPath = ".classDependencyIndex";
    private static final String artifactDependencyIndexPath = ".artifactDependencyIndex";

    public static void runIndexer(File inputDirectory){
        String indexDir =  System.getProperty("os.name").toLowerCase().contains("win") ? "C:/SmallIndexes/"  : System.getProperty("user.dir") + "/SmallIndexes/";
        LuceneArtifactIndex artifactIndex = new LuceneArtifactIndex(new LuceneIndexDAOLocalImpl(new File(indexDir + artifactIndexPath), true, 1));
        LuceneDependencyIndex artifactDependencyIndex = new LuceneDependencyIndex(new LuceneIndexDAOLocalImpl(new File(indexDir + artifactDependencyIndexPath), true, 1));
        LuceneJavaClassIndex javaClassIndex = new LuceneJavaClassIndex(new LuceneIndexDAOLocalImpl(new File(indexDir +classIndexPath),true,1));
        LuceneJavaClassDependencyIndex javaClassDependencyIndex = new LuceneJavaClassDependencyIndex(new LuceneIndexDAOLocalImpl(new File(indexDir + classDependencyIndexPath),true,1));

        artifactIndex.openSession();
        javaClassIndex.openSession();
        javaClassDependencyIndex.openSession();
        artifactDependencyIndex.openSession();

        try {
            Files.walkFileTree(
                    Paths.get(inputDirectory.getPath()),
                    new DirectoryVisitor(
                            artifactIndex,
                            javaClassIndex,
                            javaClassDependencyIndex));
        } catch (IOException e) {
            e.printStackTrace();
        }

        artifactIndex.flushSession();
        javaClassIndex.flushSession();
        javaClassDependencyIndex.flushSession();
        artifactDependencyIndex.flushSession();

        DependencyResolver.resolveDependenciesForArtifact(artifactIndex,artifactDependencyIndex,javaClassIndex, javaClassDependencyIndex);

        artifactIndex.closeSession();
        javaClassIndex.closeSession();
        javaClassDependencyIndex.closeSession();
        artifactDependencyIndex.closeSession();


    }

}
