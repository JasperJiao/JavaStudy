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
 * 			1.利用awt画一个 15*15的棋盘
        	2.点击的时候获取坐标在该坐标上画一个棋子   我方白棋  电脑黑棋
        	3.创建3维赢法数组  5个连续的坐标是一个赢法   横线  竖线  正斜线 反斜线   ，赢法输入如下
        	前2位是连续的5个坐标   最后一位是赢法的编号
        			wins[0][0][2]=true
                	wins[1][0][2]=true
                	wins[2][0][2]=true
                	wins[3][0][2]=true
                	wins[4][0][2]=true
            4.定义2个数组分别代表我方占用的赢法编号和电脑方占用的赢法编号
            5.当我方下一个子时，循环所有赢法以确定该子坐标所在的赢法，并标记该赢法属于我方，并设置分数
            				myWin[k]++;同时标注电脑以不可能在此赢法上获胜
                            computerWin[k] = 6;//这个位置计算机不可能赢了，指定一个无效的数字
            6.当我方一个赢法编号等于5时获胜 myWin[k]==5，否则让电脑下子
            
            7.电脑统计所有空白坐标，确定一个分值最大的坐标落子
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
	public static final int MARGIN=20;//边距  
	public static final int GRID_SPAN=40;//网格间距  
	public static final int COLS=24;//棋盘行列数 
	public static final int R=20;//棋盘行列数
	List points=null;
	
	boolean over = false;//判断游戏是否结束
    boolean me = true; //我
    int[][] chressBord = new int[15][15];//记录棋盘每个坐标是否有棋子  0=没有  1=有
    int count = 0; //赢法总数
    int[] myWin;
    int[] computerWin;
    //存储赢法的数组，三维数组       5个连续的坐标代表一个赢法
    boolean[][][] wins = new boolean[15][15][1000];
    {
    	//横线赢法
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 11; j++){
            	//k循环只是为了连起来5个坐标
                for(int k = 0; k < 5; k++){
                    wins[j+k][i][count] = true;
                }
                count++;
            }
        }
        //竖线赢法
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 11; j++){
            	//k循环只是为了连起来5个坐标
                for(int k = 0; k < 5; k++){
                    wins[i][j+k][count] = true;
                }
                count++;
            }
        }
        
        //正斜线赢法
        for(int i = 0; i < 11; i++){
            for(int j = 0; j < 11; j++){
                for(int k = 0; k < 5; k++){
                    wins[i+k][j+k][count] = true;
                }
                count++;
            }
        }
        //反斜线赢法
        for(int i = 0; i < 11; i++){ 
            for(int j = 14; j > 3; j--){
                for(int k = 0; k < 5; k++){
                    wins[i+k][j-k][count] = true;
                }
                count++;
            }
        }
        //统计我所占用的赢法以及该赢法的分数
        myWin = new int[count];
        //统计计算机所占用的赢法以及该赢法的分数
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
			//画竖线  (COLS-1)*GRID_SPAN+MARGIN = 580
			g.setColor(Color.red);
    		g.drawLine(MARGIN+i*GRID_SPAN, MARGIN, MARGIN+i*GRID_SPAN, 580);
    		//画横线
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
			  
			  //如果游戏结束不可下棋
	          if(over){
	              return;
	          }
	          //如果不该我下棋不可下棋
	          if(!me){
	              return;
	          }
	          //在棋盘上点击的坐标
	          int x = e.getX();
	          int y = e.getY();
	          //除以40向下取整能够保证 i和j的值是0-15的整数 分别代表棋盘上的一个坐标
	          int i = x / GRID_SPAN;
	          int j = y / GRID_SPAN;
	          //判断该坐标是否已经有棋子
	          if(chressBord[i][j] == 0){
	          	 //在指定坐标上画棋子
	              //oneStep(i,j,me);
	        	  points.add(new Point(i,j,Color.BLUE));
	              //有棋子坐标赋值为1
	              chressBord[i][j] = 1; //标明该点已落子  
	              //获取该坐标所在的赢法
	              for(int k = 0; k < count; k++){ // 将可能赢的情况都加1
	                  if(wins[i][j][k]){
	                      // 我在该赢法上加1;
	                      myWin[k]++;
	                      computerWin[k] = 6;//这个位置计算机不可能赢了，指定一个无效的数字
	                      if(myWin[k] == 5){
	                    	  JOptionPane.showMessageDialog(null, "恭喜你获胜", "标题",JOptionPane.WARNING_MESSAGE);
	                          System.out.println("恭喜你获胜");
	                    	  over = true;
	                      }
	                  }
	              }
	              if(!over){
	                  me = !me;
	                  computerAI();
	              }
	          } 
			//重新绘制
	        MyPanel f = (MyPanel)e.getSource();
		    f.repaint();
		 }
		 
		// 计算机下棋
        public void computerAI(){
          	  //统计我在一个空白子上的分数
              int[][] myScore = new int[15][15];
              //统计计算机在一个空白子上的分数
              int[][] computerScore = new int[15][15];
              //赢法中的最大分值
              int max = 0;
              //计算机下棋的坐标
              int u = 0, v = 0;
              //给所有的空白坐标都算一个分数（如果该坐标所在的赢法数组没有分数就不计算）
              for(int i = 0; i < 15; i++){
                  for(int j = 0; j < 15; j++){
                      //如果该坐标无子，判断该坐标上所有的赢法，根据赢法的棋子数量赋值，这样每个赢法
                      //都有一个分值
                      if(chressBord[i][j] == 0){
                          //遍历每个赢法，
                          for(int k = 0; k < count; k++){
                              if(wins[i][j][k]){
                                 //分别统计该点对于人和计算机而言 谁的利益最大
                                 //人已经在该赢法上下了一子，要在该点做拦截
                                 //若该位置包含多个赢法将累加
                                 if(myWin[k] == 1){
                                      myScore[i][j] += 200;
                                  }else if(myWin[k] == 2){
                                      myScore[i][j] += 400;
                                  }else if(myWin[k] == 3){
                                      myScore[i][j] += 2000;
                                  }else if(myWin[k] == 4){
                                      myScore[i][j] += 10000;
                                  }
                                  
                                  //电脑占用的赢法  该子对于电脑的价值
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
                              //分数相等时判断下计算机在该点是否分数比之前的点分数大
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
              chressBord[u][v] = 1;  //表明该点已落子
              for(int k = 0; k < count; k++){
                  if(wins[u][v][k]){
                      computerWin[k]++;
                      myWin[k] = 6;//这个位置对方不可能赢了
                      if(computerWin[k] == 5){
                    	  JOptionPane.showMessageDialog(null, "计算机获胜", "标题",JOptionPane.WARNING_MESSAGE);
                          System.out.println("计算机获胜");
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

