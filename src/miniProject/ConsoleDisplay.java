package miniProject;

public class ConsoleDisplay {
    static void title() {
        System.out.println();
        System.out.println("+-----------------------------------------------------+");
        System.out.println("|                  미니 프로젝트 1차                  |");
        System.out.println("+-----------------------------------------------------+");
    }
    
    static void mainPage() {
        System.out.println();
        System.out.println("               ========  MAIN  ========");
        System.out.println("               |                      |");
        System.out.println("               |  1. 회원가입         |");
        System.out.println("               |  2. 로그인           |");
        System.out.println("               |  3. 아이디 찾기      |");
        System.out.println("               |  4. 비밀번호 초기화  |");
        System.out.println("               |  5. 종료             |");
        System.out.println("               |                      |");
        System.out.println("               +----------------------+");
        System.out.println();
        System.out.println("          * 원하는 기능의 번호를 입력하세요 *");
        System.out.print("            > ");
        Operation.cmd = Operation.sc.nextLine();
        System.out.println();
    }

    //=============== 회원가입 화면 ==========================
    static void setId() {
        System.out.println();
        System.out.println("       - > - > - > -  회원가입  - < - < - < -");
        System.out.println("    +");
        System.out.println("       생성할 계정 ID를 입력하세요. [6~20자, 영문/숫자/특수문자 사용 가능]");
        System.out.print("       > ID: ");
    }

    static void setPw() {
        System.out.println("    +");
        System.out.println("       비밀번호를 설정하세요. [6~20자, 영문/숫자/특수문자 모두 포함]");
        System.out.print("       > PASSWORD: ");
    }

    static void setName() {
        System.out.println("    +");
        System.out.println("       이름을 입력하세요");
        System.out.print("       > ");
    }

    static void setPno() {
        System.out.println("    +");
        System.out.println("       전화번호를 입력하세요(ex 010-1234-5678)");
        System.out.print("       > ");
    }

    static void setAdrs() {
        System.out.println("    +");
        System.out.println("       주소를 입력하세요");
        System.out.print("       > ");
    }

    static void setGender() {
        System.out.println("    +");
        System.out.println("       성별을 입력하세요");
        System.out.print("       (남성 : 1 | 여성 : 2)");
        System.out.print(" > ");
    }

    static void confrimSignUp(String id, String pw, String name, String pno, String adrs, String gender) {
        System.out.println();
        System.out.println("       -  -  -  -  -  -  -  -  -  -  -  -  -  - ");
        System.out.println();
        System.out.println("       다음의 정보로 가입하시겠습니까?");
        System.out.println("      +--");
        System.out.printf("      | ID: %26s\n", id);
        System.out.printf("        PASSWORD: %20s\n", pw);
        System.out.printf("        이름:%23s\n", name);
        System.out.printf("        전화번호: %20s\n", pno);
        System.out.printf("        주소: \n%29s\n", adrs);
        System.out.printf("        성별: %23s |", (gender.equals("1") ? "남" : "여"));
        System.out.println();
        System.out.println("                                     --+");
        System.out.println();
        System.out.println("      |  1. 가입          ");
        System.out.println("      |  2. 다시 입력     ");
        System.out.println("      |  3. 취소하고 이전 화면으로  ");
        System.out.println();
        System.out.println("      . . . . . . . . . . . . . .");
        System.out.println();
        System.out.print("      번호 선택 > ");
        Operation.cmd = Operation.sc.nextLine();
        System.out.println();
    }
    //=========== 회원가입 화면 ======================


    //============== 로그인 화면 ===================
    static void inputId() {
        System.out.println();
        System.out.println("         - > - > - > -  로그인  - < - < - < -");
        System.out.println();
        System.out.print("         > ID: ");
    }

    static void inputPw() {
        System.out.print("         > Password: ");
    }
    //============= 로그인 화면 ====================


    //============= 마이페이지 ====================
    static void myPageAdmin() {
        System.out.println();               //회원탈퇴는 다크패턴으로..
        System.out.println("               =======  MY PAGE  =======");
        System.out.println("               |                       |");
        System.out.println("               |  1. 나의 정보 확인    |");
        System.out.println("               |  2. 게시물 목록       |");
        System.out.println("               |  3. 회원 목록         |");
        System.out.println("               |  4. 로그아웃          |");
        System.out.println("               |  5. 종료              |");
        System.out.println("               |                       |");
        System.out.println("               +-----------------------+");
        System.out.println();
        System.out.println("          * 원하는 기능의 번호를 입력하세요 *");
        System.out.print("            > ");
        Operation.cmd = Operation.sc.nextLine();
        System.out.println();
    }

    static void myPage() {
        System.out.println();
        System.out.println("               =======  MY PAGE  =======");
        System.out.println("               |                       |");
        System.out.println("               |  1. 나의 정보 확인    |");
        System.out.println("               |  2. 게시물 목록       |");
        System.out.println("               |  3. 로그아웃          |");
        System.out.println("               |  4. 종료              |");
        System.out.println("               |                       |");
        System.out.println("               +-----------------------+");
        System.out.println();
        System.out.println("          * 원하는 기능의 번호를 입력하세요 *");
        System.out.print("            > ");
        Operation.cmd = Operation.sc.nextLine();
        System.out.println();
    }
    //============= 마이페이지 ====================

    
    static void findAccount() {
        System.out.println("가입 시 입력한 정보와 동일한 내용을 입력해주세요.");
        System.out.println("- - - -");
        System.out.println();
    }
    


    static void resetPw() {
        System.out.println("아이디를 입력하세요.");
        System.out.println("- - - -");
        System.out.println();
    }
    

    //============ 게시판 ===================
    static void boardFrame(int pageNum) {
        System.out.println();
        System.out.println("[ " + pageNum + " 페이지 ]");
		System.out.println("---------------------------------------------------------------------------------------");
		System.out.printf("%-4s%-33s%-12s%-7s%-10s%-5s\n", "번호", "제목", "작성자", "조회수", "작성", "수정");
		System.out.println("---------------------------------------------------------------------------------------");
    }
    
    static void boardMenu() {
        System.out.println("\n이전 페이지(p) | 다음 페이지(n) | 원하는 페이지로 이동(f) | 게시물 상세보기(v) | 새 게시물 작성(w) | 나가기(q)");
        Operation.cmd = Operation.sc.nextLine();
    }
    //============ 게시판 ===================





    static void exit() {
        System.out.println("            .");
        System.out.println("            .");
        System.out.println("            .");
        System.out.println();
        System.out.println("            프로그램을 종료합니다.");
        System.out.println();
    }
}
