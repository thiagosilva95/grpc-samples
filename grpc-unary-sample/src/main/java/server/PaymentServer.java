package server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import java.io.IOException;
import service.PaymentService;

public class PaymentServer {

  public static void main(String[] args) throws IOException, InterruptedException {
    System.out.println("Server started");

    Server server = ServerBuilder.forPort(50051)
        .addService(new PaymentService())
        .addService(ProtoReflectionService.newInstance())
        .build();

    server.start();

    Runtime.getRuntime().addShutdownHook( new Thread(
        () -> {
          System.out.println("Received Shutdown Request");
          server.shutdown();
          System.out.println("Successfully stopped the Greeting Server");
        }
    ));

    server.awaitTermination();
  }

}
