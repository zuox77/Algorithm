package Interview.Airbnb;

/*
https://www.1point3acres.com/bbs/thread-1108723-1-1.html
https://www.1point3acres.com/bbs/thread-1140063-1-1.html

例子里就是用string的形式表示一个transaction里面都包含什么信息，可能是payment也可能是refund，payment有type，amount，id这类的信息
refund的话有link to payment transaction，amount的信息。
用什么数据结构存可以自己决定

三条规则：
1: Any refund should be issued in full for a payment before considering next payment.
2: Refund will be prioritized among different payment methods based on such order: CREDIT, CREDIT_CARD, PAYPAL.
3: Refund should be issued to more recent payment.

Example 1

Transactions:
Payment1: Credit, 2023-01-10, $40
Payment2: PayPal, 2023-01-15, $60

Input:
Refund amount: $50
Output:
Refund a: linked to Payment1, Credit, $40
Refund b: linked to Payment2, PayPal, $10

Example 2

Transactions:
Payment1: Credit, 2023-01-15, $40
Payment2: PayPal, 2023-01-10, $60
Payment3: PayPal, 2023-01-20, $40
Refund1: linked to Payment1, $20

Input:
Refund amount: $50
Output:
refund a: linked to Payment1, credit, $20
refund b: linked to Payment2, PayPal, $30
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Refund {

    private static final String PAYMENT = "Payment";
    private static final String REFUND = "Refund";
    private static final Map<String, Integer> TYPEMAP = Map.of(
            "CREDIT", 1,
            "CREDIT_CARD", 2,
            "PAYPAL", 3
    );
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private final PriorityQueue<Payment> heap;

    public static class Payment {
        int id;
        String name;
        String type;
        Date date;
        int amount;

        public Payment(int id, String name, String type, Date date, int amount) {
            this.id = id;
            this.type = type;
            this.name = name;
            this.date = date;
            this.amount = amount;
        }
    }

    public Refund(List<String> transactions) throws ParseException {
        // Initialize heap
        heap = new PriorityQueue<>(transactions.size(), (o1, o2) -> {
            // Compare transaction type
            if (!o1.type.equalsIgnoreCase(o2.type)) return TYPEMAP.get(o1.type) - TYPEMAP.get(o2.type);
            // Compare date
            if (o1.date.compareTo(o2.date) != 0) return o1.date.compareTo(o2.date);
            // Compare amount
            if (o1.amount != o2.amount) return o2.amount - o1.amount;
            // Compare id
            return o1.id - o2.id;
        });
        // Initialize payment map
        Map<String, Payment> paymentMap = new HashMap<>();
        // Get full info of each transaction
        for (String transaction: transactions) {
            /*
            Example: Payment1: Credit, 2023-01-10, $40
            Example: Refund1: linked to Payment1, $20
             */
            // Get transaction type and id
            String[] tmp = transaction.split(": "); // ["Payment1", "Credit, 2023-01-10, $40"]
            String type = tmp[0].trim().substring(0, tmp[0].length() - 1); // Payment
            int id = Integer.parseInt(tmp[0].trim().substring(tmp[0].length() - 1)); // 1
            String name = tmp[0].trim(); // Payment1
            String[] tmp2;
            int amount;
            // If it's a payment
            if (type.equalsIgnoreCase(PAYMENT)) {
                tmp2 = tmp[1].split(", "); // ["Credit", "2023-01-10", "$40"]
                String paymentType = tmp2[0].trim().toUpperCase(); // CREDIT
                Date date = DATE_FORMAT.parse(tmp2[1].trim()); // 2023-01-10
                amount = Integer.parseInt(tmp2[2].trim().split("\\$")[1]); // 40
                paymentMap.put(name, new Payment(id, name, paymentType, date, amount));
                // If it's a refund
            } else if (type.equalsIgnoreCase(REFUND)) {
                tmp2 = tmp[1].split(" ");
                String paymentStr = tmp2[2].substring(0, tmp2[2].length() - 1);
                amount = Integer.parseInt(tmp2[3].substring(1));
                Payment payment = paymentMap.get(paymentStr);
                payment.amount -= amount;
            }
        }
        // Store to heap
        for (Payment payment: paymentMap.values()) {
            heap.offer(payment);
        }
    }

    public List<String> newRefund(String refundStr) {
        /*
        Example: Refund amount: $50
         */
        int amount = Integer.parseInt(refundStr.split(": ")[1].trim().substring(1));

        // Get result
        List<String> ans = new ArrayList<>();
        int count = 1;
        while (!heap.isEmpty() && amount > 0) {
            // Get payment
            Payment payment = heap.poll();
            /*
            Example:
            refund 1: linked to Payment1, credit, $20
            refund 2: linked to Payment2, PayPal, $30
             */
            if (amount >= payment.amount) {
                amount -= payment.amount;
                ans.add(REFUND + " " + count + ": linked to " + payment.name + ", " + payment.type + ", " + "$" + payment.amount);
            } else {
                ans.add(REFUND + " " + count + ": linked to " + payment.name + ", " + payment.type + ", " + "$" + amount);
                payment.amount -= amount;
                amount = 0;
                // Put back
                heap.offer(payment);
            }
            count++;
        }
        if (amount > 0) {
            ans.add("Pending " + REFUND + " : " + "$" + amount);
        }
        return ans;
    }
}

/*
        List<String> payments = Arrays.asList(
                "Payment1: Credit, 2023-01-15, $40",
                "Payment2: PayPal, 2023-01-10, $60",
                "Payment3: PayPal, 2023-01-20, $40",
                "Refund1: linked to Payment1, $20"
        );
        String input = "Refund amount: $1000";
        Refund refund = new Refund(payments);
        for (String result: refund.newRefund(input)) {
            System.out.println(result);
        }
 */
