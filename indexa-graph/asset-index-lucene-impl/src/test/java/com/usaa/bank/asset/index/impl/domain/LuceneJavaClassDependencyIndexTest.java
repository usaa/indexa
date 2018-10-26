package com.usaa.bank.asset.index.impl.domain;

import java.util.HashSet;
import java.util.Set;

import com.usaa.bank.asset.index.api.domain.asset.java.classes.HierarchicalPackage;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.JavaClass;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.JavaClassDependency;
import org.apache.lucene.store.RAMDirectory;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.usaa.bank.graph.common.identity.GUID;
import com.usaa.bank.graph.lucene.LuceneIndexDAOLocalImpl;
import com.usaa.bank.graph.common.validate.ValidationException;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(Enclosed.class)
public class LuceneJavaClassDependencyIndexTest
{
	private static final String fooDependentHash = "hashfoo";
	private static final HierarchicalPackage fooDependentPackage = new HierarchicalPackage("com.foo");
	private static final String fooDependentClass = "FooDependentClass";
	private static final GUID fooDependentGUID = JavaClass.createGUID(fooDependentHash, fooDependentPackage, fooDependentClass);

	private static final String barDependentHash = "hashbar";
	private static final HierarchicalPackage barDependentPackage = new HierarchicalPackage("com.bar");
	private static final String barDependentClass = "BarDependentClass";
	private static final GUID barDependentGUID = JavaClass.createGUID(barDependentHash, barDependentPackage, barDependentClass);

	private static final HierarchicalPackage fooPackage = new HierarchicalPackage("com.foo");
	private static final String dependencyClassName = "DependencyClass";

	private static final HierarchicalPackage bazPackage = new HierarchicalPackage("com.baz");
	private static final String anotherDependencyClassName = "AnotherDependencyClass";
	
	
	@RunWith(JUnitParamsRunner.class)
	public static class addDependencyTest
	{
		
		@Test(expected=ValidationException.class)
		@Parameters(method="nullInputParameters")
		public void GivenIndex_WhenNullInput_ThenValidationExpection(GUID guid, HierarchicalPackage hierarchicalPackage, String className)
		{
			LuceneJavaClassDependencyIndex jcdIndex = new LuceneJavaClassDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
			jcdIndex.openSession();
			jcdIndex.addDependency(guid, hierarchicalPackage, className);
			jcdIndex.closeSession();
		}
		
		@Test
		public void GivenIndex_WhenValidInput_ThenDependencyExists()
		{
			LuceneJavaClassDependencyIndex jcdIndex = new LuceneJavaClassDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
			jcdIndex.openSession();
			jcdIndex.addDependency(fooDependentGUID, fooPackage, dependencyClassName);
			jcdIndex.flushSession();
			Set<JavaClassDependency> dependencyClassSet = jcdIndex.findDependencies(fooDependentGUID);
			
			Set<JavaClassDependency> expectedSet = new HashSet<JavaClassDependency>();
			expectedSet.add(new JavaClassDependency(fooDependentGUID, fooPackage, dependencyClassName));
			
			Assert.assertThat(dependencyClassSet, Is.is(IsEqual.equalTo(expectedSet)));
		
			jcdIndex.closeSession();
		}


		@Test
		public void GivenIndexExistingClassName_WhenValidInput_ThenDependencyExists()
		{
			LuceneJavaClassDependencyIndex jcdIndex = new LuceneJavaClassDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
			jcdIndex.openSession();
			jcdIndex.addDependency(fooDependentGUID, fooPackage, dependencyClassName);
			jcdIndex.addDependency(fooDependentGUID, bazPackage, dependencyClassName);
			jcdIndex.flushSession();
			Set<JavaClassDependency> dependencyClassSet = jcdIndex.findDependencies(fooDependentGUID);

			Set<JavaClassDependency> expectedSet = new HashSet<JavaClassDependency>();
			expectedSet.add(new JavaClassDependency(fooDependentGUID, fooPackage, dependencyClassName));
			expectedSet.add(new JavaClassDependency(fooDependentGUID, bazPackage, dependencyClassName));

			Assert.assertThat(dependencyClassSet, Is.is(IsEqual.equalTo(expectedSet)));

			jcdIndex.closeSession();
		}
		
