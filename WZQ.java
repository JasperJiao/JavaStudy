package wuziqi;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

/**
 * 			1.����awt��һ�� 15*15������
        	2.�����ʱ���ȡ�����ڸ������ϻ�һ������   �ҷ�����  ���Ժ���
        	3.����3άӮ������  5��������������һ��Ӯ��   ����  ����  ��б�� ��б��   ��Ӯ����������
        	ǰ2λ��������5������   ���һλ��Ӯ���ı��
        			wins[0][0][2]=true
                	wins[1][0][2]=true
                	wins[2][0][2]=true
                	wins[3][0][2]=true
                	wins[4][0][2]=true
            4.����2������ֱ�����ҷ�ռ�õ�Ӯ����ź͵��Է�ռ�õ�Ӯ�����
            5.���ҷ���һ����ʱ��ѭ������Ӯ����ȷ�������������ڵ�Ӯ��������Ǹ�Ӯ�������ҷ��������÷���
            				myWin[k]++;ͬʱ��ע�����Բ������ڴ�Ӯ���ϻ�ʤ
                            computerWin[k] = 6;//���λ�ü����������Ӯ�ˣ�ָ��һ����Ч������
            6.���ҷ�һ��Ӯ����ŵ���5ʱ��ʤ myWin[k]==5�������õ�������
            
            7.����ͳ�����пհ����꣬ȷ��һ����ֵ������������
 * @author Administrator
 *
 */
public class WZQ {

	public static void main(String[] args) {
		
		Frame f = new Frame();
		f.add(new MyPanel());
		f.setVisible(true);
		f.setBounds(200, 50, 620, 630);
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
	}
	
}
class MyPanel extends Panel{
	public static final int MARGIN=20;//�߾�  
	public static final int GRID_SPAN=40;//������  
	public static final int COLS=24;//���������� 
	public static final int R=20;//����������
	List points=null;
	
