package miniProject;

import java.util.Scanner;

public class Operation {
    static Scanner sc = new Scanner(System.in);
    static String cmd;
    static String loginId;
    static String loginPw;

    public static void main(String[] args) {
        Operation op = new Operation();
        ConsoleDisplay.title();
        while (true) {
            ConsoleDisplay.mainPage();
            switch (cmd) {
                case "1" -> op.signUp();
                case "2" -> op.signIn();
                case "3" -> op.findAccount();
                case "4" -> op.resetPw();
                case "5" -> {ConsoleDisplay.exit(); System.exit(0);}
                default -> System.out.println(":   잘못된 입력입니다.\n:   번호를 다시 입력해주세요.");
            }      
        }
    }

    public void signUp() {
        String id; String pw; String name; String pno; String adrs; String gender;
        do {
            id = setId();
            pw = setPw();
            name = setName();
            pno = setPno();
            adrs = setAdrs();
            gender = setGender();

            ConsoleDisplay.confrimSignUp(id, pw, name, pno, adrs, gender);      // 입력한 정보 확인
            switch (cmd) {
                case "1" -> {
                    Users user = new Users();
                    if (user.createAccount(id, pw, name, pno, adrs, gender)) {
                        System.out.println("     :   축하합니다. 가입이 완료되었습니다.\n     :    로그인 후 서비스 이용 가능합니다.\n");
                    }
                }
                case "2" -> System.out.println("     :   정보 입력 화면으로 돌아갑니다.\n");
                case "3" -> {}
            }
        } while (cmd.equals("2"));
    }



    public String setId() {
        ConsoleDisplay.setId();        
        String id = Operation.sc.nextLine();
        id = idFormatWrong(id);
        return id;
    }
    
    public String setPw() {
        ConsoleDisplay.setPw();
        String pw = Operation.sc.nextLine();
        pw = pwFormatWrong(pw);
        return pw;
    }

    public String setName() {
        ConsoleDisplay.setName();
        String name = Operation.sc.nextLine();
        name = isNameNull(name);
        return name;
    }

    public String setPno() {
        ConsoleDisplay.setPno();
        String pno = Operation.sc.nextLine();
        pno = pnoFormatWrong(pno);
        return pno;
    }

    public String setAdrs() {
        ConsoleDisplay.setAdrs();
        String adrs = Operation.sc.nextLine();
        adrs = isAddressNull(adrs);
        return adrs;
    }

    public String setGender() {
        ConsoleDisplay.setGender();
        String gender = Operation.sc.nextLine();
        gender = genderInputWrong(gender);
        return gender;
    }

    public String idFormatWrong(String id) {
        if (!id.matches("^[a-zA-Z0-9!@#$%^&*()_+=-]{6,20}$")) {
            System.out.println();
            System.out.println("     :   입력 형식이 잘못되었습니다.\n");
            System.out.print("       > ID: ");
            id = Operation.sc.nextLine();
            return idFormatWrong(id);
        }
        return id;
    }

