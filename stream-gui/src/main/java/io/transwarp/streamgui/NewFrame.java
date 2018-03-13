package io.transwarp.streamgui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class NewFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private JButton button1;
    private JButton button2;
    private JTextField textIP;
    private JTextField textPort;
    private JTextField textUserName;
    private JTextField textPassword;
    private JTextField textDatabase;
    private JTextField textTableName;
    private JTextArea textResult;

    private JComboBox<String> dbtypeBox;
    private JComboBox<String> directBox;
    private JMenuBar menuBar;

    private String dbtype = "oracle";
    private String ip;
    private String port;
    private String username;
    private String password;
    private String database;
    private String tablenames;
    private boolean direct = false;

    public NewFrame() {
        super();
        this.setSize(900, 700);
        this.getContentPane().setLayout(null);// 设置布局控制器

        this.add(this.getLabel("db type:", 10, 29, 120, 18), null);// 添加标签
        this.add(this.getDbtypeBox(106, 29, 71, 27), null);// 添加下拉列表框

        this.add(this.getLabel("direct enable: ", 10, 64, 120, 18), null);// 添加标签
        this.add(this.getDirectBox(106, 64, 71, 27), null);// 添加下拉列表框

        this.add(this.getLabel("db ip:", 10, 99, 120, 18), null);// 添加标签
        this.add(this.getTextFieldIP(106, 99, 160, 20), "");// 添加文本框

        this.add(this.getLabel("db port:", 10, 129, 120, 18), null);// 添加标签
        this.add(this.getTextFieldPort(106, 129, 160, 20), "");// 添加文本框

        this.add(this.getLabel("db username:", 10, 159, 120, 18), null);// 添加标签
        this.add(this.getTextFieldUserName(106, 159, 160, 20), "");// 添加文本框

        this.add(this.getLabel("db password:", 10, 189, 120, 18), null);// 添加标签
        this.add(this.getTextFieldPassword(106, 189, 160, 20), "");// 添加文本框

        this.add(this.getLabel("database name:", 10, 219, 120, 18), null);// 添加标签
        this.add(this.getTextFieldDataBase(106, 219, 160, 20), "");// 添加文本框

        this.add(this.getLabel("table name:", 10, 249, 120, 18), null);// 添加标签
        this.add(this.getTextFieldTableName(106, 249, 160, 20), "");// 添加文本框

        this.add(this.getLabel("Copyright © 2016 Xujie. All rights reserved.",
                10, 595, 320, 18), null);// 添加标签

        // this.add(this.getLabel("tablename支持格式:", 10, 360, 320, 18), null);//
        // 添加标签
        // this.add(this.getLabel("<1> tableName", 10, 380, 320, 18), null);
        // this.add(
        // this.getLabel("<2> tableName1;tableName2...", 10, 400, 320, 18),
        // null);
        // this.add(this.getLabel("<3> tableName1:splitKey:mapNum...", 10, 420,
        // 320, 18), null);
        //
        // this.add(this.getLabel("<4> direct模式下可以是tableName1:mapNum", 10, 440,
        // 320, 18), null);

        JScrollPane jsp = new JScrollPane(this.getTextArea(300, 29, 550, 580));
        jsp.setBounds(300, 29, 550, 585);

        this.add(jsp);

        this.setJMenuBar(this.getMenu());// 添加菜单

        this.setTitle("DataLoader V1.0");// 设置窗口标题

        this.add(this.getButton1(), null);// 添加按钮

        this.add(this.getButton2(), null);// 添加按钮

    }

    public static void main(String[] args) {
        NewFrame newFrame = new NewFrame();
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setVisible(true);
    }

    private JMenuBar getMenu() {
        if (menuBar == null) {
            menuBar = new JMenuBar();
            JMenu m1 = new JMenu();
            m1.setText("文件");
            JMenu m2 = new JMenu();
            m2.setText("编辑");
            JMenu m3 = new JMenu();
            m3.setText("帮助");

            JMenuItem item11 = new JMenuItem();
            item11.setText("打开");
            JMenuItem item12 = new JMenuItem();
            item12.setText("保存");
            JMenuItem item13 = new JMenuItem();
            item13.setText("退出");

            JMenuItem item21 = new JMenuItem();
            item21.setText("复制");
            JMenuItem item22 = new JMenuItem();
            item22.setText("拷贝");
            JMenuItem item23 = new JMenuItem();
            item23.setText("剪切");

            JMenuItem item31 = new JMenuItem();
            item31.setText("欢迎");
            JMenuItem item32 = new JMenuItem();
            item32.setText("搜索");
            JMenuItem item33 = new JMenuItem();
            item33.setText("版本信息");

            m1.add(item11);
            m1.add(item12);
            m1.add(item13);

            m2.add(item21);
            m2.add(item22);
            m2.add(item23);

            m3.add(item31);
            m3.add(item32);
            m3.add(item33);

            menuBar.add(m1);
            menuBar.add(m2);
            menuBar.add(m3);
        }
        return menuBar;
    }

    /**
     * 设置下拉列表框
     *
     * @return
     */
    private JComboBox<String> getDbtypeBox(int x, int y, int width, int height) {
        if (dbtypeBox == null) {
            dbtypeBox = new JComboBox<String>();
            dbtypeBox.setBounds(x, y, width, height);
            dbtypeBox.addItem("oracle");
            dbtypeBox.addItem("db2");
            dbtypeBox.addItem("mysql");
            dbtypeBox.setSelectedItem("oracle");
            dbtypeBox.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    int index = dbtypeBox.getSelectedIndex();

                    switch (index) {
                        case 0:
                            dbtype = "oracle";
                            break;
                        case 1:
                            dbtype = "db2";
                            break;
                        case 2:
                            dbtype = "mysql";
                            break;
                        default:
                            dbtype = "oracle";
                            break;
                    }

                    textResult.setText("you pick " + dbtype);

                }
            });// 为下拉列表框添加监听器类

        }
        return dbtypeBox;
    }

    /**
     * 设置下拉列表框
     *
     * @return
     */
    private JComboBox<String> getDirectBox(int x, int y, int width, int height) {
        if (directBox == null) {
            directBox = new JComboBox<String>();
            directBox.setBounds(x, y, width, height);
            directBox.addItem("true");
            directBox.addItem("false");
            directBox.setSelectedItem("false");
            directBox.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    int index = directBox.getSelectedIndex();

                    switch (index) {
                        case 0:
                            direct = true;
                            break;
                        case 1:
                            direct = false;
                            break;
                        default:
                            direct = false;
                            break;
                    }

                }
            });// 为下拉列表框添加监听器类

        }
        return directBox;
    }

    /**
     * 设置标签
     *
     * @return 设置好的标签
     */
    private JLabel getLabel(String labelName, int x, int y, int width,
                            int height) {
        JLabel label1 = new JLabel();
        label1.setBounds(x, y, width, height);
        label1.setText(labelName);
        label1.setToolTipText("JLabel");
        return label1;
    }

    /**
     * 设置按钮
     *
     * @return 设置好的按钮
     */
    private JButton getButton1() {
        if (button1 == null) {
            button1 = new JButton();
            button1.setBounds(106, 290, 120, 27);
            button1.setText("Sqoop 脚本");
            button1.setToolTipText("Sqoop 脚本");
//			button1.addActionListener(new SqoopActionButton());// 添加监听器类，其主要的响应都由监听器类的方法实现
        }
        return button1;
    }