	boolean over = false;//�ж���Ϸ�Ƿ����
    boolean me = true; //��
    int[][] chressBord = new int[15][15];//��¼����ÿ�������Ƿ�������  0=û��  1=��
    int count = 0; //Ӯ������
    int[] myWin;
    int[] computerWin;
    //�洢Ӯ�������飬��ά����       5���������������һ��Ӯ��
    boolean[][][] wins = new boolean[15][15][1000];
    {
    	//����Ӯ��
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 11; j++){
            	//kѭ��ֻ��Ϊ��������5������
                for(int k = 0; k < 5; k++){
                    wins[j+k][i][count] = true;
                }
                count++;
            }
        }
        //����Ӯ��
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 11; j++){
            	//kѭ��ֻ��Ϊ��������5������
                for(int k = 0; k < 5; k++){
                    wins[i][j+k][count] = true;
                }
                count++;
            }
        }
        
        //��б��Ӯ��
        for(int i = 0; i < 11; i++){
            for(int j = 0; j < 11; j++){
                for(int k = 0; k < 5; k++){
                    wins[i+k][j+k][count] = true;
                }
                count++;
            }
        }
        //��б��Ӯ��
        for(int i = 0; i < 11; i++){ 
            for(int j = 14; j > 3; j--){
                for(int k = 0; k < 5; k++){
                    wins[i+k][j-k][count] = true;
                }
                count++;
            }
        }
        //ͳ������ռ�õ�Ӯ���Լ���Ӯ���ķ���
        myWin = new int[count];
        //ͳ�Ƽ������ռ�õ�Ӯ���Լ���Ӯ���ķ���
        computerWin = new int[count];
    }
	public MyPanel(){
		points = new ArrayList(); 
		setBackground(new Color(100,100,100));
		addMouseListener(new Monitor());
		setVisible(true);
	}
	@Override
	public void paint(Graphics g) {
		for(int i = 0; i < 15; i++){
			//������  (COLS-1)*GRID_SPAN+MARGIN = 580
			g.setColor(Color.red);
    		g.drawLine(MARGIN+i*GRID_SPAN, MARGIN, MARGIN+i*GRID_SPAN, 580);
    		//������
    		g.setColor(Color.red);
    		g.drawLine(MARGIN, MARGIN+i*GRID_SPAN, 580, MARGIN+i*GRID_SPAN);
        }
		Iterator i = points.iterator();
	    while(i.hasNext()){
	      Point p = (Point)i.next();
	      g.setColor(p.getColor());
	      g.fillArc(p.getX()*GRID_SPAN+MARGIN-R/2,p.getY()*GRID_SPAN+MARGIN-R/2,R,R,0,360);
	      
	    }
	}
	
	class Monitor extends MouseAdapter {
		  public void mousePressed(MouseEvent e) {
			  
			  //�����Ϸ������������
	          if(over){
	              return;
	          }
	          //������������岻������
	          if(!me){
	              return;
	          }
	          //�������ϵ��������
	          int x = e.getX();
	          int y = e.getY();
	          //����40����ȡ���ܹ���֤ i��j��ֵ��0-15������ �ֱ���������ϵ�һ������
	          int i = x / GRID_SPAN;
	          int j = y / GRID_SPAN;
	          //�жϸ������Ƿ��Ѿ�������
	          if(chressBord[i][j] == 0){
	          	 //��ָ�������ϻ�����
	              //oneStep(i,j,me);
	        	  points.add(new Point(i,j,Color.BLUE));
	              //���������긳ֵΪ1
	              chressBord[i][j] = 1; //�����õ�������  
	              //��ȡ���������ڵ�Ӯ��
	              for(int k = 0; k < count; k++){ // ������Ӯ���������1
	                  if(wins[i][j][k]){
	                      // ���ڸ�Ӯ���ϼ�1;
	                      myWin[k]++;
	                      computerWin[k] = 6;//���λ�ü����������Ӯ�ˣ�ָ��һ����Ч������
	                      if(myWin[k] == 5){
	                    	  JOptionPane.showMessageDialog(null, "��ϲ���ʤ", "����",JOptionPane.WARNING_MESSAGE);
	                          System.out.println("��ϲ���ʤ");
	                    	  over = true;
	                      }
	                  }
	              }
	              if(!over){
	                  me = !me;
	                  computerAI();
	              }
	          } 
			//���»���
	        MyPanel f = (MyPanel)e.getSource();
		    f.repaint();
		 }
		 
		// ���������
        public void computerAI(){
          	  //ͳ������һ���հ����ϵķ���
              int[][] myScore = new int[15][15];
              //ͳ�Ƽ������һ���հ����ϵķ���
              int[][] computerScore = new int[15][15];
              //Ӯ���е�����ֵ
              int max = 0;
              //��������������
              int u = 0, v = 0;
              //�����еĿհ����궼��һ��������������������ڵ�Ӯ������û�з����Ͳ����㣩
              for(int i = 0; i < 15; i++){
                  for(int j = 0; j < 15; j++){
                      //������������ӣ��жϸ����������е�Ӯ��������Ӯ��������������ֵ������ÿ��Ӯ��
                      //����һ����ֵ
                      if(chressBord[i][j] == 0){
                          //����ÿ��Ӯ����
                          for(int k = 0; k < count; k++){
                              if(wins[i][j][k]){
                                 //�ֱ�ͳ�Ƹõ�����˺ͼ�������� ˭���������
                                 //���Ѿ��ڸ�Ӯ��������һ�ӣ�Ҫ�ڸõ�������
                                 //����λ�ð������Ӯ�����ۼ�
                                 if(myWin[k] == 1){
                                      myScore[i][j] += 200;
                                  }else if(myWin[k] == 2){
                                      myScore[i][j] += 400;
                                  }else if(myWin[k] == 3){
                                      myScore[i][j] += 2000;
                                  }else if(myWin[k] == 4){
                                      myScore[i][j] += 10000;
                                  }
                                  
                                  //����ռ�õ�Ӯ��  ���Ӷ��ڵ��Եļ�ֵ
                                  if(computerWin[k] == 1){
                                      computerScore[i][j] += 220;
                                  }else if(computerWin[k] == 2){
                                      computerScore[i][j] += 420;
                                  }else if(computerWin[k] == 3){
                                      computerScore[i][j] += 2100;
                                  }else if(computerWin[k] == 4){
                                      computerScore[i][j] += 20000;
                                  }                        
                              }
                          }
                          
                          if(myScore[i][j] > max){
                              max  = myScore[i][j];
                              u = i;
                              v = j;
                          }else if(myScore[i][j] == max){
                              //�������ʱ�ж��¼�����ڸõ��Ƿ������֮ǰ�ĵ������
                              if(computerScore[i][j] > computerScore[u][v]){
                                  u = i;
                                  v = j;    
                              }
                          }
                          
                          if(computerScore[i][j] > max){
                              max  = computerScore[i][j];
                              u = i;
                              v = j;
                          }else if(computerScore[i][j] == max){
                              if(myScore[i][j] > myScore[u][v]){
                                  u = i;
                                  v = j;    
                              }
                          }
                          
                      }
                  }
              }
              points.add(new Point(u,v,Color.red));
              chressBord[u][v] = 1;  //�����õ�������
              for(int k = 0; k < count; k++){
                  if(wins[u][v][k]){
                      computerWin[k]++;
                      myWin[k] = 6;//���λ�öԷ�������Ӯ��
                      if(computerWin[k] == 5){
                    	  JOptionPane.showMessageDialog(null, "�������ʤ", "����",JOptionPane.WARNING_MESSAGE);
                          System.out.println("�������ʤ");
                          over = true;
                      }
                  }
              }
              if(!over){
                  me = !me;
              }
           
          }
	}
}

