import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class OtherFailingCommand extends HystrixCommand<String> {
    protected OtherFailingCommand() {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("FailingCommand")));
    }

    @Override
    protected String run() throws Exception {
        Thread.sleep(50);
        throw new Exception("WTF");
    }
}

