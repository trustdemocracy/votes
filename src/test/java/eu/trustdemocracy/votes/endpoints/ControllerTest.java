package eu.trustdemocracy.votes.endpoints;

import eu.trustdemocracy.votes.core.interactors.util.TokenUtils;
import eu.trustdemocracy.votes.infrastructure.FakeInteractorFactory;
import eu.trustdemocracy.votes.infrastructure.InteractorFactory;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.ext.web.client.HttpResponse;
import io.vertx.rxjava.ext.web.client.WebClient;
import java.io.IOException;
import java.net.ServerSocket;
import lombok.val;
import org.jose4j.lang.JoseException;
import org.junit.After;
import org.junit.Before;
import rx.Single;

public class ControllerTest {

  protected static final String HOST = "localhost";

  protected Vertx vertx;
  protected Integer port;
  protected WebClient client;
  protected InteractorFactory interactorFactory;

  @Before
  public void setUp(TestContext context) throws IOException, JoseException {
    TokenUtils.generateKeys();

    vertx = Vertx.vertx();
    client = WebClient.create(vertx);

    val socket = new ServerSocket(0);
    port = socket.getLocalPort();
    socket.close();

    val options = new DeploymentOptions().setConfig(new JsonObject().put("http.port", port));

    interactorFactory = new FakeInteractorFactory();

    App.setInteractorFactory(interactorFactory);
    vertx.deployVerticle(App.class.getName(), options, context.asyncAssertSuccess());
  }

  @After
  public void tearDown(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }

  protected void assert200(TestContext context, Async async, Single<HttpResponse<Buffer>> single) {
    single.subscribe(response -> {
      context.assertEquals(response.statusCode(), 200);
      context.assertTrue(response.headers().get("content-type").contains("application/json"));

      async.complete();
    }, error -> {
      context.fail(error);
      async.complete();
    });
  }

}
