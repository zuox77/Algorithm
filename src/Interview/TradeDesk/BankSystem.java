package Interview.TradeDesk;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.TreeMap;

/*
L1:
(所有的timestamp, 现在都用不到,L3开始才用的到)
>>Create account (Timestamp, id)
Create成功就返回true; Create失败就返回false.
>>Deposit(timestamp, id, amout): 成功返回余额 不成功返回Optional.Empty(),
成功返回Optional.of(余额),不成功是Empty 不成功的理由主要是account不存在
>>Transfer(timestamp, sourceAccountId, targetAccountId, amount)
成功返回Optional.(source的余额), 不成功返回Empty. 不成功的理由主要是, source和target不合法/不存在, amount大于Source的余额

L2:
当账户里面转出去钱的时候, 算做Spending(deposit不算).
Implement: >>List<String>(accountIds) topNSpenders(timestamp, N)
没啥好说的, 在account里面多track一个spending, 每次少钱的时候就加spending.
最后导出来一个sort by spending, 找出N个top, [这里我一开始想的是用minheap, 后来发现这个N是可变的...直接sort就可以]

L3:
开始有点意思了, 要求你做一个SchedulePayment, 要implement两个, 还要维持一个<全局>唯一递增的PaymentID. PaymentId = "payment{number}", number 是全局递增的, 从1开始, 每次schedule的时候+1.
注意啊, 这里面的payment是纯Spending, 不是转账, 就直接没了.
>> String(paymentId) SchedulePayment(timestamp, sourceId, amount, delay)
返回一个paymentId. 要求transfer发生在timestamp+delay的瞬间 (而且在任何其他transaction之前, 优先处理)
Schedule的时候不检查Balance, 只有transfer发生的时候才检查.
>> boolean cancelpayment(timestamp, id, PaymentId)
只有成功取消了之后, 才返回True.
PaymentId不存在, transfer已经发生, accountId和transaction不符, accountId不存在, 等等原因一律返回false.
>> 额外要求, 如果transfer在同一时间发生, 按照paymentId从小到大开始处理 (有testcase), 处理transfer的时候, 如果余额不足, 就略过.
关于L3, 我谈一下我的思路.
Transaction 需要建一个class, 里面存, id, amount, id, 一个boolean用来存是否cancelled, 还有transfer预定发生的时间.
建一个minheapPQ, 把所有的Transaction丢进去, 按照tranfer时间, 然后是paymentId进行一个排序.
然后建一个Map<PaymentId, Transaction>, 这样可以在cancel的时候快速更改里面的那个是否cancelled的boolean.
建一个private transfer(timestamp), 从PQ里面一个个往外蹦, 如果没被cancel就处理 直到 transfertime <= timestamp的全部处理完.
然后把这个transfer(timestamp)塞到其他基本每个method的最前面(前面的timestamp用上了)
L4
要求implement两个东西:
>> boolean MergeAccount(Timestamp, account1, account2)
要求accout1继承account2的所有东西, 然后account2删掉, 包括spending (for topNspender)
>> Optional<Integer> getBalance(timestamp, id, atTime)
这个很难了 要求给出在atTime的时候, accountId所属Account的balance history.
这就意味着我们要记录所有的history.
而且还有一条, 如果account 是从别的地方merge过来的, 还要继承之前所有merge进来的account的balance.
(这题我写完了, 因为这部分API不太熟悉, 没调试通, 拿了一半左右的test case)
*思路: Merge account 我就不说了
AccountHistory我选择用TreeMap做, <Timestamp, Balance> 每次balance发生变化的时候, 记录一下.
然后可以用Treemap.floorEntry去拿那个时间点的值
问题来了, 如果是account被merge怎么办, 我的想法是在account里面在加一个List, 里面存着以前所有其他账户的TreeMap(history), 然后把这些Treemap的最后一个值都改成0.
在求Balance的时候对本身的history先索引floorEntry, 然后对所有的history进行索引floorEntry, 最后把他们加在一起, 应该就是一个历史的值.
当然 最后我这题没做完, 也没全对, 只能说个想法
 */
public class BankSystem {

