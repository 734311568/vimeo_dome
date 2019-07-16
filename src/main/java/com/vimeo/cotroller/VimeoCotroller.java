package com.vimeo.cotroller;

import static com.nimbusds.jose.Payload.Origin.JSON;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Encoder;

/**
 *
 * @author 73431
 */
@Controller
@RequestMapping("/")
public class VimeoCotroller {

	private static BASE64Encoder encoder = new BASE64Encoder();// 加密

	@RequestMapping("/first")
	public String index() {
		return "forward:Vinmeo.html";

	}

	//客户端模式
	@RequestMapping(value = "/fromPost", produces = {"application/text;charset=UTF-8"})
	@ResponseBody
	public String formPost(@RequestParam String client_id, @RequestParam String client_securi) throws IOException {
//		System.out.println("前台传过来的" + client_id);
//		System.out.println("前台传过来的" + client_securi);
//		String encode = client_id + ":" + client_securi;
//		// Base64.getEncoder().
////                byte[] encode1 = Base64.getEncoder().encode(encode.getBytes());
////		String str = new String(encode1);
		String encode2 = encoder.encode((client_id + ":" + client_securi).getBytes());

//                System.out.println("经过64编码处理的" +  str);
		JSONObject jSONFriend = new JSONObject();
		//创建连接S
		String uri = "https://api.vimeo.com/oauth/authorize/client";

		HttpPost httpPost = new HttpPost(uri);
		jSONFriend.put("grant_type", "client_credentials");
		jSONFriend.put("scope", "public");
		//添加請求頭
		httpPost.setHeader("Content-type", "application/json");
		httpPost.setHeader("Authorization", "basic " + encode2);
		httpPost.setHeader("Accept", "application/vnd.vimeo.*+json;version=3.4");
		//添加請求的躰
		StringEntity stringEntityGoodFriend = new StringEntity(jSONFriend.toString(), "UTF-8");
		//添加请求体
		httpPost.setEntity(stringEntityGoodFriend);
		//模妮客戶端发送请求
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		//發送請求后返回的的數據
		CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);

		String toString = new JSONObject(EntityUtils.toString(closeableHttpResponse.getEntity())).toString();
		System.out.println("返回的數據是" + toString);

		return "你好";

	}

	//授权码模式
	@RequestMapping(value = "/cliendCode", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getCodeMode(@RequestParam String uricode, HttpServletRequest request) throws IOException {
		//客户端的id
		String client_id = "5f90a0518d7ab04ec4d9b1e414c6c3bdc084b6d6";
		//客户端的密钥
		String client_securi = "8S094hcMBCmeaidEB2LzddqJ+Cin6TlvnLjrCimGaIGumay3X0mApH4K1WuQOaGfASBclRBnW8Ddx9fXd9lsyDmcAU3XuABu6SicPkcLop9Trgo7QXoTgBEoIL4arWZe";
		//进行base 64编码处理
		String encode2 = encoder.encode((client_id + ":" + client_securi).getBytes());
		//验证的令牌地址
		String uri = "https://api.vimeo.com/oauth/access_token";
		JSONObject jSONFriend = new JSONObject();
		//创建链接
		HttpPost httpPost = new HttpPost(uri);
		//设置的请求头
		//添加請求頭
		httpPost.setHeader("Content-type", "application/json");
		httpPost.setHeader("Authorization", "basic " + encode2);
		httpPost.setHeader("Accept", "application/vnd.vimeo.*+json;version=3.4");

		//请求体
		jSONFriend.put("grant_type", "authorization_code");
		jSONFriend.put("code", uricode);
		jSONFriend.put("redirect_uri", "http://localhost:8080/aa.html");
		//添加請求的躰
		StringEntity stringEntityGoodFriend = new StringEntity(jSONFriend.toString(), "UTF-8");
		httpPost.setEntity(stringEntityGoodFriend);
		//模拟客户端发送请求
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		//客户端返回结果
		CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);

		JSONObject jsonObject = new JSONObject(EntityUtils.toString(closeableHttpResponse.getEntity()));

		String toString = jsonObject.get("access_token").toString();
		System.out.println("返回的数据----//有包含token " + toString);
		//拿到令牌放在sesion
		HttpSession session = request.getSession();
		session.setAttribute("access_token", toString);

		String uplocal = uplocal(toString);
		System.out.println("返回的的上传的数据是\t" + uplocal);
		JSONObject tempJSONObject = new JSONObject(uplocal);

		JSONObject jsonObjectUpload = tempJSONObject.getJSONObject("upload");

		System.out.println("返回的form 表单的数据是\t" + jsonObjectUpload.toString());
		return jsonObjectUpload.toString();

	}

	public String uplocal(String access_token) throws UnsupportedEncodingException, IOException {

		//json对象封装json数据用的
		JSONObject json = new JSONObject();
		JSONObject json2 = new JSONObject();
		//解析json 拿到key的值
//		String access_token = jsono.get("access_token").toString();
		System.out.println("access_token授权码模式传过来的是\t" + access_token);
		String uri = "https://api.vimeo.com/me/videos";
		//创建post 的请求
		HttpPost post = new HttpPost(uri);
		//添加请求头
		post.setHeader("Authorization", "bearer " + access_token);
		post.setHeader("Content-Type", "application/json");
		post.setHeader("Accept", "application/vnd.vimeo.*+json;version=3.4");

		//添加请求体
		json2.put("approach", "post");
		json2.put("size", "1024");
		json2.put("redirect_url", "http://localhost:8080/aa.html");
		json.put("upload", json2);
		System.out.println("json\t" + json.toString());

		//指定编码格式
		StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");
		post.setEntity(stringEntity);
		CloseableHttpClient createDefault = HttpClients.createDefault();
		//	抛异常
		CloseableHttpResponse executeResponse = createDefault.execute(post);

		String strEntity = new JSONObject(EntityUtils.toString(executeResponse.getEntity())).toString();
		System.out.println("下载之后响应" + strEntity);
		return strEntity;

	}

	@RequestMapping(value = "/delete")
	public String deleteVideos(HttpServletRequest request) throws IOException {
		String accessToken = request.getSession().getAttribute("access_token").toString();
		System.out.println("从sesion拿出来的令牌\t" + accessToken);
		//要删除的视频id 
		
		String idString = "347262582";
		String uri = "https://api.vimeo.com/videos/" + idString;

		HttpDelete delete = new HttpDelete(uri);
		delete.setHeader("Authorization","bearer " + accessToken);
		CloseableHttpClient createDefault = HttpClients.createDefault();
		//	抛异常
		CloseableHttpResponse executeResponse = createDefault.execute(delete);
		String str;
		JSONObject jsonObjectDelete;
		try {
			 jsonObjectDelete = new JSONObject(EntityUtils.toString(executeResponse.getEntity()));
			
		
		
		} catch (IllegalArgumentException e) {
			return "删除成功";
		}
		
		
		return jsonObjectDelete.toString();

	}

}