		@Test
		public void GivenIndexDuplicateDependency_WhenValidInput_ThenDependencyOverwritten()
		{
			LuceneJavaClassDependencyIndex jcdIndex = new LuceneJavaClassDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
			jcdIndex.openSession();
			jcdIndex.addDependency(fooDependentGUID, fooPackage, dependencyClassName);
			jcdIndex.addDependency(fooDependentGUID, fooPackage, dependencyClassName);
			jcdIndex.flushSession();
			Set<JavaClassDependency> dependencyClassSet = jcdIndex.findDependencies(fooDependentGUID);
			
			Set<JavaClassDependency> expectedSet = new HashSet<JavaClassDependency>();
			expectedSet.add(new JavaClassDependency(fooDependentGUID, fooPackage, dependencyClassName));
		
			Assert.assertThat(dependencyClassSet, Is.is(IsEqual.equalTo(expectedSet)));
			
			jcdIndex.closeSession();
		}
		
		// =================================================
		// utility methods
		// =================================================
		@SuppressWarnings("unused")
		private static Object[] nullInputParameters()
		{
			return new Object[][]{
				{null, fooPackage, dependencyClassName}
				, {fooDependentGUID, null, dependencyClassName}
				, {fooDependentGUID, fooPackage, null}
			};
		}
	}
	
	
	@RunWith(JUnitParamsRunner.class)
	public static class removeDependencyTest
	{
		@Test(expected=ValidationException.class)
		@Parameters(method="nullInputParemeters")
		public void GivenIndex_WhenNullInput_ThenValidationExpection(GUID guid, HierarchicalPackage hierarchicalPackage, String className)
		{
			LuceneJavaClassDependencyIndex jcdIndex = new LuceneJavaClassDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
			jcdIndex.openSession();
			jcdIndex.removeDependency(guid, hierarchicalPackage, className);
			jcdIndex.closeSession();
		}
		
		@Test
		public void GivenIndexExistingDependency_WhenValidInput_ThenDependencyRemoved()
		{
			LuceneJavaClassDependencyIndex jcdIndex = new LuceneJavaClassDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
			jcdIndex.openSession();
			jcdIndex.addDependency(fooDependentGUID, fooPackage, dependencyClassName);
			jcdIndex.flushSession();
			
			Set<JavaClassDependency> dependencyClassSet = jcdIndex.findDependencies(fooDependentGUID);
			Set<JavaClassDependency> expectedSet = new HashSet<JavaClassDependency>();
			expectedSet.add(new JavaClassDependency(fooDependentGUID, fooPackage, dependencyClassName));
			Assert.assertThat(dependencyClassSet, Is.is(IsEqual.equalTo(expectedSet)));
			
			jcdIndex.removeDependency(fooDependentGUID, fooPackage, dependencyClassName);
			jcdIndex.flushSession();
			expectedSet.clear();
			Set<JavaClassDependency> removedDependencyClassSet = jcdIndex.findDependencies(fooDependentGUID);
			Assert.assertThat(removedDependencyClassSet, Is.is(IsEqual.equalTo(expectedSet)));
			
			jcdIndex.closeSession();
		}
		
		@Test
		public void GivenIndexEmptyDependency_WhenValidInput_ThenNoChange()
		{
			LuceneJavaClassDependencyIndex jcdIndex = new LuceneJavaClassDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
			jcdIndex.openSession();
			
			Set<JavaClassDependency> dependencyClassSet = jcdIndex.findDependencies(fooDependentGUID);
			Set<JavaClassDependency> expectedSet = new HashSet<JavaClassDependency>();
			Assert.assertThat(dependencyClassSet, Is.is(IsEqual.equalTo(expectedSet)));
			
			jcdIndex.removeDependency(fooDependentGUID, fooPackage, dependencyClassName);
			jcdIndex.flushSession();
			Set<JavaClassDependency> removedDependencyClassSet = jcdIndex.findDependencies(fooDependentGUID);
			Assert.assertThat(removedDependencyClassSet, Is.is(IsEqual.equalTo(expectedSet)));
			
			jcdIndex.closeSession();
		}
		
		// =================================================
		// utility methods
		// =================================================
		@SuppressWarnings("unused")
		private static Object[] nullInputParemeters()
		{
			return new Object[][]{
				{null, fooPackage, dependencyClassName}
				, {fooDependentGUID, null, dependencyClassName}
				, {fooDependentGUID, fooPackage, null}
			};
		}
	}	
	

	@RunWith(JUnitParamsRunner.class)
	public static class removeDependenciesTest
	{
		@Test(expected=ValidationException.class)
		@Parameters(method="nullInputParemeters")
		public void GivenIndex_WhenRemoveDependenciesNullInput_ThenValidationExpection(HierarchicalPackage hierarchicalPackage, String className)
		{
			LuceneJavaClassDependencyIndex jcdIndex = new LuceneJavaClassDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
			jcdIndex.openSession();
			jcdIndex.removeDependencies(hierarchicalPackage, className);
			jcdIndex.closeSession();
		}
		
