import config.MailApplicationConfig;
import io.vertx.core.Vertx;
import vertx.verticle.MailApplicationVerticle;

/**
 * @author Don9
 * @create 2018-01-23-10:20
 **/
public class ApplicationMain {

    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();

        MailApplicationConfig.INSTANCE.loadConfig();

        vertx.deployVerticle(new MailApplicationVerticle());

    }

}
