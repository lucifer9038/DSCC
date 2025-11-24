package gdrivedemo2;

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
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class GDriveJavaApplication {
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

    // --------------------- FILE LISTING ---------------------
    private static void listFiles(Drive service, int pageSize) throws IOException {
        FileList result = service.files().list()
                .setPageSize(pageSize)
                .setFields("nextPageToken, files(id, name)")
                .execute();

        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s)%n", file.getName(), file.getId());
            }
        }
    }

    // --------------------- FILE SEARCH ---------------------
    private static void searchFiles(Drive service, String query) throws IOException {
        String pageToken = null;
        int srNo = 1;
        boolean found = false;
        System.out.println("Search Results:");
        do {
            FileList result = service.files().list()
                    .setQ(query)
                    .setSpaces("drive")
                    .setFields("nextPageToken, files(id, name, createdTime, mimeType)")
                    .setPageToken(pageToken)
                    .execute();

            for (File file : result.getFiles()) {
                found = true;
                System.out.println(srNo++ + ". " + file.getName() + "\t" + file.getId());
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);
        if (!found) {System.out.println("No files found for query: " + query);}
    }

    // --------------------- LIST ROOT FOLDERS ---------------------
    private static List<File> listRootFolders(Drive service) throws IOException {
        String query = "mimeType = 'application/vnd.google-apps.folder' and 'root' in parents";
        FileList result = service.files().list()
                .setQ(query)
                .setSpaces("drive")
                .setFields("files(id, name)")
                .execute();

        List<File> folders = result.getFiles();
        int srNo = 0;
        System.out.println("Root Folders:");
        for (File folder : folders) {
            System.out.println(srNo++ + ". " + folder.getName() + "\t" + folder.getId());
        }
        return folders;
    }
    // --------------------- LIST SUBFOLDERS ---------------------
    private static void listSubFolders(Drive service, String parentFolderId) throws IOException {
        String query = "mimeType = 'application/vnd.google-apps.folder' and '" + parentFolderId + "' in parents";
        FileList result = service.files().list()
                .setQ(query)
                .setSpaces("drive")
                .setFields("files(id, name)")
                .execute();

        int srNo = 0;
        System.out.println("SubFolders:");
        for (File subFolder : result.getFiles()) {
            System.out.println(srNo++ + ". " + subFolder.getName() + "\t" + subFolder.getId());
        }
    }

    // --------------------- CREATE FOLDER ---------------------
    private static File createFolder(Drive service, String folderName, String parentFolderId) throws IOException {
        File folderMetadata = new File();
        folderMetadata.setName(folderName);
        folderMetadata.setMimeType("application/vnd.google-apps.folder");

        if (parentFolderId != null) {
            folderMetadata.setParents(Collections.singletonList(parentFolderId));
        }

        File createdFolder = service.files().create(folderMetadata)
                .setFields("id, name, parents")
                .execute();

        System.out.println("Folder Created : " + createdFolder.getName() + " under dir " + createdFolder.getParents() + " (ID: " + createdFolder.getId() + ")");
        return createdFolder;
    }

    // --------------------- UPLOAD FILE ---------------------
    private static File uploadFile(Drive service, String localFilePath, String mimeType,
                                   String uploadFileName, String parentFolderId) throws IOException {
        java.io.File localFile = new java.io.File(localFilePath);
        try (FileInputStream fis = new FileInputStream(localFile)) {
            AbstractInputStreamContent uploadStreamContent = new InputStreamContent(mimeType, fis);

            File fileMetadata = new File();
            fileMetadata.setName(uploadFileName);
            if (parentFolderId != null) {
                fileMetadata.setParents(Collections.singletonList(parentFolderId));
            }

            File uploadedFile = service.files()
                    .create(fileMetadata, uploadStreamContent)
                    .setFields("id, name, webContentLink, webViewLink, parents")
                    .execute();

            System.out.println("File Uploaded: " + uploadedFile.getName());
            System.out.println("WebContentLink: " + uploadedFile.getWebContentLink());
            System.out.println("WebViewLink: " + uploadedFile.getWebViewLink());
            return uploadedFile;
        }
    }

    // --------------------- MAIN ---------------------
    public static void main(String[] args) throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = getCredentials(HTTP_TRANSPORT);

        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName("GDrive Access")
                .build();

        System.out.println("----" + service.getApplicationName() + "----");

        // 1. List first 2 files
        listFiles(service, 2);

        // 2. Search files
        searchFiles(service, "name contains 'MyGDriveJavaFile'");

        // 3. Show root folders & pick one
        List<File> rootFolders = listRootFolders(service);
        Scanner input = new Scanner(System.in);
        System.out.println("Enter serial number of folder to see subfolders:");
        int choice = input.nextInt();
        String parentFolderId = rootFolders.get(choice).getId();

        // 4. List subfolders of chosen folder
        listSubFolders(service, parentFolderId);

        // 5. Create new folder under root
        File newFolder = createFolder(service, "FolderUsingJava", null);

        // 6. Upload file inside new folder
        uploadFile(service,
                "/Users/priteshbhuravane/MCA/Sem3/Practical.pdf",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "MyGDriveJavaFile.docx",
                newFolder.getId());
    }
}