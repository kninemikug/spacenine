package miniProject;

import java.util.Scanner;

public class Operation {
    static Scanner sc = new Scanner(System.in);
    static String cmd;
    static String loginId;
    static String loginPw;

    public static void main(String[] args) {
        ConsoleDisplay.title();
        while (true) {
            ConsoleDisplay.mainPage();
            /*
             * 1: 회원가입
             * 2: 로그인
             * 3: 아이디 찾기
             * 4: 비밀번호 변경
             * 5: 종료
             */

            switch (cmd) {
                case "1":
                    signUp();
                    break;
            
                case "2":
                    signIn();
                    break;
                
                case "3":
                    findAccount();
                    break;
    
                case "4":
                    resetPw();
                    break;
    
                case "5":
                    ConsoleDisplay.exit();
                    System.exit(0);
                    break;
                default:
                    System.out.println(":   잘못된 입력입니다.\n:   번호를 다시 입력해주세요.");
                    break;
            }      
        }


    }

    public static void signUp() {
        do {
            ConsoleDisplay.setId();        
            String id = Operation.sc.nextLine();
            id = idFormatWrong(id);
            
            ConsoleDisplay.setPw();
            String pw = Operation.sc.nextLine();
            pw = pwFormatWrong(pw);
            
            ConsoleDisplay.setName();
            String name = Operation.sc.nextLine();
            name = isNameNull(name);
            
            ConsoleDisplay.setPno();
            String pno = Operation.sc.nextLine();
            pno = pnoFormatWrong(pno);
            
            ConsoleDisplay.setAdrs();
            String adrs = Operation.sc.nextLine();
            adrs = isAddressNull(adrs);
            
            ConsoleDisplay.setGender();
            String gender = Operation.sc.nextLine();
            gender = genderInputWrong(gender);
            
            ConsoleDisplay.confrimSignUp(id, pw, name, pno, adrs, gender);
            
            switch (cmd) {
                case "1":
                    //가입  -> 중복확인 -> 회원등록
                    Users user = new Users();
                    try {
                        boolean isSuccess = user.createAccount(id, pw, name, pno, adrs, gender);
                        if (isSuccess) {
                            System.out.println(":   축하합니다. 가입이 완료되었습니다.\n:    로그인 후 서비스 이용 가능합니다.\n");
                        }
                    } catch (Exception e) {
                        // 계정 중복되면 예외 발생
                    }
                    break;
                case "2":
                    System.out.println(":   정보 입력 화면으로 돌아갑니다.\n");
                    break;
                case "3":
                    break;
            }
        } while (cmd.equals("2"));
    }

    public static String idFormatWrong(String id) {
        if (!id.matches("^[a-zA-Z0-9!@#$%^&*()_+=-]{6,20}$")) {
            System.out.println();
            System.out.println(":   입력 형식이 잘못되었습니다.\n");
            System.out.print("       > ID: ");
            id = Operation.sc.nextLine();
            return idFormatWrong(id);
        }
        return id;
    }

