/**
 * 
 */
package org.kuali.student.git.model.tree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TreeFormatter;
import org.kuali.student.git.model.GitRepositoryUtils;
import org.kuali.student.git.model.tree.GitTreeData.GitTreeDataVisitor;
import org.kuali.student.git.model.tree.utils.GitTreeProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ocleirig
 * 
 */
public class GitTreeNodeData {

	private static final Logger log = LoggerFactory
			.getLogger(BlobResourceContext.class);

	private String name;

	protected Map<String, ObjectId> blobReferences = new HashMap<String, ObjectId>();

	protected Map<String, GitTreeNodeData> subTreeReferences = new HashMap<String, GitTreeNodeData>();

	protected ObjectId originalTreeObjectId;

	// set to true when this node has been changed
	// or a child that crosses this nodes path has been changed.
	private boolean dirty = false;

	// set to true when this nodes subtrees have been loaded
	private boolean initialized = false;

	private GitTreeNodeInitializer nodeInitializer;

	/**
	 * @param string
	 * 
	 */
	public GitTreeNodeData(GitTreeNodeInitializer nodeInitializer, String name) {
		super();
		this.nodeInitializer = nodeInitializer;
		this.name = name;
	}
	
	

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}



	public boolean deletePath(String path) {

		// remove all the blobs contained in the path given.

		String[] parts = path.split("/");

		return deletePath(parts, 0);
	}

	/**
	 * @return the originalTreeObjectId
	 */
	public ObjectId getOriginalTreeObjectId() {
		return originalTreeObjectId;
	}

	private void initializeSubTrees() {

		if (!this.initialized) {

			this.initialized = true;

			try {
				this.nodeInitializer.initialize(this);
			} catch (Exception e) {
				log.error("failed to initialize node: " + this.name, e);
			}

		}
	}

	/**
	 * @return the subtreeInitialized
	 */
	public boolean isInitialized() {
		return initialized;
	}

	private boolean deletePath(String[] parts, int partOffset) {

		int difference = (parts.length - partOffset);

		if (difference == 0) {
			// should never occur
			throw new RuntimeException(
					"should not delete all branch content should be deleting the branch");
		}

		String name = parts[partOffset];

		if (difference == 1) {

			if (!this.isInitialized()) {
				try {
					nodeInitializer.initialize(this);
				} catch (Exception e) {
					throw new RuntimeException("nodeInitializer Failed", e);
				}
			}

			boolean blobReference = false;
			boolean treeReference = false;

			if (blobReferences.containsKey(name)) {
				blobReferences.remove(name);
				blobReference = true;
				setDirty(true);
			} else if (subTreeReferences.containsKey(name)) {
				subTreeReferences.remove(name);
				treeReference = true;
				setDirty(true);
			}

			if (blobReference == false && treeReference == false) {
				String lastPart = parts[parts.length - 1];

				if (lastPart.contains("\\."))
					log.warn("failed to delete any tree or blob for " + name);
			}

			if (blobReference || treeReference)
				return true;
			else
				return false;
		} else {
			// > 1
			// need to get down a level.
			GitTreeNodeData leaf = subTreeReferences.get(name);

			if (leaf == null) {
				String lastPart = parts[parts.length - 1];

				if (lastPart.contains("\\."))
					log.warn("missing leaf blob = " + name);
			} else {

				if (!leaf.isInitialized()) {
					try {
						nodeInitializer.initialize(leaf);
					} catch (Exception e) {
						throw new RuntimeException("nodeInitializer Failed", e);
					}
				}
				/*
				 * bubble up the dirty flag to the root along the deleted path.
				 */
				if (leaf.deletePath(parts, partOffset + 1)) {
					setDirty(true);

					return true;
				} else
					return false;
			}
		}

		return false;

	}

	/**
	 * @return the dirty
	 */
	public boolean isDirty() {

		return dirty;

	}

	/**
	 * @param dirty
	 *            the dirty to set
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public boolean addBlob(String filePath, ObjectId blobId)
			throws MissingObjectException, IncorrectObjectTypeException,
			IOException {
		return addResource(filePath, new BlobResourceContext(blobId));

	}

	private boolean addResource(String filePath, ResourceContext context)
			throws MissingObjectException, IncorrectObjectTypeException,
			IOException {

		String[] parts = filePath.split("\\/");

		if (parts.length > 0) {
			return this.addResource(parts, 0, context);
		} else {
			log.info("failed to add " + context.getErrorMessage());
			return false;
		}

	}

	private boolean addResource(String[] parts, int partOffset,
			ResourceContext context) throws MissingObjectException,
			IncorrectObjectTypeException, IOException {

		int difference = (parts.length - partOffset);

		if (difference == 0) {
			// should never occur
			throw new RuntimeException("too deep");
		}

		String name = parts[partOffset];

		if (name.isEmpty()) {
			log.info("name is empty at partOffset= " + partOffset
					+ context.getErrorMessage());

		}

		if (difference == 1) {

			if (!this.isInitialized()) {
				try {
					nodeInitializer.initialize(this);
				} catch (Exception e) {
					throw new RuntimeException("nodeInitializer Failed", e);
				}
			}

			context.storeResource(name, this);

			this.setDirty(true);

			return true;
		} else {
			// > 1
			// need to get down a level.
			GitTreeNodeData leaf = subTreeReferences.get(name);

			if (leaf == null) {
				leaf = new GitTreeNodeData(nodeInitializer, name);
				leaf.setInitialized(true);
				subTreeReferences.put(name, leaf);
			} else {

				if (!leaf.isInitialized()) {
					try {
						nodeInitializer.initialize(leaf);
					} catch (Exception e) {
						throw new RuntimeException("nodeInitializer Failed", e);
					}
				}
			}
			
			if (leaf.addResource(parts, partOffset + 1, context)) {
				setDirty(true);
				return true;
			} else
				return false;
		}
	}

	public ObjectId buildTree(ObjectInserter inserter) throws IOException {

		log.debug("buildTree: starting");

		if (!isDirty() && originalTreeObjectId != null) {

			return originalTreeObjectId;
		}

		// else we need to recompute the tree at this level.

		TreeFormatter tree = new TreeFormatter();

		List<JGitTreeData> treeDataList = new ArrayList<JGitTreeData>();

		for (Map.Entry<String, ObjectId> entry : this.blobReferences.entrySet()) {
			String name = entry.getKey();
			ObjectId sha1 = entry.getValue();

			treeDataList
					.add(new JGitTreeData(name, FileMode.REGULAR_FILE, sha1));

			log.debug(String
					.format("added entry (name=%s, sha1=%s", name, sha1));

		}

		for (Map.Entry<String, GitTreeNodeData> entry : this.subTreeReferences
				.entrySet()) {

			String name = entry.getKey();
			GitTreeNodeData nodeData = entry.getValue();

			ObjectId subTreeId = nodeData.buildTree(inserter);

			treeDataList.add(new JGitTreeData(name, FileMode.TREE, subTreeId));

			log.debug(String.format("added tree (name=%s, sha1=%s", name,
					subTreeId));
		}

		/*
		 * Compare the string sort vs byte sort
		 */

		Collections.sort(treeDataList, JGitTreeData.GIT_SORT_ORDERING);

		for (JGitTreeData treeData : treeDataList) {

			tree.append(treeData.getName(), treeData.getFileMode(),
					treeData.getObjectId());
		}

		log.debug("buildTree: finished");

		return inserter.insert(tree);
	}

	public void visit(GitTreeDataVisitor vistor) {

		for (Map.Entry<String, ObjectId> blobEntry : blobReferences.entrySet()) {
			String name = blobEntry.getKey();
			ObjectId objectId = blobEntry.getValue();

			vistor.visitBlob(name, objectId);
		}

		for (Map.Entry<String, GitTreeNodeData> treeEntry : subTreeReferences
				.entrySet()) {
			GitTreeNodeData subTree = treeEntry.getValue();

			subTree.visit(vistor);
		}
	}

	/**
	 * In some initialization cases we want the node to know its been
	 * initialized properly.
	 * 
	 * @param id
	 * @param initialized
	 */
	public void setGitTreeObjectId(ObjectId id) {
		this.originalTreeObjectId = id;
	}

	public void resetDirtyFlag() {

		// can only not be dirty if there is an original
		// always dirty if there is no original
		if (this.originalTreeObjectId != null)
			this.setDirty(false);

		for (GitTreeNodeData subTreeData : this.subTreeReferences.values()) {

			subTreeData.resetDirtyFlag();
		}
	}

	public boolean addTree(GitTreeProcessor treeProcessor, String path,
			ObjectId treeId) throws MissingObjectException,
			IncorrectObjectTypeException, IOException {

		return addResource(path, new TreeResourceContext(treeProcessor, treeId));

	}

	/**
	 * Find the object id for the path as a string.
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws CorruptObjectException
	 * @throws IncorrectObjectTypeException
	 * @throws MissingObjectException
	 */
	public ObjectId find(Repository repo, String path)
			throws MissingObjectException, IncorrectObjectTypeException,
			CorruptObjectException, IOException {

		String parts[] = path.split("/");

		GitTreeNodeData currentNode = this;

		int uptoFile = parts.length - 1;

		for (int i = 0; i < uptoFile; i++) {

			String name = parts[i];

			if (!currentNode.isInitialized()) {
				// check the path using git through the objectid
				ObjectId treeId = currentNode.getOriginalTreeObjectId();

				if (treeId != null) {

					return GitRepositoryUtils.findInTree(repo, treeId,
							StringUtils.join(parts, "/", i, parts.length));

				}

			} else {
				GitTreeNodeData nextNode = currentNode.subTreeReferences
						.get(name);

				if (nextNode == null) {
					return null;
				} else
					currentNode = nextNode;
			}

		}

		String filePart = parts[uptoFile];

		ObjectId blobId = currentNode.blobReferences.get(filePart);

		if (blobId == null) {
			// check if it is a name of a tree

			GitTreeNodeData subTree = currentNode.subTreeReferences
					.get(filePart);

			if (subTree != null) {
				return subTree.getOriginalTreeObjectId();
			} else
				return null;
		} else {
			return blobId;
		}

	}

	/**
	 * Replace ourself with the loaded node
	 * 
	 * @param loadedNode
	 */
	public void replaceWith(GitTreeNodeData loadedNode) {

		if (!initialized) {

			this.blobReferences.putAll(loadedNode.blobReferences);

			this.subTreeReferences.putAll(loadedNode.subTreeReferences);

			this.initialized = true;

			this.dirty = false;

		} else {
			log.warn("replacing an already initialized node! name=" + loadedNode.name);
		}
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public GitTreeNodeData addDirectTree(String entryName, GitTreeNodeData subTree) {

		return this.subTreeReferences.put(entryName, subTree);
	}

	public ObjectId addDirectBlob(String entryName, ObjectId objectId) {
		return this.blobReferences.put(entryName, objectId);
	}



	/**
	 * Merge the content from the given node into ourself.
	 * 
	 * If there is a collision then the merge will fail.
	 * 
	 * @param node
	 */
	public boolean merge(GitTreeNodeData node) {
		
		boolean mergedSomething = false;
		
		for (Entry<String, ObjectId> entry : node.blobReferences.entrySet()) {
			
			String nodeBlobName = entry.getKey();
			
			ObjectId nodeBlobId = entry.getValue();
			
			ObjectId ourBlobId = this.blobReferences.get(nodeBlobName);
			
			if (ourBlobId == null) {
				// we don't have this blob
				this.blobReferences.put(nodeBlobName, nodeBlobId);
				mergedSomething = true;
			}
			else if (!ourBlobId.equals(nodeBlobId)) {
				log.warn(String.format("skipping merge due to blob collision merge (name=%s, ourBlobId=%s, nodeBlobId=%s)", nodeBlobName, ourBlobId, nodeBlobId));
			}
			
		}
		
		for (Entry<String, GitTreeNodeData> entry : node.subTreeReferences.entrySet()) {
			
			GitTreeNodeData nodeSubTree = entry.getValue();
			
			GitTreeNodeData ourSubTree = this.subTreeReferences.get(nodeSubTree.getName());
			
			if (ourSubTree == null) {
				// we don't have this tree
				this.subTreeReferences.put(nodeSubTree.getName(), nodeSubTree);
				mergedSomething = true;
			}
			else {
				if (ourSubTree.merge(nodeSubTree)) {
					mergedSomething = true;
				}
			}
			
			
		}
		
		if (mergedSomething)
			setDirty(true);
		
		return mergedSomething;
		
		
	}

}