    public String pwFormatWrong(String pw) {
        if (!pw.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-])[a-zA-Z\\d!@#$%^&*()_+=-]{6,20}$")) {
            System.out.println();
            System.out.println("     :   입력 형식이 잘못되었습니다.\n");
            System.out.print("       > PASSWORD: ");
            pw = Operation.sc.nextLine();
            return pwFormatWrong(pw);
        }
        return pw;
    }

    public String isNameNull(String name) {
        if (name.equals("")) {
            System.out.println();
            System.out.println("     :   값을 입력해주세요.\n");
            System.out.print("       > 이름: ");
            name = Operation.sc.nextLine();
            return isNameNull(name);
        }
        return name;
    }
    
    public String pnoFormatWrong(String pno) {
        if (!pno.matches("^\\d{2,4}-\\d{3,4}-\\d{4}$")) {
            System.out.println();
            System.out.println("     :   입력 형식이 잘못되었습니다.\n");
            System.out.print("       > 전화번호: ");
            pno = Operation.sc.nextLine();
            return pnoFormatWrong(pno);
        }
        return pno;
    }

    public String isAddressNull(String adrs) {
        if (adrs.equals("")) {
            System.out.println();
            System.out.println("     :   값을 입력해주세요.\n");
            System.out.print("       > 주소: ");
            adrs = Operation.sc.nextLine();
            return isAddressNull(adrs);
        }
        return adrs;
    }

    public String genderInputWrong(String gender) {
        if (!gender.equals("1") && !gender.equals("2")) {
            System.out.println();
            System.out.println("     :   입력이 잘못되었습니다.\n");
            System.out.println("       (남성 : 1 | 여성 : 2)");
            System.out.print("       > ");
            gender = Operation.sc.nextLine();
            return genderInputWrong(gender);
        }
        return gender;
    }



    public void signIn() {
        Users user = new Users();
        do {
            ConsoleDisplay.inputId();
            loginId = Operation.sc.nextLine();
            ConsoleDisplay.inputPw();
            loginPw = Operation.sc.nextLine();
            
            boolean accountMatch = user.checkAccount(loginId, loginPw);
            if (accountMatch) {
                System.out.printf("\n     :   로그인 성공\n     :   현재 로그인 계정 [ %s ]\n", loginId);
                user.updateLastLogin(loginId);
                break;
            }
            System.out.println("\n     :   아이디 또는 비밀번호를 잘못 입력하셨습니다.");
        } while (true); 
        
        if (user.checkAdmin(loginId)) {
            do myPageAdmin(user);
            while (!cmd.equals("4") && !cmd.equals("deleteaccount")); 
        } else {
            do myPage(user);
            while (!cmd.equals("3") && !cmd.equals("deleteaccount"));
        }
    }
    
    public void deleteAccount(Users user) {
        if (!user.checkAdmin(loginId)) {
            System.out.println("비밀번호를 입력하세요");
            String inputPw = Operation.sc.nextLine();
            if (inputPw.equals(loginPw)) {
                confirmDelAcc(user);
            }
        } else {
            confirmDelAcc(user);
        }
    }

    public void confirmDelAcc(Users user) {
        System.out.println("정말 탈퇴합니까?");
        String delAcc = Operation.sc.nextLine();
        if (delAcc.equalsIgnoreCase("y")) {
            user.delete();
            System.out.println("탈퇴 완료");
        }  
    }

    public void myPageAdmin(Users user) {
        ConsoleDisplay.myPageAdmin();
        switch (cmd) {
            case "1" -> user.myInfo(loginId);
            case "2" -> {Board board = new Board(); board.list();}
            case "3" -> user.list();
            case "4" -> {user.updateLastLogout(loginId); System.out.println("     :   로그아웃 되었습니다.");}
            case "5" -> {ConsoleDisplay.exit(); user.updateLastLogout(loginId); System.exit(0);}
            case "deleteaccount" -> deleteAccount(user);        // 회원탈퇴 시크릿코드
            default -> System.out.println("     :   잘못된 입력입니다.\n:   번호를 다시 입력해주세요.");
        }
    }

    public void myPage(Users user) {
        ConsoleDisplay.myPage();
        switch (cmd) {
            case "1" -> user.myInfo(loginId);
            case "2" -> {Board board = new Board(); board.list();}
            case "3" -> {user.updateLastLogout(loginId); System.out.println("로그아웃 되었습니다.");}
            case "4" -> {ConsoleDisplay.exit(); user.updateLastLogout(loginId); System.exit(0);}
            case "deleteaccount" -> deleteAccount(user);
            default -> System.out.println(":   잘못된 입력입니다.\n:   번호를 다시 입력해주세요.");
         }
    }

    public void findAccount() {
        Users user = new Users();
        ConsoleDisplay.findAccount();
        ConsoleDisplay.setName();
        String name = Operation.sc.nextLine();
        ConsoleDisplay.setPno();
        String pno = Operation.sc.nextLine();
        user.matchInfo(name, pno);
    }

    public void resetPw() {
        Users user = new Users();
        ConsoleDisplay.resetPw();
        String id = Operation.sc.nextLine();
        if(user.idExist(id)) user.changePw(id, user.newPw());
    }
}