    public static String pwFormatWrong(String pw) {
        if (!pw.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-])[a-zA-Z\\d!@#$%^&*()_+=-]{6,20}$")) {
            System.out.println();
            System.out.println(":   입력 형식이 잘못되었습니다.\n");
            System.out.print("       > PASSWORD: ");
            pw = Operation.sc.nextLine();
            return idFormatWrong(pw);
        }
        return pw;
    }

    public static String isNameNull(String name) {
        if (name.equals("")) {
            System.out.println();
            System.out.println(":   값을 입력해주세요.\n");
            System.out.print("       > 이름: ");
            name = Operation.sc.nextLine();
            return isNameNull(name);
        }
        return name;
    }
    
    public static String pnoFormatWrong(String pno) {
        if (!pno.matches("^\\d{2,4}-\\d{3,4}-\\d{4}$")) {
            System.out.println();
            System.out.println(":   입력 형식이 잘못되었습니다.\n");
            System.out.print("       > 전화번호: ");
            pno = Operation.sc.nextLine();
            return pnoFormatWrong(pno);
        }
        return pno;
    }

    public static String isAddressNull(String adrs) {
        if (adrs.equals("")) {
            System.out.println();
            System.out.println(":   값을 입력해주세요.\n");
            System.out.print("       > 주소: ");
            adrs = Operation.sc.nextLine();
            return isAddressNull(adrs);
        }
        return adrs;
    }

    public static String genderInputWrong(String gender) {
        if (!gender.equals("1") && !gender.equals("2")) {
            System.out.println();
            System.out.println(":   입력이 잘못되었습니다.\n");
            System.out.println("       (남성 : 1 | 여성 : 2)");
            System.out.print("       > ");
            gender = Operation.sc.nextLine();
            return genderInputWrong(gender);
        }
        return gender;
    }



    public static void signIn() {
        Users user = new Users();
        
        do {
            ConsoleDisplay.inputId();
            loginId = Operation.sc.nextLine();
            ConsoleDisplay.inputPw();
            loginPw = Operation.sc.nextLine();
            
            boolean accountMatch = user.checkAccount(loginId, loginPw);
            if (accountMatch) {
                System.out.printf(":   로그인 성공\n:    현재 로그인 계정 [ %s ]\n", loginId);
                System.out.println("\n[로그인 완료]");

                user.updateLastLogin(loginId);
                
                break;
            }
            System.out.println("아이디 또는 비밀번호를 잘못 입력하셨습니다.");
        } while (true); 

        boolean isAdmin = user.checkAdmin(loginId);
        
        if (isAdmin) {
            do myPageAdmin();
            while (!cmd.equals("4")); 
        } else {
            do myPage();
            while (!cmd.equals("3"));
        }
    }

    public static void myPageAdmin() {
        ConsoleDisplay.myPageAdmin();
        Users user = new Users();
        /*
         * 1: 나의 정보 확인
         * 2: 게시물 목록
                Board board = new Board();
                board.list();
         * 3: 회원 목록
                Users users = new Users();
                users.list();
         * 4: 로그아웃
         * 5: 종료
         */

         switch (cmd) {
            case "1":
                user.myInfo(loginId);
                break;
            case "2":
                Board board = new Board();
                board.list();
                break;
            case "3":
                user.list();
                break;
            case "4":
                user.updateLastLogout(loginId);
                System.out.println("로그아웃 되었습니다.");
                break;
            case "5":
                ConsoleDisplay.exit();
                user.updateLastLogout(loginId);
                System.exit(0);
                break;
         
            default:
                System.out.println(":   잘못된 입력입니다.\n:   번호를 다시 입력해주세요.");
                break;
         }
    }

    public static void myPage() {
        ConsoleDisplay.myPage();
        Users user = new Users();

        switch (cmd) {
            case "1":
                user.myInfo(loginId);
                break;
            case "2":
                Board board = new Board();
                board.list();
                break;
            case "3":
                user.updateLastLogout(loginId);
                System.out.println("로그아웃 되었습니다.");
                break;
            case "4":
                ConsoleDisplay.exit();
                user.updateLastLogout(loginId);
                System.exit(0);
                break;
         
            default:
                System.out.println(":   잘못된 입력입니다.\n:   번호를 다시 입력해주세요.");
                break;
         }
    }

    public static void findAccount() {
        Users user = new Users();
        
        ConsoleDisplay.findAccount();
        
        ConsoleDisplay.setName();
        String name = Operation.sc.nextLine();
        
        ConsoleDisplay.setPno();
        String pno = Operation.sc.nextLine();
        
        user.matchInfo(name, pno);
    }

    public static void resetPw() {
        Users user = new Users();
        ConsoleDisplay.resetPw();
        String id = Operation.sc.nextLine();
        if(user.idExist(id)) {
            user.changePw(id, user.newPw());
        }
        
        
        
        
        
        
        // user.deletePw(id);

    }
}
