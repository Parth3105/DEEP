package in.ac.daiict.deep.constant.response;

public class ResponseMessage {
    public static int UPLOAD_COUNT = 0;

    public static final String USER_NOT_FOUND = "Your information is currently unavailable. Please try again later or contact support.";
    public static final String STUDENT_NOT_FOUND="Student information not found. Ensure the student is registered or data has been uploaded.";
    public static final String USER_NOT_REGISTERED = "Your enrollment status is incomplete. Submission of the preferences is necessary to proceed.";
    public static final String STUDENT_NOT_REGISTERED = "The student appears to be unregistered or has not completed the enrollment process.";
    public static final String UPLOAD_OFFERS = "Warning: Course Data has been updated. Please re-upload Course Offering file to avoid data loss.";
    public static final String NO_FILES_UPLOADED = "Warning: No files were uploaded. Please make sure to select and upload files before submitting.";
    public static final String RESULTS_NOT_FOUND = "No results found!";
    public static final String STUDENT_DATA_NOT_FOUND="Student data is not available for this semester.";
    public static final String COURSE_DATA_NOT_FOUND="Course data is not available.";
    public static final String COURSE_OFFERS_NOT_FOUND="Course offerings is not available";
    public static final String RUN_ALLOCATION_SUCCESS_STATUS ="Success";
    public static final String DOWNLOAD_RESULTS_NOT_FOUND="Allocation process is not completed yet. Please run the allocation to generate results.";
    public static final String DOWNLOADING_ERROR="Download failed due to a server error. Please check system logs for more details.";
    public static final String DOWNLOAD_START="Downloading...";
    public static final String STUDENT_PREFERENCES_NOT_FOUND="Student Preferences Not found";
    public static final String UPLOAD_DATA_NOT_FOUND="Required data files are missing. Please upload all necessary data before running the allocation.";

    public static String getUploadSuccessMessage() {
        return "You're all set! " + UPLOAD_COUNT + " file(s) have been successfully uploaded and saved.";
    }
}