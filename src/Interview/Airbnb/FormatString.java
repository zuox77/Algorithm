package Interview.Airbnb;

/*
Articles = [
  "This is a short article.",
  "Now for a longer article. This one has a lot of text.",
  "Another article with medium length."
]

width = 55
 +-------------------------------------------------------+
 |This is a short article.                               |
 +-------------------------------------------------------+
 |Now for a longer article. This one has a lot of text.  |
 +-------------------------------------------------------+
 |Another article with medium length.                    |
 +-------------------------------------------------------+

Follow-up:
 width = 12
 +------------+
 |This is a   |
 |short       |
 |article.    |
 +------------+
 |Now for a   |
 |longer      |
 |article.    |
 |This one has|
 |a lot of    |
 |text.       |
 +------------+
 |Another     |
 |article with|
 |medium      |
 |length.     |
 +------------+
 */

public class FormatString {
    public static void print(String[] articles, int width) {
        // Check width
        boolean widthShorter = false;
        for (String article : articles) {
            if (article.length() > width) {
                widthShorter = true;
                break;
            }
        }
        if (!widthShorter) printWithBoarder(articles, width);

        // Get boarder
        StringBuilder sb = new StringBuilder();
        sb.append("+").repeat("-", width).append("+");
        String boarder = sb.toString();

        for (String article : articles) {
            // Print boarder
            System.out.println(boarder);
            // Reset sb and count
            sb.setLength(0);
            int count = 0;

            sb.append("|");
            for (String word : article.split(" ")) {
                int wordLength = word.length();
                // Need new line
                if (count + wordLength > width) {
                    sb.repeat(" ", width - count).append("|");
                    System.out.println(sb);
                    // Reset
                    sb.setLength(0);
                    sb.append("|");
                    count = 0;
                }
                sb.append(word);
                if (count + wordLength < width) {
                    sb.append(" ");
                    count++;
                }
                count += wordLength;
            }
            sb.repeat(" ", width - count).append("|");
            System.out.println(sb);
        }
        System.out.println(boarder);
    }

    public static void printWithBoarder(String[] articles, int width) {
        StringBuilder sbBoarder = new StringBuilder();
        sbBoarder.append("+").repeat("-", width).append("+");
        StringBuilder sbContent = new StringBuilder();
        for (String article : articles) {
            // Print boarder
            System.out.println(sbBoarder);
            sbContent.setLength(0);
            // Get length
            int len = article.length();
            sbContent.append("|").append(article).repeat(" ", width - len).append("|");
            System.out.println(sbContent);
        }
        System.out.println(sbBoarder);
    }
}

/*
       String[] articles = new String[] {
               "This is a short article.",
               "Now for a longer article. This one has a lot of text.",
               "Another article with medium length."
       };
       int width = 12;
       Solution.print2(articles, width);
*/
