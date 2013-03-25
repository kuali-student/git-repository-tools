import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.ISVNCanceller;
import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNPropertyValue;
import org.tmatesoft.svn.core.internal.util.SVNPathUtil;
import org.tmatesoft.svn.core.internal.wc.DefaultDumpFilterHandler;
import org.tmatesoft.svn.core.internal.wc.ISVNLoadHandler;
import org.tmatesoft.svn.core.internal.wc.SVNAdminHelper;
import org.tmatesoft.svn.core.internal.wc.SVNDumpStreamParser;
import org.tmatesoft.svn.core.internal.wc.SVNErrorManager;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.admin.SVNAdminEvent;
import org.tmatesoft.svn.core.wc.admin.SVNAdminEventAction;
import org.tmatesoft.svn.core.wc.admin.SVNAdminEventAdapter;
import org.tmatesoft.svn.util.SVNLogType;

/**
 * 
 */

/**
 * Svn Dump Filter is a way to change the contents of a revision as it is loaded.
 * 
 * @author ocleirig
 *
 */
public class SvnDumpFilter {

	private static final Logger log = LoggerFactory.getLogger(SvnDumpFilter.class);
	
	// should be 8MB
	private static final int READ_BUF_SIZE = 8192*1024;
	
	private SVNDumpStreamParser parser;

	private ISVNCanceller canceller;
	/**
	 * 
	 */
	public SvnDumpFilter() {
		
		parser = new SVNDumpStreamParser(canceller = new ISVNCanceller() {
			
			public void checkCancelled() throws SVNCancelException {
				
			}
		});
		
	
		
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

		if (args.length != 2) {
			log.error("USAGE: <source dump file> <target dump file>");
			System.exit(-1);
		}
		
		String sourceDumpFile = args[0];
		String targetDumpFile = args[1];
		
		SvnDumpFilter sdf = new SvnDumpFilter();
		
		boolean exclude = true;
		boolean renumberRevisions = true;
		boolean dropEmptyRevisions = false;
		boolean preserveRevisionProperties = true;
		Collection prefixes = new HashSet<Object>();
		boolean skipMissingMergeSources = false;
		
		try {
			BufferedInputStream inputStream;
			BufferedOutputStream outputStream;
			sdf.doFilter(inputStream = new BufferedInputStream(new FileInputStream(sourceDumpFile), READ_BUF_SIZE), outputStream = new BufferedOutputStream(new FileOutputStream(targetDumpFile, false), READ_BUF_SIZE), exclude, renumberRevisions, dropEmptyRevisions, preserveRevisionProperties, prefixes, skipMissingMergeSources);
			
			try {
				inputStream.close();
				outputStream.close();
			} catch (IOException e) {
				log.warn("Problem closing streams");
			}
			
		} catch (FileNotFoundException e) {
			log.error("file not found", e);
		} catch (SVNException e) {
			log.error("svn exception", e);
		}
	}