		@Test
		public void GivenIndexMultipleDependencies_WhenValidInput_ThenDependenciesRemoved()
		{
			LuceneJavaClassDependencyIndex jcdIndex = new LuceneJavaClassDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
			jcdIndex.openSession();
			jcdIndex.addDependency(fooDependentGUID, fooPackage, dependencyClassName);
			jcdIndex.addDependency(barDependentGUID, fooPackage, dependencyClassName);
			jcdIndex.flushSession();
			
			Set<GUID> dependentSet = jcdIndex.findDependents(fooPackage, dependencyClassName);
			Set<GUID> expectedSet = new HashSet<GUID>();
			expectedSet.add(fooDependentGUID);
			expectedSet.add(barDependentGUID);
			Assert.assertThat(dependentSet, Is.is(IsEqual.equalTo(expectedSet)));
			
			jcdIndex.removeDependencies(fooPackage, dependencyClassName);
			jcdIndex.flushSession();
			expectedSet.clear();
			Set<GUID> removedDependentSet = jcdIndex.findDependents(fooPackage, dependencyClassName);
			Assert.assertThat(removedDependentSet, Is.is(IsEqual.equalTo(expectedSet)));
			
			jcdIndex.closeSession();
		}
		
		// =================================================
		// utility methods
		// =================================================
		@SuppressWarnings("unused")
		private static Object[] nullInputParemeters()
		{
			return new Object[][]{
				{null, dependencyClassName}
				, {fooPackage, null}
			};
		}
	}	
	
	
	
	@RunWith(JUnitParamsRunner.class)
	public static class findDependenciesTest
	{

		@Test(expected=ValidationException.class)
		public void GivenIndex_WhenNullInput_ThenValidationExpection()
		{
			LuceneJavaClassDependencyIndex jcdIndex = new LuceneJavaClassDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
			jcdIndex.openSession();
			jcdIndex.findDependencies(null);
			jcdIndex.closeSession();
		}
		
		@Test
		public void GivenIndexMultipleDependencies_WhenValidInput_ThenDependencyExists()
		{
			LuceneJavaClassDependencyIndex jcdIndex = new LuceneJavaClassDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
			jcdIndex.openSession();
			jcdIndex.addDependency(fooDependentGUID, fooPackage, dependencyClassName);
			jcdIndex.addDependency(fooDependentGUID, bazPackage, anotherDependencyClassName);
			jcdIndex.flushSession();
			Set<JavaClassDependency> dependencyClassSet = jcdIndex.findDependencies(fooDependentGUID);
			
			Set<JavaClassDependency> expectedSet = new HashSet<JavaClassDependency>();
			expectedSet.add(new JavaClassDependency(fooDependentGUID, fooPackage, dependencyClassName));
			expectedSet.add(new JavaClassDependency(fooDependentGUID, bazPackage, anotherDependencyClassName));
		
			Assert.assertThat(dependencyClassSet, Is.is(IsEqual.equalTo(expectedSet)));
			
			jcdIndex.closeSession();
		}
		
		@Test
		public void GivenIndexEmptyDependencies_WhenValidInput_ThenEmptylResults()
		{
			LuceneJavaClassDependencyIndex jcdIndex = new LuceneJavaClassDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
			jcdIndex.openSession();
			Set<JavaClassDependency> dependencyClassSet = jcdIndex.findDependencies(fooDependentGUID);
			
			Set<JavaClassDependency> expectedSet = new HashSet<JavaClassDependency>();
//			expectedSet.add(new DependencyClass(fooPackage, dependencyClassName));
//			expectedSet.add(new DependencyClass(barPackage, anotherDependencyClassName));

			Assert.assertThat(dependencyClassSet, Is.is(IsEqual.equalTo(expectedSet)));
			
			jcdIndex.closeSession();
		}
		
	}
	
	
	@RunWith(JUnitParamsRunner.class)
	public static class findDependenciesPaginatedTest
	{
		@Test(expected=ValidationException.class)
		@Parameters(method="negInputParemeters")
		public void GivenIndex_WhenNegInput_ThenValidationExpection(int offset, int resultSize)
		{
			LuceneJavaClassDependencyIndex jcdIndex = new LuceneJavaClassDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
			jcdIndex.openSession();
			jcdIndex.findDependencies(offset, resultSize);
			jcdIndex.closeSession();
		}
		
