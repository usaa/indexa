package com.usaa.bank.asset.index.impl.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.usaa.bank.asset.index.api.domain.asset.DigitalAsset;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.HierarchicalPackage;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.IJavaClass;
import com.usaa.bank.asset.index.api.domain.asset.java.classes.JavaClass;
import com.usaa.bank.graph.common.identity.GUID;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.Test;

import com.usaa.bank.asset.index.impl.util.DocumentBuilder;
import com.usaa.bank.asset.index.impl.util.JavaClassBuilder;
import com.usaa.bank.graph.common.identity.GUIDFactory;
import com.usaa.bank.graph.lucene.LuceneIndexDAOLocalImpl;

public class LuceneClassIndexTest {
	private LuceneJavaClassIndex artifactIndex = null;
	private String hash = "hash1234";
	private String className = "class1234";
	private int major = 1;
	private int minor = 2;
	private long date = 1234567890;
	private String artifactPath = "www/bank/1234";
	private GUID guid = GUIDFactory.createGUID(hash+ JavaClass.GUID_DELIMITER+artifactPath+JavaClass.GUID_DELIMITER+className);
	private HierarchicalPackage classPackage = new HierarchicalPackage(artifactPath);
	private Set<String> tags = new HashSet<>();

	@Before
	public void runOnceBeforeClass() {
		this.artifactIndex = new LuceneJavaClassIndex(new LuceneIndexDAOLocalImpl(new RAMDirectory()));
	}

	@Test
	public void createByFieldsTest() {
		this.artifactIndex.openSession();
		checkCorrectness(this.artifactIndex.create(this.createTags(),this.createFields()));
		this.artifactIndex.flushSession();
		checkCorrectness(this.artifactIndex.get(guid));
		this.artifactIndex.closeSession();
	}

	@Test
	public void saveByJavaClassTest() {
		this.artifactIndex.openSession();
		this.artifactIndex.save(JavaClassBuilder.createJavaClass(DocumentBuilder.createDocument(guid.getStringValue(),this.createFields())));
		this.artifactIndex.flushSession();
		checkCorrectness(this.artifactIndex.get(guid));
		this.artifactIndex.closeSession();
	}

	@Test
	public void createByArgsTest() {
		this.artifactIndex.openSession();
		checkCorrectness(this.artifactIndex.create(hash, classPackage, className, date, date, major, minor));
		this.artifactIndex.flushSession();
		checkCorrectness(this.artifactIndex.get(guid));
		this.artifactIndex.closeSession();

	}

	@Test
	public void getByArgsTest() {
		this.artifactIndex.openSession();
		checkCorrectness(this.artifactIndex.create(hash, classPackage, className, date, date, major, minor));
		this.artifactIndex.flushSession();
		checkCorrectness(this.artifactIndex.get(hash, classPackage, className));
		this.artifactIndex.closeSession();

	}

	@Test
	public void removeByArgsTest() {
		this.artifactIndex.openSession();
		checkCorrectness(this.artifactIndex.create(hash, classPackage, className, date, date, major, minor));
		this.artifactIndex.flushSession();
		this.artifactIndex.remove(hash, classPackage, className);
		this.artifactIndex.flushSession();
		assertNull(this.artifactIndex.get(hash, classPackage, className));
		this.artifactIndex.closeSession();

	}

	@Test
	public void removeByGuidTest() {
		this.artifactIndex.openSession();
		checkCorrectness(this.artifactIndex.create(this.createTags(),this.createFields()));
		this.artifactIndex.flushSession();
		this.artifactIndex.remove(guid);
		this.artifactIndex.flushSession();
		assertNull(this.artifactIndex.get(guid));
		this.artifactIndex.closeSession();
	}

	@Test
	public void removeJavaClassTest() {
		this.artifactIndex.openSession();
		checkCorrectness(this.artifactIndex.create(this.createTags(),this.createFields()));
		this.artifactIndex.flushSession();
		this.artifactIndex.remove(hash,classPackage,className);
		this.artifactIndex.flushSession();
		assertNull(this.artifactIndex.get(hash,classPackage,className));
		this.artifactIndex.closeSession();
	}

