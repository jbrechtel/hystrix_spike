import com.netflix.config.ConfigurationManager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FailingCommandTest {
    @Test
    public void failingCommandTripsCircuitBreaker() throws Exception {
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.FailingCommand.circuitBreaker.enabled", true);
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.FailingCommand.circuitBreaker.requestVolumeThreshold", 10);
        for (int i = 0; i < 40; i++) {
            try {
                FailingCommand command = new FailingCommand();
                command.execute();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        System.out.println("Executed 20 requests");
    }

    @Test
    public void sometimesFailingCommandTripsCircuitBreaker() throws Exception {
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.SometimesFailingCommand.circuitBreaker.enabled", true);
        //ConfigurationManager.getConfigInstance().setProperty("hystrix.command.SometimesFailingCommand.circuitBreaker.requestVolumeThreshold", 10);
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.SometimesFailingCommand.circuitBreaker.errorThresholdPercentage", 10);
        for (int i = 0; i < 2000; i++) {
            try {
                SometimesFailingCommand command = new SometimesFailingCommand();
                command.execute();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        System.out.println("Executed 2000 requests");
    }

    @Test
    public void commandsWithTheSameNameShouldFailTogether() throws Exception {
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.FailingCommand.circuitBreaker.enabled", true);
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.FailingCommand.circuitBreaker.requestVolumeThreshold", 10);
        for (int i = 0; i < 20; i++) {
            try {
                FailingCommand command = new FailingCommand();
                command.execute();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            try {
                OtherFailingCommand command = new OtherFailingCommand();
                command.execute();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        System.out.println("Executed 20 requests");
    }

    @Test
    public void commandsWithDifferentNamesShouldNotCauseEachOtherToFail() throws Exception {
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.FooCommand.circuitBreaker.enabled", true);
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.FooCommand.circuitBreaker.requestVolumeThreshold", 20);

        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.BarCommand.circuitBreaker.enabled", true);
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.BarCommand.circuitBreaker.requestVolumeThreshold", 20);
        for (int i = 0; i < 20; i++) {
            try {
                CommandWithCustomName command = new CommandWithCustomName("FooCommand");
                command.execute();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            try {
                CommandWithCustomName command = new CommandWithCustomName("FooCommand");
                command.execute();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        System.out.println("Executed 20 requests");
    }

    @Test
    public void circuitBreakerClosesAfterRecovery() throws Exception {
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.RecoveringCommand.circuitBreaker.enabled", true);
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.RecoveringCommand.circuitBreaker.requestVolumeThreshold", 10);
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.RecoveringCommand.circuitBreaker.sleepWindowInMilliseconds", 100);

        for (int i = 0; i < 25000; i++) {
            try {
                RecoveringCommand command = new RecoveringCommand(50);
                command.execute();
                System.out.println("Success!");
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
