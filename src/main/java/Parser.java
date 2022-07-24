import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum DayTime {
    MORNING,
    DAY,
    EVENING
}

public class Parser {

    private static Document  getPage() throws IOException {
        String url = "http://pogoda.spb.ru";
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }

    private static Pattern  pattern = Pattern.compile("\\d{2}\\.\\d{2}"); // Регулярные выражения

    private static String getDateFromString(String stringDate) throws Exception { // 25.07 Понедельник Погода Сегодня
        Matcher matcher = pattern.matcher(stringDate);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new Exception("Cannot extract date from string");
    }

    private static int printPartValues(Elements values, int index) { // index - начало, с которого печатаем
        int iterationCount = 4;
        if (index == 0) {
            Element valueLn = values.get(3);

            int dayTime = 0;

            // TODO: fix iterationCount
            if (valueLn.text().contains("Утро")) {
                iterationCount = 3;
            }

        }
            for (int i = 0; i < iterationCount; i++) {
                Element valueLine = values.get(index + i);
                for (Element td : valueLine.select("td")) {
                    System.out.print(td.text() + "    ");
                }
                System.out.println();
            }
        return iterationCount;
    }
    public static void main (String[] args) throws Exception {
        Document page = getPage();
        // css query language
        Element tableWth = page.select("table[class=wt]").first();
        Elements names = tableWth.select("tr[class=wth]");
        Elements values = tableWth.select("tr[valign=top]");

        int index = 0;
        for(Element name : names) {
            String dateString = name.select("th[id=dt]").text();
            String date = getDateFromString(dateString);
            System.out.println(date + "         Явления         Осадки         Температура         Давл         Влажность         Ветер");
            int iterationCount = printPartValues(values, index);
            index += iterationCount;
        }

    }


}
