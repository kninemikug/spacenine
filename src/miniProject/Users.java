package miniProject;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Types;

import lombok.Data;
import oracle.jdbc.datasource.impl.OracleDataSource;

@Data
public class Users {
    private int no;
    private String id;
    private String pw;
    private String name;
    private String pno;
    private String adrs;
    private int gender;

    private Connection conn;

    public Users() {
        try {
            Class.forName("oracle.jdbc.OracleDriver");

			//연결하기
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

    public boolean createAccount(String id, String pw, String name, String pno, String adrs, String gender) {
        try {
            String sql = "" +
                "INSERT INTO users (NO, ID, PASSWORD, NAME, PHONE, ADDRESS, GENDER, CREATED_AT)" +
                "VALUES (users_seq.nextval, ?, ?, ?, ?, ?, ?, sysdate)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, pw);
            pstmt.setString(3, name);
            pstmt.setString(4, pno);
            pstmt.setString(5, adrs);
            pstmt.setInt(6, Integer.parseInt(gender));
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println(":   이미 존재하는 ID입니다.");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            exit();
        }
        return true;
    }

    public boolean checkAccount(String loginId, String loginPw) {
        try {
            String sql = "{call login_pro(?, ?)}";
            CallableStatement cstmt = conn.prepareCall(sql);
            cstmt.setString(1, loginId);
            cstmt.registerOutParameter(2, Types.VARCHAR);
            try (cstmt) {
                cstmt.executeUpdate();
                if (!cstmt.getString(2).equals(loginPw)) {
                    return false;
                }
            } catch (SQLException e) {
                return false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            exit();
        }
        return true;
    }

    public void updateLastLogin(String loginId) {
        try {
            String sql = "UPDATE users SET last_login = sysdate WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loginId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            exit();
        }
    }

    public void updateLastLogout(String loginId) {
        try {
            String sql = "UPDATE users set last_logout = sysdate WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loginId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            exit();
        }
    }

    public boolean checkAdmin(String loginId) {
        try {
            String sql = "SELECT admin FROM users where id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loginId);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            if (rs.getInt("admin") == 1) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            exit();
        }
        return false;
    }


    public void matchInfo(String name, String pno) {
        try {
            String sql = "SELECT id FROM users where name = ? and phone = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pstmt.setString(1, name);
            pstmt.setString(2, pno);
            ResultSet rs = pstmt.executeQuery();

            if(!rs.next()) {System.out.println("해당 정보와 일치하는 아이디가 없습니다."); return;}
            else {
                rs.beforeFirst();
                System.out.println("입력하신 내용과 일치하는 계정은 다음과 같습니다.");
                while(rs.next()) {
                    System.out.println("[ " + rs.getString("id") + " ]");
                }
                rs.close();
                pstmt.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            exit();
        }
    }

    public boolean idExist(String id) {
        try {
            String sql = "SELECT id FROM users where id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            ResultSet rs  = pstmt.executeQuery();
            if(rs.next()) {
                rs.close();
                pstmt.close();
                return true;
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    };

    public String newPw() {
        try {
            System.out.println("새로운 비밀번호를 입력해주세요. [6~20자, 영문/숫자/특수문자 모두 포함]");
            System.out.print("> ");
            String newPw = Operation.sc.nextLine();
            newPw = Operation.pwFormatWrong(newPw);
            return newPw;
        } catch (Exception e) {
            e.printStackTrace();
            exit();
            return "fail";
        }
    }

    public void changePw(String id, String newPw) {
        try {
            String sql  = "Update users SET password = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPw);
            pstmt.setString(2, id);
            pstmt.executeUpdate();
            pstmt.close();

            System.out.println("새로운 비밀번호로 변경이 완료되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            exit();
        }
    }

    public void myInfo(String loginId) {
        try {
            String sql = "Select id, name, phone, address, gender FROM users WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loginId);
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.next()) {
                System.out.println(rs.getString("id"));
                System.out.println(rs.getString("name"));
                System.out.println(rs.getString("phone"));
                System.out.println(rs.getString("address"));
                System.out.println(rs.getInt("gender") == 1 ? "남성" : "여성");
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            exit();
        }
    }

    public void list() {
        try {
            String sql = "Select * FROM users ORDER BY 1";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                no = rs.getInt("no");
                id = rs.getString("id");
                name = rs.getString("name");
                pno = rs.getString("phone");
                adrs = rs.getString("address");
                gender = rs.getInt("gender");
                System.out.printf("%d | %s | %s | %s | %s | %d \n", no, id, name, pno, adrs, gender);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            exit();
        }
    }

    public void delete() {
        try {
            String sql = "DELETE users WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, Operation.loginId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            exit();
        }
    }

    public void exit() {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
        if(Operation.loginId != null) {
			updateLastLogout(Operation.loginId);
		}
		System.out.println("** 프로그램 종료 **");
		System.exit(0);
	}
}