    private static final Comparator<Account> spendingComparator =
            new Comparator<Account>() {
                @Override
                public int compare(Account o1, Account o2) {
                    if (o1.spent != o2.spent) return o2.spent - o1.spent;
                    return o1.id.compareTo(o2.id);
                }
            };
    private static final Comparator<Payment> cashbackComparator =
            new Comparator<Payment>() {
                @Override
                public int compare(Payment o1, Payment o2) {
                    if (o1.scheduledTime != o2.scheduledTime)
                        return o1.scheduledTime - o2.scheduledTime;
                    return o1.paymentId.compareTo(o2.paymentId);
                }
            };
    private static final Map<String, Account> accounts = new HashMap<>();
    private static final Map<String, Payment> allPayments = new HashMap<>();
    private static final PriorityQueue<Payment> sortedPendingCashbacks =
            new PriorityQueue<>(cashbackComparator);
    private static final int ONE_DAY_IN_MILLISECONDS = 86400000;
    private static final double CASHBACK_RATE = 0.02;
    private static final String PAYMENT_ID_PREFIX = "payment";
    private int globalPaymentId;

    public BankSystem() {
        this.globalPaymentId = 1;
    }

    private void executeCashback(int timeStamp) {
        // 如果pendingCashbacks为空,直接返回
        if (sortedPendingCashbacks.isEmpty()) return;
        // 将小于当前的timeStamp的所有cashback都完成
        while (!sortedPendingCashbacks.isEmpty()
                && sortedPendingCashbacks.peek().scheduledTime <= timeStamp) {
            Payment payment = sortedPendingCashbacks.poll();
            Account account = accounts.get(payment.id);
            account.balance += payment.cashback;
            // 更新payment状态
            payment.cashbackStatus = CashbackStatus.CASHBACK_RECEIVED;
        }
    }

    /*
    L1
     */
    public boolean createAccount(int timeStamp, String accountId) {
        // 完成cashback
        executeCashback(timeStamp);
        // 检查账号是否存在,存在则返回false
        if (accounts.containsKey(accountId)) return false;
        // 不存在则创建新账号
        accounts.put(accountId, new Account(accountId));
        return true;
    }

    public Optional<Integer> deposit(int timeStamp, String accountId, int amount) {
        // 完成cashback
        executeCashback(timeStamp);
        // 检查账号是否存在,不存在则返回kong,同时检查amount是否合理,不合理也返回空
        if (!accounts.containsKey(accountId) || amount <= 0) return Optional.empty();
        // 更新该账号的balance
        int balance = accounts.get(accountId).balance;
        balance += amount;
        accounts.get(accountId).balance = balance;

        return Optional.of(balance);
    }

    public Optional<Integer> transfer(
            int timeStamp, String sourceAccountId, String targetAccountId, int amount) {
        // 完成cashback
        executeCashback(timeStamp);
        // 如果source账号和target账号一致,则返回空
        if (sourceAccountId == targetAccountId) return Optional.empty();
        // 如果任意账号不存在,则返回空
        if (!accounts.containsKey(sourceAccountId) || !accounts.containsKey(targetAccountId))
            return Optional.empty();
        // 计算balance
        int sourceBalance = accounts.get(sourceAccountId).balance;
        int targetBalance = accounts.get(targetAccountId).balance;
        // 如果source的balance不足,返回空
        if (sourceBalance < amount) return Optional.empty();
        // 更新双方账户的balance
        sourceBalance -= amount;
        targetBalance += amount;
        accounts.get(sourceAccountId).balance = sourceBalance;
        accounts.get(targetAccountId).balance = targetBalance;
        // 更新source的spent
        accounts.get(sourceAccountId).spent += amount;

        return Optional.of(sourceBalance);
    }

    /*
    L2
     */
    public List<String> topSpenders(int timeStamp, int k) {
        // 完成cashback
        executeCashback(timeStamp);
        // 按照规则排序
        List<Account> sortedAccounts =
                accounts.values().stream().sorted(spendingComparator).toList();
        // 判断k和accounts数量,取最小值
        int times = Math.min(k, sortedAccounts.size());
        // 按照要求编译输出
        List<String> answer = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            Account account = sortedAccounts.get(i);
            answer.add(account.id + "(" + account.spent + ")");
        }

