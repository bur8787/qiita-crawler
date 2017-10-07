import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;

import java.io.IOException;

public class CrawlService {

    static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.AP_NORTHEAST_1).build();
    static DynamoDBMapper mapper = new DynamoDBMapper(client);

    public void execute() throws InterruptedException, IOException {
        CrawlQueue queue = popQueue();
        CrawlerInterface crawler = buildCrawler(queue);
        CrawlResult result = crawler.crawl(queue);
        updateQueueStatus(result);
        CrawlQueue nextQueue = crawler.createNextQueue(result);
        pushQueue(nextQueue);
    }

    private CrawlerInterface buildCrawler(CrawlQueue queue) {
        String crawlType = queue.getCrawlType();
        switch (crawlType) {
            case "qiita-user-name":
                return new QiitaUserNameCrawler();
            case "qiita-user-contribution":
                return new QiitaUserContributionCrawler();
            default:
                return null;
        }
    }

    private void updateQueueStatus(CrawlResult result) {
        CrawlQueue queue = result.getQueue();
        queue.setStatus(CrawlQueue.Status.SUCCESS);
        mapper.save(queue);
    }

    private static CrawlQueue popQueue() {
        DynamoDBScanExpression expression = new DynamoDBScanExpression();
        PaginatedScanList<CrawlQueue> scan = mapper.scan(CrawlQueue.class,
                expression);
        CrawlQueue targetQueue = getTargetQueue(scan);
        inactiveQueue(targetQueue);
        return targetQueue;
    }

    private static CrawlQueue getTargetQueue(PaginatedScanList<CrawlQueue> scan) {
        CrawlQueue targetQueue = null;
        for (CrawlQueue queue : scan) {
            if (!queue.getStatus().equals(CrawlQueue.Status.READY)) {
                continue;
            }
            if (targetQueue == null
                    || queue.getQueueId() < targetQueue.getQueueId()) {
                targetQueue = queue;
            }
        }
        return targetQueue;
    }

    private static void pushQueue(CrawlQueue queue) {
        if (queue == null) {
            return;
        }
        mapper.save(queue);
    }

    private static void inactiveQueue(CrawlQueue queue) {
        queue.setStatus(CrawlQueue.Status.PROCESSING);
        mapper.save(queue);
    }

}
