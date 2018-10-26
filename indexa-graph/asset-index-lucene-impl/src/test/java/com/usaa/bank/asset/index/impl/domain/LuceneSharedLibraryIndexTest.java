package com.usaa.bank.asset.index.impl.domain;

import com.usaa.bank.asset.index.api.domain.asset.shared.library.ISharedLibrary;
import com.usaa.bank.asset.index.api.domain.asset.shared.library.SharedLibrary;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import com.usaa.bank.graph.lucene.LuceneIndexDAOLocalImpl;

import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LuceneSharedLibraryIndexTest {
	private LuceneSharedLibraryIndex artifactIndex = null;
	private String id = "id123";
	private String name = "name123";
	private String guid = id+ SharedLibrary.GUID_DELIMITER+name;

	@Before
	public void runOnceBeforeClass() { this.artifactIndex = new LuceneSharedLibraryIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory())); }

	@Test
	public void createSharedLibraryByIdAndNameTest() {
		this.artifactIndex.openSession();
		checkCorrectness(this.artifactIndex.create(id,name));
		this.artifactIndex.flushSession();
		checkCorrectness(this.artifactIndex.get(GUIDFactory.createGUID(guid)));
		this.artifactIndex.closeSession();
	}

	@Test
	public void removeSharedLibraryByIdAndNameTest() {
		this.artifactIndex.openSession();
		checkCorrectness(this.artifactIndex.create(id,name));
		this.artifactIndex.flushSession();
		this.artifactIndex.remove(id,name);
		this.artifactIndex.flushSession();
		assertNull(this.artifactIndex.get(GUIDFactory.createGUID(guid)));
		this.artifactIndex.closeSession();
	}

	@Test
	public void getSharedLibraryByIdAndNameTest() {
		this.artifactIndex.openSession();
		checkCorrectness(this.artifactIndex.create(id,name));
		this.artifactIndex.flushSession();
		checkCorrectness(this.artifactIndex.get(id,name));
		this.artifactIndex.closeSession();
	}

	@Test
	public void findSharedLibrariesByNameTest() {
		this.artifactIndex.openSession();
		checkCorrectness(this.artifactIndex.create(id,name));
		this.artifactIndex.flushSession();
		checkCorrectness(new ArrayList<>(this.artifactIndex.find(name)).get(0));
		this.artifactIndex.closeSession();
	}

	@Test
	public void createByFieldsTest() {
		this.artifactIndex.openSession();
		checkCorrectness(this.artifactIndex.create(this.createTags(),this.createFields()));
		this.artifactIndex.flushSession();
		checkCorrectness(this.artifactIndex.get(GUIDFactory.createGUID(guid)));
		this.artifactIndex.closeSession();
	}

	@Test
	public void saveBySharedLibTest() {
		this.artifactIndex.openSession();
		this.artifactIndex.save(new SharedLibrary(id, name));
		this.artifactIndex.flushSession();
		checkCorrectness(this.artifactIndex.get(GUIDFactory.createGUID(guid)));
		this.artifactIndex.closeSession();
	}

	@Test
	public void removeByGuidTest() {
		this.artifactIndex.openSession();
		checkCorrectness(this.artifactIndex.create(id,name));
		this.artifactIndex.flushSession();
		this.artifactIndex.remove(GUIDFactory.createGUID(guid));
		this.artifactIndex.flushSession();
		assertNull(this.artifactIndex.get(GUIDFactory.createGUID(guid)));
		this.artifactIndex.closeSession();
	}

	@Test
	public void findByKeyTest() {
		this.artifactIndex.openSession();
		checkCorrectness(this.artifactIndex.create(id,name));
		this.artifactIndex.flushSession();
		checkCorrectness(new ArrayList<>(this.artifactIndex.find(SharedLibrary.KEY_ID,id)).get(0));
		this.artifactIndex.closeSession();
	}

	@Test
	public void findByFieldsTest() {
		this.artifactIndex.openSession();
		checkCorrectness(this.artifactIndex.create(id,name));
		this.artifactIndex.flushSession();
		checkCorrectness(new ArrayList<>(this.artifactIndex.find(this.createFindFields())).get(0));
		this.artifactIndex.closeSession();
	}

	private void checkCorrectness(ISharedLibrary artifact) {
		assertEquals(String.format("%s:%s", id, name), String.format("%s:%s", artifact.getId(), artifact.getName()));
	}
	private Map<String, Set<String>> createFields() {
		Map<String, Set<String>> fields = new HashMap<>();
		Set<String> value = new HashSet<>();
		value.add(id);
		fields.put(SharedLibrary.KEY_ID,value);
		value = new HashSet<>();
		value.add(name);
		fields.put(SharedLibrary.KEY_NAME,value);
		return fields;
	}
	private Map<String, String> createFindFields() {
		Map<String, String> fields = new HashMap<>();
		fields.put(SharedLibrary.KEY_ID,id);
		fields.put(SharedLibrary.KEY_NAME,name);
		return fields;
	}
	private Set<String> createTags() {
		Set<String> tags = new HashSet<>();
		tags.add("tag123");
		return tags;
	}
}