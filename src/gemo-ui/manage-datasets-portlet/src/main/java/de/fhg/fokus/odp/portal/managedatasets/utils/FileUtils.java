package de.fhg.fokus.odp.portal.managedatasets.utils;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liferay.faces.bridge.model.UploadedFile;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.documentlibrary.DuplicateFileException;
import com.liferay.portlet.documentlibrary.FileSizeException;
import com.liferay.portlet.documentlibrary.NoSuchFileEntryException;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFolderLocalServiceUtil;

/**
 * static and stateless util class to work with the local file system
 * 
 * @author dsc
 * 
 */
public class FileUtils {

	/** The logger. */
	private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

	/**
	 * uploads a file to the user specific liferay storage you must define
	 * LiferayFacesContext and the current ThemeDisplay to use this typing:
	 * 
	 * <pre>
	 * <code>
	 * 		LiferayFacesContext lfc = LiferayFacesContext.getInstance();
	 * 	ThemeDisplay themeDisplay = lfc.getThemeDisplay();
	 *    	
	 * 		//upload file to local liferay storage
	 * 	DLFileEntry fileEntry = FileUtils.uploadFileToFolder(...
	 * 	...
	 * </code>
	 * </pre>
	 * 
	 * @param file
	 * @param folderName
	 * @param themeDisplay
	 * @return
	 * @throws SystemException
	 * @throws IOException
	 * @throws PortalException
	 * @throws Exception
	 */
	public static DLFileEntry uploadFileToFolder(UploadedFile file,
			final String folderName, ThemeDisplay themeDisplay)
			throws SystemException, PortalException, IOException {
		// create the Liferay instances and contexts

		ServiceContext serviceContext = new ServiceContext();
		long repositoryID = themeDisplay.getScopeGroupId();
		// long groupID = themeDisplay.getLayout().getGroupId();
		serviceContext.setScopeGroupId(repositoryID);
		long userID = themeDisplay.getUserId();
		String userName = themeDisplay.getUser().getScreenName();

		// get the folder (create if not exist)
		DLFolder folder = getFolder(folderName);
		LOG.debug("Uploaded file metadata: " + file.getAbsolutePath()
				+ " id :  " + file.getId());
		// get a valid and system wide file name for the user
		final String fileName = NameUtils.UsersUsableUniqueNamefromFile(file);

		// update or add fileEntry
		return createUpdateFileEntry(folder.getFolderId(), repositoryID,
				fileName, userID, file,
				MimeTypesUtil.getContentType(file.getName()) + "-file"
						+ " for user " + userName + "' with user-id " + userID,
				serviceContext);
	}

	/**
	 * returns the Liferay folder ID by the name of the target folder (ID over
	 * Portal Properties)
	 * 
	 * @param propsUtilName
	 * @return
	 * @throws SystemException
	 * @throws Exception
	 */
	public static DLFolder getFolder(String folderPropsUtilName)
			throws SystemException {
		long folderId = Long.parseLong(folderPropsUtilName);
		DLFolder folder;
		try {
			/* Check if folder for file exists */
			folder = DLFolderLocalServiceUtil.getDLFolder(folderId);
			LOG.debug("Folder for files with propsUtilname "
					+ folderPropsUtilName + " exists.");
			return folder;
		} catch (PortalException e) {
			LOG.debug("Folder with propsUtilname '" + folderPropsUtilName
					+ "' does not exist, creating...");

		}
		folder = DLFolderLocalServiceUtil.createDLFolder(folderId);
		folder.setName(folderPropsUtilName);
		folder = DLFolderLocalServiceUtil.addDLFolder(folder);
		return folder;
	}

