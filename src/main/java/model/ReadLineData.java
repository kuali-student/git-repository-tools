package model;

import java.io.PrintWriter;

public class ReadLineData {
	
	private int skippedLines = 0;
	
	private String line;

	public ReadLineData(String line, int skippedLines) {
		super();
		this.line = line;
		this.skippedLines = skippedLines;
	}

	/**
	 * @return the skippedLines
	 */
	public int getSkippedLines() {
		return skippedLines;
	}

	/**
	 * @param skippedLines the skippedLines to set
	 */
	public void setSkippedLines(int skippedLines) {
		this.skippedLines = skippedLines;
	}

	/**
	 * @return the line
	 */
	public String getLine() {
		return line;
	}

	/**
	 * @param line the line to set
	 */
	public void setLine(String line) {
		this.line = line;
	}

	public boolean startsWith(String prefix) {
		return this.line.startsWith(prefix);
	}

	public void println(PrintWriter writer) {
		
		for (int i = 0; i < skippedLines; i++) {
			writer.print("\n"); // unix style end of lines
		}
		
		writer.print(line);
		writer.print("\n"); // unix style end of lines
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ReadLineData [skippedLines=");
		builder.append(skippedLines);
		builder.append(", line=");
		builder.append(line);
		builder.append("]");
		return builder.toString();
	}

	
	
	

}
