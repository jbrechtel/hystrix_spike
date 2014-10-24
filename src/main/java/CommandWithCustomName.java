import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;

public class CommandWithCustomName extends HystrixCommand<String> {
    protected CommandWithCustomName(final String name) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(name)).andCommandKey(HystrixCommandKey.Factory.asKey(name)));
    }

    @Override
    protected String run() throws Exception {
        Thread.sleep(50);
        throw new Exception("WTF");
    }
}
