package service;

import static generated.PaymentResponse.newBuilder;

import generated.PaymentRequest;
import generated.PaymentResponse;
import generated.PaymentServiceGrpc;
import io.grpc.stub.StreamObserver;

public class PaymentService extends PaymentServiceGrpc.PaymentServiceImplBase {

  @Override
  public void processPayment(
      PaymentRequest request,
      StreamObserver<PaymentResponse> responseObserver
  ) {

    PaymentResponse response = newBuilder()
        .setSuccess(true)
        .setTransactionId("abc")
        .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();

  }
}