		@Test
		public void GivenIndexMultipleDependencies_WhenValidInput_ThenDependencyExists()
		{
			LuceneJavaClassDependencyIndex jcdIndex = new LuceneJavaClassDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
			jcdIndex.openSession();
			
			int totalDependencySize = 20;
			int dependenciesPageSize = 10;
			
			for(int i=0; i<totalDependencySize; i++)
			{
				jcdIndex.addDependency(fooDependentGUID, fooPackage, dependencyClassName+i);
			}
			jcdIndex.flushSession();
			Set<JavaClassDependency> dependencyClassSet = jcdIndex.findDependencies(0, dependenciesPageSize);
			
			Assert.assertThat(dependencyClassSet.size(), Is.is(IsEqual.equalTo(dependenciesPageSize)));
			
			jcdIndex.closeSession();
		}
		
		@Test
		public void GivenIndexEmptyDependencies_WhenValidInput_ThenEmptylResults()
		{
			LuceneJavaClassDependencyIndex jcdIndex = new LuceneJavaClassDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
			jcdIndex.openSession();
			
			int dependenciesPageSize = 10;
			Set<JavaClassDependency> dependencyClassSet = jcdIndex.findDependencies(0, dependenciesPageSize);
			
			Set<JavaClassDependency> expectedSet = new HashSet<JavaClassDependency>();
		
			Assert.assertThat(dependencyClassSet, Is.is(IsEqual.equalTo(expectedSet)));
			
			jcdIndex.closeSession();
		}
		
		// =================================================
		// utility methods
		// =================================================
		@SuppressWarnings("unused")
		private static Object[] negInputParemeters()
		{

			return new Object[][]{
				{-1, 1}
				, {1, -1}
			};

		}

	}	
	
	@RunWith(JUnitParamsRunner.class)
	public static class findDependentsTest
	{
		@Test(expected=ValidationException.class)
		@Parameters(method="nullInputParemeters")
		public void GivenIndex_WhenNullInput_ThenValidationExpection(HierarchicalPackage hierarchicalPackage, String className)
		{
			LuceneJavaClassDependencyIndex jcdIndex = new LuceneJavaClassDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
			jcdIndex.openSession();
			jcdIndex.findDependents(hierarchicalPackage, className);
			jcdIndex.closeSession();
		}
		
		@Test
		public void GivenIndexMultipleDependents_WhenValidInput_ThenDependentsExists()
		{
			LuceneJavaClassDependencyIndex jcdIndex = new LuceneJavaClassDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
			jcdIndex.openSession();
			jcdIndex.addDependency(fooDependentGUID, fooPackage, dependencyClassName);
			jcdIndex.addDependency(barDependentGUID, fooPackage, dependencyClassName);
			jcdIndex.flushSession();
			Set<GUID> dependentGUIDSet = jcdIndex.findDependents(fooPackage, dependencyClassName);
			
			Set<GUID> expectedSet = new HashSet<GUID>();
			expectedSet.add(fooDependentGUID);
			expectedSet.add(barDependentGUID);
		
			Assert.assertThat(dependentGUIDSet, Is.is(IsEqual.equalTo(expectedSet)));
			
			jcdIndex.closeSession();
		}
		
		@Test
		public void GivenIndexSingleDependent_WhenValidInput_ThenDependentExists()
		{
			LuceneJavaClassDependencyIndex jcdIndex = new LuceneJavaClassDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
			jcdIndex.openSession();
			jcdIndex.addDependency(fooDependentGUID, fooPackage, dependencyClassName);
			jcdIndex.flushSession();
			Set<GUID> dependentGUIDSet = jcdIndex.findDependents(fooPackage, dependencyClassName);
			
			Set<GUID> expectedSet = new HashSet<GUID>();
			expectedSet.add(fooDependentGUID);
		
			Assert.assertThat(dependentGUIDSet, Is.is(IsEqual.equalTo(expectedSet)));
			
			jcdIndex.closeSession();
		}
		
		@Test
		public void GivenIndexNoDependents_WhenValidInput_ThenEmptyResults()
		{
			LuceneJavaClassDependencyIndex jcdIndex = new LuceneJavaClassDependencyIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
			jcdIndex.openSession();
			Set<GUID> dependentGUIDSet = jcdIndex.findDependents(fooPackage, dependencyClassName);
			
			Set<GUID> expectedSet = new HashSet<GUID>();
		
			Assert.assertThat(dependentGUIDSet, Is.is(IsEqual.equalTo(expectedSet)));
			
			jcdIndex.closeSession();
		}

		// =================================================
		// utility methods
		// =================================================
		@SuppressWarnings("unused")
		private static Object[] nullInputParemeters()
		{
			return new Object[][]{
				{null, dependencyClassName}
				, {fooPackage, null}
			};
		}

	}		
	
}
	
	
	

