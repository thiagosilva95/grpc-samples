package client;

import static generated.PaymentRequest.newBuilder;
import static generated.PaymentServiceGrpc.newStub;

import generated.PaymentRequest;
import generated.PaymentRequest.Currency;
import generated.PaymentResponse;
import generated.PaymentServiceGrpc.PaymentServiceStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class PaymentClient {

  public static void main(String[] args) throws InterruptedException {
    System.out.println("Client starting...");

    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();

    PaymentServiceStub paymentService = newStub(channel);

    CountDownLatch latch = new CountDownLatch(1);

    StreamObserver<PaymentRequest> requestObserver = paymentService.processPayment(new StreamObserver<>() {
      @Override
      public void onNext(PaymentResponse value) {
        System.out.println("Received a response from the Server");
        System.out.println(value);
      }

      @Override
      public void onError(Throwable t) {
        System.out.println("Error on Service");
      }

      @Override
      public void onCompleted() {
        System.out.println("Server has completed sending us something");
        latch.countDown();
      }
    });

    System.out.println("Sending Message #1");
    requestObserver.onNext(
        newBuilder()
            .setCardNumber("1234 5678 8765 4321 card 1")
            .setExpiryDate("01/2029")
            .setCvc("123")
            .setAmount(100)
            .setCurrency(Currency.BRL)
            .build()
    );

    System.out.println("Sending Message #2");
    requestObserver.onNext(
        newBuilder()
            .setCardNumber("1234 5678 8765 4321 card 2")
            .setExpiryDate("01/2029")
            .setCvc("123")
            .setAmount(100)
            .setCurrency(Currency.BRL)
            .build()
    );

    System.out.println("Sending Message #3");
    requestObserver.onNext(
        newBuilder()
            .setCardNumber("1234 5678 8765 4321 card 3")
            .setExpiryDate("01/2029")
            .setCvc("123")
            .setAmount(100)
            .setCurrency(Currency.BRL)
            .build()
    );

    requestObserver.onCompleted();

    latch.await(10L, TimeUnit.SECONDS);

    System.out.println("Shutting down channel");
    channel.shutdown();
  }

}
