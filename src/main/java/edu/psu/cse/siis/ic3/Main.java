package edu.psu.cse.siis.ic3;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.psu.cse.siis.ic3.db.SQLConnection;
import edu.psu.cse.siis.ic3.manifest.SHA256Calculator;

public class Main {

	public static void main(String[] args) {

		

		boolean multiapks = false;
		ArrayList<String> arguments = new ArrayList<String>();

		int argc = args.length;
		String dirPath="";
		for ( int i = 0; i < argc; i ++){
			if ( args[i].matches("-dir")) {
				multiapks = true;
				dirPath = args[++i];
			}
			else
				arguments.add(args[i]);	
		}

		Set<String> fileList = new HashSet<String>();

		if (multiapks){
			try {
				DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dirPath));
				for (Path path : stream) {
					if (!Files.isDirectory(path)) {
						if (path.getFileName().toString().endsWith(".apk"))
							fileList.add(path.getFileName()
								.toString());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}


			Iterator<String> it = fileList.iterator();
			arguments.add("-input");
			while(it.hasNext()){
				String target = it.next();
				// System.out.println(target);
				arguments.add(dirPath+"/"+target);
				execute(arguments.toArray(new String[0]));
				arguments.remove(dirPath+"/"+target);
			}
		}
		else{
			execute(args);
		}		
	}

	public static void execute(String[] arguments){
		Ic3CommandLineParser parser = new Ic3CommandLineParser();
		Ic3CommandLineArguments commandLineArguments = parser.parseCommandLine(arguments, Ic3CommandLineArguments.class);
		String apk = commandLineArguments.getInput();
		System.out.println("-----Start analysis apk : "+apk);

		edu.psu.cse.siis.coal.Main.reset();
		SQLConnection.reset();
		
		if (commandLineArguments == null) {
			return;
		}
		commandLineArguments.processCommandLineArguments();
		if ( commandLineArguments.getProtobufDestination() == null){
			commandLineArguments.setProtobufDestination((String) apk.subSequence(0,apk.lastIndexOf("/")));
		}

		if (commandLineArguments.getDbName()!=null){
			SQLConnection.init(commandLineArguments.getDbName(), "./cc.properties", null, 3306);

			try {
				String shasum = SHA256Calculator.getSHA256(new File(commandLineArguments.getInput()));
	
				if (SQLConnection.checkIfAppAnalyzed(shasum)) {
					return;
				}
	
			} catch (SQLException | NoSuchAlgorithmException | IOException e) {
				
				System.out.println("Error computing SHA of apk file!");
			}
		}
		

		Ic3Analysis analysis = new Ic3Analysis(commandLineArguments);
		analysis.performAnalysis(commandLineArguments);


	}
}