//	private class SqoopActionButton implements ActionListener
//	{
//		Connection connection = null;
//		ResultSet rs = null;
//
//		JDBCUtil ju = new JDBCUtil();
//
//		public void actionPerformed(ActionEvent e)
//		{
//			String[] tablenamess = tablenames.split(";");
//
//			try
//			{
//				if ("oracle".equals(dbtype))
//				{
//					connection = ju.getOracleConnection(textResult, ip, port,
//							username, password, database);
//					textResult.setText("");
//
//					if (connection != null)
//					{
//						for (String table : tablenamess)
//						{
//							String mapstr = "-m 1";
//							String[] tmp = table.split(":");
//							String tablename = tmp[0];
//
//							String[] sqls = getSql(textResult, dbtype + ".txt",
//									tablename, mapstr);
//							String sql = sqls[0];
//
//							String col;
//							int mapnum = 1;
//
//							if (tmp.length > 1)
//							{
//
//								if (!direct && tmp.length != 3)
//								{
//									textResult
//											.setText(textResult.getText()
//													+ "\r\n"
//													+ tablename
//													+ "的格式错误，你可能希望非direct模式下启动多个sqoop map并行采集数据，那你正确格式应该是：tablename:coulmnname:mapnumber!");
//									continue;
//								}
//
//								if (direct)
//								{
//									if (tmp.length == 2)
//									{
//										mapnum = Integer.valueOf(tmp[1]);
//									}
//
//									if (tmp.length == 3)
//									{
//										mapnum = Integer.valueOf(tmp[2]);
//									}
//
//									mapstr = "-m " + mapnum;
//
//									sql = sqls[1];
//
//								} else
//								{
//									col = tmp[1];
//									mapnum = Integer.valueOf(tmp[2]);
//
//									mapstr = "-m " + mapnum
//											+ "  --split-by \"MOD(ORA_HASH( "
//											+ col + "), " + mapnum + ")\" "
//											+ " --boundary-query \"select 0,"
//											+ (mapnum - 1) + " from dual\" ";
//
//								}
//							}
//
//							rs = connection.createStatement().executeQuery(sql);
//							ResultSetMetaData rsmd = rs.getMetaData();
//							int count = rsmd.getColumnCount();
//							String columns = "";
//
//							while (rs.next())
//							{
//								// 打印数据
//								for (int i = 0; i < count; i++)
//								{
//									String column = rs.getString(i + 1);
//									if (column != null && column != "null")
//									{
//										columns = columns + column + " ";
//									}
//
//								}
//								textResult.setText(textResult.getText()
//										+ "\r\n" + columns);
//								columns = "";
//							}
//
//							textResult.setText(textResult.getText() + "\r\n");
//
//						}
//					}
//
//				} else if ("db2".equals(dbtype))
//				{
//					connection = ju.getDB2Connection(textResult, ip, port,
//							username, password, database);
//
//					if (connection != null)
//					{
//						for (String table : tablenamess)
//						{
//							String mapstr = "-m 1";
//							String[] tmp = table.split(":");
//							String tablename = tmp[0];
//
//							String[] sqls = getSql(textResult, dbtype + ".txt",
//									tablename, mapstr);
//							String sql = sqls[0];
//
//							String col = "";
//							int mapnum = 1;
//							if (tmp.length > 1)
//							{
//
//								if (!direct && tmp.length != 3)
//								{
//									textResult
//											.setText(textResult.getText()
//													+ "\r\n"
//													+ tablename
//													+ "的格式错误，你可能希望非direct模式下启动多个sqoop map并行采集数据，那你正确格式应该是：tablename:coulmnname:mapnumber!");
//									continue;
//								}
//
//								if (direct)
//								{
//									if (tmp.length == 2)
//									{
//										mapnum = Integer.valueOf(tmp[1]);
//									}
//
//									if (tmp.length == 3)
//									{
//										mapnum = Integer.valueOf(tmp[2]);
//									}
//
//									mapstr = "-m " + mapnum;
//
//									sql = sqls[1];
//
//								} else
//								{
//									col = tmp[1];
//									mapnum = Integer.valueOf(tmp[2]);
//
//									mapstr = "-m "
//											+ mapnum
//											+ "  --split-by \"MOD(HASHEDVALUE( "
//											+ col + "), " + mapnum + ")\" "
//											+ " --boundary-query \"VALUES (0,"
//											+ (mapnum - 1) + ")\" ";
//								}
//							}
//
//							rs = connection.createStatement().executeQuery(sql);
//							ResultSetMetaData rsmd = rs.getMetaData();
//							int count = rsmd.getColumnCount();
//							String columns = "";
//
//							while (rs.next())
//							{
//								// 打印数据
//								for (int i = 0; i < count; i++)
//								{
//									columns = columns + rs.getString(i + 1)
//											+ " ";
//								}
//								textResult.setText(textResult.getText()
//										+ "\r\n" + columns);
//								columns = "";
//							}
//
//							textResult.setText(textResult.getText() + "\r\n");
//						}
//					}
//				} else if ("mysql".equals(dbtype))
//				{
//					connection = ju.getMysqlConnection(textResult, ip, port,
//							username, password, database);
//
//					if (connection != null)
//					{
//						for (String table : tablenamess)
//						{
//							String mapstr = "-m 1";
//							String[] tmp = table.split(":");
//							String tablename = tmp[0];
//
//							String[] sqls = getSql(textResult, dbtype + ".txt",
//									tablename, mapstr);
//
//							String sql = sqls[0];
//
//							String col = "";
//							int mapnum = 1;
//
//							if (tmp.length > 1)
//							{
//
//								if (!direct && tmp.length != 3)
//								{
//									textResult
//											.setText(textResult.getText()
//													+ "\r\n"
//													+ tablename
//													+ "的格式错误，你可能希望非direct模式下启动多个sqoop map并行采集数据，那你正确格式应该是：tablename:coulmnname:mapnumber!");
//									continue;
//								}
//
//								if (direct)
//								{
//									if (tmp.length == 2)
//									{
//										mapnum = Integer.valueOf(tmp[1]);
//									}
//
//									if (tmp.length == 3)
//									{
//										mapnum = Integer.valueOf(tmp[2]);
//									}
//
//									mapstr = "-m " + mapnum;
//
//								} else
//								{
//									col = tmp[1];
//									mapnum = Integer.valueOf(tmp[2]);
//
//									mapstr = "-m " + mapnum + "  --split-by "
//											+ col;
//								}
//							}
//
//							rs = connection.createStatement().executeQuery(sql);
//							ResultSetMetaData rsmd = rs.getMetaData();
//							int count = rsmd.getColumnCount();
//							String columns = "";
//
//							while (rs.next())
//							{
//								// 打印数据
//								for (int i = 0; i < count; i++)
//								{
//									columns = columns + rs.getString(i + 1)
//											+ " ";
//								}
//								textResult.setText(textResult.getText()
//										+ "\r\n" + columns);
//								columns = "";
//							}
//
//							textResult.setText(textResult.getText() + "\r\n");
//						}
//					}
//
//				}
//			} catch (Exception e1)
//			{
//				textResult.setText(textResult.getText() + "\r\n"
//						+ e1.getMessage());
//			}
//		}
//	}

