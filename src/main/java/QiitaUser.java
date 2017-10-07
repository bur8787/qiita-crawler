import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "qiita_user")
public class QiitaUser {

  private int createdAt;
  private String userId;
  private int contributionCount;

  @DynamoDBHashKey(attributeName = "created_at")
  public int getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(int createdAt) {
    this.createdAt = createdAt;
  }

  @DynamoDBRangeKey(attributeName = "user_id")
  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  @DynamoDBAttribute(attributeName = "contribution_count")
  public int getContributionCount() {
    return contributionCount;
  }

  public void setContributionCount(int contributionCount) {
    this.contributionCount = contributionCount;
  }
}
