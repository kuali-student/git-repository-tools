import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNPropertyData;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCClient;



/**
 * @author Kuali Student Team
 * 
 */
public class ExtractSvnIngoreDetails {

	private static final Logger log = LoggerFactory
			.getLogger(ExtractSvnIngoreDetails.class);

	/**
	 * 
	 */
	public ExtractSvnIngoreDetails() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.err.println("USAGE: <absolute path to working copy>");
			System.exit(-1);
		}
		try {
			File diskRepo = new File(args[0]);

			SVNClientManager clientManager = SVNClientManager.newInstance();

			SVNWCClient wcClient = clientManager.getWCClient();

			Map<String, List<File>>svnIgnoreContentToFileListMap = new HashMap<String, List<File>>();

			Iterator<File> dirIter = FileUtils.iterateFilesAndDirs(diskRepo,
					new IOFileFilter() {

						public boolean accept(File arg0, String arg1) {
							// TODO Auto-generated method stub
							return false;
						}

						public boolean accept(File arg0) {
							// TODO Auto-generated method stub
							return false;
						}
					}, new IOFileFilter() {

						public boolean accept(File arg0, String arg1) {
							// TODO Auto-generated method stub
							return false;
						}

						public boolean accept(File arg0) {

							if (arg0.isDirectory()
									&& !arg0.getName().contains(".svn"))
								return true;
							else
								return false;
						}
					});

			while (dirIter.hasNext()) {
				File file = dirIter.next();

				if (!file.isDirectory()) {
					// this is a problem
					throw new RuntimeException("");
				}

				SVNPropertyData prop = wcClient.doGetProperty(file,
						"svn:ignore", SVNRevision.HEAD, SVNRevision.WORKING);

				if (prop != null) {
					
					String propertyValue = prop.getValue().getString();
					
					List<File> paths = svnIgnoreContentToFileListMap.get(propertyValue);
					
					if (paths == null) {
						paths = new ArrayList<File>();
						svnIgnoreContentToFileListMap.put(propertyValue, paths);
						
					}
					
					paths.add(file);
					
				}
			}
			
			log.warn(svnIgnoreContentToFileListMap.size() + " distinct ignores");
			
			int counter = 1;
			
			for (String contents : svnIgnoreContentToFileListMap.keySet()) {
				
				List<String>lines = new ArrayList<String>();
				
				lines.add(contents);
				
				List<File> paths = svnIgnoreContentToFileListMap.get(contents);
				for (File file : paths) {
					
					lines.add (file.getAbsolutePath());
					
				}
				
				FileUtils.writeLines(new File (counter + ".dat"), lines);
				
				counter++;
			}

		} catch (Exception e) {

			log.error("exception = ", e);
		}

	}

}
