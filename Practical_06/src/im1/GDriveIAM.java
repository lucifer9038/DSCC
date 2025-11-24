package im1;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Permissions;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import com.google.common.util.concurrent.CycleDetectingLockFactory.WithExplicitOrdering;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class GDriveIAM {
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final java.io.File CREDENTIALS_FOLDER = new java.io.File("/Users/priteshbhuravane/MCA/Sem3 ");
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);

    // --------------------- AUTHENTICATION ---------------------
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        java.io.File clientSecretFilePath = new java.io.File("/Users/priteshbhuravane/MCA/Sem3/client_secret.json");
        if (!clientSecretFilePath.exists()) {
            throw new FileNotFoundException("Credentials File not found!");
        }

        try (InputStream in = new FileInputStream(clientSecretFilePath)) {
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                    .setDataStoreFactory(new FileDataStoreFactory(CREDENTIALS_FOLDER))
                    .setAccessType("offline")
                    .build();
            return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        }
    }



	public static void main(String[] args) throws IOException, GeneralSecurityException {
		 final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
	        Credential credential = getCredentials(HTTP_TRANSPORT);

	        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
	                .setApplicationName("GDrive Access")
	                .build();

	        //1. create a folder for testing
	        File folderMetadata = new File();
	        folderMetadata.setName("IAM Test Folder");
	        folderMetadata.setMimeType("application/vnd.google-apps.folder");
	        
	        File folder = service.files().create(folderMetadata)
	        		.setFields("id,name").execute();
	        String folderID = folder.getId();
	        
	        System.out.println("Create Folder: " +folder.getName() + " (ID:" +folderID+")");
	        
	        //2. Upload word document filde undere newly crwerated folder
	        String localFilePath = "/Users/priteshbhuravane/MCA/Sem3/DSCC/IAM_GoggleDrive.docx";	
	        java.io.File localFile = new java.io.File(localFilePath);
	        
	        if (!localFile.exists()) {
	        	System.out.println("Local doc file not found a : " +localFilePath);
	        	return;
	        }
	        com.google.api.services.drive.model.File fileMetadata =new com.google.api.services.drive.model.File();
	        fileMetadata.setName("IAM_GoggleDrive.docx");
	        fileMetadata.setParents(Collections.singletonList(folderID));
	        
	        com.google.api.client.http.FileContent mediaContent = new com.google.api.client.http.FileContent("application/vnd.openxmlformats-officedocument.wordprocessingml.document", localFile);
	        
	        File uplodedFile = service.files().create(fileMetadata,mediaContent)
	        		.setFields("id , name, parents")
	        		.execute();
	        System.out.println("Uploded file : "+uplodedFile.getName()+"(ID:"+uplodedFile.getId()+")inside folder: " +folder.getName());
	        
	        //3.share folder with user (Identity Management
	        Permission userPermission = new Permission()
	        	    .setType("user")
	        	    .setRole("writer")
	        	    .setEmailAddress("bhuravanepritesh@gmail.com");
	        	service.permissions().create(folderID, userPermission)
	        	    .setSendNotificationEmail(true)
	        	    .execute();

	        
	        System.out.println("Shared folder WithExplicitOrdering bhuravanepritesh@gmail.com as writer");
	        
	        //4. List permisiion for the folder
	        PermissionList permissions = service.permissions().list(folderID).execute();
	        for(Permission perm : permissions.getPermissions())
	        {
	        	System.out.println("Permision ID:" +perm.getId()+",Type:"+perm.getType()+",Role:"+perm.getRole());
	        }
	        
	       // 5. Update permision (eg chnage user from writer ->reader)
	        if(!permissions.getPermissions().isEmpty())
	        {
	        	String permissionId = permissions.getPermissions().get(0).getId();
	        	Permission newPermission = new Permission().setRole("reader");
	        	service.permissions().update(folderID, permissionId, newPermission).execute();
	        	System.out.println("Update user role to Reader");
	        }
//	        //6. Remove user permission
//	        if (!permissions.getPermissions().isEmpty()) {
//	        	String permissionId = permissions.getPermissions().get(0).getId();
//	        	service.permissions().delete(folderID, permissionId).execute();
//	        	System.out.println("Removed user access from folder");
//			}
	}

}