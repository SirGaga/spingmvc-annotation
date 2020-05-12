package com.zhangjie.async.controller;

import java.util.UUID;
import java.util.concurrent.Callable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import com.zhangjie.async.queue.DeferredResultQueue;

/**
 *
 */
@Controller
public class AsyncController {

	/**
	 * 1.控制器返回 Callable 
	 * 2.SpringMVC异步处理，将 Callable 提交到 TaskExecutor 使用一个隔离的线程进行执行
	 * 3.DispatcherServlet 和 所有的Filter退出web容器的线程，但是response保持打开状态
	 * 4.Callable 返回结果，SpringMVC将请求重新派发给容器，恢复之前的处理
	 * 5.DispatcherServlet 再次执行，根据 Callable 返回的结果，SpringMVC继续进行视图渲染流程等（从收请求到视图渲染）
	 * preHandle...
	 * postHandle...
	 * afterCompletion...
	 * preHandle.../springmvc/async01
	 * 主线程开始...Thread[http-nio-8080-exec-6,5,main]--->1589253954161
	 * 主线程结束...Thread[http-nio-8080-exec-6,5,main]--->1589253954162
	 * ======DispatcherServlet 和 所有的Filter退出web容器的线程================
	 * 
	 * ======等待Callable执行==============================================
	 * 副线程开始...Thread[MvcAsync1,5,main]--->1589253954179
	 * 副线程结束...Thread[MvcAsync1,5,main]--->1589253956180
	 * ======Callable执行完成==============================================
	 * preHandle.../springmvc/async01
	 * postHandle...(Callable的之前的返回值就是目标方法的返回值)
	 * afterCompletion...
	 * 
	 * 异步拦截器：
	 * 	1.原生API的 AsyncListener
	 * 	2.SpringMVC下，实现 AsyncHandlerInterceptor
	 * @return
	 */
    @ResponseBody
    @RequestMapping("/async01")
    public Callable<String> async01(){
        System.out.println("主线程开始..."+Thread.currentThread()+"--->"+System.currentTimeMillis());
        Callable<String> callable = () -> {
            System.out.println("副线程开始..."+Thread.currentThread()+"--->"+System.currentTimeMillis());
            Thread.sleep(2000);
            System.out.println("副线程结束..."+Thread.currentThread()+"--->"+System.currentTimeMillis());
            return "async01";
        };
        System.out.println("主线程结束..."+Thread.currentThread()+"--->"+System.currentTimeMillis());
        return callable;
    }
    
    @ResponseBody
    @RequestMapping("/createOrder")
    public DeferredResult<Object> createOrder(){
    	DeferredResult<Object> deferredResult = new DeferredResult<Object>((long)3000,"create fail");
    	
    	DeferredResultQueue.save(deferredResult);
    	
    	
    	return deferredResult;
    }
    
    @ResponseBody
    @RequestMapping("/create")
    public String create() {
    	String order = UUID.randomUUID().toString();
    	DeferredResult<Object> deferredResult = DeferredResultQueue.get();
    	deferredResult.setResult(order);
    	return "success ----> "+order;
    }

}
