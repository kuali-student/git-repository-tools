/*
 * Copyright 2014 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 1.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.kuali.student.git.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BoundedInputStream;
import org.apache.commons.io.input.CountingInputStream;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.util.io.CountingOutputStream;
import org.iq80.snappy.SnappyInputStream;
import org.iq80.snappy.SnappyOutputStream;
import org.kuali.student.git.utils.GitBranchUtils;
import org.kuali.student.git.utils.GitBranchUtils.ILargeBranchNameProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Kuali Student Team
 * 
 */
public class SvnRevisionMapper implements ILargeBranchNameProvider {

	private static final Logger log = LoggerFactory
			.getLogger(SvnRevisionMapper.class);

	private static final String REVISION_MAP_FILE_NAME = "revisions.map";

	private static final String REVISION_MAP_INDEX_FILE_NAME = "revisions.idx";

	public static class SvnRevisionMap {
		private long revision;
		private String branchName;
		private String branchPath;
		private String commitId;

		/**
		 * @param branchName
		 * @param commitId
		 */
		public SvnRevisionMap(long revision, String branchName,
				String branchPath, String commitId) {
			super();
			this.revision = revision;
			this.branchName = branchName;
			this.branchPath = branchPath;
			this.commitId = commitId;
		}

		/**
		 * @return the branchPath
		 */
		public String getBranchPath() {
			return branchPath;
		}

		/**
		 * @return the branchName
		 */
		public String getBranchName() {
			return branchName;
		}

		/**
		 * @return the commitId
		 */
		public String getCommitId() {
			return commitId;
		}

		/**
		 * @return the revision
		 */
		public long getRevision() {
			return revision;
		}

		/**
		 * @param revision
		 *            the revision to set
		 */
		public void setRevision(long revision) {
			this.revision = revision;
		}

	}

	private File revisonMappings;
	private Repository repo;

	private InputStream revisionMapInputStream;

	/**
	 * 
	 */
	public SvnRevisionMapper(Repository repo) {

		this.repo = repo;
		revisonMappings = new File(repo.getDirectory(), "jsvn");

		revisonMappings.mkdirs();

	}

	public void createRevisionMap(long revision, List<Ref> branchHeads)
			throws IOException {

		File actual = new File(revisonMappings, REVISION_MAP_FILE_NAME);

		File temporary = new File(revisonMappings, REVISION_MAP_FILE_NAME
				+ ".tmp");

		boolean firstRevision = false;

		if (!actual.exists())
			firstRevision = true;

		long revisionStartByteIndex = -1;

		CountingOutputStream revisionMappingStream = null;
		CountingInputStream countingInputStream = null;
		
		if (!firstRevision) {

			
			countingInputStream = new CountingInputStream(
					new SnappyInputStream(new FileInputStream(actual)));

			revisionMappingStream = new CountingOutputStream(
					new SnappyOutputStream(new FileOutputStream(temporary)));

			long copied = IOUtils.copyLarge(countingInputStream,
					revisionMappingStream);

			revisionStartByteIndex = copied;

		} else {
			// first revision
			revisionStartByteIndex = 0L;
			revisionMappingStream = new CountingOutputStream(
					new SnappyOutputStream(new FileOutputStream(actual)));
		}

		PrintWriter pw = new PrintWriter(revisionMappingStream);

		for (Ref branchHead : branchHeads) {
			/*
			 * Only archive active branches. skip those containing @
			 */
			if (!branchHead.getName().contains("\\@"))
				pw.println(revision + "::" + branchHead.getName() + "::"
						+ branchHead.getObjectId().name());
		}

		pw.flush();
		pw.close();

		if (!firstRevision) {
			
			countingInputStream.close();
			
			try {
				Files.delete(actual.toPath());
				
				FileUtils.moveFile(temporary, actual);
			} catch (Exception e) {
				log.warn("", e);
			}
		}

		/*
		 * Write the number of bytes written for this revision.
		 */
		pw = new PrintWriter(new FileOutputStream(new File(revisonMappings,
				REVISION_MAP_INDEX_FILE_NAME), true));

		long totalBytesWritten = revisionMappingStream.getCount()
				- revisionStartByteIndex;

		pw.println(revision + "::" + revisionStartByteIndex + "::"
				+ totalBytesWritten);

		pw.close();
	}

