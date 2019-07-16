


$(document).ready(function () {
//		var div =$("#div").html();
	$("#btn").click(function () {
		var clent_id = "5f90a0518d7ab04ec4d9b1e414c6c3bdc084b6d6";
		var redirect_uri = "http://localhost:8080/aa.html";
		//打开一个网址
		var A = window.open("https://api.vimeo.com/oauth/authorize?\n\
				response_type=code&\n\
				client_id=" + clent_id + "&\n\
				redirect_uri=" + redirect_uri + "&\n\
				state=1" + "&scope=upload delete");


	});
	$("#btn1").click(function () {

		function GetQueryString(code) {
			var reg = new RegExp("(^|&)" + code + "=([^&]*)(&|$)", "i");
			var r = window.location.search.substr(1).match(reg); //获取url中"?"符后的字符串并正则匹配
			var context = "";
			if (r != null)
				context = r[2];
			reg = null;
			r = null;
			return context == null || context == "" || context == "undefined" ? "" : context;
		}
		var uricode = GetQueryString("code");
		alert(uricode);
		$.post("/cliendCode", {"uricode": uricode}, function (data) {
			alert(data["form"]);

			$("#div").html(data["form"]);

		});

	});
	$("#btn2").click(function () {
		
		$.get("/delete",function(data){
			
			alert(data);
		},"text");
		
	});
	
	
});