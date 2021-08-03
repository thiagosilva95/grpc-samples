package service;

import static generated.PaymentResponse.newBuilder;

import generated.PaymentRequest;
import generated.PaymentResponse;
import generated.PaymentServiceGrpc.PaymentServiceImplBase;
import io.grpc.stub.StreamObserver;

public class PaymentService extends PaymentServiceImplBase {

  @Override
  public void processPayment(PaymentRequest request, StreamObserver<PaymentResponse> responseObserver) {

    try {
      for (int i = 1; i <= 10; i++) {
        String id = "response number: " + i;

        PaymentResponse response = newBuilder()
            .setSuccess(true)
            .setTransactionId(id)
            .build();

        responseObserver.onNext(response);

        Thread.sleep(1000);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      responseObserver.onCompleted();
    }

  }
}
