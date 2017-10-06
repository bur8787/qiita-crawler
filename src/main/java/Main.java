public class Main{

//	private static String BASE_URL = "http://qiita.com/";
//	private static String API_URL = "api/v2/users/";
//	private static List<String> USER_ID_INITIAL = Arrays.asList("A", "B", "C",
//			"D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
//			"Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2",
//			"3", "4", "5", "6", "7", "8", "9", "_");
//	private static int SLEEP_MILLIS = 4000;
//	private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder
//			.standard().withRegion(Regions.AP_NORTHEAST_1).build();
//	private static DynamoDB dynamoDB = new DynamoDB(client);
//	private static String QIITA_TOKEN = System.getenv("QIITA_TOKEN");
//
//
//	public Object handleRequest(Object request, Context contexst) {
//		try {
//			main(null);
//		} catch (IOException | InterruptedException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public static void main(String[] args) throws IOException,
//			InterruptedException {
//		List<String> userList = createUserList();
//		Map<String, QiitaUser> userInfoMap = fetchUserInfo(userList);
//		saveUserInfo(userInfoMap);
//	}
//
//	private static List<String> createUserList() throws InterruptedException,
//			IOException {
//		LinkedList<String> userList = new LinkedList<String>();
//		for (String initial : USER_ID_INITIAL) {
//			int count = 1;
//			boolean existUser = true;
//			while (existUser) {
//				String url = BASE_URL + "users?char=" + initial + "&page="
//						+ count;
//				Document listDocument = fetchUrl(url);
//				Elements elements = listDocument
//						.select("#main > div > div > div.col-sm-9 > div.js-hovercard");
//				if (elements.isEmpty()) {
//					break;
//				}
//				for (Element element : elements) {
//					String userId = element.attr("data-hovercard-target-name");
//					userList.add(userId);
//					System.out.println("createUserList:" + userId);
//				}
//				count++;
//			}
//		}
//		return userList;
//	}
//
//	private static Map<String, QiitaUser> fetchUserInfo(List<String> userList)
//			throws InterruptedException, IOException {
//		HashMap<String, QiitaUser> userMap = new HashMap<String, QiitaUser>();
//		for (String userId : userList) {
//			QiitaUser user = new QiitaUser();
//			user.setUserId(userId);
//			int contributionCount = fetchContributionCount(userId);
//			user.setContributionCount(contributionCount);
//			String otherInfo = fetchUserAPI(userId);
//			user.setOtherInfo(otherInfo);
//			userMap.put(userId, user);
//			System.out.println("fetchUserInfo:" + userId);
//		}
//		return userMap;
//	}
//
//	private static int fetchContributionCount(String userId)
//			throws InterruptedException, IOException {
//		String userPage = BASE_URL + userId;
//		Document userDocument = fetchUrl(userPage);
//		Elements activityElements = userDocument
//				.select("span.userActivityChart_statCount");
//		String text = activityElements.get(1).text();
//		int contributionCount = Integer.parseInt(text);
//		return contributionCount;
//	}
//
//	private static String fetchUserAPI(String userId)
//			throws InterruptedException, IOException {
//		String userPage = BASE_URL + API_URL + userId;
//		Document userDocument = fetchAPI(userPage);
//		String userApiResponse = userDocument.text();
//		return userApiResponse;
//	}
//
//	private static void saveUserInfo(Map<String, QiitaUser> userInfoMap) {
//		Calendar calendar = Calendar.getInstance();
//		Date date = calendar.getTime();
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
//		String createdDateString = simpleDateFormat.format(date);
//		int createdDate = Integer.parseInt(createdDateString);
//		Table table = dynamoDB.getTable("qiita_user");
//		Item item = new Item();
//		Set<Entry<String, QiitaUser>> entrySet = userInfoMap.entrySet();
//		for (Entry<String, QiitaUser> entry : entrySet) {
//			QiitaUser user = entry.getValue();
//			String userId = user.getUserId();
//			int contributionCount = user.getContributionCount();
//			String otherInfo = user.getOtherInfo();
//			item = new Item()
//					.withPrimaryKey("created_at", createdDate, "user_id",
//							userId)
//					.withInt("contribution_count", contributionCount)
//					.withString("other_info", otherInfo);
//			table.putItem(item);
//		}
//	}
//
//	private static Document fetchUrl(String url) throws InterruptedException,
//			IOException {
//		Document document = Jsoup.connect(url).ignoreContentType(true).get();
//		Thread.sleep(SLEEP_MILLIS);
//		return document;
//	}
//
//	private static Document fetchAPI(String url) throws InterruptedException,
//			IOException {
//		Document document = Jsoup.connect(url).ignoreContentType(true)
//				.header("Authorization", "Bearer " + QIITA_TOKEN).get();
//		Thread.sleep(SLEEP_MILLIS);
//		return document;
//	}
}