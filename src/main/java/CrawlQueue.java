import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

@DynamoDBTable(tableName="crawl_queue")
public class CrawlQueue{

	private long queueId = createQueueId();;
	private String crawlType;
	private Map<String,AttributeValue> parameter;
	private Status status = Status.READY;
	
	public static enum Status{
		READY, PROCESSING ,SUCCESS, FAILED ;
	}

    @DynamoDBHashKey(attributeName="queue_id")  
    public long getQueueId() { return queueId;}
    public void setQueueId(long queueId) {this.queueId = queueId;}

    @DynamoDBAttribute(attributeName="parameter")  
    public Map<String,AttributeValue>  getParameter() { return parameter;}
    public void setParameter(Map<String,AttributeValue> parameter) {this.parameter = parameter;}

    @DynamoDBAttribute(attributeName="crawlType")  
    public String getCrawlType() { return crawlType;}
    public void setCrawlType(String crawlType) {this.crawlType = crawlType;}
    
    @DynamoDBAttribute(attributeName="status")  
    @DynamoDBTypeConvertedEnum
    public Status getStatus() { return status;}
    public void setStatus(Status status) { this.status = status;}

    public AttributeValue  getParameterAttribute(String key) { return this.parameter.get(key);}

	private long createQueueId() {
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyyMMddHHmmssSSS");
		String createdDateString = simpleDateFormat.format(date);
		return Long.parseLong(createdDateString);
	}
}
