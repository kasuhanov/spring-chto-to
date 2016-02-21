package su.asgor.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import su.asgor.model.PurchaseType;

@Service
public class LoaderService {
	@Autowired
	private ParserService parser;
	private final Logger log = LoggerFactory.getLogger(this.getClass());
    public void start(){
    	log.info("loader");
    	//load();
    	//unzip();
		//parse();
    }
    private void load(){
        try {
            String serverAddress = "ftp.zakupki.gov.ru";
            String userId = "fz223free";
            String password = "fz223free";
            String remoteDirectory = "out/published/Cheliabinskaya_obl/purchaseNotice/daily";
            String localDirectory = null;
            for(PurchaseType pt: PurchaseType.values()){
            	File workingDir = new File("downloaded/purchaseNotice"+pt);
            	workingDir.mkdirs();
            }	
            FTPClient ftp = new FTPClient();
            ftp.connect(serverAddress);
            if(!ftp.login(userId, password)){
                ftp.logout();
                throw new Exception("Cant connect to remote ftp. Invalid ftp properties");
            } 
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())){
                ftp.disconnect();
                throw new Exception("Cant connect to remote ftp. Invalid ftp properties");
            }
            ftp.enterLocalPassiveMode();
            ftp.changeWorkingDirectory(remoteDirectory);
            for(PurchaseType pt: PurchaseType.values()){
            	ftp.changeWorkingDirectory("../../purchaseNotice"+pt+"/daily");
            	localDirectory = "downloaded/purchaseNotice"+pt;
	            FTPFile[] ftpFiles = ftp.listFiles();
	            if (ftpFiles != null && ftpFiles.length > 0) {
	                for (FTPFile file : ftpFiles) {
	                    if (!file.isFile()) {
	                    	System.out.println(file.getName());
	                        continue;
	                    }
	                    File newFile = new File(localDirectory+"/"+file.getName());
	                    if(newFile.exists()){
	                    	log.info("file already exists - "+file.getName());
	                        continue;
	                    }
	                    newFile.createNewFile();
	                    OutputStream output = new FileOutputStream(newFile,false);
	                    log.info("Downloading file - " + file.getName());
	                    ftp.retrieveFile(file.getName(), output);
	                    output.close();
	                }
	            }
	            log.info("Done loading files in "+pt);
            }
            log.info("Files are loaded. Disconnecing.");
            ftp.logout();
            ftp.disconnect();
        }catch (Exception e){
        	log.error(e.getMessage(),e);
        }
    }
    private void unzip(){
    	log.info("Starting unzip");
        File workingDir = new File("downloaded/unzipped");
        workingDir.mkdirs();	
    	try {
    		for(PurchaseType pt: PurchaseType.values()){
	        	Files.walk(Paths.get("downloaded/purchaseNotice"+pt)).forEach(filePath -> {
	        	    if (Files.isRegularFile(filePath)) {
	        	    	try {
	        	    		log.info("unzipping file - "+filePath.getFileName());
							unzipFile(filePath, new File("downloaded/unzipped/purchaseNotice"+pt).toPath());
						} catch (Exception e) {
							log.error("error unzipping "+filePath.getFileName()+e.getMessage(),e);
						}
	        	    }
	        	});
	        	log.info("Done unzipping in "+pt);
    		}
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
    }
    private void unzipFile(Path filePath, Path destination) throws IOException {
    	Map<String, String> zipProperties = new HashMap<>();
    	zipProperties.put("create", "false");
    	zipProperties.put("encoding", "UTF-8");
    	URI zipFile = URI.create("jar:file:" + filePath.toUri().getPath());

    	try (FileSystem zipfs = FileSystems.newFileSystem(zipFile, zipProperties)) {
    	  Path rootPath = zipfs.getPath("/");
    	  Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {

    	    @Override
    	    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    	      Path targetPath = destination.resolve(rootPath.relativize(dir).toString());
    	      if (!Files.exists(targetPath)) {
    	        Files.createDirectory(targetPath);
    	      }
    	      return FileVisitResult.CONTINUE;
    	    }

    	    @Override
    	    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    	      if(!Files.exists(destination.resolve(rootPath.relativize(file).toString()))){
    	    	  Files.copy(file, destination.resolve(rootPath.relativize(file).toString()));
    	    	  log.info("created file - "+destination.resolve(rootPath.relativize(file).toString()));
    	      }else {
    	    	  log.info("file already exists- "+destination.resolve(rootPath.relativize(file).toString()));
    	      }
    	      return FileVisitResult.CONTINUE;
    	    }
    	  });
    	}
    }
    private void parse(){
    	log.info("Starting parsing");
    	try {
    		for(PurchaseType pt: PurchaseType.values()){
	        	Files.walk(Paths.get("downloaded/unzipped/purchaseNotice"+pt)).forEach(filePath -> {
	        	    if (Files.isRegularFile(filePath)&&(new File(filePath.toUri()).length()>0)) {
	        	    	log.info("parsing  file - "+filePath.getFileName());
	        	    	try {
							parser.parse(filePath.toString().substring(0,filePath.toString().lastIndexOf(File.separator))+File.separator,
									filePath.getFileName().toString());
						} catch (Exception e) {
							log.error(e.getMessage(),e);
						}
	        	    }
	        	});
	        	log.info("Done unzipping in "+pt);
    		}
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
    }
}
