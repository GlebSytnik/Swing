import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

public class Main3 {
    public static JTable jt;
    String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gym1?serverTimezone=UTC";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "root";
    public static final String GET_COACH = "SELECT * FROM coach";

    public static void main(String[] args) throws ClassNotFoundException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        JFrame frame = new JFrame("Board");
        frame.setSize(300, 400);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(3, 3));
        //panel.setBackground(Color.BLUE);

        JButton button = new JButton("Coach");
        JButton button2 = new JButton("Customers");
        JButton button3 = new JButton("Train");
        panel.add(button);
        panel.add(button2);
        panel.add(button3);
        button.addActionListener(new Main3.ButtonActionListener());
        // frame.add(button,BorderLayout.PAGE_START);
        //frame.add(button2,BorderLayout.PAGE_END);
        frame.add(panel, BorderLayout.WEST);

        frame.setVisible(true);
    }

    public static class ButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList columnNames = new ArrayList();
            ArrayList data = new ArrayList();

            //  Connect to an MySQL Database, run query, get result set
            String url = "jdbc:mysql://localhost:3306/gym1?serverTimezone=UTC";
            String userid = "root";
            String password = "root";
            String sql = "SELECT * FROM  coach";

            // Java SE 7 has try-with-resources
            // This will ensure that the sql objects are closed when the program
            // is finished with them
            try (Connection connection = DriverManager.getConnection(url, userid, password);
                 Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                ResultSetMetaData md = rs.getMetaData();
                int columns = md.getColumnCount();

                //  Get column names
                for (int i = 1; i <= columns; i++) {
                    columnNames.add(md.getColumnName(i));
                }

                //  Get row data
                while (rs.next()) {
                    ArrayList row = new ArrayList(columns);

                    for (int i = 1; i <= columns; i++) {
                        row.add(rs.getObject(i));
                    }

                    data.add(row);
                }
            } catch (SQLException q) {
                System.out.println(q.getMessage());
            }

            // Create Vectors and copy over elements from ArrayLists to them
            // Vector is deprecated but I am using them in this example to keep
            // things simple - the best practice would be to create a custom defined
            // class which inherits from the AbstractTableModel class
            Vector columnNamesVector = new Vector();
            Vector dataVector = new Vector();

            for (int i = 0; i < data.size(); i++) {
                ArrayList subArray = (ArrayList) data.get(i);
                Vector subVector = new Vector();
                for (int j = 0; j < subArray.size(); j++) {
                    subVector.add(subArray.get(j));
                }
                dataVector.add(subVector);
            }

            for (int i = 0; i < columnNames.size(); i++)
                columnNamesVector.add(columnNames.get(i));

            //  Create table with database data
            JTable table = new JTable(dataVector, columnNamesVector) {

            };
            JScrollPane scrollPane = new JScrollPane(table);
            JPanel buttonPanel = new JPanel();

        }

    }
}