        return answer;
    }

    /*
    L3
     */
    public Optional<String> pay(int timeStamp, String accountId, int amount) {
        // 完成cashback
        executeCashback(timeStamp);
        // 检查账号是否存在,不存在则返回空,同时检查amount是否合理,不合理也返回空
        if (!accounts.containsKey(accountId) || amount <= 0) return Optional.empty();
        // 如果balance不足,返回空
        Account account = accounts.get(accountId);
        if (account.balance < amount) return Optional.empty();
        // 更新balance
        account.balance -= amount;
        // 更新spent
        account.spent += amount;
        // 计算cashback和计划时间
        int cashback = (int) Math.round(CASHBACK_RATE * amount);
        int scheduledTime = timeStamp + ONE_DAY_IN_MILLISECONDS;
        // 创建payment
        String paymentId = PAYMENT_ID_PREFIX + globalPaymentId++;
        Payment payment = new Payment(account.id, paymentId, cashback, scheduledTime);
        // 加入heap和map
        sortedPendingCashbacks.add(payment);
        allPayments.put(paymentId, payment);
        account.payments.put(paymentId, payment);

        return Optional.of(paymentId);
    }

    public Optional<String> getPaymentStatus(int timeStamp, String accountId, String paymentId) {
        // 完成cashback
        executeCashback(timeStamp);
        // 检查账号和paymentId是否存在,不存在则返回空
        if (!accounts.containsKey(accountId) || !allPayments.containsKey(paymentId))
            return Optional.empty();
        // paymentId所关联的accountId是否与输入的accountId相等,不相等则返回空
        if (!allPayments.get(paymentId).id.equals(accountId)) return Optional.empty();

        return Optional.of(allPayments.get(paymentId).cashbackStatus.status);
    }

    /*
    L4
     */
    public boolean mergeAccounts(int timeStamp, String accountId1, String accountId2) {
        // 完成cashback
        executeCashback(timeStamp);
        // 如果两个accountId一样
        if (accountId1.equals(accountId2)) return false;
        // 如果任意一个accontId不存在,返回false
        if (!accounts.containsKey(accountId1) || !accounts.containsKey(accountId2)) return false;

        Account account1 = accounts.get(accountId1);
        Account account2 = accounts.get(accountId2);

        // 更新balance
        account1.balance += account2.balance;
        // 更新spent
        account1.spent += account2.spent;
        // 更新account2的所有payments的id
        for (Payment payment : account2.payments.values()) {
            payment.id = account1.id;
        }
        // 清除account2
        accounts.remove(account2.id);

        return true;
    }

    private enum CashbackStatus {
        IN_PROGRESS("IN_PROGRESS"),
        CASHBACK_RECEIVED("CASHBACK_RECEIVED");

        final String status;

        CashbackStatus(String status) {
            this.status = status;
        }
    }

    private static class AccountHistory {
        int balance;
        int spent;
    }

    private static class Account {
        String id;
        int balance;
        int spent;
        Map<String, Payment> payments;
        Map<Integer, AccountHistory> history;
        Account next;

        public Account(String id) {
            this.id = id;
            this.balance = 0;
            this.spent = 0;
            this.payments = new HashMap<>();
            this.history = new TreeMap<>((a, b) -> a - b);
            //            history.put(timestamp, new AccountHistory());
            this.next = null;
        }
    }

    private static class Payment {
        String id;
        String paymentId;
        int cashback;
        int scheduledTime;
        CashbackStatus cashbackStatus;

        public Payment(String id, String paymentId, int cashback, int scheduledTime) {
            this.id = id;
            this.paymentId = paymentId;
            this.cashback = cashback;
            this.scheduledTime = scheduledTime;
            this.cashbackStatus = CashbackStatus.IN_PROGRESS;
        }
    }

    //    public Optional<Integer> getBalance(int timeStamp, int timeAt, String accountId) {
    //        // 完成cashback
    //        executeCashback(timeStamp);
    //        // 检查账号是否存在,不存在则返回空
    //        if (!accounts.containsKey(accountId)) return Optional.empty();
    //    }
}
