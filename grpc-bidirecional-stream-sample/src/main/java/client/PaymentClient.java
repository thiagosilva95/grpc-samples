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
import java.util.Arrays;
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

    Arrays.asList("card 1", "card 2", "card 3")
        .forEach( card -> {
              System.out.println("**** Send " + card + " message ***");
              requestObserver.onNext(
                  newBuilder()
                      .setCardNumber(card)
                      .setExpiryDate("01/2029")
                      .setCvc("123")
                      .setAmount(100)
                      .setCurrency(Currency.BRL)
                      .build()
              );

              try {
                Thread.sleep(300);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
        );

    requestObserver.onCompleted();

    latch.await(10L, TimeUnit.SECONDS);

    System.out.println("Shutting down channel");
    channel.shutdown();
  }

}
