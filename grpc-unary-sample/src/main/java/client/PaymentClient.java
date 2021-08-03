package client;

import static generated.PaymentRequest.newBuilder;
import static generated.PaymentServiceGrpc.newBlockingStub;

import generated.PaymentRequest;
import generated.PaymentRequest.Currency;
import generated.PaymentResponse;
import generated.PaymentServiceGrpc.PaymentServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class PaymentClient {

  public static void main(String[] args) {
    System.out.println("Client starting...");

    ManagedChannel channel = ManagedChannelBuilder
        .forAddress("localhost", 50051)
        .usePlaintext()
        .build();

    PaymentServiceBlockingStub paymentService = newBlockingStub(channel);

    PaymentRequest request = newBuilder()
        .setCardNumber("1234 5678 8765 4321")
        .setExpiryDate("01/2029")
        .setCvc("123")
        .setAmount(100)
        .setCurrency(Currency.BRL)
        .build();

    PaymentResponse result = paymentService.processPayment(request);
    if (result.getSuccess()) {
      // Processa o pedido
    }

    System.out.println(result);

    System.out.println("Shutting down channel");
    channel.shutdown();
  }

}