	/**
	 * creates or updates the file entry with the Liferay context
	 * 
	 * @param fileEntry
	 * @param folderId
	 *            , the primary key of the file entry's parent folder
	 * @param repositoryID
	 *            , the primary key of the file entry's repository
	 * @param fileName
	 * @param userID
	 *            , the primary key of the file entry's creator/owner
	 * @param file
	 * @param fileDescription
	 *            , description, the file's description
	 * @param serviceContext
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 * @throws IOException
	 */
	public static DLFileEntry createUpdateFileEntry(long folderId,
			long repositoryID, String fileName, long userID, UploadedFile file,
			String fileDescription, ServiceContext serviceContext)
			throws PortalException, SystemException, IOException {

		// first, try to get the existing file/item:
		DLFileEntry entry = null;
		try {
			entry = DLFileEntryLocalServiceUtil.getFileEntry(repositoryID, /* folderId */
					0, fileName);
			// if it exist, remove
			if (entry != null)
				DLFileEntryLocalServiceUtil.deleteDLFileEntry(entry);
		} catch (NoSuchFileEntryException noSuchFileEntryException) {
			LOG.debug("No such fileEntry with filename " + fileName
					+ " (repositoryID:" + repositoryID + ",folderID:"
					+ folderId + "). Creating file now...");
		}

		LOG.debug("Trying to add file with parameters: " + "folder-id"
				+ folderId + "file bytes array length: "
				+ file.getBytes().length + "dl file max size: "
				+ PrefsPropsUtil.getLong(PropsKeys.DL_FILE_MAX_SIZE));
		if ((PrefsPropsUtil.getLong(PropsKeys.DL_FILE_MAX_SIZE) > 0)
				&& ((file.getBytes() == null) || (file.getBytes().length > PrefsPropsUtil
						.getLong(PropsKeys.DL_FILE_MAX_SIZE)))) {
			throw new FileSizeException(fileName);
		}
		// ad the new one
		addFileEntry(folderId, fileName, file, serviceContext, repositoryID,
				userID, fileDescription);
		entry = DLFileEntryLocalServiceUtil.getFileEntry(repositoryID, /* folderId */
				0, fileName);
		return entry;
	}

	/**
	 * adds a file entry to Liferay context
	 * 
	 * @param folderId
	 *            , the primary key of the file entry's parent folder
	 * @param fileName
	 *            as sourceFileName, the original file's name
	 * @param file
	 *            , bytes, the file's data (optionally null)
	 * @param serviceContext
	 *            , the service context to be applied.
	 * @param repositoryID
	 *            ,repositoryId, the primary key of the file entry's repository
	 * @param userID
	 *            ,userId, the primary key of the file entry's creator/owner
	 * @param fileDescription
	 *            , description, the file's description
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 * @throws IOException
	 */
	private static FileEntry addFileEntry(long folderId, String fileName,
			UploadedFile file, ServiceContext serviceContext,
			long repositoryID, long userID, String fileDescription)
			throws PortalException, SystemException, IOException {
		FileEntry fileEntry = null;
		try {
			// fileEntry = DLAppLocalServiceUtil.getFileEntry(groupId, folderId,
			// title)
			fileEntry = DLAppLocalServiceUtil.addFileEntry(userID, // userId,
																	// the
																	// primary
																	// key of
																	// the file
																	// entry's
																	// creator/owner
					repositoryID, // repositoryId, the primary key of the file
									// entry's repository
					folderId, // folderId, the primary key of the file entry's
								// parent folder
					fileName, // sourceFileName, the original file's name
					MimeTypesUtil.getContentType(file.getName()), // mimeType
																	// the
																	// file's
																	// MIME type
					fileName, // title, the name to be assigned to the file
								// (optionally null )
					fileDescription, // description, the file's description
					"new", // changeLog, the file's version change log
					file.getBytes(), // bytes, the file's data (optionally null)
					serviceContext // ServiceContext, the service context to be
									// applied.
					);
			LOG.debug("Added file '" + fileName + "' with" + " fileEntry-Id: "
					+ fileEntry.getFileEntryId() + ", repository-ID: "
					+ fileEntry.getRepositoryId() + ", folderID: "
					+ fileEntry.getFolderId() + ", group-ID: "
					+ fileEntry.getGroupId());

		} catch (DuplicateFileException e) {
			LOG.debug("Duplicated File '" + fileName + "' with"
					+ ", repository-ID: " + repositoryID + ", folderID: "
					+ folderId + ", group-ID: ?, start updating it...");
			throw e;
		}
		return fileEntry;
	}

}
