<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script type="text/javascript" src="http://127.0.0.1:8080/lzy/js/jquery-1.10.2.min.js"></script>

<script type="text/javascript" src="http://127.0.0.1:8080/lzy/js/BigInt.js"></script>
<script type="text/javascript" src="http://127.0.0.1:8080/lzy/js/Barrett.js"></script>
<script type="text/javascript" src="http://127.0.0.1:8080/lzy/js/RSA.js"></script>

<script type="text/javascript" src="js/jsencrypt.min.js"></script>

<script type="text/javascript">
	$(function(){
		$('#login_btn').click(function(){
			var userName=$('#username').val();
			var passWord=$('#password').val();
			setMaxDigits(131);
			$.ajax({
				async:false,
				url:'/lzy/login/passkey',
				dataType:'json',
				success:function(data){
					console.log(data);
					var key = new RSAKeyPair(data.public_exponent, "", data.modulus);
					var passcode = encryptedString(key, passWord); 
					console.log(passcode);
					
					
					var encrypt = new JSEncrypt();
					encrypt.setPublicKey(data.modulus);  
					var encryptData = encrypt.encrypt(passWord);//加密后的字符串  
					console.log(encryptData) 
					encryptData = encrypt.encrypt(passWord);//加密后的字符串  
					console.log(encryptData)  
					
					
					$.ajax({
						async:false,
						type:'post',
						url:'/lzy/login/auth',
						dataType:'json',
						data:{userName:userName,passWord:passcode},
						success:function(data){
							console.log('yes');
						},
						error:function(data){
							console.log(passcode);
						},
					});
				},				
			});
		
		});
	});
	
	
	
</script>
</head>
<body>
	用户名：<input id='username' type="text"/><br/>
	密     码：<input id='password' type="password"/><br/>
	<button id='login_btn'>登陆</button>
</body>
</html>