package com.usaa.mapper.processors;

import com.usaa.bank.asset.index.api.domain.asset.artifact.IArtifact;
import com.usaa.bank.asset.index.api.domain.asset.artifact.IArtifactIndex;
import com.usaa.bank.asset.index.api.domain.asset.dependency.DependencyScope;
import com.usaa.bank.asset.index.api.domain.asset.dependency.IDependencyIndex;
import com.usaa.bank.asset.index.api.domain.asset.gav.ArtifactType;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.IJavaClass;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.IJavaClassDependencyIndex;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.IJavaClassIndex;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.JavaClassDependency;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class DependencyResolver {

    public static void resolveDependenciesForArtifact(IArtifactIndex artifactIndex, IDependencyIndex artifactDependencyIndex, IJavaClassIndex javaClassIndex,
                                                      IJavaClassDependencyIndex javaClassDependencyIndex) {

        // Iterate the artifacts entries with ArtifactType as COMPONENT

        Map<String, String> artifact_java = new HashMap<String, String>();

        for (IArtifact artifact : artifactIndex.find(ArtifactType.COMPONENT)) {
            if (artifact != null) {
                System.out.println("processing artifact: " + artifact.getGUID());
                Set<IJavaClass> javaClassSet = javaClassIndex.find(artifact.getHash());
                if (javaClassSet != null) {
                    for (IJavaClass javaClass : javaClassSet) {
                        Set<JavaClassDependency> javaClassDependencySet = javaClassDependencyIndex.findDependencies(javaClass.getGUID());
                        if (javaClassDependencySet != null) {
                            for (JavaClassDependency javaClassDependency : javaClassDependencySet) {
                                if (!javaClassDependency.getDependencyPackage().toString().startsWith("java.")) {
                                    Set<IJavaClass> dependencyJavaClassSet = javaClassIndex.find(javaClassDependency.getDependencyPackage(), javaClassDependency.getDependencyClassName());
                                    if (dependencyJavaClassSet != null) {
                                        for (IJavaClass dependencyJavaClass : dependencyJavaClassSet) {
                                            Set<IArtifact> dependencyArtifactSet = artifactIndex.find(dependencyJavaClass.getArtifactHash());
                                            if (dependencyArtifactSet != null && !dependencyArtifactSet.contains(artifact) && dependencyArtifactSet.size() > 0) {
                                                for (IArtifact dependencyArtifact : dependencyArtifactSet) {
                                                    if (!dependencyArtifact.getArtifactPath().equals("/")) {
                                                        System.out.println("adding artifact dependency. dependent:" + artifact.getGUID() + " dependency: " + dependencyArtifact.getGUID());
                                                        artifactDependencyIndex.addDependency(
                                                                artifact.getGUID(),
                                                                dependencyArtifact.getGUID(),
                                                                DependencyScope.BUILD,
                                                                null);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
