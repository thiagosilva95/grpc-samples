package service;

import static generated.PaymentResponse.newBuilder;

import generated.PaymentRequest;
import generated.PaymentResponse;
import generated.PaymentServiceGrpc.PaymentServiceImplBase;
import io.grpc.stub.StreamObserver;

public class PaymentService extends PaymentServiceImplBase {

  @Override
  public StreamObserver<PaymentRequest> processPayment(StreamObserver<PaymentResponse> responseObserver) {
    return new StreamObserver<>() {
      StringBuilder result = new StringBuilder();

      @Override
      public void onNext(PaymentRequest value) {
        result.append("Card: " + value.getCardNumber() + "\n");
      }

      @Override
      public void onError(Throwable t) {
        System.out.println("Error on Service");
      }

      @Override
      public void onCompleted() {
        responseObserver.onNext(
            newBuilder()
            .setSuccess(true)
            .setTransactionId(result.toString())
            .build()
        );
        responseObserver.onCompleted();
      }
    };
  }

}
