package pk.com.Taj.app.beans;

public class RewardPointDetail {
    private String RewardPoint;
    private String RewardDescription;

    public RewardPointDetail() {

    }

    public RewardPointDetail(String rewardPoint, String rewardDescription) {
        RewardPoint = rewardPoint;
        RewardDescription = rewardDescription;
    }

    public String getRewardPoint() {
        return RewardPoint;
    }

    public void setRewardPoint(String rewardPoint) {
        RewardPoint = rewardPoint;
    }

    public String getRewardDescription() {
        return RewardDescription;
    }

    public void setRewardDescription(String rewardDescription) {
        RewardDescription = rewardDescription;
    }
}