//	private class InceptorActionButton implements ActionListener
//	{
//		Connection connection = null;
//		ResultSet rs = null;
//
//		JDBCUtil ju = new JDBCUtil();
//
//		public void actionPerformed(ActionEvent e)
//		{
//			String[] tablenamess = tablenames.split(";");
//
//			try
//			{
//				textResult.setText("");
//
//				if ("oracle".equals(dbtype))
//				{
//					connection = ju.getOracleConnection(textResult, ip, port,
//							username, password, database);
//
//					textResult.setText("");
//
//					if (connection != null)
//					{
//						for (String table : tablenamess)
//						{
//							String mapstr = "-m 1";
//							String[] tmp = table.split(":");
//							String tablename = tmp[0];
//
//							String col;
//							int mapnum = 1;
//
//							if (tmp.length > 1)
//							{
//
//								if (!direct && tmp.length != 3)
//								{
//									textResult
//											.setText(textResult.getText()
//													+ "\r\n"
//													+ tablename
//													+ "的格式错误，你可能希望非direct模式下启动多个sqoop map并行采集数据，那你正确格式应该是：tablename:coulmnname:mapnumber!");
//									continue;
//								}
//
//								if (direct)
//								{
//									if (tmp.length == 2)
//									{
//										mapnum = Integer.valueOf(tmp[1]);
//									}
//
//									if (tmp.length == 3)
//									{
//										mapnum = Integer.valueOf(tmp[2]);
//									}
//
//									mapstr = "-m " + mapnum;
//
//								} else
//								{
//									col = tmp[1];
//									mapnum = Integer.valueOf(tmp[2]);
//
//									mapstr = "-m " + mapnum
//											+ "  --split-by \"MOD(ORA_HASH( "
//											+ col + "), " + mapnum + ")\" "
//											+ " --boundary-query \"select 0,"
//											+ (mapnum - 1) + " from dual\" ";
//								}
//							}
//
//							String[] sqls = getSql(textResult, dbtype + ".txt",
//									tablename, mapstr);
//
//							rs = connection.createStatement().executeQuery(
//									sqls[2]);
//							ResultSetMetaData rsmd = rs.getMetaData();
//							int count = rsmd.getColumnCount();
//							String columns = "";
//
//							while (rs.next())
//							{
//								// 打印数据
//								for (int i = 0; i < count; i++)
//								{
//									String column = rs.getString(i + 1);
//									if (column != null && column != "null")
//									{
//										columns = columns + column + " ";
//									}
//
//								}
//								textResult.setText(textResult.getText()
//										+ "\r\n" + columns);
//								columns = "";
//							}
//
//							textResult.setText(textResult.getText() + "\r\n");
//
//						}
//					}
//
//				} else if ("db2".equals(dbtype))
//				{
//					connection = ju.getDB2Connection(textResult, ip, port,
//							username, password, database);
//
//					if (connection != null)
//					{
//						for (String table : tablenamess)
//						{
//							String mapstr = "-m 1";
//							String[] tmp = table.split(":");
//							String tablename = tmp[0];
//
//							String col = "";
//							int mapnum = 1;
//							if (tmp.length > 1)
//							{
//
//								if (!direct && tmp.length != 3)
//								{
//									textResult
//											.setText(textResult.getText()
//													+ "\r\n"
//													+ tablename
//													+ "的格式错误，你可能希望非direct模式下启动多个sqoop map并行采集数据，那你正确格式应该是：tablename:coulmnname:mapnumber!");
//									continue;
//								}
//
//								if (direct)
//								{
//									if (tmp.length == 2)
//									{
//										mapnum = Integer.valueOf(tmp[1]);
//									}
//
//									if (tmp.length == 3)
//									{
//										mapnum = Integer.valueOf(tmp[2]);
//									}
//
//									mapstr = "-m " + mapnum;
//
//								} else
//								{
//									col = tmp[1];
//									mapnum = Integer.valueOf(tmp[2]);
//
//									mapstr = "-m "
//											+ mapnum
//											+ "  --split-by \"MOD(HASHEDVALUE( "
//											+ col + "), " + mapnum + ")\" "
//											+ " --boundary-query \"VALUES (0,"
//											+ (mapnum - 1) + ")\" ";
//								}
//							}
//
//							String[] sqls = getSql(textResult, dbtype + ".txt",
//									tablename, mapstr);
//
//							rs = connection.createStatement().executeQuery(
//									sqls[2]);
//							ResultSetMetaData rsmd = rs.getMetaData();
//							int count = rsmd.getColumnCount();
//							String columns = "";
//
//							while (rs.next())
//							{
//								// 打印数据
//								for (int i = 0; i < count; i++)
//								{
//									columns = columns + rs.getString(i + 1)
//											+ " ";
//								}
//								textResult.setText(textResult.getText()
//										+ "\r\n" + columns);
//								columns = "";
//							}
//
//							textResult.setText(textResult.getText() + "\r\n");
//						}
//					}
//				} else if ("mysql".equals(dbtype))
//				{
//					connection = ju.getMysqlConnection(textResult, ip, port,
//							username, password, database);
//
//					if (connection != null)
//					{
//						for (String table : tablenamess)
//						{
//							String mapstr = "-m 1";
//							String[] tmp = table.split(":");
//							String tablename = tmp[0];
//
//							// textResult.setText(textResult.getText() + "\r\n"
//							// + "--================" + tablename
//							// + "================--");
//
//							String col = "";
//							int mapnum = 1;
//
//							if (tmp.length > 1)
//							{
//
//								if (!direct && tmp.length != 3)
//								{
//									textResult
//											.setText(textResult.getText()
//													+ "\r\n"
//													+ tablename
//													+ "的格式错误，你可能希望非direct模式下启动多个sqoop map并行采集数据，那你正确格式应该是：tablename:coulmnname:mapnumber!");
//									continue;
//								}
//
//								if (direct)
//								{
//									if (tmp.length == 2)
//									{
//										mapnum = Integer.valueOf(tmp[1]);
//									}
//
//									if (tmp.length == 3)
//									{
//										mapnum = Integer.valueOf(tmp[2]);
//									}
//
//									mapstr = "-m " + mapnum;
//
//								} else
//								{
//									col = tmp[1];
//									mapnum = Integer.valueOf(tmp[2]);
//
//									mapstr = "-m " + mapnum + "  --split-by "
//											+ col;
//								}
//							}
//
//							String[] sqls = getSql(textResult, dbtype + ".txt",
//									tablename, mapstr);
//
//							rs = connection.createStatement().executeQuery(
//									sqls[1]);
//							ResultSetMetaData rsmd = rs.getMetaData();
//							int count = rsmd.getColumnCount();
//							String columns = "";
//
//							while (rs.next())
//							{
//								// 打印数据
//								for (int i = 0; i < count; i++)
//								{
//									columns = columns + rs.getString(i + 1)
//											+ " ";
//								}
//								textResult.setText(textResult.getText()
//										+ "\r\n" + columns);
//								columns = "";
//							}
//
//							textResult.setText(textResult.getText() + "\r\n");
//						}
//					}
//				}
//
//			} catch (Exception e1)
//			{
//				textResult.setText(textResult.getText() + "\r\n"
//						+ e1.getMessage());
//			}
//		}
//	}

    /**
     * 设置按钮
     *
     * @return 设置好的按钮
     */
    private JButton getButton2() {
        if (button2 == null) {
            button2 = new JButton();
            button2.setBounds(106, 330, 120, 27);
            button2.setText("Inceptor 外表");
            button2.setToolTipText("Inceptor 外表");
//			button2.addActionListener(new InceptorActionButton());// 添加监听器类，其主要的响应都由监听器类的方法实现
        }
        return button2;
    }

    private JTextField getTextFieldUserName(int x, int y, int width, int height) {
        textUserName = new JTextField();
        textUserName.setBounds(x, y, width, height);
        textUserName.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                String txt = null;
                try {
                    txt = e.getDocument().getText(
                            e.getDocument().getStartPosition().getOffset(),
                            e.getDocument().getLength());
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
                username = txt;
                // textResult.setText(textResult.getText() + "\r\n" +
                // "username: "+ username);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        return textUserName;
    }

    /**
     * 设定文本域
     *
     * @return
     */
    private JTextField getTextFieldPassword(int x, int y, int width, int height) {
        textPassword = new JTextField();
        textPassword.setBounds(x, y, width, height);
        textPassword.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                String txt = null;
                try {
                    txt = e.getDocument().getText(
                            e.getDocument().getStartPosition().getOffset(),
                            e.getDocument().getLength());
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
                password = txt;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        return textPassword;
    }

    private JTextField getTextFieldIP(int x, int y, int width, int height) {
        textIP = new JTextField();
        textIP.setBounds(x, y, width, height);
        textIP.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                String txt = null;
                try {
                    txt = e.getDocument().getText(
                            e.getDocument().getStartPosition().getOffset(),
                            e.getDocument().getLength());
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
                ip = txt;
                // textResult.setText(textResult.getText() + "\r\n" + "ip: " +
                // ip);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }

        });

        return textIP;
    }

    private JTextField getTextFieldPort(int x, int y, int width, int height) {
        textPort = new JTextField();
        textPort.setBounds(x, y, width, height);
        textPort.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                String txt = null;
                try {
                    txt = e.getDocument().getText(
                            e.getDocument().getStartPosition().getOffset(),
                            e.getDocument().getLength());
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
                port = txt;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        return textPort;
    }

    private JTextField getTextFieldDataBase(int x, int y, int width, int height) {
        textDatabase = new JTextField();
        textDatabase.setBounds(x, y, width, height);
        textDatabase.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                String txt = null;
                try {
                    txt = e.getDocument().getText(
                            e.getDocument().getStartPosition().getOffset(),
                            e.getDocument().getLength());
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
                database = txt;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        return textDatabase;
    }

    private JTextField getTextFieldTableName(int x, int y, int width, int height) {
        textTableName = new JTextField();
        textTableName.setBounds(x, y, width, height);
        textTableName.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                String txt = null;
                try {
                    txt = e.getDocument().getText(
                            e.getDocument().getStartPosition().getOffset(),
                            e.getDocument().getLength());
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
                tablenames = txt;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        return textTableName;
    }

    private JTextArea getTextArea(int x, int y, int width, int height) {
        textResult = new JTextArea();
        textResult.setBounds(x, y, width, height);
        textResult.setLineWrap(true);
        textResult.setWrapStyleWord(true);

        return textResult;
    }

    public String[] getSql(JTextArea textResult, String filename,
                           String tablename, String mapstr) {

        String[] sqls = new String[3];
        try {
            File file = new File("conf/" + filename);
            String fileContent = "";
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file));
                BufferedReader br = new BufferedReader(read);
                String line = null;
                while ((line = br.readLine()) != null) {
                    fileContent = fileContent + " " + line;
                }

                fileContent = fileContent.replace("#ip#", ip);
                fileContent = fileContent.replace("#port#", port);
                fileContent = fileContent.replace("#database#", database);
                fileContent = fileContent.replace("#username#", username);
                fileContent = fileContent.replace("#password#", password);
                fileContent = fileContent.replace("#mapstr#", mapstr);
                sqls = fileContent.replace("#tablename#", tablename).split(" ;");

                read.close();
            } else {
                textResult.setText(textResult.getText() + "\r\n"
                        + "conf目录不存在配置文件：" + filename);
            }
        } catch (Exception e) {
            textResult.setText(textResult.getText() + "\r\n" + "读取文件出错");
        }

        return sqls;
    }
}
