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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.CommitBuilder;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TreeFormatter;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.kuali.student.git.importer.GitImporterMain;
import org.kuali.student.git.model.ref.exception.BranchRefExistsException;
import org.kuali.student.git.model.ref.utils.GitRefUtils;

/**
 * 
 * A base that can be used to run consecutive runs of the GitImporterMain.
 * 
 * @author Kuali Student Team
 *
 */
@RunWith(BlockJUnit4ClassRunner.class)
public abstract class AbstractGitImporterMainTestCase {

	private String name;
	
	protected Repository repository;

    protected RevWalk rw;

	private boolean enablePlugin;

	private boolean bare = true;

	/**
	 * @param name
	 * @param b 
	 */
	public AbstractGitImporterMainTestCase(String name, boolean enablePlugin) {
		this.name = name;
		this.enablePlugin = enablePlugin;
		
	}
	
	protected void setBare(boolean bare) {
		this.bare = bare;
	}

	@Before
	public void setupRepository () throws IOException, BranchRefExistsException {
		
		File gitRepository = new File ("target/" + name);
		
		FileUtils.deleteDirectory(gitRepository);
		
		repository = GitRepositoryUtils
				.buildFileRepository(gitRepository, true, bare);

        if (rw == null)
            rw = new org.eclipse.jgit.revwalk.RevWalk(repository);
        else
            rw.reset();
		
	}
		
	protected void runImporter(Repository repository, long importRevision, String dumpFilePath, String repoURL, String repoUUID, String emailHostPart) throws IOException {
		
		SvnRevisionMapper revisionMapper = new SvnRevisionMapper(repository);
//		
		revisionMapper.initialize();
		
		Map<String, Ref> heads = repository.getRefDatabase().getRefs(Constants.R_HEADS);
		
		if (heads.size() > 0) {
			revisionMapper.createRevisionMap(importRevision-1L, new ArrayList<Ref>(heads.values()));
		}
		
		revisionMapper.shutdown();
		
		if (enablePlugin)
			System.getProperties().setProperty("spring.profiles.active", "configured-plugin");
		
		GitImporterMain.main(new String [] {dumpFilePath, repository.getDirectory().getAbsolutePath(), "target/"+name+"-r"+importRevision+"-veto.log", "target/"+name+"-r"+importRevision+"-copyFrom-skipped.log", "target/"+name+"-r"+importRevision+"-blob.log", "0", repoURL, repoUUID, emailHostPart});

		
	}
		
	
}
