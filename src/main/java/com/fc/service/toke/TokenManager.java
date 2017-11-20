package com.fc.service.toke;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

public class TokenManager {
	
	/**
	 * token过期时长：30分钟   单位毫秒
	 */
	public final static long PAST_TIME = 9999  * 60 * 1000;
	
	private static TokenManager manager = new TokenManager();
	
	/**
	 * token容器
	 */
	private Map<String, Token> map = new ConcurrentHashMap<>();
	private TokenManager(){}
	
	
	
	/**
	 * 静态工厂方法：获取TokenManager实例
	 * @return
	 */
	public static final TokenManager getInstance(){
		return manager;
	}
	
	/**
	 * 添加token，如果该用户已经存在token记录则修改，没有则添加，程序自动创建token字符串
	 * @param ipocid
	 */
	public synchronized String addToken(String ipocid){
		if(ipocid == null){
			throw new NullPointerException("参数为空");
		}
		Token token = map.get(ipocid);
		if(token != null){
			return updateToken(ipocid);
		}
		Token newToken = Token.newInstance(ipocid);
		map.put(ipocid, newToken);
		return newToken.getTokenStr();
	}
	/**
	 * 添加token，如果该用户已经存在token记录则修改，没有则添加
	 * 如果userId为空  则从tokenStr中解析userId
	 * 如果失败则返回null
	 * @param ipocid       
	 * @param tokenStr
	 */
	public synchronized String addToken(String ipocid, String tokenStr){
		if(StringUtils.isBlank(tokenStr)){
			throw new NullPointerException("参数为空");
		}
		if(StringUtils.isBlank(ipocid)){
			try {
				ipocid = getUserId(tokenStr);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		Token token = map.get(ipocid);
		if(token != null){
			return updateToken(ipocid, tokenStr);
		}
		Token newToken = Token.newInstance(ipocid, tokenStr);
		map.put(ipocid, newToken);
		return newToken.getTokenStr();
	}
	
	
	
	/**
	 * 更新token,程序自动创建token字符串
	 * @param ipocid
	 */
	private String updateToken(String ipocid){
		Token token = map.get(ipocid);
		if(token == null){
			
			return addToken(ipocid);
		}
		
		return token.updateToken();
	}
	/**
	 * 更新token
	 * @param ipocid
	 * @param tokenStr
	 */
	private String updateToken(String ipocid, String tokenStr){
		Token token = map.get(ipocid);
		if(token == null){
			
			return addToken(ipocid, tokenStr);
		}
		
		return token.updateToken(tokenStr);
	}
	
	/**
	 * 获取对应的token对象
	 * @param ipocid
	 * @return
	 */
	public synchronized Token get(String ipocid){
		return map.get(ipocid);
	}
	
	/**
	 * 校验token
	 * @param tokenStr
	 * @return
	 */
	public synchronized boolean verify(String tokenStr){
		if(StringUtils.isBlank(tokenStr)){
			return false;
		}
		String userId = null;
		try{
			userId = getUserId(tokenStr);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		Token token = map.get(userId);
		if(token == null){
			return false;
		}
		if(token.verify(tokenStr, PAST_TIME)){
			return true;
		}
		return false;
	}
	
	private String getUserId(String tokenStr) throws Exception{
		return tokenStr.substring(tokenStr.indexOf("-") + 1);
	}

}
