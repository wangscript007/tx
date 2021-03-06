package com.joker.tx.proxy;

import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;

import com.joker.tx.Interceptor;
import com.joker.tx.TxInterceptor;

public class ServiceProxyFactory<V> {
	private static ServiceProxyFactory factory = new ServiceProxyFactory();
	private ServiceProxyFactory() {}
	
	/**
	 * 获取ServiceProxyFactory实例
	 * @author joker
	 * {@link https://github.com/Jokerblazes/tx.git}
	 * @return
	 */
	public static ServiceProxyFactory getInstance() {
		return factory;
	}
	
	private Interceptor interceptor = new TxInterceptor();
	private ConcurrentHashMap<V,ServiceInvocationHandler> map = new ConcurrentHashMap<V, ServiceInvocationHandler>();
	
	/**
	 * 添加过滤器
	 * @param interceptor
	 * @return
	  * @author joker
	 * {@link https://github.com/Jokerblazes/tx.git}
	 */
	public ServiceProxyFactory addInterceptor(Interceptor interceptor) {
		interceptor.setInterceptor(interceptor);
		return this;
	}
	
	/**
	 * 生成代理对象
	 * @param service
	 * @return
	  * @author joker
	 * {@link https://github.com/Jokerblazes/tx.git}
	 */
	public V getServiceProxy(V service) {
		ServiceInvocationHandler handler = getAndSetService(service);
		System.out.println(handler);
		return (V) Proxy.newProxyInstance(service.getClass().getClassLoader(),
				service.getClass().getInterfaces(), handler);
	}
	
	
	/**
	 * 
	 * @param service
	 * @return
	  * @author joker
	 * {@link https://github.com/Jokerblazes/tx.git}
	 */
	private synchronized ServiceInvocationHandler getAndSetService(V service) {
		ServiceInvocationHandler handler = map.get(service);
		if (handler == null) {
			handler = new ServiceInvocationHandler<V>(service,interceptor);
			map.put(service, handler);
		}
		return handler;
	}
}
