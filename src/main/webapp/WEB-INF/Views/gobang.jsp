<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>五子棋</title>
    <style type="text/css">
        canvas{
        display: block;
        margin: 50px auto;
        box-shadow: -2px -2px 2px #F3F2F2, 5px 5px 5px #6F6767;
        }
    </style>
</head>
<body>
    <canvas id="mycanvas" width="450px" height="450px"></canvas>
    <script type="text/javascript" src="http://127.0.0.1:8080/lzy/js/jquery-1.10.2.min.js"></script>
    <script type="text/javascript">
        var chess = document.getElementById("mycanvas");
        var context = chess.getContext('2d');
        var chessBox = [];//用于存放棋盘中落子的情况
        var me = {};
        me.Blank=0;
        me.Black=1;
        me.White=2;
        var chessName=[,"黑棋","白旗"];
        var chessDataList=[];
        for(var i=0;i<15;i++){
            chessBox[i]=[];
            for(var j=0;j<15;j++){
                chessBox[i][j]=0;//初始值为0
                chessBox.count=15*15;
            }
        }
        function drawChessBoard(){
            for(var i=0;i<15;i++){
                context.strokeStyle="#D6D1D1";
                context.moveTo(15+i*30,15);//垂直方向画15根线，相距30px;
                context.lineTo(15+i*30,435);
                context.stroke();
                context.moveTo(15,15+i*30);//水平方向画15根线，相距30px;棋盘为14*14；
                context.lineTo(435,15+i*30);
                context.stroke();
            }
        }
        drawChessBoard();//绘制棋盘
        function oneStep(i,j,k){
            context.beginPath();
            context.arc(15+i*30,15+j*30,13,0,2*Math.PI);//绘制棋子
            var g=context.createRadialGradient(15+i*30,15+j*30,13,15+i*30,15+j*30,0);//设置渐变
            if(k==me.Black){                           //k=true是黑棋，否则是白棋
                g.addColorStop(0,'#0A0A0A');//黑棋
                g.addColorStop(1,'#636766');
            }else {
                g.addColorStop(0,'#D1D1D1');//白棋
                g.addColorStop(1,'#F9F9F9');
            }
            context.fillStyle=g;
            context.fill();
            context.closePath();
            chessBox.count--;
            isEnd(i,j,k);
        }
        chess.onclick=function(e){
            var x = e.offsetX;//相对于棋盘左上角的x坐标
            var y = e.offsetY;//相对于棋盘左上角的y坐标
            var i = Math.floor(x/30);
            var j = Math.floor(y/30);
            if( chessBox[i][j] == 0 ) {
                oneStep(i,j,me.Black);
                if(me.Black){
                    chessBox[i][j]=1;
                }/*else{
                    chessBox[i][j]=2;
                }
                me=!me;//下一步白棋*/
                // nextStepAI();
                nextStepAI_v2();
            }
        }

        //==================================================custom code================================================


        function nextStepAI(){
        	var tempI,tempJ,nextI,nextJ;
        	var weight;
        	for(var i=0;i<15;i++){
        		for(var j=0;j<15;j++){
        			if(chessBox[i][j]==1){//遍历横竖斜有没有连成双边3子或单边4子的
        				//横
        				tempI=i;
        				tempJ=j;
        				weight=1;
        				while(tempI>=0&&chessBox[--tempI][j]==1){//左
        					weight++;
        					if(tempI-1>=0&&chessBox[tempI-1][j]==0){//空一子跳过并算权重
        						weight++;
        						nextI=--tempI;
        						nextJ=j;
        					}
        				}
        				if(weight>=5){
        					oneStep(nextI,nextJ,!me);
        					chessBox[nextI][nextJ]=2;
        					return;
        				}
        				
        				tempI=i;
        				tempJ=j;
        				while(tempI<15&&chessBox[++tempI][j]==1){//右
        					weight++;
        					if(tempI+1<15&&chessBox[tempI+1][j]==0){//空一子跳过并算权重
	        					weight++;
	        					nextI=++tempI;
        						nextJ=j;
	        				}
        				}
        				if(weight>=5){
        					oneStep(nextI,nextJ,!me);
        					chessBox[nextI][nextJ]=2;
        					return;
        				}

        				//竖
        				tempI=i;
        				tempJ=j;
        				weight=1;
        				while(tempJ>=0&&chessBox[i][--tempJ]==1){//上
        					weight++;
        					if(tempJ-1>=0&&chessBox[i][tempJ-1]==0){//空一子跳过并算权重
        						weight++;
        						nextI=i;
        						nextJ=--tempJ;
        					}
        				}
        				if(weight>=5){
        					oneStep(nextI,nextJ,!me);
        					chessBox[nextI][nextJ]=2;
        					return;
        				}

        				tempI=i;
        				tempJ=j;
        				while(tempJ<15&&chessBox[i][++tempJ]==1){//下
        					weight++;
        					if(tempJ+1<15&&chessBox[i][tempJ+1]==0){//空一子跳过并算权重
	        					weight++;
	        					nextI=i;
        						nextJ=++tempJ;
	        				}
        				}
        				if(weight>=5){
        					oneStep(nextI,nextJ,!me);
        					chessBox[nextI][nextJ]=2;
        					return;
        				}


        				//左斜（左低右高）
        				tempI=i;
        				tempJ=j;
        				weight=1;
        				while(tempI>=0&&tempJ<15&&chessBox[--tempI][++tempJ]==1){//左低
        					weight++;
        					if(tempI-1>=0&&tempJ+1<15&&chessBox[tempI-1][tempJ+1]==0){//空一子跳过并算权重
        						weight++;
        						nextI=--tempI;
        						nextJ=++tempJ;
        					}
        				}
        				if(weight>=5){
        					oneStep(nextI,nextJ,!me);
        					chessBox[nextI][nextJ]=2;
        					return;
        				}

        				tempI=i;
        				tempJ=j;
        				while(tempI<15&&tempJ>=0&&chessBox[++tempI][--tempJ]==1){//右高
        					weight++;
        					if(tempI+1<15&&tempJ-1>=0&&chessBox[tempI+1][tempJ-1]==0){//空一子跳过并算权重
        						weight++;
        						nextI=++tempI;
        						nextJ=--tempJ;
        					}
        				}
        				if(weight>=5){
        					oneStep(nextI,nextJ,!me);
        					chessBox[nextI][nextJ]=2;
        					return;
        				}

        				//右斜（右低左高）
        				tempI=i;
        				tempJ=j;
        				weight=1;
        				while(tempI<15&&tempJ<15&&chessBox[++tempI][++tempJ]==1){//右低
        					weight++;
        					if(tempI+1<15&&tempJ+1<15&&chessBox[tempI+1][tempJ+1]==0){//空一子跳过并算权重
        						weight++;
        						nextI=++tempI;
        						nextJ=++tempJ;
        					}
        				}
        				if(weight>=5){
        					oneStep(nextI,nextJ,!me);
        					chessBox[nextI][nextJ]=2;
        					return;
        				}

        				tempI=i;
        				tempJ=j;
        				while(tempI>=0&&tempJ>=0&&chessBox[--tempI][--tempJ]==1){//左高
        					weight++;
        					if(tempI-1>=0&&tempJ-1>=0&&chessBox[tempI-1][tempJ-1]==0){//空一子跳过并算权重
        						weight++;
        						nextI=--tempI;
        						nextJ=--tempJ;
        					}
        				}
        				if(weight>=5){
        					oneStep(nextI,nextJ,!me);
        					chessBox[nextI][nextJ]=2;
        					return;
        				}

        			}
        		}
        	}

        	// alert(count);

        }


        //
        function nextStepAI_v2(){
        	var bchess=[];
        	var wchess=[];
        	for(var i=0;i<15;i++){
        		for(var j=0;j<15;j++){
        			if(chessBox[i][j]==0){
        				bchess.push(calculate(i,j,me.Black));
        				wchess.push(calculate(i,j,me.White));
        			}
        		}
        	}
        	nextStep(bchess,wchess,me.White);
        }

        //判断游戏是否结束
        function isEnd(i,j,k){
        	
        	var chessData=new ChessData();
            chessData.i=i;
            chessData.j=j;
            chessData.type=k;
            chessDataList.push(chessData);
        
        	var chess=calculate(i,j,k);
        	var end=false;
        	var serverData={};
        	if(chess.henStepWeight>=5||chess.shuStepWeight>=5||chess.zxStepWeight>=5||chess.yxStepWeight>=5){
        		alert(chessName[k]+"胜");
        		serverData.winner=k;
        		end=true;
        	}
        	if(chessBox.count==0){
        		alert("平局");
        		serverData.winner=0;
        		end=true;
        	}
        	if(end==true){
        		serverData.data=JSON.stringify(chessDataList);
        		saveData(serverData);
        		//刷新页面
        		//location.reload();
        	}
        }


        //下一步
        function nextStep(BchessList,WchessList,k){
        	var maxBWeight=0,maxWWeight=0;
        	for(var i=0;i<BchessList.length;i++){
        		if(maxBWeight<BchessList[i].finalWeight){
        			maxBWeight=BchessList[i].finalWeight;
        		}
        	}
        	for(var i=0;i<WchessList.length;i++){
        		if(maxWWeight<WchessList[i].finalWeight){
        			maxWWeight=WchessList[i].finalWeight;
        		}
        	}
        	if(k==me.White){//白
        		if(maxBWeight>=(maxWWeight+1)&&maxBWeight>=4){
        			var chess=getStep(BchessList,maxBWeight);
        			oneStep(chess.i,chess.j,me.White);
        			chessBox[chess.i][chess.j]=me.White;
        		}else{
        			var chess=getStep(WchessList,maxWWeight);
        			oneStep(chess.i,chess.j,me.White);
        			chessBox[chess.i][chess.j]=me.White;
        		}
        	}else{//黑
        		if(maxWWeight>=(maxBWeight+1)&&maxWWeight>=4){
        			var chess=getStep(WchessList,maxWWeight);
        			oneStep(chess.i,chess.j,me.Black);
        			chessBox[chess.i][chess.j]=me.Black;
        		}else{
        			var chess=getStep(BchessList,maxBWeight);
        			oneStep(chess.i,chess.j,me.Black);
        			chessBox[chess.i][chess.j]=me.Black;
        		}
        	}
        }

        function getStep(chessList,weight){
        	var tcl=[];
        	for(var i=0;i<chessList.length;i++){
        		if(weight==chessList[i].finalWeight){
        			tcl.push(chessList[i]);
        		}
        	}
        	var luckyNum=Math.floor(Math.random()*(tcl.length-1));
        	return tcl[luckyNum];
        }

        //计算下一步该坐标的可行性以及权重
        function calculate(i,j,k){
        	var tempI,tempJ;
        	var stepWeight;//步数权重
        	var chess=new Chess();
        	chess.i=i;
        	chess.j=j;
        	chess.henBlankWeight=0;
        	chess.shuBlankWeight=0;
        	chess.zxBlankWeight=0;
        	chess.yxBlankWeight=0;
        	chess.henDirctionWeight=0;
        	chess.shuDirctionWeight=0;
        	chess.zxDirctionWeight=0;
        	chess.yxDirctionWeight=0;
        	//横
			tempI=i;
			tempJ=j;
			stepWeight=1;
			step=1;
			while(tempI-1>=0&&chessBox[tempI-1][j]==k){//左
				stepWeight++;
				step++;
				tempI--;
			}
			if(tempI-1>=0&&chessBox[tempI-1][j]==me.Blank){
				chess.henBlankWeight++;
			}
			while(tempI-1>=0&&chessBox[tempI-1][j]==me.Blank){
				step++;
				tempI--;
			}

			tempI=i;
			tempJ=j;
			while(tempI+1<15&&chessBox[tempI+1][j]==k){//右
				stepWeight++;
				step++;
				tempI++;
			}
			if(tempI+1<15&&chessBox[tempI+1][j]==me.Blank){
				chess.henBlankWeight++;
			}
			while(tempI+1<15&&chessBox[tempI+1][j]==me.Blank){
				step++;
				tempI++;
			}

			chess.henStepWeight=stepWeight;
			chess.henDirctionWeight=(step>=5?1:0);

			//竖
			tempI=i;
			tempJ=j;
			stepWeight=1;
			step=1;
			while(tempJ-1>=0&&chessBox[i][tempJ-1]==k){//上
				stepWeight++;
				step++;
				tempJ--;
			}
			if(tempJ-1>=0&&chessBox[i][tempJ-1]==me.Blank){
				chess.shuBlankWeight++;
			}
			while(tempJ-1>=0&&chessBox[i][tempJ-1]==me.Blank){
				step++;
				tempJ--;
			}

			tempI=i;
			tempJ=j;
			while(tempJ+1<15&&chessBox[i][tempJ+1]==k){//下
				stepWeight++;
				step++;
				tempJ++;
			}
			if(tempJ+1<15&&chessBox[i][tempJ+1]==me.Blank){
				chess.shuBlankWeight++;
			}
			while(tempJ+1<15&&chessBox[i][tempJ+1]==me.Blank){
				step++;
				tempJ++;
			}

			chess.shuStepWeight=stepWeight;
			chess.shuDirctionWeight=(step>=5?1:0);

			//左斜（左低右高）
			tempI=i;
			tempJ=j;
			stepWeight=1;
			step=1;
			while(tempI-1>=0&&tempJ+1<15&&chessBox[tempI-1][tempJ+1]==k){//左低
				stepWeight++;
				step++;
				tempI--;
				tempJ++;
			}
			if(tempI-1>=0&&tempJ+1<15&&chessBox[tempI-1][tempJ+1]==me.Blank){
				chess.zxBlankWeight++;
			}
			while(tempI-1>=0&&tempJ+1<15&&chessBox[tempI-1][tempJ+1]==me.Blank){
				step++;
				tempI--;
				tempJ++;
			}

			tempI=i;
			tempJ=j;
			while(tempI+1<15&&tempJ-1>=0&&chessBox[tempI+1][tempJ-1]==k){//右高
				stepWeight++;
				step++;
				tempI++;
				tempJ--;
			}
			if(tempI+1<15&&tempJ-1>=0&&chessBox[tempI+1][tempJ-1]==me.Blank){//右高
				chess.zxBlankWeight++;
			}
			while(tempI+1<15&&tempJ-1>=0&&chessBox[tempI+1][tempJ-1]==me.Blank){//右高
				step++;
				tempI++;
				tempJ--;
			}

			chess.zxStepWeight=stepWeight;
			chess.zxDirctionWeight=(step>=5?1:0);

			//右斜（右低左高）
			tempI=i;
			tempJ=j;
			stepWeight=1;
			step=1;
			while(tempI+1<15&&tempJ+1<15&&chessBox[tempI+1][tempJ+1]==k){//右低
				stepWeight++;
				step++;
				tempI++;
				tempJ++;
			}
			if(tempI+1<15&&tempJ+1<15&&chessBox[tempI+1][tempJ+1]==me.Blank){
				chess.yxBlankWeight++;
			}
			while(tempI+1<15&&tempJ+1<15&&chessBox[tempI+1][tempJ+1]==me.Blank){
				step++;
				tempI++;
				tempJ++;
			}

			tempI=i;
			tempJ=j;
			while(tempI-1>=0&&tempJ-1>=0&&chessBox[tempI-1][tempJ-1]==k){//左高
				stepWeight++;
				step++;
				tempI--;
				tempJ--;
			}
			if(tempI-1>=0&&tempJ-1>=0&&chessBox[tempI-1][tempJ-1]==me.Blank){
				chess.yxBlankWeight++;
			}
			while(tempI-1>=0&&tempJ-1>=0&&chessBox[tempI-1][tempJ-1]==me.Blank){
				step++;
				tempI--;
				tempJ--;
			}

			chess.yxStepWeight=stepWeight;
			chess.yxDirctionWeight=(step>=5?1:0);

			chess.finalWeight=calculateFinalWeight(chess);
			return chess;

        }

        function calculateFinalWeight(chess){

        	//加权平均权重v1
			if(chess.henDirctionWeight==0){
				chess.henStepWeight=0;
			}
			if(chess.shuDirctionWeight==0){
				chess.henStepWeight=0;
			}
			if(chess.zxDirctionWeight==0){
				chess.zxStepWeight=0;
			}
			if(chess.yxDirctionWeight==0){
				chess.yxStepWeight=0;
			}
			var finalWeight=chess.henStepWeight+0.5*chess.henBlankWeight;
			if(finalWeight<chess.shuStepWeight+0.5*chess.shuBlankWeight){
				finalWeight=chess.shuStepWeight+0.5*chess.shuBlankWeight;
			}
			if(finalWeight<chess.zxStepWeight+0.5*chess.zxBlankWeight){
				finalWeight=chess.zxStepWeight+0.5*chess.zxBlankWeight;
			}
			if(finalWeight<chess.yxStepWeight+0.5*chess.yxBlankWeight){
				finalWeight=chess.yxStepWeight+0.5*chess.yxBlankWeight;
			}
			return finalWeight;

			/*//加权平均权重v2
			var finalWeight=chess.henStepWeight+chess.henBlankWeight*0.5;
			if(chess.dirctionWeight==0){
				return 0;
			}
			if(finalWeight<chess.shuStepWeight+chess.shuBlankWeight*0.5){
				finalWeight=chess.shuStepWeight;
			}
			if(finalWeight<chess.zxStepWeight+chess.zxBlankWeight*0.5){
				finalWeight=chess.zxStepWeight;
			}
			if(finalWeight<chess.yxStepWeight+chess.yxBlankWeight*0.5){
				finalWeight=chess.yxStepWeight;
			}
			return finalWeight;*/
			

			/*//加权平均权重v2(4*1,3*0.75,2*0.5,1*0.25) 
			var weightList=[];
			var finalWeight=0;
			weightList.push(chess.henStepWeight);
			weightList.push(chess.shuStepWeight);
			weightList.push(chess.zxStepWeight);
			weightList.push(chess.yxStepWeight);
			for(var i=0;i<weightList.length;i++){
				if(weightList[i]>=4){
					alert('1111');
					finalWeight+=weightList[i]*1;
				}else if(weightList[i]==3){
					finalWeight+=3*0.75;
				}else if(weightList[i]==2){
					finalWeight+=2*0.5
				}else{
					finalWeight+=0;
				}
			}
			return finalWeight;*/
			
        }

        //棋子
        function Chess(){
        	var i;
        	var j;
        	//步数权重
        	var henStepWeight;
        	var shuStepWeight;
        	var zxStepWeight;
        	var yxStepWeight;
        	//两端空白权重
        	var henBlankWeight;
        	var shuBlankWeight;
        	var zxBlankWeight;
        	var yxBlankWeight;
        	//方向权重
        	var henDirctionWeight;
        	var shuDirctionWeight;
        	var zxDirctionWeight;
        	var yxDirctionWeight;
        	//加权平均权重
        	var finalWeight;
        }


		//传输对战数据
		function saveData(data){
		console.log(data);
			$.ajax({
				async:false,
				url: "/lzy/games?gobangdata",
				data:{data:data.data,winner:data.winner,},
				dataType:'text',
				success:function(msg){
					alert(msg);
				},
				error:function(msg){
					alert("后台错误："+msg);
				}
			});
		}
		
		function ChessData(){
			var i;//横坐标
			var j;//纵坐标
			var type;//1.黑  2.白
		}
		
    </script>
</body>
</html>