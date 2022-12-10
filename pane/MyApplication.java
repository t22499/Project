package pane;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import sun.applet.Main;

import java.util.function.Predicate;

public class MyApplication extends Application{
	private  int  width = 560;//棋盘宽度
	private  int  height = 600;//棋盘高度
	private  int  padding = 40;//棋盘中线与线之间的距离
	private  int  margin = 20;//棋盘中线距离棋盘边的距离
	private  int  lineCount = 14;//棋盘中水平线的垂直线的个数
	private  Pane pane = null;//定义画板对象
	private  Stage stage = null;//舞台对象
	private  boolean isBlack = true;//是否为黑色，true为黑，false就为白
	private Chess[] chesses = new Chess[lineCount * lineCount];//装棋子的容器
	private int count = 0;//索引
	private int isWinCount = 1;//判断连续棋子的个数
	private boolean isWin = false;//判断是否胜利的标记，true代表胜利，false代表未胜利




	public void start(Stage stage) throws Exception {
		this.pane = getPane();
		this.stage = stage;


		//落子
		pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				double x = event.getX();
				double y = event.getY();

                if(!(x >=20 && x <= 540 && y >= 20 && y <= 540)){
					return;
				}

                int x1 = ((int)x - margin +padding/2)/padding;
                int y1 = ((int)y - margin +padding/2)/padding;
//				System.out.println(x1 + "..." + y1);

				//判断_x和_y坐标的位置上是否有棋子，有的话，就不继续向下执行了
				if(isHas(x1, y1)) {
					//System.out.println("该坐标上有棋子");
					return;
				}



				//创建圆圈对象，并设置一些参数
				Circle circle = null;
				//创建棋子对象
				Chess chess = null;

				if(isBlack){
					//黑色
					circle = new Circle(x1 * padding + margin, y1 * padding + margin, 10,Color.BLACK);
					isBlack = false;
					chess = new Chess(x1,y1,Color.BLACK);
				}else {
					//白色
					circle = new Circle(x1 * padding + margin, y1 * padding + margin, 10,Color.WHITE);
					isBlack = true;
					chess = new Chess(x1,y1,Color.WHITE);
				}
				//将圆圈放入画板上
				pane.getChildren().add(circle);
				//向容器中存储一个棋子对象
				chesses[count] = chess;
				count++;

				if(isWin(chess)){
					//弹框
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					//设置文字说明
					alert.setTitle("标题");
					alert.setHeaderText("你胜利了");
					alert.setContentText("好棒啊");

					alert.showAndWait();//展示弹框
//					System.out.println("aaa");


					isWin = true;

				}



			}
		});
		
		//创建场景对象
		Scene scene = new Scene(pane,width, height);
		//将场景放入舞台
		stage.setScene(scene);
		//展示舞台
		stage.show();
		
	}

	//判断胜利机制
	private boolean isWin(Chess chess){
		int x = chess.getX();
		int y = chess.getY();

		//水平方向判断

		//向右
		for(int i = x + 1; i <= x + 4 && i <= 13; i++) {
			//判断这个(i, y)坐标，有没有棋子，颜色又是什么
			Chess _chess = getChess(i, y);
			if(_chess != null && chess.getColor().equals(_chess.getColor())) {
				isWinCount++;
			} else {
				break;
			}
		}

		//向左
		for(int i = x - 1; i >= x - 4 && i >= 0; i--) {
			//判断这个(i, y)坐标，有没有棋子，颜色又是什么
			Chess _chess = getChess(i, y);
			if(_chess != null && chess.getColor().equals(_chess.getColor())) {
				isWinCount++;
			} else {
				break;
			}
		}









		//判断计数器的个数是否大于等于5个
		if(isWinCount >= 5) {
			isWinCount = 1;
			return true;
		}

		//判断垂直方向
		isWinCount = 1;

		return false;
	}




	//获取指定坐标处的棋子对象
	private Chess getChess(int x, int y) {
		for(int i = 0; i < count; i++) {
			Chess chess = chesses[i];
			if(chess.getX() == x && chess.getY() == y) {
				return chess;
			}
		}
		return null;
	}








	//判断是否已经有棋子了
	private boolean isHas(int x,int y){
		for(int i = 0; i < count; i++){
			Chess chess = chesses[i];
			if(chess.getX() == x && chess.getY() == y){
				return true;
			}
		}
		return false;
	}






	//创建画板对象，将各个东西扔到画板上
	   private Pane getPane() {  	
	        //创建画板对象
			Pane pane = new Pane();
			//设置画板颜色
			pane.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
			//创建线条对象
			int increment = 0;
			for(int i = 0; i < lineCount; i++) {
				Line rowLine = new Line(margin, margin + increment, width - margin, margin + increment);
				Line colLine = new Line(margin + increment, margin, margin + increment,  width - margin);
				//将线条放到画板中
				pane.getChildren().add(rowLine);
				pane.getChildren().add(colLine);
				
					increment += padding;
			}

			Button startButton1 = getStartButton();
			Button startButton2 = getSaveButtton();

			pane.getChildren().add(startButton1);
			pane.getChildren().add(startButton2);




			return pane;



	}
	//保存棋谱
	private Button getSaveButtton(){
		//添加按钮对象
		Button startButton2 = new Button("保存棋谱");
		//设置按钮大小
		startButton2.setPrefSize(80, 30);
		//设置x和y的坐标
		startButton2.setLayoutX(120);
		startButton2.setLayoutY(550);


		return startButton2;
	}



	//再来一局
     private Button getStartButton() {
		 //添加按钮对象
		 Button startButton1 = new Button("再来一局");

		 //设置按钮大小
		 startButton1.setPrefSize(80, 30);

		 //设置x和y的坐标
		 startButton1.setLayoutX(20);
		 startButton1.setLayoutY(550);

		 //给按钮对象绑定鼠标点击事件
		 startButton1.setOnAction(new EventHandler<ActionEvent>(){

				 public void handle(ActionEvent event) {
					 //再来一局实现

					 //清空画板上的圆圈Circle对象
					 pane.getChildren().removeIf(new Predicate<Object>() {
						 public boolean test(Object t) {
							 return t instanceof  Circle;
						 }
					 });

					 //棋子容器清空

					 chesses = new Chess[lineCount * lineCount];

					 //计数器归0
					 count = 0;

					 //胜负标记归为false
					 isWin = false;
					 //将黑白标记置为true
					 isBlack = true;


			 }
		 });





		 return startButton1;

	}




	//程序执行的入口
	public static void main(String[] args) {
		launch(args);
	}
	

}
