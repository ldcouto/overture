package org.overture.core.tests;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

/**
 * Input/Result handler for the test framework. Should be used in parametric
 * constructors of test cases. Provides paths for test inputs (typically VDM
 * sources) and results (JSON files)
 * 
 * @author ldc
 */
public class PathsProvider {

	/** The Constant RESULT_EXTENSION. */
	private final static String RESULT_EXTENSION = ".RESULT";

	/** The Constant VDM_EXTENSION_REGEX. */
	private final static String VDM_EXTENSION_REGEX = "(.*)\\.vdm(pp|rt|sl)";

	/**
	 * Processes (recursively) a folder of source inputs and result files.
	 * 
	 * The source input files must have one of the three VDM extensions (,vdmsl,
	 * .vdmpp, .vdmrt). The result files must be in the same folder as the
	 * respective inputs and have the same name including the extension but also
	 * have .RESULT as a second extension. <br>
	 * <br>
	 * ex: Test.vdmsl, Test.vdmsl.RESULT
	 * 
	 * 
	 * @param root
	 *            the path(s) to the root(s) folder of the test inputs
	 * @return the a collection of test model file and result paths in the form
	 *         of {filename ,filepath, resultpath} arrays
	 */
	public static Collection<Object[]> computePaths(String... root) {
		File dir;
		Collection<Object[]> r = files(new File(root[0]), true);

		for (int i = 1; i < root.length; i++) {
			dir = new File(root[i]);
			r.addAll(files(dir, false));
		}
		return r;
	}

	private static Collection<Object[]> files(File dir, boolean result) {
		Collection<File> files = FileUtils.listFiles(dir, new RegexFileFilter(
				VDM_EXTENSION_REGEX), DirectoryFileFilter.DIRECTORY);

		List<Object[]> paths = new Vector<Object[]>();

		for (File file : files) {
			paths.add(new Object[] { file.getName(), file.getPath(),
					file.getPath() + RESULT_EXTENSION });

		}
		return paths;
	}

}
