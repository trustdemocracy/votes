package eu.trustdemocracy.votes.endpoints;

import eu.trustdemocracy.votes.endpoints.controllers.Controller;
import eu.trustdemocracy.votes.endpoints.util.Runner;
import eu.trustdemocracy.votes.infrastructure.DefaultInteractorFactory;
import eu.trustdemocracy.votes.infrastructure.InteractorFactory;
import eu.trustdemocracy.votes.infrastructure.JWTKeyFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.val;

public class App extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(App.class);
  private static final int DEFAULT_PORT = 8080;

  private static InteractorFactory interactorFactory = DefaultInteractorFactory.getInstance();

  private Router router;

  public static void main(String... args) {
    Runner.runVerticle(App.class.getName());
  }

  @Override
  public void start() throws Exception {
    val port = config().getInteger("http.port", DEFAULT_PORT);
    setKeys();

    vertx.executeBlocking(future -> {
      router = Router.router(vertx);
      router.route().handler(BodyHandler.create());
      registerControllers();

      vertx.createHttpServer()
          .requestHandler(router::accept)
          .listen(port);

      future.complete();
    }, result -> {
      if (result.succeeded()) {
        LOG.info("App listening on port: " + port);
      } else {
        LOG.error("Failed to start verticle", result.cause());
      }
    });
  }

  private void registerControllers() {
    val controllers = Stream.of(
        Controller.class
    ).collect(Collectors.toCollection(HashSet<Class<? extends Controller>>::new));

    for (val controller : controllers) {
      try {
        val constructor = controller.getConstructor(App.class);
        constructor.newInstance(this);
      } catch (Exception e) {
        LOG.error("Failing to attach controller [" + controller.getName() + "]", e);
      }
    }
  }

  public Router getRouter() {
    return router;
  }

  public InteractorFactory getInteractorFactory() {
    return interactorFactory;
  }

  public static void setInteractorFactory(InteractorFactory interactorFactory) {
    if (interactorFactory == null) {
      throw new NullPointerException("InteractorFactory cannot be null");
    }
    App.interactorFactory = interactorFactory;
  }

  private void setKeys() throws Exception {
    if (JWTKeyFactory.getPublicKey() == null || JWTKeyFactory.getPrivateKey() == null) {
      JWTKeyFactory.initKeys();
    }
  }

}
