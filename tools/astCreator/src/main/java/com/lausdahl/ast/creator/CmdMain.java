package com.lausdahl.ast.creator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CmdMain
{
	public final static boolean GENERATE_VDM = false;

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		if (args.length == 0)
		{
			help();
		}
		
		if(args.length==2)
		{
			String grammarFilePath = args[0];
			grammarFilePath= grammarFilePath.replace('/', File.separatorChar).replace('\\', File.separatorChar);
			String outputPath = args[1];
			outputPath=outputPath.replace('/', File.separatorChar).replace('\\', File.separatorChar);
			Main.create(new FileInputStream(grammarFilePath), new File(outputPath), true,GENERATE_VDM);
		}
	
		if (args.length == 4)
		{
			System.out.println("Creating extension AST.");
			File ast1File = new File(args[0]);
			File ast2File = new File(args[1]);
			String extName= args[2];
			File output   = new File(args[3]);
			
			// Check ast file 1
			if (!(ast1File.exists() && ast1File.canRead()))
				{ System.out.println(ast1File+" does not exists or is not reable.");return; }
			
			// Check ast file 2
			if (!(ast2File.exists() && ast2File.canRead()))
				{ System.out.println(ast2File+" does not exists or is not reable.");return; }
			
			// Fire in the hall
			Main.create(new FileInputStream(ast1File), new FileInputStream(ast2File),output,extName,GENERATE_VDM);
				
		}
		
	}

	private static void help()
	{
		StringBuffer buf = new StringBuffer();
		buf.append("Generates a AST from a grammar file.\n");
		buf.append("generator [grammer file] [output path]\n\n");
		buf.append("grammer file: The file path to the grammar file to be generated.\n");
		buf.append("output path: The output path to the folder where the ");
		buf.append("              generated sources should be placed\n");
		//buf.append("tostring grammar file: Optional argument for a toString grammar file.\n");

		System.out.println(buf.toString());

	}

}