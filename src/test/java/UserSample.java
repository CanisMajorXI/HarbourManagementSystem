import com.redsun.pojo.User;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class UserSample {
    private static Set<Integer> idSet = new HashSet<>();
    private static Set<String> emailSet = new HashSet<>();
    private static Random random = new Random();

    public static void main(String[] args) {
        User[] users = new User[100];
        for (int i = 0; i < 100; i++) {
            users[i] = new User();
            int id = 0;
            String email = null;
            do {
                id = getId();
            } while (!idSet.add(id));
            do {
                email = getEmail();
            } while (!emailSet.add(email));
            users[i].setId(id);
            users[i].setPassword(getPassword());
            users[i].setEmail(getEmail());
            int type = 1 + random.nextInt(4);
            users[i].setType((byte) type);
            String info = "INSERT INTO user_table VALUES (" + users[i].getId() + ",'" + users[i].getPassword() + "','" + users[i].getEmail() + "'," + users[i].getType() + ");";
            System.out.println(info);
        }
    }

    private static int getId() {
        int bottmBound = 10000000;
        return random.nextInt(90000000) + bottmBound;
    }

    private static String getPassword() {
        StringBuffer stringBuffer = new StringBuffer();
        int digit = random.nextInt(13) + 6;
        for (int i = 0; i < digit; i++) {
            int type = random.nextInt(62);
            if (type < 26) {
                stringBuffer.append((char) (type + 'a'));
            } else if (type < 52) {
                stringBuffer.append((char) (type + 'A' - 26));
            } else {
                stringBuffer.append((char) (type + '0') - 52);
            }
        }
        return stringBuffer.toString();
    }

    private static String getEmail() {
        StringBuffer stringBuffer = new StringBuffer();
        int digit = random.nextInt(5) + 6;
        for (int i = 0; i < digit; i++) {
            int type = random.nextInt(62);
            if (type < 26) {
                stringBuffer.append((char) (type + 'a'));
            } else if (type < 52) {
                stringBuffer.append((char) (type + 'a' - 26));
            } else {
                stringBuffer.append((char) (type + '0') - 52);
            }
        }
        stringBuffer.append("@redsun.com");
        return stringBuffer.toString();
    }
}
