/* Jedd - A language for implementing relations using BDDs
 * Copyright (C) 2003 Ondrej Lhotak
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

package polyglot.frontend;

import polyglot.ast.*;
import polyglot.types.*;
import polyglot.util.*;
import polyglot.visit.*;
import polyglot.main.*;

import java.util.*;
import java.io.*;

public class JeddSourceLoader extends SourceLoader {
    public JeddSourceLoader(ExtensionInfo sourceExt, Collection sourcePath) {
        super( sourceExt, sourcePath );
    }

    /** Load a source from a specific file. */
    public FileSource fileSource(String fileName) throws IOException {
	File sourceFile = new File(fileName);

	if (! sourceFile.exists()) {
	    throw new FileNotFoundException(fileName);
	}

        if (! fileName.endsWith("." + sourceExt.fileExtension())
        &&  ! fileName.endsWith(".java") ) {
            throw new IOException("Source \"" + fileName +
                                  "\" does not have the extension \"." +
                                  sourceExt.fileExtension() + "\".");
        }

	if (Report.should_report(Report.frontend, 2))
	    Report.report(2, "Loading class from " + sourceFile);

	return new CmdLineFileSource(fileName);
    }

    /** Load the source file for the given class name using the source path. */
    public FileSource classSource(String className) {
	/* Search the source path. */
        String fileName = className.replace('.', File.separatorChar) +
                                        "." + sourceExt.fileExtension();
        FileSource ret = classSourceGuts(className, fileName);
        if( ret != null ) return ret;

        fileName = className.replace('.', File.separatorChar) + ".java";
        return classSourceGuts(className, fileName);
    }

    private FileSource classSourceGuts(String className, String fileName) {
	if (current_dir == null) {
            current_dir = new File(System.getProperty("user.dir"));
        }

	for( Iterator directoryIt = sourcePath.iterator(); directoryIt.hasNext(); ) {

	    final File directory = (File) directoryIt.next();
            Set dirContents = (Set)directoryContentsCache.get(directory);
            if (dirContents == null) {
                dirContents = new HashSet();
                directoryContentsCache.put(directory, dirContents);
                if (directory.exists()) {
                    String[] contents = directory.list();
                    for (int j = 0; j < contents.length; j++) {
                        dirContents.add(contents[j]);
                    }
                }                
            }

            // check if the source file exists in the directory
            int index = fileName.indexOf(File.separatorChar);
            if (index < 0) index = fileName.length(); 
            String firstPart = fileName.substring(0, index);

            if (dirContents.contains(firstPart)) {
                // the directory contains at least the first part of the
                // file path. We will check if this file exists.
                File sourceFile;
                
                if (directory != null && directory.equals(current_dir)) {
                    sourceFile = new File(fileName);
                }
                else {
                    sourceFile = new File(directory, fileName);
                }
                
                if (sourceFile.exists()) {
                    if (Report.should_report(Report.frontend, 2))
                        Report.report(2, "Loading " + className + " from " + sourceFile);
                
                    return new FileSource(sourceFile);
                }
            }
	}

	return null;
    }

}
