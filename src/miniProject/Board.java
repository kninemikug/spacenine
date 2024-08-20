package miniProject;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import lombok.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class Board {
    private int no;
    private String user_id;
    private String title;
    private String content;
    private int view;
    private String regdate;
    private String last_edit_at;
	private String password;

    private Connection conn;

    public Board() {
        try {
            Class.forName("oracle.jdbc.OracleDriver");

            conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521/xe",
                "miniproj1",
                "1004"
            );
        } catch (Exception e) {
            e.printStackTrace();
            exit();
        }
    }

    public void list() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		
		int pageNum = 1;
		int temp = 0;
		boolean isLastPage = false;
		int loop = 0;
		do {
			try {
				String sql = "SELECT * FROM (SELECT rownum rnum, b.* FROM board b WHERE rownum <= ?*10 ORDER BY regdate DESC) " +
								"WHERE rnum BETWEEN (?-1)*10+1 and ?*10 " +
								"AND rownum <= 10 " +
								"ORDER BY regdate, no";
				PreparedStatement pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				pstmt.setInt(1, pageNum);
				pstmt.setInt(2, pageNum);
				pstmt.setInt(3, pageNum);
				ResultSet rs = pstmt.executeQuery();
				
				if (!rs.next()) {
					System.out.println("해당 페이지는 존재하지 않습니다.");
					pageNum = temp;
				} else {
					ConsoleDisplay.boardFrame(pageNum);
					rs.beforeFirst();

					while(rs.next()) {		
						no = rs.getInt("no");
						user_id = rs.getString("user_id");
						title = rs.getString("title");
						content = rs.getString("content");
						view = rs.getInt("view");
						regdate = rs.getString("regdate");
						Date regdate_d = rs.getDate("regdate");
						last_edit_at = rs.getString("regdate");
						Date last_edit_at_d = rs.getDate("regdate");
						
						Date now = new Date();
						Calendar cal = Calendar.getInstance();
						cal.setTime(now);
						cal.add(Calendar.DATE, -1);

						if (regdate_d.compareTo(cal.getTime()) < 0) {
							regdate = regdate_d.toString();
						} else {
							regdate_d = rs.getTime("regdate");
							regdate = sdf.format(regdate_d);
						}

						if (last_edit_at_d.compareTo(cal.getTime()) < 0) {
							last_edit_at = last_edit_at_d.toString();
						} else {
							last_edit_at_d = rs.getTime("last_edit_at");
							last_edit_at = sdf.format(last_edit_at_d);
						}

						System.out.printf(" %-5d%-35s%-15s%-5d%-12s%-12s\n", no, title, user_id, view, regdate, last_edit_at);
						loop++;
					}

					if (loop < 10) {
						isLastPage = true;
					}
					loop = 0;
				}
				rs.close();
				pstmt.close();
			} catch(SQLException e) {
				e.printStackTrace();
				exit();
			}

			ConsoleDisplay.boardMenu();

			switch (Operation.cmd) {
				case "p", "P" -> {
					pageNum--;
					if (pageNum < 1) {
						System.out.println("첫 번째 페이지입니다.");
						pageNum = 1;
					}
				}
				case "n", "N" -> {
					if (isLastPage) System.out.println("마지막 페이지입니다.");
					else pageNum++;
				}
				case "f", "F" -> {
					System.out.println("몇 페이지로 이동?");
					try {
						int input = Integer.parseInt(Operation.sc.nextLine());
						temp = pageNum;
						pageNum = input;
					} catch (Exception e) {
						System.out.println("잘못된 입력입니다.");
					}
				}
				case "v", "V" -> view();
				case "w", "W" -> create();
				case "q", "Q" -> {}
				default -> System.out.println("잘못된 입력입니다.");
			}
		} while (!Operation.cmd.equalsIgnoreCase("q"));		
    }

	

	public void view(){
		Users user = new Users();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.DATE, -1);
		
		System.out.println("조회하려는 게시물 번호를 입력하세요");
		int searchNo = Integer.parseInt(Operation.sc.nextLine());

		try {
			String sql = "SELECT no, user_id, title, content, \"VIEW\", regdate, last_edit_at, password FROM board WHERE no = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, searchNo);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				no = rs.getInt("no");
				user_id = rs.getString("user_id");
				title = rs.getString("title");
				content = rs.getString("content");
				view = rs.getInt("view");
				regdate = rs.getString("regdate");
				Date regdate_d = rs.getDate("regdate");
				last_edit_at = rs.getString("regdate");
				Date last_edit_at_d = rs.getDate("regdate");
				password = rs.getString("password");

				if (regdate_d.compareTo(cal.getTime()) < 0) {
					regdate = regdate_d.toString();
				} else {
					regdate_d = rs.getTime("regdate");
					regdate = sdf.format(regdate_d);
				}
				if (last_edit_at_d.compareTo(cal.getTime()) < 0) {
					last_edit_at = last_edit_at_d.toString();
				} else {
					last_edit_at_d = rs.getTime("last_edit_at");
					last_edit_at = sdf.format(last_edit_at_d);
				}

				System.out.println("게시물 번호: " + no);
				System.out.println("작성자: " + user_id);
				System.out.println("작성 일시: " + regdate);
				System.out.println("수정 일시: " + last_edit_at);
				System.out.println("제목: " + title);
				System.out.println("본문: " + content);

				sql = "UPDATE board SET \"VIEW\" = ? + 1 WHERE no = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, view);
				pstmt.setInt(2, no);
				pstmt.executeUpdate();

				System.out.println("---------\n");

				if (user_id.equals(Operation.loginId)) {
					System.out.println("수정(e) 삭제(d) 나가기(q)");
					Operation.cmd = Operation.sc.nextLine();
					
					switch (Operation.cmd) {
						case "e", "E" -> {
							System.out.println("게시물 비밀번호를 입력하세요.");
							String postPw = Operation.sc.nextLine();
							if (postPw.equals(password)) {
								String regConfirm = null;
								do {
									System.out.print("제목 > "); 	
									title = Operation.sc.nextLine();
									System.out.println("내용 (내용 입력을 종료하려면 마지막 줄에 q를 입력하세요.)");
									System.out.print("> ");
									StringBuilder sb = new StringBuilder();
									String line = Operation.sc.nextLine();
									while (!line.equalsIgnoreCase("q")) {
										sb.append(line).append("\n");
										line = Operation.sc.nextLine();
									}
									content = sb.toString();
									System.out.println("입력을 종료했습니다. 이대로 등록할까요?");
									System.out.println("1. 등록 | 2. 재입력 | 3. 나가기");
									regConfirm = Operation.sc.nextLine();
									regConfirm = confirmRegistration(regConfirm);
								} while (regConfirm.equals("2"));
								
								if(regConfirm.equals("1")) {
									try {
										sql = "UPDATE board SET title = ?, content = ?, last_edit_at = sysdate WHERE no = ?";
										pstmt = conn.prepareStatement(sql);
										pstmt.setString(1, title);
										pstmt.setString(2, content);
										pstmt.setInt(3, no);
										pstmt.executeUpdate();
									} catch (Exception e) {
										e.printStackTrace();
										exit();
									}
								}
							}	
						}
						case "d", "D" -> {
							System.out.println("게시물 비밀번호를 입력하세요.");
							String postPw = Operation.sc.nextLine();
							if (postPw.equals(password)) {
								try {
									sql = "DELETE board WHERE no = ?";
									pstmt = conn.prepareStatement(sql);
									pstmt.setInt(1, no);
									pstmt.executeUpdate();
									System.out.println("삭제가 완료되었습니다.");
								} catch (Exception e) {e.printStackTrace(); exit();}
							}
						}
						case "q", "Q" -> {}
					}
				} else if (user.checkAdmin(Operation.loginId)) {
					System.out.println("삭제(d) 나가기(q)");
					Operation.cmd = Operation.sc.nextLine();

					switch (Operation.cmd) {
						case "d", "D" -> {
							try {
								sql = "DELETE board WHERE no = ?";
								pstmt = conn.prepareStatement(sql);
								pstmt.setInt(1, no);
								pstmt.executeUpdate();
								System.out.println("삭제가 완료되었습니다.");
							} catch (Exception e) {e.printStackTrace(); exit();}
						}
						case "q", "Q" -> {}
					}
				} else {
					System.out.println("나가기(q)");
					Operation.cmd = Operation.sc.nextLine();
				}
			}
			rs.close(); pstmt.close();
		} catch (Exception e) {e.printStackTrace(); exit();}
	}

	public void create() {
		//입력 받기
		System.out.println("[에디터 오픈]");
		String regConfirm = null;
		do {
			System.out.print("제목 > "); 	
			title = Operation.sc.nextLine();
			System.out.println("내용 (내용 입력을 종료하려면 마지막 줄에 q를 입력하세요.)");
			System.out.println("> ");
			StringBuilder sb = new StringBuilder();
			String line = Operation.sc.nextLine();
			while (!line.equalsIgnoreCase("q")) {
				sb.append(line).append("\n");
				line = Operation.sc.nextLine();
			}
			content = sb.toString();

			System.out.println("입력을 종료했습니다. 이대로 등록할까요?");
			System.out.println("1. 등록 | 2. 재입력 | 3. 나가기");
			regConfirm = Operation.sc.nextLine();
			regConfirm = confirmRegistration(regConfirm);
		} while (regConfirm.equals("2"));
		
		if(regConfirm.equals("1")) {
			System.out.println("게시물 수정/삭제 시 사용할 비밀번호를 입력해주세요.");
			String postPw = Operation.sc.nextLine();
			try {
				String sql = "INSERT INTO board (no, user_id, title, content, regdate, last_edit_at, password) "
							+ "VALUES (board_seq.nextval, ?, ?, ?, sysdate, sysdate, ?)";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, Operation.loginId);
				pstmt.setString(2, title);
				pstmt.setString(3, content);
				pstmt.setString(4, postPw);
				
				// sql문 execute 실패하면 시퀀스 값 nextval 된 거 다시 1 낮춤
				try {
					pstmt.executeUpdate();
				} catch (Exception e) {
					sql = "ALTER SEQUENCE board_seq INCREMENT BY -1";
					PreparedStatement seqCancle = conn.prepareStatement(sql);
					seqCancle.executeUpdate();
					seqCancle.close();
				}
				pstmt.close();
			} catch (Exception e) {e.printStackTrace(); exit();}
		}
	}

	public String confirmRegistration(String regConfirm) {
		if (!regConfirm.equals("1") && !regConfirm.equals("2") && !regConfirm.equals("3")) {
			System.out.println("잘못된 입력입니다.");
			System.out.println("1. 등록 | 2. 재입력 | 3. 나가기");
			regConfirm = Operation.sc.nextLine();
			return confirmRegistration(regConfirm);
		}
		return regConfirm;
	}


    public void exit() {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {}
		}
		if(Operation.loginId != null) {
			Users user = new Users();
			user.updateLastLogout(Operation.loginId);
		}
		System.out.println("** 게시판 종료 **");
		System.exit(0);
	}
}
