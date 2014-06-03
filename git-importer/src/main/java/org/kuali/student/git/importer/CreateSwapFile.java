/**
 * 
 */
package org.kuali.student.git.importer;

import java.io.RandomAccessFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * When running the importer it can be a lot faster to run in a tmpfs partition.  However due to the size of the repository we will need to add in a large swap file
 * to allow the C git GC to be performed without causing out of memory issues.
 * <p>
 * This tool can be used to create a large file that can be attached as a swap file.
 * <p>
 * This is a lot faster than using dd.
 * <p>
 * If you are on a modern file system you can use fallocate but I was running ext3 which necessitated this utility class.
 * <p>
 * This program will create sparse files but they can still be used as swap files if routed through the loopback.
 * <code>
 * losetup /dev/loop0 <swap file>
 * swapon /dev/loop0
 * </code>
 * @author ocleirig
 *
 */
public class CreateSwapFile {

	private static final Logger log = LoggerFactory.getLogger(CreateSwapFile.class);
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (args.length != 2) {
			System.err.println("USAGE: <swap file name> <size in gigabytes>");
			System.exit(1);
		}
		
		String swapFileName = args[0];
		
		String size = args[1];
		
		  try {
              
			  RandomAccessFile f = new RandomAccessFile(swapFileName, "rw");
              
              f.setLength((Integer.parseInt(size) * FileUtils.ONE_GB) - 1);

              f.write(123);
              
              f.close();
              
         } catch (Exception e) {
             log.error("Failed to create file " + swapFileName + " " + size + " GB due to:", e);
         }

	}

}