	@Test
	public void findJavaClassesByHashTest() {
		this.artifactIndex.openSession();
		checkCorrectness(this.artifactIndex.create(this.createTags(),this.createFields()));
		this.artifactIndex.flushSession();
		checkCorrectness(new ArrayList<>(this.artifactIndex.find(hash)).get(0));
		this.artifactIndex.closeSession();
	}

	@Test
	public void findJavaClassesField() {
		this.artifactIndex.openSession();
		checkCorrectness(this.artifactIndex.create(this.createTags(),this.createFields()));
		this.artifactIndex.flushSession();
		checkCorrectness(new ArrayList<>(this.artifactIndex.find(DigitalAsset.KEY_GUID, guid.getStringValue())).get(0));
		this.artifactIndex.closeSession();
	}

	@Test
	public void findJavaClassesByFields() {
		Map<String, String> findFields = new HashMap<>();
		findFields.put(DigitalAsset.KEY_GUID, guid.getStringValue());
		this.artifactIndex.openSession();
		checkCorrectness(this.artifactIndex.create(this.createTags(),this.createFields()));
		this.artifactIndex.flushSession();
		checkCorrectness(new ArrayList<>(this.artifactIndex.find(findFields)).get(0));
		this.artifactIndex.closeSession();
	}

	@Test
	public void findJavaClassesByPackageAndNameTest() {
		this.artifactIndex.openSession();
		checkCorrectness(this.artifactIndex.create(this.createTags(),this.createFields()));
		this.artifactIndex.flushSession();
		checkCorrectness(new ArrayList<>(this.artifactIndex.find(classPackage,className)).get(0));
		this.artifactIndex.closeSession();
	}

	@Test
	public void findJavaClassesByPackageTest() {
		this.artifactIndex.openSession();
		checkCorrectness(this.artifactIndex.create(this.createTags(),this.createFields()));
		this.artifactIndex.flushSession();
		checkCorrectness(new ArrayList<>(this.artifactIndex.find(classPackage)).get(0));
		this.artifactIndex.closeSession();
	}

	// ================================================
	// Utility Methods
	// ================================================
	private void checkCorrectness(IJavaClass artifact)
	{
		assertEquals(
				String.format("%s:%s:%s:%s:%s:%s:%s",
						hash, classPackage.toString(), className, String.valueOf(date), String.valueOf(date),
						String.valueOf(major), String.valueOf(minor)),
				String.format("%s:%s:%s:%s:%s:%s:%s",
						artifact.getArtifactHash(), artifact.getPackage().toString(), artifact.getClassName(),
						String.valueOf(artifact.getCreatedDate()), String.valueOf(artifact.getModifiedDate()),
						String.valueOf(artifact.getMajorVersion()), String.valueOf(artifact.getMinorVersion())));
	}
	private Map<String, Set<String>> createFields() {
		Map<String, Set<String>> fields = new HashMap<>();
		Set<String> value = new HashSet<>();
		value.add(hash);
		fields.put(JavaClass.KEY_ARTIFACT_HASH,value);
		value = new HashSet<>();
		value.add(artifactPath);
		fields.put(JavaClass.KEY_PACKAGE,value);
		value = new HashSet<>();
		value.add(className);
		fields.put(JavaClass.KEY_CLASS_NAME,value);
		value = new HashSet<>();
		value.add(String.valueOf(date));
		fields.put(JavaClass.KEY_CREATED_DATE,value);
		value = new HashSet<>();
		value.add(String.valueOf(date));
		fields.put(JavaClass.KEY_MODIFIED_DATE,value);
		value = new HashSet<>();
		value.add(String.valueOf(major));
		fields.put(JavaClass.KEY_MAJOR_VERSION,value);
		value = new HashSet<>();
		value.add(String.valueOf(minor));
		fields.put(JavaClass.KEY_MINOR_VERSION,value);
		return fields;
	}
	private Set<String> createTags() {
		Set<String> tags = new HashSet<>();
		tags.add("tag123");
		return tags;
	}
}