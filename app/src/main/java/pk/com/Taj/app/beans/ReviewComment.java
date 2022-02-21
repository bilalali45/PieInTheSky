package pk.com.Taj.app.beans;

public class ReviewComment {
    private String CommentsId;
    private String ProfileImageURL;
    private String FullName;
    private String SubDetail;
    private String CommentsText;
    private String CommentsDate;
    private String CommentsDays;
    private int TotalLike;
    private String UserId;

    public String getCommentsId() {
        return CommentsId;
    }

    public void setCommentsId(String commentsId) {
        CommentsId = commentsId;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getSubDetail() {
        return SubDetail;
    }

    public void setSubDetail(String subDetail) {
        SubDetail = subDetail;
    }

    public String getCommentsText() {
        return CommentsText;
    }

    public void setCommentsText(String commentsText) {
        CommentsText = commentsText;
    }

    public String getCommentsDate() {
        return CommentsDate;
    }

    public void setCommentsDate(String commentsDate) {
        CommentsDate = commentsDate;
    }

    public int getTotalLike() {
        return TotalLike;
    }

    public void setTotalLike(int totalLike) {
        TotalLike = totalLike;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getProfileImageURL() {
        return ProfileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        ProfileImageURL = profileImageURL;
    }

    public String getCommentsDays() {
        return CommentsDays;
    }

    public void setCommentsDays(String commentsDays) {
        CommentsDays = commentsDays;
    }
}
