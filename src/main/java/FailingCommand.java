import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class FailingCommand extends HystrixCommand<String> {
    protected FailingCommand() {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("FailingCommand")));
    }

    @Override
    protected String run() throws Exception {
        Thread.sleep(50);
        throw new Exception("WTF");
    }
}
