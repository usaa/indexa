package com.usaa.bank.graph.common.hierarchy;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.usaa.bank.graph.common.validate.ValidationException;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(Enclosed.class)
public class Test_DirectedGraph
{
/*

	@RunWith(JUnitParamsRunner.class)
	public static class AddVertexTest
	{
		@Test(expected=ValidationException.class)
		public void GivenDirectedGraph_WhenFromNullInput_ThenValidationExpection()
		{
			DirectedGraph<String> directedGraph = new DirectedGraph<String>();
			directedGraph.addVertex(null, "b");
		}
		
		@Test
		public void GivenDirectedGraph_WhenNullTo_ThenNodeExist()
		{
			DirectedGraph<String> directedGraph = new DirectedGraph<String>();
			directedGraph.addVertex("a", null);

			Assert.assertThat(directedGraph.getNodes().size(), Is.is(IsEqual.equalTo(1)));
		}
		
		@Test
		public void GivenDirectedGraph_WhenNewChild_ThenNodeAndVertexExist()
		{
			DirectedGraph<String> directedGraph = new DirectedGraph<String>();
			directedGraph.addVertex("a", "b");

			Assert.assertThat(directedGraph.getNodes().size(), Is.is(IsEqual.equalTo(2)));

			Assert.assertTrue(directedGraph.getAdjacencyFrom("a").contains("b"));
			Assert.assertTrue(directedGraph.getAdjacencyTo("b").contains("a"));
		}
		
		@Test
		public void GivenDirectedGraph_WhenExistingChild_ThenNodeAndVertexExist()
		{
			DirectedGraph<String> directedGraph = new DirectedGraph<String>();
			directedGraph.addVertex("a", "b");
			directedGraph.addVertex("b", "c");

			directedGraph.addVertex("a", "c");

			Assert.assertThat(directedGraph.getNodes().size(), Is.is(IsEqual.equalTo(3)));

			Assert.assertTrue(directedGraph.getAdjacencyFrom("a").contains("c"));
			Assert.assertTrue(directedGraph.getAdjacencyTo("c").contains("a"));
		}

	}
	

	
	@RunWith(JUnitParamsRunner.class)
	public static class RemoveVertexTest
	{
		@Test(expected=ValidationException.class)
		@Parameters(method="nullInputParemeters")
		public void GivenDirectedGraph_WhenNullInput_ThenValidationExpection(String from, String to)
		{
			DirectedGraph<String> directedGraph = new DirectedGraph<String>();
			directedGraph.removeVertex(from, to);
		}

		@Test
		public void GivenDirectedGraphWithChild_WhenRemoveValidChild_ThenValidNodeVertex()
		{
			DirectedGraph<String> directedGraph = new DirectedGraph<String>();
			directedGraph.addVertex("a", "b");
			directedGraph.removeVertex("a", "b");

			Assert.assertThat(directedGraph.getNodes().size(), Is.is(IsEqual.equalTo(2)));

			Assert.assertThat(directedGraph.getAdjacencyFrom("a").size(), Is.is(IsEqual.equalTo(0)));
			Assert.assertThat(directedGraph.getAdjacencyTo("b").size(), Is.is(IsEqual.equalTo(0)));
		}		
		
		
		@Test
		public void GivenDirectedGraphWithGrandChild_WhenRemoveChild_ThenValidNodeVertex()
		{
			DirectedGraph<String> directedGraph = new DirectedGraph<String>();
			directedGraph.addVertex("a", "b");
			directedGraph.addVertex("b", "c");
			directedGraph.removeVertex("a", "b");

			Assert.assertThat(directedGraph.getNodes().size(), Is.is(IsEqual.equalTo(3)));

			Assert.assertThat(directedGraph.getAdjacencyFrom("a").size(), Is.is(IsEqual.equalTo(0)));
			Assert.assertThat(directedGraph.getAdjacencyTo("b").size(), Is.is(IsEqual.equalTo(0)));

			Assert.assertThat(directedGraph.getAdjacencyFrom("b").size(), Is.is(IsEqual.equalTo(1)));
			Assert.assertThat(directedGraph.getAdjacencyTo("c").size(), Is.is(IsEqual.equalTo(1)));
		}		
		
		
		@Test
		public void GivenDirectedGraph_WhenRemoveWithNonExistentTo_ThenNoChange()
		{
			DirectedGraph<String> directedGraph = new DirectedGraph<String>();
			directedGraph.addVertex("a", "b");
			directedGraph.addVertex("b", "c");
			directedGraph.removeVertex("a", "d");

			Assert.assertThat(directedGraph.getNodes().size(), Is.is(IsEqual.equalTo(3)));
			Assert.assertThat(directedGraph.getAdjacencyFrom("a").size(), Is.is(IsEqual.equalTo(1)));
			Assert.assertThat(directedGraph.getAdjacencyTo("b").size(), Is.is(IsEqual.equalTo(1)));
		}		

		@Test
		public void GivenDirectedGraph_WhenRemoveWithNonExistentFrom_ThenNoChange()
		{
			DirectedGraph<String> directedGraph = new DirectedGraph<String>();
			directedGraph.addVertex("a", "b");
			directedGraph.addVertex("b", "c");
			directedGraph.removeVertex("d", "a");
			Assert.assertThat(directedGraph.getNodes().size(), Is.is(IsEqual.equalTo(3)));
			Assert.assertThat(directedGraph.getAdjacencyFrom("a").size(), Is.is(IsEqual.equalTo(1)));
			Assert.assertThat(directedGraph.getAdjacencyTo("b").size(), Is.is(IsEqual.equalTo(1)));
		}		
		
		@SuppressWarnings("unused")
		private static Object[] nullInputParemeters()
		{
			return new Object[][]{
					{"a", null}
					,{null, "b"}
					,{null, null}
			};
		}
	}
	
	
	@RunWith(JUnitParamsRunner.class)
	public static class RemoveNodeTest
	{
		@Test(expected=ValidationException.class)
		public void GivenDirectedGraph_WhenNullInput_ThenValidationExpection()
		{
			DirectedGraph<String> directedGraph = new DirectedGraph<String>();
			directedGraph.removeNode(null);
		}

		@Test
		public void GivenDirectedGraphGrandChild_WhenRemoveGrandChild_ThenValidNodeVertex()
		{
			DirectedGraph<String> directedGraph = new DirectedGraph<String>();
			directedGraph.addVertex("a", "b");
			directedGraph.addVertex("b", "c");
			directedGraph.removeNode("c");

			Assert.assertThat(directedGraph.getNodes().size(), Is.is(IsEqual.equalTo(2)));
			Assert.assertTrue(!directedGraph.getNodes().contains("c"));

			Assert.assertThat(directedGraph.getAdjacencyFrom("b").size(), Is.is(IsEqual.equalTo(0)));
		}		
		
		@Test
		public void GivenDirectedGraphGrandChild_WhenRemoveParent_ThenValidNodeVertex()
		{
			DirectedGraph<String> directedGraph = new DirectedGraph<String>();
			directedGraph.addVertex("a", "b");
			directedGraph.addVertex("b", "c");
			directedGraph.removeNode("b");

			Assert.assertThat(directedGraph.getNodes().size(), Is.is(IsEqual.equalTo(2)));
			Assert.assertTrue(!directedGraph.getNodes().contains("b"));

			Assert.assertThat(directedGraph.getAdjacencyFrom("a").size(), Is.is(IsEqual.equalTo(0)));
			Assert.assertThat(directedGraph.getAdjacencyTo("c").size(), Is.is(IsEqual.equalTo(0)));
		}		
		
		@Test
		public void GivenDirectedGraph_WhenRemoveDisconnectedNode_ThenVerticesUnchanged()
		{
			DirectedGraph<String> directedGraph = new DirectedGraph<String>();
			directedGraph.addVertex("a", "b");
			directedGraph.addVertex("b", "c");
			directedGraph.removeVertex("a", "b");
			directedGraph.removeNode("a");

			Assert.assertThat(directedGraph.getNodes().size(), Is.is(IsEqual.equalTo(2)));
			Assert.assertTrue(!directedGraph.getNodes().contains("a"));

			Assert.assertThat(directedGraph.getAdjacencyFrom("b").size(), Is.is(IsEqual.equalTo(1)));
			Assert.assertThat(directedGraph.getAdjacencyTo("c").size(), Is.is(IsEqual.equalTo(1)));
		}
		
	}	
*/

}