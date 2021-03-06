package cn.gdou.material.frame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import java.util.List;


import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;


import cn.gdou.material.entity.Good;
import cn.gdou.material.entity.Order;
import cn.gdou.material.entity.User;
import cn.gdou.material.factory.ServiceFactory;
import cn.gdou.material.util.Item;
import cn.gdou.material.util.MyFont;

@SuppressWarnings("serial")
public class AddOrderJFrame extends JFrame implements MouseListener, ActionListener{
	JPanel backgroundPanel, labelPanel, contentPanel, buttonPanel;
	// 表格对象
	JTable table;
	int selectedRow;
	OrderManagerJPanel parentPanel;
	JLabel label_good, label_operator,label_company,label_number,label_state;
	JComboBox<Item> select_name,select_state ;
	JTextField  operator,company,number;
	JButton button_add;
	// 获得屏幕的大小
	final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	// 用户对象
	User user;
	public AddOrderJFrame(User user, OrderManagerJPanel parentPanel) {
		this.user = user;
		this.parentPanel = parentPanel;

		initBackgroundPanel();

		this.add(backgroundPanel);
		
		this.setTitle("添加入库单");
		this.setSize(480, 270);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setResizable(false);
	}
	public void initBackgroundPanel() {
		backgroundPanel = new JPanel(new BorderLayout());
		initContentPanel();
		initButtonPanel();
		initLabelPanel();
		
		backgroundPanel.add(labelPanel, "North");
		backgroundPanel.add(contentPanel, "Center");
		backgroundPanel.add(buttonPanel, "South");
	}
	// 初始化商品信息面板
	public void initContentPanel() {
		List<Good> list_goods = null;
		contentPanel = new JPanel(new GridLayout(5, 2));
		label_good = new JLabel("商品名称", JLabel.CENTER);
		label_operator = new JLabel("操作人员", JLabel.CENTER);
		label_company = new JLabel("相关公司", JLabel.CENTER);
		label_number = new JLabel("数量", JLabel.CENTER);
		label_state = new JLabel("出库or入库", JLabel.CENTER);

		// 商品名称下拉框
		select_name = new JComboBox<Item>();
		try {
			 list_goods = ServiceFactory.getGoodServiceInstance().list();
		}catch (Exception e) {
			e.printStackTrace();
		}
		select_name.addItem(new Item("请选择","请选择"));
		
		//将读取到的商品数据遍历到列表中
		if (!list_goods.isEmpty()) {
			for (Good object : list_goods) {
				select_name.addItem(new Item((int) object.getGoodId(),object));
			}
		}
		
		
		select_name.addActionListener(this);
		select_state = new JComboBox<Item>();
		select_state.addItem(new Item("请选择","请选择"));
		select_state.addItem(new Item("0","入库"));
		select_state.addItem(new Item("1","出库"));
		
		
		operator = new JTextField(user.getName());
		operator.setEditable(false);
		company = new JTextField("");
		number = new JTextField("");
		contentPanel.add(label_good);
		contentPanel.add(select_name);
		contentPanel.add(label_operator);
		contentPanel.add(operator);
		contentPanel.add(label_company);
		contentPanel.add(company);
		contentPanel.add(label_number);
		contentPanel.add(number);
		contentPanel.add(label_state);
		contentPanel.add(select_state);

					
		
	}
	// 初始化按钮面板
	public void initButtonPanel() {
		buttonPanel = new JPanel();

		button_add = new JButton("保存");			
		button_add.setFont(MyFont.Static);
		button_add.addMouseListener(this);

		buttonPanel.add(button_add);
	}
	// 初始化label面板
	public void initLabelPanel() {
		labelPanel = new JPanel();

		JLabel title = new JLabel("订单信息");
		title.setFont(MyFont.Static);

		labelPanel.add(title);
	}

		
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == button_add) {

			String number_String = number.getText().trim();
			String operator_String = operator.getText().trim();
			String company_String = company.getText().trim();
			Item it =(Item) select_name.getSelectedItem();
			Item it_1 = (Item) select_state.getSelectedItem();
			
			if ("请选择".equals(it.getValue())) {
				JOptionPane.showMessageDialog(null, "请选择入库商品");
			}else if(number_String.isEmpty()) {
				JOptionPane.showMessageDialog(null, "请输入数量");
			}else if(operator_String.isEmpty()) {
				JOptionPane.showMessageDialog(null, "请输入操作人员");
			}else if(company_String.isEmpty()) {
				JOptionPane.showMessageDialog(null, "请输入相关公司");				
			}else if("请选择".equals(it_1.getValue())) {
				JOptionPane.showMessageDialog(null, "请选择出库or入库");	
			}else{
				Boolean result = null ;
				Good gd = (Good)it.getValue();
				Integer number_int = Integer.valueOf(number_String);
				Order vo = new Order();
				vo.setOrderTime(new Date());
				vo.setGood(gd);
				vo.setCompany(company_String);
				vo.setOperator(operator_String);
				vo.setNumber(number_int);
				if("出库".equals(it_1.getValue())) {
				vo.setState(0);
				}else if("入库".equals(it_1.getValue())) {
				vo.setState(1);
				}				
				try {
					result = ServiceFactory.getOrderServiceInstance().insert(vo);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				if (result) {
					JOptionPane.showMessageDialog(null, "订单添加成功");
					this.setVisible(false);
					parentPanel.refreshTablePanel();
				}else {
					JOptionPane.showMessageDialog(null, "添加订单失败，请检查信息是否有误或与管理员联系");
					this.setVisible(false);
					parentPanel.refreshTablePanel();
				}
				
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
