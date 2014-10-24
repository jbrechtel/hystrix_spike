import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class RecoveringCommand extends HystrixCommand<String> {
    private final int timesToFail;
    private static int failureCount = 0;

    public RecoveringCommand(int timesToFail) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RecoveringCommand")));
        this.timesToFail = timesToFail;
    }

    @Override
    protected String run() throws Exception {
        Thread.sleep(50);
        if(failureCount == timesToFail) {
            return "HELLO";
        }

        failureCount++;
        throw new Exception("FAIL!");
    }
}
