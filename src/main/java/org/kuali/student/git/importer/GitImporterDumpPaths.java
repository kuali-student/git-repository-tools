/*
 * Copyright 2013 The Kuali Foundation
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
package org.kuali.student.git.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.iq80.snappy.SnappyInputStream;
import org.kuali.student.git.model.GitBranchData;
import org.kuali.student.git.model.LargeBranchNameProviderMapImpl;
import org.kuali.student.git.model.branch.BranchDetector;
import org.kuali.student.git.model.exceptions.VetoBranchException;
import org.kuali.student.git.utils.GitBranchUtils;
import org.kuali.student.git.utils.GitBranchUtils.ILargeBranchNameProvider;
import org.kuali.student.svn.tools.AbstractParseOptions;
import org.kuali.student.svn.tools.SvnDumpFilter;
import org.kuali.student.svn.tools.merge.model.BranchData;
import org.kuali.student.svn.tools.model.INodeFilter;
import org.kuali.student.svn.tools.model.ReadLineData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Kuali Student Team
 * 
 * We need to look at the paths that will appear after r15000 or so to make sure we handle them properly.
 * 
 * This is a tool that will dump out both actual and copy from paths from a dump file. 
 * 
 * Later these can be branch detected and evaluated asto whether or not they are correct.
 * 
 */
public class GitImporterDumpPaths {

	private static final Logger log = LoggerFactory
			.getLogger(GitImporterDumpPaths.class);

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

		if (args.length != 2) {
			log.error("USAGE: <svn dump file> <output file name>");
			System.exit(-1);
		}

		try {

			ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
					"git/GitImporterMain-applicationContext.xml");

			applicationContext.registerShutdownHook();

			SvnDumpFilter filter = applicationContext
					.getBean(SvnDumpFilter.class);

			final BranchDetector branchDetector = applicationContext.getBean("branchDetector", BranchDetector.class);
			
			// final MergeDetectorData detectorData = applicationContext
			// .getBean(MergeDetectorData.class);

			File dumpFile = new File(args[0]);

			if (!dumpFile.exists()) {
				throw new FileNotFoundException(args[0] + " path not found");
			}

			File outputFile = new File(args[1]).getAbsoluteFile();

			final PrintWriter pathLog = new PrintWriter(outputFile);
			
			// extract any known branches from the repository
			
			SnappyInputStream compressedInputStream = new SnappyInputStream(new FileInputStream (dumpFile));
			
			filter.parseDumpFile(compressedInputStream, new AbstractParseOptions() {

				private Map<String, GitBranchData>knownBranchesMap = new HashMap<String, GitBranchData>();
				
				private LargeBranchNameProviderMapImpl largeBranchNameProvider = new LargeBranchNameProviderMapImpl();
			
				private long currentRevision = -1L;
				
				/* (non-Javadoc)
				 * @see org.kuali.student.svn.tools.AbstractParseOptions#onStreamEnd(org.kuali.student.svn.tools.model.ReadLineData)
				 */
				@Override
				public void onStreamEnd(ReadLineData lineData) {

					writeDetectedBranches();
					
				}

				/* (non-Javadoc)
				 * @see org.kuali.student.svn.tools.AbstractParseOptions#onRevision(long, org.kuali.student.svn.tools.model.ReadLineData)
				 */
				@Override
				public void onRevision(long currentRevision,
						ReadLineData lineData) {
					
//					writeDetectedBranches();
					
					log.info("revision = " + currentRevision);
					
					this.currentRevision = currentRevision;
				}

				private void writeDetectedBranches() {
					
					for(Map.Entry<String, GitBranchData>entry : knownBranchesMap.entrySet()) {
						String branchName = entry.getKey();
						
						GitBranchData data = entry.getValue();
						
						pathLog.println(currentRevision + "::" + branchName + "::" + data.getBranchPath());
						
					}
					
					this.knownBranchesMap.clear();
					
					this.largeBranchNameProvider.clear();
					
					
				}

				/* (non-Javadoc)
				 * @see org.kuali.student.svn.tools.AbstractParseOptions#onAfterNode(long, java.lang.String, java.util.Map, org.kuali.student.svn.tools.model.INodeFilter)
				 */
				@Override
				public void onAfterNode(long currentRevision, String path,
						Map<String, String> nodeProperties,
						INodeFilter nodeFilter) {
					
					
					storeBranchData (currentRevision, path);
					
					String copyFromPath = nodeProperties.get(SvnDumpFilter.SVN_DUMP_KEY_NODE_COPYFROM_PATH);
					String copyFromRevision = nodeProperties.get(SvnDumpFilter.SVN_DUMP_KEY_NODE_COPYFROM_REV);
					
					if (copyFromPath != null) {
						
						storeBranchData(copyFromRevision, copyFromPath);
						
					}
					
				}

				private void storeBranchData(String revision, String path) {
					storeBranchData(Long.valueOf(revision), path);
				}
				private void storeBranchData(long revision, String path) {

					try {
						BranchData branchData = branchDetector.parseBranch(revision, path);
						
						String branchName = GitBranchUtils.getCanonicalBranchName(branchData.getBranchPath(), revision, largeBranchNameProvider);
						
						if (!knownBranchesMap.containsKey(branchName)) {
							GitBranchData data = new GitBranchData(branchName, revision, largeBranchNameProvider, branchDetector);
							
							knownBranchesMap.put(branchName, data);
						}
						
					} catch (VetoBranchException e) {
						// skip over non branches
						return;
					}
				}
				
				
			});

			pathLog.close();

		} catch (Exception e) {
			log.error("Processing failed", e);
		}

	}
	
	

}
