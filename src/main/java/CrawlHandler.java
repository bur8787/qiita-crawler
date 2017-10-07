import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.IOException;

public class CrawlHandler implements RequestHandler<Object, Object> {

  public static void main(String[] args) {
    CrawlHandler crawlHandler = new CrawlHandler();
    crawlHandler.handleRequest(null, null);
  }

  @Override
  public Object handleRequest(Object input, Context context) {
    CrawlService crawlService = new CrawlService();
    try {
      crawlService.execute();
    } catch (InterruptedException | IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}
