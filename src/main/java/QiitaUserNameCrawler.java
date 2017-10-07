import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class QiitaUserNameCrawler implements CrawlerInterface {

  private static String BASE_URL = "http://qiita.com/";
  private static List<String> USER_ID_INITIAL = Arrays.asList("A", "B", "C",
      "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
      "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2",
      "3", "4", "5", "6", "7", "8", "9", "_");

  public CrawlResult crawl(CrawlQueue queue) {
    String initialChar = queue.getParameterAttribute("initial_char").getS();
    int pageNo = Integer.parseInt(queue.getParameterAttribute("page_no")
        .getN());
    List<QiitaUser> qiitaUserList = null;
    try {
      List<String> userList = fetchUser(initialChar, pageNo);
      qiitaUserList = mapQiitaUser(userList);
      saveQiitaUser(qiitaUserList);
    } catch (InterruptedException | IOException e) {
      e.printStackTrace();
    }
    int crawlCount;
    if (qiitaUserList == null || qiitaUserList.isEmpty()) {
      crawlCount = 0;
    } else {
      crawlCount = qiitaUserList.size();
    }
    CrawlResult result = new CrawlResult();
    result.setQueue(queue);
    result.setCrawlCount(crawlCount);
    return result;
  }


  public CrawlQueue createNextQueue(CrawlResult result) {
    CrawlQueue nextQueue = new CrawlQueue();
    HashMap<String, AttributeValue> parameterMap = new HashMap<String, AttributeValue>();
    String initialChar = result.getQueue()
        .getParameterAttribute("initial_char").getS();
    int pageNo = Integer.parseInt(result.getQueue()
        .getParameterAttribute("page_no").getN());
    if (result.getCrawlCount() == 0) {
      String nextChar = nextChar(initialChar);
      if (nextChar == null) {
        return null;
      }
      AttributeValue value1 = new AttributeValue();
      value1.setS(nextChar);
      parameterMap.put("initial_char", value1);
      AttributeValue value2 = new AttributeValue();
      value2.setN("1");
      parameterMap.put("page_no", value2);
    } else {
      parameterMap.put("initial_char", result.getQueue()
          .getParameterAttribute("initial_char"));
      AttributeValue value2 = new AttributeValue();
      pageNo += 1;
      String pageNoStr = String.valueOf(pageNo);
      value2.setN(pageNoStr);
      parameterMap.put("page_no", value2);
    }
    nextQueue.setParameter(parameterMap);
    return nextQueue;
  }


  private static List<String> fetchUser(String initialChar, int pageNo)
      throws InterruptedException, IOException {
    LinkedList<String> userList = new LinkedList<String>();
    String url = BASE_URL + "users?char=" + initialChar + "&page=" + pageNo;
    Document listDocument = fetchUrl(url);
    Elements elements = listDocument
        .select("#main > div > div > div.col-sm-9 > div.js-hovercard");
    for (Element element : elements) {
      String userId = element.attr("data-hovercard-target-name");
      userList.add(userId);
      System.out.println("createUserList:" + userId);
    }
    return userList;
  }

  private static Document fetchUrl(String url) throws InterruptedException,
      IOException {
    Document document = Jsoup.connect(url).ignoreContentType(true).get();
    return document;
  }

  private static void saveQiitaUser(List<QiitaUser> qiitaUserList) {
    for (QiitaUser qiitaUser : qiitaUserList) {
      CrawlService.mapper.save(qiitaUser);
    }
  }

  private static List<QiitaUser> mapQiitaUser(List<String> userList) {
    List<QiitaUser> qiitaUserList = new ArrayList<QiitaUser>();
    for (String userId : userList) {
      QiitaUser qiitaUser = new QiitaUser();
      qiitaUser.setUserId(userId);
      int createdAt = createdAt();
      qiitaUser.setCreatedAt(createdAt);
      qiitaUserList.add(qiitaUser);
    }
    return qiitaUserList;
  }

  private static int createdAt() {
    Calendar calendar = Calendar.getInstance();
    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    while (dayOfWeek != Calendar.MONTH) {
      calendar.add(Calendar.DATE, -1);
      dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    }
    Date date = calendar.getTime();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    String createdDateString = simpleDateFormat.format(date);
    return Integer.parseInt(createdDateString);
  }

  public static String nextChar(String presentChar) {
    for (int i = 0; i < USER_ID_INITIAL.size(); i++) {
      if (presentChar.equals(USER_ID_INITIAL.get(i))) {
        if (i == USER_ID_INITIAL.size() - 1) {
          return null;
        }
        return USER_ID_INITIAL.get(i + 1);
      }
    }
    return null;
  }

}
