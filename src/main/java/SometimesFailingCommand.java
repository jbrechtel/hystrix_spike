import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class SometimesFailingCommand extends HystrixCommand<String> {
    protected SometimesFailingCommand() {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("SometimesFailingCommand")));
    }

    @Override
    protected String run() throws Exception {
        Thread.sleep(50);
        if(Math.random() > 0.75) {
            throw new Exception("FAIL!");
        }
        return "HELLO";
    }
}