	/**
	 * Get the list of all references at the svn revision number given.
	 * 
	 * @param revision
	 * @return
	 * @throws IOException
	 */
	public List<SvnRevisionMap> getRevisionHeads(long revision)
			throws IOException {

		InputStream inputStream = getRevisionInputStream(revision);

		List<String> lines = IOUtils.readLines(inputStream, "UTF-8");
		
		inputStream.close();
		
		revisionMapInputStream.close();
		
		String revisionString = String.valueOf(revision);

		List<SvnRevisionMap> revisionHeads = new ArrayList<SvnRevisionMap>();

		for (String line : lines) {

			String[] parts = line.split("::");
			
			if (!parts[0].equals(revisionString)) {
				log.warn(parts[0] + " is not a line for " + revisionString);
				continue;
			}

			String branchName = parts[1];

			String commitId = parts[2];

			String branchPath = GitBranchUtils.getBranchPath(branchName,
					revision, this);

			String filteredPath = branchPath.replaceAll("@[0-9]+$", "");

			revisionHeads.add(new SvnRevisionMap(revision, branchName,
					filteredPath, commitId));

		}

		return revisionHeads;

	}

	private InputStream getRevisionInputStream(long revision)
			throws IOException {

		BufferedReader indexReader = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(revisonMappings,
						REVISION_MAP_INDEX_FILE_NAME))));

		String revString = String.valueOf(revision);

		long byteStartOffset = -1;
		long totalbytes = -1;

		while (true) {

			String line = indexReader.readLine();

			if (line == null)
				break;

			if (line.startsWith(revString)) {

				String parts[] = line.split("::");

				if (parts.length != 3)
					continue;

				if (parts[0].trim().equals(revString)) {
					byteStartOffset = Long.parseLong(parts[1]);
					totalbytes = Long.parseLong(parts[2]);
					break;
				}

			}

		}

		indexReader.close();

		if (byteStartOffset == -1)
			return null;

		File actual = new File(revisonMappings, REVISION_MAP_FILE_NAME);

		revisionMapInputStream = new SnappyInputStream(
				new FileInputStream(actual));

		revisionMapInputStream.skip(byteStartOffset);

		return new BoundedInputStream(revisionMapInputStream, totalbytes);

	}

	/**
	 * Get the object id of the commit refered to by the branch at the revision
	 * given.
	 * 
	 * @param revision
	 * @param branchName
	 * @return
	 * @throws IOException
	 */
	public ObjectId getRevisionBranchHead(long revision, String branchName)
			throws IOException {

		InputStream inputStream = getRevisionInputStream(revision);

		List<String> lines = IOUtils.readLines(inputStream, "UTF-8");
		
		inputStream.close();
		
		revisionMapInputStream.close();

		String revisionString = String.valueOf(revision);
		
		for (String line : lines) {

			String[] parts = line.split("::");

			if (!parts[0].equals(revisionString)) {
				log.warn("incorrect version");
				continue;
			}
			
			if (parts[1].equals(Constants.R_HEADS + branchName)) {
				ObjectId id = ObjectId.fromString(parts[2]);

				return id;

			}

		}

		// this is actually an exceptional case
		// if not found it means that the reference can't be found.
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kuali.student.git.utils.GitBranchUtils.ILargeBranchNameProvider#
	 * getBranchName(java.lang.String, long)
	 */
	@Override
	public String getBranchName(String longBranchId, long revision) {

		try {
			File revisionFile = new File(revisonMappings, "r" + revision
					+ "-large-branches");

			List<String> lines = FileUtils.readLines(revisionFile, "UTF-8");

			for (String line : lines) {

				String[] parts = line.split("::");

				if (parts.length != 2) {
					continue;
				}

				if (parts[0].equals(longBranchId)) {
					return parts[1].trim();
				}

			}

			// not found
			return null;
		} catch (IOException e) {
			log.warn("failed to find longbranch for id = " + longBranchId);
			return null;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kuali.student.git.utils.GitBranchUtils.ILargeBranchNameProvider#
	 * storeLargeBranchName(java.lang.String, java.lang.String, long)
	 */
	@Override
	public String storeLargeBranchName(String branchName, long revision) {

		try {
			ObjectId largeBranchNameId = GitBranchUtils
					.getBranchNameObjectId(branchName);

			String existingBranchName = getBranchName(largeBranchNameId.name(),
					revision);

			if (existingBranchName != null)
				return largeBranchNameId.getName();

			File revisionFile = new File(revisonMappings, "r" + revision
					+ "-large-branches");

			PrintWriter pw = new PrintWriter(new FileOutputStream(revisionFile,
					true));

			pw.println(largeBranchNameId.name() + "::" + branchName);

			pw.flush();
			pw.close();

			return largeBranchNameId.name();
		} catch (FileNotFoundException e) {
			log.warn("storeLargeBranchName: failed to open r" + revision
					+ "-large-branches");
			return null;
		}
	}

}
