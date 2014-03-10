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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
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
	
	private static class RevisionMapOffset {
		private long revision;
		private long startBtyeOffset;
		private long totalBytes;
		/**
		 * @param revision
		 * @param startBtyeOffset
		 * @param totalBytes
		 */
		public RevisionMapOffset(long revision, long startBtyeOffset,
				long totalBytes) {
			super();
			this.revision = revision;
			this.startBtyeOffset = startBtyeOffset;
			this.totalBytes = totalBytes;
		}
		/**
		 * @return the revision
		 */
		public long getRevision() {
			return revision;
		}
		/**
		 * @return the startBtyeOffset
		 */
		public long getStartBtyeOffset() {
			return startBtyeOffset;
		}
		/**
		 * @return the totalBytes
		 */
		public long getTotalBytes() {
			return totalBytes;
		}
		
		
	}

	private File revisonMappings;
	private Repository repo;

	private TreeMap<String, RevisionMapOffset>revisionMap = new TreeMap<>();

	private File revisionMapDataFile;

	private File revisionMapIndexFile;

	private PrintWriter revisionMapIndexWriter;

	private RandomAccessFile revisionMapDataRandomAccessFile;

	private long endOfRevisionMapDataFileInBytes;
	
	/**
	 * 
	 */
	public SvnRevisionMapper(Repository repo) {

		this.repo = repo;
		revisonMappings = new File(repo.getDirectory(), "jsvn");

		revisonMappings.mkdirs();

		revisionMapDataFile = new File(revisonMappings, REVISION_MAP_FILE_NAME);
		
		revisionMapIndexFile = new File(revisonMappings, REVISION_MAP_INDEX_FILE_NAME);
		
	}
	
	public void initialize () throws IOException {
		
		revisionMapDataRandomAccessFile = new RandomAccessFile(revisionMapDataFile, "rws");
		
		if (revisionMapIndexFile.exists()) {
			// load in any existing data.
			loadIndexData();
		}
		
		endOfRevisionMapDataFileInBytes = revisionMapDataFile.length();
		
		revisionMapIndexWriter = new PrintWriter(new FileOutputStream(revisionMapIndexFile, true));
		
		
	}
	public void shutdown() throws IOException {
		
		revisionMapIndexWriter.flush();
		revisionMapIndexWriter.close();
		
		revisionMapDataRandomAccessFile.close();
		
		
	}

	private void loadIndexData() throws IOException {
		
		BufferedReader indexReader = new BufferedReader(new InputStreamReader(
				new FileInputStream(revisionMapIndexFile)));


		while (true) {

			String line = indexReader.readLine();

			if (line == null)
				break;

				String parts[] = line.split("::");

				if (parts.length != 3)
					continue;

				long revision = Long.parseLong(parts[0]);
				long byteStartOffset = Long.parseLong(parts[1]);
				long totalbytes = Long.parseLong(parts[2]);
				
				revisionMap.put(parts[0], new RevisionMapOffset(revision, byteStartOffset, totalbytes));


		}

		indexReader.close();
		
	}
	
	private void createRevisionMapEntry (long revision, List<String>branchHeadLines) throws IOException {
		
		OutputStream revisionMappingStream = null;
		
		ByteArrayOutputStream bytesOut;
		
		revisionMappingStream = 
					new BZip2CompressorOutputStream(bytesOut = new ByteArrayOutputStream());

		PrintWriter pw = new PrintWriter(revisionMappingStream);

		IOUtils.writeLines(branchHeadLines, "\n", pw);

		pw.flush();
		
		pw.close();
		
		byte[] data = bytesOut.toByteArray();
		
		revisionMapDataRandomAccessFile.seek(endOfRevisionMapDataFileInBytes);

		revisionMapDataRandomAccessFile.write(data);
		
		/*
		 * Write the number of bytes written for this revision.
		 */

		updateRevisionIndex(revision, endOfRevisionMapDataFileInBytes, data.length);
		
		endOfRevisionMapDataFileInBytes += data.length;
		
	}

	public void createRevisionMap(long revision, List<Ref> branchHeads)
			throws IOException {

		List<String>branchHeadLines = new ArrayList<>(branchHeads.size());
		
		for (Ref branchHead : branchHeads) {
			/*
			 * Only archive active branches. skip those containing @
			 */
			if (!branchHead.getName().contains("@"))
				branchHeadLines.add(revision + "::" + branchHead.getName() + "::"
						+ branchHead.getObjectId().name());
		}
		
		
		createRevisionMapEntry(revision, branchHeadLines);
		
	}

	private void updateRevisionIndex(long revision, long revisionStartByteIndex, long bytesWritten) {
		
		revisionMap.put(String.valueOf(revision), new RevisionMapOffset(revision, revisionStartByteIndex, bytesWritten));
		
		revisionMapIndexWriter.println(revision + "::" + revisionStartByteIndex + "::"
				+ bytesWritten);

		revisionMapIndexWriter.flush();
		
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
		
		if (inputStream == null)
			return null;

		List<String> lines = IOUtils.readLines(inputStream, "UTF-8");
		
		inputStream.close();
		
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

			revisionHeads.add(new SvnRevisionMap(revision, branchName,
					branchPath, commitId));

		}

		return revisionHeads;

	}

	private InputStream getRevisionInputStream(long revision)
			throws IOException {

		String revString = String.valueOf(revision);
		
		RevisionMapOffset revisionOffset = revisionMap.get(revString);

		if (revisionOffset == null)
			return null;

		byte[] data = new byte[(int) revisionOffset.getTotalBytes()];
		
		revisionMapDataRandomAccessFile.seek(revisionOffset.getStartBtyeOffset());
		
		revisionMapDataRandomAccessFile.readFully(data);

		return new BZip2CompressorInputStream(new ByteArrayInputStream(data));

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
		
		if (inputStream == null)
			return null;

		List<String> lines = IOUtils.readLines(inputStream, "UTF-8");
		
		inputStream.close();
		
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

	public void repackMapFile() throws IOException {
		
		// close the data file
		revisionMapDataRandomAccessFile.close();
		
		
		// close the index file
		revisionMapIndexWriter.close();
		
		revisionMapIndexFile.delete();
		
		endOfRevisionMapDataFileInBytes = 0L;
		
		revisionMapIndexWriter = new PrintWriter(new FileOutputStream(new File(revisonMappings,
				REVISION_MAP_INDEX_FILE_NAME), true));
		
		// clear the in memory index
		revisionMap.clear();
		
		File copy = new File(revisonMappings, "repack-source.dat");
		
		FileUtils.copyFile(revisionMapDataFile, copy);
		
		revisionMapDataFile.delete();
		
		revisionMapDataRandomAccessFile = new RandomAccessFile(revisionMapDataFile, "rws");
		
		BufferedReader reader = new BufferedReader (new InputStreamReader(new BZip2CompressorInputStream(new FileInputStream(copy), true)));
		
		String currentRevision = null;
		
		List<String>currentRevisionHeads = new ArrayList<String>();
		
		while (true) {

			String line = reader.readLine();
			
			if (line == null) {
				if (currentRevision != null) {
					// archive the last revision
					createRevisionMapEntry(Long.parseLong(currentRevision), currentRevisionHeads);
					
				}
				break;
			}
		
			String parts[] = line.split ("::");
			
			String revisionString = parts[0];
			
			if (currentRevision == null)
				currentRevision = revisionString;
			
			if (!currentRevision.equals(revisionString)) {
				
				// write the revision data and update the index file
				createRevisionMapEntry(Long.parseLong(currentRevision), currentRevisionHeads);
				
				currentRevision = revisionString;
				
				currentRevisionHeads.clear();
				
			}
			
			currentRevisionHeads.add(line);
			
			
		}
		
		reader.close();
		
		copy.delete();
		
		
	}

}
