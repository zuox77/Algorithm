package OOD;
import java.util.HashMap;

/*
https://www.1point3acres.com/bbs/thread-970148-1-1.html
https://www.1point3acres.com/bbs/thread-964779-1-1.html
https://www.1point3acres.com/bbs/thread-921749-1-1.html
Given card with cost in terms of tokens. For eg to buy some Card A, you need 3 Blue tokens and 2 Green tokens.
Tokens can be of Red, Green, Blue, Black or White color.
Now there is player who is holding some tokens.
For eg player has 4 Blue tokens and 2 Green tokens, then player can buy above Card A.
Lets say if player only has 2 Blue tokens and 2 Green tokens, then player can not buy Card A above as player is short of 1 Blue token.
1. Implement canPurchase() that returns true if player can buy the card or false otherwise.
More examples :
Cost of Card : 2 White, 1 Black and 4 BlueIf Player has : 2 White, 2 Black and 4 Blue, method will return true
If Player has : 2 White, 2 Green and 4 Blue, method will return false
2. Implement purchase() that will actually purchase the card
3. Add discount mechanism to the system and update purchase() and canPurchase()
4. Introduce a new gold token which can substitute any token

思路: 
1. 建立Color类或者enum Color类给Token和Card用
2. 在Token类里面计数, 而不是在Card或者Customer里面计数
3. Card里面用HashMap<Color, Token>存价格, 用Color作为Key是因为Color如果是enum或者一个常量类, 实例只有一个, 查找方便
4. Customer里面用HashMap<Color, Token>存手牌, 用HashMap<Color, Integer>存折扣
5. 如果有discount, 那么在purchase和canPurchase里面都记得将discount算进去
6. 如果有gold, 那判断canPurchase的时候, 记得复制一个gold的值, 然后在for循环中一直更新复制值, 保证能支付所有的token, 比如: 
    card需要: 2 Black, 2 White, 2 Blue
    有: 1 Black, 1 White, 1 Blue, 2 Gold
    那么其实是无法买card的, 因为gold不够, 但如果不在for循环中判断且更新复制值, 那么判断会出错
    在purchase的时候, 记得计算先计算不用gold剩下的值, 然后再用gold补剩余的
 */
public class TokenBuyCards {
    public enum Color {
        Red("Red"), Blue("Blue"), Green("Green"), White("White"), Black("Black"), Gold("Gold");
        private String name;
        private Color(String name) {
            this.name = name;
        }
        @Override
        public String toString() {
            return this.name;
        }
    }
    public class Token {
        /*
        1. Token有颜色属性和计数属性
        2. 在Card里, 计数表示价格, 在Customer里, 计数表示手牌有多少token
         */
        private Color color;
        private int count;
        // constructor
        public Token(Color color, int count) {
            this.color = color;
            this.count = count;
        }
        // get methods
        public Color getTokenColor() {
            return this.color;
        }
        public int getTokenNumber() {
            return this.count;
        }
        // update methods
        public void setTokenNumber(int value) {
            this.count = value;
        }
    }

    public class Card {
        /*
        1. 卡也有颜色属性
        2. 一个卡由多个token决定价格, 所以一定要一个哈希表
         */
        private HashMap<Color, Token> cardToken = new HashMap<>();
        private Color color;
        // constructor
        public Card(HashMap<Color, Token> cardToken, Color color) {
            this.cardToken = cardToken;
            this.color = color;
        }
        // get methods
        public Color getCardColor() {
            return this.color;
        }
        public HashMap<Color, Token> getCardTokens() {
            return this.cardToken;
        }
    }
    public class Customer {
        /*
        1. Customer由多个token组成, 表示拥有的token数量
        2. Customer有discount, 买了什么牌就有什么牌的折扣
        3. Customer要注意Gold牌可以当任意牌用
         */
        private HashMap<Color, Token> customerToken;
        private HashMap<Color, Integer> discount = new HashMap<>();
        // constructor
        public Customer(HashMap<Color, Token> customerToken) {
            this.customerToken = customerToken;
        }
        // get methods
        public HashMap<Color, Token> getAllTokens() {
            return this.customerToken;
        }
        public Token getToken(Color color) {
            if (this.customerToken.containsKey(color)) {
                return this.customerToken.get(color);
            }
            return null;
        }
        // purchase methods
        public boolean purchase(Card card) {
            if (canPurchase(card)) {
                updateAllToken(card);
                return true;
            }
            return false;
        }
        public boolean canPurchase(Card card) {
            Color cardColor = card.color;
            if (this.customerToken.get(Color.Gold) != null) {
                int goldNumber = this.customerToken.get(Color.Gold).getTokenNumber();
            }
            for (HashMap.Entry<Color, Token> entry: card.getCardTokens().entrySet()) {
                Color tokenColor = entry.getKey();
                int price = entry.getValue().getTokenNumber();
                // customer doesn't have the token of this color
                if (!this.customerToken.containsKey(tokenColor)) {
                    return false;
                }
                // token of this color is not enough
                int tokenNumber = this.customerToken.get(tokenColor).getTokenNumber();
//                if (tokenNumber < price) {
//                    return false;
//                }
//                // with discount:
//                int discountNumber = this.discount.getOrDefault(cardColor, 0);
//                if (tokenNumber + discountNumber < price) {
//                    return false;
//                }
//                // with discount and gold:
//                if (tokenNumber + goldNumber + discountNumber < price) {
//                    return false;
//                }
//                goldNumber = price < tokenNumber + discountNumber ? goldNumber : price - tokenNumber + discountNumber;
            }
            return true;
        }
        public void updateAllToken(Card card) {
            HashMap<Color, Token> cardToken  = card.getCardTokens();
            for (HashMap.Entry<Color, Token> entry: cardToken.entrySet()) {
                updateToken(entry.getKey(), entry.getValue());
            }
        }
        public void updateToken(Color cardTokenColor, Token cardToken) {
            Token customerToken = this.customerToken.get(cardTokenColor);
            int customerTokenNumber = customerToken.getTokenNumber();
            // no discount
            customerToken.setTokenNumber(customerTokenNumber - cardToken.getTokenNumber());
            // discount
            int discountNum = discount.getOrDefault(cardTokenColor, 0);
            if (this.discount.containsKey(cardTokenColor)) {
                customerToken.setTokenNumber(customerTokenNumber + discountNum - cardToken.getTokenNumber());
                this.discount.put(cardTokenColor, discountNum + 1);
            } else {
                customerToken.setTokenNumber(customerTokenNumber - cardToken.getTokenNumber());
                this.discount.put(cardTokenColor, 1);
            }
        }
    }
}