	/*
	 * Copied from SvnKit SVNAdminClient
	 */
	private void writeDumpData(OutputStream out, String data) throws SVNException {
        try {
            out.write(data.getBytes("UTF-8"));
        } catch (IOException ioe) {
            SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.IO_ERROR, ioe.getLocalizedMessage());
            SVNErrorManager.error(err, ioe, SVNLogType.FSFS);
        }
    }
	
	public void doFilter(InputStream dumpStream, OutputStream resultDumpStream, boolean exclude,
            boolean renumberRevisions, boolean dropEmptyRevisions, boolean preserveRevisionProperties,
            Collection prefixes, boolean skipMissingMergeSources) throws SVNException {
        CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();

        writeDumpData(resultDumpStream, String.format ("%s: %d\n\n", SVNAdminHelper.DUMPFILE_MAGIC_HEADER, SVNAdminHelper.DUMPFILE_FORMAT_VERSION));

        SVNAdminEventAdapter myEventHandler;
        CustomDumpFilter handler = new CustomDumpFilter(resultDumpStream, myEventHandler = new SVNAdminEventAdapter(), exclude, renumberRevisions,
                dropEmptyRevisions, preserveRevisionProperties, prefixes, skipMissingMergeSources);
        
        SVNDumpStreamParser parser = new SVNDumpStreamParser(canceller);
        
        parser.parseDumpStream(dumpStream, handler, decoder);

        if (myEventHandler != null) {
            if (handler.getDroppedRevisionsCount() > 0) {
                String message = MessageFormat.format("Dropped {0} revision(s).", new Object[] {
                        String.valueOf(handler.getDroppedRevisionsCount()) });
                SVNAdminEvent event = new SVNAdminEvent(SVNAdminEventAction.DUMP_FILTER_TOTAL_REVISIONS_DROPPED,
                        message);
                event.setDroppedRevisionsCount(handler.getDroppedRevisionsCount());
                myEventHandler.handleAdminEvent(event, ISVNEventHandler.UNKNOWN);
            }

            if (renumberRevisions) {
                Map renumberHistory = handler.getRenumberHistory();
                Long[] reNumberedRevisions = (Long[]) renumberHistory.keySet().toArray(new Long[renumberHistory.size()]);
                Arrays.sort(reNumberedRevisions);
                for (int i = reNumberedRevisions.length; i > 0; i--) {
                    Long revision = reNumberedRevisions[i - 1];
                    CustomDumpFilter.RevisionItem revItem = (CustomDumpFilter.RevisionItem) renumberHistory.get(revision);
                    if (revItem.wasDropped()) {
                        String message = MessageFormat.format("{0} => (dropped)", new Object[] { revision.toString() });
                        SVNAdminEvent event = new SVNAdminEvent(revision.longValue(),
                                SVNAdminEventAction.DUMP_FILTER_DROPPED_RENUMBERED_REVISION, message);
                        myEventHandler.handleAdminEvent(event, ISVNEventHandler.UNKNOWN);
                    } else {
                        String message = MessageFormat.format("{0} => {1}", new Object[] { revision.toString(),
                                String.valueOf(revItem.getRevision()) });
                        SVNAdminEvent event = new SVNAdminEvent(revItem.getRevision(), revision.longValue(),
                                SVNAdminEventAction.DUMP_FILTER_RENUMBERED_REVISION, message);
                        myEventHandler.handleAdminEvent(event, ISVNEventHandler.UNKNOWN);
                    }
                }
            }

            Map droppedNodes = handler.getDroppedNodes();
            if (!droppedNodes.isEmpty()) {
                String message = MessageFormat.format("Dropped {0} node(s)", new Object[] {
                        String.valueOf(droppedNodes.size()) });
                SVNAdminEvent event = new SVNAdminEvent(SVNAdminEventAction.DUMP_FILTER_TOTAL_NODES_DROPPED,
                        message);
                event.setDroppedNodesCount(droppedNodes.size());
                myEventHandler.handleAdminEvent(event, ISVNEventHandler.UNKNOWN);
                String[] paths = (String[]) droppedNodes.keySet().toArray(new String[droppedNodes.size()]);
                Arrays.sort(paths, SVNPathUtil.PATH_COMPARATOR);
                for (int i = 0; i < paths.length; i++) {
                    String path = paths[i];
                    message = "'" + path + "'";
                    event = new SVNAdminEvent(SVNAdminEventAction.DUMP_FILTER_DROPPED_NODE, path, message);
                    myEventHandler.handleAdminEvent(event, ISVNEventHandler.UNKNOWN);
                }
            }
        }
    }
	public void applyDumpFileFilter(String sourceDumpFile,
			String targetDumpFile) throws FileNotFoundException, SVNException {
		
		CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
		
		parser.parseDumpStream(new BufferedInputStream(new FileInputStream(sourceDumpFile), READ_BUF_SIZE), new ISVNLoadHandler() {
			
			public void setRevisionProperty(String propertyName,
					SVNPropertyValue propertyValue) throws SVNException {
				log.info("debug");
			}
			
			public void setNodeProperty(String propertyName,
					SVNPropertyValue propertyValue) throws SVNException {
				log.info("debug");
				
			}
			
			public void setFullText() throws SVNException {
				log.info("debug");
				
			}
			
			public void removeNodeProperties() throws SVNException {
				log.info("debug");
				
			}
			
			public void parseUUID(String uuid) throws SVNException {
				log.info("debug");
				
			}
			
			public void parseTextBlock(InputStream dumpStream, long contentLength,
					boolean isDelta) throws SVNException {
				log.info("debug");
				
			}
			
			public void openRevision(Map headers) throws SVNException {
				log.info("debug");
				
			}
			
			public void openNode(Map headers) throws SVNException {
				log.info("debug");
				
			}
			
			public void deleteNodeProperty(String propertyName) throws SVNException {
				log.info("debug");
				
			}
			
			public void closeRevision() throws SVNException {
				log.info("debug");
				
			}
			
			public void closeNode() throws SVNException {
				log.info("debug");
				
			}
			
			public void applyTextDelta() throws SVNException {
				log.info("debug");
				
			}
		}, decoder);
		
	}

}
