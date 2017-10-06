
public interface CrawlerInterface {
	CrawlResult crawl(CrawlQueue queue);
	CrawlQueue createNextQueue(CrawlResult result);
}
