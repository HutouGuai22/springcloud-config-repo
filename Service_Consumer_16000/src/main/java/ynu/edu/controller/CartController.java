package ynu.edu.controller;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.annotation.Resource;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ynu.edu.entity.Cart;
import ynu.edu.entity.CommonResult;
import ynu.edu.entity.User;
import ynu.edu.feign.ServiceProviderService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Resource
    private ServiceProviderService serviceInstance;

    @GetMapping("/getCartByIdA/breaker-a/{userId}")
    @LoadBalanced
    @CircuitBreaker(name = "instanceA", fallbackMethod = "getCartByIdDownA")
    public CommonResult<Cart> getCartByIdA(@PathVariable("userId") Integer userId) {
        Cart cart = new Cart();
        List<String> goods = new ArrayList<>();
        goods.add("电池");
        goods.add("无人机");
        goods.add("笔记本电脑");
        cart.setGoodList(goods);
        User u = serviceInstance.GetUserById(userId);
        cart.setUser(u);

        Integer code = 200;
        String message = "success(16000)";

        CommonResult<Cart> commonResult = new CommonResult<>();
        commonResult.setCode(code);
        commonResult.setMessage(message);
        commonResult.setResult(cart);

        return commonResult;
    }

    @GetMapping("/getCartByIdB/breaker-b/{userId}")
    @LoadBalanced
    @CircuitBreaker(name = "instanceB", fallbackMethod = "getCartByIdBDownB")
    public CommonResult<Cart> getCartByIdB(@PathVariable("userId") Integer userId) {
        Cart cart = new Cart();
        List<String> goods = new ArrayList<>();
        goods.add("电池");
        goods.add("无人机");
        goods.add("笔记本电脑");
        cart.setGoodList(goods);
        User u = serviceInstance.GetUserById(userId);
        cart.setUser(u);

        Integer code = 200;
        String message = "success(16000)";

        CommonResult<Cart> commonResult = new CommonResult<>();
        commonResult.setCode(code);
        commonResult.setMessage(message);
        commonResult.setResult(cart);

        return commonResult;
    }

    @GetMapping("/getCartById/bulkhead/{userId}")
    @LoadBalanced
    @Bulkhead(name = "bulkheadService", fallbackMethod = "getCartByIdBulkheadDown") // 使用隔离器
    public CommonResult<Cart> getCartByIdC(@PathVariable("userId") Integer userId) {
        Cart cart = new Cart();
        List<String> goods = new ArrayList<>();
        goods.add("电池");
        goods.add("无人机");
        goods.add("笔记本电脑");
        cart.setGoodList(goods);
        User u = serviceInstance.GetUserById(userId);
        cart.setUser(u);

        Integer code = 200;
        String message = "success(16000)";

        CommonResult<Cart> commonResult = new CommonResult<>();
        commonResult.setCode(code);
        commonResult.setMessage(message);
        commonResult.setResult(cart);

        return commonResult;
    }

    @GetMapping("/getCartById/ratelimit/{userId}")
    @LoadBalanced
    @RateLimiter(name = "rateLimitedService", fallbackMethod = "getCartByIdBRateLimitDown") // 使用单一限流器
    public CommonResult<Cart> getCartByIdD(@PathVariable("userId") Integer userId) {
        Cart cart = new Cart();
        List<String> goods = new ArrayList<>();
        goods.add("电池");
        goods.add("无人机");
        goods.add("笔记本电脑");
        cart.setGoodList(goods);
        User u = serviceInstance.GetUserById(userId);
        cart.setUser(u);

        Integer code = 200;
        String message = "success(16000)";

        CommonResult<Cart> commonResult = new CommonResult<>();
        commonResult.setCode(code);
        commonResult.setMessage(message);
        commonResult.setResult(cart);

        return commonResult;
    }

    public CommonResult<Cart> getCartByIdBulkheadDown(Integer userId, Throwable e) { // 隔离器降级方法
        e.printStackTrace();
        String message = "获取用户" + userId + "信息的服务当前被隔离，因此方法降级";
        System.out.println(message);
        CommonResult<Cart> result = new CommonResult<>(503, message, new Cart());
        return result;
    }

    public CommonResult<Cart> getCartByIdBRateLimitDown(Integer userId, Throwable e) { // 限流降级方法
        e.printStackTrace();
        String message = "获取用户" + userId + "信息的服务当前被限流，因此方法降级";
        System.out.println(message);
        CommonResult<Cart> result = new CommonResult<>(429, message, new Cart());
        return result;
    }

    public CommonResult<Cart> getCartByIdDownA(Integer userId, Throwable e) {
        e.printStackTrace();
        String message = "获取用户" + userId + "信息的服务当前超时，因此方法降级（实例A）";
        System.out.println(message);
        CommonResult<Cart> result = new CommonResult<>(400, message, new Cart());
        return result;
    }

    public CommonResult<Cart> getCartByIdBDownB(Integer userId, Throwable e) {
        e.printStackTrace();
        String message = "获取用户" + userId + "信息的服务当前超时，因此方法降级（实例B）";
        System.out.println(message);
        CommonResult<Cart> result = new CommonResult<>(400, message, new Cart());
        return result;
    }
}
